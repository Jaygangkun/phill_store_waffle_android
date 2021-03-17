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
public class ItemSpecification implements Parcelable, Comparable<ItemSpecification> {


    private boolean isArrayData;
    private String chooseMessage;

    public String getChooseMessage() {
        return chooseMessage;
    }

    public void setChooseMessage(String chooseMessage) {
        this.chooseMessage = chooseMessage;
    }

    private int selectedCount;
    @SerializedName("range")
    @Expose
    private int range;
    @SerializedName("sequence_number")
    @Expose
    private long sequenceNumber;

    @SerializedName("max_range")
    @Expose
    private int maxRange;
    @SerializedName("price")
    @Expose
    private double price;
    @SerializedName("name")
    @Expose
    private Object name;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("specification_name")
    @Expose
    private String specification_name;
    @SerializedName("list")
    @Expose
    private ArrayList<ProductSpecification> list;
    @SerializedName("type")
    @Expose
    private int type;
    @SerializedName("is_required")
    @Expose
    private boolean isRequired;
    @SerializedName("unique_id")
    @Expose
    private int unique_id;

    public ItemSpecification() {
    }

    public int getSelectedCount() {
        return selectedCount;
    }

    public void setSelectedCount(int selectedCount) {
        this.selectedCount = selectedCount;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getMaxRange() {
        return maxRange;
    }

    public void setMaxRange(int maxRange) {
        this.maxRange = maxRange;
    }

    public String getSpecification_name() {
        return specification_name;
    }

    public void setSpecification_name(String specification_name) {
        this.specification_name = specification_name;
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

    public void setNameList(List<String> name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<ProductSpecification> getList() {
        return list;
    }

    public void setList(ArrayList<ProductSpecification> list) {
        this.list = list;
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

    public int getUniqueId() {
        return unique_id;
    }

    public void setUniqueId(int unique_id) {
        this.unique_id = unique_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.chooseMessage);
        dest.writeInt(this.selectedCount);
        dest.writeInt(this.range);
        dest.writeInt(this.maxRange);
        dest.writeDouble(this.price);
        this.isArrayData = name instanceof List;
        dest.writeByte(this.isArrayData ? (byte) 1 : (byte) 0);
        if (!isArrayData) {
            dest.writeString(String.valueOf(name));
        } else {
            dest.writeStringList((List<String>) name);
        }
        dest.writeString(this.id);
        dest.writeString(this.specification_name);
        dest.writeTypedList(this.list);
        dest.writeInt(this.type);
        dest.writeByte(this.isRequired ? (byte) 1 : (byte) 0);
        dest.writeInt(this.unique_id);
        dest.writeLong(this.sequenceNumber);
    }

    protected ItemSpecification(Parcel in) {
        this.chooseMessage = in.readString();
        this.selectedCount = in.readInt();
        this.range = in.readInt();
        this.maxRange = in.readInt();
        this.price = in.readDouble();
        this.isArrayData = in.readByte() != 0;
        this.name = !isArrayData ? in.readString() : in.createStringArrayList();
        this.id = in.readString();
        this.specification_name = in.readString();
        this.list = in.createTypedArrayList(ProductSpecification.CREATOR);
        this.type = in.readInt();
        this.isRequired = in.readByte() != 0;
        this.unique_id = in.readInt();
        this.sequenceNumber = in.readLong();
    }

    public static final Creator<ItemSpecification> CREATOR = new Creator<ItemSpecification>() {
        @Override
        public ItemSpecification createFromParcel(Parcel source) {
            return new ItemSpecification(source);
        }

        @Override
        public ItemSpecification[] newArray(int size) {
            return new ItemSpecification[size];
        }
    };

    public long getSequenceNumber() {
        return sequenceNumber;
    }

    @Override
    public int compareTo(ItemSpecification o) {
        return this.sequenceNumber > o.sequenceNumber ? 1 : -1;
    }
}