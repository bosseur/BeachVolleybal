package nl.bosseur.beachvolleybal.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import nl.bosseur.beachvolleybal.BeachVolleyApplication;
import nl.bosseur.beachvolleybal.ExecutionStateEnum;
import nl.bosseur.beachvolleybal.R;
import nl.bosseur.beachvolleybal.tasks.FivbRequestTask;

/**
 * Created by bosseur on 16/06/15.
 *
 */
public abstract class BeachVolleyBallDelegate extends AppCompatActivity{
    public static final String CURRENT_APP_STATE = "CURRENT_APP_STATE";

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(CURRENT_APP_STATE, getState());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
       setState((ExecutionStateEnum)savedInstanceState.getSerializable(CURRENT_APP_STATE));
    }

    public void showProgress(String messageLoading) {
        LinearLayout linlaHeaderProgress = (LinearLayout) findViewById(R.id.linlaHeaderProgress);
        linlaHeaderProgress.setVisibility(View.VISIBLE);
        TextView tvLoading = (TextView) findViewById(R.id.loading_info);
        tvLoading.setText(messageLoading);
    }

    public abstract ExecutionStateEnum getState();

    public abstract void setState(ExecutionStateEnum state);

    public abstract void showResults();

    public abstract void executeTask();

    public abstract BeachVolleyApplication getBeachVolleyApplication();

    public abstract String createFivbTourRequest();

    public abstract void update(String result);

    public void hideProgress() {
        LinearLayout linlaHeaderProgress = (LinearLayout) findViewById(R.id.linlaHeaderProgress);
        linlaHeaderProgress.setVisibility(View.GONE);
    }
}
