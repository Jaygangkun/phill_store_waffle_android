package com.edelivery.store.models.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DateTime implements Parcelable {

@SerializedName("status")
@Expose
private Integer status;
@SerializedName("date")
@Expose
private String date;

    protected DateTime(Parcel in) {
        if (in.readByte() == 0) {
            status = null;
        } else {
            status = in.readInt();
        }
        date = in.readString();
    }

    public static final Creator<DateTime> CREATOR = new Creator<DateTime>() {
        @Override
        public DateTime createFromParcel(Parcel in) {
            return new DateTime(in);
        }

        @Override
        public DateTime[] newArray(int size) {
            return new DateTime[size];
        }
    };

    public Integer getStatus() {
return status;
}

public void setStatus(Integer status) {
this.status = status;
}

public String getDate() {
return date;
}

public void setDate(String date) {
this.date = date;
}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (status == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(status);
        }
        dest.writeString(date);
    }
}