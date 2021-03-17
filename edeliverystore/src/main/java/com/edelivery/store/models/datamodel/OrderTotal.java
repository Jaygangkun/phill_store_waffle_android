package com.edelivery.store.models.datamodel;

import com.google.gson.annotations.SerializedName;

public class OrderTotal {


    @SerializedName("is_weekly_invoice_paid")
    private boolean isWeeklyInvoicePaid;

    @SerializedName("total_earning")
    private double totalEarning = 0;

    @SerializedName("wallet")
    private double wallet = 0;

    @SerializedName("total_item_price")
    private double totalItemPrice = 0;

    @SerializedName("total_admin_profit_on_store")
    private double totalAdminProfitOnStore = 0;

    @SerializedName("total_store_tax_price")
    private double totalStoreTaxPrice = 0;

    @SerializedName("store_have_order_payment")
    private double storeHaveOrderPayment = 0;

    @SerializedName("store_have_service_payment")
    private double storeHaveServicePayment = 0;

    @SerializedName("_id")
    private Object id;

    @SerializedName("pay_to_store")
    private double payToStore = 0;

    @SerializedName("total_store_income")
    private double totalStoreIncome = 0;

    @SerializedName("total_order_price")
    private double totalOrderPrice = 0;
    @SerializedName("total_wallet_income_set_in_cash_order")
    private double totalWalletIncomeSetInCashOrder = 0;
    @SerializedName("total_wallet_income_set_in_other_order")
    private double totalWalletIncomeSetInOtherOrder = 0;
    @SerializedName("total_provider_have_cash_payment_on_hand")
    private double totalProviderHaveCashPaymentOnHand = 0;
    @SerializedName("total_paid")
    private double totalPaid = 0;
    @SerializedName("total_remaining_to_paid")
    private double totalRemainingToPaid = 0;
    @SerializedName("total_wallet_income_set")
    private double totalWalletIncomeSet = 0;

    @SerializedName("total_cancellation_income")
    private double totalCancellationIncome;

    @SerializedName("store_have_promo_payment")
    private double storeHavePromoPayment;

    public double getTotalCancellationIncome() {
        return totalCancellationIncome;
    }

    public void setTotalCancellationIncome(double totalCancellationIncome) {
        this.totalCancellationIncome = totalCancellationIncome;
    }

    public double getStoreHavePromoPayment() {
        return storeHavePromoPayment;
    }

    public void setStoreHavePromoPayment(double storeHavePromoPayment) {
        this.storeHavePromoPayment = storeHavePromoPayment;
    }

    public double getTotalEarning() {
        return totalEarning;
    }

    public void setTotalEarning(double totalEarning) {
        this.totalEarning = totalEarning;
    }

    public double getWallet() {
        return wallet;
    }

    public void setWallet(double wallet) {
        this.wallet = wallet;
    }

    public double getTotalItemPrice() {
        return totalItemPrice;
    }

    public void setTotalItemPrice(double totalItemPrice) {
        this.totalItemPrice = totalItemPrice;
    }

    public double getTotalAdminProfitOnStore() {
        return totalAdminProfitOnStore;
    }

    public void setTotalAdminProfitOnStore(double totalAdminProfitOnStore) {
        this.totalAdminProfitOnStore = totalAdminProfitOnStore;
    }

    public double getTotalStoreTaxPrice() {
        return totalStoreTaxPrice;
    }

    public void setTotalStoreTaxPrice(double totalStoreTaxPrice) {
        this.totalStoreTaxPrice = totalStoreTaxPrice;
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

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public double getPayToStore() {
        return payToStore;
    }

    public void setPayToStore(double payToStore) {
        this.payToStore = payToStore;
    }

    public double getTotalStoreIncome() {
        return totalStoreIncome;
    }

    public void setTotalStoreIncome(double totalStoreIncome) {
        this.totalStoreIncome = totalStoreIncome;
    }

    public double getTotalOrderPrice() {
        return totalOrderPrice;
    }

    public void setTotalOrderPrice(double totalOrderPrice) {
        this.totalOrderPrice = totalOrderPrice;
    }

    public boolean isWeeklyInvoicePaid() {
        return isWeeklyInvoicePaid;
    }

    public void setWeeklyInvoicePaid(boolean weeklyInvoicePaid) {
        isWeeklyInvoicePaid = weeklyInvoicePaid;
    }

    public double getTotalWalletIncomeSetInCashOrder() {
        return totalWalletIncomeSetInCashOrder;
    }

    public void setTotalWalletIncomeSetInCashOrder(double totalWalletIncomeSetInCashOrder) {
        this.totalWalletIncomeSetInCashOrder = totalWalletIncomeSetInCashOrder;
    }

    public double getTotalWalletIncomeSetInOtherOrder() {
        return totalWalletIncomeSetInOtherOrder;
    }

    public void setTotalWalletIncomeSetInOtherOrder(double totalWalletIncomeSetInOtherOrder) {
        this.totalWalletIncomeSetInOtherOrder = totalWalletIncomeSetInOtherOrder;
    }

    public double getTotalProviderHaveCashPaymentOnHand() {
        return totalProviderHaveCashPaymentOnHand;
    }

    public void setTotalProviderHaveCashPaymentOnHand(double totalProviderHaveCashPaymentOnHand) {
        this.totalProviderHaveCashPaymentOnHand = totalProviderHaveCashPaymentOnHand;
    }

    public double getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(double totalPaid) {
        this.totalPaid = totalPaid;
    }

    public double getTotalRemainingToPaid() {
        return totalRemainingToPaid;
    }

    public void setTotalRemainingToPaid(double totalRemainingToPaid) {
        this.totalRemainingToPaid = totalRemainingToPaid;
    }

    public double getTotalWalletIncomeSet() {
        return totalWalletIncomeSet;
    }

    public void setTotalWalletIncomeSet(double totalWalletIncomeSet) {
        this.totalWalletIncomeSet = totalWalletIncomeSet;
    }
}