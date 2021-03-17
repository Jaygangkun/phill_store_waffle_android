package com.edelivery.store.models.responsemodel;

import com.edelivery.store.models.datamodel.Category;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by elluminati on 11-Jan-18.
 */

public class CategoriesResponse {
    @SerializedName("error_code")
    @Expose
    private int errorCode;
    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("message")
    @Expose
    private int message;

    @SerializedName("deliveries")
    @Expose
    private ArrayList<Category> categories;

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

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }
}
