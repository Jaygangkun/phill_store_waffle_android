package com.edelivery.store.models.responsemodel;

import com.edelivery.store.models.datamodel.City;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by elluminati on 11-Jan-18.
 */

public class CityResponse {
    @SerializedName("error_code")
    @Expose
    private int errorCode;
    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("message")
    @Expose
    private int message;
    @SerializedName("cities")
    @Expose
    private ArrayList<City> cities;


    @SerializedName("is_referral_store")
    @Expose
    private boolean isReferralStore;


    @SerializedName("currency_sign")
    @Expose
    private String currencySign;

    @SerializedName("country_phone_code")
    @Expose
    private String countryPhoneCode;

    @SerializedName("minimum_phone_number_length")
    @Expose
    private int minimumPhoneNumberLength;
    @SerializedName("maximum_phone_number_length")
    @Expose
    private int maximumPhoneNumberLength;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getMessage() {
        return message;
    }

    public void setMessage(int message) {
        this.message = message;
    }

    public ArrayList<City> getCities() {
        return cities;
    }

    public void setCities(ArrayList<City> cities) {
        this.cities = cities;
    }

    public boolean isReferralStore() {
        return isReferralStore;
    }

    public void setReferralStore(boolean referralStore) {
        isReferralStore = referralStore;
    }

    public String getCurrencySign() {
        return currencySign;
    }

    public void setCurrencySign(String currencySign) {
        this.currencySign = currencySign;
    }

    public String getCountryPhoneCode() {
        return countryPhoneCode;
    }

    public void setCountryPhoneCode(String countryPhoneCode) {
        this.countryPhoneCode = countryPhoneCode;
    }

    public int getMinimumPhoneNumberLength() {
        return minimumPhoneNumberLength;
    }

    public void setMinimumPhoneNumberLength(int minimumPhoneNumberLength) {
        this.minimumPhoneNumberLength = minimumPhoneNumberLength;
    }

    public int getMaximumPhoneNumberLength() {
        return maximumPhoneNumberLength;
    }

    public void setMaximumPhoneNumberLength(int maximumPhoneNumberLength) {
        this.maximumPhoneNumberLength = maximumPhoneNumberLength;
    }

}
