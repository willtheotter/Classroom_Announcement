package com.example.classroomannouncement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.classroomannouncement.viewmodels.SystemSettingsViewModel;

public class SystemSettingsActivity extends AppCompatActivity {
    private SystemSettingsViewModel settingsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_settings);

        // Setup navigation buttons
        Button adminDashboardButton = findViewById(R.id.adminDashboardButton);
        Button systemSettingsButton = findViewById(R.id.systemSettingsButton);

        // Set button states - current activity should be highlighted/disabled
        systemSettingsButton.setEnabled(false);
        adminDashboardButton.setEnabled(true);

        // Navigation handlers
        adminDashboardButton.setOnClickListener(v -> {
            startActivity(new Intent(this, AdminDashboardActivity.class));
            finish();
        });

        systemSettingsButton.setOnClickListener(v -> {
            // Already in SystemSettings, no action needed
        });

        // Initialize ViewModel
        settingsViewModel = new ViewModelProvider(this).get(SystemSettingsViewModel.class);

        // Get references to UI elements
        EditText announcementDuration = findViewById(R.id.announcementDuration);
        EditText maxUsers = findViewById(R.id.maxUsers);
        Button saveButton = findViewById(R.id.saveSettingsButton);

        // Load current settings
        settingsViewModel.getSystemSettings().observe(this, settings -> {
            if (settings != null) {
                announcementDuration.setText(String.valueOf(settings.getAnnouncement_duration()));
                maxUsers.setText(String.valueOf(settings.getMax_users()));
            }
        });

        // Save button handler
        saveButton.setOnClickListener(v -> {
            try {
                int duration = Integer.parseInt(announcementDuration.getText().toString());
                int users = Integer.parseInt(maxUsers.getText().toString());
                settingsViewModel.updateSettings(duration, users);
                finish();
            } catch (NumberFormatException e) {
                announcementDuration.setError("Invalid number");
            }
        });
    }
}