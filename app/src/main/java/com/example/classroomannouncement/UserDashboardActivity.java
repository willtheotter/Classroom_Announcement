package com.example.classroomannouncement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.MaterialToolbar;
import com.example.classroomannouncement.viewmodels.UserViewModel;

public class UserDashboardActivity extends AppCompatActivity {
    private UserViewModel userViewModel;
    private String currentUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        // Setup toolbar with back button
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Button editProfileButton = findViewById(R.id.editProfileButton);
        Button viewUsersButton = findViewById(R.id.viewUsersButton);
        TextView welcomeText = findViewById(R.id.welcomeText);

        // Get current user email
        currentUserEmail = getIntent().getStringExtra("USER_EMAIL");

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        if (currentUserEmail != null) {
            userViewModel.loadCurrentUser(currentUserEmail);
            userViewModel.getCurrentUser().observe(this, user -> {
                if (user != null) {
                    welcomeText.setText("Welcome, " + user.getName() + "!");
                }
            });
        }

        editProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditProfileActivity.class);
            intent.putExtra("CURRENT_EMAIL", currentUserEmail);
            startActivityForResult(intent, 1);
        });

        viewUsersButton.setOnClickListener(v ->
                startActivity(new Intent(this, ViewUsersActivity.class)));
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && currentUserEmail != null) {
            userViewModel.loadCurrentUser(currentUserEmail);
        }
    }
}
