package com.example.sprintproject.viewmodels;

import android.util.Log;
import androidx.annotation.NonNull;
import com.example.sprintproject.model.AuthRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.Objects;

public class AuthViewModel {
    private final FirebaseAuth myFIREBASEAUTH = AuthRepository.createAuthRepository();
    private static final String TAG = "UsernamePassword";
    private boolean isAuthenticated = false;

    public boolean getAuthenticationStatus() {
        return isAuthenticated;
    }

    public void checkCurrentUser() {
        FirebaseUser currentUser = myFIREBASEAUTH.getCurrentUser();
        if (currentUser != null) {
            currentUser.reload();
            isAuthenticated = true;
        } else {
            isAuthenticated = false;
        }
    }

    public FirebaseUser getCurrentUser() {
        return myFIREBASEAUTH.getCurrentUser();
    }

    public void createUser(String username, String password, Callback callback) {
        myFIREBASEAUTH.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = myFIREBASEAUTH.getCurrentUser();
                            callback.onSuccess(user);
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            callback.onFailure(Objects.requireNonNull(task.getException()).
                                    getMessage());
                        }
                    }
                });
    }

    public void signIn(String username, String password, Callback callback) {
        myFIREBASEAUTH.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = myFIREBASEAUTH.getCurrentUser();
                            callback.onSuccess(user);
                            isAuthenticated = true;
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            callback.onFailure(Objects.requireNonNull(task.getException()).
                                    getMessage());
                            isAuthenticated = false;
                        }
                    }
                });
    }

    public void signOut() {
        myFIREBASEAUTH.signOut();
        isAuthenticated = false;
    }

    public interface Callback {
        void onSuccess(FirebaseUser user);
        void onFailure(String error);
    }

}
