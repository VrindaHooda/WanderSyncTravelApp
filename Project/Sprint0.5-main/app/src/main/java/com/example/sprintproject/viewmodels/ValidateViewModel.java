package com.example.sprintproject.viewmodels;

import android.text.TextUtils;
import androidx.lifecycle.ViewModel;
import java.util.Date;

public class ValidateViewModel extends ViewModel {

    /**
     * Validates the login credentials by ensuring both username and password are not empty.
     *
     * @param username the username to validate
     * @param password the password to validate
     * @return {@code true} if both fields are non-empty; {@code false} otherwise
     */
    public boolean validateLogin(String username, String password) {
        return !TextUtils.isEmpty(username) && !TextUtils.isEmpty(password);
    }

    /**
     * Validates the registration details by ensuring
     * the username and password meet the required criteria:
     * <ul>
     *     <li>Both fields are non-empty</li>
     *     <li>Neither contains spaces</li>
     *     <li>Username has at least 3 characters</li>
     *     <li>Password has at least 6 characters</li>
     * </ul>
     *
     * @param username the username to validate
     * @param password the password to validate
     * @return {@code true} if the fields meet all criteria; {@code false} otherwise
     */
    public boolean validateRegistration(String username, String password) {
        return !TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)
                && !username.contains(" ") && !password.contains(" ")
                && username.length() >= 3 && password.length() >= 6;
    }

    /**
     * Validates a date range by ensuring the start date is before the end date.
     *
     * @param startDate the start date to validate
     * @param endDate   the end date to validate
     * @return {@code true} if both dates are non-null and the end date is after the start date;
     *         {@code false} otherwise
     */
    public boolean validateDate(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return false;
        }
        return endDate.after(startDate);
    }
}

