package com.example.sprintproject.model;

import com.google.firebase.auth.FirebaseAuth;

public class AuthRepository {

    // Private constructor to prevent instantiation
    private AuthRepository() { }

    // Method to get the FirebaseAuth instance
    public static FirebaseAuth getAuthRepository() {
        return FirebaseAuth.getInstance();
    }
}
