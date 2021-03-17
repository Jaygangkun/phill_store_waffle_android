package com.edelivery.store.models.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class StoreSettings {


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
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("item_tax")
    @Expose
    private double itemTax;
    @SerializedName("wallet_currency_code")
    @Expose
    private String walletCurrencyCode;
    @SerializedName("price_rating")
    @Expose
    private int priceRating;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("country_phone_code")
    @Expose
    private String countryPhoneCode;
    @SerializedName("store_delivery_time")
    @Expose
    private ArrayList<StoreTime> storeDeliveryTime;
    @SerializedName("store_time")
    @Expose
    private ArrayList<StoreTime> storeTime;
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
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("server_token")
    @Expose
    private String serverToken;
    @SerializedName("website_url")
    @Expose
    private String websiteUrl;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("location")
    @Expose
    private List<Double> location;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("slogan")
    @Expose
    private String slogan;
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
    @SerializedName("is_store_set_schedule_delivery_time")
    private boolean isStoreSetScheduleDeliveryTime;
    @SerializedName("languages_supported")
    private List<Languages> languages;

    public StoreSettings() {
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


    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }


    public double getItemTax() {
        return itemTax;
    }

    public void setItemTax(double itemTax) {
        this.itemTax = itemTax;
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


    public ArrayList<StoreTime> getStoreTime() {
        return storeTime;
    }

    public void setStoreTime(ArrayList<StoreTime> storeTime) {
        this.storeTime = storeTime;
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


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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


    public double getFreeDeliveryPrice() {
        return freeDeliveryPrice;
    }

    public void setFreeDeliveryPrice(double freeDeliveryPrice) {
        this.freeDeliveryPrice = freeDeliveryPrice;
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

    public ArrayList<StoreTime> getStoreDeliveryTime() {
        return storeDeliveryTime;
    }

    public void setStoreDeliveryTime(ArrayList<StoreTime> storeDeliveryTime) {
        this.storeDeliveryTime = storeDeliveryTime;
    }

    public void setStoreSetScheduleDeliveryTime(boolean storeSetScheduleDeliveryTime) {
        isStoreSetScheduleDeliveryTime = storeSetScheduleDeliveryTime;
    }
}