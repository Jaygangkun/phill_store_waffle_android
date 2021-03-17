package com.edelivery.store.models.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class StoreTime implements Parcelable {

    public static final Creator<StoreTime> CREATOR = new Creator<StoreTime>() {
        @Override
        public StoreTime createFromParcel(Parcel source) {
            return new StoreTime(source);
        }

        @Override
        public StoreTime[] newArray(int size) {
            return new StoreTime[size];
        }
    };
    @SerializedName("is_store_open_full_time")
    private boolean isStoreOpenFullTime;
    @SerializedName("is_store_open")
    private boolean isStoreOpen;
    @SerializedName("day")
    private int day;
    @SerializedName("day_time")
    private List<DayTime> dayTime;

    public StoreTime() {
    }

    protected StoreTime(Parcel in) {
        this.isStoreOpenFullTime = in.readByte() != 0;
        this.isStoreOpen = in.readByte() != 0;
        this.day = in.readInt();
        this.dayTime = in.createTypedArrayList(DayTime.CREATOR);
    }

    public boolean isStoreOpenFullTime() {
        return isStoreOpenFullTime;
    }

    public void setStoreOpenFullTime(boolean storeOpenFullTime) {
        isStoreOpenFullTime = storeOpenFullTime;
    }

    public boolean isStoreOpen() {
        return isStoreOpen;
    }

    public void setStoreOpen(boolean storeOpen) {
        isStoreOpen = storeOpen;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public List<DayTime> getDayTime() {
        return dayTime;
    }

    public void setDayTime(List<DayTime> dayTime) {
        this.dayTime = dayTime;
    }

    @Override
    public String toString() {
        return
                "StoreTime{" +
                        "is_store_open_full_time = '" + isStoreOpenFullTime + '\'' +
                        ",is_store_open = '" + isStoreOpen + '\'' +
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
        dest.writeByte(this.isStoreOpenFullTime ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isStoreOpen ? (byte) 1 : (byte) 0);
        dest.writeInt(this.day);
        dest.writeTypedList(this.dayTime);
    }
}