package com.edelivery.store.models.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CartProductItemDetail implements Parcelable {

    public static final Creator<CartProductItemDetail> CREATOR = new
            Creator<CartProductItemDetail>() {
                @Override
                public CartProductItemDetail createFromParcel(Parcel source) {
                    return new CartProductItemDetail(source);
                }

                @Override
                public CartProductItemDetail[] newArray(int size) {
                    return new CartProductItemDetail[size];
                }
            };
    @SerializedName("store_id")
    @Expose
    private String storeId;
    @SerializedName("is_visible_in_store")
    @Expose
    private boolean isVisibleInStore;
    @SerializedName("unique_id")
    @Expose
    private int uniqueId;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("details")
    @Expose
    private String details;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("items")
    @Expose
    private List<Item> items;

    public CartProductItemDetail() {
    }

    protected CartProductItemDetail(Parcel in) {
        this.storeId = in.readString();
        this.isVisibleInStore = in.readByte() != 0;
        this.uniqueId = in.readInt();
        this.updatedAt = in.readString();
        this.imageUrl = in.readString();
        this.name = in.readString();
        this.createdAt = in.readString();
        this.details = in.readString();
        this.id = in.readString();
        this.items = in.createTypedArrayList(Item.CREATOR);
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public boolean isIsVisibleInStore() {
        return isVisibleInStore;
    }

    public void setIsVisibleInStore(boolean isVisibleInStore) {
        this.isVisibleInStore = isVisibleInStore;
    }

    public int getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(int uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.storeId);
        dest.writeByte(this.isVisibleInStore ? (byte) 1 : (byte) 0);
        dest.writeInt(this.uniqueId);
        dest.writeString(this.updatedAt);
        dest.writeString(this.imageUrl);
        dest.writeString(this.name);
        dest.writeString(this.createdAt);
        dest.writeString(this.details);
        dest.writeString(this.id);
        dest.writeTypedList(this.items);
    }
}