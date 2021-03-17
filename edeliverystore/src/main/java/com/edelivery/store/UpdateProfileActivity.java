package com.edelivery.store;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.edelivery.store.component.AddDetailInMultiLanguageDialog;
import com.edelivery.store.component.CustomAlterDialog;
import com.edelivery.store.component.CustomEditTextDialog;
import com.edelivery.store.models.datamodel.StoreData;
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
import com.edelivery.store.utils.PreferenceHelper;
import com.edelivery.store.utils.Utilities;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONArray;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.elluminati.edelivery.store.BuildConfig.IMAGE_URL;

public class UpdateProfileActivity extends BaseActivity {

    private String TAG = "UpdateProfileActivity";
    private EditText etName, etEmail, etNewPassword, etConfirmNewPassword, etMobileNo,
            etVerifyPassword;
    private Uri uri;
    private RoundedImageView ivProfile;
    private TextView tvCountryCode;
    private Dialog dialog;
    private CustomTextView tvChangePassword;
    private LinearLayout llChangePassword;
    private CustomEditTextDialog accountVerifyDialog;
    private ScrollView scrollView;
    private ImageHelper imageHelper;
    private String currentPhotoPath;
    private AddDetailInMultiLanguageDialog addDetailInMultiLanguageDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        Utilities.printLog("---updateProfile", "onCreate");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbar(toolbar, R.drawable.ic_back, R.color.color_app_theme);
        ((TextView) findViewById(R.id.tvToolbarTitle)).setText(getString(R.string.text_profile));
        initToolbarButton();
        etName = (EditText) findViewById(R.id.etName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etNewPassword = (EditText) findViewById(R.id.etNewPassword);
        etConfirmNewPassword = (EditText) findViewById(R.id.etConfirmNewPassword);
        etMobileNo = (EditText) findViewById(R.id.etMobileNo);

        tvCountryCode = (TextView) findViewById(R.id.tvCountryCode);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        ivProfile = (RoundedImageView) findViewById(R.id.ivProfile);
        llChangePassword = (LinearLayout) findViewById(R.id.llChangePassword);
        tvChangePassword = (CustomTextView) findViewById(R.id.tvChangePassword);
        tvChangePassword.setOnClickListener(this);
        findViewById(R.id.ivProfileImageSelect).setOnClickListener(this);
        imageHelper = new ImageHelper(this);
        etName.setOnClickListener(this);

        setEnableView(false);
        getStoreData();
        updateUiForSocial();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {
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
            case R.id.ivProfileImageSelect:
                if (isEditable)
                    showPhotoSelectionDialog();
                break;

            case R.id.btnRegister:
                validate();
                break;

            case R.id.btnPositive:
                if (!TextUtils.isEmpty(etVerifyPassword.getText().toString())) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    updateProfile();
                } else {
                    etVerifyPassword.setError(getString(R.string.msg_empty_password));
                }
                break;
            case R.id.btnNegative:
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                break;
            case R.id.tvChangePassword:
                if (llChangePassword.getVisibility() == View.VISIBLE) {
                    llChangePassword.setVisibility(View.GONE);
                } else {
                    llChangePassword.setVisibility(View.VISIBLE);
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(View.FOCUS_DOWN);
                        }
                    });
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        setToolbarEditIcon(false, 0);
        setToolbarCameraIcon(false);
        return true;
    }

    private void initToolbarButton() {
        CustomTextView tvtoolbarbtn = (CustomTextView) findViewById(R.id.tvtoolbarbtn);
        tvtoolbarbtn.setOnClickListener(v -> {
            if (!isEditable) {
                isEditable = true;
                setEnableView(true);
                tvtoolbarbtn.setText(R.string.text_save);
            } else {
                validate();
            }
        });
        tvtoolbarbtn.setVisibility(View.VISIBLE);
        tvtoolbarbtn.setText(R.string.text_edit);
    }

    private void setEnableView(boolean isEnable) {
        etName.setEnabled(isEnable);
        etEmail.setEnabled(isEnable);
        etMobileNo.setEnabled(isEnable);
        etNewPassword.setEnabled(isEnable);
        etConfirmNewPassword.setEnabled(isEnable);
        tvCountryCode.setEnabled(isEnable);
        tvChangePassword.setEnabled(isEnable);

//        etName.setFocusableInTouchMode(isEnable);
        etMobileNo.setFocusableInTouchMode(isEnable);

        etNewPassword.setFocusableInTouchMode(isEnable);
        etConfirmNewPassword.setFocusableInTouchMode(isEnable);
        tvCountryCode.setFocusableInTouchMode(isEnable);
        tvChangePassword.setFocusableInTouchMode(isEnable);

        if (TextUtils.isEmpty(preferenceHelper.getSocialId())) {
            etEmail.setEnabled(isEnable);
            etEmail.setFocusableInTouchMode(isEnable);
        } else {
            etEmail.setEnabled(false);
            etEmail.setFocusableInTouchMode(false);
        }
    }

    /**
     * this method call webservice for get Store Detail data
     */
    private void getStoreData() {
        Utilities.showProgressDialog(this);
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put(Constant.STORE_ID, ApiClient.makeTextRequestBody(
                PreferenceHelper.getPreferenceHelper(this).getStoreId()));
        map.put(Constant.SERVER_TOKEN, ApiClient.makeTextRequestBody(PreferenceHelper
                .getPreferenceHelper(this).getServerToken()));
        Call<StoreDataResponse> call = ApiClient.getClient().create(ApiInterface.class)
                .getStoreDate(map);

        call.enqueue(new Callback<StoreDataResponse>() {
            @Override
            public void onResponse(Call<StoreDataResponse> call, Response<StoreDataResponse>
                    response) {
                Utilities.removeProgressDialog();
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        preferenceHelper.putIsStoreCanCreateGroup(response.body().getStoreDetail().
                                getDeliveryDetails().isStoreCanCreateGroup());
                        setStoreData(response.body().getStoreDetail());
                    } else {
                        ParseContent.getParseContentInstance().showErrorMessage
                                (UpdateProfileActivity.this, response.body().getErrorCode(), true);
                    }
                } else {
                    Utilities.showHttpErrorToast(response.code(), UpdateProfileActivity.this);
                }
            }

            @Override
            public void onFailure(Call<StoreDataResponse> call, Throwable t) {
                Utilities.printLog(TAG, t.getMessage());
            }
        });
    }

    private void setStoreData(StoreData storeData) {

        if (storeData != null) {
            etName.setText(storeData.getName());
            etName.setTag(storeData.getNameList());
            etEmail.setText(storeData.getEmail());
            etMobileNo.setText(storeData.getPhone());
            tvCountryCode.setText(storeData.getCountryPhoneCode());
            GlideApp.with(this).load(IMAGE_URL + storeData.getImageUrl()).into(ivProfile);
            setContactNoLength(preferenceHelper.getMaxPhoneNumberLength());
        }
    }

    private void setContactNoLength(int length) {
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(length);
        etMobileNo.setFilters(FilterArray);
    }

    private void validate() {

        if (TextUtils.isEmpty(etName.getText().toString())) {
            etName.setError(this.getResources().getString(R.string.msg_empty_name));
        } else if (TextUtils.isEmpty(etEmail.getText().toString())) {
            etEmail.setError(this.getResources().getString(R.string.msg_empty_email));
        } else if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()) {
            etEmail.setError(this.getResources().getString(R.string.msg_valid_email));
        } else if (TextUtils.isEmpty(etMobileNo.getText().toString())) {
            etMobileNo.setError(this.getResources().getString(R.string.msg_empty_mobileNo));
        } else if (etMobileNo.getText().toString().trim().length()
                > preferenceHelper.getMaxPhoneNumberLength() || etMobileNo.getText()
                .toString().trim()
                .length
                        () < preferenceHelper.getMinPhoneNumberLength()) {
            String msg = getResources().getString(R.string
                    .msg_please_enter_valid_mobile_number_length)
                    + " " +
                    "" + preferenceHelper.getMinPhoneNumberLength() + getResources().getString(R
                    .string
                    .text_or)
                    + preferenceHelper.getMaxPhoneNumberLength() + " " + getResources().getString
                    (R.string
                            .text_digits);
            etMobileNo.setError(msg);
            etMobileNo.requestFocus();
        } else if (PreferenceHelper.getPreferenceHelper(this).isStoreProfilePicRequired() &&
                uri == null) {
            new CustomAlterDialog(this, null, getString(R.string.msg_empty_profile)) {
                @Override
                public void btnOnClick(int btnId) {
                    this.dismiss();
                }
            }.show();
        } else if (tvChangePassword.getVisibility() == View.VISIBLE && !TextUtils.isEmpty
                (etNewPassword.getText()
                        .toString().trim())
                && etNewPassword
                .getText
                        ().toString().trim().length() < 6) {
            etNewPassword.setError(getString(R.string.msg_password_length));
            etNewPassword.requestFocus();
        } else if (tvChangePassword.getVisibility() == View.VISIBLE && !etNewPassword.getText()
                .toString().trim().equalsIgnoreCase(etConfirmNewPassword
                        .getText().toString().trim())) {
            etConfirmNewPassword.setError(getString(R.string.msg_mismatch_password));
        } else {
            checkForOtp();
        }


    }

    private void checkForOtp() {
        if (checkProfileWitchOtpValidationON() != -1) {
            getOtp();
        } else {
            showVerificationDialog();
        }
    }

    /**
     * this method will manage which otp validation is on from admin panel
     *
     * @return get code according for validation
     */
    private int checkProfileWitchOtpValidationON() {
        if (checkEmailVerification() && checkPhoneNumberVerification()) {

            return Constant.SMS_AND_EMAIL_VERIFICATION_ON;

        } else if (checkPhoneNumberVerification()) {
            return Constant.SMS_VERIFICATION_ON;
        } else if (checkEmailVerification()) {
            return Constant.EMAIL_VERIFICATION_ON;
        }
        return -1;

    }

    private boolean checkPhoneNumberVerification() {
        return preferenceHelper.getIsVerifyPhone() && !TextUtils.equals(etMobileNo
                .getText().toString(), preferenceHelper.getPhone());

    }

    private boolean checkEmailVerification() {
        return preferenceHelper.getIsVerifyEmail() && !TextUtils.equals(etEmail
                .getText().toString(), preferenceHelper.getEmail());

    }

    /**
     * this method called webservice for get OTP for mobile or email
     */

    private void getOtp() {
        Utilities.showCustomProgressDialog(this, false);
        Utilities.printLog("getOtp", "in to otp");
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put(Constant.ID, ApiClient.makeTextRequestBody(
                preferenceHelper.getStoreId()));
        switch (checkProfileWitchOtpValidationON()) {
            case Constant.SMS_AND_EMAIL_VERIFICATION_ON:
                map.put(Constant.EMAIL, ApiClient.makeTextRequestBody(
                        etEmail.getText().toString()));
                map.put(Constant.PHONE, ApiClient.makeTextRequestBody(String.valueOf(etMobileNo
                        .getText().toString())));
                break;
            case Constant.SMS_VERIFICATION_ON:
                map.put(Constant.PHONE, ApiClient.makeTextRequestBody(String.valueOf(etMobileNo
                        .getText().toString())));
                break;
            case Constant.EMAIL_VERIFICATION_ON:
                map.put(Constant.EMAIL, ApiClient.makeTextRequestBody(etEmail.getText().toString
                        ()));
                break;
            default:
                // do with default
                break;
        }
        map.put(Constant.TYPE, ApiClient.makeTextRequestBody(
                String.valueOf(Constant.Type.STORE)));
        map.put(Constant.COUNTRY_CODE, ApiClient.makeTextRequestBody(preferenceHelper
                .getCountryPhoneCode()));


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
                        ParseContent.getParseContentInstance().showErrorMessage
                                (UpdateProfileActivity.this, response.body().getErrorCode(), true);
                        //getOtp();
                    }
                } else {
                    Utilities.showHttpErrorToast(response.code(), UpdateProfileActivity.this);
                }

            }

            @Override
            public void onFailure(Call<OTPResponse> call, Throwable t) {
                Utilities.printLog("getOtp", t.getMessage());
            }
        });

    }

    private void openAccountVerifyDialog(final String otpForSms, final String otpForEmail) {


        if (accountVerifyDialog != null && accountVerifyDialog.isShowing()) {
            return;
        }
        final int otpType = checkProfileWitchOtpValidationON();
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
            accountVerifyDialog = new CustomEditTextDialog(this, getString(R.string
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
                                        preferenceHelper.putIsEmailVerified(true);
                                        preferenceHelper.putIsPhoneNumberVerified(true);
                                        dismiss();
                                        showVerificationDialog();
                                    } else {
                                        Utilities.showToast(UpdateProfileActivity.this, getString
                                                (R.string.msg_otp_wrong));
                                    }
                                }
                                break;
                            case Constant.SMS_VERIFICATION_ON:
                                if (TextUtils.isEmpty(etSMSOtp.getText())) {
                                    etSMSOtp.setError(getString(R.string.msg_invalid_data));
                                } else {
                                    if (TextUtils.equals(otpForSms, etSMSOtp.getText().toString()
                                            .trim())) {
                                        preferenceHelper.putIsPhoneNumberVerified(true);
                                        dismiss();
                                        showVerificationDialog();
                                    } else {
                                        Utilities.showToast(UpdateProfileActivity.this, getString
                                                (R.string.msg_otp_wrong));
                                    }
                                }
                                break;
                            case Constant.EMAIL_VERIFICATION_ON:

                                if (TextUtils.isEmpty(etEmailOtp.getText())) {
                                    etEmailOtp.setError(getString(R.string.msg_invalid_data));
                                } else {
                                    if (TextUtils.equals(otpForEmail, etEmailOtp.getText()
                                            .toString().trim())) {
                                        preferenceHelper.putIsEmailVerified(true);
                                        dismiss();
                                        showVerificationDialog();
                                    } else {
                                        Utilities.showToast(UpdateProfileActivity.this, getString
                                                (R.string.msg_otp_wrong));
                                    }
                                }

                                break;
                        }
                    } else {
                        dismiss();
                        finish();
                    }
                }
            };
            accountVerifyDialog.setCancelable(false);
            accountVerifyDialog.show();
        }

    }

    private void showVerificationDialog() {

        if (TextUtils.isEmpty(preferenceHelper.getSocialId())) {
            if (dialog == null) {
                dialog = new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_account_verification);
                etVerifyPassword = (TextInputEditText) dialog.findViewById(R.id.etCurrentPassword);

                dialog.findViewById(R.id.btnPositive).setOnClickListener(this);
                dialog.findViewById(R.id.btnNegative).setOnClickListener(this);

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog1) {
                        dialog = null;
                    }
                });
                WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                params.width = WindowManager.LayoutParams.MATCH_PARENT;
                dialog.show();
            }
        } else {
            updateProfile();
        }


    }

    /**
     * this method call a webservice for update profile data
     */
    private void updateProfile() {
        Utilities.showProgressDialog(this);
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put(Constant.STORE_ID, ApiClient.makeTextRequestBody(
                PreferenceHelper.getPreferenceHelper(this).getStoreId()));
        map.put(Constant.SERVER_TOKEN, ApiClient.makeTextRequestBody(PreferenceHelper
                .getPreferenceHelper(this).getServerToken()));
        map.put(Constant.NAME, ApiClient.makeTextRequestBody(
                new JSONArray((List<String>) etName.getTag())));
        map.put(Constant.EMAIL, ApiClient.makeTextRequestBody(
                etEmail.getText().toString().trim().toLowerCase()));

        map.put(Constant.PHONE, ApiClient.makeTextRequestBody(
                etMobileNo.getText().toString().trim()));


        if (TextUtils.isEmpty(preferenceHelper.getSocialId())) {
            map.put(Constant.CURRENT_PASS_WORD, ApiClient.makeTextRequestBody(etVerifyPassword
                    .getText().toString()));
            map.put(Constant.NEW_PASS_WORD, ApiClient.makeTextRequestBody(etNewPassword.getText()
                    .toString()));
            map.put(Constant.SOCIAL_ID, ApiClient.makeTextRequestBody(""));
            map.put(Constant.LOGIN_BY, ApiClient.makeTextRequestBody
                    (Constant.MANUAL));

        } else {
            map.put(Constant.CURRENT_PASS_WORD, ApiClient.makeTextRequestBody(""));
            map.put(Constant.NEW_PASS_WORD, ApiClient.makeTextRequestBody(""));
            map.put(Constant.SOCIAL_ID, ApiClient.makeTextRequestBody(preferenceHelper
                    .getSocialId()));
            map.put(Constant.LOGIN_BY, ApiClient.makeTextRequestBody
                    (Constant.SOCIAL));
        }
        map.put(Constant.IS_PHONE_NUMBER_VERIFIED, ApiClient.makeTextRequestBody(String.valueOf
                (preferenceHelper
                        .isPhoneNumberVerified())));

        map.put(Constant.IS_EMAIL_VERIFIED, ApiClient.makeTextRequestBody(String.valueOf
                (preferenceHelper.isEmailVerified())));

        Call<StoreDataResponse> call;

        if (TextUtils.isEmpty(currentPhotoPath)) {
            call = ApiClient.getClient().create(ApiInterface.class).updateProfile(map);
        } else {
            call = ApiClient.getClient().create(ApiInterface.class).updateProfile(map,
                    ApiClient.makeMultipartRequestBody(this, currentPhotoPath, Constant
                            .IMAGE_URL));
        }

        call.enqueue(new Callback<StoreDataResponse>() {
            @Override
            public void onResponse(Call<StoreDataResponse> call, Response<StoreDataResponse>
                    response) {
                Utilities.removeProgressDialog();
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        parseContent.parseStoreData(response.body());
                        setEnableView(false);
                        isEditable = false;
                        ParseContent.getParseContentInstance().showMessage(UpdateProfileActivity
                                        .this,
                                response.body().getMessage());
                        onBackPressed();
                    } else {
                        ParseContent.getParseContentInstance().showErrorMessage
                                (UpdateProfileActivity.this, response.body().getErrorCode(), true);
                    }
                } else {
                    Utilities.showHttpErrorToast(response.code(), UpdateProfileActivity.this);
                }
            }

            @Override
            public void onFailure(Call<StoreDataResponse> call, Throwable t) {
                Utilities.printLog(TAG, t.getMessage());


            }
        });
    }

    private void showPhotoSelectionDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
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
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission
                .READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat
                .checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission
                    .READ_EXTERNAL_STORAGE}, Constant.PERMISSION_CHOOSE_PHOTO);
        } else {
/*            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            startActivityForResult(galleryIntent, Constant.PERMISSION_CHOOSE_PHOTO);*/
            Intent galleryIntent = new Intent();
            galleryIntent.setType("image/*");
            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(galleryIntent, Constant.PERMISSION_CHOOSE_PHOTO);
        }
    }

    private void takePhotoFromCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager
                .PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission
                    .CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constant
                    .PERMISSION_TAKE_PHOTO);
        } else {
            takePhotoFromCamera();
        }
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = imageHelper.createImageFile();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(this, getPackageName(), file);
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

            default:
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
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
                default:
                    break;
            }
        }
    }

    private void setImage(final Uri uri) {

        currentPhotoPath = ImageHelper.getFromMediaUriPfd(this, getContentResolver
                (), uri).getPath();
        new ImageCompression(this).setImageCompressionListener(new ImageCompression
                .ImageCompressionListener() {
            @Override
            public void onImageCompression(String compressionImagePath) {
                currentPhotoPath = compressionImagePath;
                GlideApp.with(UpdateProfileActivity.this).load(uri)
                        .error(R
                                .drawable.icon_default_profile).into(ivProfile);
            }
        }).execute(currentPhotoPath);
    }

    private void updateUiForSocial() {
        if (TextUtils.isEmpty(preferenceHelper.getSocialId())) {
            tvChangePassword.setVisibility(View.VISIBLE);
        } else {
            tvChangePassword.setVisibility(View.GONE);
        }
    }

    private void addMultiLanguageDetail(String title, List<String> detailMap,
                                        @NonNull AddDetailInMultiLanguageDialog.SaveDetails saveDetails) {

        if (addDetailInMultiLanguageDialog != null && addDetailInMultiLanguageDialog.isShowing()) {
            return;
        }
        addDetailInMultiLanguageDialog = new AddDetailInMultiLanguageDialog(this, title,
                saveDetails, detailMap, true);
        addDetailInMultiLanguageDialog.show();
    }
}
