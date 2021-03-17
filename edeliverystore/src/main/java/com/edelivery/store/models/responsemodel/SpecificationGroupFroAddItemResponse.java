package com.edelivery.store.models.responsemodel;

import com.edelivery.store.models.datamodel.ItemSpecification;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class SpecificationGroupFroAddItemResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("specification_group")
    private List<ItemSpecification> specificationGroup;

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

    public List<ItemSpecification> getSpecificationGroup() {
        return specificationGroup;
    }

    public void setSpecificationGroup(List<ItemSpecification> specificationGroup) {
        this.specificationGroup = specificationGroup;
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
                "SpecificationGroupResponse{" +
                        "success = '" + success + '\'' +
                        ",specification_group = '" + specificationGroup + '\'' +
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