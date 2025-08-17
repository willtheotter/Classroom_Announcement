package com.example.classroomannouncement.Database.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "system_settings")
public class SystemSettings {
    @PrimaryKey
    @ColumnInfo(defaultValue = "1")
    private int id = 1;

    @ColumnInfo(name = "announcement_duration", defaultValue = "7")
    private int announcementDuration;

    @ColumnInfo(name = "max_users", defaultValue = "100")
    private int maxUsers;

    @ColumnInfo(name = "message_retention_days", defaultValue = "30")
    private int messageRetentionDays = 30;

    @ColumnInfo(name = "max_products", defaultValue = "500")
    private int maxProducts = 500;

    // Standard getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getAnnouncementDuration() { return announcementDuration; }
    public void setAnnouncementDuration(int announcementDuration) {
        this.announcementDuration = announcementDuration;
    }

    public int getMaxUsers() { return maxUsers; }
    public void setMaxUsers(int maxUsers) {
        this.maxUsers = maxUsers;
    }

    public int getMessageRetentionDays() { return messageRetentionDays; }
    public void setMessageRetentionDays(int messageRetentionDays) {
        this.messageRetentionDays = messageRetentionDays;
    }

    public int getMaxProducts() { return maxProducts; }
    public void setMaxProducts(int maxProducts) {
        this.maxProducts = maxProducts;
    }
}