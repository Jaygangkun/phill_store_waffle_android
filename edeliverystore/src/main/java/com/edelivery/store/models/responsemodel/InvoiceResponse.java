package com.edelivery.store.models.responsemodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.edelivery.store.models.datamodel.UserDetail;
import com.edelivery.store.models.datamodel.OrderPaymentDetail;
import com.edelivery.store.models.datamodel.Service;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InvoiceResponse implements Parcelable {
    public static final Parcelable.Creator<InvoiceResponse> CREATOR = new Parcelable
            .Creator<InvoiceResponse>() {
        @Override
        public InvoiceResponse createFromParcel(Parcel source) {
            return new InvoiceResponse(source);
        }

        @Override
        public InvoiceResponse[] newArray(int size) {
            return new InvoiceResponse[size];
        }
    };
    @SerializedName("min_order_price")
    private double minOrderPrice;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("payment_gateway_name")
    private String payment;
    @SerializedName("order_payment")
    @Expose
    private OrderPaymentDetail orderPayment;
    @SerializedName("store")
    @Expose
    private UserDetail store;
    @SerializedName("server_time")
    @Expose
    private String serverTime;
    @SerializedName("timezone")
    @Expose
    private String timezone;
    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("service")
    @Expose
    private Service service;
    @SerializedName("message")
    @Expose
    private int message;
    @SerializedName("error_code")
    @Expose
    private int errorCode;

    public InvoiceResponse() {
    }

    protected InvoiceResponse(Parcel in) {
        this.minOrderPrice = in.readDouble();
        this.currency = in.readString();
        this.payment = in.readString();
        this.orderPayment = in.readParcelable(OrderPaymentDetail.class.getClassLoader());
        this.store = in.readParcelable(UserDetail.class.getClassLoader());
        this.serverTime = in.readString();
        this.timezone = in.readString();
        this.success = in.readByte() != 0;
        this.service = in.readParcelable(Service.class.getClassLoader());
        this.message = in.readInt();
        this.errorCode = in.readInt();
    }

    public double getMinOrderPrice() {
        return minOrderPrice;
    }

    public void setMinOrderPrice(double minOrderPrice) {
        this.minOrderPrice = minOrderPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public OrderPaymentDetail getOrderPayment() {
        return orderPayment;
    }

    public void setOrderPayment(OrderPaymentDetail orderPayment) {
        this.orderPayment = orderPayment;
    }

    public UserDetail getStore() {
        return store;
    }

    public void setStore(UserDetail store) {
        this.store = store;
    }

    public String getServerTime() {
        return serverTime;
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public int getMessage() {
        return message;
    }

    public void setMessage(int message) {
        this.message = message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.minOrderPrice);
        dest.writeString(this.currency);
        dest.writeString(this.payment);
        dest.writeParcelable(this.orderPayment, flags);
        dest.writeParcelable(this.store, flags);
        dest.writeString(this.serverTime);
        dest.writeString(this.timezone);
        dest.writeByte(this.success ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.service, flags);
        dest.writeInt(this.message);
        dest.writeInt(this.errorCode);
    }
}