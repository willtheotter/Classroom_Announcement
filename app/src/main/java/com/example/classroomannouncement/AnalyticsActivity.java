package com.example.classroomannouncement;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.classroomannouncement.adapters.AnalyticsAdapter;
import com.example.classroomannouncement.viewmodels.AnalyticsViewModel;

public class AnalyticsActivity extends AppCompatActivity {
    private AnalyticsViewModel analyticsViewModel;
    private AnalyticsAdapter analyticsAdapter;
    private RecyclerView analyticsRecyclerView;
    private Button clearAnalyticsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);

        // Initialize ViewModel
        analyticsViewModel = new ViewModelProvider(this).get(AnalyticsViewModel.class);

        // Setup RecyclerView
        analyticsRecyclerView = findViewById(R.id.analyticsRecyclerView);
        analyticsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        analyticsAdapter = new AnalyticsAdapter();
        analyticsRecyclerView.setAdapter(analyticsAdapter);

        // Setup clear button
        clearAnalyticsButton = findViewById(R.id.clearAnalyticsButton);
        clearAnalyticsButton.setOnClickListener(v -> {
            analyticsViewModel.clearAllAnalytics();
            Toast.makeText(this, "Analytics cleared", Toast.LENGTH_SHORT).show();
        });

        // Observe analytics data
        analyticsViewModel.getAnalyticsData().observe(this, analytics -> {
            if (analytics != null) {
                analyticsAdapter.setAnalyticsList(analytics);
            }
        });
    }
}