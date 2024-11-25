package com.example.sprintproject.views;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.sprintproject.R;
import com.google.firebase.auth.FirebaseAuth;


public class AddUserActivity extends AppCompatActivity {

    private EditText emailEditText;
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_users_form);
        emailEditText = findViewById(R.id.emailenterable);
        Button addUserButton = findViewById(R.id.adduserbtn);
        addUserButton.setOnClickListener(v -> validateEmail());
        Button exitButton = findViewById(R.id.exitbtn);
        exitButton.setOnClickListener(v -> {
            finish();
        });
    }

    /**
     * Validates the entered email address. Checks for non-empty input and proper email format.
     * Displays appropriate error messages or a success toast message.
     */
    private void validateEmail() {
        String email = emailEditText.getText().toString().trim();

        if (email.isEmpty()) {
            emailEditText.setError("Email is required");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Enter a valid email address");
            return;
        }

        Toast.makeText(this, "Valid email entered!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Checks if the provided email address is already registered in Firebase Authentication.
     *
     * @param email    the email address to check
     * @param callback the callback to handle the result, indicating whether the user exists
     */
    public void isUserAdded(String email, MainActivity.OnUserAddedCallback callback) {
        firebaseAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(task -> {
            boolean userExists = task.isSuccessful() && task.getResult()
                    .getSignInMethods() != null
                    && !task.getResult().getSignInMethods().isEmpty();
            callback.onCallback(userExists);
        });
    }
}
