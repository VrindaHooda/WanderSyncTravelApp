package com.example.sprintproject.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserRepository {
    private FirebaseDatabase database;
    private DatabaseReference reference;

    public UserRepository() {
        this.database = FirebaseDatabase.getInstance();
        this.reference = database.getReference("message");
    }

    public createAccount(String username,)
}
