package com.edelivery.store.models.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ProviderDetail implements Parcelable {


    public static final Creator<ProviderDetail> CREATOR = new Creator<ProviderDetail>() {
        @Override
        public ProviderDetail createFromParcel(Parcel source) {
            return new ProviderDetail(source);
        }

        @Override
        public ProviderDetail[] newArray(int size) {
            return new ProviderDetail[size];
        }
    };
    @SerializedName("_id")
    private String id;

    @SerializedName("phone")
    private String phone;
    @SerializedName("country_phone_code")
    private String countryPhoneCode;
    @SerializedName("user_rate")
    private double rate;
    @SerializedName("provider_location")
    @Expose
    private List<Double> providerLocation;
    @SerializedName("bearing")
    private float bearing;
    @SerializedName("image_url")
    private String imageUrl;
    @SerializedName("last_name")
    private String lastName = "";
    @SerializedName("first_name")
    private String firstName = "";

    public ProviderDetail() {
    }

    protected ProviderDetail(Parcel in) {
        this.phone = in.readString();
        this.countryPhoneCode = in.readString();
        this.rate = in.readDouble();
        this.providerLocation = new ArrayList<Double>();
        in.readList(this.providerLocation, Double.class.getClassLoader());
        this.bearing = in.readFloat();
        this.imageUrl = in.readString();
        this.lastName = in.readString();
        this.firstName = in.readString();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public float getBearing() {
        return bearing;
    }

    public void setBearing(float bearing) {
        this.bearing = bearing;
    }

    public void setBearing(int bearing) {
        this.bearing = bearing;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String toString() {
        return
                "ProviderDetail{" +
                        "phone = '" + phone + '\'' +
                        ",country_phone_code = '" + countryPhoneCode + '\'' +
                        ",rate = '" + rate + '\'' +
                        ",bearing = '" + bearing + '\'' +
                        ",image_url = '" + imageUrl + '\'' +
                        ",last_name = '" + lastName + '\'' +
                        ",first_name = '" + firstName + '\'' +
                        "}";
    }

    public List<Double> getProviderLocation() {
        return providerLocation;
    }

    public void setProviderLocation(List<Double> providerLocation) {
        this.providerLocation = providerLocation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.phone);
        dest.writeString(this.countryPhoneCode);
        dest.writeDouble(this.rate);
        dest.writeList(this.providerLocation);
        dest.writeFloat(this.bearing);
        dest.writeString(this.imageUrl);
        dest.writeString(this.lastName);
        dest.writeString(this.firstName);
    }

    public String getId() {
        return id;
    }
}