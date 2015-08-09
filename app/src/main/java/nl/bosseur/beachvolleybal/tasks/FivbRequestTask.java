package nl.bosseur.beachvolleybal.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

import nl.bosseur.beachvolleybal.R;
import nl.bosseur.beachvolleybal.activity.BeachVolleyBallDelegate;

/**
 * Created by bosseur on 16/06/15.
 */
public class FivbRequestTask extends AsyncTask<String, Object, String> {

    private static String REQUEST_URL;
    private URL url;
    private BeachVolleyBallDelegate beachVolleyBallDelegate;

    private final ProgressDialog progressBar;

    public FivbRequestTask(BeachVolleyBallDelegate beachActivity) {
        REQUEST_URL = beachActivity.getResources().getString(R.string.vis_sdk_url);
        this.beachVolleyBallDelegate = beachActivity;
        progressBar = new ProgressDialog(beachActivity);
    }

    @Override
    protected String doInBackground(final String... params) {
        ((Activity)beachVolleyBallDelegate).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setMessage(params[0]);
                progressBar.setIndeterminate(true);
                progressBar.show();
            }
        });
        String resultado = "";
        HttpURLConnection connection = null;
        try {
            this.url = new URL(REQUEST_URL + "?Request=" + URLEncoder.encode(beachVolleyBallDelegate.createFivbTourRequest(), "UTF-8"));
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestProperty("Accept", "application/xml");
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();

            InputStream is = connection.getInputStream();
            Scanner scanner = new Scanner(is, "UTF-8");
            scanner.useDelimiter("\\A");

            resultado = scanner.next();
            scanner.close();

            connection.disconnect();

        } catch (Exception e) {
            return resultado;
        }finally {
            if (connection != null ){
                connection.disconnect();
            }
        }

        return resultado;
    }

    @Override
    protected void onPostExecute(String s) {
        ((Activity)beachVolleyBallDelegate).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.hide();
            }
        });
        super.onPostExecute(s);
        this.beachVolleyBallDelegate.update(s);
    }
}
