package com.example.classroomannouncement;

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

        // Setup toolbar
        findViewById(R.id.backButton).setOnClickListener(v -> finish());

        // Initialize ViewModel
        settingsViewModel = new ViewModelProvider(this).get(SystemSettingsViewModel.class);

        // Get references to UI elements
        EditText announcementDuration = findViewById(R.id.announcementDuration);
        EditText maxUsers = findViewById(R.id.maxUsers);
        Button saveButton = findViewById(R.id.saveSettingsButton);

        // Load current settings - using proper getter methods
        settingsViewModel.getSystemSettings().observe(this, settings -> {
            if (settings != null) {
                announcementDuration.setText(String.valueOf(settings.getAnnouncementDuration()));
                maxUsers.setText(String.valueOf(settings.getMaxUsers()));
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
                if (announcementDuration.getText().toString().isEmpty()) {
                    announcementDuration.setError("Required field");
                }
                if (maxUsers.getText().toString().isEmpty()) {
                    maxUsers.setError("Required field");
                }
            }
        });
    }
}