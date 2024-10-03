package com.example.sprintproject.model;

import com.google.firebase.auth.FirebaseAuth;

public class AuthRepository {

    public static FirebaseAuth createAuthRepository() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        return auth;
    }
}