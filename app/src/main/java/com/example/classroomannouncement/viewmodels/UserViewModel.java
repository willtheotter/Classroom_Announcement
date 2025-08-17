package com.example.classroomannouncement.viewmodels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.classroomannouncement.Database.Entities.User;
import com.example.classroomannouncement.Database.Repositories.UserRepo;
import java.util.List;

public class UserViewModel extends AndroidViewModel {
    private final UserRepo repository;
    private final LiveData<List<User>> allUsers;
    private final MutableLiveData<User> currentUser = new MutableLiveData<>();

    public UserViewModel(Application application) {
        super(application);
        repository = new UserRepo(application);
        allUsers = repository.getAllUsers();
    }

    // Add these new methods for profile editing
    public LiveData<User> getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        currentUser.setValue(user);
    }

    public void loadCurrentUser(String email) {
        new Thread(() -> {
            User user = repository.getUserByEmail(email);
            currentUser.postValue(user);
        }).start();
    }

    public LiveData<Boolean> updateCurrentUser(String name, String email, String password) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        new Thread(() -> {
            User user = currentUser.getValue();
            if (user != null) {
                user.setName(name);
                user.setEmail(email);
                if (password != null && !password.isEmpty()) {
                    user.setPassword(password); // Note: Hash this in production!
                }
                repository.updateUser(user);
                currentUser.postValue(user);
                result.postValue(true);
            } else {
                result.postValue(false);
            }
        }).start();
        return result;
    }

    // Existing methods remain the same...
    public void insert(User user) { repository.registerUser(user); }
    public void update(User user) { repository.updateUser(user); }
    public void updateAdminStatus(int userId, boolean isAdmin) { repository.updateAdminStatus(userId, isAdmin); }
    public void delete(User user) { repository.deleteUser(user); }
    public void deleteById(int userId) { repository.deleteUserById(userId); }
    public LiveData<List<User>> getAllUsers() { return allUsers; }
    public LiveData<User> getUserByEmailLive(String email) { return repository.getUserByEmailLive(email); }
    public User getUserByEmail(String email) { return repository.getUserByEmail(email); }
    public User getUserById(int userId) { return repository.getUserById(userId); }

    @Override
    protected void onCleared() {
        super.onCleared();
        repository.shutdown();
    }
}