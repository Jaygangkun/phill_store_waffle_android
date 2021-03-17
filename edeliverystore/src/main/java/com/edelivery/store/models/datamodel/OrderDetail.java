package com.edelivery.store.models.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderDetail {


    @SerializedName("provider_detail")
    private ProviderDetail providerDetail;

    @SerializedName("request_detail")
    private RequestDetail requestDetail;

    @SerializedName("order")
    @Expose
    private Order order;

    @SerializedName("order_payment_detail")
    private OrderPaymentDetail orderPaymentDetail;

    @SerializedName("cart_detail")
    @Expose
    private OrderData cartDetail;

    @SerializedName("user_id")
    @Expose
    private String userId;

    @SerializedName("is_confirmation_code_required_at_pickup_delivery")
    private boolean isConfirmationCodeRequiredAtPickupDelivery;
    @SerializedName("is_confirmation_code_required_at_complete_delivery")
    private boolean isConfirmationCodeRequiredAtCompleteDelivery;

    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("order_count")
    @Expose
    private double orderCount;

    public double getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(double orderCount) {
        this.orderCount = orderCount;
    }

    public OrderPaymentDetail getOrderPaymentDetail() {
        return orderPaymentDetail;
    }

    public OrderData getCartDetail() {
        return cartDetail;
    }

    public String getUserId() {
        return userId;
    }

    public boolean isConfirmationCodeRequiredAtPickupDelivery() {
        return isConfirmationCodeRequiredAtPickupDelivery;
    }

    public boolean isConfirmationCodeRequiredAtCompleteDelivery() {
        return isConfirmationCodeRequiredAtCompleteDelivery;
    }

    public String getCurrency() {
        return currency;
    }

    public Order getOrder() {
        return order;
    }
    public RequestDetail getRequestDetail(){
        return requestDetail;
    }

    public ProviderDetail getProviderDetail() {
        return providerDetail;
    }


}