package com.edelivery.store.models.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class SetSpecificationGroup {

    @SerializedName("store_id")
    @Expose
    private String storeId;

    @SerializedName("product_id")
    @Expose
    private String productId;

    @SerializedName("server_token")
    @Expose
    private String serverToken;

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStoreId() {
        return storeId;
    }


    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }

    public void setServerToken(String serverToken) {
        this.serverToken = serverToken;
    }

    public String getServerToken() {
        return serverToken;
    }


    @SerializedName("specification_group_name")
    @Expose
    private List<List<String>> specificationGroup;


    public void setSpecificationGroup(List<List<String>> specificationGroup) {
        this.specificationGroup = specificationGroup;
    }

    @SerializedName("name")
    @Expose
    private List<String> name;

    @SerializedName("sequence_number")
    @Expose
    private long sequenceNumber;

    @SerializedName("sp_id")
    @Expose
    private String spId;

    public List<String> getName() {
        return name;
    }

    public void setName(List<String> name) {
        this.name = name;
    }

    public String getSpId() {
        return spId;
    }

    public void setSpId(String spId) {
        this.spId = spId;
    }

    public long getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(long sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
}