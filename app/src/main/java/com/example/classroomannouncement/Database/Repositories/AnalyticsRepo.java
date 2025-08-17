package com.example.classroomannouncement.Database.Repositories;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.example.classroomannouncement.Database.AppDatabase;
import com.example.classroomannouncement.Database.Daos.AnalyticsDao;
import com.example.classroomannouncement.Database.Entities.Analytics;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AnalyticsRepo {
    private final AnalyticsDao analyticsDao;
    private final ExecutorService executor;

    public AnalyticsRepo(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        analyticsDao = db.analyticsDao();
        executor = Executors.newSingleThreadExecutor();
    }

    public List<Analytics> getAllAnalytics() {
        return analyticsDao.getAllAnalytics();
    }

    public LiveData<List<Analytics>> getAllAnalyticsLive() {
        return analyticsDao.getAllAnalyticsLive();
    }

    public List<Analytics> getProductViews(int productId) {
        return analyticsDao.getProductViews(productId);
    }

    public int getTotalViewsForProduct(int productId) {
        Integer count = analyticsDao.getTotalViewsForProduct(productId);
        return count != null ? count : 0;
    }

    public void insertAnalytics(Analytics analytics) {
        executor.execute(() -> analyticsDao.insert(analytics));
    }

    public void updateAnalytics(Analytics analytics) {
        executor.execute(() -> analyticsDao.update(analytics));
    }

    public void clearAllAnalytics() {
        executor.execute(analyticsDao::clearAll);
    }
}