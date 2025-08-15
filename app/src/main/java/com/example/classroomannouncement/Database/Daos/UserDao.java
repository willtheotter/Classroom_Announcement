package com.example.classroomannouncement.Database.Daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.classroomannouncement.Database.Entities.User;
import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);

    @Query("DELETE FROM users WHERE id = :userId")
    void deleteById(int userId);

    @Query("SELECT * FROM users")
    LiveData<List<User>> getAllUsers();

    @Query("SELECT * FROM users WHERE email = :email")
    LiveData<User> getUserByEmailLive(String email);

    @Query("SELECT * FROM users WHERE email = :email")
    User getUserByEmail(String email);

    @Query("UPDATE users SET is_admin = :isAdmin WHERE id = :userId")
    void updateAdminStatus(int userId, boolean isAdmin);

    @Query("SELECT * FROM users WHERE id = :userId")
    User getUserById(int userId);
}