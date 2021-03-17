package com.edelivery.store.models.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class Settings {

	@SerializedName("is_upload_user_documents")
	@Expose
	private boolean isUploadUserDocuments;

	@SerializedName("is_provider_profile_picture_required")
	@Expose
	private boolean isProviderProfilePictureRequired;

	@SerializedName("is_upload_store_documents")
	@Expose
	private boolean isUploadStoreDocuments;

	@SerializedName("is_sms_notification")
	@Expose
	private boolean isSmsNotification;

	@SerializedName("is_store_profile_picture_required")
	@Expose
	private boolean isStoreProfilePictureRequired;

	@SerializedName("is_referral_to_store")
	@Expose
	private boolean isReferralToStore;

	@SerializedName("is_hide_optional_field_in_provider_register")
	@Expose
	private boolean isHideOptionalFieldInProviderRegister;

	@SerializedName("is_store_mail_verification")
	@Expose
	private boolean isStoreMailVerification;

	@SerializedName("is_provider_mail_verification")
	@Expose
	private boolean isProviderMailVerification;

	@SerializedName("is_hide_optional_field_in_store_register")
	@Expose
	private boolean isHideOptionalFieldInStoreRegister;

	@SerializedName("is_user_mail_verification")
	@Expose
	private boolean isUserMailVerification;

	@SerializedName("is_referral_to_provider")
	@Expose
	private boolean isReferralToProvider;

	@SerializedName("is_referral_to_user")
	@Expose
	private boolean isReferralToUser;

	@SerializedName("is_hide_optional_field_in_user_register")
	@Expose
	private boolean isHideOptionalFieldInUserRegister;

	@SerializedName("is_mail_notification")
	@Expose
	private boolean isMailNotification;

	@SerializedName("is_provider_sms_verification")
	@Expose
	private boolean isProviderSmsVerification;

	@SerializedName("is_user_profile_picture_required")
	@Expose
	private boolean isUserProfilePictureRequired;

	@SerializedName("is_upload_provider_documents")
	@Expose
	private boolean isUploadProviderDocuments;

	@SerializedName("is_user_sms_verification")
	@Expose
	private boolean isUserSmsVerification;

	@SerializedName("_id")
	@Expose
	private String id;

	@SerializedName("is_store_sms_verification")
	@Expose
	private boolean isStoreSmsVerification;

	public void setUploadUserDocuments(boolean isUploadUserDocuments){
		this.isUploadUserDocuments = isUploadUserDocuments;
	}

	public boolean isUploadUserDocuments(){
		return isUploadUserDocuments;
	}

	public void setProviderProfilePictureRequired(boolean isProviderProfilePictureRequired){
		this.isProviderProfilePictureRequired = isProviderProfilePictureRequired;
	}

	public boolean isProviderProfilePictureRequired(){
		return isProviderProfilePictureRequired;
	}

	public void setUploadStoreDocuments(boolean isUploadStoreDocuments){
		this.isUploadStoreDocuments = isUploadStoreDocuments;
	}

	public boolean isUploadStoreDocuments(){
		return isUploadStoreDocuments;
	}

	public void setSmsNotification(boolean isSmsNotification){
		this.isSmsNotification = isSmsNotification;
	}

	public boolean isSmsNotification(){
		return isSmsNotification;
	}

	public void setStoreProfilePictureRequired(boolean isStoreProfilePictureRequired){
		this.isStoreProfilePictureRequired = isStoreProfilePictureRequired;
	}

	public boolean isStoreProfilePictureRequired(){
		return isStoreProfilePictureRequired;
	}

	public void setReferralToStore(boolean isReferralToStore){
		this.isReferralToStore = isReferralToStore;
	}

	public boolean isReferralToStore(){
		return isReferralToStore;
	}

	public void setHideOptionalFieldInProviderRegister(boolean isHideOptionalFieldInProviderRegister){
		this.isHideOptionalFieldInProviderRegister = isHideOptionalFieldInProviderRegister;
	}

	public boolean isHideOptionalFieldInProviderRegister(){
		return isHideOptionalFieldInProviderRegister;
	}

	public void setStoreMailVerification(boolean isStoreMailVerification){
		this.isStoreMailVerification = isStoreMailVerification;
	}

	public boolean isStoreMailVerification(){
		return isStoreMailVerification;
	}

	public void setProviderMailVerification(boolean isProviderMailVerification){
		this.isProviderMailVerification = isProviderMailVerification;
	}

	public boolean isProviderMailVerification(){
		return isProviderMailVerification;
	}

	public void setHideOptionalFieldInStoreRegister(boolean isHideOptionalFieldInStoreRegister){
		this.isHideOptionalFieldInStoreRegister = isHideOptionalFieldInStoreRegister;
	}

	public boolean isHideOptionalFieldInStoreRegister(){
		return isHideOptionalFieldInStoreRegister;
	}

	public void setUserMailVerification(boolean isUserMailVerification){
		this.isUserMailVerification = isUserMailVerification;
	}

	public boolean isUserMailVerification(){
		return isUserMailVerification;
	}

	public void setReferralToProvider(boolean isReferralToProvider){
		this.isReferralToProvider = isReferralToProvider;
	}

	public boolean isReferralToProvider(){
		return isReferralToProvider;
	}

	public void setReferralToUser(boolean isReferralToUser){
		this.isReferralToUser = isReferralToUser;
	}

	public boolean isReferralToUser(){
		return isReferralToUser;
	}

	public void setHideOptionalFieldInUserRegister(boolean isHideOptionalFieldInUserRegister){
		this.isHideOptionalFieldInUserRegister = isHideOptionalFieldInUserRegister;
	}

	public boolean isHideOptionalFieldInUserRegister(){
		return isHideOptionalFieldInUserRegister;
	}

	public void setMailNotification(boolean isMailNotification){
		this.isMailNotification = isMailNotification;
	}

	public boolean isMailNotification(){
		return isMailNotification;
	}

	public void setProviderSmsVerification(boolean isProviderSmsVerification){
		this.isProviderSmsVerification = isProviderSmsVerification;
	}

	public boolean isProviderSmsVerification(){
		return isProviderSmsVerification;
	}

	public void setUserProfilePictureRequired(boolean isUserProfilePictureRequired){
		this.isUserProfilePictureRequired = isUserProfilePictureRequired;
	}

	public boolean isUserProfilePictureRequired(){
		return isUserProfilePictureRequired;
	}

	public void setUploadProviderDocuments(boolean isUploadProviderDocuments){
		this.isUploadProviderDocuments = isUploadProviderDocuments;
	}

	public boolean isUploadProviderDocuments(){
		return isUploadProviderDocuments;
	}

	public void setUserSmsVerification(boolean isUserSmsVerification){
		this.isUserSmsVerification = isUserSmsVerification;
	}

	public boolean isUserSmsVerification(){
		return isUserSmsVerification;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setStoreSmsVerification(boolean isStoreSmsVerification){
		this.isStoreSmsVerification = isStoreSmsVerification;
	}

	public boolean isStoreSmsVerification(){
		return isStoreSmsVerification;
	}
}