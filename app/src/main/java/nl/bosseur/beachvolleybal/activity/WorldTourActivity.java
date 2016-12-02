package nl.bosseur.beachvolleybal.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import nl.bosseur.beachvolleybal.BeachVolleyApplication;
import nl.bosseur.beachvolleybal.R;
import nl.bosseur.beachvolleybal.fragments.MatchesFragment;
import nl.bosseur.beachvolleybal.fragments.WorldTourListFragment;
import nl.bosseur.beachvolleybal.model.tournament.BeachTournament;


public class WorldTourActivity extends AppCompatActivity {

    public static final String TOURNAMENT = "tournament";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_world_tour);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle("World Beach Tour");
        setSupportActionBar(toolbar);

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            WorldTourListFragment firstFragment = new WorldTourListFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            firstFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            if(isTablet()){
                startFragment(firstFragment, R.id.fragment_container);
                startFragment(new MatchesFragment(), R.id.fragment_matches);
            }else {
                startFragment(firstFragment, R.id.fragment_container);
            }
        }

    }

    private void startFragment(Fragment fragment, Integer id) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction
				.replace(id, fragment);
        if(!isTablet()) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public BeachVolleyApplication getBeachVolleyApplication() {
        return (BeachVolleyApplication)getApplication();
    }

    public void showMatches(BeachTournament tournament) {
        Bundle arguments = new Bundle();
        arguments.putSerializable("tournament", tournament);
        MatchesFragment matchesFragment = new MatchesFragment();
        matchesFragment.setArguments(arguments);

        if(isTablet()){
            startFragment(matchesFragment, R.id.fragment_matches);
        }else {
            startFragment(matchesFragment, R.id.fragment_container);
        }
    }

    public boolean isTablet() {
        return getResources().getBoolean(R.bool.isTablet);
    }
}
