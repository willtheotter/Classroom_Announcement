package com.example.classroomannouncement;

import android.app.Application;
import android.content.Intent;
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

import com.example.classroomannouncement.Database.AppDatabase; // ✅ Added this import
import com.example.classroomannouncement.Database.Entities.User;
import com.example.classroomannouncement.viewmodels.UserViewModel;

public class MainActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView goToSignupLink;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // ✅ Ensure admin account exists
        AppDatabase.verifyAdminAccount(this);  // <--- KEY FIX

        // Handle window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize ViewModel
        Application application = (Application) getApplicationContext();
        userViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(application))
                .get(UserViewModel.class);

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
        Class<?> destination = user.isAdmin() ? LandingPage.class : StudentHomePage.class;
        Intent intent = new Intent(this, destination);
        intent.putExtra("user_email", user.getEmail());
        intent.putExtra("roleLabel", user.isAdmin() ? "Admin" : "Student");
        startActivity(intent);
        finish();
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
