package com.edelivery.store.models.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.edelivery.store.models.singleton.Language;
import com.edelivery.store.utils.Utilities;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SubStore implements Parcelable {

    private Boolean isArrayData;

    @SerializedName("urls")
    private List<SubStoreAccessService> subStoreAccessServices;

    @SerializedName("country_phone_code")
    private String countryPhoneCode;

    @SerializedName("phone")
    private String phone;

    @SerializedName("device_token")
    private String deviceToken;

    @SerializedName("name")
    private Object name;

    @SerializedName("is_approved")
    private boolean isApproved;

    @SerializedName("device_type")
    private String deviceType;

    @SerializedName("server_token")
    private String serverToken;

    @SerializedName("main_store_id")
    private String mainStoreId;

    @SerializedName("_id")
    private String id;

    @SerializedName("email")
    private String email;

    public List<SubStoreAccessService> getSubStoreAccessServices() {
        return subStoreAccessServices;
    }

    public String getCountryPhoneCode() {
        return countryPhoneCode;
    }

    public String getPhone() {
        return phone;
    }

    public String getDeviceToken() {
        return deviceToken;
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
    public List<String> getNameList(){
        return (List<String>) name;
    }

    public boolean isIsApproved() {
        return isApproved;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public String getServerToken() {
        return serverToken;
    }

    public String getMainStoreId() {
        return mainStoreId;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.subStoreAccessServices);
        dest.writeString(this.countryPhoneCode);
        dest.writeString(this.phone);
        dest.writeString(this.deviceToken);
        this.isArrayData = name instanceof List;
        dest.writeByte(this.isArrayData ? (byte) 1 : (byte) 0);
        if (!isArrayData) {
            dest.writeString(String.valueOf(name));
        } else {
            dest.writeStringList((List<String>) name);
        }
        dest.writeByte(this.isApproved ? (byte) 1 : (byte) 0);
        dest.writeString(this.deviceType);
        dest.writeString(this.serverToken);
        dest.writeString(this.mainStoreId);
        dest.writeString(this.id);
        dest.writeString(this.email);
    }

    public SubStore() {
    }

    protected SubStore(Parcel in) {
        this.subStoreAccessServices = in.createTypedArrayList(SubStoreAccessService.CREATOR);
        this.countryPhoneCode = in.readString();
        this.phone = in.readString();
        this.deviceToken = in.readString();
        this.isArrayData = in.readByte() != 0;
        this.name = !isArrayData ? in.readString() : in.createStringArrayList();
        this.isApproved = in.readByte() != 0;
        this.deviceType = in.readString();
        this.serverToken = in.readString();
        this.mainStoreId = in.readString();
        this.id = in.readString();
        this.email = in.readString();
    }

    public static final Parcelable.Creator<SubStore> CREATOR = new Parcelable.Creator<SubStore>() {
        @Override
        public SubStore createFromParcel(Parcel source) {
            return new SubStore(source);
        }

        @Override
        public SubStore[] newArray(int size) {
            return new SubStore[size];
        }
    };

}