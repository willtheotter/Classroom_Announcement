package com.example.classroomannouncement.Database.Repositories;

import android.content.Context;
import androidx.lifecycle.LiveData;
import com.example.classroomannouncement.Database.AppDatabase;
import com.example.classroomannouncement.Database.Daos.UserDao;
import com.example.classroomannouncement.Database.Entities.User;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserRepo {
    private final UserDao userDao;
    private final ExecutorService executor;

    public UserRepo(Context context) {
        AppDatabase db = AppDatabase.getDatabase(context);
        this.userDao = db.userDao();
        this.executor = Executors.newSingleThreadExecutor();
    }

    // Insert operations
    public void registerUser(User user) {
        executor.execute(() -> userDao.insert(user));
    }

    // Update operations
    public void updateUser(User user) {
        executor.execute(() -> userDao.update(user));
    }

    public void updateAdminStatus(int userId, boolean isAdmin) {
        executor.execute(() -> userDao.updateAdminStatus(userId, isAdmin));
    }

    // Delete operations
    public void deleteUser(User user) {
        executor.execute(() -> userDao.delete(user));
    }

    public void deleteUserById(int userId) {
        executor.execute(() -> userDao.deleteById(userId));
    }

    // Query operations
    public LiveData<List<User>> getAllUsers() {
        return userDao.getAllUsers();
    }

    public LiveData<User> getUserByEmailLive(String email) {
        return userDao.getUserByEmailLive(email);
    }

    public User getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }

    public User getUserById(int userId) {
        return userDao.getUserById(userId);
    }

    public void shutdown() {
        executor.shutdown();
    }
}