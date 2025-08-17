package com.example.classroomannouncement.Database.Daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.classroomannouncement.Database.Entities.Message;

import java.util.List;

@Dao
public interface MessageDao {
    @Insert
    long insert(Message message);

    @Update
    int update(Message message);

    @Query("SELECT * FROM messages ORDER BY timestamp ASC")
    LiveData<List<Message>> getAllMessages();

    @Query("SELECT * FROM messages WHERE senderId = :userId OR senderId = 'admin' ORDER BY timestamp ASC")
    LiveData<List<Message>> getMessagesForUser(String userId);

    @Query("UPDATE messages SET isRead = 1 WHERE senderId != :currentUserId AND isRead = 0")
    int markMessagesAsRead(String currentUserId);

    @Query("SELECT * FROM messages WHERE content LIKE '%' || :query || '%' ORDER BY timestamp DESC")
    LiveData<List<Message>> searchMessages(String query);

    @Query("SELECT * FROM messages WHERE id = :messageId LIMIT 1")
    LiveData<Message> getMessageById(int messageId);
}