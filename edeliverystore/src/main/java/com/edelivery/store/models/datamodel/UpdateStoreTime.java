package com.edelivery.store.models.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by elluminati on 13-Oct-17.
 */

public class UpdateStoreTime {


    @SerializedName("old_password")
    @Expose
    private String oldPassword;

    @SerializedName("social_id")
    @Expose
    private String socialId;

    @SerializedName("store_id")
    @Expose
    private String storeId;
    @SerializedName("server_token")
    @Expose
    private String serverToken;

    @SerializedName("store_time")
    @Expose
    private ArrayList<StoreTime> storeTime;

    @SerializedName("store_delivery_time")
    @Expose
    private ArrayList<StoreTime> storeDeliveryTime;

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getServerToken() {
        return serverToken;
    }

    public void setServerToken(String serverToken) {
        this.serverToken = serverToken;
    }

    public ArrayList<StoreTime> getStoreTime() {
        return storeTime;
    }

    public void setStoreTime(ArrayList<StoreTime> storeTime) {
        this.storeTime = storeTime;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getSocialId() {
        return socialId;
    }

    public void setSocialId(String socialId) {
        this.socialId = socialId;
    }

    public ArrayList<StoreTime> getStoreDeliveryTime() {
        return storeDeliveryTime;
    }

    public void setStoreDeliveryTime(ArrayList<StoreTime> storeDeliveryTime) {
        this.storeDeliveryTime = storeDeliveryTime;
    }
}
