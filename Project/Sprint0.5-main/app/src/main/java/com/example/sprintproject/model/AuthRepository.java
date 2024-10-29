package com.example.sprintproject.model;

import com.google.firebase.auth.FirebaseAuth;

public class AuthRepository {
    private static AuthRepository instance;

    // Private constructor to prevent instantiation
    private AuthRepository() {}

    public static AuthRepository getInstance() {
        if (instance == null) {
            instance = new AuthRepository();
        }
        return instance;
    }

    // Method to get the FirebaseAuth instance
    public static FirebaseAuth getAuthRepository() {
        return FirebaseAuth.getInstance();
    }
}
