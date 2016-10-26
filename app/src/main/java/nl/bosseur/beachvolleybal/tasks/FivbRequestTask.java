package nl.bosseur.beachvolleybal.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import nl.bosseur.beachvolleybal.R;
import nl.bosseur.beachvolleybal.activity.BeachVolleyBallDelegate;

/**
 * Created by bosseur on 16/06/15.
 *
 */
public class FivbRequestTask extends AsyncTask<String, Object, String> {

    private static String REQUEST_URL;
    private URL url;
    private BeachVolleyBallDelegate beachVolleyBallDelegate;

    private final ProgressDialog progressBar;

    public FivbRequestTask(BeachVolleyBallDelegate beachActivity) {
        REQUEST_URL = beachActivity.getResources().getString(R.string.vis_sdk_url);
        this.beachVolleyBallDelegate = beachActivity;
        this.beachVolleyBallDelegate.getBeachVolleyApplication().registerAsyncTask(this);
        progressBar = new ProgressDialog(beachActivity);
    }

    @Override
    protected String doInBackground(final String... params) {
        String resultado = "";
        HttpURLConnection connection = null;
        try {
            connection = getHttpURLConnection();

            InputStream is = connection.getInputStream();
            Scanner scanner = new Scanner(is, StandardCharsets.UTF_8.name());
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

    private HttpURLConnection getHttpURLConnection() throws Exception {
        this.url = new URL(REQUEST_URL + "?Request=" + URLEncoder.encode(beachVolleyBallDelegate.createFivbTourRequest(), "UTF-8"));
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestProperty("Accept", "application/xml");
        connection.setRequestMethod("GET");
        connection.setDoInput(true);
        connection.connect();
        return connection;
    }


    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        beachVolleyBallDelegate.getBeachVolleyApplication().unregisterAsyncTask(this);
        this.beachVolleyBallDelegate.update(result);
    }
}
