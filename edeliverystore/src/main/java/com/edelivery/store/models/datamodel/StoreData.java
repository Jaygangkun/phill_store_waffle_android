package com.edelivery.store.models.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.edelivery.store.models.singleton.Language;
import com.edelivery.store.utils.Utilities;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class StoreData implements Parcelable {
    public static final Creator<StoreData> CREATOR = new Creator<StoreData>() {
        @Override
        public StoreData createFromParcel(Parcel source) {
            return new StoreData(source);
        }

        @Override
        public StoreData[] newArray(int size) {
            return new StoreData[size];
        }
    };
    @SerializedName("is_store_can_set_cancellation_charge")
    @Expose
    private boolean isStoreCanSetCancellationCharge = true;
    @SerializedName("is_store_create_order")
    @Expose
    private boolean isStoreCreateOrder = true;
    @SerializedName("is_store_edit_menu")
    @Expose
    private boolean isStoreEditMenu = true;
    @SerializedName("is_store_edit_item")
    @Expose
    private boolean isStoreEditItem = true;
    @SerializedName("is_store_add_promocode")
    @Expose
    private boolean isStoreAddPromoCode = true;
    @SerializedName("is_store_can_add_provider")
    @Expose
    private boolean isStoreCanAddProvider;
    @SerializedName("is_store_can_complete_order")
    @Expose
    private boolean isStoreCanCompleteOrder;
    @SerializedName("login_by")
    @Expose
    private String loginBy;
    @SerializedName("social_id")
    @Expose
    private String socialId;
    @SerializedName("longitude")
    @Expose
    private double longitude;
    @SerializedName("latitude")
    @Expose
    private double latitude;
    @SerializedName("old_password")
    @Expose
    private String oldPassword;
    @SerializedName("store_id")
    @Expose
    private String storeId;
    @SerializedName("is_use_item_tax")
    @Expose
    private boolean isUseItemTax;
    @SerializedName("is_provide_pickup_delivery")
    @Expose
    private boolean isProvidePickupDelivery;
    @SerializedName("is_ask_estimated_time_for_ready_order")
    @Expose
    private boolean isAskEstimatedTimeForReadyOrder;
    @SerializedName("famous_products_tags")
    @Expose
    private ArrayList<List<String>> famousProductsTags;
    @SerializedName("free_delivery_within_radius")
    @Expose
    private double freeDeliveryWithinRadius;
    @SerializedName("delivery_details")
    @Expose
    private DeliveryDetails deliveryDetails;
    @SerializedName("is_store_busy")
    @Expose
    private boolean isBusy;
    @SerializedName("social_ids")
    @Expose
    private ArrayList<String> socialIds;
    @SerializedName("store_delivery_id")
    @Expose
    private String storeDeliveryId;
    @SerializedName("is_email_verified")
    @Expose
    private boolean isEmailVerified;
    @SerializedName("is_document_uploaded")
    @Expose
    private boolean isDocumentUploaded;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("device_type")
    @Expose
    private String deviceType;
    @SerializedName("admin_profit_mode_on_store")
    @Expose
    private double adminProfitModeOnStore;
    @SerializedName("item_tax")
    @Expose
    private double itemTax;
    @SerializedName("is_referral")
    @Expose
    private boolean isReferral;
    @SerializedName("wallet_currency_code")
    @Expose
    private String walletCurrencyCode;
    @SerializedName("price_rating")
    @Expose
    private int priceRating;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("country_phone_code")
    @Expose
    private String countryPhoneCode;
    @SerializedName("user_rate")
    @Expose
    private double rate;
    @SerializedName("is_phone_number_verified")
    @Expose
    private boolean isPhoneNumberVerified;
    @SerializedName("store_delivery_time")
    @Expose
    private ArrayList<StoreTime> storeDeliveryTime;
    @SerializedName("store_time")
    @Expose
    private ArrayList<StoreTime> storeTime;
    @SerializedName("total_referrals")
    @Expose
    private int totalReferrals;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("is_business")
    @Expose
    private boolean isBusiness;
    @SerializedName("free_delivery_for_above_order_price")
    @Expose
    private double freeDeliveryPrice;
    @SerializedName("unique_id")
    @Expose
    private Integer uniqueId;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("wallet")
    @Expose
    private double wallet;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("server_token")
    @Expose
    private String serverToken;
    @SerializedName("rate_count")
    @Expose
    private int rateCount;
    @SerializedName("website_url")
    @Expose
    private String websiteUrl;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("device_token")
    @Expose
    private String deviceToken;
    @SerializedName("referral_code")
    @Expose
    private String referralCode;
    @SerializedName("name")
    @Expose
    private Object name;
    @SerializedName("is_approved")
    @Expose
    private boolean isApproved;
    @SerializedName("location")
    @Expose
    private List<Double> location;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("slogan")
    @Expose
    private String slogan;
    @SerializedName("country_id")
    @Expose
    private String countryId;
    @SerializedName("city_id")
    @Expose
    private String cityId;
    @SerializedName("is_store_pay_delivery_fees")
    @Expose
    private boolean isStorePayDeliveryFees;
    @SerializedName("delivery_time")
    @Expose
    private int deliveryTime;
    @SerializedName("delivery_time_max")
    @Expose
    private int deliveryTimeMax;
    @SerializedName("inform_schedule_order_before_min")
    private int informScheduleOrderBeforeMin;
    @SerializedName("schedule_order_create_after_minute")
    private int scheduleOrderCreateAfterMinute;
    @SerializedName("is_store_set_schedule_delivery_time")
    private boolean isStoreSetScheduleDeliveryTime;
    @SerializedName("is_taking_schedule_order")
    private boolean isTakingScheduleOrder;
    @SerializedName("min_order_price")
    private double minOrderPrice;
    @SerializedName("is_provide_delivery_anywhere")
    private boolean isProvideDeliveryAnywhere;
    @SerializedName("order_cancellation_charge_type")
    private int orderCancellationChargeType;
    @SerializedName("delivery_radius")
    private double deliveryRadius;
    @SerializedName("max_item_quantity_add_by_user")
    private int maxItemQuantityAddByUser;
    @SerializedName("order_cancellation_charge_value")
    private double orderCancellationChargeValue;
    @SerializedName("order_cancellation_charge_for_above_order_price")
    private double orderCancellationChargeForAboveOrderPrice;
    @SerializedName("is_order_cancellation_charge_apply")
    private boolean isOrderCancellationChargeApply;
    @SerializedName("languages_supported")
    private List<Languages> languages;

    public StoreData() {
    }

    protected StoreData(Parcel in) {
        this.isStoreCanAddProvider = in.readByte() != 0;
        this.isStoreCanCompleteOrder = in.readByte() != 0;
        this.loginBy = in.readString();
        this.socialId = in.readString();
        this.longitude = in.readDouble();
        this.latitude = in.readDouble();
        this.oldPassword = in.readString();
        this.storeId = in.readString();
        this.isUseItemTax = in.readByte() != 0;
        this.isProvidePickupDelivery = in.readByte() != 0;
        this.isAskEstimatedTimeForReadyOrder = in.readByte() != 0;
        famousProductsTags = new ArrayList<>();
        in.readList(famousProductsTags, List.class.getClassLoader());
        this.freeDeliveryWithinRadius = in.readDouble();
        this.deliveryDetails = in.readParcelable(DeliveryDetails.class.getClassLoader());
        this.isBusy = in.readByte() != 0;
        this.socialIds = in.createStringArrayList();
        this.storeDeliveryId = in.readString();
        this.isEmailVerified = in.readByte() != 0;
        this.isDocumentUploaded = in.readByte() != 0;
        this.createdAt = in.readString();
        this.deviceType = in.readString();
        this.adminProfitModeOnStore = in.readDouble();
        this.itemTax = in.readDouble();
        this.isReferral = in.readByte() != 0;
        this.walletCurrencyCode = in.readString();
        this.priceRating = in.readInt();
        this.password = in.readString();
        this.updatedAt = in.readString();
        this.countryPhoneCode = in.readString();
        this.rate = in.readDouble();
        this.isPhoneNumberVerified = in.readByte() != 0;
        this.storeTime = in.createTypedArrayList(StoreTime.CREATOR);
        this.storeDeliveryTime = in.createTypedArrayList(StoreTime.CREATOR);
        this.totalReferrals = in.readInt();
        this.currency = in.readString();
        this.email = in.readString();
        this.isBusiness = in.readByte() != 0;
        this.freeDeliveryPrice = in.readDouble();
        this.uniqueId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.address = in.readString();
        this.wallet = in.readDouble();
        this.imageUrl = in.readString();
        this.serverToken = in.readString();
        this.rateCount = in.readInt();
        this.websiteUrl = in.readString();
        this.phone = in.readString();
        this.deviceToken = in.readString();
        this.referralCode = in.readString();
        this.name = this.name instanceof String ? in.readString() : in.createStringArrayList();
        this.isApproved = in.readByte() != 0;
        this.location = new ArrayList<Double>();
        in.readList(this.location, Double.class.getClassLoader());
        this.id = in.readString();
        this.slogan = in.readString();
        this.countryId = in.readString();
        this.cityId = in.readString();
        this.isStorePayDeliveryFees = in.readByte() != 0;
        this.deliveryTime = in.readInt();
        this.deliveryTimeMax = in.readInt();
        this.informScheduleOrderBeforeMin = in.readInt();
        this.scheduleOrderCreateAfterMinute = in.readInt();
        this.isTakingScheduleOrder = in.readByte() != 0;
        this.minOrderPrice = in.readDouble();
        this.isProvideDeliveryAnywhere = in.readByte() != 0;
        this.orderCancellationChargeType = in.readInt();
        this.deliveryRadius = in.readDouble();
        this.maxItemQuantityAddByUser = in.readInt();
        this.orderCancellationChargeValue = in.readDouble();
        this.orderCancellationChargeForAboveOrderPrice = in.readDouble();
        this.isOrderCancellationChargeApply = in.readByte() != 0;
        this.languages = in.createTypedArrayList(Languages.CREATOR);
    }

    public boolean isStoreCanSetCancellationCharge() {
        return isStoreCanSetCancellationCharge;
    }

    public void setStoreCanSetCancellationCharge(boolean storeCanSetCancellationCharge) {
        isStoreCanSetCancellationCharge = storeCanSetCancellationCharge;
    }

    public boolean isStoreCreateOrder() {
        return isStoreCreateOrder;
    }

    public void setStoreCreateOrder(boolean storeCreateOrder) {
        isStoreCreateOrder = storeCreateOrder;
    }

    public boolean isStoreEditMenu() {
        return isStoreEditMenu;
    }

    public void setStoreEditMenu(boolean storeEditMenu) {
        isStoreEditMenu = storeEditMenu;
    }

    public boolean isStoreEditItem() {
        return isStoreEditItem;
    }

    public void setStoreEditItem(boolean storeEditItem) {
        isStoreEditItem = storeEditItem;
    }

    public boolean isStoreAddPromoCode() {
        return isStoreAddPromoCode;
    }

    public void setStoreAddPromoCode(boolean storeAddPromoCode) {
        isStoreAddPromoCode = storeAddPromoCode;
    }

    public boolean isStoreCanAddProvider() {
        return isStoreCanAddProvider;
    }

    public void setStoreCanAddProvider(boolean storeCanAddProvider) {
        isStoreCanAddProvider = storeCanAddProvider;
    }

    public boolean isStoreCanCompleteOrder() {
        return isStoreCanCompleteOrder;
    }

    public void setStoreCanCompleteOrder(boolean storeCanCompleteOrder) {
        isStoreCanCompleteOrder = storeCanCompleteOrder;
    }

    public String getLoginBy() {
        return loginBy;
    }

    public void setLoginBy(String loginBy) {
        this.loginBy = loginBy;
    }

    public String getSocialId() {
        return socialId;
    }

    public void setSocialId(String socialId) {
        this.socialId = socialId;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public boolean isUseItemTax() {
        return isUseItemTax;
    }

    public void setUseItemTax(boolean useItemTax) {
        isUseItemTax = useItemTax;
    }

    public boolean isProvidePickupDelivery() {
        return isProvidePickupDelivery;
    }

    public void setProvidePickupDelivery(boolean providePickupDelivery) {
        isProvidePickupDelivery = providePickupDelivery;
    }

    public int getDeliveryTimeMax() {
        return deliveryTimeMax;
    }

    public void setDeliveryTimeMax(int deliveryTimeMax) {
        this.deliveryTimeMax = deliveryTimeMax;
    }

    public boolean isAskEstimatedTimeForReadyOrder() {
        return isAskEstimatedTimeForReadyOrder;
    }

    public void setAskEstimatedTimeForReadyOrder(boolean askEstimatedTimeForReadyOrder) {
        isAskEstimatedTimeForReadyOrder = askEstimatedTimeForReadyOrder;
    }

    public ArrayList<List<String>> getFamousProductsTags() {
        return famousProductsTags;
    }

    public void setFamousProductsTags(ArrayList<List<String>> famousProductsTags) {
        this.famousProductsTags = famousProductsTags;
    }

    public DeliveryDetails getDeliveryDetails() {
        return deliveryDetails;
    }

    public void setDeliveryDetails(DeliveryDetails deliveryDetails) {
        this.deliveryDetails = deliveryDetails;
    }

    public double getFreeDeliveryWithinRadius() {

        return freeDeliveryWithinRadius;
    }

    public void setFreeDeliveryWithinRadius(double freeDeliveryWithinRadius) {
        this.freeDeliveryWithinRadius = freeDeliveryWithinRadius;
    }

    public boolean isBusy() {
        return isBusy;
    }

    public void setBusy(boolean busy) {
        isBusy = busy;
    }

    public boolean isStorePayDeliveryFees() {
        return isStorePayDeliveryFees;
    }

    public void setStorePayDeliveryFees(boolean storePayDeliveryFees) {
        isStorePayDeliveryFees = storePayDeliveryFees;
    }

    public int getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(int deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getStoreDeliveryId() {
        return storeDeliveryId;
    }

    public void setStoreDeliveryId(String storeDeliveryId) {
        this.storeDeliveryId = storeDeliveryId;
    }

    public boolean isEmailVerified() {
        return isEmailVerified;
    }

    public void setEmailVerified(boolean isEmailVerified) {
        this.isEmailVerified = isEmailVerified;
    }

    public boolean isDocumentUploaded() {
        return isDocumentUploaded;
    }

    public void setDocumentUploaded(boolean isDocumentUploaded) {
        this.isDocumentUploaded = isDocumentUploaded;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public double getAdminProfitModeOnStore() {
        return adminProfitModeOnStore;
    }

    public void setAdminProfitModeOnStore(double adminProfitModeOnStore) {
        this.adminProfitModeOnStore = adminProfitModeOnStore;
    }

    public double getItemTax() {
        return itemTax;
    }

    public void setItemTax(double itemTax) {
        this.itemTax = itemTax;
    }

    public boolean isReferral() {
        return isReferral;
    }

    public void setReferral(boolean isReferral) {
        this.isReferral = isReferral;
    }

    public String getWalletCurrencyCode() {
        return walletCurrencyCode;
    }

    public void setWalletCurrencyCode(String walletCurrencyCode) {
        this.walletCurrencyCode = walletCurrencyCode;
    }

    public int getPriceRating() {
        return priceRating;
    }

    public void setPriceRating(int priceRating) {
        this.priceRating = priceRating;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCountryPhoneCode() {
        return countryPhoneCode;
    }

    public void setCountryPhoneCode(String countryPhoneCode) {
        this.countryPhoneCode = countryPhoneCode;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public boolean isPhoneNumberVerified() {
        return isPhoneNumberVerified;
    }

    public void setPhoneNumberVerified(boolean isPhoneNumberVerified) {
        this.isPhoneNumberVerified = isPhoneNumberVerified;
    }

    public ArrayList<com.edelivery.store.models.datamodel.StoreTime> getStoreTime() {
        return storeTime;
    }

    public void setStoreTime(ArrayList<StoreTime> storeTime) {
        this.storeTime = storeTime;
    }

    public int getTotalReferrals() {
        return totalReferrals;
    }

    public void setTotalReferrals(int totalReferrals) {
        this.totalReferrals = totalReferrals;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isBusiness() {
        return isBusiness;
    }

    public void setBusiness(boolean isBusiness) {
        this.isBusiness = isBusiness;
    }

    public int getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(int uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getWallet() {
        return wallet;
    }

    public void setWallet(double wallet) {
        this.wallet = wallet;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getServerToken() {
        return serverToken;
    }

    public void setServerToken(String serverToken) {
        this.serverToken = serverToken;
    }

    public int getRateCount() {
        return rateCount;
    }

    public void setRateCount(int rateCount) {
        this.rateCount = rateCount;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public String getName() {
        if (name instanceof String) {
            return String.valueOf(name);
        } else {
            return Utilities.getDetailStringFromList((ArrayList<String>) name,
                    Language.getInstance().
                            getAdminLanguageIndex());

        }
    }

    public List<String> getNameList() {
        return (ArrayList<String>) name;
    }

    public void setName(Object name) {
        this.name = name;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean isApproved) {
        this.isApproved = isApproved;
    }

    public List<Double> getLocation() {
        return location;
    }

    public void setLocation(List<Double> location) {
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public double getFreeDeliveryPrice() {
        return freeDeliveryPrice;
    }

    public void setFreeDeliveryPrice(double freeDeliveryPrice) {
        this.freeDeliveryPrice = freeDeliveryPrice;
    }

    public ArrayList<String> getSocialIds() {
        return socialIds;
    }

    public void setSocialIds(ArrayList<String> socialIds) {
        this.socialIds = socialIds;
    }

    public int getInformScheduleOrderBeforeMin() {
        return informScheduleOrderBeforeMin;
    }

    public void setInformScheduleOrderBeforeMin(int informScheduleOrderBeforeMin) {
        this.informScheduleOrderBeforeMin = informScheduleOrderBeforeMin;
    }

    public int getScheduleOrderCreateAfterMinute() {
        return scheduleOrderCreateAfterMinute;
    }

    public void setScheduleOrderCreateAfterMinute(int scheduleOrderCreateAfterMinute) {
        this.scheduleOrderCreateAfterMinute = scheduleOrderCreateAfterMinute;
    }

    public boolean isIsTakingScheduleOrder() {
        return isTakingScheduleOrder;
    }

    public void setIsTakingScheduleOrder(boolean isTakingScheduleOrder) {
        this.isTakingScheduleOrder = isTakingScheduleOrder;
    }

    public double getMinOrderPrice() {
        return minOrderPrice;
    }

    public void setMinOrderPrice(double minOrderPrice) {
        this.minOrderPrice = minOrderPrice;
    }

    public boolean isIsProvideDeliveryAnywhere() {
        return isProvideDeliveryAnywhere;
    }

    public void setIsProvideDeliveryAnywhere(boolean isProvideDeliveryAnywhere) {
        this.isProvideDeliveryAnywhere = isProvideDeliveryAnywhere;
    }

    public int getOrderCancellationChargeType() {
        return orderCancellationChargeType;
    }

    public void setOrderCancellationChargeType(int orderCancellationChargeType) {
        this.orderCancellationChargeType = orderCancellationChargeType;
    }

    public double getDeliveryRadius() {
        return deliveryRadius;
    }

    public void setDeliveryRadius(double deliveryRadius) {
        this.deliveryRadius = deliveryRadius;
    }

    public int getMaxItemQuantityAddByUser() {
        return maxItemQuantityAddByUser;
    }

    public void setMaxItemQuantityAddByUser(int maxItemQuantityAddByUser) {
        this.maxItemQuantityAddByUser = maxItemQuantityAddByUser;
    }

    public double getOrderCancellationChargeValue() {
        return orderCancellationChargeValue;
    }

    public void setOrderCancellationChargeValue(double orderCancellationChargeValue) {
        this.orderCancellationChargeValue = orderCancellationChargeValue;
    }

    public double getOrderCancellationChargeForAboveOrderPrice() {
        return orderCancellationChargeForAboveOrderPrice;
    }

    public void setOrderCancellationChargeForAboveOrderPrice(double orderCancellationChargeForAboveOrderPrice) {
        this.orderCancellationChargeForAboveOrderPrice = orderCancellationChargeForAboveOrderPrice;
    }

    public boolean isIsOrderCancellationChargeApply() {
        return isOrderCancellationChargeApply;
    }

    public void setIsOrderCancellationChargeApply(boolean isOrderCancellationChargeApply) {
        this.isOrderCancellationChargeApply = isOrderCancellationChargeApply;
    }

    public List<Languages> getLanguages() {
        return languages;
    }

    public void setLanguages(List<Languages> languages) {
        this.languages = languages;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isStoreCanAddProvider ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isStoreCanCompleteOrder ? (byte) 1 : (byte) 0);
        dest.writeString(this.loginBy);
        dest.writeString(this.socialId);
        dest.writeDouble(this.longitude);
        dest.writeDouble(this.latitude);
        dest.writeString(this.oldPassword);
        dest.writeString(this.storeId);
        dest.writeByte(this.isUseItemTax ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isProvidePickupDelivery ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isAskEstimatedTimeForReadyOrder ? (byte) 1 : (byte) 0);
        dest.writeList(famousProductsTags);
        dest.writeDouble(this.freeDeliveryWithinRadius);
        dest.writeParcelable(this.deliveryDetails, flags);
        dest.writeByte(this.isBusy ? (byte) 1 : (byte) 0);
        dest.writeStringList(this.socialIds);
        dest.writeString(this.storeDeliveryId);
        dest.writeByte(this.isEmailVerified ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isDocumentUploaded ? (byte) 1 : (byte) 0);
        dest.writeString(this.createdAt);
        dest.writeString(this.deviceType);
        dest.writeDouble(this.adminProfitModeOnStore);
        dest.writeDouble(this.itemTax);
        dest.writeByte(this.isReferral ? (byte) 1 : (byte) 0);
        dest.writeString(this.walletCurrencyCode);
        dest.writeInt(this.priceRating);
        dest.writeString(this.password);
        dest.writeString(this.updatedAt);
        dest.writeString(this.countryPhoneCode);
        dest.writeDouble(this.rate);
        dest.writeByte(this.isPhoneNumberVerified ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.storeTime);
        dest.writeTypedList(this.storeDeliveryTime);
        dest.writeInt(this.totalReferrals);
        dest.writeString(this.currency);
        dest.writeString(this.email);
        dest.writeByte(this.isBusiness ? (byte) 1 : (byte) 0);
        dest.writeDouble(this.freeDeliveryPrice);
        dest.writeValue(this.uniqueId);
        dest.writeString(this.address);
        dest.writeDouble(this.wallet);
        dest.writeString(this.imageUrl);
        dest.writeString(this.serverToken);
        dest.writeInt(this.rateCount);
        dest.writeString(this.websiteUrl);
        dest.writeString(this.phone);
        dest.writeString(this.deviceToken);
        dest.writeString(this.referralCode);
        if (name instanceof String) {
            dest.writeString(String.valueOf(name));
        } else {
            dest.writeStringList((List<String>) name);
        }
        dest.writeByte(this.isApproved ? (byte) 1 : (byte) 0);
        dest.writeList(this.location);
        dest.writeString(this.id);
        dest.writeString(this.slogan);
        dest.writeString(this.countryId);
        dest.writeString(this.cityId);
        dest.writeByte(this.isStorePayDeliveryFees ? (byte) 1 : (byte) 0);
        dest.writeInt(this.deliveryTime);
        dest.writeInt(this.deliveryTimeMax);
        dest.writeInt(this.informScheduleOrderBeforeMin);
        dest.writeInt(this.scheduleOrderCreateAfterMinute);
        dest.writeByte(this.isTakingScheduleOrder ? (byte) 1 : (byte) 0);
        dest.writeDouble(this.minOrderPrice);
        dest.writeByte(this.isProvideDeliveryAnywhere ? (byte) 1 : (byte) 0);
        dest.writeInt(this.orderCancellationChargeType);
        dest.writeDouble(this.deliveryRadius);
        dest.writeInt(this.maxItemQuantityAddByUser);
        dest.writeDouble(this.orderCancellationChargeValue);
        dest.writeDouble(this.orderCancellationChargeForAboveOrderPrice);
        dest.writeByte(this.isOrderCancellationChargeApply ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.languages);
    }

    public ArrayList<StoreTime> getStoreDeliveryTime() {
        return storeDeliveryTime;
    }

    public void setStoreDeliveryTime(ArrayList<StoreTime> storeDeliveryTime) {
        this.storeDeliveryTime = storeDeliveryTime;
    }

    public boolean isStoreSetScheduleDeliveryTime() {
        return isStoreSetScheduleDeliveryTime;
    }

    public void setStoreSetScheduleDeliveryTime(boolean storeSetScheduleDeliveryTime) {
        isStoreSetScheduleDeliveryTime = storeSetScheduleDeliveryTime;
    }
}