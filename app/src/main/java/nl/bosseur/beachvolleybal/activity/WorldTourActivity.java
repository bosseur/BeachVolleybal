package nl.bosseur.beachvolleybal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import nl.bosseur.beachvolleybal.BeachVolleyApplication;
import nl.bosseur.beachvolleybal.ExecutionStateEnum;
import nl.bosseur.beachvolleybal.R;
import nl.bosseur.beachvolleybal.adapter.WorldTourAdapter;
import nl.bosseur.beachvolleybal.model.tournament.BeachTournament;
import nl.bosseur.beachvolleybal.model.util.BeachTournamentXmlParser;
import nl.bosseur.beachvolleybal.tasks.FivbRequestTask;


public class WorldTourActivity extends BeachVolleyBallDelegate {

    public static final String TOURNAMENT = "tournament";
    private ListView eventList;
    private DateFormat df;
    private ExecutionStateEnum state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.df = new SimpleDateFormat(getString(R.string.dateformat));
        setContentView(R.layout.activity_world_tour);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle("World Beach Tour");
        setSupportActionBar(toolbar);
        eventList = (ListView) findViewById(R.id.eventList);
        state = ExecutionStateEnum.START;
    }

    @Override
    protected void onResume() {
        super.onResume();
        state.execute(this);
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
        return (BeachVolleyApplication)getApplication();
    }

    public ExecutionStateEnum getState() {
        return state;
    }

    public void setState(ExecutionStateEnum state) {
        this.state = state;
    }

    @Override
    public String createFivbTourRequest() {
        Calendar inicioAno = Calendar.getInstance();
        Calendar fimAno = Calendar.getInstance();
        return "<Requests><Request Type=\"GetBeachTournamentList\" " +
        "Fields=\"Type CountryCode Code Name Title Gender Type Earnings StartDateMainDraw EndDateMainDraw No NoEvent Status\">"+
        "<Filter FirstDate=\"" + getStartDate(inicioAno) + "\" LastDate=\"" + getEndDate(fimAno) + "\"/>"+
        "</Request></Requests>";
    }

    private String getStartDate(Calendar cal) {
        cal.set(cal.get(Calendar.YEAR), Calendar.JANUARY, 1);
        return df.format(cal.getTime());
    }

    private String getEndDate(Calendar cal) {
        cal.set(cal.get(Calendar.YEAR), Calendar.DECEMBER, 31);
        return df.format(cal.getTime());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_world_tour, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.refresh) {
            executeTask();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void update(String result) {
        this.state = ExecutionStateEnum.RECEIVED;
        try {
            getBeachVolleyApplication().getEvents().addAll(filter(BeachTournamentXmlParser.unmarschall(result)));
            showResults();
        } catch (Exception e) {
            //Log.e("ParseEvento", e.getMessage(),e);
            Toast.makeText(this,"Error reading world tour events", Toast.LENGTH_LONG).show();
        }


    }

    public void showResults() {
        eventList.setVisibility(View.VISIBLE);
        WorldTourAdapter adapter = new WorldTourAdapter(this, getBeachVolleyApplication().getEvents());
        ListView listView = (ListView) findViewById(R.id.eventList);
        listView.setAdapter( adapter );
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				BeachTournament tournament = WorldTourActivity.this.getBeachVolleyApplication().getEvents().get(position);
				Intent intent = new Intent(WorldTourActivity.this, TournamentMatchesActivity.class);
				intent.putExtra(WorldTourActivity.TOURNAMENT, tournament);
				startActivity(intent);
			}
		});
    }

    private List<BeachTournament> filter(List<BeachTournament> events) {
        Map<Integer, BeachTournament> eventsWorldTour = new TreeMap<>();

        for (BeachTournament event: events){

            if( event.getStatus() != 0 &&  (event.getType() == 0 ||
                    event.getType() == 1 ||
                    event.getType() == 4 ||
                    event.getType() == 5 ||
                    event.getType() == 32 ||
                    event.getType() == 33) && event.getEventNumber() != 0 ){
                if(!eventsWorldTour.containsKey(event.getEventNumber())) {
                    eventsWorldTour.put(event.getEventNumber(), event);
                }else{
                    BeachTournament tournament = eventsWorldTour.get(event.getEventNumber());
                    if( event.getType() == 0 ) {
                        tournament.setOtherGenderTournamentCode(event.getNumber().toString());
                    }else{
                        tournament.setOtherGenderTournamentCode(event.getNumber().toString());
                    }

                }
            }
        }

        ArrayList<BeachTournament> tournaments = new ArrayList<>(eventsWorldTour.values());
        Collections.sort(tournaments);
        return tournaments;
    }
}
