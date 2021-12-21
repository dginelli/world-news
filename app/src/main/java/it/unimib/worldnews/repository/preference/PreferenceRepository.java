package it.unimib.worldnews.repository.preference;

import static it.unimib.worldnews.utils.Constants.USER_COLLECTION;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import it.unimib.worldnews.model.User;
import it.unimib.worldnews.utils.Constants;

public class PreferenceRepository implements IPreferenceRepository {

    private static final String TAG = "PreferenceRepository";

    private final DatabaseReference mFirebaseDatabase;
    private final MutableLiveData<Boolean> mResponseLiveData;
    private final MutableLiveData<User> mUserLiveData;

    public PreferenceRepository(Application application) {
        mFirebaseDatabase = FirebaseDatabase.getInstance(Constants.FIREBASE_DATABASE_URL).getReference();
        mResponseLiveData = new MutableLiveData<>();
        mUserLiveData = new MutableLiveData<>();
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

    @Override
    public MutableLiveData<User> readUserInfo(String uId) {

        mFirebaseDatabase.child(USER_COLLECTION).child(uId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    mUserLiveData.postValue(task.getResult().getValue(User.class));
                }
                else {
                    Log.d("firebase", "Error getting data", task.getException());
                    mUserLiveData.postValue(null);
                }
            }
        });

        return mUserLiveData;
    }
}
