package com.edelivery.store.models.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class ProductDayTime implements Parcelable {

    public static final Creator<ProductDayTime> CREATOR = new Creator<ProductDayTime>() {
        @Override
        public ProductDayTime createFromParcel(Parcel source) {
            return new ProductDayTime(source);
        }

        @Override
        public ProductDayTime[] newArray(int size) {
            return new ProductDayTime[size];
        }
    };
    @SerializedName("product_open_time")
    private String productOpenTime;
    @SerializedName("product_close_time")
    private String productCloseTime;

    public ProductDayTime() {
    }

    protected ProductDayTime(Parcel in) {
        this.productOpenTime = in.readString();
        this.productCloseTime = in.readString();
    }

    public String getProductOpenTime() {
        return productOpenTime;
    }

    public void setProductOpenTime(String productOpenTime) {
        this.productOpenTime = productOpenTime;
    }

    public String getProductCloseTime() {
        return productCloseTime;
    }

    public void setProductCloseTime(String productCloseTime) {
        this.productCloseTime = productCloseTime;
    }

    @Override
    public String toString() {
        return
                "DayTime{" +
                        "product_open_time = '" + productOpenTime + '\'' +
                        ",product_close_time = '" + productCloseTime + '\'' +
                        "}";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.productOpenTime);
        dest.writeString(this.productCloseTime);
    }
}