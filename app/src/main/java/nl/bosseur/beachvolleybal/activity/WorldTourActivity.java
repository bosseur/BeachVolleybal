package nl.bosseur.beachvolleybal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import java.util.List;

import nl.bosseur.beachvolleybal.R;
import nl.bosseur.beachvolleybal.adapter.WorldTourAdapter;
import nl.bosseur.beachvolleybal.model.tournament.BeachTournament;
import nl.bosseur.beachvolleybal.model.util.BeachTournamentXmlParser;
import nl.bosseur.beachvolleybal.tasks.FivbRequestTask;


public class WorldTourActivity extends BeachVolleyBallDelegate {

    public static final String TOURNAMENT = "tournament";
    private List<BeachTournament> events;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_world_tour);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle("World Beach Tour");
        setSupportActionBar(toolbar);

        buscarTournaments();

    }

    private void buscarTournaments(){
        FivbRequestTask task = new FivbRequestTask(this);
        task.execute("Retrieving season tournaments");
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
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");


        return df.format(cal.getTime());
    }

    private String getEndDate(Calendar cal) {
        cal.set(cal.get(Calendar.YEAR), Calendar.DECEMBER, 31);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

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
            buscarTournaments();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void update(String result) {

        try {
            this.events = BeachTournamentXmlParser.unmarschall(result);
            this.events = filter(events);
            WorldTourAdapter adapter = new WorldTourAdapter(this, events);
            ListView listView = (ListView) findViewById(R.id.eventList);
            listView.setAdapter( adapter );
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    BeachTournament tournament = WorldTourActivity.this.events.get(position);
                    Intent intent = new Intent(WorldTourActivity.this, TournamentMatchesActivity.class);
                    intent.putExtra(WorldTourActivity.TOURNAMENT, tournament);
                    startActivity(intent);
                }
            });
        } catch (Exception e) {
            Log.e("ParseEvento", e.getMessage(),e);
            Toast.makeText(this,"Erro ao ler eventos", Toast.LENGTH_LONG).show();
        }


    }

    private List<BeachTournament> filter(List<BeachTournament> events) {
        List<BeachTournament> eventsWorldTour = new ArrayList<BeachTournament>();
        BeachTournament tournament = null;
        for (BeachTournament event: events){

            if( event.getStatus() != 0 &&  (event.getType() == 0 ||
                    event.getType() == 1 ||
                    event.getType() == 4 ||
                    event.getType() == 5 ||
                    event.getType() == 32 ||
                    event.getType() == 33) && event.getEventNumber() != 0 ){
                if(!eventsWorldTour.contains(event)) {
                    eventsWorldTour.add(event);
                }else{
                    if( event.getType() == 0 ) {
                        tournament.setOtherGenderTournamentCode(event.getNumber().toString());
                    }else{
                        tournament.setOtherGenderTournamentCode(event.getNumber().toString());
                    }

                }
                tournament = event;
            }

        }

        return eventsWorldTour;
    }
}
