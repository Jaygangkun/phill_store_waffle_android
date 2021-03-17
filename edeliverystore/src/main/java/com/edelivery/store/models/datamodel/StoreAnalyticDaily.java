package com.edelivery.store.models.datamodel;

import com.google.gson.annotations.SerializedName;

public class StoreAnalyticDaily {


    @SerializedName("total_orders")
    private int totalOrders;

    @SerializedName("total_items")
    private int totalItems;

    @SerializedName("date_in_server_time")
    private String dateInServerTime;

    @SerializedName("date")
    private String date;

    @SerializedName("rejection_ratio")
    private double rejectionRatio;

    @SerializedName("unique_id")
    private int uniqueId;

    @SerializedName("completed_ratio")
    private double completedRatio;

    @SerializedName("rejected")
    private int rejected;

    @SerializedName("cancellation_ratio")
    private double cancellationRatio;

    @SerializedName("total_active_job_time")
    private long totalActiveJobTime;

    @SerializedName("accepted")
    private int accepted;

    @SerializedName("received")
    private int received;

    @SerializedName("completed")
    private int completed;

    @SerializedName("not_answered")
    private int notAnswered;

    @SerializedName("total_online_time")
    private long totalOnlineTime;

    @SerializedName("working_hours")
    private int workingHours;

    @SerializedName("provider_id")
    private String providerId;

    @SerializedName("cancelled")
    private int cancelled;

    @SerializedName("_id")
    private String id;

    @SerializedName("tag")
    private String tag;

    @SerializedName("acception_ratio")
    private double acceptionRatio;

    public void setDateInServerTime(String dateInServerTime) {
        this.dateInServerTime = dateInServerTime;
    }

    public String getDateInServerTime() {
        return dateInServerTime;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setRejectionRatio(double rejectionRatio) {
        this.rejectionRatio = rejectionRatio;
    }

    public double getRejectionRatio() {
        return rejectionRatio;
    }

    public void setUniqueId(int uniqueId) {
        this.uniqueId = uniqueId;
    }

    public int getUniqueId() {
        return uniqueId;
    }

    public void setCompletedRatio(double completedRatio) {
        this.completedRatio = completedRatio;
    }

    public double getCompletedRatio() {
        return completedRatio;
    }

    public void setRejected(int rejected) {
        this.rejected = rejected;
    }

    public int getRejected() {
        return rejected;
    }

    public void setCancellationRatio(double cancellationRatio) {
        this.cancellationRatio = cancellationRatio;
    }

    public double getCancellationRatio() {
        return cancellationRatio;
    }

    public void setTotalActiveJobTime(long totalActiveJobTime) {
        this.totalActiveJobTime = totalActiveJobTime;
    }

    public long getTotalActiveJobTime() {
        return totalActiveJobTime;
    }

    public void setAccepted(int accepted) {
        this.accepted = accepted;
    }

    public int getAccepted() {
        return accepted;
    }

    public void setReceived(int received) {
        this.received = received;
    }

    public int getReceived() {
        return received;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }

    public int getCompleted() {
        return completed;
    }

    public void setNotAnswered(int notAnswered) {
        this.notAnswered = notAnswered;
    }

    public int getNotAnswered() {
        return notAnswered;
    }

    public void setTotalOnlineTime(long totalOnlineTime) {
        this.totalOnlineTime = totalOnlineTime;
    }

    public long getTotalOnlineTime() {
        return totalOnlineTime;
    }

    public void setWorkingHours(int workingHours) {
        this.workingHours = workingHours;
    }

    public int getWorkingHours() {
        return workingHours;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setCancelled(int cancelled) {
        this.cancelled = cancelled;
    }

    public int getCancelled() {
        return cancelled;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public void setAcceptionRatio(double acceptionRatio) {
        this.acceptionRatio = acceptionRatio;
    }

    public double getAcceptionRatio() {
        return acceptionRatio;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(int totalOrders) {
        this.totalOrders = totalOrders;
    }
}