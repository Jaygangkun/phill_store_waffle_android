package com.edelivery.store.models.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class City {


    @SerializedName("city_name")
    @Expose
    private String cityName = "";

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("city_nick_name")
    @Expose
    private String cityNickName = "";

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCityNickName() {
        return cityNickName;
    }

    public void setCityNickName(String cityNickName) {
        this.cityNickName = cityNickName;
    }
}