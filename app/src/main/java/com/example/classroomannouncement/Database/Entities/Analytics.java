package com.example.classroomannouncement.Database.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "analytics")
public class Analytics {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "eventType")
    private String eventType;

    @ColumnInfo(name = "details")
    private String details;

    @ColumnInfo(name = "timestamp")
    private long timestamp;

    @ColumnInfo(name = "userId")
    private Integer userId;

    // No-arg constructor required by Room
    public Analytics() {
    }

    // Parameterized constructor marked with @Ignore
    @Ignore
    public Analytics(String eventType, String details, long timestamp, Integer userId) {
        this.eventType = eventType;
        this.details = details;
        this.timestamp = timestamp;
        this.userId = userId;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}