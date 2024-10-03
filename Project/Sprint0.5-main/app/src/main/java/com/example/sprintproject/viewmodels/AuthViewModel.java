package com.example.sprintproject.viewmodels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.sprintproject.model.AuthRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthViewModel {
    private final FirebaseAuth MY_FIREBASEAUTH = AuthRepository.createAuthRepository();
    private static final String TAG = "UsernamePassword";


    // LiveData to observe authentication status
    private MutableLiveData<Boolean> isAuthenticated = new MutableLiveData<>();

    public LiveData<Boolean> getAuthenticationStatus() {
        return isAuthenticated;
    }
    public void checkCurrentUser() {
        FirebaseUser currentUser = MY_FIREBASEAUTH.getCurrentUser();
        if (currentUser != null) {
            currentUser.reload();
            isAuthenticated.setValue(true);
        } else {
            isAuthenticated.setValue(false);
        }
    }

    public void createUser(String username, String password) {
        MY_FIREBASEAUTH.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUser:success");
                            FirebaseUser user = MY_FIREBASEAUTH.getCurrentUser();
                            isAuthenticated.setValue(true);
                        } else {
                            Log.w(TAG, "createUser:failure", task.getException());
                            isAuthenticated.setValue(false);
                        }
                    }
                });
    }

    public boolean signIn (String username, String password) {
        MY_FIREBASEAUTH.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signIn:success");
                            FirebaseUser user = MY_FIREBASEAUTH.getCurrentUser();
                            isAuthenticated.setValue(true);
                        } else {
                            Log.w(TAG, "signIn:failure", task.getException());
                            isAuthenticated.setValue(false);
                        }
                    }
                });
    }

    public void signOut () {
        MY_FIREBASEAUTH.signOut();
        isAuthenticated.setValue(false);
    }

}
