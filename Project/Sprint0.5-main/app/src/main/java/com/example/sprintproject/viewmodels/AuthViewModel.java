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
    public interface Callback {
        void onSuccess(FirebaseUser user);
        void onFailure(String error);
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
    public FirebaseUser getCurrentUser() {
        return MY_FIREBASEAUTH.getCurrentUser();
    }

    public void createUser(String username, String password, Callback callback) {
        MY_FIREBASEAUTH.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUser:success");
                            FirebaseUser user = MY_FIREBASEAUTH.getCurrentUser();
                            callback.onSuccess(user);
                        } else {
                            Log.w(TAG, "createUser:failure", task.getException());
                            callback.onFailure(task.getException().getMessage());
                        }
                    }
                });
    }

    public void signIn(String username, String password, Callback callback) {
        MY_FIREBASEAUTH.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() { // Removed 'this'
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signIn: success");
                            FirebaseUser user = MY_FIREBASEAUTH.getCurrentUser();
                            callback.onSuccess(user); // Use the callback to handle success
                            isAuthenticated.setValue(true);
                        } else {
                            Log.w(TAG, "signIn: failure", task.getException());
                            callback.onFailure(task.getException().getMessage()); // Use the callback to handle failure
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
