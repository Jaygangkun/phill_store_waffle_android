package com.edelivery.store.models.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class OrderPaymentDetail implements Parcelable {


    public static final Creator<OrderPaymentDetail> CREATOR = new Creator<OrderPaymentDetail>() {
        @Override
        public OrderPaymentDetail createFromParcel(Parcel source) {
            return new OrderPaymentDetail(source);
        }

        @Override
        public OrderPaymentDetail[] newArray(int size) {
            return new OrderPaymentDetail[size];
        }
    };
    @SerializedName("delivery_price_used_type")
    @Expose
    private int deliveryPriceUsedType;
    @SerializedName("user_pay_payment")
    @Expose
    private double userPayPayment;
    @SerializedName("total_cart_price")
    @Expose
    private double totalCartPrice;
    @SerializedName("is_user_pick_up_order")
    @Expose
    private boolean isUserPickUpOrder;
    @SerializedName("is_store_pay_delivery_fees")
    @Expose
    private boolean isStorePayDeliveryFees;
    @SerializedName("is_order_payment_status_set_by_store")
    private boolean isOrderPaymentStatusSetByStore;
    @SerializedName("is_promo_for_delivery_service")
    @Expose
    private boolean isPromoForDeliveryService;
    @SerializedName("order_unique_id")
    private int orderUniqueId;
    @SerializedName("total_base_price")
    @Expose
    private double totalBasePrice;
    @SerializedName("total_time_price")
    @Expose
    private double totalTimePrice;
    @SerializedName("total_admin_profit_on_delivery")
    @Expose
    private double totalAdminProfitOnDelivery;
    @SerializedName("price_per_unit_distance")
    @Expose
    private double pricePerUnitDistance;
    @SerializedName("admin_profit_mode_on_store")
    @Expose
    private double adminProfitModeOnStore;
    @SerializedName("distance_price")
    @Expose
    private double distancePrice;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("wallet_payment")
    @Expose
    private double walletPayment;
    @SerializedName("min_fare")
    @Expose
    private double minFare;
    @SerializedName("total_delivery_and_store_price")
    @Expose
    private int totalDeliveryAndStorePrice;
    @SerializedName("__v")
    @Expose
    private int V;
    @SerializedName("total_distance")
    @Expose
    private double totalDistance;
    @SerializedName("total_after_tax_price")
    @Expose
    private double totalAfterTaxPrice;
    @SerializedName("total_time")
    @Expose
    private double totalTime;
    @SerializedName("card_payment")
    @Expose
    private double cardPayment;
    @SerializedName("unique_id")
    @Expose
    private int uniqueId;
    @SerializedName("surge_charges")
    @Expose
    private double surgeCharges;
    @SerializedName("total_admin_tax_price")
    @Expose
    private double totalAdminTaxPrice;
    @SerializedName("admin_profit_value_on_store")
    @Expose
    private double adminProfitValueOnStore;
    @SerializedName("total_service_price")
    @Expose
    private double totalServicePrice;
    @SerializedName("total_store_tax_price")
    @Expose
    private double totalStoreTaxPrice;
    @SerializedName("is_payment_mode_cash")
    @Expose
    private boolean isPaymentModeCash;
    @SerializedName("total_specifications")
    @Expose
    private int totalSpecifications;
    @SerializedName("cash_payment")
    @Expose
    private double cashPayment;
    @SerializedName("is_cancellation_fee")
    @Expose
    private boolean isCancellationFee;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("admin_currency_code")
    @Expose
    private String adminCurrencyCode;
    @SerializedName("service_tax")
    @Expose
    private double serviceTax;
    @SerializedName("provider_id")
    @Expose
    private Object providerId;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("total_delivery_price_after_surge")
    @Expose
    private double totalDeliveryPriceAfterSurge;
    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("total_delivery_price")
    @Expose
    private double totalDeliveryPrice;
    @SerializedName("total_after_wallet_payment")
    @Expose
    private double totalAfterWalletPayment;
    @SerializedName("price_per_unit_time")
    @Expose
    private double pricePerUnitTime;
    @SerializedName("total_item_count")
    @Expose
    private int totalItem;
    @SerializedName("total_admin_profit_on_store")
    @Expose
    private double totalAdminProfitOnStore;
    @SerializedName("is_payment_paid")
    @Expose
    private boolean isPaymentPaid;
    @SerializedName("total_distance_price")
    @Expose
    private double totalDistancePrice;
    @SerializedName("item_tax")
    @Expose
    private double itemTax;
    @SerializedName("admin_profit_mode_on_delivery")
    @Expose
    private double adminProfitModeOnDelivery;
    @SerializedName("currency_code")
    @Expose
    private String currencyCode;
    @SerializedName("total_order_price")
    @Expose
    private double totalOrderPrice;
    @SerializedName("is_pending_payment")
    @Expose
    private boolean isPendingPayment;
    @SerializedName("admin_profit_value_on_delivery")
    @Expose
    private double adminProfitValueOnDelivery;
    @SerializedName("total")
    @Expose
    private double total;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("payment_id")
    @Expose
    private String paymentId;
    @SerializedName("is_surge_hours")
    @Expose
    private boolean isSurgeHours;
    @SerializedName("base_price")
    @Expose
    private double basePrice;
    @SerializedName("delivered_at")
    @Expose
    private String deliveredAt;
    @SerializedName("base_price_distance")
    @Expose
    private double basePriceDistance;
    @SerializedName("promo_payment")
    @Expose
    private double promoPayment;
    @SerializedName("current_rate")
    @Expose
    private double currentRate;
    @SerializedName("is_min_fare_used")
    @Expose
    private boolean isMinFareUsed;
    @SerializedName("total_surge_price")
    @Expose
    private double totalSurgePrice;
    @SerializedName("total_provider_income")
    @Expose
    private double totalProviderIncome;
    @SerializedName("remaining_payment")
    @Expose
    private double remainingPayment;
    @SerializedName("is_distance_unit_mile")
    @Expose
    private boolean isDistanceUnitMile;
    @SerializedName("total_store_income")
    @Expose
    private double totalStoreIncome;
    @SerializedName("total_item_price")
    @Expose
    private double totalItemPrice;
    @SerializedName("total_item_specification_price")
    @Expose
    private double totalItemSpecificationPrice;
    @SerializedName("pay_to_store")
    private double payToStore;
    @SerializedName("total_store_have_payment")
    private double totalStoreHavePayment;
    @SerializedName("store_have_order_payment")
    private double storeHaveOrderPayment;
    @SerializedName("store_have_service_payment")
    private double storeHaveServicePayment;
    public OrderPaymentDetail() {
    }

    protected OrderPaymentDetail(Parcel in) {
        this.deliveryPriceUsedType = in.readInt();
        this.userPayPayment = in.readDouble();
        this.totalCartPrice = in.readDouble();
        this.isUserPickUpOrder = in.readByte() != 0;
        this.isStorePayDeliveryFees = in.readByte() != 0;
        this.isOrderPaymentStatusSetByStore = in.readByte() != 0;
        this.isPromoForDeliveryService = in.readByte() != 0;
        this.orderUniqueId = in.readInt();
        this.totalBasePrice = in.readDouble();
        this.totalTimePrice = in.readDouble();
        this.totalAdminProfitOnDelivery = in.readDouble();
        this.pricePerUnitDistance = in.readDouble();
        this.adminProfitModeOnStore = in.readDouble();
        this.distancePrice = in.readDouble();
        this.createdAt = in.readString();
        this.walletPayment = in.readDouble();
        this.minFare = in.readDouble();
        this.totalDeliveryAndStorePrice = in.readInt();
        this.V = in.readInt();
        this.totalDistance = in.readDouble();
        this.totalAfterTaxPrice = in.readDouble();
        this.totalTime = in.readDouble();
        this.cardPayment = in.readDouble();
        this.uniqueId = in.readInt();
        this.surgeCharges = in.readDouble();
        this.totalAdminTaxPrice = in.readDouble();
        this.adminProfitValueOnStore = in.readDouble();
        this.totalServicePrice = in.readDouble();
        this.totalStoreTaxPrice = in.readDouble();
        this.isPaymentModeCash = in.readByte() != 0;
        this.totalSpecifications = in.readInt();
        this.cashPayment = in.readDouble();
        this.isCancellationFee = in.readByte() != 0;
        this.userId = in.readString();
        this.adminCurrencyCode = in.readString();
        this.serviceTax = in.readDouble();
        this.id = in.readString();
        this.totalDeliveryPriceAfterSurge = in.readDouble();
        this.orderId = in.readString();
        this.totalDeliveryPrice = in.readDouble();
        this.totalAfterWalletPayment = in.readDouble();
        this.pricePerUnitTime = in.readDouble();
        this.totalItem = in.readInt();
        this.totalAdminProfitOnStore = in.readDouble();
        this.isPaymentPaid = in.readByte() != 0;
        this.totalDistancePrice = in.readDouble();
        this.itemTax = in.readDouble();
        this.adminProfitModeOnDelivery = in.readDouble();
        this.currencyCode = in.readString();
        this.totalOrderPrice = in.readDouble();
        this.isPendingPayment = in.readByte() != 0;
        this.adminProfitValueOnDelivery = in.readDouble();
        this.total = in.readDouble();
        this.updatedAt = in.readString();
        this.paymentId = in.readString();
        this.isSurgeHours = in.readByte() != 0;
        this.basePrice = in.readDouble();
        this.deliveredAt = in.readString();
        this.basePriceDistance = in.readDouble();
        this.promoPayment = in.readDouble();
        this.currentRate = in.readDouble();
        this.isMinFareUsed = in.readByte() != 0;
        this.totalSurgePrice = in.readDouble();
        this.totalProviderIncome = in.readDouble();
        this.remainingPayment = in.readDouble();
        this.isDistanceUnitMile = in.readByte() != 0;
        this.totalStoreIncome = in.readDouble();
        this.totalItemPrice = in.readDouble();
        this.totalItemSpecificationPrice = in.readDouble();
        this.payToStore = in.readDouble();
        this.totalStoreHavePayment = in.readDouble();
        this.storeHaveOrderPayment = in.readDouble();
        this.storeHaveServicePayment = in.readDouble();
    }

    public int getDeliveryPriceUsedType() {
        return deliveryPriceUsedType;
    }

    public void setDeliveryPriceUsedType(int deliveryPriceUsedType) {
        this.deliveryPriceUsedType = deliveryPriceUsedType;
    }

    public double getUserPayPayment() {
        return userPayPayment;
    }

    public void setUserPayPayment(double userPayPayment) {
        this.userPayPayment = userPayPayment;
    }

    public double getTotalCartPrice() {
        return totalCartPrice;
    }

    public void setTotalCartPrice(double totalCartPrice) {
        this.totalCartPrice = totalCartPrice;
    }

    public boolean isUserPickUpOrder() {
        return isUserPickUpOrder;
    }

    public void setUserPickUpOrder(boolean userPickUpOrder) {
        isUserPickUpOrder = userPickUpOrder;
    }

    public double getTotalItemSpecificationPrice() {
        return totalItemSpecificationPrice;
    }

    public void setTotalItemSpecificationPrice(double totalItemSpecificationPrice) {
        this.totalItemSpecificationPrice = totalItemSpecificationPrice;
    }

    public double getTotalItemPrice() {
        return totalItemPrice;
    }

    public void setTotalItemPrice(double totalItemPrice) {
        this.totalItemPrice = totalItemPrice;
    }

    public double getTotalBasePrice() {
        return totalBasePrice;
    }

    public void setTotalBasePrice(double totalBasePrice) {
        this.totalBasePrice = totalBasePrice;
    }

    public double getTotalTimePrice() {
        return totalTimePrice;
    }

    public void setTotalTimePrice(double totalTimePrice) {
        this.totalTimePrice = totalTimePrice;
    }

    public double getTotalAdminProfitOnDelivery() {
        return totalAdminProfitOnDelivery;
    }

    public void setTotalAdminProfitOnDelivery(double totalAdminProfitOnDelivery) {
        this.totalAdminProfitOnDelivery = totalAdminProfitOnDelivery;
    }

    public double getPricePerUnitDistance() {
        return pricePerUnitDistance;
    }

    public void setPricePerUnitDistance(double pricePerUnitDistance) {
        this.pricePerUnitDistance = pricePerUnitDistance;
    }

    public double getAdminProfitModeOnStore() {
        return adminProfitModeOnStore;
    }

    public void setAdminProfitModeOnStore(double adminProfitModeOnStore) {
        this.adminProfitModeOnStore = adminProfitModeOnStore;
    }

    public double getDistancePrice() {
        return distancePrice;
    }

    public void setDistancePrice(double distancePrice) {
        this.distancePrice = distancePrice;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public double getWalletPayment() {
        return walletPayment;
    }

    public void setWalletPayment(double walletPayment) {
        this.walletPayment = walletPayment;
    }

    public double getMinFare() {
        return minFare;
    }

    public void setMinFare(double minFare) {
        this.minFare = minFare;
    }

    public int getTotalDeliveryAndStorePrice() {
        return totalDeliveryAndStorePrice;
    }

    public void setTotalDeliveryAndStorePrice(int totalDeliveryAndStorePrice) {
        this.totalDeliveryAndStorePrice = totalDeliveryAndStorePrice;
    }

    public int getV() {
        return V;
    }

    public void setV(int V) {
        this.V = V;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(double totalDistance) {
        this.totalDistance = totalDistance;
    }

    public double getTotalAfterTaxPrice() {
        return totalAfterTaxPrice;
    }

    public void setTotalAfterTaxPrice(double totalAfterTaxPrice) {
        this.totalAfterTaxPrice = totalAfterTaxPrice;
    }

    public double getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(double totalTime) {
        this.totalTime = totalTime;
    }

    public double getCardPayment() {
        return cardPayment;
    }

    public void setCardPayment(double cardPayment) {
        this.cardPayment = cardPayment;
    }

    public int getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(int uniqueId) {
        this.uniqueId = uniqueId;
    }

    public double getSurgeCharges() {
        return surgeCharges;
    }

    public void setSurgeCharges(double surgeCharges) {
        this.surgeCharges = surgeCharges;
    }

    public double getTotalAdminTaxPrice() {
        return totalAdminTaxPrice;
    }

    public void setTotalAdminTaxPrice(double totalAdminTaxPrice) {
        this.totalAdminTaxPrice = totalAdminTaxPrice;
    }

    public double getAdminProfitValueOnStore() {
        return adminProfitValueOnStore;
    }

    public void setAdminProfitValueOnStore(double adminProfitValueOnStore) {
        this.adminProfitValueOnStore = adminProfitValueOnStore;
    }

    public double getTotalServicePrice() {
        return totalServicePrice;
    }

    public void setTotalServicePrice(double totalServicePrice) {
        this.totalServicePrice = totalServicePrice;
    }

    public double getTotalStoreTaxPrice() {
        return totalStoreTaxPrice;
    }

    public void setTotalStoreTaxPrice(double totalStoreTaxPrice) {
        this.totalStoreTaxPrice = totalStoreTaxPrice;
    }

    public boolean isIsPaymentModeCash() {
        return isPaymentModeCash;
    }

    public void setIsPaymentModeCash(boolean isPaymentModeCash) {
        this.isPaymentModeCash = isPaymentModeCash;
    }

    public int getTotalSpecifications() {
        return totalSpecifications;
    }

    public void setTotalSpecifications(int totalSpecifications) {
        this.totalSpecifications = totalSpecifications;
    }

    public double getCashPayment() {
        return cashPayment;
    }

    public void setCashPayment(double cashPayment) {
        this.cashPayment = cashPayment;
    }

    public boolean isIsCancellationFee() {
        return isCancellationFee;
    }

    public void setIsCancellationFee(boolean isCancellationFee) {
        this.isCancellationFee = isCancellationFee;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAdminCurrencyCode() {
        return adminCurrencyCode;
    }

    public void setAdminCurrencyCode(String adminCurrencyCode) {
        this.adminCurrencyCode = adminCurrencyCode;
    }

    public double getServiceTax() {
        return serviceTax;
    }

    public void setServiceTax(double serviceTax) {
        this.serviceTax = serviceTax;
    }

    public Object getProviderId() {
        return providerId;
    }

    public void setProviderId(Object providerId) {
        this.providerId = providerId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getTotalDeliveryPriceAfterSurge() {
        return totalDeliveryPriceAfterSurge;
    }

    public void setTotalDeliveryPriceAfterSurge(double totalDeliveryPriceAfterSurge) {
        this.totalDeliveryPriceAfterSurge = totalDeliveryPriceAfterSurge;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public double getTotalDeliveryPrice() {
        return totalDeliveryPrice;
    }

    public void setTotalDeliveryPrice(double totalDeliveryPrice) {
        this.totalDeliveryPrice = totalDeliveryPrice;
    }

    public double getTotalAfterWalletPayment() {
        return totalAfterWalletPayment;
    }

    public void setTotalAfterWalletPayment(double totalAfterWalletPayment) {
        this.totalAfterWalletPayment = totalAfterWalletPayment;
    }

    public double getPricePerUnitTime() {
        return pricePerUnitTime;
    }

    public void setPricePerUnitTime(double pricePerUnitTime) {
        this.pricePerUnitTime = pricePerUnitTime;
    }

    public int getTotalItem() {
        return totalItem;
    }

    public void setTotalItem(int totalItem) {
        this.totalItem = totalItem;
    }

    public double getTotalAdminProfitOnStore() {
        return totalAdminProfitOnStore;
    }

    public void setTotalAdminProfitOnStore(double totalAdminProfitOnStore) {
        this.totalAdminProfitOnStore = totalAdminProfitOnStore;
    }

    public boolean isIsPaymentPaid() {
        return isPaymentPaid;
    }

    public void setIsPaymentPaid(boolean isPaymentPaid) {
        this.isPaymentPaid = isPaymentPaid;
    }

    public double getTotalDistancePrice() {
        return totalDistancePrice;
    }

    public void setTotalDistancePrice(double totalDistancePrice) {
        this.totalDistancePrice = totalDistancePrice;
    }

    public double getItemTax() {
        return itemTax;
    }

    public void setItemTax(double itemTax) {
        this.itemTax = itemTax;
    }

    public double getAdminProfitModeOnDelivery() {
        return adminProfitModeOnDelivery;
    }

    public void setAdminProfitModeOnDelivery(double adminProfitModeOnDelivery) {
        this.adminProfitModeOnDelivery = adminProfitModeOnDelivery;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public double getTotalOrderPrice() {
        return totalOrderPrice;
    }

    public void setTotalOrderPrice(double totalOrderPrice) {
        this.totalOrderPrice = totalOrderPrice;
    }

    public boolean isIsPendingPayment() {
        return isPendingPayment;
    }

    public void setIsPendingPayment(boolean isPendingPayment) {
        this.isPendingPayment = isPendingPayment;
    }

    public double getAdminProfitValueOnDelivery() {
        return adminProfitValueOnDelivery;
    }

    public void setAdminProfitValueOnDelivery(double adminProfitValueOnDelivery) {
        this.adminProfitValueOnDelivery = adminProfitValueOnDelivery;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public boolean isIsSurgeHours() {
        return isSurgeHours;
    }

    public void setIsSurgeHours(boolean isSurgeHours) {
        this.isSurgeHours = isSurgeHours;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public String getDeliveredAt() {
        return deliveredAt;
    }

    public void setDeliveredAt(String deliveredAt) {
        this.deliveredAt = deliveredAt;
    }

    public double getBasePriceDistance() {
        return basePriceDistance;
    }

    public void setBasePriceDistance(double basePriceDistance) {
        this.basePriceDistance = basePriceDistance;
    }

    public double getPromoPayment() {
        return promoPayment;
    }

    public void setPromoPayment(double promoPayment) {
        this.promoPayment = promoPayment;
    }

    public double getCurrentRate() {
        return currentRate;
    }

    public void setCurrentRate(double currentRate) {
        this.currentRate = currentRate;
    }

    public boolean isIsMinFareUsed() {
        return isMinFareUsed;
    }

    public void setIsMinFareUsed(boolean isMinFareUsed) {
        this.isMinFareUsed = isMinFareUsed;
    }

    public double getTotalSurgePrice() {
        return totalSurgePrice;
    }

    public void setTotalSurgePrice(double totalSurgePrice) {
        this.totalSurgePrice = totalSurgePrice;
    }

    public double getTotalProviderIncome() {
        return totalProviderIncome;
    }

    public void setTotalProviderIncome(double totalProviderIncome) {
        this.totalProviderIncome = totalProviderIncome;
    }

    public double getRemainingPayment() {
        return remainingPayment;
    }

    public void setRemainingPayment(double remainingPayment) {
        this.remainingPayment = remainingPayment;
    }

    public boolean isIsDistanceUnitMile() {
        return isDistanceUnitMile;
    }

    public void setIsDistanceUnitMile(boolean isDistanceUnitMile) {
        this.isDistanceUnitMile = isDistanceUnitMile;
    }

    public double getTotalStoreIncome() {
        return totalStoreIncome;
    }

    public void setTotalStoreIncome(double totalStoreIncome) {
        this.totalStoreIncome = totalStoreIncome;
    }

    public boolean isPromoForDeliveryService() {
        return isPromoForDeliveryService;
    }

    public void setPromoForDeliveryService(boolean promoForDeliveryService) {
        isPromoForDeliveryService = promoForDeliveryService;
    }

    public double getPayToStore() {
        return payToStore;
    }

    public void setPayToStore(double payToStore) {
        this.payToStore = payToStore;
    }

    public double getTotalStoreHavePayment() {
        return totalStoreHavePayment;
    }

    public void setTotalStoreHavePayment(double totalStoreHavePayment) {
        this.totalStoreHavePayment = totalStoreHavePayment;
    }

    public double getStoreHaveOrderPayment() {
        return storeHaveOrderPayment;
    }

    public void setStoreHaveOrderPayment(double storeHaveOrderPayment) {
        this.storeHaveOrderPayment = storeHaveOrderPayment;
    }

    public double getStoreHaveServicePayment() {
        return storeHaveServicePayment;
    }

    public void setStoreHaveServicePayment(double storeHaveServicePayment) {
        this.storeHaveServicePayment = storeHaveServicePayment;
    }

    public boolean isOrderPaymentStatusSetByStore() {
        return isOrderPaymentStatusSetByStore;
    }

    public void setOrderPaymentStatusSetByStore(boolean orderPaymentStatusSetByStore) {
        isOrderPaymentStatusSetByStore = orderPaymentStatusSetByStore;
    }

    public int getOrderUniqueId() {
        return orderUniqueId;
    }

    public void setOrderUniqueId(int orderUniqueId) {
        this.orderUniqueId = orderUniqueId;
    }

    public boolean isStorePayDeliveryFees() {
        return isStorePayDeliveryFees;
    }

    public void setStorePayDeliveryFees(boolean storePayDeliveryFees) {
        isStorePayDeliveryFees = storePayDeliveryFees;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.deliveryPriceUsedType);
        dest.writeDouble(this.userPayPayment);
        dest.writeDouble(this.totalCartPrice);
        dest.writeByte(this.isUserPickUpOrder ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isStorePayDeliveryFees ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isOrderPaymentStatusSetByStore ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isPromoForDeliveryService ? (byte) 1 : (byte) 0);
        dest.writeInt(this.orderUniqueId);
        dest.writeDouble(this.totalBasePrice);
        dest.writeDouble(this.totalTimePrice);
        dest.writeDouble(this.totalAdminProfitOnDelivery);
        dest.writeDouble(this.pricePerUnitDistance);
        dest.writeDouble(this.adminProfitModeOnStore);
        dest.writeDouble(this.distancePrice);
        dest.writeString(this.createdAt);
        dest.writeDouble(this.walletPayment);
        dest.writeDouble(this.minFare);
        dest.writeInt(this.totalDeliveryAndStorePrice);
        dest.writeInt(this.V);
        dest.writeDouble(this.totalDistance);
        dest.writeDouble(this.totalAfterTaxPrice);
        dest.writeDouble(this.totalTime);
        dest.writeDouble(this.cardPayment);
        dest.writeInt(this.uniqueId);
        dest.writeDouble(this.surgeCharges);
        dest.writeDouble(this.totalAdminTaxPrice);
        dest.writeDouble(this.adminProfitValueOnStore);
        dest.writeDouble(this.totalServicePrice);
        dest.writeDouble(this.totalStoreTaxPrice);
        dest.writeByte(this.isPaymentModeCash ? (byte) 1 : (byte) 0);
        dest.writeInt(this.totalSpecifications);
        dest.writeDouble(this.cashPayment);
        dest.writeByte(this.isCancellationFee ? (byte) 1 : (byte) 0);
        dest.writeString(this.userId);
        dest.writeString(this.adminCurrencyCode);
        dest.writeDouble(this.serviceTax);
        dest.writeString(this.id);
        dest.writeDouble(this.totalDeliveryPriceAfterSurge);
        dest.writeString(this.orderId);
        dest.writeDouble(this.totalDeliveryPrice);
        dest.writeDouble(this.totalAfterWalletPayment);
        dest.writeDouble(this.pricePerUnitTime);
        dest.writeInt(this.totalItem);
        dest.writeDouble(this.totalAdminProfitOnStore);
        dest.writeByte(this.isPaymentPaid ? (byte) 1 : (byte) 0);
        dest.writeDouble(this.totalDistancePrice);
        dest.writeDouble(this.itemTax);
        dest.writeDouble(this.adminProfitModeOnDelivery);
        dest.writeString(this.currencyCode);
        dest.writeDouble(this.totalOrderPrice);
        dest.writeByte(this.isPendingPayment ? (byte) 1 : (byte) 0);
        dest.writeDouble(this.adminProfitValueOnDelivery);
        dest.writeDouble(this.total);
        dest.writeString(this.updatedAt);
        dest.writeString(this.paymentId);
        dest.writeByte(this.isSurgeHours ? (byte) 1 : (byte) 0);
        dest.writeDouble(this.basePrice);
        dest.writeString(this.deliveredAt);
        dest.writeDouble(this.basePriceDistance);
        dest.writeDouble(this.promoPayment);
        dest.writeDouble(this.currentRate);
        dest.writeByte(this.isMinFareUsed ? (byte) 1 : (byte) 0);
        dest.writeDouble(this.totalSurgePrice);
        dest.writeDouble(this.totalProviderIncome);
        dest.writeDouble(this.remainingPayment);
        dest.writeByte(this.isDistanceUnitMile ? (byte) 1 : (byte) 0);
        dest.writeDouble(this.totalStoreIncome);
        dest.writeDouble(this.totalItemPrice);
        dest.writeDouble(this.totalItemSpecificationPrice);
        dest.writeDouble(this.payToStore);
        dest.writeDouble(this.totalStoreHavePayment);
        dest.writeDouble(this.storeHaveOrderPayment);
        dest.writeDouble(this.storeHaveServicePayment);
    }
}