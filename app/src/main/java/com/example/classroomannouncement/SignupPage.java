package com.example.classroomannouncement;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.classroomannouncement.Database.Entities.User;
import com.example.classroomannouncement.viewmodels.UserViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class SignupPage extends AppCompatActivity {

    // UI Components
    private TextInputEditText fullNameEditText, signupEmailEditText,
            signupPasswordEditText, confirmPasswordEditText;
    private TextInputLayout passwordInputLayout, confirmPasswordInputLayout;
    private Button signupButton;
    private TextView goToLoginLink;
    private UserViewModel userViewModel;

    // Constants
    public static final String EXTRA_NAME = "USER_NAME";
    public static final String EXTRA_EMAIL = "USER_EMAIL";
    public static final String EXTRA_IS_ADMIN = "IS_ADMIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);

        // Initialize ViewModel
        Application application = (Application) getApplicationContext();
        userViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(application))
                .get(UserViewModel.class);

        initializeViews();
        setupButtonListeners();
    }

    private void initializeViews() {
        fullNameEditText = findViewById(R.id.fullNameEditText);
        signupEmailEditText = findViewById(R.id.signupEmailEditText);
        signupPasswordEditText = findViewById(R.id.signupPasswordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        passwordInputLayout = findViewById(R.id.passwordInputLayout);
        confirmPasswordInputLayout = findViewById(R.id.confirmPasswordInputLayout);
        signupButton = findViewById(R.id.signupButton);
        goToLoginLink = findViewById(R.id.goToLoginLink);
    }

    private void setupButtonListeners() {
        signupButton.setOnClickListener(v -> attemptSignup());
        goToLoginLink.setOnClickListener(v -> navigateToLogin());
    }

    private void attemptSignup() {
        String name = fullNameEditText.getText().toString().trim();
        String email = signupEmailEditText.getText().toString().trim().toLowerCase();
        String password = signupPasswordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        // Reset error states
        passwordInputLayout.setError(null);
        confirmPasswordInputLayout.setError(null);

        if (!validateInputs(name, email, password, confirmPassword)) {
            return;
        }

        signupButton.setEnabled(false);

        // Check if email exists
        userViewModel.getUserByEmailLive(email).observe(this, user -> {
            if (user != null) {
                // Email exists
                signupButton.setEnabled(true);
                signupEmailEditText.setError("Email already registered");
                Toast.makeText(this, "Email already registered", Toast.LENGTH_SHORT).show();
            } else {
                // Create new user
                User newUser = new User(name, email, password, false);
                userViewModel.insert(newUser);
                navigateToHome(newUser);
            }
        });
    }

    private boolean validateInputs(String name, String email,
                                   String password, String confirmPassword) {
        boolean isValid = true;

        if (name.isEmpty()) {
            fullNameEditText.setError("Full name required");
            isValid = false;
        }

        if (email.isEmpty()) {
            signupEmailEditText.setError("Email required");
            isValid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            signupEmailEditText.setError("Invalid email format");
            isValid = false;
        }

        if (password.isEmpty()) {
            passwordInputLayout.setError("Password required");
            isValid = false;
        } else if (password.length() < 6) {
            passwordInputLayout.setError("Minimum 6 characters");
            isValid = false;
        }

        if (confirmPassword.isEmpty()) {
            confirmPasswordInputLayout.setError("Confirm password");
            isValid = false;
        } else if (!password.equals(confirmPassword)) {
            confirmPasswordInputLayout.setError("Passwords don't match");
            isValid = false;
        }

        return isValid;
    }

    private void navigateToHome(User user) {
        Intent intent = new Intent(this, LandingPage.class);
        intent.putExtra(EXTRA_NAME, user.getName());
        intent.putExtra(EXTRA_EMAIL, user.getEmail());
        intent.putExtra(EXTRA_IS_ADMIN, user.isAdmin());
        startActivity(intent);
        finishAffinity();
    }

    private void navigateToLogin() {
        startActivity(new Intent(this, LoginPage.class));
        finish();
    }
}