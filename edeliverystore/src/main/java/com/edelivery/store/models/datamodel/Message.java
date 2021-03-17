package com.edelivery.store.models.datamodel;

import com.google.gson.annotations.SerializedName;

public class Message {
    public Message() {
    }

    @SerializedName("id")
    private String id;

    @SerializedName("chat_type")
    private int chat_type;

    @SerializedName("message")
    private String message;

    @SerializedName("time")
    private String time;

    @SerializedName("sender_type")
    private int sender_type;

    @SerializedName("is_read")
    private boolean is_read;

    @SerializedName("receiver_id")
    private String receiver_id;

    public Message(String id,int chat_type, String message, String time, int sender_type,String receiver_id, boolean is_read) {
        this.id = id;
        this.chat_type = chat_type;
        this.message = message;
        this.time = time;
        this.sender_type = sender_type;
        this.is_read = is_read;
        this.receiver_id = receiver_id;
    }

    public int getChat_type() {
        return chat_type;
    }

    public void setChat_type(int chat_type) {
        this.chat_type = chat_type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getSender_type() {
        return sender_type;
    }

    public void setSender_type(int sender_type) {
        this.sender_type = sender_type;
    }

    public boolean isIs_read() {
        return is_read;
    }

    public void setIs_read(boolean is_read) {
        this.is_read = is_read;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(String receiver_id) {
        this.receiver_id = receiver_id;
    }
}
