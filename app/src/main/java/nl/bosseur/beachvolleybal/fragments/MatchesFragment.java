package nl.bosseur.beachvolleybal.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import nl.bosseur.beachvolleybal.ExecutionStateEnum;
import nl.bosseur.beachvolleybal.R;
import nl.bosseur.beachvolleybal.model.match.BeachRound;
import nl.bosseur.beachvolleybal.model.match.TournamentMatch;
import nl.bosseur.beachvolleybal.model.tournament.BeachTournament;
import nl.bosseur.beachvolleybal.model.util.BeachMatchesXmlParser;
import nl.bosseur.beachvolleybal.tasks.FivbRequestTask;
import nl.bosseur.beachvolleybal.view.SlidingTabLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class MatchesFragment extends BeachVolleyBallDelegate {

	private BeachTournament tournament;
	private int phase = 4;
	private CharSequence[] titles;

	private SlidingTabLayout mSlidingTabLayout;

	private ExecutionStateEnum state;
	/**
	 * A {@link ViewPager} which will be used in conjunction with the {@link SlidingTabLayout} above.
	 */
	private ViewPager mViewPager;

	public MatchesFragment() {
		this.state = ExecutionStateEnum.START;
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View matchesView = inflater.inflate(R.layout.activity_tournament_matches, container, false);

		if( getArguments() != null){
			tournament = (BeachTournament) getArguments().getSerializable("tournament");
			if( tournament.getFemaleTournamentCode() != null && tournament.getMaleTournamentCode() != null ){
				titles = new CharSequence[]{getString(R.string.male), getString(R.string.female)};
			}else if( tournament.getFemaleTournamentCode() != null){
				titles = new CharSequence[]{getString(R.string.female)};
			}else{
				titles = new CharSequence[]{getString(R.string.male)};
			}
		}

		Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.tool_bar);
		toolbar.setTitle(tournament.getTile());

		this.state = ExecutionStateEnum.START;

		return  matchesView;
	}



	public ExecutionStateEnum getState() {
		return state;
	}

	public void setState(ExecutionStateEnum state) {
		this.state = state;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_world_tour, menu);
		MenuItem menuMainDraw = menu.add( getString(R.string.main_draw));
		menuMainDraw.setOnMenuItemClickListener(item ->{
			MatchesFragment.this.phase = 4;
			showResults();
			return true;
		});
		MenuItem menuQuali = menu.add( getString(R.string.qualification));
		menuQuali.setOnMenuItemClickListener(item -> {
			MatchesFragment.this.phase = 3;
			showResults();
			return true;

		});
	}

	@Override
	public void onResume() {
		super.onResume();
		this.state.execute(this);
	}

	@Override
	public void executeTask() {
		getBeachVolleyApplication().getBeachMatches().clear();
        showProgress(getString(R.string.retrieving_matches) + tournament.getTile());
		this.state = ExecutionStateEnum.RUNNING;
        FivbRequestTask task = new FivbRequestTask(this);
        task.execute(getString(R.string.retrieving_matches) + tournament.getTile());
        this.state.execute(this);
	}

	@Override
	public String createFivbTourRequest() {
		String requestRounds ;
		String requestMatches ;

		if( tournament.getFemaleTournamentCode() != null && tournament.getMaleTournamentCode() != null ){
			requestRounds = createRequestRound(tournament.getMaleTournamentCode());
			requestRounds += createRequestRound(tournament.getFemaleTournamentCode());
			requestMatches = createRequestMatches(tournament.getMaleTournamentCode());
			requestMatches += createRequestMatches(tournament.getFemaleTournamentCode());
		}else{
			requestRounds   = createRequestRound(tournament.getNumber().toString());
			requestMatches  = createRequestMatches( tournament.getNumber().toString() );
		}


		return "<Requests>" + requestRounds + requestMatches + "</Requests>";
	}

	private String createRequestMatches(String numberTournament) {
		return "<Request Type=\"GetBeachMatchList\" " +
				"Fields=\"NoTournament NoInTournament NoTeamA NoTeamB TeamAFederationCode TeamBFederationCode NoRound LocalDate LocalTime TeamAName TeamBName Court PointsTeamASet1 PointsTeamBSet1 PointsTeamASet2 PointsTeamBSet2 PointsTeamASet3 PointsTeamBSet3 DurationSet1 DurationSet2 DurationSet3 ResultType TeamAPositionInMainDraw TeamBPositionInMainDraw\">"+
				"<Filter NoTournament=\"" + numberTournament + "\" />"+
				"</Request>";
	}

	private String createRequestRound(String numberTournament) {
		return "<Request Type=\"GetBeachRoundList\" " +
				"Fields=\"NoTournament Code Name Bracket Phase StartDate EndDate No \">"+
				"<Filter NoTournament=\"" + numberTournament + "\"/>"+
				"</Request>";
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.refresh) {
			ExecutionStateEnum.START.execute(this);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}


	//    @Override
	public void update(String result) {
		this.state = ExecutionStateEnum.RECEIVED;
		try {
			getBeachVolleyApplication().getBeachMatches().clear();
			getBeachVolleyApplication().getBeachMatches().addAll( BeachMatchesXmlParser.unmarschall(result) );
		}catch (Exception ex){
			//Log.e("Error no parse", ex.getMessage(), ex);
			Toast.makeText(getActivity(),"Error parsing match data", Toast.LENGTH_SHORT).show();
			return;
		}
		this.phase = 4;
		if( getBeachVolleyApplication().getBeachMatches().isEmpty() ){
			Toast.makeText(getActivity(),"Information not yet available", Toast.LENGTH_SHORT).show();
			return;
		}
        this.state.execute(this);
	}

	public void showResults() {
		List<List<BeachRound>> rounds = new ArrayList<>();
		for (List<BeachRound> round : getBeachVolleyApplication().getBeachMatches()) {
			rounds.add(filter(round));
		}

		// Get the ViewPager and set it's PagerAdapter so that it can display items
		mViewPager = (ViewPager) getActivity().findViewById(R.id.pager);

		mViewPager.setAdapter(new MatchesFragment.MatchesPagerAdapter(titles, rounds));
		mViewPager.getAdapter().notifyDataSetChanged();
		// Give the SlidingTabLayout the ViewPager, this must be done AFTER the ViewPager has had
		// it's PagerAdapter set.
		mSlidingTabLayout = (SlidingTabLayout) getActivity().findViewById(R.id.tabs);
		mSlidingTabLayout.setDistributeEvenly(true);
		mSlidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
			@Override
			public int getIndicatorColor(int position) {
				return getResources().getColor(R.color.tabsScrollColor);
			}

			@Override
			public int getDividerColor(int position) {
				return getResources().getColor(R.color.tabsScrollColor);
			}
		});
		mSlidingTabLayout.setViewPager(mViewPager);
	}

	private List<BeachRound> filter(List<BeachRound> rounds) {
		List<BeachRound> roundsSelectedPhase = new ArrayList<>();
		for (BeachRound round: rounds){
			if( round.getPhase() == this.phase ){
				roundsSelectedPhase.add( round );
			}
		}
		return roundsSelectedPhase;
	}
	/**
	 * The {@link android.support.v4.view.PagerAdapter} used to display pages in this sample.
	 * The individual pages are simple and just display two lines of text. The important section of
	 * this class is the {@link #getPageTitle(int)} method which controls what is displayed in the
	 * {@link nl.bosseur.beachvolleybal.view.SlidingTabLayout}.
	 */
	class MatchesPagerAdapter extends PagerAdapter {

		private CharSequence[] titles;
		private List<List<BeachRound>> beachMatches;

		public MatchesPagerAdapter(CharSequence[] titles, List<List<BeachRound>> beachMatches) {
			this.titles = titles;
			this.beachMatches = beachMatches;
		}

		/**
		 * @return the number of pages to display
		 */
		@Override
		public int getCount() {
			return titles.length;
		}

		/**
		 * @return true if the value returned from {@link #instantiateItem(android.view.ViewGroup, int)} is the
		 * same object as the {@link View} added to the {@link android.support.v4.view.ViewPager}.
		 */
		@Override
		public boolean isViewFromObject(View view, Object o) {
			return o == view;
		}

		/**
		 * Return the title of the item at {@code position}. This is important as what this method
		 * returns is what is displayed in the {@link nl.bosseur.beachvolleybal.view.SlidingTabLayout}.
		 * <p>
		 * Here we construct one using the position value, but for real application the title should
		 * refer to the item's contents.
		 */
		@Override
		public CharSequence getPageTitle(int position) {
			return titles[position];
		}

		/**
		 * Instantiate the {@link View} which should be displayed at {@code position}. Here we
		 * inflate a layout from the apps resources and then change the text view to signify the position.
		 */
		@Override
		public Object instantiateItem(ViewGroup container, int position) {

			// Inflate a new layout from our resources
			View view = MatchesFragment.this.getActivity().getLayoutInflater().inflate(R.layout.groups, container, false);
			// Add the newly created View to the ViewPager
			container.addView(view);

			List<BeachRound> rounds = this.beachMatches.get(position);

			LinearLayout layout = (LinearLayout) view.findViewById(R.id.match_root);
			for (BeachRound round : rounds){

				LayoutInflater layoutInflater = getActivity().getLayoutInflater();
				View viewMatches = layoutInflater.inflate(R.layout.group_matches, null);

				TextView group = (TextView) viewMatches.findViewById(R.id.group_name);
				group.setText(round.getName());

				LinearLayout lista = (LinearLayout) viewMatches.findViewById(R.id.match_list);
				for (TournamentMatch match: round.getMatches()){
					View matchView = layoutInflater.inflate(R.layout.match, null);


					TextView matchNumber = (TextView) matchView.findViewById(R.id.matchNumber);
					matchNumber.setText( Integer.valueOf(match.getMatchNumber()).toString() );

					TextView teamA = (TextView) matchView.findViewById(R.id.teamA);
					teamA.setText(match.getTeamACode() );

					TextView teamB = (TextView) matchView.findViewById(R.id.teamB);
					teamB.setText(match.getTeamBCode());

					TextView pointsASet1 = (TextView) matchView.findViewById(R.id.pointsSet1TeamA);
					pointsASet1.setText( match.getPointsTeamASet1() );

					TextView pointsASet2 = (TextView) matchView.findViewById(R.id.pointsSet2TeamA);
					pointsASet2.setText( match.getPointsTeamASet2() );

					TextView pointsASet3 = (TextView) matchView.findViewById(R.id.pointsSet3TeamA);
					pointsASet3.setText( match.getPointsTeamASet3() );

					TextView pointsBSet1 = (TextView) matchView.findViewById(R.id.pointsSet1TeamB);
					pointsBSet1.setText( match.getPointsTeamBSet1() );

					TextView pointsBSet2 = (TextView) matchView.findViewById(R.id.pointsSet2TeamB);
					pointsBSet2.setText( match.getPointsTeamBSet2() );

					TextView pointsBSet3 = (TextView) matchView.findViewById(R.id.pointsSet3TeamB);
					pointsBSet3.setText( match.getPointsTeamBSet3() );

					lista.addView( matchView );
				}

				layout.addView(viewMatches);
			}

			// Return the View
			return view;
		}

		/**
		 * Destroy the item from the {@link android.support.v4.view.ViewPager}. In our case this is simply removing the
		 * {@link View}.
		 */
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}
}
