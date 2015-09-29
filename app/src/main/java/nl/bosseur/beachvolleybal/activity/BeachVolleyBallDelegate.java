package nl.bosseur.beachvolleybal.activity;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import nl.bosseur.beachvolleybal.R;
import nl.bosseur.beachvolleybal.tasks.FivbRequestTask;

/**
 * Created by bosseur on 16/06/15.
 *
 */
public abstract class BeachVolleyBallDelegate extends AppCompatActivity{



    public void showProgress(String messageLoaing) {
        LinearLayout linlaHeaderProgress = (LinearLayout) findViewById(R.id.linlaHeaderProgress);
        linlaHeaderProgress.setVisibility(View.VISIBLE);
        TextView tvLoading = (TextView) findViewById(R.id.loading_info);
        tvLoading.setText(messageLoaing);
    }

    public abstract String createFivbTourRequest();

    public abstract void update(String result);

    public void hideProgress() {
        LinearLayout linlaHeaderProgress = (LinearLayout) findViewById(R.id.linlaHeaderProgress);
        linlaHeaderProgress.setVisibility(View.GONE);
    }
}
