package com.example.sprintproject.model;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Id {

    private static Id instance;
    private String id;

    // Private constructor to prevent instantiation
    private Id() {
    }

    private Id(String id) {
        this.id = id;
    }

    // Public method to provide access to the instance
    public static synchronized Id getInstance() {
        if (instance == null) {
            instance = new Id();
        }
        return instance;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
