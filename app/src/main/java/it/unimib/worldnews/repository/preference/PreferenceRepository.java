package it.unimib.worldnews.repository.preference;

import static it.unimib.worldnews.utils.Constants.USER_COLLECTION;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import it.unimib.worldnews.model.User;
import it.unimib.worldnews.utils.Constants;

public class PreferenceRepository implements IPreferenceRepository {

    private static final String TAG = "PreferenceRepository";

    private final DatabaseReference mFirebaseDatabase;
    private final MutableLiveData<Boolean> mResponseLiveData;

    public PreferenceRepository(Application application) {
        mFirebaseDatabase = FirebaseDatabase.getInstance(Constants.FIREBASE_DATABASE_URL).getReference();
        mResponseLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<Boolean> saveUserPreferences(User user) {
        if (user != null) {
            mFirebaseDatabase.child(USER_COLLECTION).child(user.getUId()).setValue(user).
                addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        mResponseLiveData.postValue(true);
                    } else {
                        mResponseLiveData.postValue(false);
                    }
            });
        }
        return mResponseLiveData;
    }
}
