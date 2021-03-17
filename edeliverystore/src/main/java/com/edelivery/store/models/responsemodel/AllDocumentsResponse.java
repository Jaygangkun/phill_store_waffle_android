package com.edelivery.store.models.responsemodel;

import com.edelivery.store.models.datamodel.Documents;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllDocumentsResponse {

    @SerializedName("documents")
    private List<Documents> documents;

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private int message;

    public void setDocuments(List<Documents> documents) {
        this.documents = documents;
    }

    public List<Documents> getDocuments() {
        return documents;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setMessage(int message) {
        this.message = message;
    }

    public int getMessage() {
        return message;
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

    @SerializedName("is_document_uploaded")
    private boolean isDocumentUploaded;

    public boolean isDocumentUploaded() {
        return isDocumentUploaded;
    }

    public void setDocumentUploaded(boolean documentUploaded) {
        isDocumentUploaded = documentUploaded;
    }
}