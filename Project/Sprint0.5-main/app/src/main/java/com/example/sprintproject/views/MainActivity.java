package com.example.sprintproject.views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import com.example.sprintproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private static final int SPLASH_SCREEN_DELAY = 3000;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        mAuth = FirebaseAuth.getInstance();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, HomeScreen.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_SCREEN_DELAY);
    }

    /**
     * Checks if the currently logged-in account matches the provided email address.
     *
     * @param email the email address to check against the logged-in user's email
     * @return {@code true} if the logged-in user's email matches the provided email, {@code false} otherwise
     */
    public boolean isAccountCreated(String email) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            return user.getEmail().equals(email);
        } else {
            return false;
        }
    }

    /**
     * Checks if a user with the given email exists in the database and invokes a callback with the result.
     *
     * @param email    the email address to search for in the database
     * @param callback the callback to invoke with the result; {@code true} if the user exists, {@code false} otherwise
     */
    public void isUserAdded(String email, OnUserAddedCallback callback) {
        Query query = databaseReference.orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    callback.onCallback(true);
                } else {
                    callback.onCallback(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public interface OnUserAddedCallback {

        /**
         * Invoked when the existence of a user is determined.
         *
         * @param exists {@code true} if the user exists in the database, {@code false} otherwise
         */
        void onCallback(boolean exists);
    }
}