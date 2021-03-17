package com.edelivery.store.models.responsemodel;

import com.edelivery.store.models.datamodel.WalletHistory;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WalletHistoryResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("wallet_history")
    private List<WalletHistory> walletHistory;

    @SerializedName("message")
    private int message;
    @SerializedName("error_code")
    @Expose
    private int errorCode;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<WalletHistory> getWalletHistory() {
        return walletHistory;
    }

    public void setWalletHistory(List<WalletHistory> walletHistory) {
        this.walletHistory = walletHistory;
    }

    public int getMessage() {
        return message;
    }

    public void setMessage(int message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return
                "WalletHistoryResponse{" +
                        "success = '" + success + '\'' +
                        ",wallet_history = '" + walletHistory + '\'' +
                        ",message = '" + message + '\'' +
                        "}";
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}