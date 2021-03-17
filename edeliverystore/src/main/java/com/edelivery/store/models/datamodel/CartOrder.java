package com.edelivery.store.models.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CartOrder {


    @SerializedName("total_item_tax")
    @Expose
    private double cartOrderTotalTaxPrice;

    @SerializedName("total_cart_price")
    @Expose
    private double cartOrderTotalPrice;
    @SerializedName("cart_id")
    @Expose
    private String cartId;
    @SerializedName("user_type")
    private int userType;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("cart_unique_token")
    @Expose
    private String androidId;
    @SerializedName("note_for_deliveryman")
    @Expose
    private String noteForDeliveryman;
    @SerializedName("dest_latitude")
    @Expose
    private double deliveryLatitude;
    @SerializedName("store_id")
    @Expose
    private String storeId;
    @SerializedName("pickup_addresses")
    private List<Addresses> pickupAddresses;
    @SerializedName("destination_addresses")
    private List<Addresses> destinationAddresses;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("server_token")
    @Expose
    private String serverToken;
    @SerializedName("order_details")
    @Expose
    private List<CartProducts> products;
    @SerializedName("order_payment_id")
    @Expose
    private String orderPaymentId;

    public double getCartOrderTotalTaxPrice() {
        return cartOrderTotalTaxPrice;
    }

    public void setCartOrderTotalTaxPrice(double cartOrderTotalTaxPrice) {
        this.cartOrderTotalTaxPrice = cartOrderTotalTaxPrice;
    }

    public double getCartOrderTotalPrice() {
        return cartOrderTotalPrice;
    }

    public void setCartOrderTotalPrice(double cartOrderTotalPrice) {
        this.cartOrderTotalPrice = cartOrderTotalPrice;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getDeliveryLatitude() {
        return deliveryLatitude;
    }

    public void setDeliveryLatitude(double deliveryLatitude) {
        this.deliveryLatitude = deliveryLatitude;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getServerToken() {
        return serverToken;
    }

    public void setServerToken(String serverToken) {
        this.serverToken = serverToken;
    }

    public List<CartProducts> getProducts() {
        return products;
    }

    public void setProducts(List<CartProducts> products) {
        this.products = products;
    }


    public String getOrderPaymentId() {
        return orderPaymentId;
    }

    public void setOrderPaymentId(String orderPaymentId) {
        this.orderPaymentId = orderPaymentId;
    }

    public String getNoteForDeliveryman() {
        return noteForDeliveryman;
    }

    public void setNoteForDeliveryman(String noteForDeliveryman) {
        this.noteForDeliveryman = noteForDeliveryman;
    }

    public String getAndroidId() {
        return androidId;
    }

    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }


}