package nl.bosseur.beachvolleybal.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import nl.bosseur.beachvolleybal.BeachVolleyApplication;
import nl.bosseur.beachvolleybal.ExecutionStateEnum;
import nl.bosseur.beachvolleybal.R;

/**
 * Created by bosseur on 16/06/15.
 *
 */
public abstract class BeachVolleyBallDelegate extends Fragment{
    public static final String CURRENT_APP_STATE = "CURRENT_APP_STATE";

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(CURRENT_APP_STATE, getState());
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
       super.onViewStateRestored(savedInstanceState);
        if( savedInstanceState != null ){
            setState((ExecutionStateEnum) savedInstanceState.getSerializable(CURRENT_APP_STATE));
        }
    }

    public void showProgress(String messageLoading) {
        FragmentActivity activity = getActivity();
        LinearLayout linlaHeaderProgress = (LinearLayout) activity.findViewById(R.id.lineHeaderProgress);
        linlaHeaderProgress.setVisibility(View.VISIBLE);
        TextView tvLoading = (TextView) activity.findViewById(R.id.loading_info);
        tvLoading.setText(messageLoading);
    }

    public BeachVolleyApplication getBeachVolleyApplication() {
        return (BeachVolleyApplication) getActivity().getApplication();
    }

    public abstract ExecutionStateEnum getState();

    public abstract void setState(ExecutionStateEnum state);

    public abstract void showResults();

    public abstract void executeTask();

    public abstract String createFivbTourRequest();

    public abstract void update(String result);

    public void hideProgress() {
        LinearLayout lineHeaderProgress = (LinearLayout) getActivity().findViewById(R.id.lineHeaderProgress);
        lineHeaderProgress.setVisibility(View.GONE);
    }
}
