package com.example.sprintproject.model;

import com.google.firebase.auth.FirebaseAuth;

public class AuthRepository {
    /**
     * The single instance of {@code AuthRepository}.
     */
    private static AuthRepository instance;

    /**
     * Private constructor to prevent direct instantiation.
     */
    private AuthRepository() { }

    /**
     * Returns the singleton instance of {@code AuthRepository}.
     * If the instance is null, a new one is created.
     *
     * @return the singleton instance of {@code AuthRepository}
     */
    public static AuthRepository getInstance() {
        if (instance == null) {
            instance = new AuthRepository();
        }
        return instance;
    }

    /**
     * Provides access to the Firebase Authentication instance.
     *
     * @return the {@code FirebaseAuth} instance
     */
    public static FirebaseAuth getAuthRepository() {
        return FirebaseAuth.getInstance();
    }
}
