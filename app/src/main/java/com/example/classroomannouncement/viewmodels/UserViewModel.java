package com.example.classroomannouncement.viewmodels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.classroomannouncement.Database.Entities.User;
import com.example.classroomannouncement.Database.Repositories.UserRepo;
import java.util.List;

public class UserViewModel extends AndroidViewModel {
    private final UserRepo repository;
    private final LiveData<List<User>> allUsers;

    public UserViewModel(Application application) {
        super(application);
        repository = new UserRepo(application);
        allUsers = repository.getAllUsers();
    }

    // Insert operations
    public void insert(User user) {
        repository.registerUser(user);
    }

    // Update operations
    public void update(User user) {
        repository.updateUser(user);
    }

    public void updateAdminStatus(int userId, boolean isAdmin) {
        repository.updateAdminStatus(userId, isAdmin);
    }

    // Delete operations
    public void delete(User user) {
        repository.deleteUser(user);
    }

    public void deleteById(int userId) {
        repository.deleteUserById(userId);
    }

    // Query operations
    public LiveData<List<User>> getAllUsers() {
        return allUsers;
    }

    public LiveData<User> getUserByEmailLive(String email) {
        return repository.getUserByEmailLive(email);
    }

    public User getUserByEmail(String email) {
        return repository.getUserByEmail(email);
    }

    public User getUserById(int userId) {
        return repository.getUserById(userId);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        repository.shutdown();
    }
}