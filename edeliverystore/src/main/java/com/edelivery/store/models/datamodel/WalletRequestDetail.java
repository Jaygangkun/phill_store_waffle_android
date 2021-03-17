package com.edelivery.store.models.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class WalletRequestDetail implements Parcelable {


    public static final Creator<WalletRequestDetail> CREATOR = new
            Creator<WalletRequestDetail>() {
                @Override
                public WalletRequestDetail createFromParcel(Parcel source) {
                    return new WalletRequestDetail(source);
                }

                @Override
                public WalletRequestDetail[] newArray(int size) {
                    return new WalletRequestDetail[size];
                }
            };
    @SerializedName("transaction_date")
    private String transactionDate;
    @SerializedName("unique_id")
    private int uniqueId;
    @SerializedName("user_unique_id")
    private int userUniqueId;
    @SerializedName("after_total_wallet_amount")
    private double afterTotalWalletAmount;
    @SerializedName("wallet_to_admin_current_rate")
    private double walletToAdminCurrentRate;
    @SerializedName("approved_requested_wallet_amount")
    private double approvedRequestedWalletAmount;
    @SerializedName("is_payment_mode_cash")
    private boolean isPaymentModeCash;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("wallet_status")
    private int walletStatus;
    @SerializedName("transaction_details")
    private String transactionDetails;
    @SerializedName("description_for_request_wallet_amount")
    private String descriptionForRequestWalletAmount;
    @SerializedName("wallet_currency_code")
    private String walletCurrencyCode;
    @SerializedName("completed_date")
    private Object completedDate;
    @SerializedName("wallet_request_cancelled_by")
    private int walletRequestCancelledBy;
    @SerializedName("total_wallet_amount")
    private double totalWalletAmount;
    @SerializedName("requested_wallet_amount")
    private double requestedWalletAmount;
    @SerializedName("user_type")
    private int userType;
    @SerializedName("updated_at")
    private String updatedAt;
    @SerializedName("wallet_request_cancelled_id")
    private String walletRequestCancelledId;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("admin_currency_code")
    private String adminCurrencyCode;
    @SerializedName("_id")
    private String id;
    @SerializedName("country_id")
    private String countryId;

    public WalletRequestDetail() {
    }

    protected WalletRequestDetail(Parcel in) {
        this.transactionDate = in.readString();
        this.uniqueId = in.readInt();
        this.userUniqueId = in.readInt();
        this.afterTotalWalletAmount = in.readDouble();
        this.walletToAdminCurrentRate = in.readDouble();
        this.approvedRequestedWalletAmount = in.readDouble();
        this.isPaymentModeCash = in.readByte() != 0;
        this.createdAt = in.readString();
        this.walletStatus = in.readInt();
        this.transactionDetails = in.readString();
        this.descriptionForRequestWalletAmount = in.readString();
        this.walletCurrencyCode = in.readString();
        this.walletRequestCancelledBy = in.readInt();
        this.totalWalletAmount = in.readDouble();
        this.requestedWalletAmount = in.readDouble();
        this.userType = in.readInt();
        this.updatedAt = in.readString();
        this.walletRequestCancelledId = in.readString();
        this.userId = in.readString();
        this.adminCurrencyCode = in.readString();
        this.id = in.readString();
        this.countryId = in.readString();
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
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

    public double getAfterTotalWalletAmount() {
        return afterTotalWalletAmount;
    }

    public void setAfterTotalWalletAmount(double afterTotalWalletAmount) {
        this.afterTotalWalletAmount = afterTotalWalletAmount;
    }

    public double getWalletToAdminCurrentRate() {
        return walletToAdminCurrentRate;
    }

    public void setWalletToAdminCurrentRate(double walletToAdminCurrentRate) {
        this.walletToAdminCurrentRate = walletToAdminCurrentRate;
    }

    public double getApprovedRequestedWalletAmount() {
        return approvedRequestedWalletAmount;
    }

    public void setApprovedRequestedWalletAmount(double approvedRequestedWalletAmount) {
        this.approvedRequestedWalletAmount = approvedRequestedWalletAmount;
    }

    public boolean isPaymentModeCash() {
        return isPaymentModeCash;
    }

    public void setPaymentModeCash(boolean paymentModeCash) {
        isPaymentModeCash = paymentModeCash;
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

    public int getWalletRequestCancelledBy() {
        return walletRequestCancelledBy;
    }

    public void setWalletRequestCancelledBy(int walletRequestCancelledBy) {
        this.walletRequestCancelledBy = walletRequestCancelledBy;
    }

    public double getTotalWalletAmount() {
        return totalWalletAmount;
    }

    public void setTotalWalletAmount(double totalWalletAmount) {
        this.totalWalletAmount = totalWalletAmount;
    }

    public double getRequestedWalletAmount() {
        return requestedWalletAmount;
    }

    public void setRequestedWalletAmount(double requestedWalletAmount) {
        this.requestedWalletAmount = requestedWalletAmount;
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

    public String getWalletRequestCancelledId() {
        return walletRequestCancelledId;
    }

    public void setWalletRequestCancelledId(String walletRequestCancelledId) {
        this.walletRequestCancelledId = walletRequestCancelledId;
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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.transactionDate);
        dest.writeInt(this.uniqueId);
        dest.writeInt(this.userUniqueId);
        dest.writeDouble(this.afterTotalWalletAmount);
        dest.writeDouble(this.walletToAdminCurrentRate);
        dest.writeDouble(this.approvedRequestedWalletAmount);
        dest.writeByte(this.isPaymentModeCash ? (byte) 1 : (byte) 0);
        dest.writeString(this.createdAt);
        dest.writeInt(this.walletStatus);
        dest.writeString(this.transactionDetails);
        dest.writeString(this.descriptionForRequestWalletAmount);
        dest.writeString(this.walletCurrencyCode);
        dest.writeInt(this.walletRequestCancelledBy);
        dest.writeDouble(this.totalWalletAmount);
        dest.writeDouble(this.requestedWalletAmount);
        dest.writeInt(this.userType);
        dest.writeString(this.updatedAt);
        dest.writeString(this.walletRequestCancelledId);
        dest.writeString(this.userId);
        dest.writeString(this.adminCurrencyCode);
        dest.writeString(this.id);
        dest.writeString(this.countryId);
    }
}