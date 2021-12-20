package it.unimib.worldnews.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;

import it.unimib.worldnews.R;
import it.unimib.worldnews.ui.authentication.AuthenticationActivity;
import it.unimib.worldnews.utils.SharedPreferencesProvider;

/**
 * It shows the favorite news.
 */
public class FavoriteNewsFragment extends Fragment {

    private static final String TAG = "FavoriteNewsFragment";

    public FavoriteNewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // It is necessary to specify that the toolbar has a custom menu
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite_news, container, false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        // The custom menu that we want to add to the toolbar
        inflater.inflate(R.menu.favorite_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        // Listener for rhe items in the custom menu
        if (item.getItemId() == R.id.delete_all) {
            Log.d(TAG, "onOptionsItemSelected: " + item);
            return true;
        } else if (item.getItemId() == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            SharedPreferencesProvider sharedPreferencesProvider =
                    new SharedPreferencesProvider(requireActivity().getApplication());
            sharedPreferencesProvider.deleteAll();
            startActivity(new Intent(requireActivity(), AuthenticationActivity.class));
            requireActivity().finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
