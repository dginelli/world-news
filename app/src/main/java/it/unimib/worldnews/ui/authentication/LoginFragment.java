package it.unimib.worldnews.ui.authentication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import it.unimib.worldnews.R;
import it.unimib.worldnews.utils.Constants;

/**
 * It shows the login screen.
 */
public class LoginFragment extends Fragment {

    private static final String TAG = "LoginFragment";

    private UserViewModel mUserViewModel;
    private GoogleSignInClient mGoogleSignInClient;
    private ActivityResultLauncher<Intent> mGoogleSignUpActivityResult;
    private String email;
    private String password;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        mGoogleSignUpActivityResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        mUserViewModel.signUpWithGoogle(data).observe(getViewLifecycleOwner(), authenticationResponse -> {
                            if (authenticationResponse.isSuccess()) {
                                NavHostFragment.findNavController(LoginFragment.this).
                                        navigate(R.id.action_loginFragment_to_preferencesActivity);
                            } else {
                                updateUIForFailure(authenticationResponse.getMessage());
                            }
                        });
                    } else {
                        Log.d(TAG, "Error with Google Registration");
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_login, container, false);

        final EditText editTextEmail = view.findViewById(R.id.email);
        final EditText editTextPassword = view.findViewById(R.id.password);

        mUserViewModel.signInWithEmail(email, password).observe(getViewLifecycleOwner(), authenticationResponse -> {
            if (authenticationResponse != null) {
                if (authenticationResponse.isSuccess()) {
                    NavHostFragment.findNavController(LoginFragment.this).
                            navigate(R.id.action_loginFragment_to_preferencesActivity);
                } else {
                    updateUIForFailure(authenticationResponse.getMessage());
                }
            }
        });

        final Button buttonLoginWithEmail = view.findViewById(R.id.login_button);
        buttonLoginWithEmail.setOnClickListener(item -> {
            email = editTextEmail.getText().toString();
            password = editTextPassword.getText().toString();
            mUserViewModel.signInWithEmail(email, password);
        });

        final Button buttonRegisterWithGoogle = view.findViewById(R.id.google_login_button);
        buttonRegisterWithGoogle.setOnClickListener(item -> {
            // Configure Google Sign In
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(Constants.DEFAULT_WEB_CLIENT_ID)
                    .requestEmail()
                    .build();

            mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            mGoogleSignUpActivityResult.launch(signInIntent);
        });

        final TextView textViewCreateAccount = view.findViewById(R.id.textview_create_account);
        textViewCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_registerFragment);
            }
        });

        return view;
    }

    /**
     * It shows a warning message to the user with a Snackbar.
     * @param message The warning message to be shown in the Snackbar.
     */
    private void updateUIForFailure(String message) {
        Snackbar.make(requireActivity().findViewById(android.R.id.content),
                message, Snackbar.LENGTH_SHORT).show();
        mUserViewModel.clear();
    }
}
