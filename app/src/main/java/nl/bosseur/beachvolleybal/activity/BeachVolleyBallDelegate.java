package nl.bosseur.beachvolleybal.activity;

import android.support.v7.app.ActionBarActivity;

/**
 * Created by bosseur on 16/06/15.
 */
public abstract class BeachVolleyBallDelegate extends ActionBarActivity{

    public abstract String createFivbTourRequest();

    public abstract void update(String result);

}
