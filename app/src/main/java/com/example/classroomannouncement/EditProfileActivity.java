package com.example.classroomannouncement;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.classroomannouncement.Database.Entities.User;
import com.example.classroomannouncement.viewmodels.UserViewModel;

public class EditProfileActivity extends AppCompatActivity {
    private UserViewModel userViewModel;
    private EditText nameEditText, emailEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        Button saveButton = findViewById(R.id.saveButton);
        Button cancelButton = findViewById(R.id.cancelButton);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Load current user data
        String currentEmail = getIntent().getStringExtra("CURRENT_EMAIL");
        if (currentEmail != null) {
            userViewModel.loadCurrentUser(currentEmail);
            userViewModel.getCurrentUser().observe(this, user -> {
                if (user != null) {
                    nameEditText.setText(user.getName());
                    emailEditText.setText(user.getEmail());
                    // Password field intentionally left blank
                }
            });
        }

        saveButton.setOnClickListener(v -> updateProfile());
        cancelButton.setOnClickListener(v -> finish());
    }

    private void updateProfile() {
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Name and email cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        userViewModel.updateCurrentUser(name, email, password.isEmpty() ? null : password)
                .observe(this, success -> {
                    if (success) {
                        Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}