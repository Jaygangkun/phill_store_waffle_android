package com.edelivery.store.models.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WalletResponse {

    @SerializedName("wallet")
    @Expose
    private double wallet;

    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("message")
    @Expose
    private int message;

    @SerializedName("wallet_currency_code")
    @Expose
    private String walletCurrencyCode;

    public void setWallet(double wallet) {
        this.wallet = wallet;
    }

    public double getWallet() {
        return wallet;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setMessage(int message) {
        this.message = message;
    }

    public int getMessage() {
        return message;
    }

    public void setWalletCurrencyCode(String walletCurrencyCode) {
        this.walletCurrencyCode = walletCurrencyCode;
    }

    public String getWalletCurrencyCode() {
        return walletCurrencyCode;
    }

    @SerializedName("error_code")
    @Expose
    private int errorCode;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}