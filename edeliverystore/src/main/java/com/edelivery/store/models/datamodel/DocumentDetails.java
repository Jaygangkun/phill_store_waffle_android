package com.edelivery.store.models.datamodel;

import com.google.gson.annotations.SerializedName;

public class DocumentDetails {

    @SerializedName("is_unique_code")
    private boolean isUniqueCode;

    @SerializedName("document_for")
    private int documentFor;

    @SerializedName("document_name")
    private String documentName;

    @SerializedName("unique_id")
    private int uniqueId;

    @SerializedName("updated_at")
    private String updatedAt;


    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("is_mandatory")
    private boolean isMandatory;

    @SerializedName("_id")
    private String id;

    @SerializedName("is_expired_date")
    private boolean isExpiredDate;

    @SerializedName("country_id")
    private String countryId;

    @SerializedName("is_show")
    private boolean isShow;
    @SerializedName("document_details")
    private DocumentDetails documentDetails;

    public boolean isIsUniqueCode() {
        return isUniqueCode;
    }

    public void setIsUniqueCode(boolean isUniqueCode) {
        this.isUniqueCode = isUniqueCode;
    }

    public int getDocumentFor() {
        return documentFor;
    }

    public void setDocumentFor(int documentFor) {
        this.documentFor = documentFor;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public int getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(int uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }


    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isIsMandatory() {
        return isMandatory;
    }

    public void setIsMandatory(boolean isMandatory) {
        this.isMandatory = isMandatory;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isIsExpiredDate() {
        return isExpiredDate;
    }

    public void setIsExpiredDate(boolean isExpiredDate) {
        this.isExpiredDate = isExpiredDate;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public boolean isIsShow() {
        return isShow;
    }

    public void setIsShow(boolean isShow) {
        this.isShow = isShow;
    }

    public DocumentDetails getDocumentDetails() {
        return documentDetails;
    }

    public void setDocumentDetails(DocumentDetails documentDetails) {
        this.documentDetails = documentDetails;
    }

}