package com.edelivery.store.persistentroomdata;

import android.content.Context;

import com.edelivery.store.persistentroomdata.callback.NotificationInsertCallBack;
import com.edelivery.store.persistentroomdata.callback.NotificationLoadCallBack;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by Ravi Bhalodi on 02,July,2020 in Elluminati
 */
public class NotificationRepository {

    private static NotificationRepository INSTANCE;
    private AppExecutors appExecutors;
    private NotificationDatabase notificationDatabase;

    private NotificationRepository(Context context) {
        appExecutors = new AppExecutors();
        notificationDatabase = NotificationDatabase.getInstance(context);
    }

    public static NotificationRepository getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new NotificationRepository(context);
        }
        return INSTANCE;
    }


    public void getNotification(int notificationType,
                                NotificationLoadCallBack notificationLoadCallBack) {
        final WeakReference<NotificationLoadCallBack> callBackWeakReference =
                new WeakReference<>(notificationLoadCallBack);
        // request the addresses on the I/O thread
        appExecutors.diskIO().execute(() -> {
            final List<Notification> notifications =
                    notificationDatabase.notificationDao().getAllNotification(notificationType);
            // notify on the main thread
            appExecutors.mainThread().execute(() -> {
                final NotificationLoadCallBack loadCallBack = callBackWeakReference.get();
                if (loadCallBack == null) {
                    return;
                }
                if (notifications == null) {
                    loadCallBack.onDataNotAvailable();
                } else {
                    loadCallBack.onNotificationLoad(notifications);
                }
            });
        });
    }

    public void insertNotification(Notification notification,
                                   NotificationInsertCallBack notificationInsertCallBack) {
        final WeakReference<NotificationInsertCallBack> callBackWeakReference =
                new WeakReference<>(notificationInsertCallBack);
        // request the addresses on the I/O thread
        appExecutors.diskIO().execute(() -> {
            notificationDatabase.notificationDao().insert(notification);
            // notify on the main thread
            appExecutors.mainThread().execute(() -> {
                final NotificationInsertCallBack insertCallBack = callBackWeakReference.get();
                if (insertCallBack == null) {
                    return;
                }
                insertCallBack.onNotificationInsert(notification);
            });
        });
    }

    public void clearNotification() {
        appExecutors.diskIO().execute(() -> notificationDatabase.notificationDao().deleteAllNotification());
    }
}
