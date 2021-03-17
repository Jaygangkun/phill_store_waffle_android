package com.edelivery.store.models.responsemodel;

import com.edelivery.store.models.datamodel.SpecificationGroup;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class SpecificationGroupResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("specification_group")
    private List<SpecificationGroup> specificationGroup;

    @SerializedName("message")
    private int message;

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSpecificationGroup(List<SpecificationGroup> specificationGroup) {
        this.specificationGroup = specificationGroup;
    }

    public List<SpecificationGroup> getSpecificationGroup() {
        return specificationGroup;
    }

    public void setMessage(int message) {
        this.message = message;
    }

    public int getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return
                "SpecificationGroupResponse{" +
                        "success = '" + success + '\'' +
                        ",specification_group = '" + specificationGroup + '\'' +
                        ",message = '" + message + '\'' +
                        "}";
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