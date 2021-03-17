package com.edelivery.store.models.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class OrderData implements Parcelable {

    public static final Creator<OrderData> CREATOR = new Creator<OrderData>() {
        @Override
        public OrderData createFromParcel(Parcel source) {
            return new OrderData(source);
        }

        @Override
        public OrderData[] newArray(int size) {
            return new OrderData[size];
        }
    };
    @SerializedName("total_service_price")
    private double totalServicePrice;
    @SerializedName("total")
    private double total;
    @SerializedName("store_profit")
    private double storeProfit;
    @SerializedName("user_detail")
    @Expose
    private UserDetail userDetail;
    @SerializedName("total_order_price")
    @Expose
    private double toatlOrderPrice;
    @SerializedName("completed_at")
    @Expose
    private String completedAt;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("pickup_addresses")
    private List<Addresses> pickupAddresses;
    @SerializedName("destination_addresses")
    private List<Addresses> destinationAddresses;
    @Expose
    private OrderPaymentDetail orderPaymentDetail;
    @SerializedName("order_status")
    @Expose
    private int orderStatus;
    @SerializedName("unique_id")
    @Expose
    private int uniqueId;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("order_details")
    @Expose
    private ArrayList<OrderDetails> orderDetails;
    @SerializedName("currency")
    @Expose
    private String currency="";
    @SerializedName("is_store_rated_to_provider")
    private boolean isStoreRatedToProvider;
    @SerializedName("is_store_rated_to_user")
    private boolean isStoreRatedToUser;
    @SerializedName("is_schedule_order")
    @Expose
    private boolean isScheduleOrder;
    @SerializedName("schedule_order_start_at")
    @Expose
    private String scheduleOrderStartAt;
    @SerializedName("schedule_order_start_at2")
    @Expose
    private String scheduleOrderStartAt2;

    public OrderData() {
    }

    protected OrderData(Parcel in) {
        this.totalServicePrice = in.readDouble();
        this.total = in.readDouble();
        this.storeProfit = in.readDouble();
        this.userDetail = in.readParcelable(UserDetail.class.getClassLoader());
        this.toatlOrderPrice = in.readDouble();
        this.completedAt = in.readString();
        this.createdAt = in.readString();
        this.pickupAddresses = in.createTypedArrayList(Addresses.CREATOR);
        this.destinationAddresses = in.createTypedArrayList(Addresses.CREATOR);
        this.orderPaymentDetail = in.readParcelable(OrderPaymentDetail.class.getClassLoader());
        this.orderStatus = in.readInt();
        this.uniqueId = in.readInt();
        this.id = in.readString();
        this.orderDetails = in.createTypedArrayList(OrderDetails.CREATOR);
        this.currency = in.readString();
        this.isStoreRatedToProvider = in.readByte() != 0;
        this.isStoreRatedToUser = in.readByte() != 0;
        this.isScheduleOrder = in.readByte() != 0;
        this.scheduleOrderStartAt = in.readString();
        this.scheduleOrderStartAt2 = in.readString();
    }

    public double getTotalServicePrice() {
        return totalServicePrice;
    }

    public void setTotalServicePrice(double totalServicePrice) {
        this.totalServicePrice = totalServicePrice;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getStoreProfit() {
        return storeProfit;
    }

    public void setStoreProfit(double storeProfit) {
        this.storeProfit = storeProfit;
    }

    public UserDetail getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(UserDetail userDetail) {
        this.userDetail = userDetail;
    }

    public double getToatlOrderPrice() {
        return toatlOrderPrice;
    }

    public void setToatlOrderPrice(double toatlOrderPrice) {
        this.toatlOrderPrice = toatlOrderPrice;
    }

    public String getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(String completedAt) {
        this.completedAt = completedAt;
    }

    public boolean isStoreRatedToProvider() {
        return isStoreRatedToProvider;
    }

    public void setStoreRatedToProvider(boolean storeRatedToProvider) {
        isStoreRatedToProvider = storeRatedToProvider;
    }

    public boolean isStoreRatedToUser() {
        return isStoreRatedToUser;
    }

    public void setStoreRatedToUser(boolean storeRatedToUser) {
        isStoreRatedToUser = storeRatedToUser;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
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

    public OrderPaymentDetail getOrderPaymentDetail() {
        return orderPaymentDetail;
    }

    public void setOrderPaymentDetail(OrderPaymentDetail orderPaymentDetail) {
        this.orderPaymentDetail = orderPaymentDetail;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(int uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<OrderDetails> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(ArrayList<OrderDetails> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public boolean isScheduleOrder() {
        return isScheduleOrder;
    }

    public void setScheduleOrder(boolean scheduleOrder) {
        isScheduleOrder = scheduleOrder;
    }

    public String getScheduleOrderStartAt() {
        return scheduleOrderStartAt;
    }

    public void setScheduleOrderStartAt(String scheduleOrderStartAt) {
        this.scheduleOrderStartAt = scheduleOrderStartAt;
    }

    public String getScheduleOrderStartAt2() {
        return scheduleOrderStartAt2;
    }

    public void setScheduleOrderStartAt2(String scheduleOrderStartAt2) {
        this.scheduleOrderStartAt2 = scheduleOrderStartAt2;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.totalServicePrice);
        dest.writeDouble(this.total);
        dest.writeDouble(this.storeProfit);
        dest.writeParcelable(this.userDetail, flags);
        dest.writeDouble(this.toatlOrderPrice);
        dest.writeString(this.completedAt);
        dest.writeString(this.createdAt);
        dest.writeTypedList(this.pickupAddresses);
        dest.writeTypedList(this.destinationAddresses);
        dest.writeParcelable(this.orderPaymentDetail, flags);
        dest.writeInt(this.orderStatus);
        dest.writeInt(this.uniqueId);
        dest.writeString(this.id);
        dest.writeTypedList(this.orderDetails);
        dest.writeString(this.currency);
        dest.writeByte(this.isStoreRatedToProvider ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isStoreRatedToUser ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isScheduleOrder ? (byte) 1 : (byte) 0);
        dest.writeString(this.scheduleOrderStartAt);
        dest.writeString(this.scheduleOrderStartAt2);
    }
}