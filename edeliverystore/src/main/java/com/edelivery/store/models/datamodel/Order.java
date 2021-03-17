package com.edelivery.store.models.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class Order implements Parcelable {


    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel source) {
            return new Order(source);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    //// v1 /// change

    @SerializedName("is_user_pick_up_order")
    private boolean isUserPickUpOrder;

    @SerializedName("order_unique_id")
    private int orderUniqueId;

    @SerializedName("total")
    private double total;

    ///// ////////////////////////////

    @SerializedName("delivery_status")
    private int deliveryStatus;
    @SerializedName("pickup_addresses")
    @Expose
    private List<Addresses> pickupAddresses;
    @SerializedName("destination_addresses")
    @Expose

    private List<Addresses> destinationAddresses;
    @SerializedName("total_order_price")
    @Expose
    private double totalOrderPrice;
    @SerializedName("is_store_rated_to_provider")
    @Expose
    private boolean isStoreRatedToProvider;
    @SerializedName("is_store_rated_to_user")
    @Expose
    private boolean isStoreRatedToUser;
    @SerializedName("confirmation_code_for_complete_delivery")
    private int confirmationCodeForCompleteDelivery;
    @SerializedName("is_confirmation_code_required_at_pickup_delivery")
    private boolean isConfirmationCodeRequiredAtPickupDelivery;
    @SerializedName("is_confirmation_code_required_at_complete_delivery")
    private boolean isConfirmationCodeRequiredAtCompleteDelivery;
    @SerializedName("provider_detail")
    private ProviderDetail providerDetail;
    @SerializedName("bearing")
    private float bearing;
    @SerializedName("order_payment_detail")
    private OrderPaymentDetail orderPaymentDetail;
    @SerializedName("provider_type")
    @Expose
    private int providerType;
    @SerializedName("provider_type_id")
    @Expose
    private String providerTypeId;
    @SerializedName("cancelled_at")
    @Expose
    private String cancelledAt;
    @SerializedName("schedule_order_server_start_at")
    @Expose
    private String scheduleOrderServerStartAt;
    @SerializedName("start_for_delivery_at")
    @Expose
    private String startForDeliveryAt;
    @SerializedName("order_status_id")
    @Expose
    private int orderStatusId;
    @SerializedName("promo_code")
    @Expose
    private String promoCode;
    @SerializedName("provider_previous_location")
    @Expose
    private List<Double> providerPreviousLocation;
    @SerializedName("accepted_at")
    @Expose
    private String acceptedAt;
    @SerializedName("current_provider")
    @Expose
    private String currentProvider;
    @SerializedName("order_status")
    @Expose
    private int orderStatus;
    @SerializedName("user_type")
    @Expose
    private int userType;
    @SerializedName("start_preparing_order_at")
    @Expose
    private String startPreparingOrderAt;
    @SerializedName("order_type")
    @Expose
    private int orderType;
    @SerializedName("unique_code")
    @Expose
    private int uniqueCode;
    @SerializedName("confirmation_code_for_pick_up_delivery")
    @Expose
    private String confirmationCodeForPickUpDelivery;
    @SerializedName("unique_id")
    @Expose
    private int uniqueId;
    @SerializedName("is_payment_mode_cash")
    @Expose
    private boolean isPaymentModeCash;
    @SerializedName("provider_location")
    @Expose
    private List<Double> providerLocation;
    @SerializedName("is_cancellation_fee")
    @Expose
    private boolean isCancellationFee;
    @SerializedName("completed_at")
    @Expose
    private String completedAt;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("order_ready_at")
    @Expose
    private String orderReadyAt;
    @SerializedName("provider_id")
    @Expose
    private String providerId;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("request_id")
    @Expose
    private String requestId;
    @SerializedName("schedule_order_start_at")
    @Expose
    private String scheduleOrderStartAt;
    @SerializedName("schedule_order_start_at2")
    @Expose
    private String scheduleOrderStartAt2;
    @SerializedName("is_payment_paid")
    @Expose
    private boolean isPaymentPaid;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("order_status_by")
    @Expose
    private String orderStatusBy;
    @SerializedName("cart_detail")
    @Expose
    private OrderData cartDetail;
    @SerializedName("order_detail")
    @Expose
    private OrderData orderDetail;
    @SerializedName("is_pending_payment")
    @Expose
    private boolean isPendingPayment;
    @SerializedName("is_schedule_order")
    @Expose
    private boolean isScheduleOrder;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("is_surge_hours")
    @Expose
    private boolean isSurgeHours;
    @SerializedName("service_id")
    @Expose
    private String serviceId;
    @SerializedName("start_for_pickup_at")
    @Expose
    private String startForPickupAt;
    @SerializedName("user_type_id")
    @Expose
    private String userTypeId;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("delivered_at")
    @Expose
    private String deliveredAt;
    @SerializedName("store_accepted_at")
    @Expose
    private String storeAcceptedAt;
    @SerializedName("order_payment_id")
    @Expose
    private String orderPaymentId;
    @SerializedName("store_id")
    @Expose
    private String storeId;
    @SerializedName("picked_order_at")
    @Expose
    private String pickedOrderAt;
    @SerializedName("user_detail")
    @Expose
    private UserDetail userDetail;
    @SerializedName("admin_currency")
    @Expose
    private String adminCurrency;
    @SerializedName("is_min_fare_used")
    @Expose
    private boolean isMinFareUsed;
    @SerializedName("arrived_on_store_at")
    @Expose
    private String arrivedOnStoreAt;
    @SerializedName("promo_id")
    @Expose
    private String promoId;
    @SerializedName("store_order_created_at")
    @Expose
    private String storeOrderCreatedAt;
    @SerializedName("is_distance_unit_mile")
    @Expose
    private boolean isDistanceUnitMile;
    @SerializedName("timezone")
    @Expose

    private String timeZone;
    @SerializedName("order_change")
    private boolean orderChange;

    @SerializedName("date_time")
    @Expose
    private List<DateTime> dateTime = null;

    public Order() {
    }

    protected Order(Parcel in) {
        this.deliveryStatus = in.readInt();
        this.pickupAddresses = in.createTypedArrayList(Addresses.CREATOR);
        this.destinationAddresses = in.createTypedArrayList(Addresses.CREATOR);
        this.totalOrderPrice = in.readDouble();
        this.isStoreRatedToProvider = in.readByte() != 0;
        this.isStoreRatedToUser = in.readByte() != 0;
        this.confirmationCodeForCompleteDelivery = in.readInt();
        this.isConfirmationCodeRequiredAtPickupDelivery = in.readByte() != 0;
        this.isConfirmationCodeRequiredAtCompleteDelivery = in.readByte() != 0;
        this.bearing = in.readFloat();
        this.orderPaymentDetail = in.readParcelable(OrderPaymentDetail.class.getClassLoader());
        this.providerType = in.readInt();
        this.providerTypeId = in.readString();
        this.cancelledAt = in.readString();
        this.scheduleOrderServerStartAt = in.readString();
        this.startForDeliveryAt = in.readString();
        this.orderStatusId = in.readInt();
        this.promoCode = in.readString();
        this.providerPreviousLocation = new ArrayList<Double>();
        in.readList(this.providerPreviousLocation, Double.class.getClassLoader());
        this.acceptedAt = in.readString();
        this.currentProvider = in.readString();
        this.orderStatus = in.readInt();
        this.userType = in.readInt();
        this.startPreparingOrderAt = in.readString();
        this.orderType = in.readInt();
        this.uniqueCode = in.readInt();
        this.confirmationCodeForPickUpDelivery = in.readString();
        this.uniqueId = in.readInt();
        this.isPaymentModeCash = in.readByte() != 0;
        this.providerLocation = new ArrayList<Double>();
        in.readList(this.providerLocation, Double.class.getClassLoader());
        this.isCancellationFee = in.readByte() != 0;
        this.completedAt = in.readString();
        this.userId = in.readString();
        this.orderReadyAt = in.readString();
        this.providerId = in.readString();
        this.id = in.readString();
        this.orderId = in.readString();
        this.requestId = in.readString();
        this.scheduleOrderStartAt = in.readString();
        this.scheduleOrderStartAt2 = in.readString();
        this.isPaymentPaid = in.readByte() != 0;
        this.createdAt = in.readString();
        this.orderStatusBy = in.readString();
        this.userDetail = in.readParcelable(UserDetail.class.getClassLoader());
        this.cartDetail = in.readParcelable(OrderData.class.getClassLoader());
        this.orderDetail = in.readParcelable(OrderData.class.getClassLoader());
        this.isPendingPayment = in.readByte() != 0;
        this.isScheduleOrder = in.readByte() != 0;
        this.updatedAt = in.readString();
        this.isSurgeHours = in.readByte() != 0;
        this.serviceId = in.readString();
        this.startForPickupAt = in.readString();
        this.userTypeId = in.readString();
        this.currency = in.readString();
        this.deliveredAt = in.readString();
        this.storeAcceptedAt = in.readString();
        this.orderPaymentId = in.readString();
        this.storeId = in.readString();
        this.pickedOrderAt = in.readString();
        this.adminCurrency = in.readString();
        this.isMinFareUsed = in.readByte() != 0;
        this.arrivedOnStoreAt = in.readString();
        this.promoId = in.readString();
        this.storeOrderCreatedAt = in.readString();
        this.isDistanceUnitMile = in.readByte() != 0;
        this.timeZone = in.readString();
        this.orderChange = in.readByte() != 0;
        in.readList(this.dateTime, Double.class.getClassLoader());
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public OrderData getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(OrderData orderDetail) {
        this.orderDetail = orderDetail;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(int deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
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

    public double getTotalOrderPrice() {
        return totalOrderPrice;
    }

    public void setTotalOrderPrice(double totalOrderPrice) {
        this.totalOrderPrice = totalOrderPrice;
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

    public boolean isConfirmationCodeRequiredAtPickupDelivery() {
        return isConfirmationCodeRequiredAtPickupDelivery;
    }

    public void setConfirmationCodeRequiredAtPickupDelivery(boolean confirmationCodeRequiredAtPickupDelivery) {
        isConfirmationCodeRequiredAtPickupDelivery = confirmationCodeRequiredAtPickupDelivery;
    }

    public boolean isConfirmationCodeRequiredAtCompleteDelivery() {
        return isConfirmationCodeRequiredAtCompleteDelivery;
    }

    public void setConfirmationCodeRequiredAtCompleteDelivery(boolean confirmationCodeRequiredAtCompleteDelivery) {
        isConfirmationCodeRequiredAtCompleteDelivery = confirmationCodeRequiredAtCompleteDelivery;
    }

    public int getProviderType() {
        return providerType;
    }

    public void setProviderType(int providerType) {
        this.providerType = providerType;
    }

    public String getProviderTypeId() {
        return providerTypeId;
    }

    public void setProviderTypeId(String providerTypeId) {
        this.providerTypeId = providerTypeId;
    }

    public String getCancelledAt() {
        return cancelledAt;
    }

    public void setCancelledAt(String cancelledAt) {
        this.cancelledAt = cancelledAt;
    }

    public String getScheduleOrderServerStartAt() {
        return scheduleOrderServerStartAt;
    }

    public void setScheduleOrderServerStartAt(String scheduleOrderServerStartAt) {
        this.scheduleOrderServerStartAt = scheduleOrderServerStartAt;
    }

    public String getConfirmationCodeForPickUpDelivery() {
        return confirmationCodeForPickUpDelivery;
    }

    public void setConfirmationCodeForPickUpDelivery(String confirmationCodeForPickUpDelivery) {
        this.confirmationCodeForPickUpDelivery = confirmationCodeForPickUpDelivery;
    }

    public String getStartForDeliveryAt() {
        return startForDeliveryAt;
    }

    public void setStartForDeliveryAt(String startForDeliveryAt) {
        this.startForDeliveryAt = startForDeliveryAt;
    }

    public int getOrderStatusId() {
        return orderStatusId;
    }

    public void setOrderStatusId(int orderStatusId) {
        this.orderStatusId = orderStatusId;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public List<Double> getProviderPreviousLocation() {
        return providerPreviousLocation;
    }

    public void setProviderPreviousLocation(List<Double> providerPreviousLocation) {
        this.providerPreviousLocation = providerPreviousLocation;
    }

    public String getAcceptedAt() {
        return acceptedAt;
    }

    public void setAcceptedAt(String acceptedAt) {
        this.acceptedAt = acceptedAt;
    }

    public String getCurrentProvider() {
        return currentProvider;
    }

    public void setCurrentProvider(String currentProvider) {
        this.currentProvider = currentProvider;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getStartPreparingOrderAt() {
        return startPreparingOrderAt;
    }

    public void setStartPreparingOrderAt(String startPreparingOrderAt) {
        this.startPreparingOrderAt = startPreparingOrderAt;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public int getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(int uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public int getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(int uniqueId) {
        this.uniqueId = uniqueId;
    }

    public boolean isIsPaymentModeCash() {
        return isPaymentModeCash;
    }

    public void setIsPaymentModeCash(boolean isPaymentModeCash) {
        this.isPaymentModeCash = isPaymentModeCash;
    }

    public List<Double> getProviderLocation() {
        return providerLocation;
    }

    public void setProviderLocation(List<Double> providerLocation) {
        this.providerLocation = providerLocation;
    }

    public boolean isIsCancellationFee() {
        return isCancellationFee;
    }

    public void setIsCancellationFee(boolean isCancellationFee) {
        this.isCancellationFee = isCancellationFee;
    }

    public String getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(String completedAt) {
        this.completedAt = completedAt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderReadyAt() {
        return orderReadyAt;
    }

    public void setOrderReadyAt(String orderReadyAt) {
        this.orderReadyAt = orderReadyAt;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public boolean isIsPaymentPaid() {
        return isPaymentPaid;
    }

    public void setIsPaymentPaid(boolean isPaymentPaid) {
        this.isPaymentPaid = isPaymentPaid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getOrderStatusBy() {
        return orderStatusBy;
    }

    public void setOrderStatusBy(String orderStatusBy) {
        this.orderStatusBy = orderStatusBy;
    }

    public boolean isIsPendingPayment() {
        return isPendingPayment;
    }

    public void setIsPendingPayment(boolean isPendingPayment) {
        this.isPendingPayment = isPendingPayment;
    }

    public boolean isIsScheduleOrder() {
        return isScheduleOrder;
    }

    public void setIsScheduleOrder(boolean isScheduleOrder) {
        this.isScheduleOrder = isScheduleOrder;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isIsSurgeHours() {
        return isSurgeHours;
    }

    public void setIsSurgeHours(boolean isSurgeHours) {
        this.isSurgeHours = isSurgeHours;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getStartForPickupAt() {
        return startForPickupAt;
    }

    public void setStartForPickupAt(String startForPickupAt) {
        this.startForPickupAt = startForPickupAt;
    }

    public String getUserTypeId() {
        return userTypeId;
    }

    public void setUserTypeId(String userTypeId) {
        this.userTypeId = userTypeId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDeliveredAt() {
        return deliveredAt;
    }

    public void setDeliveredAt(String deliveredAt) {
        this.deliveredAt = deliveredAt;
    }

    public String getStoreAcceptedAt() {
        return storeAcceptedAt;
    }

    public void setStoreAcceptedAt(String storeAcceptedAt) {
        this.storeAcceptedAt = storeAcceptedAt;
    }

    public String getOrderPaymentId() {
        return orderPaymentId;
    }

    public void setOrderPaymentId(String orderPaymentId) {
        this.orderPaymentId = orderPaymentId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getPickedOrderAt() {
        return pickedOrderAt;
    }

    public void setPickedOrderAt(String pickedOrderAt) {
        this.pickedOrderAt = pickedOrderAt;
    }

    public UserDetail getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(UserDetail userDetail) {
        this.userDetail = userDetail;
    }

    public String getAdminCurrency() {
        return adminCurrency;
    }

    public void setAdminCurrency(String adminCurrency) {
        this.adminCurrency = adminCurrency;
    }

    public boolean isIsMinFareUsed() {
        return isMinFareUsed;
    }

    public void setIsMinFareUsed(boolean isMinFareUsed) {
        this.isMinFareUsed = isMinFareUsed;
    }

    public String getArrivedOnStoreAt() {
        return arrivedOnStoreAt;
    }

    public void setArrivedOnStoreAt(String arrivedOnStoreAt) {
        this.arrivedOnStoreAt = arrivedOnStoreAt;
    }

    public String getPromoId() {
        return promoId;
    }

    public void setPromoId(String promoId) {
        this.promoId = promoId;
    }

    public String getStoreOrderCreatedAt() {
        return storeOrderCreatedAt;
    }

    public void setStoreOrderCreatedAt(String storeOrderCreatedAt) {
        this.storeOrderCreatedAt = storeOrderCreatedAt;
    }

    public boolean isIsDistanceUnitMile() {
        return isDistanceUnitMile;
    }

    public void setIsDistanceUnitMile(boolean isDistanceUnitMile) {
        this.isDistanceUnitMile = isDistanceUnitMile;
    }

    public OrderPaymentDetail getOrderPaymentDetail() {
        return orderPaymentDetail;
    }

    public void setOrderPaymentDetail(OrderPaymentDetail orderPaymentDetail) {
        this.orderPaymentDetail = orderPaymentDetail;
    }

    public float getBearing() {
        return bearing;
    }

    public void setBearing(float bearing) {
        this.bearing = bearing;
    }



    public int getConfirmationCodeForCompleteDelivery() {
        return confirmationCodeForCompleteDelivery;
    }

    public void setConfirmationCodeForCompleteDelivery(int confirmationCodeForCompleteDelivery) {
        this.confirmationCodeForCompleteDelivery = confirmationCodeForCompleteDelivery;
    }

    public OrderData getCartDetail() {
        return cartDetail;
    }

    public void setCartDetail(OrderData cartDetail) {
        this.cartDetail = cartDetail;
    }

    public boolean isOrderChange() {
        return orderChange;
    }

    public void setOrderChange(boolean orderChange) {
        this.orderChange = orderChange;
    }

    public List<DateTime> getDateTime() {
        return dateTime;
    }

    public void setDateTime(List<DateTime> dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.deliveryStatus);
        dest.writeTypedList(this.pickupAddresses);
        dest.writeTypedList(this.destinationAddresses);
        dest.writeDouble(this.totalOrderPrice);
        dest.writeByte(this.isStoreRatedToProvider ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isStoreRatedToUser ? (byte) 1 : (byte) 0);
        dest.writeInt(this.confirmationCodeForCompleteDelivery);
        dest.writeByte(this.isConfirmationCodeRequiredAtPickupDelivery ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isConfirmationCodeRequiredAtCompleteDelivery ? (byte) 1 : (byte) 0);
        dest.writeFloat(this.bearing);
        dest.writeParcelable(this.orderPaymentDetail, flags);
        dest.writeInt(this.providerType);
        dest.writeString(this.providerTypeId);
        dest.writeString(this.cancelledAt);
        dest.writeString(this.scheduleOrderServerStartAt);
        dest.writeString(this.startForDeliveryAt);
        dest.writeInt(this.orderStatusId);
        dest.writeString(this.promoCode);
        dest.writeList(this.providerPreviousLocation);
        dest.writeString(this.acceptedAt);
        dest.writeString(this.currentProvider);
        dest.writeInt(this.orderStatus);
        dest.writeInt(this.userType);
        dest.writeString(this.startPreparingOrderAt);
        dest.writeInt(this.orderType);
        dest.writeInt(this.uniqueCode);
        dest.writeString(this.confirmationCodeForPickUpDelivery);
        dest.writeInt(this.uniqueId);
        dest.writeByte(this.isPaymentModeCash ? (byte) 1 : (byte) 0);
        dest.writeList(this.providerLocation);
        dest.writeByte(this.isCancellationFee ? (byte) 1 : (byte) 0);
        dest.writeString(this.completedAt);
        dest.writeString(this.userId);
        dest.writeString(this.orderReadyAt);
        dest.writeString(this.providerId);
        dest.writeString(this.id);
        dest.writeString(this.orderId);
        dest.writeString(this.requestId);
        dest.writeString(this.scheduleOrderStartAt);
        dest.writeString(this.scheduleOrderStartAt2);
        dest.writeByte(this.isPaymentPaid ? (byte) 1 : (byte) 0);
        dest.writeString(this.createdAt);
        dest.writeString(this.orderStatusBy);
        dest.writeParcelable(this.userDetail, flags);
        dest.writeParcelable(this.cartDetail, flags);
        dest.writeParcelable(this.orderDetail, flags);
        dest.writeByte(this.isPendingPayment ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isScheduleOrder ? (byte) 1 : (byte) 0);
        dest.writeString(this.updatedAt);
        dest.writeByte(this.isSurgeHours ? (byte) 1 : (byte) 0);
        dest.writeString(this.serviceId);
        dest.writeString(this.startForPickupAt);
        dest.writeString(this.userTypeId);
        dest.writeString(this.currency);
        dest.writeString(this.deliveredAt);
        dest.writeString(this.storeAcceptedAt);
        dest.writeString(this.orderPaymentId);
        dest.writeString(this.storeId);
        dest.writeString(this.pickedOrderAt);

        dest.writeString(this.adminCurrency);
        dest.writeByte(this.isMinFareUsed ? (byte) 1 : (byte) 0);
        dest.writeString(this.arrivedOnStoreAt);
        dest.writeString(this.promoId);
        dest.writeString(this.storeOrderCreatedAt);
        dest.writeByte(this.isDistanceUnitMile ? (byte) 1 : (byte) 0);
        dest.writeString(this.timeZone);
        dest.writeByte(this.orderChange ? (byte) 1 : (byte) 0);
        dest.writeList(this.dateTime);
    }

    public int getOrderUniqueId() {
        return orderUniqueId;
    }

    public boolean isUserPickUpOrder() {
        return isUserPickUpOrder;
    }

    public double getTotal() {
        return total;
    }

    public ProviderDetail getProviderDetail() {
        return providerDetail;
    }
}