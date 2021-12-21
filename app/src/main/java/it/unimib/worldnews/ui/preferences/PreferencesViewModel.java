package it.unimib.worldnews.ui.preferences;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import it.unimib.worldnews.model.User;
import it.unimib.worldnews.repository.preference.IPreferenceRepository;
import it.unimib.worldnews.repository.preference.PreferenceRepository;

public class PreferencesViewModel extends AndroidViewModel {

    private MutableLiveData<Boolean> mResponseLiveData;
    private MutableLiveData<User> mUserLiveData;
    private final IPreferenceRepository mPreferencesRepository;

    public PreferencesViewModel(@NonNull Application application) {
        super(application);
        mPreferencesRepository = new PreferenceRepository(application);
    }

    public MutableLiveData<Boolean> saveUserPreferences(User user) {
        mResponseLiveData = mPreferencesRepository.saveUserPreferences(user);
        return mResponseLiveData;
    }

    public MutableLiveData<User> readUserInformation(String uId) {
        mUserLiveData = mPreferencesRepository.readUserInfo(uId);
        return mUserLiveData;
    }
}
