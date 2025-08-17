package com.example.classroomannouncement;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.example.classroomannouncement.Database.Entities.Announcement;
import com.example.classroomannouncement.viewmodels.AnnouncementViewModel;

public class ViewAnnouncementActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView announcementTitle;
    private TextView announcementAuthor;
    private TextView announcementDate;
    private TextView announcementContent;
    private int currentAnnouncementId;
    private AnnouncementViewModel announcementViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_announcement);

        initViews();
        setupToolbar();
        setupViewModel();

        // Get announcement ID from intent
        currentAnnouncementId = getIntent().getIntExtra("announcement_id", -1);
        if (currentAnnouncementId == -1) {
            showErrorAndFinish("Invalid announcement");
            return;
        }

        // Load and display announcement data
        loadAnnouncementData();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        announcementTitle = findViewById(R.id.announcementTitle);
        announcementAuthor = findViewById(R.id.announcementAuthor);
        announcementDate = findViewById(R.id.announcementDate);
        announcementContent = findViewById(R.id.announcementContent);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void setupViewModel() {
        announcementViewModel = new ViewModelProvider(this).get(AnnouncementViewModel.class);
    }

    private void loadAnnouncementData() {
        announcementViewModel.getAnnouncementById(currentAnnouncementId).observe(this, announcement -> {
            if (announcement == null) {
                showErrorAndFinish("Announcement not found");
                return;
            }
            displayAnnouncement(announcement);
        });
    }

    private void displayAnnouncement(Announcement announcement) {
        announcementTitle.setText(announcement.getTitle());
        announcementContent.setText(announcement.getContent());
        announcementDate.setText(announcement.getFormattedDate());

        // Set author information if available (you'll need to add this field to your Announcement entity)
        // announcementAuthor.setText("Posted by: " + announcement.getAuthor());

        // For now, we'll use a placeholder
        announcementAuthor.setText("Posted by: Admin");
    }

    private void showErrorAndFinish(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}