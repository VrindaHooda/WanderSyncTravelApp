package com.example.sprintproject.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sprintproject.model.AuthRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthViewModel extends ViewModel {
    private final FirebaseAuth myFirebaseAuth = AuthRepository.getAuthRepository(); // Use the singleton
    private static final String TAG = "UsernamePassword";

    private MutableLiveData<String> userIdLiveData = new MutableLiveData<>();
    private MutableLiveData<String> emailLiveData = new MutableLiveData<>();

    private String userId;
    private String username;

    public LiveData<String> getUserIdLiveData() {
        return userIdLiveData;
    }

    public LiveData<String> getEmailLiveData() {
        return emailLiveData;
    }

    // Get authentication status
    public boolean isAuthenticated() {
        return myFirebaseAuth.getCurrentUser() != null;
    }

    public FirebaseUser getUser() {
        return myFirebaseAuth.getCurrentUser();
    }

    // Register a new user
    public void createUser(String email, String password, AuthCallback callback) {
        myFirebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser user = myFirebaseAuth.getCurrentUser();
                        if (user != null) {
                            this.userId = user.getUid();
                            this.username = user.getEmail();
                            userIdLiveData.setValue(userId);
                            emailLiveData.setValue(username);
                        }
                        callback.onSuccess(user);
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        String errorMsg = task.getException() != null ? task.getException().getMessage() : "Registration failed";
                        callback.onFailure(errorMsg);
                    }
                });
    }

    // Sign in an existing user
    public void signIn(String email, String password, AuthCallback callback) {
        myFirebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = myFirebaseAuth.getCurrentUser();
                        Log.d(TAG, "isAuthenticated: " + isAuthenticated());
                        if (isAuthenticated()) {
                            this.userId = user.getUid(); // Ensure user is not null
                            this.username = user.getEmail();
                            userIdLiveData.setValue(userId); // Update LiveData
                            emailLiveData.setValue(username);
                            callback.onSuccess(user);
                        } else {
                            // This case should generally not happen if the sign-in is successful
                            Log.w(TAG, "signInWithEmail:success, but user is null");
                            callback.onFailure("User data is unavailable.");
                        }
                    } else {
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        String errorMsg = task.getException() != null ? task.getException().getMessage() : "Sign in failed";
                        callback.onFailure(errorMsg);
                    }
                });
    }


    // Sign out the current user
    public void signOut() {
        myFirebaseAuth.signOut();
        userIdLiveData.setValue(null); // Clear LiveData on sign out
        emailLiveData.setValue(null);
    }

    // Callback interface for authentication results
    public interface AuthCallback {
        void onSuccess(FirebaseUser user);
        void onFailure(String error);
    }
}