package com.edelivery.store.models.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class SetSpecificationList {


    @SerializedName("specification_group_id")
    @Expose
    public String specificationGroupId;
    @SerializedName("specification_id")
    @Expose
    private List<String> specificationId;
    @SerializedName("store_id")
    @Expose
    private String storeId;

    @SerializedName("specification_name")
    @Expose
    private List<Specifications.NameAndPrice> specificationName;

    @SerializedName("product_id")
    @Expose
    private String productId;

    @SerializedName("server_token")
    @Expose
    private String serverToken;

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public List<Specifications.NameAndPrice> getSpecificationName() {
        return specificationName;
    }

    public void setSpecificationName(List<Specifications.NameAndPrice> specificationName) {
        this.specificationName = specificationName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getServerToken() {
        return serverToken;
    }

    public void setServerToken(String serverToken) {
        this.serverToken = serverToken;
    }

    @Override
    public String toString() {
        return "SetSpecificationList{" +
                "specificationGroupId='" + specificationGroupId + '\'' +
                ", storeId='" + storeId + '\'' +
                ", specificationName=" + specificationName +
                ", productId='" + productId + '\'' +
                ", serverToken='" + serverToken + '\'' +
                '}';
    }

    public String getSpecificationGroupId() {
        return specificationGroupId;
    }

    public void setSpecificationGroupId(String specificationGroupId) {
        this.specificationGroupId = specificationGroupId;
    }

    public List<String> getSpecificationId() {
        return specificationId;
    }

    public void setSpecificationId(List<String> specificationId) {
        this.specificationId = specificationId;
    }
}