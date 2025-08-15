package com.example.classroomannouncement.Database.Daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.classroomannouncement.Database.Entities.SystemSettings;

@Dao
public interface SystemSettingsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SystemSettings settings);

    @Update
    void update(SystemSettings settings);

    @Query("SELECT * FROM system_settings LIMIT 1")
    LiveData<SystemSettings> getSystemSettings();

    @Query("SELECT * FROM system_settings LIMIT 1")
    SystemSettings getSystemSettingsSync();

    // Updated query to match exact column names
    @Query("UPDATE system_settings SET announcement_duration = :duration, max_users = :maxUsers WHERE id = 1")
    void updateSettings(int duration, int maxUsers);
}