package com.edelivery.store.models.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Card {

	@SerializedName("unique_id")
	@Expose
	private int uniqueId;

	@SerializedName("updated_at")
	@Expose
	private String updatedAt;

	@SerializedName("user_id")
	@Expose
	private String userId;

	@SerializedName("last_four")
	@Expose
	private String lastFour;

	@SerializedName("payment_id")
	@Expose
	private String paymentId;

	@SerializedName("payment_token")
	@Expose
	private String paymentToken;

	@SerializedName("card_expiry_date")
	@Expose
	private String cardExpiryDate;

	@SerializedName("card_holder_name")
	@Expose
	private String cardHolderName;

	@SerializedName("created_at")
	@Expose
	private String createdAt;

	@SerializedName("_id")
	@Expose
	private String id;

	@SerializedName("card_type")
	@Expose
	private String cardType;

	@SerializedName("customer_id")
	@Expose
	private String customerId;

	@SerializedName("is_default")
	@Expose
	private boolean isDefault;

	public void setUniqueId(int uniqueId){
		this.uniqueId = uniqueId;
	}

	public int getUniqueId(){
		return uniqueId;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return userId;
	}

	public void setLastFour(String lastFour){
		this.lastFour = lastFour;
	}

	public String getLastFour(){
		return lastFour;
	}

	public void setPaymentId(String paymentId){
		this.paymentId = paymentId;
	}

	public String getPaymentId(){
		return paymentId;
	}

	public void setPaymentToken(String paymentToken){
		this.paymentToken = paymentToken;
	}

	public String getPaymentToken(){
		return paymentToken;
	}

	public String getCardExpiryDate() {
		return cardExpiryDate;
	}

	public void setCardExpiryDate(String cardExpiryDate) {
		this.cardExpiryDate = cardExpiryDate;
	}

	public String getCardHolderName() {
		return cardHolderName;
	}

	public void setCardHolderName(String cardHolderName) {
		this.cardHolderName = cardHolderName;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setCardType(String cardType){
		this.cardType = cardType;
	}

	public String getCardType(){
		return cardType;
	}

	public void setCustomerId(String customerId){
		this.customerId = customerId;
	}

	public String getCustomerId(){
		return customerId;
	}

	public void setIsDefault(boolean isDefault){
		this.isDefault = isDefault;
	}

	public boolean isIsDefault(){
		return isDefault;
	}


}