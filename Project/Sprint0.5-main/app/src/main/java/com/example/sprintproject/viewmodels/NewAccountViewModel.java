package com.example.sprintproject.viewmodels;

import android.util.Log;

import com.example.sprintproject.model.AuthRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class NewAccountViewModel {
    private static final String TAG = "NewAccountViewModel";
    private final AuthViewModel authViewModel;

    // Constructor
    public NewAccountViewModel() {
        this.authViewModel = new AuthViewModel();
    }
    public String getUserId() {
        FirebaseUser currentUser = authViewModel.getCurrentUser();
        if (currentUser != null) {
            return currentUser.getUid();
        } else {
            Log.w(TAG, "No user is currently signed in.");
            return null; // No user signed in
        }
    }

    public interface UserCreationCallback {
        void onSuccess(); // Called when user creation is successful
        void onFailure(String error); // Called when user creation fails
    }

    // Method to create a new user
    public void writeNewUser(String userId, String username, String password, UserCreationCallback callback) {
        authViewModel.createUser(username, password, new AuthViewModel.Callback() {
            @Override
            public void onSuccess(FirebaseUser user) {
                Log.d(TAG, "User created: " + userId);
                callback.onSuccess();
            }

            @Override
            public void onFailure(String error) {
                Log.e(TAG, "User creation failed: " + error);
                callback.onFailure(error);
            }
        });
    }

    // Callback interface for user creation results
    public interface Callback {
        void onSuccess();

        void onFailure(String error);
    }
}
