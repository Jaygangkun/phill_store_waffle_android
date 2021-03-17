package com.edelivery.store.models.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class ProductTime implements Parcelable {

    public static final Creator<ProductTime> CREATOR = new Creator<ProductTime>() {
        @Override
        public ProductTime createFromParcel(Parcel source) {
            return new ProductTime(source);
        }

        @Override
        public ProductTime[] newArray(int size) {
            return new ProductTime[size];
        }
    };
    @SerializedName("is_product_open_full_time")
    private boolean isProductOpenFullTime;
    @SerializedName("is_product_open")
    private boolean isProductOpen;
    @SerializedName("day")
    private int day;
    @SerializedName("day_time")
    private List<ProductDayTime> dayTime;

    public ProductTime() {
    }

    protected ProductTime(Parcel in) {
        this.isProductOpenFullTime = in.readByte() != 0;
        this.isProductOpen = in.readByte() != 0;
        this.day = in.readInt();
        this.dayTime = in.createTypedArrayList(ProductDayTime.CREATOR);
    }

    public boolean isProductOpenFullTime() {
        return isProductOpenFullTime;
    }

    public void setProductOpenFullTime(boolean storeOpenFullTime) {
        isProductOpenFullTime = storeOpenFullTime;
    }

    public boolean isProductOpen() {
        return isProductOpen;
    }

    public void setProductOpen(boolean storeOpen) {
        isProductOpen = storeOpen;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public List<ProductDayTime> getDayTime() {
        return dayTime;
    }

    public void setDayTime(List<ProductDayTime> dayTime) {
        this.dayTime = dayTime;
    }

    @Override
    public String toString() {
        return
                "ProductTime{" +
                        "is_store_open_full_time = '" + isProductOpenFullTime + '\'' +
                        ",is_store_open = '" + isProductOpen + '\'' +
                        ",day = '" + day + '\'' +
                        ",day_time = '" + dayTime + '\'' +
                        "}";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isProductOpenFullTime ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isProductOpen ? (byte) 1 : (byte) 0);
        dest.writeInt(this.day);
        dest.writeTypedList(this.dayTime);
    }
}