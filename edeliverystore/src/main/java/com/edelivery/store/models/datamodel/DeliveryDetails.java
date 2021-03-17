package com.edelivery.store.models.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by elluminati on 08-Dec-17.
 */

public class DeliveryDetails implements Parcelable {

    @SerializedName("famous_products_tags")
    @Expose
    private List<List<String>> famousProductsTags;

    @SerializedName("is_store_can_create_group")
    @Expose
    private boolean isStoreCanCreateGroup;

    public List<List<String>> getFamousProductsTags() {
        return famousProductsTags;
    }

    public boolean isStoreCanCreateGroup() {
        return isStoreCanCreateGroup;
    }

    public DeliveryDetails() {
    }


    protected DeliveryDetails(Parcel in) {
        isStoreCanCreateGroup = in.readByte() != 0;
        famousProductsTags = new ArrayList<>();
        in.readList(famousProductsTags, List.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isStoreCanCreateGroup ? 1 : 0));
        dest.writeList(famousProductsTags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DeliveryDetails> CREATOR = new Creator<DeliveryDetails>() {
        @Override
        public DeliveryDetails createFromParcel(Parcel in) {
            return new DeliveryDetails(in);
        }

        @Override
        public DeliveryDetails[] newArray(int size) {
            return new DeliveryDetails[size];
        }
    };
}