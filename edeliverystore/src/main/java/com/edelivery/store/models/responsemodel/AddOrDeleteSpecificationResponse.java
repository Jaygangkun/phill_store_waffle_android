package com.edelivery.store.models.responsemodel;

import com.edelivery.store.models.datamodel.Specifications;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by elluminati on 06-Jul-17.
 */

public class AddOrDeleteSpecificationResponse {

    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("message")
    @Expose
    private int message;

    @SerializedName("error_code")
    @Expose
    private int errorCode;

    @SerializedName("specifications")
    private List<Specifications> specifications;

    public List<Specifications> getSpecifications() {
        return specifications;
    }

    public void setSpecifications(List<Specifications> specifications) {
        this.specifications = specifications;
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
