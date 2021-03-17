package com.edelivery.store.models.responsemodel;

import com.edelivery.store.models.datamodel.Addresses;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PushDataResponse {


    @SerializedName("order_count")
    @Expose
    private int orderCount;
    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("currency")
    @Expose
    private String currency = "$";
    @SerializedName("total_order_price")
    @Expose
    private double totalOrderPrice;
    @SerializedName("unique_id")
    @Expose
    private int uniqueId;
    @SerializedName("pickup_addresses")
    @Expose
    private List<Addresses> pickupAddresses;
    @SerializedName("destination_addresses")
    @Expose

    private List<Addresses> destinationAddresses;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("user_image")
    private String userImage;

    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getTotalOrderPrice() {
        return totalOrderPrice;
    }

    public void setTotalOrderPrice(double totalOrderPrice) {
        this.totalOrderPrice = totalOrderPrice;
    }

    public int getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(int uniqueId) {
        this.uniqueId = uniqueId;
    }

    public List<Addresses> getPickupAddresses() {
        return pickupAddresses;
    }

    public void setPickupAddresses(List<Addresses> pickupAddresses) {
        this.pickupAddresses = pickupAddresses;
    }

    public List<Addresses> getDestinationAddresses() {
        return destinationAddresses;
    }

    public void setDestinationAddresses(List<Addresses> destinationAddresses) {
        this.destinationAddresses = destinationAddresses;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }


}