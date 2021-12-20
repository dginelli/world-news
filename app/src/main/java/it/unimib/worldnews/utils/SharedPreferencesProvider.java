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
    private final SharedPreferences sharedPref;

    public SharedPreferencesProvider(Application application) {
        this.mApplication = application;
        sharedPref = mApplication.getSharedPreferences(Constants.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
    }

    public String getCountry() {
        return sharedPref.getString(Constants.SHARED_PREFERENCES_COUNTRY_OF_INTEREST, null);
    }

    /**
     * It gets the last time in which news was downloaded from the Web Service.
     * @return the last time in which news was downloaded from the Web Service.
     */
    public long getLastUpdate() {
        return sharedPref.getLong(Constants.LAST_UPDATE, 0);
    }

    /**
     * It saves the last time in which news was downloaded from the Web Service.
     * @param lastUpdate last time in which news was downloaded from the Web Service.
     */
    public void setLastUpdate(long lastUpdate) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(Constants.LAST_UPDATE, lastUpdate);
        editor.apply();
    }

    public void setAuthenticationToken(String token) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constants.AUTHENTICATION_TOKEN, token);
        editor.apply();
    }

    public void setUserId(String userId) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constants.USER_ID, userId);
        editor.apply();
    }

    public void deleteAll() {
        sharedPref.edit().clear().apply();
    }
}
