package com.edelivery.store.models.responsemodel;

import com.edelivery.store.models.datamodel.Order;
import com.edelivery.store.models.datamodel.ProviderDetail;
import com.edelivery.store.models.datamodel.UserDetail;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class HistoryDetailsResponse {

    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("payment_gateway_name")
    @Expose
    private String paymentGatewayName;
    @SerializedName("user_detail")
    @Expose
    private UserDetail userDetail;
    @SerializedName("message")
    @Expose
    private int message;
    @SerializedName("order_list")
    @Expose
    private Order order;
    @SerializedName("provider_detail")
    @Expose
    private ProviderDetail providerDetail;
    @SerializedName("error_code")
    @Expose
    private int errorCode;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPaymentGatewayName() {
        return paymentGatewayName;
    }

    public void setPaymentGatewayName(String paymentGatewayName) {
        this.paymentGatewayName = paymentGatewayName;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public UserDetail getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(UserDetail userDetail) {
        this.userDetail = userDetail;
    }

    public int getMessage() {
        return message;
    }

    public void setMessage(int message) {
        this.message = message;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public ProviderDetail getProviderDetail() {
        return providerDetail;
    }

    public void setProviderDetail(ProviderDetail providerDetail) {
        this.providerDetail = providerDetail;
    }

    @Override
    public String toString() {
        return
                "HistoryDetailsResponse{" +
                        "success = '" + success + '\'' +
                        ",user_detail = '" + userDetail + '\'' +
                        ",message = '" + message + '\'' +
                        ",order_list = '" + order + '\'' +
                        ",provider_detail = '" + providerDetail + '\'' +
                        "}";
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}