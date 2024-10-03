package com.example.sprintproject.viewmodels;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import com.example.sprintproject.model.AuthRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthViewModel {
    private final FirebaseAuth MY_FIREBASEAUTH = AuthRepository.createAuthRepository();
    private static final String TAG = "UsernamePassword";

    public void checkCurrentUser() {
        FirebaseUser currentUser = MY_FIREBASEAUTH.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
    }

    public void createUser(String username, String password) {
        MY_FIREBASEAUTH.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUser:success");
                            FirebaseUser user = MY_FIREBASEAUTH.getCurrentUser();
                            updateUI(user);
                        } else {
                            Log.w(TAG, "createUser:failure", task.getException());
                            updateUI(null);
                        }
                    }
                });
    }

    public void signIn (String username, String password) {
        MY_FIREBASEAUTH.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signIn:success");
                            FirebaseUser user = MY_FIREBASEAUTH.getCurrentUser();
                            updateUI(user);
                        } else {
                            Log.w(TAG, "signIn:failure", task.getException());
                            updateUI(null);
                        }
                    }
                });
    }

    public void signOut () {
        MY_FIREBASEAUTH.signOut();
    }

}
