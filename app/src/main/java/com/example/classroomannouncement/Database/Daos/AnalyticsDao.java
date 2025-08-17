package com.example.classroomannouncement.Database.Daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.classroomannouncement.Database.Entities.Analytics;
import java.util.List;

@Dao
public interface AnalyticsDao {
    @Query("SELECT * FROM analytics ORDER BY timestamp DESC")
    List<Analytics> getAllAnalytics();

    @Query("SELECT * FROM analytics ORDER BY timestamp DESC")
    LiveData<List<Analytics>> getAllAnalyticsLive();

    @Query("SELECT * FROM analytics WHERE productId = :productId AND eventType = 'view'")
    List<Analytics> getProductViews(int productId);

    @Query("SELECT SUM(viewCount) FROM analytics WHERE productId = :productId")
    Integer getTotalViewsForProduct(int productId);

    @Insert
    void insert(Analytics analytics);

    @Update
    void update(Analytics analytics);

    @Query("DELETE FROM analytics")
    void clearAll();
}