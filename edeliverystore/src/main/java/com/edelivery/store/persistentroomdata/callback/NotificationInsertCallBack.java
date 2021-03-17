package com.edelivery.store.persistentroomdata.callback;


import com.edelivery.store.persistentroomdata.Notification;

import androidx.annotation.MainThread;

/**
 * Created by Ravi Bhalodi on 02,July,2020 in Elluminati
 */
public interface NotificationInsertCallBack {

    @MainThread
    void onNotificationInsert(Notification notification);

}
