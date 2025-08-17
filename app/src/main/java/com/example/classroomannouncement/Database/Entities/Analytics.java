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

    @ColumnInfo(name = "viewCount", defaultValue = "0")
    private int viewCount;

    @ColumnInfo(name = "details")
    private String details;

    @ColumnInfo(name = "timestamp")
    private long timestamp;

    @ColumnInfo(name = "userId")
    private Integer userId;

    @ColumnInfo(name = "productId")
    private Integer productId;

    @ColumnInfo(name = "productName")
    private String productName;

    // Constructors
    public Analytics() {}

    @Ignore
    public Analytics(String eventType, int viewCount, String details,
                     long timestamp, Integer userId, Integer productId,
                     String productName) {
        this.eventType = eventType;
        this.viewCount = viewCount;
        this.details = details;
        this.timestamp = timestamp;
        this.userId = userId;
        this.productId = productId;
        this.productName = productName;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public int getViewCount() { return viewCount; }
    public void setViewCount(int viewCount) { this.viewCount = viewCount; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public Integer getProductId() { return productId; }
    public void setProductId(Integer productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
}