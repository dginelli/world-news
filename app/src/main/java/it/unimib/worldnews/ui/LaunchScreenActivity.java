package it.unimib.worldnews.ui;

import androidx.appcompat.app.AppCompatActivity;
// import androidx.core.splashscreen.SplashScreen;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.Set;

import it.unimib.worldnews.R;
import it.unimib.worldnews.utils.Constants;

/**
 * Launch (Splash) Screen Activity to manage the startup of
 * the application and choose which Activity to start.
 */
public class LaunchScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
         * Handle the splash screen transition with SplashScreen API
         * (see https://developer.android.com/guide/topics/ui/splash-screen)
         * SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        */

        setContentView(R.layout.activity_launch_screen);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.launch_screen_fragment_container_view);
        NavController navController = navHostFragment.getNavController();

        // Start another Activity with Navigation Controller
        if (arePreferencesSet()) {
            navController.navigate(R.id.action_from_launchScreenActivity_to_newsActivity);
        } else {
            navController.navigate(R.id.action_from_launchScreenActivity_to_preferencesActivity);
        }

        /* The equivalent way with explicit Intent
        if (arePreferencesSet()) {
            startActivity(new Intent(this, NewsActivity.class));
        } else {
            startActivity(new Intent(this, PreferencesActivity.class));
        }*/

        finish();
    }

    /**
     * It checks if the preferences have already been set by the user.
     *
     * @return true if the preferences have been set, false otherwise.
     */
    private boolean arePreferencesSet() {
        SharedPreferences sharedPref = getSharedPreferences(Constants.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);

        String countryOfInterest = sharedPref.getString(Constants.SHARED_PREFERENCES_COUNTRY_OF_INTEREST, null);
        Set<String> topicsOfInterest = sharedPref.getStringSet(Constants.SHARED_PREFERENCES_TOPICS_OF_INTEREST, null);

        if (countryOfInterest != null && topicsOfInterest != null) {
            return true;
        }

        return false;
    }
}
