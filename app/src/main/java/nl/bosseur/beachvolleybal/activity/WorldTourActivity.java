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


    }

    private void startFragment(Fragment fragment, Integer id) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(id, fragment);
        if(!isTablet()) {
            fragmentTransaction.addToBackStack(null);
        }else{
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {


            // Create a new Fragment to be placed in the activity layout
            WorldTourListFragment firstFragment = new WorldTourListFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            firstFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            if(isTablet()){
                fragmentTransaction.replace(R.id.fragment_container, firstFragment);
                MatchesFragment fragment = new MatchesFragment();
                if( getBeachVolleyApplication().getTournament() != null ){
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("tournament", getBeachVolleyApplication().getTournament());
                    fragment.setArguments(bundle);
                }
                fragmentTransaction.replace(R.id.fragment_matches, fragment);
            }else{
                if( getBeachVolleyApplication().getTournament() != null ){
                    MatchesFragment fragment = new MatchesFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("tournament", getBeachVolleyApplication().getTournament());
                    fragment.setArguments(bundle);
                    fragmentTransaction.replace(R.id.fragment_container, fragment);
                }else{
                    fragmentTransaction.replace(R.id.fragment_container, firstFragment);
                    fragmentTransaction.addToBackStack(null);
                }


            }

            fragmentTransaction.commit();

        }
    }

    public BeachVolleyApplication getBeachVolleyApplication() {
        return (BeachVolleyApplication)getApplication();
    }

    public void showMatches(BeachTournament tournament) {
        Bundle arguments = new Bundle();
        arguments.putSerializable("tournament", tournament);
        MatchesFragment matchesFragment = new MatchesFragment();
        matchesFragment.setArguments(arguments);

        int idFragment = R.id.fragment_container;
        if(isTablet()){
            idFragment = R.id.fragment_matches;
        }
        startFragment(matchesFragment, idFragment);
    }

    public boolean isTablet() {
        return getResources().getBoolean(R.bool.isTablet);
    }
}
