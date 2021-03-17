package com.edelivery.store.models.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class AppSetting {

    @SerializedName("admin_contact_phone_number")
    private String adminContactPhoneNumber;
    @SerializedName("terms_and_condition_url")
    private String termsAndConditionUrl;
    @SerializedName("privacy_policy_url")
    private String privacyPolicyUrl;
    @SerializedName("is_login_by_social")
    @Expose
    private boolean isLoginBySocial;
    @SerializedName("is_document_mandatory")
    @Expose
    private boolean isDocumentMandatory;
    @SerializedName("is_verify_email")
    @Expose
    private boolean isVerifyEmail;
    @SerializedName("is_login_by_email")
    @Expose
    private boolean isLoginByEmail;
    @SerializedName("is_show_optional_field")
    @Expose
    private boolean isShowOptionalField;
    @SerializedName("is_login_by_phone")
    @Expose
    private boolean isLoginByPhone;
    @SerializedName("version_code")
    @Expose
    private String versionCode;
    @SerializedName("message")
    @Expose
    private int message;
    @SerializedName("is_profile_picture_required")
    @Expose
    private boolean isProfilePictureRequired;
    @SerializedName("is_force_update")
    @Expose
    private boolean isForceUpdate;
    @SerializedName("is_use_referral")
    @Expose
    private boolean isUseReferral;
    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("admin_contact_email")
    @Expose
    private String adminContactEmail;
    @SerializedName("is_open_update_dialog")
    @Expose
    private boolean isOpenUpdateDialog;
    @SerializedName("google_key")
    @Expose
    private String googleKey;
    @SerializedName("is_verify_phone")
    @Expose
    private boolean isVerifyPhone;

    public String getAdminContactPhoneNumber() {
        return adminContactPhoneNumber;
    }

    public void setAdminContactPhoneNumber(String adminContactPhoneNumber) {
        this.adminContactPhoneNumber = adminContactPhoneNumber;
    }

    public String getTermsAndConditionUrl() {
        return termsAndConditionUrl;
    }

    public void setTermsAndConditionUrl(String termsAndConditionUrl) {
        this.termsAndConditionUrl = termsAndConditionUrl;
    }

    public String getPrivacyPolicyUrl() {
        return privacyPolicyUrl;
    }

    public void setPrivacyPolicyUrl(String privacyPolicyUrl) {
        this.privacyPolicyUrl = privacyPolicyUrl;
    }

    public boolean isIsDocumentMandatory() {
        return isDocumentMandatory;
    }

    public void setIsDocumentMandatory(boolean isDocumentMandatory) {
        this.isDocumentMandatory = isDocumentMandatory;
    }

    public boolean isIsVerifyEmail() {
        return isVerifyEmail;
    }

    public void setIsVerifyEmail(boolean isVerifyEmail) {
        this.isVerifyEmail = isVerifyEmail;
    }

    public boolean isIsLoginByEmail() {
        return isLoginByEmail;
    }

    public void setIsLoginByEmail(boolean isLoginByEmail) {
        this.isLoginByEmail = isLoginByEmail;
    }

    public boolean isIsHideOptionalField() {
        return isShowOptionalField;
    }

    public void setIsHideOptionalField(boolean isHideOptionalField) {
        this.isShowOptionalField = isHideOptionalField;
    }

    public boolean isIsLoginByPhone() {
        return isLoginByPhone;
    }

    public void setIsLoginByPhone(boolean isLoginByPhone) {
        this.isLoginByPhone = isLoginByPhone;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public int getMessage() {
        return message;
    }

    public void setMessage(int message) {
        this.message = message;
    }

    public boolean isIsProfilePictureRequired() {
        return isProfilePictureRequired;
    }

    public void setIsProfilePictureRequired(boolean isProfilePictureRequired) {
        this.isProfilePictureRequired = isProfilePictureRequired;
    }

    public boolean isIsForceUpdate() {
        return isForceUpdate;
    }

    public void setIsForceUpdate(boolean isForceUpdate) {
        this.isForceUpdate = isForceUpdate;
    }

    public boolean isIsUseReferral() {
        return isUseReferral;
    }

    public void setIsUseReferral(boolean isUseReferral) {
        this.isUseReferral = isUseReferral;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getAdminContactEmail() {
        return adminContactEmail;
    }

    public void setAdminContactEmail(String adminContactEmail) {
        this.adminContactEmail = adminContactEmail;
    }

    public boolean isIsOpenUpdateDialog() {
        return isOpenUpdateDialog;
    }

    public void setIsOpenUpdateDialog(boolean isOpenUpdateDialog) {
        this.isOpenUpdateDialog = isOpenUpdateDialog;
    }

    public String getGoogleKey() {
        return googleKey;
    }

    public void setGoogleKey(String googleKey) {
        this.googleKey = googleKey;
    }

    public boolean isIsVerifyPhone() {
        return isVerifyPhone;
    }

    public void setIsVerifyPhone(boolean isVerifyPhone) {
        this.isVerifyPhone = isVerifyPhone;
    }

    @Override
    public String toString() {
        return
                "AppSetting{" +
                        "is_document_mandatory = '" + isDocumentMandatory + '\'' +
                        ",is_verify_email = '" + isVerifyEmail + '\'' +
                        ",is_login_by_email = '" + isLoginByEmail + '\'' +
                        ",is_hide_optional_field = '" + isShowOptionalField + '\'' +
                        ",is_login_by_phone = '" + isLoginByPhone + '\'' +
                        ",version_code = '" + versionCode + '\'' +
                        ",message = '" + message + '\'' +
                        ",is_profile_picture_required = '" + isProfilePictureRequired + '\'' +
                        ",is_force_update = '" + isForceUpdate + '\'' +
                        ",is_use_referral = '" + isUseReferral + '\'' +
                        ",success = '" + success + '\'' +
                        ",admin_contact_email = '" + adminContactEmail + '\'' +
                        ",is_open_update_dialog = '" + isOpenUpdateDialog + '\'' +
                        ",google_key = '" + googleKey + '\'' +
                        ",is_verify_phone = '" + isVerifyPhone + '\'' +
                        "}";
    }

    public boolean isLoginBySocial() {
        return isLoginBySocial;
    }

    public void setLoginBySocial(boolean loginBySocial) {
        isLoginBySocial = loginBySocial;
    }


    @SerializedName("lang")
    private List<Languages> lang;

    public List<Languages> getLanguage() {
        return lang;
    }
}