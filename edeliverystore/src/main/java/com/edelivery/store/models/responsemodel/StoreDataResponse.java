package com.edelivery.store.models.responsemodel;

import com.edelivery.store.models.datamodel.StoreData;
import com.edelivery.store.models.datamodel.SubStore;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by elluminati on 11-Jan-18.
 */

public class StoreDataResponse {


    @SerializedName("minimum_phone_number_length")
    @Expose
    private int minPhoneNumberLength;
    @SerializedName("maximum_phone_number_length")
    @Expose
    private int maxPhoneNumberLength;

    @SerializedName("is_store_can_create_group")
    @Expose
    private boolean isStoreCanCreateGroup;

    @SerializedName("is_store_can_edit_order")
    @Expose
    private boolean isStoreCanEditOrder;

    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("message")
    @Expose
    private int message;

    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("store")
    @Expose
    private StoreData storeData;
    @SerializedName("store_detail")
    @Expose
    private StoreData storeDetail;


    @SerializedName("sub_store")
    @Expose
    private SubStore subStore;

    @SerializedName("error_code")
    @Expose
    private int errorCode;

    public StoreData getStoreDetail() {
        return storeDetail;
    }

    public void setStoreDetail(StoreData storeDetail) {
        this.storeDetail = storeDetail;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getMinPhoneNumberLength() {
        return minPhoneNumberLength;
    }

    public void setMinPhoneNumberLength(int minPhoneNumberLength) {
        this.minPhoneNumberLength = minPhoneNumberLength;
    }

    public int getMaxPhoneNumberLength() {
        return maxPhoneNumberLength;
    }

    public void setMaxPhoneNumberLength(int maxPhoneNumberLength) {
        this.maxPhoneNumberLength = maxPhoneNumberLength;
    }

    public boolean isStoreCanCreateGroup() {
        return isStoreCanCreateGroup;
    }

    public void setStoreCanCreateGroup(boolean storeCanCreateGroup) {
        isStoreCanCreateGroup = storeCanCreateGroup;
    }

    public boolean isStoreCanEditOrder() {
        return isStoreCanEditOrder;
    }

    public void setStoreCanEditOrder(boolean storeCanEditOrder) {
        isStoreCanEditOrder = storeCanEditOrder;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getMessage() {
        return message;
    }

    public void setMessage(int message) {
        this.message = message;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public StoreData getStoreData() {
        return storeData;
    }

    public void setStoreData(StoreData storeData) {
        this.storeData = storeData;
    }

    public SubStore getSubStore() {
        return subStore;
    }
}
