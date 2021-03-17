package com.edelivery.store.models.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.edelivery.store.models.singleton.Language;
import com.edelivery.store.utils.Utilities;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class ProductSpecification implements Parcelable, Comparable<ProductSpecification> {


    @SerializedName("sequence_number")
    @Expose
    private long sequenceNumber;

    private boolean isArrayData;
    @SerializedName("is_user_selected")
    @Expose
    private boolean isUserSelected;

    @SerializedName("price")
    @Expose
    private double price;
    @SerializedName("name")
    @Expose
    private Object name;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("is_default_selected")
    @Expose
    private boolean isDefaultSelected;
    @SerializedName("unique_id")
    @Expose
    private int unique_id;
    @SerializedName("specification_group_id")
    @Expose
    private String specificationGroupId;

    private String specificationName;

    public ProductSpecification() {
    }

    public void setUserSelected(boolean userSelected) {
        isUserSelected = userSelected;
    }

    public String getSpecificationGroupId() {
        return specificationGroupId;
    }

    public void setSpecificationGroupId(String specificationGroupId) {
        this.specificationGroupId = specificationGroupId;
    }

    public boolean isIsUserSelected() {
        return isUserSelected;
    }

    public void setIsUserSelected(boolean isUserSelected) {
        this.isUserSelected = isUserSelected;
    }


    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        if (name instanceof String) {
            return String.valueOf(name);
        } else {
            return Utilities.getDetailStringFromList((ArrayList<String>) name,
                    Language.getInstance().
                            getStoreLanguageIndex());

        }
    }

    public void setNameListToString() {
        this.name = Utilities.getDetailStringFromList((ArrayList<String>) name,
                Language.getInstance().
                        getStoreLanguageIndex());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isIsDefaultSelected() {
        return isDefaultSelected;
    }

    public void setIsDefaultSelected(boolean isDefaultSelected) {
        this.isDefaultSelected = isDefaultSelected;
    }

    public String getSpecificationName() {
        return specificationName;
    }

    public void setSpecificationName(String specificationName) {
        this.specificationName = specificationName;
    }

    public int getUnique_id() {
        return unique_id;
    }

    public void setUnique_id(int unique_id) {
        this.unique_id = unique_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isUserSelected ? (byte) 1 : (byte) 0);
        dest.writeDouble(this.price);
        this.isArrayData = name instanceof List;
        dest.writeByte(this.isArrayData ? (byte) 1 : (byte) 0);
        if (!isArrayData) {
            dest.writeString(String.valueOf(name));
        } else {
            dest.writeStringList((List<String>) name);
        }
        dest.writeString(this.id);
        dest.writeByte(this.isDefaultSelected ? (byte) 1 : (byte) 0);
        dest.writeInt(this.unique_id);
        dest.writeString(this.specificationGroupId);
        dest.writeString(this.specificationName);
        dest.writeLong(this.sequenceNumber);

    }

    protected ProductSpecification(Parcel in) {
        this.isUserSelected = in.readByte() != 0;
        this.price = in.readDouble();
        this.isArrayData = in.readByte() != 0;
        this.name = !isArrayData ? in.readString() : in.createStringArrayList();
        this.id = in.readString();
        this.isDefaultSelected = in.readByte() != 0;
        this.unique_id = in.readInt();
        this.specificationGroupId = in.readString();
        this.specificationName = in.readString();
        this.sequenceNumber = in.readLong();

    }

    public static final Creator<ProductSpecification> CREATOR =
            new Creator<ProductSpecification>() {
                @Override
                public ProductSpecification createFromParcel(Parcel source) {
                    return new ProductSpecification(source);
                }

                @Override
                public ProductSpecification[] newArray(int size) {
                    return new ProductSpecification[size];
                }
            };

    public long getSequenceNumber() {
        return sequenceNumber;
    }

    @Override
    public int compareTo(ProductSpecification o) {
        return this.sequenceNumber > o.sequenceNumber ? 1 : -1;
    }
}