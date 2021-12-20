package it.unimib.worldnews.repository;

import android.content.Intent;

import androidx.lifecycle.MutableLiveData;

import it.unimib.worldnews.model.AuthenticationResponse;

public interface IUserRepository {
    MutableLiveData<AuthenticationResponse> signInWithEmail(String email, String password);
    MutableLiveData<AuthenticationResponse> createUserWithGoogle(Intent data);
    MutableLiveData<AuthenticationResponse> createUserWithEmail(String email, String password);
}
