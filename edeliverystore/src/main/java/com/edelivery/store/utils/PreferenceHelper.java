package com.edelivery.store.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.edelivery.store.parse.ApiClient;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * A class to store data in preference on 30-01-2017.
 */

public class PreferenceHelper {

    private static final String IS_UPLOAD_STORE_DOCUMENTS = "isUploadStoreDocuments";
    private static final String IS_STORE_PROFILE_PICTURE_REQUIRED = "isStoreProfilePictureRequired";
    private static final String IS_REFERRAL_TO_STORE = "isReferralToStore";
    private static final String IS_HIDE_OPTIONAL_FIELD_IN_REGISTER =
            "isShowOptionalFieldInRegister";
    private static final String IS_FORCE_UPDATE = "isForceUpdate";
    private static final String VERSION_CODE = "androidStoreAppVersionCode";
    private static final String CURRENCY = "currency";
    private static final String IS_USER_REFERRAL = "use_referral";
    private static final String IS_VERIFY_EMAIL = "verify_email";
    private static final String IS_VERIFY_PHONE = "is_verify_phone";
    private static final String IS_LOGIN_BY_PHONE = "login_by_phone";
    private static final String IS_LOGIN_BY_EMAIL = "login_by_email";
    private static final String NAME = "name";
    private static final String PHONE = "phone";
    private static final String IS_APPROVED = "is_approved";
    private static final String IS_DOCUMENT_UPLOADED = "is_document_uploaded";
    private static final String IS_EMAIL_VERIFIED = "is_email_verified";
    private static final String IS_PHONE_NUMBER_VERIFIED = "is_phone_number_verified";
    private static final String ADMIN_CONTACT_EMAIL = "admin_contact_email";
    private static final String COUNTRY_PHONE_CODE = "country_phone_code";
    private static final String EMAIL = "email";
    private static PreferenceHelper preferenceHelper;
    private static SharedPreferences app_prefs;
    private final String MAX_LENGTH = "max_length";
    private final String MIN_LENGTH = "min_length";
    private final String IS_UPLOAD_DOCUMENTS = "is_upload_documents";
    private final String IS_ADMIN_DOCUMENT_MANDATORY = "is_admin_document_mandatory";
    private static final String LANGUAGE = "language";
    private static final String LANGUAGE_INDEX = "language_index";
    private final String IS_REFERRAL_ON = "is_referral_on";
    private final String WALLET_CURRENCY_CODE = "wallet_currency_code";
    private final String WALLET_AMOUNT = "wallet_amount";
    private final String REFERRAL_CODE = "referral_code";
    private final String SOCIAL_ID = "social_id";
    private final String IS_LOGIN_BY_SOCIAL = "is_login_by_social";
    private final String IS_PUSH_SOUND_ON = "is_push_sound_on";
    private final String ADMIN_CONTACT = "admin_contact";
    private final String T_AND_C = "t_and_c";
    private final String POLICY = "policy";
    private final String IS_ASK_ESTIMATED_TIME = "is_ask_estimated_time";
    private final String ADDRESS = "address";
    private final String CITY = "city";
    private final String LAT = "lat";
    private final String LNG = "lng";
    private final String ANDROID_ID = "android_id";
    private final String CART_ID = "cartId";
    private final String IS_PROVIDE_PICKUP_DELIVERY = "is_provide_pickup_delivery";
    private final String STORE_TAX = "store_tax";
    private final String IS_USE_ITEM_TAX = "is_use_item_tax";

    private final String IS_STORE_CAN_CREATE_GROUP = "is_store_can_create_group";
    private final String IS_STORE_CAN_EDIT_ORDER = "is_store_can_edit_order";
    private final String IS_STORE_CAN_ADD_PROVIDER = "is_store_can_add_provider";
    private final String IS_STORE_CAN_COMPLETE_ORDER = "is_store_can_complete_order";

    private final String IS_STORE_CREATE_ORDER = "is_store_create_order";
    private final String IS_STORE_EDIT_MENU = "is_store_edit_menu";
    private final String IS_STORE_EDIT_ITEM = "is_store_edit_item";
    private final String IS_STORE_ADD_PROMOCODE = "is_store_add_promocode";
    private final String
            IS_STORE_CAN_SET_CANCELLATION_CHARGE = "is_store_can_set_cancellation_charge";
    private final String PROFILE_PIC = "profile_pic";

    private final String SUB_STORE_ID = "sub_store_id";
    private final String SAGPAY_Integration_Password = "SAGPAY_Integration_Password";
    private final String SAGPAY_Integration_Key = "SAGPAY_Integration_Key";
    private final String SAGPAY_BASEURL = "Sagpay_baseurl";
    private final String VENDORNAME = "vendorName";

    private PreferenceHelper(Context context) {
        app_prefs = context.getSharedPreferences("Store", Context.MODE_PRIVATE);
    }

    public static PreferenceHelper getPreferenceHelper(Context context) {
        if (preferenceHelper == null) {
            preferenceHelper = new PreferenceHelper(context);
        }

        return preferenceHelper;
    }

    public void logout() {
        if (preferenceHelper.getStoreId() != null && !preferenceHelper.getStoreId().equals("")) {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(preferenceHelper.getStoreId());
        }
        putServerToken(null);
        putStoreId("");
        putSubStoreId("");
    }

    public void putPhone(String phone) {
        SharedPreferences.Editor editor = app_prefs.edit();
        editor.putString(PHONE, phone);
        editor.apply();
    }

    public String getPhone() {
        return app_prefs.getString(PHONE, null);
    }


    public void putIsApproved(boolean value) {
        SharedPreferences.Editor editor = app_prefs.edit();
        editor.putBoolean(IS_APPROVED, value);
        editor.apply();
    }

    public boolean isApproved() {
        return app_prefs.getBoolean(IS_APPROVED, false);
    }

    public void putIsDocumnetUpload(boolean value) {
        SharedPreferences.Editor editor = app_prefs.edit();
        editor.putBoolean(IS_DOCUMENT_UPLOADED, value);
        editor.apply();
    }

    public boolean isDocumnetUpload() {
        return app_prefs.getBoolean(IS_DOCUMENT_UPLOADED, false);
    }

    public void putIsPhoneNumberVerified(boolean value) {
        SharedPreferences.Editor editor = app_prefs.edit();
        editor.putBoolean(IS_PHONE_NUMBER_VERIFIED, value);
        editor.apply();
    }

    public boolean isPhoneNumberVerified() {
        return app_prefs.getBoolean(IS_PHONE_NUMBER_VERIFIED, false);
    }

    public void putIsEmailVerified(boolean value) {
        SharedPreferences.Editor editor = app_prefs.edit();
        editor.putBoolean(IS_EMAIL_VERIFIED, value);
        editor.apply();
    }

    public boolean isEmailVerified() {
        return app_prefs.getBoolean(IS_EMAIL_VERIFIED, false);
    }

    public void putName(String name) {
        SharedPreferences.Editor editor = app_prefs.edit();
        editor.putString(NAME, name);
        editor.apply();
    }

    public String getName() {
        return app_prefs.getString(NAME, null);
    }

    public void putEmail(String email) {
        SharedPreferences.Editor editor = app_prefs.edit();
        editor.putString(EMAIL, email);
        editor.apply();
    }

    public String getEmail() {
        return app_prefs.getString(EMAIL, null);
    }

    public void putCountryPhoneCode(String code) {
        SharedPreferences.Editor editor = app_prefs.edit();
        editor.putString(COUNTRY_PHONE_CODE, code);
        editor.apply();
    }

    public String getCountryPhoneCode() {
        return app_prefs.getString(COUNTRY_PHONE_CODE, null);
    }


    public void putDeviceToken(String deviceToken) {
        SharedPreferences.Editor editor = app_prefs.edit();
        editor.putString(Constant.DEVICE_TOKEN, deviceToken);
        editor.apply();
    }

    public String getDeviceToken() {
        return app_prefs.getString(Constant.DEVICE_TOKEN, null);

    }

    public void putGoogleKey(String googleKey) {
        SharedPreferences.Editor editor = app_prefs.edit();
        editor.putString(Constant.GOOGLE_KEY, googleKey);
        editor.apply();
    }

    public String getGoogleKey() {
        return app_prefs.getString(Constant.GOOGLE_KEY, null);
    }

    public void putAdminContactEmail(String email) {
        SharedPreferences.Editor editor = app_prefs.edit();
        editor.putString(ADMIN_CONTACT_EMAIL, email);
        editor.apply();
    }

    public String getAdminContactEmail() {
        return app_prefs.getString(ADMIN_CONTACT_EMAIL, null);
    }

    public void putIsVerifyEmail(boolean value) {
        SharedPreferences.Editor editor = app_prefs.edit();
        editor.putBoolean(IS_VERIFY_EMAIL, value);
        editor.apply();
    }

    public boolean getIsVerifyEmail() {
        return app_prefs.getBoolean(IS_VERIFY_EMAIL, false);
    }

    public void putIsVerifyPhone(boolean value) {
        SharedPreferences.Editor editor = app_prefs.edit();
        editor.putBoolean(IS_VERIFY_PHONE, value);
        editor.apply();
    }

    public boolean getIsVerifyPhone() {
        return app_prefs.getBoolean(IS_VERIFY_PHONE, false);
    }


    public void putIsUserReferal(String googleKey) {
        SharedPreferences.Editor editor = app_prefs.edit();
        editor.putString(IS_USER_REFERRAL, googleKey);
        editor.apply();
    }

    public String getIsUserReferral() {
        return app_prefs.getString(IS_USER_REFERRAL, null);
    }


    public void putIsLoginByPhone(boolean isLogin) {
        SharedPreferences.Editor editor = app_prefs.edit();
        editor.putBoolean(IS_LOGIN_BY_PHONE, isLogin);
        editor.apply();
    }

    public boolean getIsLoginByPhone() {
        return app_prefs.getBoolean(IS_LOGIN_BY_PHONE, false);
    }

    public void putIsLoginByEmail(boolean isLogin) {
        SharedPreferences.Editor editor = app_prefs.edit();
        editor.putBoolean(IS_LOGIN_BY_EMAIL, isLogin);
        editor.apply();
    }

    public boolean getIsLoginByEmail() {
        return app_prefs.getBoolean(IS_LOGIN_BY_EMAIL, false);
    }

    public void putStoreId(String userId) {
        SharedPreferences.Editor editor = app_prefs.edit();
        editor.putString(Constant.STORE_ID, userId);
        editor.apply();
        ApiClient.setStoreId(userId);
    }


    public String getStoreId() {
        return app_prefs.getString(Constant.STORE_ID, null);
    }

    public String getServerToken() {
        return app_prefs.getString(Constant.SERVER_TOKEN, null);
    }

    public void putServerToken(String serverToken) {
        SharedPreferences.Editor editor = app_prefs.edit();
        editor.putString(Constant.SERVER_TOKEN, serverToken);
        editor.apply();
        ApiClient.setServerToken(serverToken);
    }

    public void putUploadStoreDocuments(boolean isUploadStoreDocuments) {
        SharedPreferences.Editor editor = app_prefs.edit();
        editor.putBoolean(IS_UPLOAD_STORE_DOCUMENTS, isUploadStoreDocuments);
        editor.apply();
    }

    public boolean isUploadStoreDocuments() {
        return app_prefs.getBoolean(IS_UPLOAD_STORE_DOCUMENTS, false);
    }

    public void putStoreProfilePicRequired(boolean isStoreProfilePicRequired) {
        SharedPreferences.Editor editor = app_prefs.edit();
        editor.putBoolean(IS_STORE_PROFILE_PICTURE_REQUIRED, isStoreProfilePicRequired);
        editor.apply();
    }

    public boolean isStoreProfilePicRequired() {
        return app_prefs.getBoolean(IS_STORE_PROFILE_PICTURE_REQUIRED, false);
    }

    public void putReferralToStore(boolean isReferralToStore) {
        SharedPreferences.Editor editor = app_prefs.edit();
        editor.putBoolean(IS_REFERRAL_TO_STORE, isReferralToStore);
        editor.apply();
    }

    public boolean isReferralToStore() {
        return app_prefs.getBoolean(IS_REFERRAL_TO_STORE, false);
    }

    public void putShowOptionalFieldInRegister(boolean isHideOptionalFieldInRegister) {
        SharedPreferences.Editor editor = app_prefs.edit();
        editor.putBoolean(IS_HIDE_OPTIONAL_FIELD_IN_REGISTER, isHideOptionalFieldInRegister);
        editor.apply();
    }

    public boolean isShowOptionalFieldInRegister() {
        return app_prefs.getBoolean(IS_HIDE_OPTIONAL_FIELD_IN_REGISTER, false);
    }


    public void putForceUpdate(boolean isForceUpdate) {
        SharedPreferences.Editor editor = app_prefs.edit();
        editor.putBoolean(IS_FORCE_UPDATE, isForceUpdate);
        editor.apply();
    }

    public boolean isForceUpdate() {
        return app_prefs.getBoolean(IS_FORCE_UPDATE, false);
    }

    public void putVersionCode(String versionCode) {
        SharedPreferences.Editor editor = app_prefs.edit();
        editor.putString(VERSION_CODE, versionCode);
        editor.apply();
    }

    public String getVersionCode() {
        return app_prefs.getString(VERSION_CODE, null);
    }

    public void putCurrency(String currency) {
        SharedPreferences.Editor editor = app_prefs.edit();
        editor.putString(CURRENCY, currency);
        editor.apply();
    }

    public String getCurrency() {
        return app_prefs.getString(CURRENCY, null);
    }

    public void putMaxPhoneNumberLength(int length) {
        SharedPreferences.Editor editor = app_prefs.edit();
        editor.putInt(MAX_LENGTH, length);
        editor.commit();
    }

    public int getMaxPhoneNumberLength() {
        return app_prefs.getInt(MAX_LENGTH, 10);
    }

    public void putMinPhoneNumberLength(int length) {
        SharedPreferences.Editor editor = app_prefs.edit();
        editor.putInt(MIN_LENGTH, length);
        editor.commit();
    }

    public int getMinPhoneNumberLength() {
        return app_prefs.getInt(MIN_LENGTH, 9);
    }

    public void putIsUserAllDocumentsUpload(boolean isRequired) {
        SharedPreferences.Editor editor = app_prefs.edit();
        editor.putBoolean(IS_UPLOAD_DOCUMENTS, isRequired);
        editor.commit();
    }

    public boolean getIsUserAllDocumentsUpload() {
        return app_prefs.getBoolean(IS_UPLOAD_DOCUMENTS, false);
    }

    public void putIsAdminDocumentMandatory(boolean isUpload) {
        SharedPreferences.Editor editor = app_prefs.edit();
        editor.putBoolean(IS_ADMIN_DOCUMENT_MANDATORY, isUpload);
        editor.commit();
    }

    public boolean getIsAdminDocumentMandatory() {
        return app_prefs.getBoolean(IS_ADMIN_DOCUMENT_MANDATORY, false);
    }

    public void putLanguageCode(String code) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(LANGUAGE, code);
        edit.commit();
//        ApiClient.setLanguage(code);
    }

    public String getLanguageCode() {
        return app_prefs.getString(LANGUAGE, "");

    }

    public void putLanguageIndex(String index) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(LANGUAGE_INDEX, index);
        edit.commit();
        ApiClient.setLanguage(index);
    }

    public String getLanguageIndex() {
        return app_prefs.getString(LANGUAGE_INDEX, "0");

    }

    public void putIsReferralOn(boolean isReferralOn) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putBoolean(IS_REFERRAL_ON, isReferralOn);
        edit.commit();
    }

    public boolean getIsReferralOn() {
        return app_prefs.getBoolean(IS_REFERRAL_ON, false);
    }

    public void putWalletCurrencyCode(String code) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(WALLET_CURRENCY_CODE, code);
        edit.commit();
    }

    public String getWalletCurrencyCode() {
        return app_prefs.getString(WALLET_CURRENCY_CODE, "--");
    }

    public void putWalletAmount(float amount) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putFloat(WALLET_AMOUNT, amount);
        edit.commit();
    }

    public float getWalletAmount() {
        return app_prefs.getFloat(WALLET_AMOUNT, 0);
    }

    public void putReferralCode(String code) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(REFERRAL_CODE, code);
        edit.commit();
    }

    public String getReferralCode() {
        return app_prefs.getString(REFERRAL_CODE, "--");
    }

    public void putSocialId(String id) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(SOCIAL_ID, id);
        edit.commit();
    }

    public String getSocialId() {
        return app_prefs.getString(SOCIAL_ID, "");

    }

    public void putIsLoginBySocial(boolean isLogin) {
        SharedPreferences.Editor editor = app_prefs.edit();
        editor.putBoolean(IS_LOGIN_BY_SOCIAL, isLogin);
        editor.commit();
    }

    public boolean getIsLoginBySocial() {
        return app_prefs.getBoolean(IS_LOGIN_BY_SOCIAL, false);
    }

    public boolean getIsPushNotificationSoundOn() {

        return app_prefs.getBoolean(IS_PUSH_SOUND_ON, true);
    }

    public void putIsPushNotificationSoundOn(boolean isSoundOn) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putBoolean(IS_PUSH_SOUND_ON, isSoundOn);
        edit.commit();

    }

    public void putAdminContact(String contact) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(ADMIN_CONTACT, contact);
        edit.commit();
    }

    public String getAdminContact() {
        return app_prefs.getString(ADMIN_CONTACT, null);

    }


    public void putTermsANdConditions(String tandc) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(T_AND_C, tandc);
        edit.commit();
    }

    public String getTermsANdConditions() {
        return app_prefs.getString(T_AND_C, null);

    }


    public void putPolicy(String policy) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(POLICY, policy);
        edit.commit();
    }

    public String getPolicy() {
        return app_prefs.getString(POLICY, null);

    }

    public void putIsAskForEstimatedTimeForOrderReady(boolean value) {
        SharedPreferences.Editor editor = app_prefs.edit();
        editor.putBoolean(IS_ASK_ESTIMATED_TIME, value);
        editor.apply();
    }

    public boolean getIsAskForEstimatedTimeForOrderReady() {
        return app_prefs.getBoolean(IS_ASK_ESTIMATED_TIME, false);
    }

    public void putIsProvidePickupDelivery(boolean value) {
        SharedPreferences.Editor editor = app_prefs.edit();
        editor.putBoolean(IS_PROVIDE_PICKUP_DELIVERY, value);
        editor.apply();
    }

    public boolean getIsProvidePickupDelivery() {
        return app_prefs.getBoolean(IS_PROVIDE_PICKUP_DELIVERY, false);
    }

    public void putCityId(String cityId) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(Constant.CITY_ID, cityId);
        edit.commit();
    }

    public String getCityId() {
        return app_prefs.getString(Constant.CITY_ID, null);

    }


    public void putAddress(String address) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(ADDRESS, address);
        edit.commit();
    }

    public String getAddress() {
        return app_prefs.getString(ADDRESS, null);

    }

    public void putCity(String city) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(CITY, city);
        edit.commit();
    }

    public String getCity() {
        return app_prefs.getString(CITY, null);

    }

    public void putLatitude(String lat) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(LAT, lat);
        edit.commit();
    }

    public String getLatitude() {
        return app_prefs.getString(LAT, "0");

    }

    public void putLongitude(String lng) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(LNG, lng);
        edit.commit();
    }

    public String getLongitude() {
        return app_prefs.getString(LNG, "0");

    }

    public void putAndroidId(String id) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(ANDROID_ID, id);
        edit.commit();
    }

    public String getAndroidId() {
        return app_prefs.getString(ANDROID_ID, "");

    }

    public void putCartId(String id) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(CART_ID, id);
        edit.commit();
    }

    public String getCartId() {
        return app_prefs.getString(CART_ID, "");

    }

    public void clearVerification() {
        putIsEmailVerified(false);
        putIsPhoneNumberVerified(false);
    }

    public boolean getIsUseItemTax() {
        return app_prefs.getBoolean(IS_USE_ITEM_TAX, false);

    }

    public void putIsUseItemTax(boolean isUseItemTax) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putBoolean(IS_USE_ITEM_TAX, isUseItemTax);
        edit.commit();
    }

    public float getStoreTax() {
        return app_prefs.getFloat(STORE_TAX, 0f);

    }

    public void putStoreTax(float storeTax) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putFloat(STORE_TAX, storeTax);
        edit.commit();
    }


    public void putIsStoreCanCreateGroup(boolean isStoreCanCreateGroup) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putBoolean(IS_STORE_CAN_CREATE_GROUP, isStoreCanCreateGroup);
        edit.commit();
    }

    public boolean getIsStoreCanCreateGroup() {
        return app_prefs.getBoolean(IS_STORE_CAN_CREATE_GROUP, false);
    }

    public void putIsStoreCanEditOrder(boolean isStoreCanEditOrder) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putBoolean(IS_STORE_CAN_EDIT_ORDER, isStoreCanEditOrder);
        edit.commit();
    }

    public boolean getIsStoreCanEditOrder() {
        return app_prefs.getBoolean(IS_STORE_CAN_EDIT_ORDER, false);
    }

    public void putIsStoreCanAddProvider(boolean isEnable) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putBoolean(IS_STORE_CAN_ADD_PROVIDER, isEnable);
        edit.commit();
    }

    public boolean getIsStoreCanAddProvider() {
        return app_prefs.getBoolean(IS_STORE_CAN_ADD_PROVIDER, false);
    }

    public void putIsStoreCanCompleteOrder(boolean isEnable) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putBoolean(IS_STORE_CAN_COMPLETE_ORDER, isEnable);
        edit.commit();
    }

    public boolean getIsStoreCanCompleteOrder() {
        return app_prefs.getBoolean(IS_STORE_CAN_COMPLETE_ORDER, false);
    }


    public void putIsStoreCreateOrder(boolean isEnable) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putBoolean(IS_STORE_CREATE_ORDER, isEnable);
        edit.commit();
    }

    public boolean getIsStoreCreateOrder() {
        return app_prefs.getBoolean(IS_STORE_CREATE_ORDER, false);
    }

    public void putIsStoreEditMenu(boolean isEnable) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putBoolean(IS_STORE_EDIT_MENU, isEnable);
        edit.commit();
    }

    public boolean getIsStoreEditMenu() {
        return app_prefs.getBoolean(IS_STORE_EDIT_MENU, false);
    }

    public void putIsStoreEditItem(boolean isEnable) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putBoolean(IS_STORE_EDIT_ITEM, isEnable);
        edit.commit();
    }

    public boolean getIsStoreEditItem() {
        return app_prefs.getBoolean(IS_STORE_EDIT_ITEM, false);
    }


    public void putIsStoreAddPromoCode(boolean isEnable) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putBoolean(IS_STORE_ADD_PROMOCODE, isEnable);
        edit.commit();
    }

    public boolean getIsStoreAddPromoCode() {
        return app_prefs.getBoolean(IS_STORE_ADD_PROMOCODE, false);
    }


    public void putIsStoreCanSetCancellationCharge(boolean isEnable) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putBoolean(IS_STORE_CAN_SET_CANCELLATION_CHARGE, isEnable);
        edit.commit();
    }

    public boolean getIsStoreCanSetCancellationCharge() {
        return app_prefs.getBoolean(IS_STORE_CAN_SET_CANCELLATION_CHARGE, false);
    }

    public void putProfilePic(String profilePic) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(PROFILE_PIC, profilePic);
        edit.commit();
    }

    public String getProfilePic() {
        return app_prefs.getString(PROFILE_PIC, null);

    }

    public static String getLanguageCodeApp() {
        if (preferenceHelper != null) {
            return app_prefs.getString(LANGUAGE, "");
        } else {
            return "en";
        }

    }

    public static int getLanguageIndexApp() {
        if (preferenceHelper != null) {
            return Integer.parseInt(app_prefs.getString(LANGUAGE_INDEX, "0"));
        } else {
            return Integer.parseInt("0");
        }

    }

    public void putSubStoreId(String subStoreId) {
        SharedPreferences.Editor editor = app_prefs.edit();
        editor.putString(SUB_STORE_ID, subStoreId);
        editor.apply();
        ApiClient.setSubStoreId(subStoreId);
    }


    public String getSubStoreId() {
        return app_prefs.getString(SUB_STORE_ID, "");
    }

    public void putSagpayIntegration_Password(String merchantSessionUrl) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(SAGPAY_Integration_Password, merchantSessionUrl);
        edit.commit();
    }

    public String getSagpayIntegration_Password() {
        return app_prefs.getString(SAGPAY_Integration_Password, "");

    }
    public void putSagpay_Integration_Key(String cardIdentifierUrl) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(SAGPAY_Integration_Key, cardIdentifierUrl);
        edit.commit();
    }

    public String getSagpay_Integration_Key() {
        return app_prefs.getString(SAGPAY_Integration_Key, "");

    }

    public void putSagpay_baseurl(String sagpay_url) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(SAGPAY_BASEURL, sagpay_url);
        edit.commit();
    }

    public String getSagpay_baseurl() {
        return app_prefs.getString(SAGPAY_BASEURL, "");

    }
    public void putSagpay_vendorName(String sagpay_url) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(VENDORNAME, sagpay_url);
        edit.commit();
    }

    public String getSagpay_vendorName() {
        return app_prefs.getString(VENDORNAME, "");

    }
}
