package com.edelivery.store.models.responsemodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.edelivery.store.models.datamodel.Item;
import com.edelivery.store.models.datamodel.Product;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by elluminati on 11-Jan-18.
 */

public class ProductResponse implements Parcelable {
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("error_code")
    @Expose
    private int errorCode;
    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("message")
    @Expose
    private int message;
    @SerializedName("products")
    @Expose
    private List<Product> products;

    @SerializedName("items")
    @Expose
    private ArrayList<Item> itemList;

    protected ProductResponse(Parcel in) {
        currency = in.readString();
        errorCode = in.readInt();
        success = in.readByte() != 0;
        message = in.readInt();
        products = in.createTypedArrayList(Product.CREATOR);
        itemList = in.createTypedArrayList(Item.CREATOR);
    }

    public static final Creator<ProductResponse> CREATOR = new Creator<ProductResponse>() {
        @Override
        public ProductResponse createFromParcel(Parcel in) {
            return new ProductResponse(in);
        }

        @Override
        public ProductResponse[] newArray(int size) {
            return new ProductResponse[size];
        }
    };

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getMessage() {
        return message;
    }

    public void setMessage(int message) {
        this.message = message;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(currency);
        dest.writeInt(errorCode);
        dest.writeByte((byte) (success ? 1 : 0));
        dest.writeInt(message);
        dest.writeTypedList(products);
        dest.writeTypedList(itemList);
    }
}
