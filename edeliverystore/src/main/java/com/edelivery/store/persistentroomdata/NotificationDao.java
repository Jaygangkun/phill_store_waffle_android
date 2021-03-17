package com.edelivery.store.persistentroomdata;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

/**
 * Created by Ravi Bhalodi on 02,July,2020 in Elluminati
 */
@Dao
public interface NotificationDao {
    @Query("SELECT * FROM notification WHERE notification_type = :notificationType")
    List<Notification> getAllNotification(int notificationType);

    @Insert
    void insert(Notification notification);

    @Delete
    void delete(Notification notification);

    @Update
    void update(Notification notification);

    @Query("DELETE FROM notification")
    void deleteAllNotification();
}
