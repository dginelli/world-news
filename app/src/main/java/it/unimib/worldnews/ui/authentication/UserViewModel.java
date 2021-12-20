package it.unimib.worldnews.ui.authentication;

import android.app.Application;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import it.unimib.worldnews.model.AuthenticationResponse;
import it.unimib.worldnews.repository.IUserRepository;
import it.unimib.worldnews.repository.UserRepository;

public class UserViewModel extends AndroidViewModel {

    private final IUserRepository mUserRepository;

    public UserViewModel(@NonNull Application application) {
        super(application);
        this.mUserRepository = new UserRepository(application);
    }

    public MutableLiveData<AuthenticationResponse> signInWithEmail(String email, String password) {
        return mUserRepository.signInWithEmail(email, password);
    }

    public MutableLiveData<AuthenticationResponse> signUpWithEmail(String email, String password) {
        return mUserRepository.createUserWithEmail(email, password);
    }

    public MutableLiveData<AuthenticationResponse> signUpWithGoogle(Intent intent) {
        return mUserRepository.createUserWithGoogle(intent);
    }
}
