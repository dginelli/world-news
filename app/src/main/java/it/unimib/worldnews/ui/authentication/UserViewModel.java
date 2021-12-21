package it.unimib.worldnews.ui.authentication;

import android.app.Application;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import it.unimib.worldnews.model.AuthenticationResponse;
import it.unimib.worldnews.repository.user.IUserRepository;
import it.unimib.worldnews.repository.user.UserRepository;

/**
 * ViewModel to manage the user authentication.
 */
public class UserViewModel extends AndroidViewModel {

    private MutableLiveData<AuthenticationResponse> mAuthenticationResponseLiveData;
    private final IUserRepository mUserRepository;

    public UserViewModel(@NonNull Application application) {
        super(application);
        this.mUserRepository = new UserRepository(application);
    }

    public MutableLiveData<AuthenticationResponse> signInWithEmail(String email, String password) {
        mAuthenticationResponseLiveData = mUserRepository.signInWithEmail(email, password);
        return mAuthenticationResponseLiveData;
    }

    public MutableLiveData<AuthenticationResponse> signUpWithEmail(String email, String password) {
        mAuthenticationResponseLiveData = mUserRepository.createUserWithEmail(email, password);
        return mAuthenticationResponseLiveData;
    }

    public MutableLiveData<AuthenticationResponse> signUpWithGoogle(Intent intent) {
        mAuthenticationResponseLiveData = mUserRepository.createUserWithGoogle(intent);
        return mAuthenticationResponseLiveData;
    }

    public void clear() {
        mAuthenticationResponseLiveData.postValue(null);
    }
}
