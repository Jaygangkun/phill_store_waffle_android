package com.edelivery.store.models.datamodel;

import com.edelivery.store.models.singleton.Language;
import com.edelivery.store.utils.Utilities;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class Category implements Serializable {

    @SerializedName("is_business")
    @Expose
    private boolean isBusiness;

    @SerializedName("icon_url")
    @Expose
    private String iconUrl;

    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    @SerializedName("delivery_type")
    @Expose
    private int deliveryType;

    @SerializedName("delivery_name")
    @Expose
    private List<String> deliveryName;

    @SerializedName("image_url")
    @Expose
    private String imageUrl;

    @SerializedName("__v")
    @Expose
    private int V;

    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @SerializedName("_id")
    @Expose
    private String id;

    public void setIsBusiness(boolean isBusiness) {
        this.isBusiness = isBusiness;
    }

    public boolean isIsBusiness() {
        return isBusiness;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public int getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(int deliveryType) {
        this.deliveryType = deliveryType;
    }

    public void setDeliveryName(List<String> deliveryName) {
        this.deliveryName = deliveryName;
    }

    public String getDeliveryName() {
        return Utilities.getDetailStringFromList((ArrayList<String>)deliveryName, Language.getInstance().
                getAdminLanguageIndex());
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setV(int V) {
        this.V = V;
    }

    public int getV() {
        return V;
    }


    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}