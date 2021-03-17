package com.edelivery.store.models.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DocumentResponse {

    @SerializedName("success")
    private boolean success;


    @SerializedName("is_document_uploaded")
    private boolean isDocumentUploaded;

    @SerializedName("message")
    private int message;

    @SerializedName("expired_date")
    private String expiredDate;

    @SerializedName("error_code")
    @Expose
    private int errorCode;

    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("unique_code")
    private String uniqueCode;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isIsDocumentUploaded() {
        return isDocumentUploaded;
    }

    public void setIsDocumentUploaded(boolean isDocumentUploaded) {
        this.isDocumentUploaded = isDocumentUploaded;
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

    public boolean isDocumentUploaded() {
        return isDocumentUploaded;
    }

    public void setDocumentUploaded(boolean documentUploaded) {
        isDocumentUploaded = documentUploaded;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public String getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(String expiredDate) {
        this.expiredDate = expiredDate;
    }
}