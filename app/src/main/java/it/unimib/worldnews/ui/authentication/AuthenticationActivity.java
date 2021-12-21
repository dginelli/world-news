package it.unimib.worldnews.ui.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import it.unimib.worldnews.R;

/**
 * Activity to manage the login and registration of a user.
 */
public class AuthenticationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
    }
}