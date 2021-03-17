package com.edelivery.store;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.edelivery.store.component.CustomAlterDialog;
import com.edelivery.store.models.responsemodel.IsSuccessResponse;
import com.edelivery.store.parse.ApiClient;
import com.edelivery.store.parse.ApiInterface;
import com.edelivery.store.parse.ParseContent;
import com.edelivery.store.utils.Constant;
import com.edelivery.store.utils.GlideApp;
import com.edelivery.store.utils.ImageHelper;
import com.edelivery.store.utils.Utilities;
import com.edelivery.store.widgets.CustomInputEditText;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;
import com.google.android.material.textfield.TextInputEditText;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddBankDetailActivity extends BaseActivity {

    public static final String TAG = AddBankDetailActivity.class.getName();

    private CustomInputEditText etAccountNumber, etAccountHolderName, etRoutingNumber,
            etPersonalIdNumber;
    private EditText etVerifyPassword, etAddress, etPostalCode;
    private Dialog dialog;
    private CustomTextView tvDob;
    private Uri uri;
    private ImageHelper imageHelper;
    private ImageView ivFrontDocumentImage, ivBackDocumentImage, ivAdditionDocumentImage;
    private RadioButton rbMale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bank_detail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbar(toolbar, R.drawable.ic_back, R.color.color_app_theme);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utilities.hideSoftKeyboard(AddBankDetailActivity.this);
                onBackPressed();
            }
        });
        ((TextView) findViewById(R.id.tvToolbarTitle)).setText(getString(R.string
                .text_bank_detail));
        initToolbarButton();
        imageHelper = new ImageHelper(this);
        etRoutingNumber = findViewById(R.id.etRoutingNumber);
        etAccountNumber = findViewById(R.id.etBankAccountNumber);
        etPersonalIdNumber = findViewById(R.id.etPersonalIdNumber);
        etAccountHolderName = findViewById(R.id.etAccountHolderName);
        tvDob = findViewById(R.id.tvDob);
        ivFrontDocumentImage = findViewById(R.id.ivFrontDocumentImage);
        ivBackDocumentImage = findViewById(R.id.ivBackDocumentImage);
        ivAdditionDocumentImage = findViewById(R.id.ivAdditionDocumentImage);
        tvDob.setOnClickListener(this);
        ivFrontDocumentImage.setOnClickListener(this);
        ivBackDocumentImage.setOnClickListener(this);
        ivAdditionDocumentImage.setOnClickListener(this);
        rbMale = findViewById(R.id.rbMale);
        etAddress = findViewById(R.id.etAddress);
        etPostalCode = findViewById(R.id.etPostalCode);

    }

    private void openDatePickerDialog() {

        final Calendar calendar = Calendar.getInstance();
        final int currentYear = calendar.get(Calendar.YEAR);
        final int currentMonth = calendar.get(Calendar.MONTH);
        final int currentDate = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog
                .OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                tvDob.setText((monthOfYear + 1) + "-" + dayOfMonth + "-" + year);
                tvDob.setTextColor(ResourcesCompat.getColor(getResources(), R.color
                        .color_app_text, null));
            }
        };
        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, onDateSetListener,
                currentYear,
                currentMonth,
                currentDate);
        // DOB for stripe we only allow 13 year ago date
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(Calendar.YEAR, calendar1.get(Calendar.YEAR) - 13);
        datePickerDialog.getDatePicker().setMaxDate(calendar1.getTimeInMillis());
        datePickerDialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        setToolbarEditIcon(false, 0);
        setToolbarCameraIcon(false);
        return true;
    }


    protected boolean isValidate() {
        String msg = null;
        if (TextUtils.isEmpty(etAccountHolderName.getText().toString().trim())) {
            msg = getString(R.string.msg_plz_account_name);
            etAccountHolderName.requestFocus();
            etAccountHolderName.setError(msg);
        } else if (TextUtils.isEmpty(etAccountNumber.getText().toString().trim())) {
            msg = getString(R.string.msg_plz_valid_account_number);
            etAccountNumber.requestFocus();
            etAccountNumber.setError(msg);
        } else if (TextUtils.isEmpty(etRoutingNumber.getText().toString().trim())) {
            msg = getString(R.string.msg_plz_valid_routing_number);
            etRoutingNumber.requestFocus();
            etRoutingNumber.setError(msg);
        } else if (TextUtils.isEmpty(etPersonalIdNumber.getText().toString().trim())) {
            msg = getString(R.string.msg_plz_valid_personal_id_number);
            etPersonalIdNumber.requestFocus();
            etPersonalIdNumber.setError(msg);
        } else if (TextUtils.equals(tvDob.getText().toString(), getResources().getString(R.string
                .text_dob))) {
            msg = getString(R.string.msg_add_dob);
            Utilities.showToast(this, msg);
        } else if (TextUtils.isEmpty(etAddress.getText().toString().trim())) {
            msg = getString(R.string.msg_valid_address);
            etAddress.requestFocus();
            etAddress.setError(msg);
        } else if (TextUtils.isEmpty(etPostalCode.getText().toString().trim())) {
            msg = getString(R.string.msg_valid_postal_code);
            etPostalCode.requestFocus();
            etPostalCode.setError(msg);
        } else if (ivFrontDocumentImage.getTag() == null) {
            msg = getString(R.string.text_upload_photo_id_front);
            Utilities.showToast(this, msg);
        } else if (ivBackDocumentImage.getTag() == null) {
            msg = getString(R.string.text_upload_photo_id_back);
            Utilities.showToast(this, msg);
        } else if (ivAdditionDocumentImage.getTag() == null) {
            msg = getString(R.string.text_upload_photo_additional);
            Utilities.showToast(this, msg);
        }
        return TextUtils.isEmpty(msg);
    }

    private void showVerificationDialog() {
        if (TextUtils.isEmpty(preferenceHelper.getSocialId())) {
            dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_account_verification);
            etVerifyPassword = (TextInputEditText) dialog.findViewById(R.id.etCurrentPassword);

            dialog.findViewById(R.id.btnPositive).setOnClickListener(this);
            dialog.findViewById(R.id.btnNegative).setOnClickListener(this);

            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            dialog.show();
        } else {
            addBankDetail("");
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {
            case R.id.btnPositive:
                if (!TextUtils.isEmpty(etVerifyPassword.getText().toString())) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    addBankDetail(etVerifyPassword.getText().toString());
                } else {
                    etVerifyPassword.setError(getString(R.string.msg_empty_password));
                }
                break;
            case R.id.btnNegative:
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                break;
            case R.id.ivFrontDocumentImage:
                showPhotoSelectionDialog(ivFrontDocumentImage);
                break;
            case R.id.ivBackDocumentImage:
                showPhotoSelectionDialog(ivBackDocumentImage);
                break;
            case R.id.ivAdditionDocumentImage:
                showPhotoSelectionDialog(ivAdditionDocumentImage);
                break;
            case R.id.tvDob:
                openDatePickerDialog();
                break;
        }
    }

    /**
     * this method call webservice for add or update bank detail;
     *
     * @param pass string
     */
    private void addBankDetail(String pass) {
        Utilities.showCustomProgressDialog(this, false);


        HashMap<String, RequestBody> hashMap = new HashMap<>();
        hashMap.put(Constant.BUSINESS_NAME, ApiClient.makeTextRequestBody("Store"));
        hashMap.put(Constant.BANK_HOLDER_ID, ApiClient.makeTextRequestBody(preferenceHelper
                .getStoreId()));
        hashMap.put(Constant.ACCOUNT_HOLDER_NAME,
                ApiClient.makeTextRequestBody(etAccountHolderName
                        .getText().toString().trim()));
        hashMap.put(Constant.BANK_ACCOUNT_HOLDER_NAME,
                ApiClient.makeTextRequestBody(etAccountHolderName
                        .getText().toString().trim()));
        hashMap.put(Constant.BANK_ACCOUNT_NUMBER,
                ApiClient.makeTextRequestBody(etAccountNumber
                        .getText().toString()));
        hashMap.put(Constant.BANK_PERSONAL_ID_NUMBER,
                ApiClient.makeTextRequestBody(etPersonalIdNumber
                        .getText().toString()));
        hashMap.put(Constant.BANK_ACCOUNT_HOLDER_TYPE,
                ApiClient.makeTextRequestBody(Constant.Bank.BANK_ACCOUNT_HOLDER_TYPE));
        hashMap.put(Constant.BANK_HOLDER_TYPE,
                ApiClient.makeTextRequestBody(Constant.TYPE_STORE));
        hashMap.put(Constant.BANK_ROUTING_NUMBER,
                ApiClient.makeTextRequestBody(etRoutingNumber
                        .getText().toString()));
        hashMap.put(Constant.DOB,
                ApiClient.makeTextRequestBody(tvDob.getText().toString()));
        hashMap.put(Constant.ADDRESS,
                ApiClient.makeTextRequestBody(etAddress.getText().toString()));
        hashMap.put(Constant.GENDER,
                ApiClient.makeTextRequestBody(rbMale.isChecked() ? "male" : "female"));
        hashMap.put(Constant.POSTAL_CODE,
                ApiClient.makeTextRequestBody(etPostalCode.getText().toString()));

        hashMap.put(Constant.SOCIAL_ID,
                ApiClient.makeTextRequestBody(preferenceHelper.getSocialId()));
        hashMap.put(Constant.PASS_WORD, ApiClient.makeTextRequestBody(pass));
        hashMap.put(Constant.SERVER_TOKEN, ApiClient.makeTextRequestBody(preferenceHelper
                .getServerToken()));
        hashMap.put(Constant.STORE_ID, ApiClient.makeTextRequestBody(preferenceHelper
                .getStoreId()));

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<IsSuccessResponse> responseCall;
        responseCall = apiInterface.addBankDetail(hashMap,
                ApiClient.makeMultipartRequestBody(this,
                        (String) ivFrontDocumentImage.getTag(R.drawable.placeholder),
                        "front"),
                ApiClient.makeMultipartRequestBody(this,
                        (String) ivBackDocumentImage.getTag(R.drawable.placeholder),
                        "back"),
                ApiClient.makeMultipartRequestBody(this,
                        (String) ivAdditionDocumentImage.getTag(R.drawable.placeholder),
                        "additional"));
        responseCall.enqueue(new Callback<IsSuccessResponse>() {
            @Override
            public void onResponse(Call<IsSuccessResponse> call, Response<IsSuccessResponse>
                    response) {
                if (parseContent.isSuccessful(response)) {
                    Utilities.hideCustomProgressDialog();
                    if (response.body().isSuccess()) {
                        setResult(Activity.RESULT_OK);
                        finish();
                    } else {
                        if (TextUtils.isEmpty(response.body().getStripeError())) {
                            ParseContent.getParseContentInstance().showErrorMessage
                                    (AddBankDetailActivity.this, response.body().getErrorCode(),
                                            false);
                        } else {
                            openBankDetailErrorDialog(response.body().getStripeError());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<IsSuccessResponse> call, Throwable t) {
                Utilities.hideCustomProgressDialog();
                Utilities.handleThrowable(TAG, t);
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void openBankDetailErrorDialog(String message) {
        CustomAlterDialog customExitDialog = new CustomAlterDialog(this, getResources().getString
                (R.string.text_error),
                message, false,
                getResources().getString(R.string.text_ok),
                getResources().getString(R.string.text_cancel)) {
            @Override
            public void btnOnClick(int btnId) {
                dismiss();
            }
        };
        customExitDialog.show();
    }

    private void showPhotoSelectionDialog(ImageView imageView) {
        tvDob.setTag(imageView);
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
            /*Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);*/
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
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                case Constant.PERMISSION_CHOOSE_PHOTO:
                    if (data != null) {
                        uri = data.getData();
                        beginCrop(uri);
                    }
                    break;

                case Constant.PERMISSION_TAKE_PHOTO:
                    if (uri != null) {
                        beginCrop(uri);
                    }
                    break;
                case Crop.REQUEST_CROP:
                    setImage(Crop.getOutput(data), (ImageView) tvDob.getTag());
                    break;
                default:
                    break;
            }
        }
    }

    private void setImage(Uri uri, ImageView imageView) {
        File file = ImageHelper.getFromMediaUriPfd(this,
                this.getContentResolver(), uri);
        if (file != null) {
            GlideApp.with(this).load(uri).error(R
                    .drawable.icon_default_profile).into(imageView);
            imageView.setTag(R.drawable.placeholder, file.getPath());
        }
    }

    /**
     * This method is used for crop the placeholder which selected or captured
     */
    public void beginCrop(Uri sourceUri) {

        Uri outputUri = Uri.fromFile(imageHelper.createImageFile());
        Crop.of(sourceUri, outputUri).start(this);
    }

    private void initToolbarButton() {
        CustomTextView tvtoolbarbtn = (CustomTextView) findViewById(R.id.tvtoolbarbtn);
        tvtoolbarbtn.setText(getString(R.string.text_save));
        tvtoolbarbtn.setOnClickListener(v -> {
            if (isValidate()) {
                showVerificationDialog();
            }
        });
        tvtoolbarbtn.setVisibility(View.VISIBLE);
    }
}
