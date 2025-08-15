package com.example.classroomannouncement.Database.Daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.classroomannouncement.Database.Entities.Analytics;
import java.util.List;

@Dao
public interface AnalyticsDao {
    @Query("SELECT * FROM analytics ORDER BY timestamp DESC")
    List<Analytics> getAllAnalytics();

    @Insert
    void insert(Analytics analytics);

    @Query("DELETE FROM analytics")
    void clearAll();
}