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
    private final IPreferenceRepository mPreferencesRepository;

    public PreferencesViewModel(@NonNull Application application) {
        super(application);
        mPreferencesRepository = new PreferenceRepository(application);
    }

    public MutableLiveData<Boolean> saveUserPreferences(User user) {
        mResponseLiveData = mPreferencesRepository.saveUserPreferences(user);
        return mResponseLiveData;
    }
}
