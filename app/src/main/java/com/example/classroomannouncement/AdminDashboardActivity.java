// AdminDashboardActivity.java
package com.example.classroomannouncement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class AdminDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        Button manageUsersButton = findViewById(R.id.manageUsersButton);
        Button systemSettingsButton = findViewById(R.id.systemSettingsButton);
        Button analyticsButton = findViewById(R.id.analyticsButton);
        View backButton = findViewById(R.id.backButton);

        manageUsersButton.setOnClickListener(v ->
                startActivity(new Intent(this, ManageUsersActivity.class)));

        systemSettingsButton.setOnClickListener(v ->
                startActivity(new Intent(this, SystemSettingsActivity.class)));

        analyticsButton.setOnClickListener(v ->
                startActivity(new Intent(this, AnalyticsActivity.class)));

        backButton.setOnClickListener(v -> finish());
    }
}