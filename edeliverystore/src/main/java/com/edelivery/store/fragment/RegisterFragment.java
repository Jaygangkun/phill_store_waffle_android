package com.edelivery.store.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.edelivery.store.RegisterLoginActivity;
import com.edelivery.store.StoreLocationActivity;
import com.edelivery.store.component.AddDetailInMultiLanguageDialog;
import com.edelivery.store.component.CustomAlterDialog;
import com.edelivery.store.component.CustomEditTextDialog;
import com.edelivery.store.component.CustomListDialog;
import com.edelivery.store.models.datamodel.Category;
import com.edelivery.store.models.datamodel.City;
import com.edelivery.store.models.datamodel.Country;
import com.edelivery.store.models.responsemodel.CategoriesResponse;
import com.edelivery.store.models.responsemodel.CityResponse;
import com.edelivery.store.models.responsemodel.CountriesResponse;
import com.edelivery.store.models.responsemodel.IsSuccessResponse;
import com.edelivery.store.models.responsemodel.OTPResponse;
import com.edelivery.store.models.responsemodel.StoreDataResponse;
import com.edelivery.store.models.singleton.Language;
import com.edelivery.store.parse.ApiClient;
import com.edelivery.store.parse.ApiInterface;
import com.edelivery.store.parse.ParseContent;
import com.edelivery.store.utils.Constant;
import com.edelivery.store.utils.GlideApp;
import com.edelivery.store.utils.ImageCompression;
import com.edelivery.store.utils.ImageHelper;
import com.edelivery.store.utils.LocationHelper;
import com.edelivery.store.utils.PreferenceHelper;
import com.edelivery.store.utils.Utilities;
import com.edelivery.store.widgets.CustomInputEditText;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.edelivery.store.utils.Constant.STORE_LOCATION_RESULT;

/**
 * RegisterFragment on 30-01-2017.
 */

public class RegisterFragment extends Fragment implements View.OnClickListener, LocationHelper
        .OnLocationReceived {

    public String cityId, countryId, countryCode, categoryId;
    public ArrayList<Country> countryList;
    public ArrayList<Category> categoryList;
    private EditText etName, etEmail, etPassword, etAddress, etMobileNo, etSlogan,
            etWebsite, etLat, etLng, etPasswordRetype;
    private TextView tvCountry, tvCity, tvCategory, tvCountryCode;
    private ImageView ivSuccess;
    private RoundedImageView ivProfile;
    private RegisterLoginActivity activity;
    private String TAG = "RegisterFragment";
    private Uri uri;
    private ArrayList<City> cityList;
    private LatLng latLng;
    private LocationHelper locationHelper;
    private int maxPhoneNumberLength, minPhoneNumberLength;
    private ImageHelper imageHelper;
    private TextView tvReferralApply;
    private CustomInputEditText etReferralCode;
    private String referralCode = "", socialId = "";
    private LinearLayout llReferral;
    private CustomListDialog customListDialog;
    private ImageView ivStoreLocation;
    private TextInputLayout tilPassword, tilRetypePassword;
    private LinearLayout llSocialLogin;
    private CheckBox cbTcPolicy;
    private CustomTextView tvPolicy;
    private String currentPhotoPath;
    private AddDetailInMultiLanguageDialog addDetailInMultiLanguageDialog;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (RegisterLoginActivity) getActivity();
        locationHelper = new LocationHelper(activity);
        locationHelper.setLocationReceivedLister(this);
        imageHelper = new ImageHelper(activity);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        etName = (EditText) view.findViewById(R.id.etName);
        etEmail = (EditText) view.findViewById(R.id.etEmail);
        etPassword = (EditText) view.findViewById(R.id.etPassword);
        etAddress = (EditText) view.findViewById(R.id.etAddress);
        etMobileNo = (EditText) view.findViewById(R.id.etMobileNo);
        etSlogan = (EditText) view.findViewById(R.id.etSlogan);
        etWebsite = (EditText) view.findViewById(R.id.etWebsite);
        etLat = (EditText) view.findViewById(R.id.etLat);
        etLng = (EditText) view.findViewById(R.id.etLng);
        ivProfile = (RoundedImageView) view.findViewById(R.id.ivProfile);
        tvCountry = (TextView) view.findViewById(R.id.tvCountry);
        tvCountryCode = (TextView) view.findViewById(R.id.tvCountryCode);
        tvCategory = (TextView) view.findViewById(R.id.tvDeliveryType);
        tvCity = (TextView) view.findViewById(R.id.tvCity);
        etPasswordRetype = (EditText) view.findViewById(R.id.etPasswordRetype);

        LinearLayout llOptionalField = (LinearLayout) view.findViewById(R.id.llOptionalField);

        if (PreferenceHelper.getPreferenceHelper(activity).isShowOptionalFieldInRegister()) {
            llOptionalField.setVisibility(View.VISIBLE);
        } else {
            llOptionalField.setVisibility(View.GONE);

        }

        view.findViewById(R.id.ivProfileImageSelect).setOnClickListener(this);
        view.findViewById(R.id.btnRegister).setOnClickListener(this);

        tvCountry.setOnClickListener(this);
        tvCity.setOnClickListener(this);
        tvCategory.setOnClickListener(this);

        tvReferralApply = (TextView) view.findViewById(R.id.tvReferralApply);
        ivSuccess = (ImageView) view.findViewById(R.id.ivSuccess);
        etReferralCode = (CustomInputEditText) view.findViewById(R.id
                .etReferralCode);
        llReferral = (LinearLayout) view.findViewById(R.id.llReferral);

        tilPassword = (TextInputLayout) view.findViewById(R.id.tilPassword);
        tilRetypePassword = (TextInputLayout) view.findViewById(R.id.tilRetypePassword);
        llSocialLogin = (LinearLayout) view.findViewById(R.id.llSocialButton);
        ivStoreLocation = (ImageView) view.findViewById(R.id.ivStoreLocation);
        cbTcPolicy = (CheckBox) view.findViewById(R.id.cbTcPolicy);
        tvPolicy = (CustomTextView) view.findViewById(R.id.tvPolicy);
        activity.initFBLogin(view);
        activity.initGoogleLogin(view);
        activity.initTwitterLogin(view);
//        etName.setOnClickListener(this);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        checkSocialLoginISOn(activity.preferenceHelper.getIsLoginBySocial());
        tvReferralApply.setOnClickListener(this);
        ivStoreLocation.setOnClickListener(this);
        etAddress.setOnClickListener(this);
        String link = activity.getResources().getString(R.string.text_link_sign_up_privacy)
                + " " +
                "<a href=\"" + activity.preferenceHelper.getTermsANdConditions() + "\"" + ">" +
                getResources().getString(R.string.text_t_and_c) +
                "</a>" + " " + activity.getResources().getString(R.string.text_and) + " " +
                "<a href=\"" + activity.preferenceHelper.getPolicy() + "\"" + ">" +
                getResources().getString(R.string.text_policy) +
                "</a>";
        tvPolicy.setText(Utilities.fromHtml(link));
        tvPolicy.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void onStart() {
        super.onStart();
        checkPermission();
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission
                .ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(activity,
                android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{android.Manifest.permission
                            .ACCESS_FINE_LOCATION, android.Manifest.permission
                            .ACCESS_COARSE_LOCATION},
                    Constant.PERMISSION_FOR_LOCATION);
        } else {
            //Do the stuff that requires permission...
            locationHelper.onStart();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        locationHelper.onStop();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.ivProfileImageSelect:
                showPhotoSelectionDialog();
                break;

            case R.id.btnRegister:
                validate();
                break;

            case R.id.tvCountry:
                if (countryList != null && countryList.size() > 0) {
                    if (customListDialog != null && customListDialog.isShowing()) {
                        return;
                    }

                    customListDialog = new CustomListDialog(activity, countryList, true) {

                        @Override
                        public void onItemClickOnList(Object object) {
                            setCountry(object);
                            this.dismiss();
                        }
                    };
                    customListDialog.show();
                }
                break;

            case R.id.tvCity:
                if (countryId != null) {
                    if (cityList != null && cityList.size() > 0) {
                        if (customListDialog != null && customListDialog.isShowing()) {
                            return;
                        }
                        customListDialog = new CustomListDialog(activity, cityList) {

                            @Override
                            public void onItemClickOnList(Object object) {
                                City city = (City) object;
                                if (!TextUtils.equals(cityId, city.getId())) {
                                    cityId = city.getId();
                                    tvCity.setText(city.getCityName());
                                    tvCity.setTextColor(ContextCompat.getColor(activity, R.color
                                            .color_app_text));
                                    tvCategory.setText(getResources().getString(R.string
                                            .text_select_category));
                                    getCategoryList(cityId);
                                }
                                this.dismiss();
                            }
                        };
                        customListDialog.show();
                    }
                } else {
                    new CustomAlterDialog(activity, null, activity.getResources().getString(R
                            .string.msg_choose_country)) {
                        @Override
                        public void btnOnClick(int btnId) {
                            this.dismiss();
                        }
                    }.show();
                }
                break;

            case R.id.tvDeliveryType:
                if (categoryList != null && categoryList.size() > 0) {
                    if (customListDialog != null && customListDialog.isShowing()) {
                        return;
                    }
                    customListDialog = new CustomListDialog(activity, categoryList, 1) {
                        @Override
                        public void onItemClickOnList(Object object) {
                            Category category = (Category) object;
                            categoryId = category.getId();
                            tvCategory.setText(category.getDeliveryName());
                            tvCategory.setTextColor(ContextCompat.getColor(activity, R.color
                                    .color_app_text));
                            this.dismiss();
                        }
                    };
                    customListDialog.show();
                }
                break;
            case R.id.tvReferralApply:
                if (TextUtils.isEmpty(etReferralCode.getText().toString().trim())) {
                    Utilities.showToast(activity, activity.getResources().getString(R.string
                            .msg_plz_enter_valid_referral));
                } else {
                    if (TextUtils.isEmpty(countryId)) {
                        Utilities.showToast(activity, activity.getResources().getString(R.string
                                .msg_select_your_country_first));
                    } else {
                        checkReferralCode();
                    }
                }
                break;
            case R.id.ivStoreLocation:
            case R.id.etAddress:
                setStoreAddressORLocation();
                break;
            case R.id.etName:
                addMultiLanguageDetail(etName.getHint().toString(),
                        (List<String>) etName.getTag(),
                        new AddDetailInMultiLanguageDialog.SaveDetails() {
                            @Override
                            public void onSave(List<String> detailList) {
                                etName.setTag(detailList);
                                etName.setText(Utilities.getDetailStringFromList(detailList,
                                        Language.getInstance().getStoreLanguageIndex()));

                            }
                        });
                break;
        }
    }

    /**
     * this method call a webservice for get city list
     *
     * @param countryId selected country id in string
     */
    private void getCityList(String countryId) {
        Utilities.showProgressDialog(activity);

        Call<CityResponse> citiesCall = ApiClient.getClient().create(ApiInterface.class).getCities
                (countryId);
        citiesCall.enqueue(new Callback<CityResponse>() {
            @Override
            public void onResponse(Call<CityResponse> call, Response<CityResponse> response) {
                Utilities.removeProgressDialog();
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        cityList = response.body().getCities();
                    } else {
                        ParseContent.getParseContentInstance().showErrorMessage(activity,
                                response.body().getErrorCode(), true);
                    }
                } else {
                    Utilities.showHttpErrorToast(response.code(), activity);
                }
            }

            @Override
            public void onFailure(Call<CityResponse> call, Throwable t) {
                t.printStackTrace();
                Utilities.printLog(TAG, t.getMessage());
            }
        });
    }

    private void validate() {

        if (TextUtils.isEmpty(etName.getText().toString().trim())) {
            etName.setError(activity.getResources().getString(R.string.msg_empty_name));
        } else if (TextUtils.isEmpty(etEmail.getText().toString().trim())) {
            etEmail.setError(activity.getResources().getString(R.string.msg_empty_email));
        } else if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString().trim()).matches()) {
            etEmail.setError(activity.getResources().getString(R.string.msg_valid_email));
        } else if (etPassword.getVisibility() == View.VISIBLE && TextUtils.isEmpty(etPassword
                .getText
                        ().toString()
        )) {
            etPassword.setError(activity.getResources().getString(R.string.msg_empty_password));
        } else if (etPassword.getVisibility() == View.VISIBLE && etPassword.getText().toString()
                .trim()
                .length() < 6) {
            etPassword.setError(activity.getResources().getString(R.string.msg_password_length));
        } else if (etPassword.getVisibility() == View.VISIBLE && !TextUtils.equals
                (etPasswordRetype.getText().toString().trim(),
                        etPassword.getText().toString().trim())) {
            etPasswordRetype.setError(activity.getString(R.string.msg_enter_password_not_match));
            etPasswordRetype.requestFocus();
        } else if (countryId == null) {
            new CustomAlterDialog(activity, null, activity.getResources().getString(R.string
                    .msg_choose_country)) {
                @Override
                public void btnOnClick(int btnId) {
                    this.dismiss();
                }
            }.show();
        } else if (cityId == null) {
            new CustomAlterDialog(activity, null, activity.getResources().getString(R.string
                    .msg_choose_city)) {
                @Override
                public void btnOnClick(int btnId) {
                    this.dismiss();
                }
            }.show();
        } else if (categoryId == null) {
            new CustomAlterDialog(activity, null, activity.getResources().getString(R.string
                    .msg_choose_type)) {
                @Override
                public void btnOnClick(int btnId) {
                    this.dismiss();
                }
            }.show();
        } else if (TextUtils.isEmpty(etAddress.getText().toString().trim())) {
            etAddress.setError(activity.getResources().getString(R.string.msg_empty_address));
        } else if (TextUtils.isEmpty(etLat.getText().toString().trim())) {
            etAddress.setError(this.getResources().getString(R.string.msg_empty_address));
        } else if (TextUtils.isEmpty(etLng.getText().toString().trim())) {
            etAddress.setError(this.getResources().getString(R.string.msg_empty_address));
        } else if (TextUtils.isEmpty(etMobileNo.getText().toString().trim())) {
            etMobileNo.setError(activity.getResources().getString(R.string.msg_empty_mobileNo));
        } else if (etMobileNo.getText().toString().trim().length()
                > maxPhoneNumberLength || etMobileNo.getText().toString().trim().length
                () < minPhoneNumberLength) {
            String msg = activity.getString(R.string.msg_please_enter_valid_mobile_number_length)
                    + " " +
                    "" + minPhoneNumberLength + activity.getString(R.string.text_or)
                    + maxPhoneNumberLength + " " + activity.getString(R.string
                    .text_digits);
            etMobileNo.setError(msg);
            etMobileNo.requestFocus();
        } else if (PreferenceHelper.getPreferenceHelper(activity).isStoreProfilePicRequired() &&
                uri == null) {
            new CustomAlterDialog(activity, null, getString(R.string.msg_empty_profile)) {
                @Override
                public void btnOnClick(int btnId) {
                    this.dismiss();
                }
            }.show();
        } else if (!cbTcPolicy.isChecked()) {
            Utilities.showToast(activity, getResources().getString(R.string
                    .msg_plz_accept_tc));
        } else {

            if (activity.preferenceHelper.getIsVerifyEmail() || activity.preferenceHelper
                    .getIsVerifyPhone()) {
                getOtp();
            } else {
                register();
            }
        }
    }

    /**
     * @deprecated
     */
    private void getLatLngFromAddress() {
        Utilities.showProgressDialog(activity);
        Call<ResponseBody> requestBodyCall = ApiClient.getClient().create(ApiInterface.class)
                .getLatLngFromAddress(etAddress.getText().toString());

        requestBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Utilities.removeProgressDialog();
                    try {
                        String responseStr = response.body().string();
                        latLng = ParseContent.getParseContentInstance().getLatLngFromAddress
                                (responseStr);
                        if (latLng != null) {
                            etLat.setText(String.valueOf(latLng.latitude));
                            etLng.setText(String.valueOf(latLng.longitude));
                        } else {
                            new CustomAlterDialog(activity, null, activity.getResources()
                                    .getString(R.string.msg_invalid_latLng)) {
                                @Override
                                public void btnOnClick(int btnId) {
                                    this.dismiss();
                                }
                            }.show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Utilities.showHttpErrorToast(response.code(), activity);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Utilities.printLog(TAG, t.getMessage());
            }
        });
    }

    private void register() {
        Utilities.showProgressDialog(activity);

        HashMap<String, RequestBody> map = new HashMap<>();
        map.put(Constant.STORE_SERVICE_ID, ApiClient.makeTextRequestBody(categoryId));
        map.put(Constant.NAME, ApiClient.makeTextRequestBody(
                etName.getText().toString().trim()));
        map.put(Constant.EMAIL, ApiClient.makeTextRequestBody(
                etEmail.getText().toString().trim()
                        .toLowerCase()));
        map.put(Constant.COUNTRY_CODE, ApiClient.makeTextRequestBody(countryCode));
        map.put(Constant.PHONE, ApiClient.makeTextRequestBody(
                etMobileNo.getText().toString().trim()));
        map.put(Constant.ADDRESS, ApiClient.makeTextRequestBody(
                etAddress.getText().toString()));
        map.put(Constant.COUNTRY_ID, ApiClient.makeTextRequestBody(countryId));
        map.put(Constant.DEVICE_TOKEN, ApiClient.makeTextRequestBody(PreferenceHelper
                .getPreferenceHelper
                        (activity).getDeviceToken()));
        map.put(Constant.DEVICE_TYPE, ApiClient.makeTextRequestBody(Constant.ANDROID));
        map.put(Constant.SLOGAN, ApiClient.makeTextRequestBody(
                etSlogan.getText().toString().trim()));
        map.put(Constant.WEBSITE_URL, ApiClient.makeTextRequestBody(etWebsite.getText().toString
                ().trim()));
        map.put(Constant.CITY_ID, ApiClient.makeTextRequestBody(
                cityId));
        map.put(Constant.LATITUDE, ApiClient.makeTextRequestBody(
                String.valueOf(etLat.getText().toString())));
        map.put(Constant.LONGITUDE, ApiClient.makeTextRequestBody(String.valueOf(etLng.getText()
                .toString())));
        map.put(Constant.APP_VERSION, ApiClient.makeTextRequestBody(
                activity.getVersionCode()));
        map.put(Constant.REFERRAL_CODE, ApiClient.makeTextRequestBody(
                referralCode));
        map.put(Constant.IS_PHONE_NUMBER_VERIFIED,
                ApiClient.makeTextRequestBody(
                        String.valueOf(activity.preferenceHelper.isPhoneNumberVerified())));
        map.put(Constant.IS_EMAIL_VERIFIED,
                ApiClient.makeTextRequestBody(
                        String.valueOf(activity.preferenceHelper.isEmailVerified())));
        map.put(Constant.SOCIAL_ID,
                ApiClient.makeTextRequestBody(
                        String.valueOf(socialId)));
        if (TextUtils.isEmpty(socialId)) {
            map.put(Constant.PASS_WORD, ApiClient.makeTextRequestBody(
                    etPassword.getText().toString()));
            map.put(Constant.LOGIN_BY, ApiClient.makeTextRequestBody(
                    Constant.MANUAL));
        } else {
            map.put(Constant.PASS_WORD, ApiClient.makeTextRequestBody(
                    ""));
            map.put(Constant.LOGIN_BY, ApiClient.makeTextRequestBody(
                    Constant.SOCIAL));
        }
        Call<StoreDataResponse> call;
        if (TextUtils.isEmpty(currentPhotoPath)) {
            call = ApiClient.getClient().create(ApiInterface.class).register(map);
        } else {
            call = ApiClient.getClient().create(ApiInterface.class).register(map, ApiClient
                    .makeMultipartRequestBody(activity, currentPhotoPath, Constant.IMAGE_URL));
        }

        call.enqueue(new Callback<StoreDataResponse>() {
            @Override
            public void onResponse(Call<StoreDataResponse> call, Response<StoreDataResponse>
                    response) {
                Utilities.removeProgressDialog();
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        ParseContent.getParseContentInstance().parseStoreData(response.body());
                        PreferenceHelper.getPreferenceHelper(activity).putAndroidId(Utilities
                                .generateRandomString());
                        PreferenceHelper.getPreferenceHelper(activity).putCartId("");
                        ParseContent.getParseContentInstance().showMessage(activity,
                                response.body().getMessage());
                        activity.gotoHomeActivity(response.body().getStoreData());
                    } else {
                        ParseContent.getParseContentInstance().showErrorMessage(activity,
                                response.body().getErrorCode(), true);
                    }
                } else {
                    activity.preferenceHelper.clearVerification();
                    Utilities.showHttpErrorToast(response.code(), activity);
                }
            }

            @Override
            public void onFailure(Call<StoreDataResponse> call, Throwable t) {
                Utilities.printLog(TAG, t.getMessage());
            }
        });
    }


    private void getOtp() {
        Utilities.showCustomProgressDialog(activity, false);
        Utilities.printLog("getOtp", "in to otp");
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put(Constant.EMAIL, ApiClient.makeTextRequestBody(
                etEmail.getText().toString()));
        map.put(Constant.PHONE, ApiClient.makeTextRequestBody(
                etMobileNo.getText().toString()));
        map.put(Constant.TYPE, ApiClient.makeTextRequestBody(
                String.valueOf(Constant.Type.STORE)));
        map.put(Constant.COUNTRY_CODE, ApiClient.makeTextRequestBody(countryCode));


        Call<OTPResponse> otpResponseCall = ApiClient.getClient().create(ApiInterface.class)
                .otpVerification
                        (map);
        otpResponseCall.enqueue(new Callback<OTPResponse>() {
            @Override
            public void onResponse(Call<OTPResponse> call, Response<OTPResponse> response) {
                Utilities.printLog("getOtp", new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    Utilities.hideCustomProgressDialog();
                    if (response.body().isSuccess()) {
                        openAccountVerifyDialog(String.valueOf(response.body().getOtpForSms()),
                                String.valueOf(response.body().getOtpForEmail()));
                    } else {
                        ParseContent.getParseContentInstance().showErrorMessage(activity,
                                response.body().getErrorCode(), true);
                        //getOtp();
                    }
                } else {
                    Utilities.showHttpErrorToast(response.code(), activity);
                }

            }

            @Override
            public void onFailure(Call<OTPResponse> call, Throwable t) {
                Utilities.printLog("getOtp", t.getMessage());
            }
        });
    }

    private void showPhotoSelectionDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(activity);
        pictureDialog.setTitle(getResources().getString(
                R.string.text_choosePicture));
        String[] pictureDialogItems = {
                getResources().getString(R.string.text_gallery),
                getResources().getString(R.string.text_camera)};

        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {

                            case 0:
                                choosePhotoFromGallery();
                                break;

                            case 1:
                                takePhotoFromCameraPermission();
                                break;

                            default:
                                break;

                        }
                    }
                });
        pictureDialog.show();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void choosePhotoFromGallery() {
        if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission
                .READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat
                .checkSelfPermission(activity, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    Constant.PERMISSION_CHOOSE_PHOTO);
        } else {
          /*  Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            startActivityForResult(galleryIntent, Constant.PERMISSION_CHOOSE_PHOTO);*/

            Intent galleryIntent = new Intent();
            galleryIntent.setType("image/*");
            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(galleryIntent, Constant.PERMISSION_CHOOSE_PHOTO);

        }
    }

    private void takePhotoFromCameraPermission() {
        if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(activity,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager
                .PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.CAMERA, android.Manifest
                    .permission.WRITE_EXTERNAL_STORAGE}, Constant.PERMISSION_TAKE_PHOTO);
        } else {
            takePhotoFromCamera();
        }
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = imageHelper.createImageFile();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(activity, activity.getPackageName(), file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(file);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, Constant.PERMISSION_TAKE_PHOTO);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {
        switch (requestCode) {
            case Constant.PERMISSION_CHOOSE_PHOTO:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        choosePhotoFromGallery();
                    }
                }
                break;
            case Constant.PERMISSION_TAKE_PHOTO:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        takePhotoFromCameraPermission();
                    }
                }
                break;
            case Constant.PERMISSION_FOR_LOCATION:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        locationHelper.onStart();
                    }
                }
                break;
            default:
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Constant.PERMISSION_CHOOSE_PHOTO:
                    if (data != null) {
                        uri = data.getData();
                        setImage(uri);
                    }
                    break;
                case Constant.PERMISSION_TAKE_PHOTO:
                    if (uri != null) {
                        setImage(uri);
                    }
                    break;
                case STORE_LOCATION_RESULT:
                    Bundle bundle = data.getExtras();
                    etAddress.setText(bundle.getString(Constant.ADDRESS));
                    etLat.setText(String.valueOf(bundle.getDouble(Constant.LATITUDE)));
                    etLng.setText(String.valueOf(bundle.getDouble(Constant.LONGITUDE)));
                    break;
                default:
                    // result
                    break;
            }
        }
    }

    private void setImage(final Uri uri) {
        currentPhotoPath = ImageHelper.getFromMediaUriPfd(activity, activity.getContentResolver
                (), uri).getPath();
        new ImageCompression(activity).setImageCompressionListener(new ImageCompression
                .ImageCompressionListener() {
            @Override
            public void onImageCompression(String compressionImagePath) {
                currentPhotoPath = compressionImagePath;
                GlideApp.with(activity).load(uri)
                        .error(R
                                .drawable.icon_default_profile).into(ivProfile);
            }
        }).execute(currentPhotoPath);
        Utilities.closeKeyboard(activity, getView());
    }

    private void openAccountVerifyDialog(final String otpForSms, final String otpForEmail) {
        final int otpType = activity.checkWitchOtpValidationON();

        if (otpType != -1) {
            String message = null;

            switch (otpType) {
                case Constant.EMAIL_VERIFICATION_ON:
                    message = getString(R.string.text_verify_email);
                    break;
                case Constant.SMS_VERIFICATION_ON:
                    message = getString(R.string.text_verify_sms);
                    break;
                case Constant.SMS_AND_EMAIL_VERIFICATION_ON:
                    message = getString(R.string.text_verify_email_sms);
                    break;
                default:
                    // do with default
                    break;
            }
            CustomEditTextDialog accountVerifyDialog = new CustomEditTextDialog(activity,
                    getString(R.string
                            .text_verify_details), message,
                    getString(R.string.text_ok), getString(R.string.text_exit),
                    otpType) {


                @Override
                public void btnOnClick(int btnId, TextInputEditText etSMSOtp, TextInputEditText
                        etEmailOtp) {
                    if (btnId == R.id.btnPositive) {
                        switch (otpType) {
                            case Constant.SMS_AND_EMAIL_VERIFICATION_ON:
                                if (TextUtils.isEmpty(etSMSOtp.getText())) {
                                    etSMSOtp.setError(getString(R.string.msg_invalid_data));
                                } else if (TextUtils.isEmpty(etEmailOtp.getText())) {
                                    etEmailOtp.setError(getString(R.string.msg_invalid_data));
                                } else {
                                    if (TextUtils.equals(otpForSms, etSMSOtp.getText().toString()
                                            .trim())
                                            && TextUtils.equals(otpForEmail, etEmailOtp.getText()
                                            .toString().trim())) {
                                        activity.preferenceHelper.putIsEmailVerified(true);
                                        activity.preferenceHelper.putIsPhoneNumberVerified(true);
                                        dismiss();
                                        register();
                                    } else {
                                        Utilities.showToast(activity, getString(R.string
                                                .msg_otp_wrong));
                                    }
                                }
                                break;
                            case Constant.SMS_VERIFICATION_ON:
                                if (TextUtils.isEmpty(etSMSOtp.getText())) {
                                    etSMSOtp.setError(getString(R.string.msg_invalid_data));
                                } else {
                                    if (TextUtils.equals(otpForSms, etSMSOtp.getText().toString()
                                            .trim())) {
                                        activity.preferenceHelper.putIsPhoneNumberVerified(true);
                                        dismiss();
                                        register();
                                    } else {
                                        Utilities.showToast(activity, getString(R.string
                                                .msg_otp_wrong));
                                    }
                                }
                                break;
                            case Constant.EMAIL_VERIFICATION_ON:

                                if (TextUtils.isEmpty(etEmailOtp.getText())) {
                                    etEmailOtp.setError(getString(R.string.msg_invalid_data));
                                } else {
                                    if (TextUtils.equals(otpForEmail, etEmailOtp.getText()
                                            .toString().trim())) {
                                        activity.preferenceHelper.putIsEmailVerified(true);
                                        dismiss();
                                        register();
                                    } else {
                                        Utilities.showToast(activity, getString(R.string
                                                .msg_otp_wrong));
                                    }
                                }

                                break;
                        }
                    } else {
                        dismiss();
                        activity.finish();
                    }
                }
            };
            accountVerifyDialog.setCancelable(false);
            accountVerifyDialog.show();
        }


    }

    @Override
    public void onConnected(Bundle bundle) {
        getCountryList();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    /**
     * this method call a webservice for get country list
     */
    private void getCountryList() {
        if (categoryList == null) {
            Call<CountriesResponse> countriesCall = ApiClient.getClient().create(ApiInterface.class)
                    .getCountries();
            countriesCall.enqueue(new Callback<CountriesResponse>() {
                @Override
                public void onResponse(Call<CountriesResponse> call, Response<CountriesResponse>
                        response) {
                    if (response.isSuccessful()) {
                        if (response.body().isSuccess()) {
                            countryList = response.body().getCountries();
                            locationHelper.getLastLocation(activity, new
                                    OnSuccessListener<Location>() {
                                        @Override
                                        public void onSuccess(Location location) {
                                            if (location != null) {
                                                new GetCityAsyncTask(location.getLatitude(),
                                                        location
                                                                .getLongitude()).execute();
                                            } else {
                                                setCountry(countryList.get(0));
                                            }
                                        }
                                    });

                        } else {
                            ParseContent.getParseContentInstance().showErrorMessage(activity,
                                    response.body().getErrorCode(), true);
                        }
                    } else {
                        Utilities.showHttpErrorToast(response.code(), activity);
                    }
                }

                @Override
                public void onFailure(Call<CountriesResponse> call, Throwable t) {
                    Utilities.printLog("RegisterLogin", t.getMessage());
                }
            });
        }
    }

    /**
     * this method call webservice fro get Delivery type Category list
     *
     * @param cityId selected cityId
     */
    private void getCategoryList(String cityId) {
        Utilities.showProgressDialog(activity);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.CITY_ID, cityId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Call<CategoriesResponse> deliveriesCall = ApiClient.getClient().create(ApiInterface.class)
                .getCategories(ApiClient.makeJSONRequestBody(jsonObject));
        deliveriesCall.enqueue(new Callback<CategoriesResponse>() {
            @Override
            public void onResponse(Call<CategoriesResponse> call, Response<CategoriesResponse>
                    response) {
                Utilities.removeProgressDialog();
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        categoryList = response.body().getCategories();
                    } else {
                        ParseContent.getParseContentInstance().showErrorMessage(activity,
                                response.body
                                        ().getErrorCode(), true);
                    }
                } else {
                    Utilities.showHttpErrorToast(response.code(), activity);
                }
            }

            @Override
            public void onFailure(Call<CategoriesResponse> call, Throwable t) {
                Utilities.printLog("RegisterLogin", t.getMessage());
            }
        });
    }

    private void setCountry(Object object) {
        Country country = (Country) object;
        if (!TextUtils.equals(countryId, country.getId())) {
            countryId = country.getId();
            Utilities.printLog(TAG, "country id" + countryId);
            countryCode = country.getCountryPhoneCode();
            tvCountryCode.setText(countryCode);
            tvCountry.setText(country.getCountryName());
            tvCountry.setTextColor(ContextCompat.getColor(activity, R.color
                    .color_app_text));
            maxPhoneNumberLength = country.getMaxPhoneNumberLength();
            minPhoneNumberLength = country.getMinPhoneNumberLength();
            setContactNoLength(maxPhoneNumberLength);
            tvCity.setText(activity.getResources().getString(R.string.text_select_city));
            tvCategory.setText(activity.getResources().getString(R.string.text_select_category));
            getCityList(countryId);
            etMobileNo.getText().clear();
            tvCategory.setText(activity.getResources().getString(R.string.text_select_category));
            etReferralCode.setEnabled(true);
            etReferralCode.getText().clear();
            ivSuccess.setVisibility(View.GONE);
            if (activity.preferenceHelper.getIsReferralOn() && country
                    .isReferralStore()) {
                llReferral.setVisibility(View.VISIBLE);
            } else {
                llReferral.setVisibility(View.GONE);
            }
        }


    }

    private void checkCountryCode(String country) {
        int countryListSize = countryList.size();
        for (int i = 0; i < countryListSize; i++) {
            if (countryList.get(i).getCountryName().toUpperCase().startsWith(country.toUpperCase
                    ())) {
                setCountry(countryList.get(i));
                return;
            }
        }
        setCountry(countryList.get(0));
    }

    private void setContactNoLength(int length) {
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(length);
        etMobileNo.setFilters(FilterArray);
    }

    public void clearError() {
        if (etName != null) {
            etName.setError(null);
            etEmail.setError(null);
            etPassword.setError(null);
            etAddress.setError(null);
            etMobileNo.setError(null);
            etSlogan.setError(null);
            etWebsite.setError(null);
            etLat.setError(null);
            etLng.setError(null);
        }
    }

    /**
     * this method call a webservice for check referral code enter by user
     */

    private void checkReferralCode() {
        Utilities.showCustomProgressDialog(activity, false);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.REFERRAL_CODE, etReferralCode.getText().toString()
                    .trim());
            jsonObject.put(Constant.COUNTRY_ID, countryId);
            jsonObject.put(Constant.TYPE, Constant.Type.STORE);
        } catch (JSONException e) {
            Utilities.handleException(RegisterFragment.class.getName(), e);
        }

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<IsSuccessResponse> responseCall = apiInterface.getCheckReferral(ApiClient
                .makeJSONRequestBody(jsonObject));
        responseCall.enqueue(new Callback<IsSuccessResponse>() {
            @Override
            public void onResponse(Call<IsSuccessResponse> call, Response<IsSuccessResponse>
                    response) {
                if (activity.parseContent.isSuccessful(response)) {
                    Utilities.hideCustomProgressDialog();
                    if (response.body().isSuccess()) {
                        tvReferralApply.setVisibility(View.GONE);
                        ivSuccess.setVisibility(View.VISIBLE);
                        etReferralCode.setEnabled(false);
                        referralCode = etReferralCode.getText().toString();
                    } else {
                        ParseContent.getParseContentInstance().showErrorMessage(activity,
                                response.body().getErrorCode(), false);
                        etReferralCode.getText().clear();
                    }

                }

            }

            @Override
            public void onFailure(Call<IsSuccessResponse> call, Throwable t) {
                Utilities.printLog(RegisterFragment.class.getName(), t + "");
            }
        });
    }


    public void updateUiForSocialLogin(String email, String socialId, String firstName, String
            lastName, Uri profileUri) {
        if (!TextUtils.isEmpty(email)) {
            etEmail.setText(email);
            etEmail.setEnabled(false);
            etEmail.setFocusableInTouchMode(false);
            activity.preferenceHelper.putIsEmailVerified(true);
        }
        this.socialId = socialId;
        etName.setText(firstName + " " + lastName);
        uri = profileUri;
        etPassword.setVisibility(View.GONE);
        etPasswordRetype.setVisibility(View.GONE);
        tilPassword.setVisibility(View.GONE);
        tilRetypePassword.setVisibility(View.GONE);

        if (uri != null) {
            Utilities.showCustomProgressDialog(activity, false);
            GlideApp.with(activity.getApplicationContext()).asBitmap()
                    .load(uri.toString())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                    Target<Bitmap> target,
                                                    boolean isFirstResource) {
                            Utilities.handleException(getClass().getSimpleName(), e);
                            Utilities.showToast(activity, e.getMessage());
                            Utilities.hideCustomProgressDialog();
                            return true;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, Object model,
                                                       Target<Bitmap> target,
                                                       DataSource dataSource,
                                                       boolean isFirstResource) {
                            currentPhotoPath = getImageFile(resource).getPath();
                            ivProfile.setImageBitmap(resource);
                            Utilities.hideCustomProgressDialog();
                            return true;
                        }
                    }).into(ivProfile);

        }

    }

    private File getImageFile(Bitmap bitmap) {

        File imageFile = new File(activity.getFilesDir(), "name.jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Utilities.handleException(getClass().getSimpleName(), e);
        }
        return imageFile;
    }

    private void checkSocialLoginISOn(boolean isSocialLogin) {
        if (isSocialLogin) {
            llSocialLogin.setVisibility(View.VISIBLE);
        } else {
            llSocialLogin.setVisibility(View.GONE);
        }
    }

    private void setStoreAddressORLocation() {
        Intent intent = new Intent(activity, StoreLocationActivity.class);
        startActivityForResult(intent, Constant.STORE_LOCATION_RESULT);
    }

    /**
     * this class will help to get current city or county according current location
     */
    private class GetCityAsyncTask extends AsyncTask<String, Void, Address> {

        double lat, lng;

        public GetCityAsyncTask(double lat, double lng) {
            this.lat = lat;
            this.lng = lng;
        }

        @Override
        protected Address doInBackground(String... params) {

            Geocoder geocoder = new Geocoder(activity, new Locale("en_US"));
            try {
                List<Address> addressList = geocoder.getFromLocation(lat,
                        lng, 1);
                if (addressList != null && !addressList.isEmpty()) {

                    Address address = addressList.get(0);
                    return address;
                }

            } catch (IOException e) {
                publishProgress();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            setCountry(countryList.get(0));
        }

        @Override
        protected void onPostExecute(Address address) {
            String countryName;
            if (address != null) {
                countryName = address.getCountryName();
                checkCountryCode(countryName);
            }


        }
    }

    private void addMultiLanguageDetail(String title, List<String> detailMap,
                                        @NonNull AddDetailInMultiLanguageDialog.SaveDetails saveDetails) {

        if (addDetailInMultiLanguageDialog != null && addDetailInMultiLanguageDialog.isShowing()) {
            return;
        }
        addDetailInMultiLanguageDialog = new AddDetailInMultiLanguageDialog(activity, title,
                saveDetails, detailMap, true);
        addDetailInMultiLanguageDialog.show();
    }
}
