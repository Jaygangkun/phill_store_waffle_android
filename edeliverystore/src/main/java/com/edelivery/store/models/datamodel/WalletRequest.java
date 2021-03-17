package com.edelivery.store.models.datamodel;

import com.google.gson.annotations.SerializedName;

public class WalletRequest {

    @SerializedName("transaction_date")
    private Object transactionDate;

    @SerializedName("unique_id")
    private int uniqueId;

    @SerializedName("user_unique_id")
    private int userUniqueId;

    @SerializedName("after_total_wallet_amount")
    private int afterTotalWalletAmount;

    @SerializedName("wallet_to_admin_current_rate")
    private int walletToAdminCurrentRate;

    @SerializedName("is_payment_mode_cash")
    private boolean isPaymentModeCash;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("wallet_status")
    private int walletStatus;

    @SerializedName("approved_requested_wallet_amount")
    private int approvedRequestedWalletAmount;

    @SerializedName("transaction_details")
    private String transactionDetails;

    @SerializedName("description_for_request_wallet_amount")
    private String descriptionForRequestWalletAmount;

    @SerializedName("wallet_currency_code")
    private String walletCurrencyCode;

    @SerializedName("completed_date")
    private Object completedDate;

    @SerializedName("requested_wallet_amount")
    private int requestedWalletAmount;

    @SerializedName("total_wallet_amount")
    private double totalWalletAmount;

    @SerializedName("user_type")
    private int userType;

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("admin_currency_code")
    private String adminCurrencyCode;

    @SerializedName("__v")
    private int V;

    @SerializedName("_id")
    private String id;

    @SerializedName("country_id")
    private String countryId;

    public Object getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Object transactionDate) {
        this.transactionDate = transactionDate;
    }

    public int getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(int uniqueId) {
        this.uniqueId = uniqueId;
    }

    public int getUserUniqueId() {
        return userUniqueId;
    }

    public void setUserUniqueId(int userUniqueId) {
        this.userUniqueId = userUniqueId;
    }

    public int getAfterTotalWalletAmount() {
        return afterTotalWalletAmount;
    }

    public void setAfterTotalWalletAmount(int afterTotalWalletAmount) {
        this.afterTotalWalletAmount = afterTotalWalletAmount;
    }

    public int getWalletToAdminCurrentRate() {
        return walletToAdminCurrentRate;
    }

    public void setWalletToAdminCurrentRate(int walletToAdminCurrentRate) {
        this.walletToAdminCurrentRate = walletToAdminCurrentRate;
    }

    public boolean isIsPaymentModeCash() {
        return isPaymentModeCash;
    }

    public void setIsPaymentModeCash(boolean isPaymentModeCash) {
        this.isPaymentModeCash = isPaymentModeCash;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getWalletStatus() {
        return walletStatus;
    }

    public void setWalletStatus(int walletStatus) {
        this.walletStatus = walletStatus;
    }

    public int getApprovedRequestedWalletAmount() {
        return approvedRequestedWalletAmount;
    }

    public void setApprovedRequestedWalletAmount(int approvedRequestedWalletAmount) {
        this.approvedRequestedWalletAmount = approvedRequestedWalletAmount;
    }

    public String getTransactionDetails() {
        return transactionDetails;
    }

    public void setTransactionDetails(String transactionDetails) {
        this.transactionDetails = transactionDetails;
    }

    public String getDescriptionForRequestWalletAmount() {
        return descriptionForRequestWalletAmount;
    }

    public void setDescriptionForRequestWalletAmount(String descriptionForRequestWalletAmount) {
        this.descriptionForRequestWalletAmount = descriptionForRequestWalletAmount;
    }

    public String getWalletCurrencyCode() {
        return walletCurrencyCode;
    }

    public void setWalletCurrencyCode(String walletCurrencyCode) {
        this.walletCurrencyCode = walletCurrencyCode;
    }

    public Object getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Object completedDate) {
        this.completedDate = completedDate;
    }

    public int getRequestedWalletAmount() {
        return requestedWalletAmount;
    }

    public void setRequestedWalletAmount(int requestedWalletAmount) {
        this.requestedWalletAmount = requestedWalletAmount;
    }

    public double getTotalWalletAmount() {
        return totalWalletAmount;
    }

    public void setTotalWalletAmount(double totalWalletAmount) {
        this.totalWalletAmount = totalWalletAmount;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAdminCurrencyCode() {
        return adminCurrencyCode;
    }

    public void setAdminCurrencyCode(String adminCurrencyCode) {
        this.adminCurrencyCode = adminCurrencyCode;
    }

    public int getV() {
        return V;
    }

    public void setV(int V) {
        this.V = V;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    @Override
    public String toString() {
        return
                "WalletRequest{" +
                        "transaction_date = '" + transactionDate + '\'' +
                        ",unique_id = '" + uniqueId + '\'' +
                        ",user_unique_id = '" + userUniqueId + '\'' +
                        ",after_total_wallet_amount = '" + afterTotalWalletAmount + '\'' +
                        ",wallet_to_admin_current_rate = '" + walletToAdminCurrentRate + '\'' +
                        ",is_payment_mode_cash = '" + isPaymentModeCash + '\'' +
                        ",created_at = '" + createdAt + '\'' +
                        ",wallet_status = '" + walletStatus + '\'' +
                        ",approved_requsted_wallet_amount = '" + approvedRequestedWalletAmount +
                        '\'' +
                        ",transaction_details = '" + transactionDetails + '\'' +
                        ",description_for_request_wallet_amount = '" +
                        descriptionForRequestWalletAmount + '\'' +
                        ",wallet_currency_code = '" + walletCurrencyCode + '\'' +
                        ",completed_date = '" + completedDate + '\'' +
                        ",requsted_wallet_amount = '" + requestedWalletAmount + '\'' +
                        ",total_wallet_amount = '" + totalWalletAmount + '\'' +
                        ",user_type = '" + userType + '\'' +
                        ",updated_at = '" + updatedAt + '\'' +
                        ",user_id = '" + userId + '\'' +
                        ",admin_currency_code = '" + adminCurrencyCode + '\'' +
                        ",__v = '" + V + '\'' +
                        ",_id = '" + id + '\'' +
                        ",country_id = '" + countryId + '\'' +
                        "}";
    }
}