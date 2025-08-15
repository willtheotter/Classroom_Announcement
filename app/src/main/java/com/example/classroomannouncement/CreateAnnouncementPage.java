package com.example.classroomannouncement;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.classroomannouncement.Database.Entities.Announcement;
import com.example.classroomannouncement.viewmodels.AnnouncementViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Date;

public class CreateAnnouncementPage extends AppCompatActivity {

    private TextInputEditText announcementTitleInput;
    private TextInputEditText announcementBodyInput;
    private Button postButton;
    private AnnouncementViewModel announcementViewModel;
    private Date expiryDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_announcement_page);

        // Initialize ViewModel
        announcementViewModel = new ViewModelProvider(this).get(AnnouncementViewModel.class);

        // Initialize UI elements
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        announcementTitleInput = findViewById(R.id.announcementTitleInput);
        announcementBodyInput = findViewById(R.id.announcementBodyInput);
        postButton = findViewById(R.id.postButton);

        // Set up the toolbar
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        // Set default expiry date to 7 days from now
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 7);
        expiryDate = calendar.getTime();

        // Set up post button click listener
        postButton.setOnClickListener(this::postAnnouncement);
    }

    private void postAnnouncement(View view) {
        String title = announcementTitleInput.getText().toString().trim();
        String content = announcementBodyInput.getText().toString().trim();

        if (title.isEmpty()) {
            announcementTitleInput.setError("Title is required");
            announcementTitleInput.requestFocus();
            return;
        }

        if (content.isEmpty()) {
            announcementBodyInput.setError("Content is required");
            announcementBodyInput.requestFocus();
            return;
        }

        try {
            // Create new announcement with just title, content, and expiry date
            Announcement announcement = new Announcement(
                    title,
                    content,
                    expiryDate
            );

            // Insert using ViewModel
            announcementViewModel.insert(announcement);

            Toast.makeText(this, "Announcement posted successfully!", Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            Toast.makeText(this, "Error posting announcement: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}