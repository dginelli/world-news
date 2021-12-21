package it.unimib.worldnews.repository.preference;

import static it.unimib.worldnews.utils.Constants.USER_COLLECTION;
import static it.unimib.worldnews.utils.Constants.USER_EMAIL;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;

import it.unimib.worldnews.R;
import it.unimib.worldnews.model.AuthenticationResponse;
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





                /*addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(@NonNull Void unused) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Log.d(TAG, e.getMessage());


            }
        });*/
        return mResponseLiveData;
    }
}
