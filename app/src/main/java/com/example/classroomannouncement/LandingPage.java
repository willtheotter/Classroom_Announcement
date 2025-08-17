package com.example.classroomannouncement;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

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

public class LandingPage extends AppCompatActivity {

    // Constants
    private static final String PREFS_NAME = "UserPrefs";
    private static final String KEY_NAME = "USER_NAME";
    private static final String KEY_EMAIL = "USER_EMAIL";
    private static final String KEY_IS_ADMIN = "IS_ADMIN";

    // UI Components
    private TextView welcomeText, roleText;
    private BottomNavigationView bottomNavigationView;
    private boolean isAdmin;

    // ViewModels and Adapters
    private AnnouncementViewModel announcementViewModel;
    private AnnouncementAdapter announcementAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        // Initialize Edge-to-Edge
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        displayUserInfo();
        setupRecyclerView();
        setupNavigation();
        setupToolbar();
    }

    private void initializeViews() {
        welcomeText = findViewById(R.id.welcomeText);
        roleText = findViewById(R.id.roleText);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        announcementViewModel = new ViewModelProvider(this).get(AnnouncementViewModel.class);

        // Get admin status from intent
        Intent intent = getIntent();
        isAdmin = intent.getBooleanExtra(KEY_IS_ADMIN, false);
    }

    private void displayUserInfo() {
        // Get user data from intent
        Intent intent = getIntent();
        String userName = intent.getStringExtra(KEY_NAME);
        String userEmail = intent.getStringExtra(KEY_EMAIL);

        // Set defaults if null
        if (userName == null || userName.isEmpty()) {
            userName = "User";
        }

        // Display user info
        welcomeText.setText("Welcome, " + userName);
        roleText.setText("Logged in as " + (isAdmin ? "Admin" : "Student"));

        // Save to SharedPreferences for future use
        saveUserToPrefs(userName, userEmail, isAdmin);
    }

    private void saveUserToPrefs(String name, String email, boolean isAdmin) {
        getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit()
                .putString(KEY_NAME, name)
                .putString(KEY_EMAIL, email)
                .putBoolean(KEY_IS_ADMIN, isAdmin)
                .apply();
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.announcementsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        announcementAdapter = new AnnouncementAdapter(new ArrayList<>(), isAdmin);
        announcementAdapter.setOnItemClickListener(new AnnouncementAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Announcement announcement) {
                // Handle announcement click
                Intent intent = new Intent(LandingPage.this, ViewAnnouncementActivity.class);
                intent.putExtra("announcement_id", announcement.getId());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(Announcement announcement) {
                // Handle delete if admin
                if (isAdmin) {
                    announcementViewModel.delete(announcement);
                }
            }
        });

        recyclerView.setAdapter(announcementAdapter);

        announcementViewModel.getAllAnnouncements().observe(this, announcements -> {
            if (announcements != null) {
                announcementAdapter.setAnnouncements(announcements);
            }
        });
    }

    private void setupNavigation() {
        // Set up the correct menu based on user role
        bottomNavigationView.getMenu().clear();
        bottomNavigationView.inflateMenu(isAdmin ? R.menu.bottom_nav_menu : R.menu.bottom_nav_menu_student);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                // Already on home
                return true;
            } else if (id == R.id.nav_chat) {
                startActivity(new Intent(this, ChatActivity.class));
                return true;
            } else if (id == R.id.nav_shop) {
                startActivity(new Intent(this, ProductListActivity.class));
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, UserDashboardActivity.class));
                return true;
            }
            return false;
        });

        // Hide dashboard if not admin
        if (!isAdmin) {
            Menu menu = bottomNavigationView.getMenu();
            MenuItem dashboardItem = menu.findItem(R.id.nav_dashboard);
            if (dashboardItem != null) {
                dashboardItem.setVisible(false);
            }
        }
    }

    private void setupToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> navigateToLogin());
    }

    private void navigateToLogin() {
        // Clear user data on logout
        getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit().clear().apply();

        Intent intent = new Intent(this, LoginPage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}