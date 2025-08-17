package com.example.classroomannouncement.Database.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Ignore;
import androidx.annotation.NonNull;

@Entity(tableName = "messages")
public class Message {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull
    public String senderId;
    public String senderName;

    @NonNull
    public String content;

    public long timestamp;
    public boolean isRead;

    // Primary constructor used by Room
    public Message(int id, @NonNull String senderId, String senderName,
                   @NonNull String content, long timestamp, boolean isRead) {
        this.id = id;
        this.senderId = senderId;
        this.senderName = senderName;
        this.content = content;
        this.timestamp = timestamp;
        this.isRead = isRead;
    }

    // Convenience constructor marked with @Ignore
    @Ignore
    public Message(@NonNull String senderId, @NonNull String content, long timestamp) {
        this(0, senderId, null, content, timestamp, false);
    }
}