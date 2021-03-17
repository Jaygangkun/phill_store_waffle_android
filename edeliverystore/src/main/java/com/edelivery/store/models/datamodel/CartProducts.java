package com.edelivery.store.models.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CartProducts implements Parcelable {

    public static final Creator<CartProducts> CREATOR = new Creator<CartProducts>() {
        @Override
        public CartProducts createFromParcel(Parcel source) {
            return new CartProducts(source);
        }

        @Override
        public CartProducts[] newArray(int size) {
            return new CartProducts[size];
        }
    };
    @SerializedName("total_item_tax")
    @Expose
    private double totalItemTax;
    @SerializedName("product_detail")
    private CartProductItemDetail productItem;
    @SerializedName("store_id")
    @Expose
    private String storeId;
    @SerializedName("total_item_price")
    @Expose
    private double totalProductItemPrice;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("items")
    @Expose
    private List<CartProductItems> items;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("unique_id")
    @Expose
    private int uniqueId;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;

    public CartProducts() {
    }

    protected CartProducts(Parcel in) {
        this.totalItemTax = in.readDouble();
        this.productItem = in.readParcelable(CartProductItemDetail.class.getClassLoader());
        this.storeId = in.readString();
        this.totalProductItemPrice = in.readDouble();
        this.productId = in.readString();
        this.items = in.createTypedArrayList(CartProductItems.CREATOR);
        this.productName = in.readString();
        this.uniqueId = in.readInt();
        this.imageUrl = in.readString();
    }

    public double getTotalItemTax() {
        return totalItemTax;
    }

    public void setTotalItemTax(double totalItemTax) {
        this.totalItemTax = totalItemTax;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public List<CartProductItems> getItems() {
        return items;
    }

    public void setItems(List<CartProductItems> items) {
        this.items = items;
    }

    public double getTotalProductItemPrice() {
        return totalProductItemPrice;
    }

    public void setTotalProductItemPrice(double totalProductItemPrice) {
        this.totalProductItemPrice = totalProductItemPrice;
    }

    public int getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(int uniqueId) {
        this.uniqueId = uniqueId;
    }

    public CartProductItemDetail getProductItem() {
        return productItem;
    }

    public void ProductItemCart(CartProductItemDetail productItem) {
        this.productItem = productItem;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.totalItemTax);
        dest.writeParcelable(this.productItem, flags);
        dest.writeString(this.storeId);
        dest.writeDouble(this.totalProductItemPrice);
        dest.writeString(this.productId);
        dest.writeTypedList(this.items);
        dest.writeString(this.productName);
        dest.writeInt(this.uniqueId);
        dest.writeString(this.imageUrl);
    }
}