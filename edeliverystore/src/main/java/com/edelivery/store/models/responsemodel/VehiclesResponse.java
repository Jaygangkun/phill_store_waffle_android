package com.edelivery.store.models.responsemodel;

import com.edelivery.store.models.datamodel.VehicleDetail;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VehiclesResponse {

    @SerializedName("error_code")
    @Expose
    private int errorCode;
    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("message")
    @Expose
    private int message;
    @SerializedName("admin_vehicles")
    @Expose
    private List<VehicleDetail> adminVehicles;
    @SerializedName("vehicles")
    @Expose
    private List<VehicleDetail> vehicles;

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

    public List<VehicleDetail> getAdminVehicles() {
        return adminVehicles;
    }

    public void setAdminVehicles(List<VehicleDetail> adminVehicles) {
        this.adminVehicles = adminVehicles;
    }

    public List<VehicleDetail> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<VehicleDetail> vehicles) {
        this.vehicles = vehicles;
    }
}
