package com.example.classroomannouncement.Database.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.classroomannouncement.Database.AppDatabase;
import com.example.classroomannouncement.Database.Daos.AnnouncementDao;
import com.example.classroomannouncement.Database.Entities.Announcement;

import java.util.List;

public class AnnouncementRepo {
    private final AnnouncementDao announcementDao;
    private final LiveData<List<Announcement>> activeAnnouncements;
    private final LiveData<List<Announcement>> allAnnouncements;

    public AnnouncementRepo(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        announcementDao = db.announcementDao();

        // Auto-expire old announcements on initialization
        AppDatabase.databaseWriteExecutor.execute(() -> {
            announcementDao.expireOldAnnouncements(System.currentTimeMillis());
        });

        activeAnnouncements = announcementDao.getActiveAnnouncements(System.currentTimeMillis());
        allAnnouncements = announcementDao.getAllAnnouncements();
    }

    public LiveData<List<Announcement>> getActiveAnnouncements() {
        return activeAnnouncements;
    }

    public LiveData<List<Announcement>> getAllAnnouncements() {
        return allAnnouncements;
    }

    public LiveData<Announcement> getAnnouncementById(int id) {
        return announcementDao.getAnnouncementById(id);
    }

    public void insert(Announcement announcement) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            announcementDao.insert(announcement);
        });
    }

    public void update(Announcement announcement) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            announcementDao.update(announcement);
        });
    }

    public void delete(Announcement announcement) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            announcementDao.delete(announcement);
        });
    }

    public void markAsExpired(int announcementId) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            announcementDao.markAsExpired(announcementId, System.currentTimeMillis());
        });
    }
}