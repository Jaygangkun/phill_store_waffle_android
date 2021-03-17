package com.edelivery.store.models.responsemodel;

import com.edelivery.store.models.datamodel.BankDetail;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BankDetailResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("bank_detail")
    private List<BankDetail> bankDetail;

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

    public List<BankDetail> getBankDetail() {
        return bankDetail;
    }

    public void setBankDetail(List<BankDetail> bankDetail) {
        this.bankDetail = bankDetail;
    }

    public int getMessage() {
        return message;
    }

    public void setMessage(int message) {
        this.message = message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

}