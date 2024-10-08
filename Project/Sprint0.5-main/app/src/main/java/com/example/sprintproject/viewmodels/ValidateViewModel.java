package com.example.sprintproject.viewmodels;
import android.text.TextUtils;
public class ValidateViewModel {
    public boolean validateLogin(String username, String password) {
        return !TextUtils.isEmpty(username) && !TextUtils.isEmpty(password);
    }

    public boolean validateRegistration(String username, String password) {
        return !TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)
                && !username.contains(" ") && !password.contains(" ");
    }
}
