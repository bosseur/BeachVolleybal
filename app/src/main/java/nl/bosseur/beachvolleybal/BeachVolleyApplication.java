package nl.bosseur.beachvolleybal;

import android.app.Application;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import nl.bosseur.beachvolleybal.model.match.BeachRound;
import nl.bosseur.beachvolleybal.model.tournament.BeachTournament;

/**
 * Created by ekoetsier on 12/09/2016.
 */
public class BeachVolleyApplication extends Application {

    private List<BeachTournament> events = new ArrayList<>();
    private List<List<BeachRound>> beachMatches = new ArrayList<>();

    private List<AsyncTask<?, ?, ?>> tasks = new ArrayList<AsyncTask<?,?,?>>();

    public BeachVolleyApplication(){
        this.tasks = new ArrayList<>();
    }


    public void registerAsyncTask(AsyncTask<?, ?, ?> task){
        tasks.add(task);
    }

    public void unregisterAsyncTask(AsyncTask<?, ?, ?> task){
        if (tasks != null &&
                tasks.contains(task)){

            int index = tasks.indexOf(task);
            AsyncTask asyncTask = tasks.get(index);

            tasks.remove(asyncTask);
        }
    }

    public void killAllAsyncTasks(){

        for (AsyncTask task : this.tasks){
            task.cancel(true);
        }
    }

    public List<BeachTournament> getEvents() {
        return events;
    }

    public List<List<BeachRound>> getBeachMatches() {
        return beachMatches;
    }
}
