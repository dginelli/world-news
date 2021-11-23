package it.unimib.worldnews.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

import it.unimib.worldnews.R;

/**
 * It manages the operations with the SharedPreferences API.
 */
public class SharedPreferencesProvider {

    private final Application mApplication;

    public SharedPreferencesProvider(Application application) {
        this.mApplication = application;
    }

    public String getCountry() {
        SharedPreferences sharedPref =
                mApplication.getSharedPreferences(Constants.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPref.getString(Constants.SHARED_PREFERENCES_COUNTRY_OF_INTEREST, null);
    }

    /**
     * It gets the last time in which news was downloaded from the Web Service.
     * @return the last time in which news was downloaded from the Web Service.
     */
    public long getLastUpdate() {
        SharedPreferences sharedPref =
                mApplication.getSharedPreferences(Constants.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPref.getLong(Constants.LAST_UPDATE, 0);
    }

    /**
     * It saves the last time in which news was downloaded from the Web Service.
     * @param lastUpdate last time in which news was downloaded from the Web Service.
     */
    public void setLastUpdate(long lastUpdate) {
        SharedPreferences sharedPref = mApplication.getSharedPreferences(Constants.SHARED_PREFERENCES_FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(Constants.LAST_UPDATE, lastUpdate);
        editor.apply();
    }
}
