package com.example.classroomannouncement.Database.Daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.classroomannouncement.Database.Entities.Announcement;

import java.util.List;

@Dao
public interface AnnouncementDao {
    @Insert
    void insert(Announcement announcement);

    @Update
    void update(Announcement announcement);

    @Delete
    void delete(Announcement announcement);

    @Query("SELECT * FROM announcements ORDER BY createdAt DESC")
    LiveData<List<Announcement>> getAllAnnouncements();

    @Query("SELECT * FROM announcements WHERE expiryTime > :currentTime AND isExpired = 0 ORDER BY createdAt DESC")
    LiveData<List<Announcement>> getActiveAnnouncements(long currentTime);

    @Query("SELECT * FROM announcements WHERE isExpired = 1 ORDER BY createdAt DESC")
    LiveData<List<Announcement>> getExpiredAnnouncements();

    // Option 1: Simple manual expiry (no time check)
    @Query("UPDATE announcements SET isExpired = 1 WHERE id = :announcementId")
    void markAsExpired(int announcementId);

    // Option 2: Time-based expiry (safer)
    @Query("UPDATE announcements SET isExpired = 1 WHERE id = :announcementId AND expiryTime <= :currentTime")
    void markAsExpired(int announcementId, long currentTime);

    @Query("UPDATE announcements SET isExpired = 1 WHERE expiryTime <= :currentTime")
    void expireOldAnnouncements(long currentTime);

    @Query("SELECT * FROM announcements WHERE id = :announcementId")
    LiveData<Announcement> getAnnouncementById(int announcementId);
}