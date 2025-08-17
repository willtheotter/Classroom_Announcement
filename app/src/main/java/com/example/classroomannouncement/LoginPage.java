package com.example.classroomannouncement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.classroomannouncement.Database.AppDatabase;
import com.example.classroomannouncement.Database.Entities.User;
import com.example.classroomannouncement.viewmodels.UserViewModel;

public class LoginPage extends AppCompatActivity {

    // UI Components
    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView goToSignupLink;
    private UserViewModel userViewModel;

    // Constants
    public static final String EXTRA_NAME = "USER_NAME";
    public static final String EXTRA_EMAIL = "USER_EMAIL";
    public static final String EXTRA_IS_ADMIN = "IS_ADMIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_page);

        // Ensure admin account exists
        AppDatabase.verifyAdminAccount(this);

        // Handle window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize ViewModel
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        initializeViews();
        setupListeners();
    }

    private void initializeViews() {
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        goToSignupLink = findViewById(R.id.goToSignupLink);
    }

    private void setupListeners() {
        loginButton.setOnClickListener(v -> attemptLogin());
        goToSignupLink.setOnClickListener(v -> navigateToSignup());
    }

    private void attemptLogin() {
        String email = emailEditText.getText().toString().trim().toLowerCase();
        String password = passwordEditText.getText().toString().trim();

        if (!validateInputs(email, password)) return;

        loginButton.setEnabled(false);

        userViewModel.getUserByEmailLive(email).observe(this, user -> {
            if (user != null && user.getPassword().equals(password)) {
                handleSuccessfulLogin(user);
            } else {
                handleLoginFailure(user == null ? "User not found" : "Invalid password");
            }
        });
    }

    private boolean validateInputs(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            showToast("Please fill all fields");
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showToast("Please enter a valid email");
            return false;
        }

        return true;
    }

    private void handleSuccessfulLogin(User user) {
        // Determine destination based on admin status
        Class<?> destination = user.isAdmin() ? AdminLandingPage.class : LandingPage.class;

        // Create intent with user data
        Intent intent = new Intent(this, destination);
        intent.putExtra(EXTRA_NAME, user.getName());
        intent.putExtra(EXTRA_EMAIL, user.getEmail());
        intent.putExtra(EXTRA_IS_ADMIN, user.isAdmin());

        // Save to SharedPreferences
        saveUserToPrefs(user.getName(), user.getEmail(), user.isAdmin());

        startActivity(intent);
        finish();
    }

    private void saveUserToPrefs(String name, String email, boolean isAdmin) {
        getSharedPreferences("UserPrefs", MODE_PRIVATE).edit()
                .putString(EXTRA_NAME, name)
                .putString(EXTRA_EMAIL, email)
                .putBoolean(EXTRA_IS_ADMIN, isAdmin)
                .apply();
    }

    private void handleLoginFailure(String error) {
        showToast(error);
        loginButton.setEnabled(true);
    }

    private void navigateToSignup() {
        startActivity(new Intent(this, SignupPage.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}