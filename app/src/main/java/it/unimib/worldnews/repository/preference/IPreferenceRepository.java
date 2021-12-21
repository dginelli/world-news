package it.unimib.worldnews.repository.preference;

import androidx.lifecycle.MutableLiveData;

import it.unimib.worldnews.model.User;

/**
 * Interface for Repositories that manage the user preferences.
 */
public interface IPreferenceRepository {
    MutableLiveData<Boolean> saveUserPreferences(User user);
    MutableLiveData<User> readUserInfo(String uId);
}
