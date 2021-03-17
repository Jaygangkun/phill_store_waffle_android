package com.edelivery.store.models.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RequestDetail {


    @SerializedName("current_provider")
    @Expose
    private String currentProvider;

    @SerializedName("provider_id")
    private String providerId;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("provider_type")
    private int providerType;

    @SerializedName("provider_type_id")
    private Object providerTypeId;

    @SerializedName("confirmation_code_for_pick_up_delivery")
    private int confirmationCodeForPickUpDelivery;

    @SerializedName("request_type")
    private int requestType;

    @SerializedName("destination_addresses")
    private List<Addresses> destinationAddresses;

    @SerializedName("delivery_type")
    private int deliveryType;


    @SerializedName("vehicle_id")
    private String vehicleId;

    @SerializedName("store_detail")
    private StoreData storeDetail;

    @SerializedName("user_detail")
    private UserDetail userDetail;

    @SerializedName("pickup_addresses")
    private List<Addresses> pickupAddresses;

    @SerializedName("_id")
    private String id;


    @SerializedName("delivery_status")
    private int deliveryStatus;

    public int getProviderType() {
        return providerType;
    }

    public Object getProviderTypeId() {
        return providerTypeId;
    }

    public int getConfirmationCodeForPickUpDelivery() {
        return confirmationCodeForPickUpDelivery;
    }

    public int getRequestType() {
        return requestType;
    }


    public int getDeliveryType() {
        return deliveryType;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public StoreData getStoreDetail() {
        return storeDetail;
    }

    public UserDetail getUserDetail() {
        return userDetail;
    }


    public String getId() {
        return id;
    }

    public int getDeliveryStatus() {
        return deliveryStatus;
    }

    public List<Addresses> getDestinationAddresses() {
        return destinationAddresses;
    }

    public List<Addresses> getPickupAddresses() {
        return pickupAddresses;
    }

    public String getProviderId() {
        return providerId;
    }

    public String getUserId() {
        return userId;
    }

    public String getCurrentProvider() {
        return currentProvider;
    }
}