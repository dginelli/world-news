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
            mButtonPressedCounter++;
            if (isCountryOfInterestSelected() && isTopicOfInterestSelected()) {
                Log.d(TAG, "One country of interest and at least one topic has been chosen");

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
    }

    /**
     * It sets the spinner and the checkbox values based on what it has been saved in the SharedPreferences file.
     */
    private void setViewsChecked() {
        SharedPreferences sharedPref = getSharedPreferences(Constants.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);

        String countryOfInterest = sharedPref.getString(Constants.SHARED_PREFERENCES_COUNTRY_OF_INTEREST, null);
        Set<String> topicsOfInterest = sharedPref.getStringSet(Constants.SHARED_PREFERENCES_TOPICS_OF_INTEREST, null);

        if (countryOfInterest != null) {
            switch (countryOfInterest) {
                case Constants.ARGENTINA:
                    mSpinnerCountries.setSelection(
                            getSpinnerPositionBasedOnValue(getResources().getString(R.string.argentina)));
                    break;
                case Constants.AUSTRALIA:
                    mSpinnerCountries.setSelection(
                            getSpinnerPositionBasedOnValue(getResources().getString(R.string.australia)));
                    break;
                case Constants.AUSTRIA:
                    mSpinnerCountries.setSelection(
                            getSpinnerPositionBasedOnValue(getResources().getString(R.string.austria)));
                    break;
                case Constants.BELGIUM:
                    mSpinnerCountries.setSelection(
                            getSpinnerPositionBasedOnValue(getResources().getString(R.string.belgium)));
                    break;
                case Constants.BRAZIL:
                    mSpinnerCountries.setSelection(
                            getSpinnerPositionBasedOnValue(getResources().getString(R.string.brazil)));
                    break;
                case Constants.BULGARIA:
                    mSpinnerCountries.setSelection(
                            getSpinnerPositionBasedOnValue(getResources().getString(R.string.bulgaria)));
                    break;
                case Constants.CANADA:
                    mSpinnerCountries.setSelection(
                            getSpinnerPositionBasedOnValue(getResources().getString(R.string.canada)));
                    break;
                case Constants.CHINA:
                    mSpinnerCountries.setSelection(
                            getSpinnerPositionBasedOnValue(getResources().getString(R.string.china)));
                    break;
                case Constants.COLOMBIA:
                    mSpinnerCountries.setSelection(
                            getSpinnerPositionBasedOnValue(getResources().getString(R.string.colombia)));
                    break;
                case Constants.CUBA:
                    mSpinnerCountries.setSelection(
                            getSpinnerPositionBasedOnValue(getResources().getString(R.string.cuba)));
                    break;
                case Constants.CZECH_REPUBLIC:
                    mSpinnerCountries.setSelection(
                            getSpinnerPositionBasedOnValue(getResources().getString(R.string.czech_republic)));
                    break;
                case Constants.EGYPT:
                    mSpinnerCountries.setSelection(
                            getSpinnerPositionBasedOnValue(getResources().getString(R.string.egypt)));
                    break;
                case Constants.FRANCE:
                    mSpinnerCountries.setSelection(
                            getSpinnerPositionBasedOnValue(getResources().getString(R.string.france)));
                    break;
                case Constants.GERMANY:
                    mSpinnerCountries.setSelection(
                            getSpinnerPositionBasedOnValue(getResources().getString(R.string.germany)));
                    break;
                case Constants.GREECE:
                    mSpinnerCountries.setSelection(
                            getSpinnerPositionBasedOnValue(getResources().getString(R.string.greece)));
                    break;
                case Constants.HONG_KONG:
                    mSpinnerCountries.setSelection(
                            getSpinnerPositionBasedOnValue(getResources().getString(R.string.hong_kong)));
                    break;
                case Constants.HUNGARY:
                    mSpinnerCountries.setSelection(
                            getSpinnerPositionBasedOnValue(getResources().getString(R.string.hungary)));
                    break;
                case Constants.INDIA:
                    mSpinnerCountries.setSelection(
                            getSpinnerPositionBasedOnValue(getResources().getString(R.string.india)));
                    break;
                case Constants.INDONESIA:
                    mSpinnerCountries.setSelection(
                            getSpinnerPositionBasedOnValue(getResources().getString(R.string.indonesia)));
                    break;
                case Constants.IRELAND:
                    mSpinnerCountries.setSelection(
                            getSpinnerPositionBasedOnValue(getResources().getString(R.string.ireland)));
                    break;
                case Constants.ISRAEL:
                    mSpinnerCountries.setSelection(
                            getSpinnerPositionBasedOnValue(getResources().getString(R.string.israel)));
                    break;
                case Constants.ITALY:
                    mSpinnerCountries.setSelection(
                            getSpinnerPositionBasedOnValue(getResources().getString(R.string.italy)));
                    break;
                case Constants.JAPAN:
                    mSpinnerCountries.setSelection(
                            getSpinnerPositionBasedOnValue(getResources().getString(R.string.japan)));
                    break;
                case Constants.LATVIA:
                    mSpinnerCountries.setSelection(
                            getSpinnerPositionBasedOnValue(getResources().getString(R.string.latvia)));
                    break;
                case Constants.LITHUANIA:
                    mSpinnerCountries.setSelection(
                            getSpinnerPositionBasedOnValue(getResources().getString(R.string.lithuania)));
                    break;
                case Constants.MALAYSIA:
                    mSpinnerCountries.setSelection(
                            getSpinnerPositionBasedOnValue(getResources().getString(R.string.malaysia)));
                    break;
                case Constants.MEXICO:
                    mSpinnerCountries.setSelection(
                            getSpinnerPositionBasedOnValue(getResources().getString(R.string.mexico)));
                    break;
                case Constants.MOROCCO:
                    mSpinnerCountries.setSelection(
                            getSpinnerPositionBasedOnValue(getResources().getString(R.string.morocco)));
                    break;
                case Constants.NETHERLANDS:
                    mSpinnerCountries.setSelection(
                            getSpinnerPositionBasedOnValue(getResources().getString(R.string.netherlands)));
                    break;
                case Constants.NEW_ZEALAND:
                    mSpinnerCountries.setSelection(
                            getSpinnerPositionBasedOnValue(getResources().getString(R.string.new_zealand)));
                    break;
                case Constants.NIGERIA:
                    mSpinnerCountries.setSelection(
                            getSpinnerPositionBasedOnValue(getResources().getString(R.string.nigeria)));
                    break;
                case Constants.NORWAY:
                    mSpinnerCountries.setSelection(
                            getSpinnerPositionBasedOnValue(getResources().getString(R.string.norway)));
                    break;
                case Constants.PHILIPPINES:
                    mSpinnerCountries.setSelection(
                            getSpinnerPositionBasedOnValue(getResources().getString(R.string.philippines)));
                    break;
                case Constants.POLAND:
                    mSpinnerCountries.setSelection(
                            getSpinnerPositionBasedOnValue(getResources().getString(R.string.poland)));
                    break;
                case Constants.PORTUGAL:
                    mSpinnerCountries.setSelection(
                            getSpinnerPositionBasedOnValue(getResources().getString(R.string.portugal)));
                    break;
                case Constants.ROMANIA:
                    mSpinnerCountries.setSelection(
                            getSpinnerPositionBasedOnValue(getResources().getString(R.string.romania)));
                    break;
                case Constants.RUSSIA:
                    mSpinnerCountries.setSelection(
                            getSpinnerPositionBasedOnValue(getResources().getString(R.string.russia)));
                    break;
                case Constants.SAUDI_ARABIA:
                    mSpinnerCountries.setSelection(
                            getSpinnerPositionBasedOnValue(getResources().getString(R.string.saudi_arabia)));
                    break;
                case Constants.SERBIA:
                    mSpinnerCountries.setSelection(
                            getSpinnerPositionBasedOnValue(getResources().getString(R.string.serbia)));
                    break;
                case Constants.SINGAPORE:
                    mSpinnerCountries.setSelection(
                            getSpinnerPositionBasedOnValue(getResources().getString(R.string.singapore)));
                    break;
                case Constants.SLOVAKIA:
                    mSpinnerCountries.setSelection(
                            getSpinnerPositionBasedOnValue(getResources().getString(R.string.slovakia)));
                    break;
                case Constants.SLOVENIA:
                    mSpinnerCountries.setSelection(
                            getSpinnerPositionBasedOnValue(getResources().getString(R.string.slovenia)));
                    break;
                case Constants.SOUTH_AFRICA:
                    mSpinnerCountries.setSelection(
                            getSpinnerPositionBasedOnValue(getResources().getString(R.string.south_africa)));
                    break;
                case Constants.SOUTH_KOREA:
                    mSpinnerCountries.setSelection(
                            getSpinnerPositionBasedOnValue(getResources().getString(R.string.south_korea)));
                    break;
                case Constants.SWEDEN:
                    mSpinnerCountries.setSelection(
                            getSpinnerPositionBasedOnValue(getResources().getString(R.string.sweden)));
                    break;
                case Constants.SWITZERLAND:
                    mSpinnerCountries.setSelection(
                            getSpinnerPositionBasedOnValue(getResources().getString(R.string.switzerland)));
                    break;
                case Constants.TAIWAN:
                    mSpinnerCountries.setSelection(
                            getSpinnerPositionBasedOnValue(getResources().getString(R.string.taiwan)));
                    break;
                case Constants.THAILAND:
                    mSpinnerCountries.setSelection(
                            getSpinnerPositionBasedOnValue(getResources().getString(R.string.thailand)));
                    break;
                case Constants.TURKEY:
                    mSpinnerCountries.setSelection(
                            getSpinnerPositionBasedOnValue(getResources().getString(R.string.turkey)));
                    break;
                case Constants.UNITED_ARAB_EMIRATES:
                    mSpinnerCountries.setSelection(
                            getSpinnerPositionBasedOnValue(getResources().getString(R.string.united_arab_emirates)));
                    break;
                case Constants.UKRAINE:
                    mSpinnerCountries.setSelection(
                            getSpinnerPositionBasedOnValue(getResources().getString(R.string.ukraine)));
                    break;
                case Constants.UNITED_KINGDOM:
                    mSpinnerCountries.setSelection(
                            getSpinnerPositionBasedOnValue(getResources().getString(R.string.united_kingdom)));
                    break;
                case Constants.UNITED_STATES:
                    mSpinnerCountries.setSelection(
                            getSpinnerPositionBasedOnValue(getResources().getString(R.string.united_states)));
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
