package it.unimib.worldnews.repository.preference;

import androidx.lifecycle.MutableLiveData;

import it.unimib.worldnews.model.User;

public interface IPreferenceRepository {
    MutableLiveData<Boolean> saveUserPreferences(User user);
}
