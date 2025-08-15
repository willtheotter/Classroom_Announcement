package com.example.classroomannouncement.Database.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "system_settings")
public class SystemSettings {
    @PrimaryKey
    private int id = 1;

    private int announcement_duration;
    private int max_users;

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getAnnouncement_duration() {
        return announcement_duration;
    }

    public void setAnnouncement_duration(int duration) {
        this.announcement_duration = duration;
    }

    public int getMax_users() {
        return max_users;
    }

    public void setMax_users(int maxUsers) {
        this.max_users = maxUsers;
    }
}