package com.example.sprintproject.viewmodels;

import android.text.TextUtils;

import androidx.lifecycle.ViewModel;

import java.util.Date;

public class ValidateViewModel extends ViewModel {

    public boolean validateLogin(String username, String password) {
        return !TextUtils.isEmpty(username) && !TextUtils.isEmpty(password);
    }

    public boolean validateRegistration(String username, String password) {
        return !TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)
                && !username.contains(" ") && !password.contains(" ")
                && username.length() >= 3 && password.length() >= 6; // Added length checks
    }

    public boolean validateDate(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return false; // Check for null dates
        }
        return endDate.after(startDate);
    }
}

