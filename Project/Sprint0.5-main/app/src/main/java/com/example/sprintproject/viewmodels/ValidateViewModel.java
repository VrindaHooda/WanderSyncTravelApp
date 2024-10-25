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
                && !username.contains(" ") && !password.contains(" ");
    }

    public boolean validateDate(Date startDate, Date endDate) {
        return endDate.after(startDate);
    }
}
