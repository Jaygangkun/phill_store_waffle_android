package com.edelivery.store.models.responsemodel;

import com.edelivery.store.models.datamodel.Order;
import com.edelivery.store.models.datamodel.VehicleDetail;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by elluminati on 11-Jan-18.
 */

public class OrderResponse {


    @SerializedName("admin_vehicles")
    @Expose
    private List<VehicleDetail> adminVehicles;


    @SerializedName("vehicles")
    @Expose
    private List<VehicleDetail> vehicles;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("error_code")
    @Expose
    private int errorCode;
    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("message")
    @Expose
    private int message;
    @SerializedName("requests")
    @Expose
    private ArrayList<Order> deliveryList;
    @SerializedName("orders")
    @Expose
    private ArrayList<Order> orderList;

    public List<VehicleDetail> getAdminVehicles() {
        return adminVehicles;
    }

    public void setAdminVehicles(List<VehicleDetail> adminVehicles) {
        this.adminVehicles = adminVehicles;
    }

    public List<VehicleDetail> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<VehicleDetail> vehicles) {
        this.vehicles = vehicles;
    }

    public ArrayList<Order> getDeliveryList() {
        return deliveryList;
    }

    public void setDeliveryList(ArrayList<Order> deliveryList) {
        this.deliveryList = deliveryList;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
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

    public ArrayList<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(ArrayList<Order> orderList) {
        this.orderList = orderList;
    }
}
