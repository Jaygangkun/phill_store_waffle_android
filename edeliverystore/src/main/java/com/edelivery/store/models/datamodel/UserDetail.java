package com.edelivery.store.models.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class UserDetail implements Parcelable {

    public static final Creator<UserDetail> CREATOR = new Creator<UserDetail>() {
        @Override
        public UserDetail createFromParcel(Parcel source) {
            return new UserDetail(source);
        }

        @Override
        public UserDetail[] newArray(int size) {
            return new UserDetail[size];
        }
    };
    @SerializedName("name")
    private String name;
    @SerializedName("is_user_type_approved")
    @Expose
    private boolean isUserTypeApproved;
    @SerializedName("is_document_uploaded")
    @Expose
    private boolean isDocumentUploaded;
    @SerializedName("is_email_verified")
    @Expose
    private boolean isEmailVerified;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("device_type")
    @Expose
    private String deviceType;
    @SerializedName("is_referral")
    @Expose
    private boolean isReferral;
    @SerializedName("wallet_currency_code")
    @Expose
    private String walletCurrencyCode;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("user_type")
    @Expose
    private int userType;
    @SerializedName("country_phone_code")
    @Expose
    private String countryPhoneCode;
    @SerializedName("user_rate")
    @Expose
    private double rate;
    @SerializedName("is_phone_number_verified")
    @Expose
    private boolean isPhoneNumberVerified;
    @SerializedName("total_referrals")
    @Expose
    private int totalReferrals;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("unique_id")
    @Expose
    private int uniqueId;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("wallet")
    @Expose
    private double wallet;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("server_token")
    @Expose
    private String serverToken;
    @SerializedName("rate_count")
    @Expose
    private int rateCount;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("device_token")
    @Expose
    private String deviceToken;
    @SerializedName("is_approved")
    @Expose
    private boolean isApproved;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("country_id")
    @Expose
    private String countryId;
    @SerializedName("is_use_wallet")
    @Expose
    private boolean isUseWallet;
    @SerializedName("city_id")
    @Expose
    private String cityId;

    public UserDetail() {
    }

    protected UserDetail(Parcel in) {
        this.name = in.readString();
        this.isUserTypeApproved = in.readByte() != 0;
        this.isDocumentUploaded = in.readByte() != 0;
        this.isEmailVerified = in.readByte() != 0;
        this.createdAt = in.readString();
        this.deviceType = in.readString();
        this.isReferral = in.readByte() != 0;
        this.walletCurrencyCode = in.readString();
        this.password = in.readString();
        this.userType = in.readInt();
        this.countryPhoneCode = in.readString();
        this.rate = in.readDouble();
        this.isPhoneNumberVerified = in.readByte() != 0;
        this.totalReferrals = in.readInt();
        this.firstName = in.readString();
        this.email = in.readString();
        this.uniqueId = in.readInt();
        this.address = in.readString();
        this.wallet = in.readDouble();
        this.imageUrl = in.readString();
        this.lastName = in.readString();
        this.serverToken = in.readString();
        this.rateCount = in.readInt();
        this.phone = in.readString();
        this.deviceToken = in.readString();
        this.isApproved = in.readByte() != 0;
        this.id = in.readString();
        this.countryId = in.readString();
        this.isUseWallet = in.readByte() != 0;
        this.cityId = in.readString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isIsUserTypeApproved() {
        return isUserTypeApproved;
    }

    public void setIsUserTypeApproved(boolean isUserTypeApproved) {
        this.isUserTypeApproved = isUserTypeApproved;
    }

    public boolean isIsDocumentUploaded() {
        return isDocumentUploaded;
    }

    public void setIsDocumentUploaded(boolean isDocumentUploaded) {
        this.isDocumentUploaded = isDocumentUploaded;
    }

    public boolean isIsEmailVerified() {
        return isEmailVerified;
    }

    public void setIsEmailVerified(boolean isEmailVerified) {
        this.isEmailVerified = isEmailVerified;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }


    public boolean isIsReferral() {
        return isReferral;
    }

    public void setIsReferral(boolean isReferral) {
        this.isReferral = isReferral;
    }

    public String getWalletCurrencyCode() {
        return walletCurrencyCode;
    }

    public void setWalletCurrencyCode(String walletCurrencyCode) {
        this.walletCurrencyCode = walletCurrencyCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }


    public String getCountryPhoneCode() {
        return countryPhoneCode;
    }

    public void setCountryPhoneCode(String countryPhoneCode) {
        this.countryPhoneCode = countryPhoneCode;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public boolean isIsPhoneNumberVerified() {
        return isPhoneNumberVerified;
    }

    public void setIsPhoneNumberVerified(boolean isPhoneNumberVerified) {
        this.isPhoneNumberVerified = isPhoneNumberVerified;
    }


    public int getTotalReferrals() {
        return totalReferrals;
    }

    public void setTotalReferrals(int totalReferrals) {
        this.totalReferrals = totalReferrals;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(int uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getWallet() {
        return wallet;
    }

    public void setWallet(double wallet) {
        this.wallet = wallet;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getServerToken() {
        return serverToken;
    }

    public void setServerToken(String serverToken) {
        this.serverToken = serverToken;
    }


    public int getRateCount() {
        return rateCount;
    }

    public void setRateCount(int rateCount) {
        this.rateCount = rateCount;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }


    public boolean isIsApproved() {
        return isApproved;
    }

    public void setIsApproved(boolean isApproved) {
        this.isApproved = isApproved;
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

    public boolean isIsUseWallet() {
        return isUseWallet;
    }

    public void setIsUseWallet(boolean isUseWallet) {
        this.isUseWallet = isUseWallet;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeByte(this.isUserTypeApproved ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isDocumentUploaded ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isEmailVerified ? (byte) 1 : (byte) 0);
        dest.writeString(this.createdAt);
        dest.writeString(this.deviceType);
        dest.writeByte(this.isReferral ? (byte) 1 : (byte) 0);
        dest.writeString(this.walletCurrencyCode);
        dest.writeString(this.password);
        dest.writeInt(this.userType);
        dest.writeString(this.countryPhoneCode);
        dest.writeDouble(this.rate);
        dest.writeByte(this.isPhoneNumberVerified ? (byte) 1 : (byte) 0);
        dest.writeInt(this.totalReferrals);
        dest.writeString(this.firstName);
        dest.writeString(this.email);
        dest.writeInt(this.uniqueId);
        dest.writeString(this.address);
        dest.writeDouble(this.wallet);
        dest.writeString(this.imageUrl);
        dest.writeString(this.lastName);
        dest.writeString(this.serverToken);
        dest.writeInt(this.rateCount);
        dest.writeString(this.phone);
        dest.writeString(this.deviceToken);
        dest.writeByte(this.isApproved ? (byte) 1 : (byte) 0);
        dest.writeString(this.id);
        dest.writeString(this.countryId);
        dest.writeByte(this.isUseWallet ? (byte) 1 : (byte) 0);
        dest.writeString(this.cityId);
    }
}