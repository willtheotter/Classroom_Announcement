package com.example.classroomannouncement;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.classroomannouncement.adapters.AnalyticsAdapter;
import com.example.classroomannouncement.Database.Entities.Analytics;
import com.example.classroomannouncement.viewmodels.AnalyticsViewModel;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AnalyticsActivity extends AppCompatActivity {
    private AnalyticsViewModel analyticsViewModel;
    private AnalyticsAdapter analyticsAdapter;
    private RecyclerView analyticsRecyclerView;
    private Button clearAnalyticsButton;
    private TextView totalViewsTextView;
    private TextView uniqueProductsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);

        // Initialize ViewModel
        analyticsViewModel = new ViewModelProvider(this).get(AnalyticsViewModel.class);

        // Initialize UI components
        analyticsRecyclerView = findViewById(R.id.analyticsRecyclerView);
        clearAnalyticsButton = findViewById(R.id.clearAnalyticsButton);
        totalViewsTextView = findViewById(R.id.totalViewsTextView);
        uniqueProductsTextView = findViewById(R.id.uniqueProductsTextView);

        // Setup RecyclerView
        setupRecyclerView();

        // Setup button click listeners
        setupButtonListeners();

        // Observe analytics data
        setupObservers();
    }

    private void setupRecyclerView() {
        analyticsAdapter = new AnalyticsAdapter();
        analyticsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        analyticsRecyclerView.setAdapter(analyticsAdapter);
    }

    private void setupButtonListeners() {
        clearAnalyticsButton.setOnClickListener(v -> {
            analyticsViewModel.clearAllAnalytics();
            Toast.makeText(this, "Analytics cleared", Toast.LENGTH_SHORT).show();
        });

        Button testEventButton = findViewById(R.id.testEventButton);
        testEventButton.setOnClickListener(v -> {
            analyticsViewModel.trackEvent("test", "Test button clicked");
            Toast.makeText(this, "Test event recorded", Toast.LENGTH_SHORT).show();
        });
    }

    private void setupObservers() {
        analyticsViewModel.getAnalyticsData().observe(this, analytics -> {
            if (analytics != null && !analytics.isEmpty()) {
                analyticsAdapter.setAnalyticsList(analytics);
                updateSummaryStatistics(analytics);
            } else {
                analyticsAdapter.setAnalyticsList(Collections.emptyList());
                totalViewsTextView.setText("Total Views: 0");
                uniqueProductsTextView.setText("Unique Products: 0");
            }
        });

        analyticsViewModel.getProductViewCount().observe(this, count -> {
            if (count != null) {
                totalViewsTextView.setText(String.format("Total Views: %d", count));
            }
        });
    }

    private void updateSummaryStatistics(List<Analytics> analytics) {
        int totalViews = 0;
        Set<Integer> productIds = new HashSet<>();

        for (Analytics item : analytics) {
            totalViews += item.getViewCount();
            if (item.getProductId() != null) {
                productIds.add(item.getProductId());
            }
        }

        totalViewsTextView.setText(String.format("Total Views: %d", totalViews));
        uniqueProductsTextView.setText(String.format("Unique Products: %d", productIds.size()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}