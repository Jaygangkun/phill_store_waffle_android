package com.edelivery.store.models.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class OrderDetails implements Parcelable {

    public static final Creator<OrderDetails> CREATOR = new Creator<OrderDetails>() {
        @Override
        public OrderDetails createFromParcel(Parcel source) {
            return new OrderDetails(source);
        }

        @Override
        public OrderDetails[] newArray(int size) {
            return new OrderDetails[size];
        }
    };
    @SerializedName("total_item_tax")
    @Expose
    private double totalItemTax;
    @SerializedName("total_item_price")
    @Expose
    private double totalProductItemPrice;
    @SerializedName("unique_id")
    @Expose
    private int uniqueId;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("items")
    @Expose
    private ArrayList<Item> items;

    public OrderDetails() {
    }

    protected OrderDetails(Parcel in) {
        this.totalItemTax = in.readDouble();
        this.totalProductItemPrice = in.readDouble();
        this.uniqueId = in.readInt();
        this.productId = in.readString();
        this.productName = in.readString();
        this.items = in.createTypedArrayList(Item.CREATOR);
    }

    public int getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(int uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public double getTotalItemTax() {
        return totalItemTax;
    }

    public void setTotalItemTax(double totalItemTax) {
        this.totalItemTax = totalItemTax;
    }

    public double getTotalProductItemPrice() {
        return totalProductItemPrice;
    }

    public void setTotalProductItemPrice(double totalProductItemPrice) {
        this.totalProductItemPrice = totalProductItemPrice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.totalItemTax);
        dest.writeDouble(this.totalProductItemPrice);
        dest.writeInt(this.uniqueId);
        dest.writeString(this.productId);
        dest.writeString(this.productName);
        dest.writeTypedList(this.items);
    }
}