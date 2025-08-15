package com.example.classroomannouncement.Database.Repositories;

import android.content.Context;
import com.example.classroomannouncement.Database.AppDatabase;
import com.example.classroomannouncement.Database.Daos.AnalyticsDao;
import com.example.classroomannouncement.Database.Entities.Analytics;
import java.util.List;

public class AnalyticsRepo {
    private final AnalyticsDao analyticsDao;

    public AnalyticsRepo(Context context) {
        AppDatabase db = AppDatabase.getDatabase(context);
        this.analyticsDao = db.analyticsDao();
    }

    public List<Analytics> getAllAnalytics() {
        return analyticsDao.getAllAnalytics();
    }

    public void insertAnalytics(Analytics analytics) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            analyticsDao.insert(analytics);
        });
    }

    public void clearAllAnalytics() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            analyticsDao.clearAll();
        });
    }
}