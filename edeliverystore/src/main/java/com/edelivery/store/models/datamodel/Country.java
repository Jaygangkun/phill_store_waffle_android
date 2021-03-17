package com.edelivery.store.models.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class Country implements Serializable {


    @SerializedName("is_referral_store")
    @Expose
    private boolean isReferralStore;

    @SerializedName("is_business")
    @Expose
    private boolean isBusiness;

    @SerializedName("currency_name")
    @Expose
    private String currencyName;

    @SerializedName("country_timezone")
    @Expose
    private ArrayList<String> countryTimezone;

    @SerializedName("no_of_user_use_referral")
    @Expose
    private int noOfUserUseReferral;

    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @SerializedName("currency_code")
    @Expose
    private String currencyCode;

    @SerializedName("country_code")
    @Expose
    private String countryCode;


    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    @SerializedName("country_phone_code")
    @Expose
    private String countryPhoneCode;

    @SerializedName("currency_sign")
    @Expose
    private String currencySign;

    @SerializedName("__v")
    @Expose
    private int V;

    @SerializedName("country_name")
    @Expose
    private String countryName;

    @SerializedName("_id")
    @Expose
    private String id;

    @SerializedName("is_distance_unit_mile")
    @Expose
    private boolean isDistanceUnitMile;

    @SerializedName("maximum_phone_number_length")
    @Expose
    private int maxPhoneNumberLength;

    @SerializedName("country_flag")
    @Expose
    private String countryFlag;

    @SerializedName("minimum_phone_number_length")
    @Expose
    private int minPhoneNumberLength;
    @SerializedName("referral_bonus_to_user")
    @Expose
    private double referralBonusToUser;
    @SerializedName("bonus_to_user_referral")
    @Expose
    private double bonusToUserReferral;

    public boolean isIsBusiness() {
        return isBusiness;
    }

    public void setIsBusiness(boolean isBusiness) {
        this.isBusiness = isBusiness;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public ArrayList<String> getCountryTimezone() {
        return countryTimezone;
    }

    public void setCountryTimezone(ArrayList<String> countryTimezone) {
        this.countryTimezone = countryTimezone;
    }

    public int getNoOfUserUseReferral() {
        return noOfUserUseReferral;
    }

    public void setNoOfUserUseReferral(int noOfUserUseReferral) {
        this.noOfUserUseReferral = noOfUserUseReferral;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public double getReferralBonusToUser() {
        return referralBonusToUser;
    }

    public void setReferralBonusToUser(double referralBonusToUser) {
        this.referralBonusToUser = referralBonusToUser;
    }

    public double getBonusToUserReferral() {
        return bonusToUserReferral;
    }

    public void setBonusToUserReferral(double bonusToUserReferral) {
        this.bonusToUserReferral = bonusToUserReferral;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCountryPhoneCode() {
        return countryPhoneCode;
    }

    public void setCountryPhoneCode(String countryPhoneCode) {
        this.countryPhoneCode = countryPhoneCode;
    }

    public String getCurrencySign() {
        return currencySign;
    }

    public void setCurrencySign(String currencySign) {
        this.currencySign = currencySign;
    }

    public int getV() {
        return V;
    }

    public void setV(int V) {
        this.V = V;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isIsDistanceUnitMile() {
        return isDistanceUnitMile;
    }

    public void setIsDistanceUnitMile(boolean isDistanceUnitMile) {
        this.isDistanceUnitMile = isDistanceUnitMile;
    }

    public int getMaxPhoneNumberLength() {
        return maxPhoneNumberLength;
    }

    public void setMaxPhoneNumberLength(int maxPhoneNumberLength) {
        this.maxPhoneNumberLength = maxPhoneNumberLength;
    }

    public String getCountryFlag() {
        return countryFlag;
    }

    public void setCountryFlag(String countryFlag) {
        this.countryFlag = countryFlag;
    }

    public int getMinPhoneNumberLength() {
        return minPhoneNumberLength;
    }

    public void setMinPhoneNumberLength(int minPhoneNumberLength) {
        this.minPhoneNumberLength = minPhoneNumberLength;
    }

    public boolean isReferralStore() {
        return isReferralStore;
    }

    public void setReferralStore(boolean referralStore) {
        isReferralStore = referralStore;
    }
}