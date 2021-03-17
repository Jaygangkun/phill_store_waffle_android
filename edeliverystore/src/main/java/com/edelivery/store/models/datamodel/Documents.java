package com.edelivery.store.models.datamodel;

import com.google.gson.annotations.SerializedName;

public class Documents {


    @SerializedName("document_details")
    private DocumentDetails documentDetails;


    @SerializedName("image_url")
    private String imageUrl;


    @SerializedName("document_id")
    private String documentId;

    @SerializedName("expired_date")
    private String expiredDate;


    @SerializedName("user_id")
    private String userId;


    @SerializedName("_id")
    private String id;


    @SerializedName("unique_code")
    private String uniqueCode;


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(String expiredDate) {
        this.expiredDate = expiredDate;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public DocumentDetails getDocumentDetails() {
        return documentDetails;
    }

    public void setDocumentDetails(DocumentDetails documentDetails) {
        this.documentDetails = documentDetails;
    }
}