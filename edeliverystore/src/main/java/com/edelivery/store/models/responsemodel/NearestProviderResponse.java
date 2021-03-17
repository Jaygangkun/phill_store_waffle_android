package com.edelivery.store.models.responsemodel;

import com.edelivery.store.models.datamodel.ProviderDetail;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NearestProviderResponse {

    @SerializedName("providers")
    private List<ProviderDetail> providers;

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private int message;
    @SerializedName("error_code")
    @Expose
    private int errorCode;

    public List<ProviderDetail> getProviders() {
        return providers;
    }

    public void setProviders(List<ProviderDetail> providers) {
        this.providers = providers;
    }

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

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}