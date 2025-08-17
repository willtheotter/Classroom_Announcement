package com.example.classroomannouncement;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
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
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class AdminLandingPage extends AppCompatActivity {

    private AnnouncementViewModel announcementViewModel;
    private AnnouncementAdapter announcementAdapter;
    private boolean isAdmin;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable edge-to-edge first
        EdgeToEdge.enable(this);

        // Set content view before any view operations
        setContentView(R.layout.activity_admin_landing_page);

        // Setup edge-to-edge after content view is set
        setupEdgeToEdge();

        // Initialize UI and ViewModel
        initializeComponents();
        setupRecyclerView();
        setupNavigationListeners();
        setupToolbarNavigation();  // Added for back arrow navigation
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
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
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

    private void setupToolbarNavigation() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            // Navigate back to MainActivity
            Intent intent = new Intent(AdminLandingPage.this, LoginPage.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();

            // Add slide animation
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        });
    }

    private void setupNavigationListeners() {
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

        // Bottom navigation click listener
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                // Already on home, do nothing or refresh
                return true;
            } else if (id == R.id.nav_create) {
                startActivity(new Intent(AdminLandingPage.this, CreateAnnouncementPage.class));
                return true;
            } else if (id == R.id.nav_dashboard) {
                startActivity(new Intent(AdminLandingPage.this, AdminDashboardActivity.class));
                return true;
            } else if (id == R.id.nav_shop) {
            startActivity(new Intent(AdminLandingPage.this, AdminProductListActivity.class));
            return true;
        }

            return false;
        });
    }

    private void navigateToViewAnnouncement(Announcement announcement) {
        Intent intent = new Intent(this, AdminViewAnnouncementPage.class);
        intent.putExtra("announcement_id", announcement.getId());
        intent.putExtra("isAdmin", isAdmin);
        startActivity(intent);
    }

    private void updateAdminUI() {
        if (bottomNavigationView != null) {
            Menu menu = bottomNavigationView.getMenu();
            menu.findItem(R.id.nav_create).setVisible(true);
            menu.findItem(R.id.nav_dashboard).setVisible(true);
        }
    }
}