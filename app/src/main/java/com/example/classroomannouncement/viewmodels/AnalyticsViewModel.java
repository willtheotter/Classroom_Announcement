package com.example.classroomannouncement.viewmodels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.classroomannouncement.Database.Repositories.AnalyticsRepo;
import com.example.classroomannouncement.Database.Entities.Analytics;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AnalyticsViewModel extends AndroidViewModel {
    private final AnalyticsRepo analyticsRepo;
    private final ExecutorService executor;
    private final MutableLiveData<List<Analytics>> analyticsData = new MutableLiveData<>();

    public AnalyticsViewModel(Application application) {
        super(application);
        analyticsRepo = new AnalyticsRepo(application.getApplicationContext());
        executor = Executors.newSingleThreadExecutor();
        loadAnalyticsData();
    }

    public LiveData<List<Analytics>> getAnalyticsData() {
        return analyticsData;
    }

    private void loadAnalyticsData() {
        executor.execute(() -> {
            List<Analytics> analytics = analyticsRepo.getAllAnalytics();
            analyticsData.postValue(analytics);
        });
    }

    public void clearAllAnalytics() {
        executor.execute(() -> {
            analyticsRepo.clearAllAnalytics();
            loadAnalyticsData();
        });
    }

    public void trackEvent(String eventType, String details) {
        executor.execute(() -> {
            // Create new Analytics object with all required fields
            Analytics analytics = new Analytics(
                    eventType,
                    details,
                    System.currentTimeMillis(),
                    null // or get current user ID if available
            );

            analyticsRepo.insertAnalytics(analytics);
            loadAnalyticsData();
        });
    }

    public void trackEvent(String eventType, String details, Integer userId) {
        executor.execute(() -> {
            Analytics analytics = new Analytics(
                    eventType,
                    details,
                    System.currentTimeMillis(),
                    userId
            );

            analyticsRepo.insertAnalytics(analytics);
            loadAnalyticsData();
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdown();
    }
}