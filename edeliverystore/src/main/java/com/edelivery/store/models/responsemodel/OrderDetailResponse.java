package com.edelivery.store.models.responsemodel;

import com.edelivery.store.models.datamodel.OrderDetail;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class OrderDetailResponse {

    @SerializedName("error_code")
    @Expose
    private int errorCode;
    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("message")
    @Expose
    private int message;

    @SerializedName("order")
    @Expose
    private OrderDetail orderDetail;

    public OrderDetail getOrderDetail() {
        return orderDetail;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public int getMessage() {
        return message;
    }
}
