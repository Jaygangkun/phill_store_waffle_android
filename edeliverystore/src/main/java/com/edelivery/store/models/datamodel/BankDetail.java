package com.edelivery.store.models.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class BankDetail implements Parcelable {

    public static final Creator<BankDetail> CREATOR = new Creator<BankDetail>() {
        @Override
        public BankDetail createFromParcel(Parcel source) {
            return new BankDetail(source);
        }

        @Override
        public BankDetail[] newArray(int size) {
            return new BankDetail[size];
        }
    };
    @SerializedName("business_name")
    private String businessName;
    @SerializedName("server_token")
    private String serverToken;
    @SerializedName("is_selected")
    private boolean isSelected;
    @SerializedName("_id")

    private String Id;
    @SerializedName("bank_detail_id")
    private String bankDetailId;
    @SerializedName("bank_holder_id")
    private String bankHolderId;
    @SerializedName("bank_holder_type")
    private int bankHolderType;
    @SerializedName("account_holder_type")
    private String accountHolderType;
    @SerializedName("personal_id_number")
    private String personalIdNumber;
    @SerializedName("routing_number")
    private String routingNumber;
    @SerializedName("dob")
    private String dob;
    @SerializedName("document")
    private String document;
    @SerializedName("social_id")
    private String socialId;
    @SerializedName("account_number")
    private String accountNumber;
    @SerializedName("bank_account_holder_name")
    private String bankAccountHolderName;
    @SerializedName("password")
    private String password;

    public BankDetail() {
    }

    protected BankDetail(Parcel in) {
        this.serverToken = in.readString();
        this.isSelected = in.readByte() != 0;
        this.Id = in.readString();
        this.bankDetailId = in.readString();
        this.bankHolderId = in.readString();
        this.bankHolderType = in.readInt();
        this.accountHolderType = in.readString();
        this.personalIdNumber = in.readString();
        this.routingNumber = in.readString();
        this.dob = in.readString();
        this.document = in.readString();
        this.socialId = in.readString();
        this.accountNumber = in.readString();
        this.bankAccountHolderName = in.readString();
        this.password = in.readString();
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getServerToken() {
        return serverToken;
    }

    public void setServerToken(String serverToken) {
        this.serverToken = serverToken;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getBankDetailId() {
        return bankDetailId;
    }

    public void setBankDetailId(String bankDetailId) {
        this.bankDetailId = bankDetailId;
    }

    public String getBankHolderId() {
        return bankHolderId;
    }

    public void setBankHolderId(String bankHolderId) {
        this.bankHolderId = bankHolderId;
    }

    public int getBankHolderType() {
        return bankHolderType;
    }

    public void setBankHolderType(int bankHolderType) {
        this.bankHolderType = bankHolderType;
    }

    public String getAccountHolderType() {
        return accountHolderType;
    }

    public void setAccountHolderType(String accountHolderType) {
        this.accountHolderType = accountHolderType;
    }

    public String getPersonalIdNumber() {
        return personalIdNumber;
    }

    public void setPersonalIdNumber(String personalIdNumber) {
        this.personalIdNumber = personalIdNumber;
    }

    public String getRoutingNumber() {
        return routingNumber;
    }

    public void setRoutingNumber(String routingNumber) {
        this.routingNumber = routingNumber;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getSocialId() {
        return socialId;
    }

    public void setSocialId(String socialId) {
        this.socialId = socialId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBankAccountHolderName() {
        return bankAccountHolderName;
    }

    public void setBankAccountHolderName(String bankAccountHolderName) {
        this.bankAccountHolderName = bankAccountHolderName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.serverToken);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
        dest.writeString(this.Id);
        dest.writeString(this.bankDetailId);
        dest.writeString(this.bankHolderId);
        dest.writeInt(this.bankHolderType);
        dest.writeString(this.accountHolderType);
        dest.writeString(this.personalIdNumber);
        dest.writeString(this.routingNumber);
        dest.writeString(this.dob);
        dest.writeString(this.document);
        dest.writeString(this.socialId);
        dest.writeString(this.accountNumber);
        dest.writeString(this.bankAccountHolderName);
        dest.writeString(this.password);
    }
}