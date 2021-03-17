package com.edelivery.store.models.datamodel;

import com.google.gson.annotations.SerializedName;

public class AutocompleteAddress1 {

    @SerializedName("address")
    private String address;
    @SerializedName("id")
    private String id;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
