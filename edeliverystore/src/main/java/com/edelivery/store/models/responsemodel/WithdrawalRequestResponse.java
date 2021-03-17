package com.edelivery.store.models.responsemodel;

import com.edelivery.store.models.datamodel.WalletRequest;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WithdrawalRequestResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private int message;

    @SerializedName("wallet_request")
    private WalletRequest walletRequest;
    @SerializedName("error_code")
    @Expose
    private int errorCode;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getMessage() {
        return message;
    }

    public void setMessage(int message) {
        this.message = message;
    }

    public WalletRequest getWalletRequest() {
        return walletRequest;
    }

    public void setWalletRequest(WalletRequest walletRequest) {
        this.walletRequest = walletRequest;
    }

    @Override
    public String toString() {
        return
                "WithdrawalRequestResponse{" +
                        "success = '" + success + '\'' +
                        ",message = '" + message + '\'' +
                        ",wallet_request = '" + walletRequest + '\'' +
                        "}";
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}