package com.example.classroomannouncement.Database.Repositories;

import android.app.Application;
import androidx.lifecycle.LiveData;

import com.example.classroomannouncement.Database.AppDatabase;
import com.example.classroomannouncement.Database.Daos.MessageDao;
import com.example.classroomannouncement.Database.Entities.Message;

import java.util.List;

public class ChatRepo {
    private final MessageDao messageDao;
    private final LiveData<List<Message>> allMessages;

    public ChatRepo(Application application) {
        AppDatabase database = AppDatabase.getDatabase(application);
        messageDao = database.messageDao();
        allMessages = messageDao.getAllMessages();
    }

    public LiveData<List<Message>> getAllMessages() {
        return allMessages;
    }

    public LiveData<List<Message>> getMessagesForUser(String userId) {
        return messageDao.getMessagesForUser(userId);
    }

    public void sendMessage(Message message) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            messageDao.insert(message);
        });
    }

    public void markMessagesAsRead(String currentUserId) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            messageDao.markMessagesAsRead(currentUserId);
        });
    }
}