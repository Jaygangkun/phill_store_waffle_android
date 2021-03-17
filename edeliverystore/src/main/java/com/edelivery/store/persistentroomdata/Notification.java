package com.edelivery.store.persistentroomdata;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Created by Ravi Bhalodi on 02,July,2020 in Elluminati
 */
@Entity
public class Notification {

    public static final int ORDER_TYPE = 0;
    public static final int MASS_TYPE = 1;


    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "message")
    private String message;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "notification_type")
    private int notificationType;


    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(int notificationType) {
        this.notificationType = notificationType;
    }

    public void setId(int id) {
        this.id = id;
    }
}
