package com.example.classroomannouncement.viewmodels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.classroomannouncement.Database.Repositories.AnnouncementRepo;
import com.example.classroomannouncement.Database.Entities.Announcement;
import java.util.List;

public class AnnouncementViewModel extends AndroidViewModel {
    private final AnnouncementRepo announcementRepo;
    private final LiveData<List<Announcement>> allAnnouncements;

    public AnnouncementViewModel(Application application) {
        super(application);
        announcementRepo = new AnnouncementRepo(application);
        allAnnouncements = announcementRepo.getAllAnnouncements();
    }

    public LiveData<List<Announcement>> getAllAnnouncements() {
        return allAnnouncements;
    }

    public LiveData<List<Announcement>> getActiveAnnouncements() {
        return announcementRepo.getActiveAnnouncements();
    }

    public LiveData<Announcement> getAnnouncementById(int id) {
        return announcementRepo.getAnnouncementById(id);
    }

    public void insert(Announcement announcement) {
        announcementRepo.insert(announcement);
    }

    public void update(Announcement announcement) {
        announcementRepo.update(announcement);
    }

    public void delete(Announcement announcement) {
        announcementRepo.delete(announcement);
    }

    public void markAsExpired(int announcementId) {
        announcementRepo.markAsExpired(announcementId);
    }
}