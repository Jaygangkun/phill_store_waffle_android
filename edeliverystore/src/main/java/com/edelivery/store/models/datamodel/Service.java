package com.edelivery.store.models.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Service implements Parcelable {


    @SerializedName("surge_hours")
    @Expose
    private List<SurgeHours> surgeHours;


    @SerializedName("cancellation_fee")
    @Expose
    private double cancellationFee;

    @SerializedName("delivery_type")
    @Expose
    private int deliveryType;

    @SerializedName("is_surge_hours")
    @Expose
    private boolean isSurgeHours;


    @SerializedName("is_business")
    @Expose
    private boolean isBusiness;

    @SerializedName("unique_id")
    @Expose
    private int uniqueId;


    @SerializedName("_id")
    @Expose
    private String id;

    @SerializedName("is_distance_unit_mile")
    @Expose
    private boolean isDistanceUnitMile;

    @SerializedName("country_id")
    @Expose
    private String countryId;

    @SerializedName("city_id")
    @Expose
    private String cityId;

    public List<SurgeHours> getSurgeHours() {
        return surgeHours;
    }

    public void setSurgeHours(List<SurgeHours> surgeHours) {
        this.surgeHours = surgeHours;
    }

    public double getCancellationFee() {
        return cancellationFee;
    }

    public void setCancellationFee(double cancellationFee) {
        this.cancellationFee = cancellationFee;
    }

    public int getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(int deliveryType) {
        this.deliveryType = deliveryType;
    }

    public boolean isSurgeHours() {
        return isSurgeHours;
    }

    public void setSurgeHours(boolean surgeHours) {
        isSurgeHours = surgeHours;
    }

    public boolean isBusiness() {
        return isBusiness;
    }

    public void setBusiness(boolean business) {
        isBusiness = business;
    }

    public int getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(int uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isDistanceUnitMile() {
        return isDistanceUnitMile;
    }

    public void setDistanceUnitMile(boolean distanceUnitMile) {
        isDistanceUnitMile = distanceUnitMile;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
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
        dest.writeList(this.surgeHours);
        dest.writeDouble(this.cancellationFee);
        dest.writeInt(this.deliveryType);
        dest.writeByte(this.isSurgeHours ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isBusiness ? (byte) 1 : (byte) 0);
        dest.writeInt(this.uniqueId);
        dest.writeString(this.id);
        dest.writeByte(this.isDistanceUnitMile ? (byte) 1 : (byte) 0);
        dest.writeString(this.countryId);
        dest.writeString(this.cityId);
    }

    public Service() {
    }

    protected Service(Parcel in) {
        this.surgeHours = new ArrayList<SurgeHours>();
        in.readList(this.surgeHours, SurgeHours.class.getClassLoader());
        this.cancellationFee = in.readDouble();
        this.deliveryType = in.readInt();
        this.isSurgeHours = in.readByte() != 0;
        this.isBusiness = in.readByte() != 0;
        this.uniqueId = in.readInt();
        this.id = in.readString();
        this.isDistanceUnitMile = in.readByte() != 0;
        this.countryId = in.readString();
        this.cityId = in.readString();
    }

    public static final Parcelable.Creator<Service> CREATOR = new Parcelable.Creator<Service>() {
        @Override
        public Service createFromParcel(Parcel source) {
            return new Service(source);
        }

        @Override
        public Service[] newArray(int size) {
            return new Service[size];
        }
    };
}