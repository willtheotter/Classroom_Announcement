package com.example.classroomannouncement.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.classroomannouncement.Database.Entities.Analytics;
import com.example.classroomannouncement.Database.Entities.Product;
import com.example.classroomannouncement.Database.Repositories.AnalyticsRepo;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AnalyticsViewModel extends AndroidViewModel {
    private final AnalyticsRepo analyticsRepo;
    private final ExecutorService executor;
    private final MutableLiveData<List<Analytics>> analyticsData = new MutableLiveData<>();
    private final MutableLiveData<Integer> productViewCount = new MutableLiveData<>();

    public AnalyticsViewModel(@NonNull Application application) {
        super(application);
        analyticsRepo = new AnalyticsRepo(application);
        executor = Executors.newSingleThreadExecutor();
        loadAnalyticsData();
    }

    public LiveData<List<Analytics>> getAnalyticsData() {
        return analyticsData;
    }

    public LiveData<Integer> getProductViewCount() {
        return productViewCount;
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
            Analytics analytics = new Analytics(
                    eventType,
                    1, // Default view count
                    details,
                    System.currentTimeMillis(),
                    null,
                    null,
                    null
            );
            analyticsRepo.insertAnalytics(analytics);
            loadAnalyticsData();
        });
    }

    public void trackEvent(String eventType, String details, Integer userId) {
        executor.execute(() -> {
            Analytics analytics = new Analytics(
                    eventType,
                    1,
                    details,
                    System.currentTimeMillis(),
                    userId,
                    null,
                    null
            );
            analyticsRepo.insertAnalytics(analytics);
            loadAnalyticsData();
        });
    }

    public void recordProductView(Product product, Integer userId) {
        executor.execute(() -> {
            List<Analytics> existingViews = analyticsRepo.getProductViews(product.getId());

            if (!existingViews.isEmpty()) {
                // Update existing view count
                Analytics existing = existingViews.get(0);
                existing.setViewCount(existing.getViewCount() + 1);
                existing.setTimestamp(System.currentTimeMillis());
                analyticsRepo.updateAnalytics(existing);
            } else {
                // Create new view record
                Analytics newView = new Analytics(
                        "view",
                        1,
                        "Viewed product: " + product.getName(),
                        System.currentTimeMillis(),
                        userId,
                        product.getId(),
                        product.getName()
                );
                analyticsRepo.insertAnalytics(newView);
            }

            // Update view count
            int totalViews = analyticsRepo.getTotalViewsForProduct(product.getId());
            productViewCount.postValue(totalViews);
            loadAnalyticsData();
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdown();
    }
}