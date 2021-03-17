package com.edelivery.store.models.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class StoreReview {

    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @SerializedName("order_unique_id")
    @Expose
    private String orderUniqueId;
    private boolean isLike;
    private boolean isDislike;
    @SerializedName("user_rating_to_store")
    private double userRatingToStore;
    @SerializedName("user_review_to_store")
    private String userReviewToStore;
    @SerializedName("id_of_users_dislike_store_comment")
    private List<String> idOfUsersDislikeStoreComment = new ArrayList<>();
    @SerializedName("user_detail")
    private UserDetail userDetail;
    @SerializedName("_id")
    private String id;
    @SerializedName("id_of_users_like_store_comment")
    private List<String> idOfUsersLikeStoreComment = new ArrayList<>();

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getOrderUniqueId() {
        return orderUniqueId;
    }

    public void setOrderUniqueId(String orderUniqueId) {
        this.orderUniqueId = orderUniqueId;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    public boolean isDislike() {
        return isDislike;
    }

    public void setDislike(boolean dislike) {
        isDislike = dislike;
    }

    public double getUserRatingToStore() {
        return userRatingToStore;
    }

    public void setUserRatingToStore(double userRatingToStore) {
        this.userRatingToStore = userRatingToStore;
    }

    public String getUserReviewToStore() {
        return userReviewToStore;
    }

    public void setUserReviewToStore(String userReviewToStore) {
        this.userReviewToStore = userReviewToStore;
    }

    public List<String> getIdOfUsersDislikeStoreComment() {
        return idOfUsersDislikeStoreComment;
    }

    public void setIdOfUsersDislikeStoreComment(List<String> idOfUsersDislikeStoreComment) {
        this.idOfUsersDislikeStoreComment = idOfUsersDislikeStoreComment;
    }

    public UserDetail getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(UserDetail userDetail) {
        this.userDetail = userDetail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getIdOfUsersLikeStoreComment() {
        return idOfUsersLikeStoreComment;
    }

    public void setIdOfUsersLikeStoreComment(List<String> idOfUsersLikeStoreComment) {
        this.idOfUsersLikeStoreComment = idOfUsersLikeStoreComment;
    }

    @Override
    public String toString() {
        return
                "StoreReview{" +
                        "user_rating_to_store = '" + userRatingToStore + '\'' +
                        ",user_review_to_store = '" + userReviewToStore + '\'' +
                        ",id_of_users_dislike_store_comment = '" + idOfUsersDislikeStoreComment +
                        '\'' +
                        ",user_detail = '" + userDetail + '\'' +
                        ",_id = '" + id + '\'' +
                        ",id_of_users_like_store_comment = '" + idOfUsersLikeStoreComment + '\'' +
                        "}";
    }
}