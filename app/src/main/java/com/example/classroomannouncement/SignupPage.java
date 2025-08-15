package com.example.classroomannouncement;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.classroomannouncement.Database.Entities.User;
import com.example.classroomannouncement.viewmodels.UserViewModel;

public class SignupPage extends AppCompatActivity {

    private EditText fullNameEditText, signupEmailEditText, signupPasswordEditText;
    private Button signupButton;
    private TextView goToLoginLink;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);

        // Initialize ViewModel with Application context
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

        if (!validateInputs(name, email, password)) {
            return;
        }

        signupButton.setEnabled(false);

        // Check email existence using LiveData observer
        userViewModel.getUserByEmailLive(email).observe(this, user -> {
            if (user != null) {
                // Email exists
                signupButton.setEnabled(true);
                Toast.makeText(this, "Email already registered", Toast.LENGTH_SHORT).show();
            } else {
                // Email available, proceed with registration
                User newUser = new User(name, email, password, false);
                userViewModel.insert(newUser);
                navigateToHome(email);
            }
        });
    }

    private boolean validateInputs(String name, String email, String password) {
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void navigateToHome(String email) {
        Intent intent = new Intent(SignupPage.this, StudentHomePage.class);
        intent.putExtra("user_email", email);
        startActivity(intent);
        finishAffinity(); // Clear back stack
    }

    private void navigateToLogin() {
        startActivity(new Intent(SignupPage.this, MainActivity.class));
        finish();
    }
}