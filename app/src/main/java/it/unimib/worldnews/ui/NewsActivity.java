package it.unimib.worldnews.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import it.unimib.worldnews.R;
import it.unimib.worldnews.model.News;

public class NewsActivity extends AppCompatActivity {

    private static final String TAG = "NewsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        // Logic to intercept the Intent and its data
        Intent intent = getIntent();
        int buttonPressedCounter = intent.getIntExtra(PreferencesActivity.EXTRA_BUTTON_PRESSED_COUNTER_KEY, 0);
        News news = intent.getParcelableExtra(PreferencesActivity.EXTRA_NEWS_KEY);

        Log.d(TAG, "onCreate: the button has been pressed " + buttonPressedCounter + " times");
        Log.d(TAG, "onCreate: the news is " + news);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.country_news, R.id.topic_news, R.id.favorites).build();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);

        // Logic to manage the behavior of the BottomNavigationView and Toolbar
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container_view);
        NavController navController = navHostFragment.getNavController();

        // For the Toolbar
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        // For the BottomNavigationView
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }
}
