package com.example.classroomannouncement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.classroomannouncement.Database.Entities.Announcement;
import com.example.classroomannouncement.adapters.AnnouncementAdapter;
import com.example.classroomannouncement.viewmodels.AnnouncementViewModel;

import java.util.ArrayList;

public class StudentHomePage extends AppCompatActivity {

    // Constants for intent extras
    public static final String EXTRA_FULL_NAME = "fullName";
    public static final String EXTRA_ROLE = "roleLabel";
    public static final String EXTRA_EMAIL = "user_email";

    // UI Components
    private TextView welcomeText, roleText;
    private ImageButton settingsButton, editProfileButton;
    private RecyclerView announcementsRecyclerView;

    // Adapter and ViewModel
    private AnnouncementAdapter announcementAdapter;
    private AnnouncementViewModel announcementViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);

        initializeViews();
        setupUserInfo();
        setupRecyclerView();
        setupClickListeners();
    }

    private void initializeViews() {
        welcomeText = findViewById(R.id.studentWelcomeText);
        roleText = findViewById(R.id.roleText);
        announcementsRecyclerView = findViewById(R.id.announcementsRecyclerView);
        settingsButton = findViewById(R.id.settingsButton);
        editProfileButton = findViewById(R.id.editProfileButton);
    }

    private void setupUserInfo() {
        Intent intent = getIntent();
        String fullName = intent.getStringExtra(EXTRA_FULL_NAME);
        String role = intent.getStringExtra(EXTRA_ROLE);
        String email = intent.getStringExtra(EXTRA_EMAIL);

        // Set default values if null
        if (fullName == null || fullName.isEmpty()) {
            fullName = getString(R.string.default_student_name);
        }

        if (role == null || role.isEmpty()) {
            role = getString(R.string.default_role);
        }

        welcomeText.setText(getString(R.string.welcome_message, fullName));
        roleText.setText(getString(R.string.logged_in_as, role));
    }

    private void setupRecyclerView() {
        announcementAdapter = new AnnouncementAdapter(new ArrayList<>(), false);
        announcementsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        announcementsRecyclerView.setAdapter(announcementAdapter);

        announcementViewModel = new ViewModelProvider(this).get(AnnouncementViewModel.class);
        announcementViewModel.getAllAnnouncements().observe(this, announcements -> {
            announcementAdapter.setAnnouncements(announcements);
        });

        announcementAdapter.setOnItemClickListener(new AnnouncementAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Announcement announcement) {
                navigateToAnnouncementDetails(announcement);
            }

            @Override
            public void onDeleteClick(Announcement announcement) {
                // Empty implementation for student view
            }
        });
    }

    private void setupClickListeners() {
        settingsButton.setOnClickListener(v -> navigateToSettings());
        editProfileButton.setOnClickListener(v -> navigateToEditProfile());
    }

    private void navigateToAnnouncementDetails(Announcement announcement) {
        Intent intent = new Intent(this, ViewAnnouncementPage.class);
        intent.putExtra("announcement_id", announcement.getId());
        startActivity(intent);
    }

    private void navigateToSettings() {
        startActivity(new Intent(this, SettingsPage.class));
    }

    private void navigateToEditProfile() {
        Intent intent = new Intent(this, EditProfilePage.class);
        intent.putExtra(EXTRA_EMAIL, getIntent().getStringExtra(EXTRA_EMAIL));
        startActivity(intent);
    }
}