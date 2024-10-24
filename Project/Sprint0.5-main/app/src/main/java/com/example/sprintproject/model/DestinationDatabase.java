package com.example.sprintproject.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DestinationDatabase {

    private static DestinationDatabase instance;

    private DatabaseReference databaseReference;

    private DestinationDatabase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);
        databaseReference = database.getReference();
    }

    public static synchronized DestinationDatabase getInstance() {
        if (instance == null) {
            instance = new DestinationDatabase();
        }
        return instance;
    }
    public void writeData(String path, Object data) {
        databaseReference.child(path).setValue(data);
    }
    public DatabaseReference readData(String path) {
        return databaseReference.child(path);
    }

}
