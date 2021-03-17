package com.edelivery.store.models.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class DayTime implements Parcelable {

    public static final Parcelable.Creator<DayTime> CREATOR = new Parcelable
            .Creator<DayTime>() {
        @Override
        public DayTime createFromParcel(Parcel source) {
            return new DayTime(source);
        }

        @Override
        public DayTime[] newArray(int size) {
            return new DayTime[size];
        }
    };
    @SerializedName("store_open_time")
    private String storeOpenTime;
    @SerializedName("store_close_time")
    private String storeCloseTime;

    public DayTime() {
    }

    protected DayTime(Parcel in) {
        this.storeOpenTime = in.readString();
        this.storeCloseTime = in.readString();
    }

    public String getStoreOpenTime() {
        return storeOpenTime;
    }

    public void setStoreOpenTime(String storeOpenTime) {
        this.storeOpenTime = storeOpenTime;
    }

    public String getStoreCloseTime() {
        return storeCloseTime;
    }

    public void setStoreCloseTime(String storeCloseTime) {
        this.storeCloseTime = storeCloseTime;
    }

    @Override
    public String toString() {
        return
                "DayTime{" +
                        "store_open_time = '" + storeOpenTime + '\'' +
                        ",store_close_time = '" + storeCloseTime + '\'' +
                        "}";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.storeOpenTime);
        dest.writeString(this.storeCloseTime);
    }
}