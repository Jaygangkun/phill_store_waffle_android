package com.edelivery.store.models.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PromoCodes implements Parcelable {

    public static final Creator<PromoCodes> CREATOR = new Creator<PromoCodes>() {
        @Override
        public PromoCodes createFromParcel(Parcel source) {
            return new PromoCodes(source);
        }

        @Override
        public PromoCodes[] newArray(int size) {
            return new PromoCodes[size];
        }
    };
    @SerializedName("promo_recursion_type")
    private int promoRecursionType;
    @SerializedName("is_promo_have_item_count_limit")
    private boolean isPromoHaveItemCountLimit;
    @SerializedName("promo_code_apply_on_minimum_item_count")
    private int promoCodeApplyOnMinimumItemCount;
    @SerializedName("is_promo_apply_on_completed_order")
    private boolean isPromoApplyOnCompletedOrder;
    @SerializedName("promo_apply_after_completed_order")
    private int promoApplyAfterCompletedOrder;
    @SerializedName("months")
    private ArrayList<String> months = new ArrayList<>();
    @SerializedName("days")
    private ArrayList<String> days = new ArrayList<>();
    @SerializedName("weeks")
    private ArrayList<String> weeks = new ArrayList<>();
    @SerializedName("promo_end_time")
    private String promoEndTime;
    @SerializedName("promo_start_time")
    private String promoStartTime;
    @SerializedName("is_promo_have_date")
    private boolean isPromoHaveDate;
    @SerializedName("promo_id")
    private String promoId;
    @SerializedName("server_token")
    private String serverToken;
    @SerializedName("store_id")
    private String storeId;
    @SerializedName("is_promo_have_minimum_amount_limit")
    private boolean isPromoHaveMinimumAmountLimit;
    @SerializedName("promo_code_apply_on_minimum_amount")
    private double promoCodeApplyOnMinimumAmount;
    @SerializedName("is_promo_have_max_discount_limit")
    private boolean isPromoHaveMaxDiscountLimit;
    @SerializedName("promo_start_date")
    private String promoStartDate;
    @SerializedName("promo_code_max_discount_amount")
    private double promoCodeMaxDiscountAmount;
    @SerializedName("promo_code_value")
    private double promoCodeValue;
    @SerializedName("promo_for")
    private int promoFor;
    @SerializedName("promo_expire_date")
    private String promoExpireDate;
    @SerializedName("is_promo_for_delivery_service")
    private boolean isPromoForDeliveryService;
    @SerializedName("promo_apply_on")
    private List<String> promoApplyOn;
    @SerializedName("promo_code_uses")
    private int promoCodeUses;
    @SerializedName("promo_code_name")
    private String promoCodeName;
    @SerializedName("is_active")
    private boolean isActive;
    @SerializedName("promo_details")
    private String promoDetails;
    @SerializedName("used_promo_code")
    private int usedPromoCode;
    @SerializedName("is_promo_required_uses")
    private boolean isPromoRequiredUses;
    @SerializedName("promo_code_type")
    private int promoCodeType;

    @SerializedName("_id")
    private String id;

    public PromoCodes() {
    }

    protected PromoCodes(Parcel in) {
        this.promoRecursionType = in.readInt();
        this.isPromoHaveItemCountLimit = in.readByte() != 0;
        this.promoCodeApplyOnMinimumItemCount = in.readInt();
        this.isPromoApplyOnCompletedOrder = in.readByte() != 0;
        this.promoApplyAfterCompletedOrder = in.readInt();
        this.months = in.createStringArrayList();
        this.days = in.createStringArrayList();
        this.weeks = in.createStringArrayList();
        this.promoEndTime = in.readString();
        this.promoStartTime = in.readString();
        this.isPromoHaveDate = in.readByte() != 0;
        this.promoId = in.readString();
        this.serverToken = in.readString();
        this.storeId = in.readString();
        this.isPromoHaveMinimumAmountLimit = in.readByte() != 0;
        this.promoCodeApplyOnMinimumAmount = in.readDouble();
        this.isPromoHaveMaxDiscountLimit = in.readByte() != 0;
        this.promoStartDate = in.readString();
        this.promoCodeMaxDiscountAmount = in.readDouble();
        this.promoCodeValue = in.readDouble();
        this.promoFor = in.readInt();
        this.promoExpireDate = in.readString();
        this.isPromoForDeliveryService = in.readByte() != 0;
        this.promoApplyOn = in.createStringArrayList();
        this.promoCodeUses = in.readInt();
        this.promoCodeName = in.readString();
        this.isActive = in.readByte() != 0;
        this.promoDetails = in.readString();
        this.usedPromoCode = in.readInt();
        this.isPromoRequiredUses = in.readByte() != 0;
        this.promoCodeType = in.readInt();
        this.id = in.readString();
    }

    public int getPromoRecursionType() {
        return promoRecursionType;
    }

    public void setPromoRecursionType(int promoRecursionType) {
        this.promoRecursionType = promoRecursionType;
    }

    public boolean isPromoHaveItemCountLimit() {
        return isPromoHaveItemCountLimit;
    }

    public void setPromoHaveItemCountLimit(boolean promoHaveItemCountLimit) {
        isPromoHaveItemCountLimit = promoHaveItemCountLimit;
    }

    public int getPromoCodeApplyOnMinimumItemCount() {
        return promoCodeApplyOnMinimumItemCount;
    }

    public void setPromoCodeApplyOnMinimumItemCount(int promoCodeApplyOnMinimumItemCount) {
        this.promoCodeApplyOnMinimumItemCount = promoCodeApplyOnMinimumItemCount;
    }

    public boolean isPromoApplyOnCompletedOrder() {
        return isPromoApplyOnCompletedOrder;
    }

    public void setPromoApplyOnCompletedOrder(boolean promoApplyOnCompletedOrder) {
        isPromoApplyOnCompletedOrder = promoApplyOnCompletedOrder;
    }

    public int getPromoApplyAfterCompletedOrder() {
        return promoApplyAfterCompletedOrder;
    }

    public void setPromoApplyAfterCompletedOrder(int promoApplyAfterCompletedOrder) {
        this.promoApplyAfterCompletedOrder = promoApplyAfterCompletedOrder;
    }

    public ArrayList<String> getMonths() {
        return months;
    }

    public void setMonths(ArrayList<String> months) {
        this.months = months;
    }

    public ArrayList<String> getDays() {
        return days;
    }

    public void setDays(ArrayList<String> days) {
        this.days = days;
    }

    public ArrayList<String> getWeeks() {
        return weeks;
    }

    public void setWeeks(ArrayList<String> weeks) {
        this.weeks = weeks;
    }

    public String getPromoEndTime() {
        return promoEndTime;
    }

    public void setPromoEndTime(String promoEndTime) {
        this.promoEndTime = promoEndTime;
    }

    public String getPromoStartTime() {
        return promoStartTime;
    }

    public void setPromoStartTime(String promoStartTime) {
        this.promoStartTime = promoStartTime;
    }

    public boolean isPromoHaveDate() {
        return isPromoHaveDate;
    }

    public void setPromoHaveDate(boolean promoHaveDate) {
        isPromoHaveDate = promoHaveDate;
    }

    public String getPromoId() {
        return promoId;
    }

    public void setPromoId(String promoId) {
        this.promoId = promoId;
    }

    public String getServerToken() {
        return serverToken;
    }

    public void setServerToken(String serverToken) {
        this.serverToken = serverToken;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public boolean isIsPromoHaveMinimumAmountLimit() {
        return isPromoHaveMinimumAmountLimit;
    }

    public void setIsPromoHaveMinimumAmountLimit(boolean isPromoHaveMinimumAmountLimit) {
        this.isPromoHaveMinimumAmountLimit = isPromoHaveMinimumAmountLimit;
    }

    public double getPromoCodeApplyOnMinimumAmount() {
        return promoCodeApplyOnMinimumAmount;
    }

    public void setPromoCodeApplyOnMinimumAmount(double promoCodeApplyOnMinimumAmount) {
        this.promoCodeApplyOnMinimumAmount = promoCodeApplyOnMinimumAmount;
    }

    public boolean isIsPromoHaveMaxDiscountLimit() {
        return isPromoHaveMaxDiscountLimit;
    }

    public void setIsPromoHaveMaxDiscountLimit(boolean isPromoHaveMaxDiscountLimit) {
        this.isPromoHaveMaxDiscountLimit = isPromoHaveMaxDiscountLimit;
    }

    public String getPromoStartDate() {
        return promoStartDate;
    }

    public void setPromoStartDate(String promoStartDate) {
        this.promoStartDate = promoStartDate;
    }

    public double getPromoCodeMaxDiscountAmount() {
        return promoCodeMaxDiscountAmount;
    }

    public void setPromoCodeMaxDiscountAmount(double promoCodeMaxDiscountAmount) {
        this.promoCodeMaxDiscountAmount = promoCodeMaxDiscountAmount;
    }

    public double getPromoCodeValue() {
        return promoCodeValue;
    }

    public void setPromoCodeValue(double promoCodeValue) {
        this.promoCodeValue = promoCodeValue;
    }

    public int getPromoFor() {
        return promoFor;
    }

    public void setPromoFor(int promoFor) {
        this.promoFor = promoFor;
    }

    public String getPromoExpireDate() {
        return promoExpireDate;
    }

    public void setPromoExpireDate(String promoExpireDate) {
        this.promoExpireDate = promoExpireDate;
    }

    public boolean isIsPromoForDeliveryService() {
        return isPromoForDeliveryService;
    }

    public void setIsPromoForDeliveryService(boolean isPromoForDeliveryService) {
        this.isPromoForDeliveryService = isPromoForDeliveryService;
    }

    public List<String> getPromoApplyOn() {
        return promoApplyOn;
    }

    public void setPromoApplyOn(List<String> promoApplyOn) {
        this.promoApplyOn = promoApplyOn;
    }

    public int getPromoCodeUses() {
        return promoCodeUses;
    }

    public void setPromoCodeUses(int promoCodeUses) {
        this.promoCodeUses = promoCodeUses;
    }

    public String getPromoCodeName() {
        return promoCodeName;
    }

    public void setPromoCodeName(String promoCodeName) {
        this.promoCodeName = promoCodeName;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getPromoDetails() {
        return promoDetails;
    }

    public void setPromoDetails(String promoDetails) {
        this.promoDetails = promoDetails;
    }

    public int getUsedPromoCode() {
        return usedPromoCode;
    }

    public void setUsedPromoCode(int usedPromoCode) {
        this.usedPromoCode = usedPromoCode;
    }

    public boolean isIsPromoRequiredUses() {
        return isPromoRequiredUses;
    }

    public void setIsPromoRequiredUses(boolean isPromoRequiredUses) {
        this.isPromoRequiredUses = isPromoRequiredUses;
    }

    public int getPromoCodeType() {
        return promoCodeType;
    }

    public void setPromoCodeType(int promoCodeType) {
        this.promoCodeType = promoCodeType;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.promoRecursionType);
        dest.writeByte(this.isPromoHaveItemCountLimit ? (byte) 1 : (byte) 0);
        dest.writeInt(this.promoCodeApplyOnMinimumItemCount);
        dest.writeByte(this.isPromoApplyOnCompletedOrder ? (byte) 1 : (byte) 0);
        dest.writeInt(this.promoApplyAfterCompletedOrder);
        dest.writeStringList(this.months);
        dest.writeStringList(this.days);
        dest.writeStringList(this.weeks);
        dest.writeString(this.promoEndTime);
        dest.writeString(this.promoStartTime);
        dest.writeByte(this.isPromoHaveDate ? (byte) 1 : (byte) 0);
        dest.writeString(this.promoId);
        dest.writeString(this.serverToken);
        dest.writeString(this.storeId);
        dest.writeByte(this.isPromoHaveMinimumAmountLimit ? (byte) 1 : (byte) 0);
        dest.writeDouble(this.promoCodeApplyOnMinimumAmount);
        dest.writeByte(this.isPromoHaveMaxDiscountLimit ? (byte) 1 : (byte) 0);
        dest.writeString(this.promoStartDate);
        dest.writeDouble(this.promoCodeMaxDiscountAmount);
        dest.writeDouble(this.promoCodeValue);
        dest.writeInt(this.promoFor);
        dest.writeString(this.promoExpireDate);
        dest.writeByte(this.isPromoForDeliveryService ? (byte) 1 : (byte) 0);
        dest.writeStringList(this.promoApplyOn);
        dest.writeInt(this.promoCodeUses);
        dest.writeString(this.promoCodeName);
        dest.writeByte(this.isActive ? (byte) 1 : (byte) 0);
        dest.writeString(this.promoDetails);
        dest.writeInt(this.usedPromoCode);
        dest.writeByte(this.isPromoRequiredUses ? (byte) 1 : (byte) 0);
        dest.writeInt(this.promoCodeType);
        dest.writeString(this.id);
    }
}