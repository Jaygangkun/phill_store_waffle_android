package com.edelivery.store.persistentroomdata;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * Created by Ravi Bhalodi on 02,July,2020 in Elluminati
 */
@Database(entities = {Notification.class}, version = 1, exportSchema = false)
public abstract class NotificationDatabase extends RoomDatabase {
    public abstract NotificationDao notificationDao();

    private static NotificationDatabase INSTANCE;
    private static final Object sLock = new Object();

    public static NotificationDatabase getInstance(Context context) {

        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        NotificationDatabase.class, context.getPackageName()+".notification.db")
                        .build();
            }
            return INSTANCE;
        }
    }

}
