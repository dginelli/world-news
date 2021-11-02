package it.unimib.worldnews.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.Set;

import it.unimib.worldnews.R;
import it.unimib.worldnews.utils.Constants;

public class LaunchScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Handle the splash screen transition.
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        setContentView(R.layout.activity_launch_screen);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.launch_screen_fragment_container_view);
        NavController navController = navHostFragment.getNavController();

        if (arePreferencesSet()) {
            navController.navigate(R.id.action_from_launchScreenActivity_to_newsActivity);
        } else {
            navController.navigate(R.id.action_from_launchScreenActivity_to_preferencesActivity);
        }

        finish();
    }

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
