package com.edelivery.store.models.responsemodel;

import com.edelivery.store.models.datamodel.Order;
import com.edelivery.store.models.datamodel.ProviderDetail;
import com.edelivery.store.models.datamodel.VehicleDetail;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by elluminati on 11-Jan-18.
 */

public class OrderStatusResponse {
    @SerializedName("delivery_status")
    @Expose
    private int deliveryStatus;
    @SerializedName("order_status")
    @Expose
    private int orderStatus;
    @SerializedName("error_code")
    @Expose
    private int errorCode;
    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("message")
    @Expose
    private int message;
    @SerializedName("vehicle_detail")
    private VehicleDetail vehicleDetail;
    @SerializedName("provider_detail")
    private ProviderDetail providerDetail;
    @SerializedName("order")
    @Expose
    private Order order;
    @SerializedName("request")
    @Expose
    private Order orderRequest;

    public int getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(int deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public VehicleDetail getVehicleDetail() {
        return vehicleDetail;
    }

    public void setVehicleDetail(VehicleDetail vehicleDetail) {
        this.vehicleDetail = vehicleDetail;
    }

    public ProviderDetail getProviderDetail() {
        return providerDetail;
    }

    public void setProviderDetail(ProviderDetail providerDetail) {
        this.providerDetail = providerDetail;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
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

    public Order getOrderRequest() {
        return orderRequest;
    }

    public void setOrderRequest(Order orderRequest) {
        this.orderRequest = orderRequest;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
