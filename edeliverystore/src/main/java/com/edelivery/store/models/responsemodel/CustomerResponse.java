package com.edelivery.store.models.responsemodel;

import com.edelivery.store.models.datamodel.Customer;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class CustomerResponse {

    @SerializedName("minimum_phone_number_length")
    private int minimumPhoneNumberLength;

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private int message;

    @SerializedName("maximum_phone_number_length")
    private int maximumPhoneNumberLength;

    @SerializedName("user")
    private Customer customer;
    @SerializedName("error_code")
    @Expose
    private int errorCode;

    public int getMinimumPhoneNumberLength() {
        return minimumPhoneNumberLength;
    }

    public void setMinimumPhoneNumberLength(int minimumPhoneNumberLength) {
        this.minimumPhoneNumberLength = minimumPhoneNumberLength;
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

    public int getMaximumPhoneNumberLength() {
        return maximumPhoneNumberLength;
    }

    public void setMaximumPhoneNumberLength(int maximumPhoneNumberLength) {
        this.maximumPhoneNumberLength = maximumPhoneNumberLength;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
        return
                "CustomerResponse{" +
                        "minimum_phone_number_length = '" + minimumPhoneNumberLength + '\'' +
                        ",success = '" + success + '\'' +
                        ",message = '" + message + '\'' +
                        ",maximum_phone_number_length = '" + maximumPhoneNumberLength + '\'' +
                        ",customer = '" + customer + '\'' +
                        "}";
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}