package com.example.sprintproject.viewmodels;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.sprintproject.model.AuthRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;
import java.util.concurrent.Executor;

public class AuthViewModel {
    private final FirebaseAuth MY_FIREBASEAUTH = AuthRepository.createAuthRepository();
    private static final String TAG = "UsernamePassword";
    private final MutableLiveData<Boolean> IS_AUTHENTICATED = new MutableLiveData<>();

    public LiveData<Boolean> getAuthenticationStatus() {
        return IS_AUTHENTICATED;
    }

    public interface Callback {
        void onSuccess(FirebaseUser user);
        void onFailure(String error);
    }

    public void checkCurrentUser() {
        FirebaseUser currentUser = MY_FIREBASEAUTH.getCurrentUser();
        if (currentUser != null) {
            currentUser.reload();
            IS_AUTHENTICATED.setValue(true);
        } else {
            IS_AUTHENTICATED.setValue(false);
        }
    }

    public FirebaseUser getCurrentUser() {
        return MY_FIREBASEAUTH.getCurrentUser();
    }

    public void createUser(String username, String password, Callback callback) {
        MY_FIREBASEAUTH.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = MY_FIREBASEAUTH.getCurrentUser();
                            callback.onSuccess(user);
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            callback.onFailure(Objects.requireNonNull(task.getException()).getMessage());
                        }
                    }
                });
    }

    public void signIn(String username, String password, Callback callback) {
        MY_FIREBASEAUTH.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = MY_FIREBASEAUTH.getCurrentUser();
                            callback.onSuccess(user);
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            callback.onFailure(Objects.requireNonNull(task.getException()).getMessage());
                        }
                    }
                });
    }

    public void signOut () {
        MY_FIREBASEAUTH.signOut();
        IS_AUTHENTICATED.setValue(false);
    }

}
