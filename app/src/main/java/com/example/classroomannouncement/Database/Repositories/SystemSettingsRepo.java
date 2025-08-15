package com.example.classroomannouncement.Database.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.classroomannouncement.Database.Daos.SystemSettingsDao;  // Fixed import
import com.example.classroomannouncement.Database.Entities.SystemSettings;
import com.example.classroomannouncement.Database.AppDatabase;

public class SystemSettingsRepo {
    private SystemSettingsDao systemSettingsDao;
    private LiveData<SystemSettings> systemSettings;

    public SystemSettingsRepo(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        systemSettingsDao = db.systemSettingsDao();
        systemSettings = systemSettingsDao.getSystemSettings();
    }

    public LiveData<SystemSettings> getSystemSettings() {
        return systemSettings;
    }

    public void updateSettings(int announcementDuration, int maxUsers) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            systemSettingsDao.updateSettings(announcementDuration, maxUsers);
        });
    }
}