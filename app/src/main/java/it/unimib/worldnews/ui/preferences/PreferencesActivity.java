package it.unimib.worldnews.ui.preferences;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import it.unimib.worldnews.R;
import it.unimib.worldnews.model.News;
import it.unimib.worldnews.model.NewsSource;
import it.unimib.worldnews.model.User;
import it.unimib.worldnews.model.UserPreference;
import it.unimib.worldnews.ui.NewsActivity;
import it.unimib.worldnews.ui.authentication.UserViewModel;
import it.unimib.worldnews.utils.Constants;

/**
 * Activity that allows the user to choose the country and the topics of interest.
 */
public class PreferencesActivity extends AppCompatActivity {

    private static final String TAG = "PreferencesActivity";
    private static final String BUTTON_PRESSED_COUNTER_KEY = "ButtonPressedCounterKey";
    private static final String NEWS_KEY = "NewsKey";

    public static final String EXTRA_BUTTON_PRESSED_COUNTER_KEY = "it.unimib.worldnews.BUTTON_PRESSED_COUNTER_KEY";
    public static final String EXTRA_NEWS_KEY = "it.unimib.worldnews.NEWS_KEY";

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

    private PreferencesViewModel mPreferencesViewModel;
    private FirebaseAuth mAuth;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences_constraint_layout);

        mPreferencesViewModel = new ViewModelProvider(this).get(PreferencesViewModel.class);
        mAuth = FirebaseAuth.getInstance();

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
            mNews = new News(null, null, "Default title",
                    null, null, null, null, null);
        }

        Log.d(TAG, "onCreate: The button has been pressed " + mButtonPressedCounter + " times");
        Log.d(TAG, "onCreate: News: " + mNews);

        final Button mButtonNext = findViewById(R.id.button_next);

        //setViewsChecked(); // It retrieves the user preferences from local

        mPreferencesViewModel.saveUserPreferences(mUser).observe(this, response -> {
            if (response) {
                Log.d(TAG, "Preferences saved on Firebase Realtime Database");
            } else {
                Log.d(TAG, "Preferences not saved on Firebase Realtime Database");
            }
        });

        mButtonNext.setOnClickListener(view -> {
            mButtonPressedCounter++;
            if (isCountryOfInterestSelected() && isTopicOfInterestSelected()) {
                Log.d(TAG, "One country of interest and at least one topic has been chosen");

                if (mButtonPressedCounter > 3) {
                    mNews.setTitle("The button has been pressed " + mButtonPressedCounter + " times");
                    mNews.setNewsSource(new NewsSource("Corriere della Sera"));
                }

                saveInformation();

                // It creates a new explicit Intent to start and pass the data to NewsActivity
                Intent intent = new Intent(this, NewsActivity.class);
                intent.putExtra(EXTRA_BUTTON_PRESSED_COUNTER_KEY, mButtonPressedCounter);
                intent.putExtra(EXTRA_NEWS_KEY, mNews);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            mPreferencesViewModel.readUserInformation(currentUser.getUid()).observe(this, user -> {
                if (user != null) {
                    setViewsCheckedWithFirebase(user.getUserPreference().getFavoriteCountry(),
                            new HashSet<>(user.getUserPreference().getFavoriteTopics()));
                }
            });
        }
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
     * in the file system through the use of SharedPreferences API.
     */
    private void saveInformation() {

        String country = mSpinnerCountries.getSelectedItem().toString();
        String countryShortName = null;

        if (country.equals(getResources().getString(R.string.argentina))) {
            countryShortName = Constants.ARGENTINA;
        } else if (country.equals(getResources().getString(R.string.australia))) {
            countryShortName = Constants.AUSTRALIA;
        } else if (country.equals(getResources().getString(R.string.austria))) {
            countryShortName = Constants.AUSTRIA;
        } else if (country.equals(getResources().getString(R.string.belgium))) {
            countryShortName = Constants.BELGIUM;
        } else if (country.equals(getResources().getString(R.string.brazil))) {
            countryShortName = Constants.BRAZIL;
        } else if (country.equals(getResources().getString(R.string.bulgaria))) {
            countryShortName = Constants.BULGARIA;
        } else if (country.equals(getResources().getString(R.string.canada))) {
            countryShortName = Constants.CANADA;
        } else if (country.equals(getResources().getString(R.string.china))) {
            countryShortName = Constants.CHINA;
        } else if (country.equals(getResources().getString(R.string.colombia))) {
            countryShortName = Constants.COLOMBIA;
        } else if (country.equals(getResources().getString(R.string.cuba))) {
            countryShortName = Constants.CUBA;
        } else if (country.equals(getResources().getString(R.string.czech_republic))) {
            countryShortName = Constants.CZECH_REPUBLIC;
        } else if (country.equals(getResources().getString(R.string.egypt))) {
            countryShortName = Constants.EGYPT;
        } else if (country.equals(getResources().getString(R.string.france))) {
            countryShortName = Constants.FRANCE;
        } else if (country.equals(getResources().getString(R.string.germany))) {
            countryShortName = Constants.GERMANY;
        } else if (country.equals(getResources().getString(R.string.greece))) {
            countryShortName = Constants.GREECE;
        } else if (country.equals(getResources().getString(R.string.hong_kong))) {
            countryShortName = Constants.HONG_KONG;
        } else if (country.equals(getResources().getString(R.string.hungary))) {
            countryShortName = Constants.HUNGARY;
        } else if (country.equals(getResources().getString(R.string.india))) {
            countryShortName = Constants.INDIA;
        } else if (country.equals(getResources().getString(R.string.indonesia))) {
            countryShortName = Constants.INDONESIA;
        } else if (country.equals(getResources().getString(R.string.ireland))) {
            countryShortName = Constants.IRELAND;
        } else if (country.equals(getResources().getString(R.string.israel))) {
            countryShortName = Constants.ISRAEL;
        } else if (country.equals(getResources().getString(R.string.italy))) {
            countryShortName = Constants.ITALY;
        } else if (country.equals(getResources().getString(R.string.japan))) {
            countryShortName = Constants.JAPAN;
        } else if (country.equals(getResources().getString(R.string.latvia))) {
            countryShortName = Constants.LATVIA;
        } else if (country.equals(getResources().getString(R.string.lithuania))) {
            countryShortName = Constants.LITHUANIA;
        } else if (country.equals(getResources().getString(R.string.malaysia))) {
            countryShortName = Constants.MALAYSIA;
        } else if (country.equals(getResources().getString(R.string.mexico))) {
            countryShortName = Constants.MEXICO;
        } else if (country.equals(getResources().getString(R.string.morocco))) {
            countryShortName = Constants.MOROCCO;
        } else if (country.equals(getResources().getString(R.string.netherlands))) {
            countryShortName = Constants.NETHERLANDS;
        } else if (country.equals(getResources().getString(R.string.new_zealand))) {
            countryShortName = Constants.NEW_ZEALAND;
        } else if (country.equals(getResources().getString(R.string.nigeria))) {
            countryShortName = Constants.NIGERIA;
        } else if (country.equals(getResources().getString(R.string.norway))) {
            countryShortName = Constants.NORWAY;
        } else if (country.equals(getResources().getString(R.string.philippines))) {
            countryShortName = Constants.PHILIPPINES;
        } else if (country.equals(getResources().getString(R.string.poland))) {
            countryShortName = Constants.POLAND;
        } else if (country.equals(getResources().getString(R.string.portugal))) {
            countryShortName = Constants.PORTUGAL;
        } else if (country.equals(getResources().getString(R.string.romania))) {
            countryShortName = Constants.ROMANIA;
        } else if (country.equals(getResources().getString(R.string.russia))) {
            countryShortName = Constants.RUSSIA;
        } else if (country.equals(getResources().getString(R.string.saudi_arabia))) {
            countryShortName = Constants.SAUDI_ARABIA;
        } else if (country.equals(getResources().getString(R.string.serbia))) {
            countryShortName = Constants.SERBIA;
        } else if (country.equals(getResources().getString(R.string.singapore))) {
            countryShortName = Constants.SINGAPORE;
        } else if (country.equals(getResources().getString(R.string.slovakia))) {
            countryShortName = Constants.SLOVAKIA;
        } else if (country.equals(getResources().getString(R.string.slovenia))) {
            countryShortName = Constants.SLOVENIA;
        } else if (country.equals(getResources().getString(R.string.south_africa))) {
            countryShortName = Constants.SOUTH_AFRICA;
        } else if (country.equals(getResources().getString(R.string.south_korea))) {
            countryShortName = Constants.SOUTH_KOREA;
        } else if (country.equals(getResources().getString(R.string.sweden))) {
            countryShortName = Constants.SWEDEN;
        } else if (country.equals(getResources().getString(R.string.switzerland))) {
            countryShortName = Constants.SWITZERLAND;
        } else if (country.equals(getResources().getString(R.string.taiwan))) {
            countryShortName = Constants.TAIWAN;
        } else if (country.equals(getResources().getString(R.string.thailand))) {
            countryShortName = Constants.THAILAND;
        } else if (country.equals(getResources().getString(R.string.turkey))) {
            countryShortName = Constants.TURKEY;
        } else if (country.equals(getResources().getString(R.string.united_arab_emirates))) {
            countryShortName = Constants.UNITED_ARAB_EMIRATES;
        } else if (country.equals(getResources().getString(R.string.ukraine))) {
            countryShortName = Constants.UKRAINE;
        } else if (country.equals(getResources().getString(R.string.united_kingdom))) {
            countryShortName = Constants.UNITED_KINGDOM;
        } else if (country.equals(getResources().getString(R.string.united_states))) {
            countryShortName = Constants.UNITED_STATES;
        } else if (country.equals(getResources().getString(R.string.venezuela))) {
            countryShortName = Constants.VENEZUELA;
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
        editor.putStringSet(Constants.SHARED_PREFERENCES_TOPICS_OF_INTEREST, topics);
        editor.apply();

        mUser = new User();
        mUser.setUId(mAuth.getUid());
        mUser.setEmail(mAuth.getCurrentUser().getEmail());

        UserPreference userPreference = new UserPreference();
        userPreference.setFavoriteCountry(countryShortName);
        userPreference.setFavoriteTopics(new ArrayList<>(topics));

        mUser.setUserPreference(userPreference);

        mPreferencesViewModel.saveUserPreferences(mUser);
    }

    /**
     * It sets the spinner and the checkbox values based on what it has been saved in the SharedPreferences file.
     */
    private void setViewsChecked() {
        SharedPreferences sharedPref = getSharedPreferences(Constants.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);

        String countryOfInterest = sharedPref.getString(Constants.SHARED_PREFERENCES_COUNTRY_OF_INTEREST, null);
        Set<String> topicsOfInterest = sharedPref.getStringSet(Constants.SHARED_PREFERENCES_TOPICS_OF_INTEREST, null);

        if (countryOfInterest != null) {
            mSpinnerCountries.setSelection(getSpinnerPositionBasedOnValue(getCountryOfInterest(countryOfInterest)));
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
     * It sets the spinner and the checkbox values based on what it has been saved into Firebase Realtime Database.
     */
    private void setViewsCheckedWithFirebase(String countryOfInterest, Set<String> topicsOfInterest) {
        if (countryOfInterest != null) {
            mSpinnerCountries.setSelection(getSpinnerPositionBasedOnValue(getCountryOfInterest(countryOfInterest)));
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

    /**
     * It retrieves the country id to be used with NewsAPI.org.
     * @param countryOfInterest the country chosen by the user
     * @return the country id
     */
    private String getCountryOfInterest(String countryOfInterest) {
        switch (countryOfInterest) {
            case Constants.ARGENTINA:
                return getResources().getString(R.string.argentina);
            case Constants.AUSTRALIA:
                return getResources().getString(R.string.australia);
            case Constants.AUSTRIA:
                return getResources().getString(R.string.austria);
            case Constants.BELGIUM:
                return getResources().getString(R.string.belgium);
            case Constants.BRAZIL:
                return getResources().getString(R.string.brazil);
            case Constants.BULGARIA:
                return getResources().getString(R.string.bulgaria);
            case Constants.CANADA:
                return getResources().getString(R.string.canada);
            case Constants.CHINA:
                return getResources().getString(R.string.china);
            case Constants.COLOMBIA:
                return getResources().getString(R.string.colombia);
            case Constants.CUBA:
                getResources().getString(R.string.cuba);
            case Constants.CZECH_REPUBLIC:
                return getResources().getString(R.string.czech_republic);
            case Constants.EGYPT:
                return getResources().getString(R.string.egypt);
            case Constants.FRANCE:
                return getResources().getString(R.string.france);
            case Constants.GERMANY:
                return getResources().getString(R.string.germany);
            case Constants.GREECE:
                return getResources().getString(R.string.greece);
            case Constants.HONG_KONG:
                return getResources().getString(R.string.hong_kong);
            case Constants.HUNGARY:
                return getResources().getString(R.string.hungary);
            case Constants.INDIA:
                return getResources().getString(R.string.india);
            case Constants.INDONESIA:
                return getResources().getString(R.string.indonesia);
            case Constants.IRELAND:
                return getResources().getString(R.string.ireland);
            case Constants.ISRAEL:
                return getResources().getString(R.string.israel);
            case Constants.ITALY:
                return getResources().getString(R.string.italy);
            case Constants.JAPAN:
                return getResources().getString(R.string.japan);
            case Constants.LATVIA:
                return getResources().getString(R.string.latvia);
            case Constants.LITHUANIA:
                return getResources().getString(R.string.lithuania);
            case Constants.MALAYSIA:
                return getResources().getString(R.string.malaysia);
            case Constants.MEXICO:
                return getResources().getString(R.string.mexico);
            case Constants.MOROCCO:
                return getResources().getString(R.string.morocco);
            case Constants.NETHERLANDS:
                return getResources().getString(R.string.netherlands);
            case Constants.NEW_ZEALAND:
                return getResources().getString(R.string.new_zealand);
            case Constants.NIGERIA:
                return getResources().getString(R.string.nigeria);
            case Constants.NORWAY:
                return getResources().getString(R.string.norway);
            case Constants.PHILIPPINES:
                return getResources().getString(R.string.philippines);
            case Constants.POLAND:
                return getResources().getString(R.string.poland);
            case Constants.PORTUGAL:
                return getResources().getString(R.string.portugal);
            case Constants.ROMANIA:
                return getResources().getString(R.string.romania);
            case Constants.RUSSIA:
                return getResources().getString(R.string.russia);
            case Constants.SAUDI_ARABIA:
                return getResources().getString(R.string.saudi_arabia);
            case Constants.SERBIA:
                return getResources().getString(R.string.serbia);
            case Constants.SINGAPORE:
                return getResources().getString(R.string.singapore);
            case Constants.SLOVAKIA:
                return getResources().getString(R.string.slovakia);
            case Constants.SLOVENIA:
                return getResources().getString(R.string.slovenia);
            case Constants.SOUTH_AFRICA:
                return getResources().getString(R.string.south_africa);
            case Constants.SOUTH_KOREA:
                return getResources().getString(R.string.south_korea);
            case Constants.SWEDEN:
                return getResources().getString(R.string.sweden);
            case Constants.SWITZERLAND:
                return getResources().getString(R.string.switzerland);
            case Constants.TAIWAN:
                return getResources().getString(R.string.taiwan);
            case Constants.THAILAND:
                return getResources().getString(R.string.thailand);
            case Constants.TURKEY:
                return getResources().getString(R.string.turkey);
            case Constants.UNITED_ARAB_EMIRATES:
                return getResources().getString(R.string.united_arab_emirates);
            case Constants.UKRAINE:
                return getResources().getString(R.string.ukraine);
            case Constants.UNITED_KINGDOM:
                return getResources().getString(R.string.united_kingdom);
            case Constants.UNITED_STATES:
                return getResources().getString(R.string.united_states);
            default:
                return null;
        }
    }
}
