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
public class Specifications implements Parcelable, Comparable<Specifications> {


    @SerializedName("sequence_number")
    @Expose
    private long sequenceNumber;

    @SerializedName("type")
    @Expose
    private int type;
    @SerializedName("is_required")
    @Expose
    private boolean isRequired;
    @SerializedName("store_id")
    private String storeId;
    @SerializedName("is_user_selected")
    private boolean isUserSelected;
    @SerializedName("unique_id")
    private int uniqueId;
    @SerializedName("updated_at")
    private String updatedAt;
    @SerializedName("price")
    private double price;
    @SerializedName("product_id")
    private String productId;
    @SerializedName("name")
    private Object name;
    @SerializedName("specification_group_id")
    private String specificationGroupId;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("_id")
    private String id;
    @SerializedName("is_default_selected")
    private boolean isDefaultSelected;
    @SerializedName("list")
    @Expose

    private List<SpecificationListItem> list;
    private boolean isArrayData;

    public Specifications() {
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isRequired() {
        return isRequired;
    }

    public void setRequired(boolean required) {
        isRequired = required;
    }

    public List<SpecificationListItem> getList() {
        return list;
    }

    public void setList(List<SpecificationListItem> list) {
        this.list = list;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public boolean isUserSelected() {
        return isUserSelected;
    }

    public void setUserSelected(boolean userSelected) {
        isUserSelected = userSelected;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        if (name instanceof String) {
            return String.valueOf(name);
        } else {
            return Utilities.getDetailStringFromList(((ArrayList<String>) name),
                    Language.getInstance().
                            getStoreLanguageIndex());

        }
    }

    public List<String> getNameList() {
        return (List<String>) name;
    }

    public void setName(List<String> name) {
        this.name = name;
    }

    public String getSpecificationGroupId() {
        return specificationGroupId;
    }

    public void setSpecificationGroupId(String specificationGroupId) {
        this.specificationGroupId = specificationGroupId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isDefaultSelected() {
        return isDefaultSelected;
    }

    public void setDefaultSelected(boolean defaultSelected) {
        isDefaultSelected = defaultSelected;
    }

    @Override
    public int compareTo(Specifications o) {
        return this.sequenceNumber > o.getSequenceNumber() ? 1 : -1;
    }

    public class NameAndPrice {
        @SerializedName("price")
        private double price;
        @SerializedName("name")
        private List<String> name;

        @SerializedName("sequence_number")
        private long sequenceNumber;

        public NameAndPrice() {
        }

        public NameAndPrice(double price, List<String> name, long sequenceNumber) {
            this.price = price;
            this.name = name;
            this.sequenceNumber = sequenceNumber;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.sequenceNumber);
        dest.writeInt(this.type);
        dest.writeByte(this.isRequired ? (byte) 1 : (byte) 0);
        dest.writeString(this.storeId);
        dest.writeByte(this.isUserSelected ? (byte) 1 : (byte) 0);
        dest.writeInt(this.uniqueId);
        dest.writeString(this.updatedAt);
        dest.writeDouble(this.price);
        dest.writeString(this.productId);
        this.isArrayData = name instanceof List;
        dest.writeByte(this.isArrayData ? (byte) 1 : (byte) 0);
        if (!isArrayData) {
            dest.writeString(String.valueOf(name));
        } else {
            dest.writeStringList((List<String>) name);
        }
        dest.writeString(this.specificationGroupId);
        dest.writeString(this.createdAt);
        dest.writeString(this.id);
        dest.writeByte(this.isDefaultSelected ? (byte) 1 : (byte) 0);
        dest.writeList(this.list);
    }

    protected Specifications(Parcel in) {
        this.sequenceNumber = in.readLong();
        this.type = in.readInt();
        this.isRequired = in.readByte() != 0;
        this.storeId = in.readString();
        this.isUserSelected = in.readByte() != 0;
        this.uniqueId = in.readInt();
        this.updatedAt = in.readString();
        this.price = in.readDouble();
        this.productId = in.readString();
        this.isArrayData = in.readByte() != 0;
        this.name = !isArrayData ? in.readString() : in.createStringArrayList();
        this.specificationGroupId = in.readString();
        this.createdAt = in.readString();
        this.id = in.readString();
        this.isDefaultSelected = in.readByte() != 0;
        this.list = new ArrayList<SpecificationListItem>();
        in.readList(this.list, SpecificationListItem.class.getClassLoader());
    }

    public static final Creator<Specifications> CREATOR = new Creator<Specifications>() {
        @Override
        public Specifications createFromParcel(Parcel source) {
            return new Specifications(source);
        }

        @Override
        public Specifications[] newArray(int size) {
            return new Specifications[size];
        }
    };

    public long getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(long sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
}