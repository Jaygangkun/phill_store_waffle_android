package com.edelivery.store.persistentroomdata.callback;


import com.edelivery.store.persistentroomdata.Notification;

import java.util.List;

import androidx.annotation.MainThread;

/**
 * Created by Ravi Bhalodi on 02,July,2020 in Elluminati
 */
public interface NotificationLoadCallBack {
    @MainThread
    void onNotificationLoad(List<Notification> notifications);

    @MainThread
    void onDataNotAvailable();
}
