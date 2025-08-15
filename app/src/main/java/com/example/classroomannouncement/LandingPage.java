package com.example.classroomannouncement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.classroomannouncement.adapters.AnnouncementAdapter;
import com.example.classroomannouncement.Database.Entities.Announcement;
import com.example.classroomannouncement.viewmodels.AnnouncementViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class LandingPage extends AppCompatActivity {

    private AnnouncementViewModel announcementViewModel;
    private AnnouncementAdapter announcementAdapter;
    private boolean isAdmin;
    private FloatingActionButton createAnnouncementButton;
    private FloatingActionButton adminDashboardButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable edge-to-edge first
        EdgeToEdge.enable(this);

        // Set content view before any view operations
        setContentView(R.layout.activity_landing_page);

        // Setup edge-to-edge after content view is set
        setupEdgeToEdge();

        // Initialize UI and ViewModel
        initializeComponents();
        setupRecyclerView();
        setupClickListeners();
        updateAdminUI();
    }

    private void setupEdgeToEdge() {
        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }
    }

    private void initializeComponents() {
        isAdmin = getIntent().getBooleanExtra("isAdmin", false);
        createAnnouncementButton = findViewById(R.id.goToCreateAnnouncementButton);
        adminDashboardButton = findViewById(R.id.adminDashboardButton);
        announcementViewModel = new ViewModelProvider(this).get(AnnouncementViewModel.class);
    }

    private void setupRecyclerView() {
        RecyclerView announcementsRecyclerView = findViewById(R.id.announcementsRecyclerView);
        announcementsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        announcementAdapter = new AnnouncementAdapter(new ArrayList<>(), isAdmin);
        announcementsRecyclerView.setAdapter(announcementAdapter);

        announcementViewModel.getAllAnnouncements().observe(this, announcements -> {
            announcementAdapter.setAnnouncements(announcements);
        });
    }

    private void setupClickListeners() {
        // Announcement item click listener
        announcementAdapter.setOnItemClickListener(new AnnouncementAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Announcement announcement) {
                navigateToViewAnnouncement(announcement);
            }

            @Override
            public void onDeleteClick(Announcement announcement) {
                if (isAdmin) {
                    announcementViewModel.delete(announcement);
                }
            }
        });

        // FAB click listeners
        createAnnouncementButton.setOnClickListener(v ->
                startActivity(new Intent(LandingPage.this, CreateAnnouncementPage.class)));

        adminDashboardButton.setOnClickListener(v ->
                startActivity(new Intent(LandingPage.this, AdminDashboardActivity.class)));
    }

    private void navigateToViewAnnouncement(Announcement announcement) {
        Intent intent = new Intent(this, ViewAnnouncementPage.class);
        intent.putExtra("announcement_id", announcement.getId());
        intent.putExtra("isAdmin", isAdmin);
        startActivity(intent);
    }

    private void updateAdminUI() {
        int adminVisibility = isAdmin ? View.VISIBLE : View.GONE;
        createAnnouncementButton.setVisibility(adminVisibility);
        adminDashboardButton.setVisibility(adminVisibility);
    }
}