package it.unimib.worldnews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.google.android.material.snackbar.Snackbar;

import java.util.HashSet;
import java.util.Set;

import it.unimib.worldnews.utils.Constants;

/**
 * Activity that allows the user to choose the country and the topics of interest.
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String BUTTON_PRESSED_COUNTER_KEY = "ButtonPressedCounterKey";
    private static final String NEWS_KEY = "NewsKey";

    private int mButtonPressedCounter;
    private News mNews;

    private Spinner mSpinnerCountries;

    private CheckBox mCheckboxBusiness;
    private CheckBox mCheckboxEntertainment;
    private CheckBox mCheckboxGeneral;
    private CheckBox mCheckboxHealth;
    private CheckBox mCheckboxScience;
    private CheckBox mCheckboxSport;
    private CheckBox mCheckboxTechnology;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_constraint_layout);

        mSpinnerCountries = findViewById(R.id.spinner_countries);

        mCheckboxBusiness = findViewById(R.id.checkbox_business);
        mCheckboxEntertainment = findViewById(R.id.checkbox_entertainment);
        mCheckboxGeneral = findViewById(R.id.checkbox_general);
        mCheckboxHealth = findViewById(R.id.checkbox_health);
        mCheckboxScience = findViewById(R.id.checkbox_science);
        mCheckboxSport = findViewById(R.id.checkbox_sport);
        mCheckboxTechnology = findViewById(R.id.checkbox_technology);

        if (savedInstanceState != null) {
            mButtonPressedCounter = savedInstanceState.getInt(BUTTON_PRESSED_COUNTER_KEY);
            mNews = savedInstanceState.getParcelable(NEWS_KEY);
        } else {
            mButtonPressedCounter = 0;
            mNews = new News("Default title", "Default source");
        }

        Log.d(TAG, "onCreate: The button has been pressed " + mButtonPressedCounter + " times");
        Log.d(TAG, "onCreate: News: " + mNews);

        final Button mButtonNext = findViewById(R.id.button_next);

        setViewsChecked();

        mButtonNext.setOnClickListener(view -> {
            if (isCountryOfInterestSelected() && isTopicOfInterestSelected()) {
                Log.d(TAG, "One country of interest and at least one topic has been chosen");
                mButtonPressedCounter++;

                if (mButtonPressedCounter > 3) {
                    mNews.setTitle("The button has been pressed " + mButtonPressedCounter + " times");
                    mNews.setSource("Corriere della Sera");
                }

                saveInformation();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState:");
        outState.putInt(BUTTON_PRESSED_COUNTER_KEY, mButtonPressedCounter);
        outState.putParcelable(NEWS_KEY, mNews);
    }

    /**
     * It checks if a country of interest has been selected.
     * @return true if a country has been selected, false otherwise.
     */
    private boolean isCountryOfInterestSelected() {

        if (mSpinnerCountries.getSelectedItem() != null) {
            return true;
        }

        Snackbar.make(findViewById(android.R.id.content), R.string.alert_text_country_of_interest, Snackbar.LENGTH_SHORT).show();
        return false;
    }

    /**
     * It checks if at least one topic of interest has been chosen.
     * @return true if at least one topic has been chosen, false otherwise.
     */
    private boolean isTopicOfInterestSelected() {

        if (mCheckboxBusiness.isChecked() || mCheckboxEntertainment.isChecked() || mCheckboxGeneral.isChecked() ||
            mCheckboxHealth.isChecked() || mCheckboxScience.isChecked() || mCheckboxSport.isChecked() || mCheckboxTechnology.isChecked()) {
            return true;
        }

        Snackbar.make(findViewById(R.id.button_next), R.string.alert_text_topic_of_interest, Snackbar.LENGTH_SHORT).show();
        return false;
    }

    /**
     * It stores the country and the topics of interest chosen by the user
     * in the file system throw the use of SharedPreferences API.
     */
    private void saveInformation() {

        String country = mSpinnerCountries.getSelectedItem().toString();
        String countryShortName = null;
        
        if (country.equals(getResources().getString(R.string.france))) {
            countryShortName = Constants.FRANCE;
        } else if (country.equals(getResources().getString(R.string.germany))) {
            countryShortName = Constants.GERMANY;
        } else if (country.equals(getResources().getString(R.string.italy))) {
            countryShortName = Constants.ITALY;
        } else if (country.equals(getResources().getString(R.string.united_kingdom))) {
            countryShortName = Constants.UK;
        } else if (country.equals(getResources().getString(R.string.united_states))) {
            countryShortName = Constants.US;
        }
        
        Set<String> topics = new HashSet<>();

        if (mCheckboxBusiness.isChecked()) {
            topics.add(Constants.BUSINESS);
        }
        if (mCheckboxEntertainment.isChecked()) {
            topics.add(Constants.ENTERTAINMENT);
        }
        if (mCheckboxGeneral.isChecked()) {
            topics.add(Constants.GENERAL);
        }
        if (mCheckboxHealth.isChecked()) {
            topics.add(Constants.HEALTH);
        }
        if (mCheckboxScience.isChecked()) {
            topics.add(Constants.SCIENCE);
        }
        if (mCheckboxSport.isChecked()) {
            topics.add(Constants.SPORTS);
        }
        if (mCheckboxTechnology.isChecked()) {
            topics.add(Constants.TECHNOLOGY);
        }

        SharedPreferences sharedPref = getSharedPreferences(Constants.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constants.SHARED_PREFERENCES_COUNTRY_OF_INTEREST, countryShortName);
        editor.putStringSet(Constants.SHARED_PREFERENCES_TOPIC_OF_INTEREST, topics);
        editor.apply();
    }

    /**
     * It sets the spinner and the checkbox values based on what it has been saved in the SharedPreferences file.
     */
    private void setViewsChecked() {
        SharedPreferences sharedPref = getSharedPreferences(Constants.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);

        String countryOfInterest = sharedPref.getString(Constants.SHARED_PREFERENCES_COUNTRY_OF_INTEREST, null);
        Set<String> topicsOfInterest = sharedPref.getStringSet(Constants.SHARED_PREFERENCES_TOPIC_OF_INTEREST, null);

        if (countryOfInterest != null) {
            switch (countryOfInterest) {
                case Constants.FRANCE:
                    mSpinnerCountries.setSelection(getSpinnerPositionBasedOnValue(getResources().getString(R.string.france)));
                    break;
                case Constants.GERMANY:
                    mSpinnerCountries.setSelection(getSpinnerPositionBasedOnValue(getResources().getString(R.string.germany)));
                    break;
                case Constants.ITALY:
                    mSpinnerCountries.setSelection(getSpinnerPositionBasedOnValue(getResources().getString(R.string.italy)));
                    break;
                case Constants.UK:
                    mSpinnerCountries.setSelection(getSpinnerPositionBasedOnValue(getResources().getString(R.string.united_kingdom)));
                    break;
                case Constants.US:
                    mSpinnerCountries.setSelection(getSpinnerPositionBasedOnValue(getResources().getString(R.string.united_states)));
                    break;
            }
        }

        if (topicsOfInterest != null) {
            if (topicsOfInterest.contains(Constants.BUSINESS)) {
                mCheckboxBusiness.setChecked(true);
            }
            if (topicsOfInterest.contains(Constants.ENTERTAINMENT)) {
                mCheckboxEntertainment.setChecked(true);
            }
            if (topicsOfInterest.contains(Constants.GENERAL)) {
                mCheckboxGeneral.setChecked(true);
            }
            if (topicsOfInterest.contains(Constants.HEALTH)) {
                mCheckboxHealth.setChecked(true);
            }
            if (topicsOfInterest.contains(Constants.SCIENCE)) {
                mCheckboxScience.setChecked(true);
            }
            if (topicsOfInterest.contains(Constants.SPORTS)) {
                mCheckboxSport.setChecked(true);
            }
            if (topicsOfInterest.contains(Constants.TECHNOLOGY)) {
                mCheckboxTechnology.setChecked(true);
            }
        }
    }

    /**
     * It allows to get the position of a given country in the string array
     * used to fill the spinner.
     * @param value the country to be checked in the string array.
     * @return the position of the country in the string array.
     */
    private int getSpinnerPositionBasedOnValue(String value) {

        String[] countries = getResources().getStringArray(R.array.country_array);

        for (int i = 0; i < countries.length; i++) {
            if (countries[i].equals(value)) {
                return i;
            }
        }

        return 0;
    }
}
