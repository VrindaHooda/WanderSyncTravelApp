package com.example.sprintproject.viewmodels;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.sprintproject.model.AuthRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AuthViewModel extends ViewModel {
    private final FirebaseAuth myFirebaseAuth = AuthRepository.getAuthRepository();
    private static final String TAG = "UsernamePassword";
    private MutableLiveData<String> userIdLiveData = new MutableLiveData<>();
    private MutableLiveData<String> emailLiveData = new MutableLiveData<>();
    private String userId;
    private String username;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();


    public LiveData<String> getUserIdLiveData() {
        return userIdLiveData;
    }

    public LiveData<String> getEmailLiveData() {
        return emailLiveData;
    }

    public boolean isAuthenticated() {
        return myFirebaseAuth.getCurrentUser() != null;
    }

    public FirebaseUser getUser() {
        return myFirebaseAuth.getCurrentUser();
    }

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
                        if (user != null) {
                            // Create a user object
                            Map<String, Object> userData = new HashMap<>();
                            userData.put("uid", user.getUid());
                            userData.put("email", user.getEmail());

                            // Store user data in Firestore
                            db.collection("users").document(user.getUid())
                                    .set(userData)
                                    .addOnSuccessListener(aVoid -> {
                                        System.out.println("User data saved successfully");
                                    })
                                    .addOnFailureListener(e -> {
                                        System.out.println("Error saving user data: " + e.getMessage());
                                    });
                        }
                        callback.onSuccess(user);
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        String errorMsg = task.getException() != null
                                ? task.getException().getMessage() : "Registration failed";
                        callback.onFailure(errorMsg);
                    }
                });
    }

    public void signIn(String email, String password, AuthCallback callback) {
        myFirebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = myFirebaseAuth.getCurrentUser();
                        Log.d(TAG, "isAuthenticated: " + isAuthenticated());
                        if (isAuthenticated()) {
                            this.userId = user.getUid();
                            this.username = user.getEmail();
                            userIdLiveData.setValue(userId);
                            emailLiveData.setValue(username);
                            callback.onSuccess(user);
                        } else {
                            Log.w(TAG, "signInWithEmail:success, but user is null");
                            callback.onFailure("User data is unavailable.");
                        }
                    } else {
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        String errorMsg = task.getException() != null
                                ? task.getException().getMessage() : "Sign in failed";
                        callback.onFailure(errorMsg);
                    }
                });
    }

    public void signOut() {
        myFirebaseAuth.signOut();
        userIdLiveData.setValue(null);
        emailLiveData.setValue(null);
    }

    public interface AuthCallback {
        void onSuccess(FirebaseUser user);
        void onFailure(String error);
    }

    public String getLoggedInUserId() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null ? user.getUid() : null;
    }

}