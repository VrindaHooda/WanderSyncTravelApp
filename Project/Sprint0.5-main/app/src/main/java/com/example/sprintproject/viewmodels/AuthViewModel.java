package com.example.sprintproject.viewmodels;

import android.text.TextUtils;
import android.util.Patterns;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthViewModel extends ViewModel {

    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    // LiveData for username and password
    public MutableLiveData<String> username = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();

    // LiveData for login status, account creation status, and error messages
    private final MutableLiveData<Boolean> _isLoginSuccessful = new MutableLiveData<>();
    private final MutableLiveData<Boolean> _isAccountCreated = new MutableLiveData<>();
    private final MutableLiveData<String> _errorMessage = new MutableLiveData<>();

    public LiveData<Boolean> isLoginSuccessful = _isLoginSuccessful;
    public LiveData<Boolean> isAccountCreated = _isAccountCreated;
    public LiveData<String> errorMessage = _errorMessage;

    // Constructor
    public AuthViewModel() {}

    // Method to handle login process
    public void login(String email, String pass) {
        if (isInputValid(email, pass)) {
            firebaseAuth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            _isLoginSuccessful.setValue(true);
                            _errorMessage.setValue(null);  // Clear any previous error message
                        } else {
                            _isLoginSuccessful.setValue(false);
                            _errorMessage.setValue("Login failed: " + task.getException().getMessage());
                        }
                    });
        }
    }

    // Method to handle account creation process
    public void createAccount(String email, String pass) {
        if (isInputValid(email, pass)) {
            firebaseAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            _isAccountCreated.setValue(true);
                            _errorMessage.setValue(null);  // Clear any previous error message
                        } else {
                            _isAccountCreated.setValue(false);
                            _errorMessage.setValue("Account creation failed: " + task.getException().getMessage());
                        }
                    });
        }
    }

    // Helper method to validate email and password input
    public boolean isInputValid(String email, String pass) {
        if (email == null || email.isEmpty()) {
            _errorMessage.setValue("Email cannot be empty.");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _errorMessage.setValue("Invalid email format.");
            return false;
        } else if (pass == null || pass.isEmpty()) {
            _errorMessage.setValue("Password cannot be empty.");
            return false;
        } else if (pass.length() < 6) {
            _errorMessage.setValue("Password must be at least 6 characters.");
            return false;
        }
        return true;
    }

    // Method to check if a user is authenticated
    public boolean isAuthenticated() {
        return firebaseAuth.getCurrentUser() != null;
    }

    // Method to get the current user
    public FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }

    // Method to handle account creation button click
    public void onCreateAccountButtonClicked() {
        String email = username.getValue();
        String password = this.password.getValue();

        // Trigger account creation logic
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            _errorMessage.setValue("Username or Password cannot be empty");
        } else {
            createAccount(email, password);
        }
    }

    // Method to handle login button click
    public void onLoginButtonClicked() {
        String email = username.getValue();
        String password = this.password.getValue();

        // Trigger login logic
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            _errorMessage.setValue("Username or Password cannot be empty");
        } else {
            login(email, password);
        }
    }

    // Method to log out the user
    public void logout() {
        firebaseAuth.signOut();
        _isLoginSuccessful.setValue(false);
    }
}