package com.example.classroomannouncement.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.classroomannouncement.Database.Repositories.ChatRepo;
import com.example.classroomannouncement.Database.Entities.Message;

import java.util.List;

public class ChatViewModel extends AndroidViewModel {
    private final ChatRepo repository;
    private final LiveData<List<Message>> allMessages;
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    public ChatViewModel(@NonNull Application application) {
        super(application);
        repository = new ChatRepo(application);
        allMessages = repository.getAllMessages();
    }

    public LiveData<List<Message>> getAllMessages() {
        return allMessages;
    }

    public LiveData<List<Message>> getUserMessages(String userId) {
        return repository.getMessagesForUser(userId);
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void sendMessage(String content, String senderId, String senderName) {
        if (content == null || content.trim().isEmpty()) {
            errorMessage.setValue("Message cannot be empty");
            return;
        }

        isLoading.setValue(true);

        try {
            Message message = new Message(senderId, content, System.currentTimeMillis());
            message.senderName = senderName;
            repository.sendMessage(message);
            errorMessage.setValue(null); // Clear any previous errors
        } catch (Exception e) {
            errorMessage.setValue("Failed to send message: " + e.getMessage());
        } finally {
            isLoading.setValue(false);
        }
    }

    public void markMessagesAsRead(String currentUserId) {
        repository.markMessagesAsRead(currentUserId);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        // Clean up resources if needed
    }
}