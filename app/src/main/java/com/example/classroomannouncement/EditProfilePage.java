package com.example.classroomannouncement;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.classroomannouncement.Database.Entities.User;
import com.example.classroomannouncement.Database.Repositories.UserRepo;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class EditProfilePage extends AppCompatActivity {

    private EditText nameEditText, currentPasswordEditText,
            newPasswordEditText, confirmPasswordEditText;
    private Button saveButton;
    private UserRepo userRepo;
    private final Executor executor = Executors.newSingleThreadExecutor();
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Initialize views
        nameEditText = findViewById(R.id.nameEditText);
        currentPasswordEditText = findViewById(R.id.currentPasswordEditText);
        newPasswordEditText = findViewById(R.id.newPasswordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        saveButton = findViewById(R.id.saveButton);

        // Initialize UserRepo
        userRepo = new UserRepo(this);

        // Get current user data (you'll need to pass this from previous activity)
        String userEmail = getIntent().getStringExtra("user_email");
        if (userEmail != null) {
            loadUserData(userEmail);
        } else {
            Toast.makeText(this, "User not identified", Toast.LENGTH_SHORT).show();
            finish();
        }

        saveButton.setOnClickListener(v -> saveProfileChanges());
    }

    private void loadUserData(String email) {
        executor.execute(() -> {
            currentUser = userRepo.getUserByEmail(email);
            runOnUiThread(() -> {
                if (currentUser != null) {
                    nameEditText.setText(currentUser.getName());
                } else {
                    Toast.makeText(this, "Failed to load user data", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        });
    }

    private void saveProfileChanges() {
        String newName = nameEditText.getText().toString().trim();
        String currentPassword = currentPasswordEditText.getText().toString().trim();
        String newPassword = newPasswordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        // Validate inputs
        if (newName.isEmpty()) {
            nameEditText.setError("Name cannot be empty");
            return;
        }

        if (currentPassword.isEmpty()) {
            currentPasswordEditText.setError("Enter current password");
            return;
        }

        if (!currentPassword.equals(currentUser.getPassword())) {
            currentPasswordEditText.setError("Incorrect password");
            return;
        }

        if (!newPassword.isEmpty()) {
            if (newPassword.length() < 6) {
                newPasswordEditText.setError("Password must be at least 6 characters");
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                confirmPasswordEditText.setError("Passwords don't match");
                return;
            }
            currentUser.setPassword(newPassword);
        }

        currentUser.setName(newName);

        executor.execute(() -> {
            userRepo.updateUser(currentUser);
            runOnUiThread(() -> {
                Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            });
        });
    }
}