package com.edelivery.store.models.datamodel;

import com.google.gson.annotations.SerializedName;

public class Withdrawal {

    @SerializedName("requested_wallet_amount")
    private double requestedWalletAmount;

    @SerializedName("is_payment_mode_cash")
    private boolean isPaymentModeCash;

    @SerializedName("id")
    private String providerId;
    @SerializedName("server_token")
    private String serverToken;
    @SerializedName("type")
    private int type;
    @SerializedName("description_for_request_wallet_amount")
    private String descriptionForRequestWalletAmount;
    @SerializedName("transaction_details")
    private BankDetail bankDetail;

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public double getRequestedWalletAmount() {
        return requestedWalletAmount;
    }

    public void setRequestedWalletAmount(double requestedWalletAmount) {
        this.requestedWalletAmount = requestedWalletAmount;
    }

    public boolean isIsPaymentModeCash() {
        return isPaymentModeCash;
    }

    public void setIsPaymentModeCash(boolean isPaymentModeCash) {
        this.isPaymentModeCash = isPaymentModeCash;
    }

    public String getServerToken() {
        return serverToken;
    }

    public void setServerToken(String serverToken) {
        this.serverToken = serverToken;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDescriptionForRequestWalletAmount() {
        return descriptionForRequestWalletAmount;
    }

    public void setDescriptionForRequestWalletAmount(String descriptionForRequestWalletAmount) {
        this.descriptionForRequestWalletAmount = descriptionForRequestWalletAmount;
    }

    public BankDetail getBankDetail() {
        return bankDetail;
    }

    public void setBankDetail(BankDetail bankDetail) {
        this.bankDetail = bankDetail;
    }

    @Override
    public String toString() {
        return
                "Withdrawal{" +
                        "requsted_wallet_amount = '" + requestedWalletAmount + '\'' +
                        ",is_payment_mode_cash = '" + isPaymentModeCash + '\'' +
                        ",server_token = '" + serverToken + '\'' +
                        ",type = '" + type + '\'' +
                        ",description_for_request_wallet_amount = '" +
                        descriptionForRequestWalletAmount + '\'' +
                        ",transaction_details = '" + bankDetail + '\'' +
                        "}";
    }
}