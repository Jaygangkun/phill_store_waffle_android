package com.edelivery.store.models.responsemodel;

import com.edelivery.store.models.datamodel.Item;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by elluminati on 26-Dec-17.
 */

public class ItemsResponse {
    @SerializedName("message")
    @Expose
    private int message;
    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("error_code")
    @Expose
    private int errorCode;
    @SerializedName("items")
    @Expose
    private ArrayList<Item> items;

    public int getMessage() {
        return message;
    }

    public void setMessage(int message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }
}
