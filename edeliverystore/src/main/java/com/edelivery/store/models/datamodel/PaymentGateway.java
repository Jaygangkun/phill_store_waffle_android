package com.edelivery.store.models.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentGateway {

    private boolean isSelect;
    @SerializedName("unique_id")
    @Expose
    private int uniqueId;

    @SerializedName("payment_key_id")
    @Expose
    private String paymentKeyId;

    @SerializedName("is_payment_visible")
    @Expose
    private boolean isPaymentVisible;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @SerializedName("is_using_card_details")
    @Expose
    private boolean isUsingCardDetails;

    @SerializedName("payment_key")
    @Expose
    private String paymentKey;

    @SerializedName("is_payment_by_web_url")
    @Expose
    private boolean isPaymentByWebUrl;

    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    @SerializedName("__v")
    @Expose
    private int V;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("is_payment_by_login")
    @Expose
    private boolean isPaymentByLogin;

    @SerializedName("_id")
    @Expose
    private String id;

    @SerializedName("Integration_Key")
    @Expose
    private String Integration_Key;

    @SerializedName("Integration_Password")
    @Expose
    private String Integration_Password;

    @SerializedName("sagpay_baseurl")
    @Expose
    private String sagpay_baseurl;

    @SerializedName("vendorName")
    @Expose
    private String vendorName;

    public void setUniqueId(int uniqueId) {
        this.uniqueId = uniqueId;
    }

    public int getUniqueId() {
        return uniqueId;
    }

    public void setPaymentKeyId(String paymentKeyId) {
        this.paymentKeyId = paymentKeyId;
    }

    public String getPaymentKeyId() {
        return paymentKeyId;
    }

    public void setIsPaymentVisible(boolean isPaymentVisible) {
        this.isPaymentVisible = isPaymentVisible;
    }

    public boolean isIsPaymentVisible() {
        return isPaymentVisible;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setIsUsingCardDetails(boolean isUsingCardDetails) {
        this.isUsingCardDetails = isUsingCardDetails;
    }

    public boolean isIsUsingCardDetails() {
        return isUsingCardDetails;
    }

    public void setPaymentKey(String paymentKey) {
        this.paymentKey = paymentKey;
    }

    public String getPaymentKey() {
        return paymentKey;
    }

    public void setIsPaymentByWebUrl(boolean isPaymentByWebUrl) {
        this.isPaymentByWebUrl = isPaymentByWebUrl;
    }

    public boolean isIsPaymentByWebUrl() {
        return isPaymentByWebUrl;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setV(int V) {
        this.V = V;
    }

    public int getV() {
        return V;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setIsPaymentByLogin(boolean isPaymentByLogin) {
        this.isPaymentByLogin = isPaymentByLogin;
    }

    public boolean isIsPaymentByLogin() {
        return isPaymentByLogin;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    private boolean isPaymentModeCash = false;

    public boolean isPaymentModeCash() {
        return isPaymentModeCash;
    }

    public void setPaymentModeCash(boolean paymentModeCash) {
        isPaymentModeCash = paymentModeCash;
    }

    public String getIntegration_Key() {
        return Integration_Key;
    }

    public void setIntegration_Key(String integration_Key) {
        Integration_Key = integration_Key;
    }

    public String getIntegration_Password() {
        return Integration_Password;
    }

    public void setIntegration_Password(String integration_Password) {
        Integration_Password = integration_Password;
    }

    public String getSagpay_baseurl() {
        return sagpay_baseurl;
    }

    public void setSagpay_baseurl(String sagpay_baseurl) {
        this.sagpay_baseurl = sagpay_baseurl;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }
}