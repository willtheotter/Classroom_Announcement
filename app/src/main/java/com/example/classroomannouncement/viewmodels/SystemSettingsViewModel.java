package com.example.classroomannouncement.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.classroomannouncement.Database.Entities.SystemSettings;
import com.example.classroomannouncement.Database.Repositories.SystemSettingsRepo;

public class SystemSettingsViewModel extends AndroidViewModel {
    private SystemSettingsRepo repository;
    private LiveData<SystemSettings> systemSettings;

    public SystemSettingsViewModel(Application application) {
        super(application);
        repository = new SystemSettingsRepo(application);
        systemSettings = repository.getSystemSettings();
    }

    public LiveData<SystemSettings> getSystemSettings() {
        return systemSettings;
    }

    public void updateSettings(int announcementDuration, int maxUsers) {
        repository.updateSettings(announcementDuration, maxUsers);
    }
}