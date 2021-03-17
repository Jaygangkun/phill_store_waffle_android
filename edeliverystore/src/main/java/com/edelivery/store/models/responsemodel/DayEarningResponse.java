package com.edelivery.store.models.responsemodel;

import com.edelivery.store.models.datamodel.OrderPaymentDetail;
import com.edelivery.store.models.datamodel.OrderTotal;
import com.edelivery.store.models.datamodel.StoreAnalyticDaily;
import com.edelivery.store.models.datamodel.WeekData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DayEarningResponse {


    @SerializedName("currency")
    private String currency;

    @SerializedName("success")
    private boolean success;

    @SerializedName("order_payments")
    private List<OrderPaymentDetail> orderPayments;

    @SerializedName("order_total")
    private OrderTotal orderTotal;

    @SerializedName("message")
    private int message;

    @SerializedName("store_analytic_daily")
    private StoreAnalyticDaily storeAnalyticDaily;

    @SerializedName("store_analytic_weekly")
    private StoreAnalyticDaily storeAnalyticWeekly;

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setOrderPayments(List<OrderPaymentDetail> orderPayments) {
        this.orderPayments = orderPayments;
    }

    public List<OrderPaymentDetail> getOrderPayments() {
        return orderPayments;
    }

    public void setOrderTotal(OrderTotal orderTotal) {
        this.orderTotal = orderTotal;
    }

    public OrderTotal getOrderTotal() {
        return orderTotal;
    }

    public void setMessage(int message) {
        this.message = message;
    }

    public int getMessage() {
        return message;
    }

    public void setStoreAnalyticDaily(StoreAnalyticDaily storeAnalyticDaily) {
        this.storeAnalyticDaily = storeAnalyticDaily;
    }

    public StoreAnalyticDaily getStoreAnalyticDaily() {
        return storeAnalyticDaily;
    }

    @SerializedName("error_code")
    @Expose
    private int errorCode;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }


    @SerializedName("order_day_total")
    private WeekData dayOfWeekOrderTotal;

    @SerializedName("date")
    private WeekData dayOfWeekDate;

    public WeekData getDayOfWeekOrderTotal() {
        return dayOfWeekOrderTotal;
    }

    public void setDayOfWeekOrderTotal(WeekData dayOfWeekOrderTotal) {
        this.dayOfWeekOrderTotal = dayOfWeekOrderTotal;
    }

    public WeekData getDayOfWeekDate() {
        return dayOfWeekDate;
    }

    public void setDayOfWeekDate(WeekData dayOfWeekDate) {
        this.dayOfWeekDate = dayOfWeekDate;
    }

    public StoreAnalyticDaily getStoreAnalyticWeekly() {
        return storeAnalyticWeekly;
    }

    public void setStoreAnalyticWeekly(StoreAnalyticDaily storeAnalyticWeekly) {
        this.storeAnalyticWeekly = storeAnalyticWeekly;
    }
}