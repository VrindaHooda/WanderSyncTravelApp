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
    private MutableLiveData<String> userId = new MutableLiveData<>();

    public LiveData<String> getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId.setValue(userId);
    }

    // Get authentication status
    public boolean isAuthenticated() {
        return myFirebaseAuth.getCurrentUser() != null;
    }

    public String getEmail() {
        FirebaseUser currentUser = myFirebaseAuth.getCurrentUser();
        if (currentUser != null) {
            // Retrieve the email
            String email = currentUser.getEmail();
            return email;
        }
        return "No user signed in";
    }

    public String getId() {
        FirebaseUser currentUser = myFirebaseAuth.getCurrentUser();
        if (currentUser != null) {
            return currentUser.getUid();
        }
        return "No user signed in";
    }

    // Register a new user
    public void createUser(String email, String password, AuthCallback callback) {
        myFirebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser user = myFirebaseAuth.getCurrentUser();
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
                        callback.onSuccess(user);

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
    }

    // Callback interface for authentication results
    public interface AuthCallback {
        void onSuccess(FirebaseUser user);
        void onFailure(String error);
    }
}
