package nl.bosseur.beachvolleybal.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import nl.bosseur.beachvolleybal.BeachVolleyApplication;
import nl.bosseur.beachvolleybal.ExecutionStateEnum;
import nl.bosseur.beachvolleybal.R;
import nl.bosseur.beachvolleybal.activity.BeachVolleyBallDelegate;
import nl.bosseur.beachvolleybal.activity.TournamentMatchesActivity;
import nl.bosseur.beachvolleybal.activity.WorldTourActivity;
import nl.bosseur.beachvolleybal.adapter.WorldTourAdapter;
import nl.bosseur.beachvolleybal.model.tournament.BeachTournament;
import nl.bosseur.beachvolleybal.model.util.BeachTournamentXmlParser;
import nl.bosseur.beachvolleybal.tasks.FivbRequestTask;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link WorldTourListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorldTourListFragment extends BeachVolleyBallDelegate {

	private ListView eventList;
	private ExecutionStateEnum state;
	private DateFormat df;

	public WorldTourListFragment() {
		state = ExecutionStateEnum.START;
	}


	private String getStartDate(Calendar cal) {
		cal.set(cal.get(Calendar.YEAR), Calendar.JANUARY, 1);
		return df.format(cal.getTime());
	}

	private String getEndDate(Calendar cal) {
		cal.set(cal.get(Calendar.YEAR), Calendar.DECEMBER, 31);
		return df.format(cal.getTime());
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param param1 Parameter 1.
	 * @param param2 Parameter 2.
	 * @return A new instance of fragment WorldTourListFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static WorldTourListFragment newInstance(String param1, String param2) {
		WorldTourListFragment fragment = new WorldTourListFragment();

		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.df = new SimpleDateFormat(getString(R.string.dateformat));

	}

	@Override
	public void onResume() {
		super.onResume();
		state.execute(this);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_world_tour_list, container, false);
		eventList = (ListView) view.findViewById(R.id.eventList);
		return view;
	}

	@Override
	public ExecutionStateEnum getState() {
		return state;
	}

	@Override
	public void setState(ExecutionStateEnum state) {
		this.state = state;
	}

	@Override
	public void showResults() {
		eventList.setVisibility(View.VISIBLE);
		WorldTourActivity activity = (WorldTourActivity) getActivity();
		WorldTourAdapter adapter = new WorldTourAdapter(activity, activity.getBeachVolleyApplication().getEvents());
		ListView listView = (ListView) activity.findViewById(R.id.eventList);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(((parent, view, position, id) -> {
			BeachTournament tournament = WorldTourListFragment.this.getBeachVolleyApplication().getEvents().get(position);
//				Intent intent = new Intent(WorldTourListFragment.this, TournamentMatchesActivity.class);
//				intent.putExtra(WorldTourActivity.TOURNAMENT, tournament);
//				startActivity(intent);
		}));

	}

	private List<BeachTournament> filter(List<BeachTournament> events) {
		Map<Integer, BeachTournament> eventsWorldTour = new TreeMap<>();


		for (BeachTournament event : events) {

			if (event.isWorldTourEvent()) {
				if (!eventsWorldTour.containsKey(event.getEventNumber())) {
					eventsWorldTour.put(event.getEventNumber(), event);
				} else {
					BeachTournament tournament = eventsWorldTour.get(event.getEventNumber());
					if (event.getType() == 0) {
						tournament.setOtherGenderTournamentCode(event.getNumber().toString());
					} else {
						tournament.setOtherGenderTournamentCode(event.getNumber().toString());
					}

				}
			}
		}

		ArrayList<BeachTournament> tournaments = new ArrayList<>(eventsWorldTour.values());
		Collections.sort(tournaments);
		return tournaments;
	}


	@Override
	public void executeTask() {
		eventList.setVisibility(View.GONE);
		this.state = ExecutionStateEnum.RUNNING;
		FivbRequestTask task = new FivbRequestTask(this);
		task.execute(getString(R.string.loading_calendar));
		this.state.execute(this);
	}

	@Override
	public BeachVolleyApplication getBeachVolleyApplication() {
		return (BeachVolleyApplication) getActivity().getApplication();
	}

	@Override
	public String createFivbTourRequest() {
		Calendar inicioAno = Calendar.getInstance();
		Calendar fimAno = Calendar.getInstance();
		return "<Requests><Request Type=\"GetBeachTournamentList\" " +
				"Fields=\"Type CountryCode Code Name Title Gender Type Earnings StartDateMainDraw EndDateMainDraw No NoEvent Status\">" +
				"<Filter FirstDate=\"" + getStartDate(inicioAno) + "\" LastDate=\"" + getEndDate(fimAno) + "\"/>" +
				"</Request></Requests>";
	}

	@Override
	public void update(String result) {
		this.state = ExecutionStateEnum.RECEIVED;
		try {
			getBeachVolleyApplication().getEvents().addAll(filter(BeachTournamentXmlParser.unmarschall(result)));
			this.state.execute(this);
		} catch (Exception e) {
			//Log.e("ParseEvento", e.getMessage(),e);
			Toast.makeText(getActivity(), "Error reading world tour events", Toast.LENGTH_LONG).show();
		}

	}

}


