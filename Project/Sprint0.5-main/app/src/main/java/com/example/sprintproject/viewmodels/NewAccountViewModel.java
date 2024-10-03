package com.example.sprintproject.viewmodels;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.sprintproject.model.User;
import com.example.sprintproject.model.UserRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

public class NewAccountViewModel {
    private final DatabaseReference MY_DATABASE_REFERENCE = UserRepository.createUserRepository();

    public String getUserId(){
        String userId = String.valueOf(
                MY_DATABASE_REFERENCE.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                        }
                        else {
                            Log.d("firebase", String.valueOf(task.getResult().getValue()));
                        }
                    }
                }));
        int userIdValue = Integer.parseInt(userId) + 1;
        return String.valueOf(userIdValue);
    }

    public void writeNewUser(String userId, String username, String password) {
        User user = new User(username, password);
        MY_DATABASE_REFERENCE.child(userId).setValue(user);
    }

}
