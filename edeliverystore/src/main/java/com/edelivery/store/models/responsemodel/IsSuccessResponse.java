package com.edelivery.store.models.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by elluminati on 11-Jan-18.
 */

public class IsSuccessResponse {

    @SerializedName("stripe_error")
    @Expose
    private String stripeError = "";
    @SerializedName("error_code")
    @Expose
    private int errorCode;
    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("message")
    @Expose
    private int message;

    public String getStripeError() {
        return stripeError;
    }

    public void setStripeError(String stripeError) {
        this.stripeError = stripeError;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
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
}
