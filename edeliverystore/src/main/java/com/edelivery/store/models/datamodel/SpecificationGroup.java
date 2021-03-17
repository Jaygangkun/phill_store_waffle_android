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

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

@Generated("com.robohorse.robopojogenerator")
public class SpecificationGroup implements Parcelable, Comparable<SpecificationGroup> {

    @SerializedName("sequence_number")
    @Expose
    private long sequenceNumber;

    private boolean isEdited;
    @SerializedName("store_id")
    private String storeId;

    @SerializedName("unique_id")
    private int uniqueId;

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("product_id")
    private String productId;


    @SerializedName("name")
    private Object name;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("_id")
    private String id;

    @SerializedName("list")
    private List<Specifications> specifications;
    private boolean isArrayData;

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setUniqueId(int uniqueId) {
        this.uniqueId = uniqueId;
    }

    public int getUniqueId() {
        return uniqueId;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
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

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setSpecifications(List<Specifications> specifications) {
        this.specifications = specifications;
    }

    public List<Specifications> getSpecifications() {
        return specifications;
    }

    @Override
    public String toString() {
        return
                "SpecificationGroup{" +
                        "store_id = '" + storeId + '\'' +
                        ",unique_id = '" + uniqueId + '\'' +
                        ",updated_at = '" + updatedAt + '\'' +
                        ",product_id = '" + productId + '\'' +
                        ",__v = '" + V + '\'' +
                        ",name = '" + name + '\'' +
                        ",created_at = '" + createdAt + '\'' +
                        ",_id = '" + id + '\'' +
                        ",specifications = '" + specifications + '\'' +
                        "}";
    }

    public boolean isEdited() {
        return isEdited;
    }

    public void setEdited(boolean edited) {
        isEdited = edited;
    }

    public SpecificationGroup() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isEdited ? (byte) 1 : (byte) 0);
        dest.writeString(this.storeId);
        dest.writeInt(this.uniqueId);
        dest.writeString(this.updatedAt);
        dest.writeString(this.productId);
        this.isArrayData = name instanceof List;
        dest.writeByte(this.isArrayData ? (byte) 1 : (byte) 0);
        if (!isArrayData) {
            dest.writeString(String.valueOf(name));
        } else {
            dest.writeStringList((List<String>) name);
        }
        dest.writeString(this.createdAt);
        dest.writeString(this.id);
        dest.writeTypedList(this.specifications);
        dest.writeLong(this.sequenceNumber);
    }

    protected SpecificationGroup(Parcel in) {
        this.isEdited = in.readByte() != 0;
        this.storeId = in.readString();
        this.uniqueId = in.readInt();
        this.updatedAt = in.readString();
        this.productId = in.readString();
        this.isArrayData = in.readByte() != 0;
        this.name = !isArrayData ? in.readString() : in.createStringArrayList();
        this.createdAt = in.readString();
        this.id = in.readString();
        this.specifications = in.createTypedArrayList(Specifications.CREATOR);
        this.sequenceNumber = in.readLong();
    }

    public static final Creator<SpecificationGroup> CREATOR = new Creator<SpecificationGroup>() {
        @Override
        public SpecificationGroup createFromParcel(Parcel source) {
            return new SpecificationGroup(source);
        }

        @Override
        public SpecificationGroup[] newArray(int size) {
            return new SpecificationGroup[size];
        }
    };

    public long getSequenceNumber() {
        return sequenceNumber;
    }

    @Override
    public int compareTo(SpecificationGroup o) {
        return this.sequenceNumber > o.sequenceNumber ? 1 : -1;
    }
}