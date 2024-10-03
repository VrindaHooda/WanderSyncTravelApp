package com.example.sprintproject.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserRepository {

    public static DatabaseReference createUserRepository() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        return database.getReference();
    }
}
