package com.edelivery.store;

import android.annotation.TargetApi;
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
import com.google.android.material.textfield.TextInputLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.edelivery.store.adapter.DocumentAdapter;
import com.edelivery.store.models.datamodel.Documents;
import com.edelivery.store.models.responsemodel.AllDocumentsResponse;
import com.edelivery.store.models.responsemodel.DocumentResponse;
import com.edelivery.store.parse.ApiClient;
import com.edelivery.store.parse.ApiInterface;
import com.edelivery.store.parse.ParseContent;
import com.edelivery.store.utils.ClickListener;
import com.edelivery.store.utils.Constant;
import com.edelivery.store.utils.GlideApp;
import com.edelivery.store.utils.ImageCompression;
import com.edelivery.store.utils.ImageHelper;
import com.edelivery.store.utils.PreferenceHelper;
import com.edelivery.store.utils.RecyclerTouchListener;
import com.edelivery.store.utils.Utilities;
import com.edelivery.store.widgets.CustomButton;
import com.edelivery.store.widgets.CustomEditText;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.elluminati.edelivery.store.BuildConfig.IMAGE_URL;


public class DocumentActivity extends BaseActivity {

    private String filePath, TAG = "DocumentActivity";
    private Dialog documentDialog;
    private Uri uri;
    private ImageView ivDocumentImage;
    private TextInputLayout tilExpireDate, tilNumberId;
    private CustomEditText etIdNumber, etExpireDate;
    private CustomTextView tvDocumentTitle;
    private RecyclerView rcvDocument;
    private CustomButton btnDocumentSubmit;
    private String expireDate;
    private List<Documents> documentList;
    private boolean isApplicationStart;
    private DocumentAdapter documentAdapter;
    private PreferenceHelper preferenceHelper;
    private ImageHelper imageHelper;
    private String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);
        imageHelper = new ImageHelper(this);
        preferenceHelper = PreferenceHelper.getPreferenceHelper(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbar(toolbar, R.drawable.ic_back, R.color.color_app_theme);
        ((TextView) findViewById(R.id.tvToolbarTitle)).setText(getString(R.string
                .text_document));
        rcvDocument = (RecyclerView) findViewById(R.id.rcvDocument);
        btnDocumentSubmit = (CustomButton) findViewById(R.id.btnDocumentSubmit);

        btnDocumentSubmit.setOnClickListener(this);
        getAllDocument();
        documentList = new ArrayList<>();
        getExtraData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        setToolbarCameraIcon(false);
        setToolbarEditIcon(false, 0);
        return true;
    }

    @Override
    public void onClick(View view) {
        //do something
        switch (view.getId()) {
            case R.id.btnDocumentSubmit:
                submitDocument();
                break;
            default:
                // do with default
                break;
        }

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
        /*    Intent galleryIntent = new Intent(Intent.ACTION_PICK,
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

        switch (requestCode) {

            case Constant.PERMISSION_CHOOSE_PHOTO:
                if (data != null) {
                    uri = data.getData();
                    setWithOutCropImage(uri);
                }
                break;

            case Constant.PERMISSION_TAKE_PHOTO:
                if (uri != null && resultCode == RESULT_OK) {
                    setWithOutCropImage(uri);
                }
                break;


            default:
                break;
        }
    }

    private void setWithOutCropImage(final Uri source) {
        if (documentDialog != null && documentDialog.isShowing()) {
            currentPhotoPath = ImageHelper.getFromMediaUriPfd(this, getContentResolver
                    (), source).getPath();
            new ImageCompression(this).setImageCompressionListener(new ImageCompression
                    .ImageCompressionListener() {
                @Override
                public void onImageCompression(String compressionImagePath) {
                    currentPhotoPath = compressionImagePath;
                    GlideApp.with(DocumentActivity.this).load(source)
                            .into(ivDocumentImage);
                }
            }).execute(currentPhotoPath);
        }
    }


    private void openDocumentUploadDialog(final int position) {


        if (documentDialog != null && documentDialog.isShowing()) {
            return;
        }

        final Documents document = documentList.get(position);

        documentDialog = new Dialog(this);
        documentDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        documentDialog.setContentView(R.layout.dialog_document_upload);
        ivDocumentImage = (ImageView) documentDialog.findViewById(R.id.ivDialogDocumentImage);
        etIdNumber = (CustomEditText) documentDialog.findViewById(R.id
                .etIdNumber);
        etExpireDate = (CustomEditText) documentDialog.findViewById(R.id.etExpireDate);
        tilExpireDate = (TextInputLayout) documentDialog.findViewById(R.id.tilExpireDate);
        tilNumberId = (TextInputLayout) documentDialog.findViewById(R.id.tilNumberId);
        tvDocumentTitle = (CustomTextView) documentDialog.findViewById(
                R.id.tvDialogDocumentTitle);
        tvDocumentTitle.setText(document.getDocumentDetails().getDocumentName());
        GlideApp.with(this).load(IMAGE_URL + document.getImageUrl())
                .dontAnimate().placeholder
                (ResourcesCompat.getDrawable(this
                        .getResources(), R.drawable.uploading, null)).fallback(ResourcesCompat
                .getDrawable
                        (this.getResources(), R.drawable.uploading, null)).into
                (ivDocumentImage);
        expireDate = "";
        if (document.getDocumentDetails().isIsExpiredDate()) {
            tilExpireDate.setVisibility(View.VISIBLE);
            String date = "";
            try {
                if (!TextUtils.isEmpty(document.getExpiredDate())) {
                    expireDate = document.getExpiredDate();
                    date = parseContent.dateFormat.format(parseContent
                            .webFormat
                            .parse(document.getExpiredDate()));
                    etExpireDate.setText(date);
                } else {
                    etExpireDate.setText(date);
                }

            } catch (ParseException e) {
                Utilities.printLog(DocumentAdapter.class.getName(), e.toString());
            }
        } else {
            tilExpireDate.setVisibility(View.GONE);
        }
        if (document.getDocumentDetails().isIsUniqueCode()) {
            tilNumberId.setVisibility(View.VISIBLE);
            etIdNumber.setText(document.getUniqueCode());
        } else {
            tilNumberId.setVisibility(View.GONE);
        }

        documentDialog.findViewById(R.id.btnPositive).setOnClickListener(new View
                .OnClickListener() {

            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(etExpireDate.getText().toString()) && document
                        .getDocumentDetails().isIsExpiredDate()) {
                    Utilities.showToast(DocumentActivity.this, getResources().getString(R.string
                            .msg_plz_enter_document_expire_date));

                } else if (TextUtils.isEmpty(etIdNumber.getText().toString().trim()) &&
                        document.getDocumentDetails().isIsUniqueCode()) {
                    etIdNumber.setError(getResources().getString(R.string
                            .msg_plz_enter_document_unique_code));
//                    Utils.showToast(getResources().getString(R.string
//                            .msg_plz_enter_document_unique_code), DocumentActivity.this);

                } else if (TextUtils.isEmpty(document.getImageUrl()) && TextUtils.isEmpty(currentPhotoPath)) {
                    Utilities.showToast(DocumentActivity.this, getResources().getString(R.string
                            .msg_plz_select_document_image));
                } else {
                    documentDialog.dismiss();
                    documentUpload(position);
                }


            }
        });

        documentDialog.findViewById(R.id.btnNegative).setOnClickListener(new View
                .OnClickListener() {


            @Override
            public void onClick(View view) {
                documentDialog.dismiss();
            }
        });

        ivDocumentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPhotoSelectionDialog();
            }
        });
        etExpireDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePickerDialog();
            }
        });
        WindowManager.LayoutParams params = documentDialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        documentDialog.getWindow().setAttributes(params);
        documentDialog.setCancelable(false);
        documentDialog.show();

    }

    private void openDatePickerDialog() {


        final Calendar calendar = Calendar.getInstance();
        final int currentYear = calendar.get(Calendar.YEAR);
        final int currentMonth = calendar.get(Calendar.MONTH);
        final int currentDate = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog
                .OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            }
        };
        final DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                onDateSetListener, currentYear,
                currentMonth,
                currentDate);
        datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, this
                .getResources()
                .getString(R.string.text_select), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (documentDialog != null && datePickerDialog.isShowing()) {
                    calendar.set(Calendar.YEAR, datePickerDialog.getDatePicker().getYear());
                    calendar.set(Calendar.MONTH, datePickerDialog.getDatePicker().getMonth());
                    calendar.set(Calendar.DAY_OF_MONTH, datePickerDialog.getDatePicker()
                            .getDayOfMonth());
                    etExpireDate.setText(parseContent.dateFormat.format(calendar.getTime()));
                    expireDate = parseContent.webFormat.format(calendar.getTime());
                }
            }
        });
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();

    }

    private void getAllDocument() {
        Utilities.showCustomProgressDialog(this, false);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.ID, preferenceHelper.getStoreId());
            jsonObject.put(Constant.TYPE, Constant.TYPE_STORE);
            jsonObject.put(Constant.SERVER_TOKEN, preferenceHelper.getServerToken());
        } catch (JSONException e) {
            Utilities.handleException(DocumentActivity.class.getName(), e);
        }
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<AllDocumentsResponse> responseCall = apiInterface.getAllDocument(ApiClient
                .makeJSONRequestBody(jsonObject));
        responseCall.enqueue(new Callback<AllDocumentsResponse>() {
            @Override
            public void onResponse(Call<AllDocumentsResponse> call, Response<AllDocumentsResponse>
                    response) {
                if (parseContent.isSuccessful(response)) {
                    Utilities.hideCustomProgressDialog();
                    Utilities.printLog("ALL_DOCUMENT", ApiClient.JSONResponse(response.body()));
                    if (response.body().isSuccess()) {
                        documentList.addAll(response.body().getDocuments());
                        preferenceHelper.putIsUserAllDocumentsUpload(response.body()
                                .isDocumentUploaded());
                        initRcvDocument();
                        if (documentList.isEmpty()) {
                            preferenceHelper.putIsUserAllDocumentsUpload(true);
                            submitDocument();
                        }

                    } else {
                        ParseContent.getParseContentInstance().showErrorMessage(DocumentActivity
                                        .this,
                                response.body().getErrorCode(), false);
                    }
                    btnDocumentSubmit.setVisibility(documentList.isEmpty() ? View.GONE : View
                            .VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<AllDocumentsResponse> call, Throwable t) {

            }
        });
    }

    private void initRcvDocument() {
        rcvDocument.setLayoutManager(new LinearLayoutManager(this));
        documentAdapter = new DocumentAdapter(documentList);
        rcvDocument.setAdapter(documentAdapter);
        rcvDocument.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration
                .VERTICAL));
        rcvDocument.addOnItemTouchListener(new RecyclerTouchListener(this, rcvDocument, new
                ClickListener() {


                    @Override
                    public void onClick(View view, int position) {
                        openDocumentUploadDialog(position);
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));
    }


    /**
     * this method call webservice for upload a document
     *
     * @param position
     */
    private void documentUpload(final int position) {
        Utilities.showCustomProgressDialog(this, false);
        HashMap<String, RequestBody> hashMap = new HashMap<>();
        hashMap.put(Constant.ID, ApiClient.makeTextRequestBody
                (preferenceHelper.getStoreId()));
        if (documentList.get(position).getDocumentDetails().isIsExpiredDate()) {
            hashMap.put(Constant.EXPIRED_DATE, ApiClient.makeTextRequestBody
                    (expireDate));
        }
        hashMap.put(Constant.DOCUMENT_ID, ApiClient.makeTextRequestBody
                (documentList.get(position).getId()));
        hashMap.put(Constant.TYPE, ApiClient.makeTextRequestBody
                (Constant.TYPE_STORE));
        hashMap.put(Constant.UNIQUE_CODE, ApiClient.makeTextRequestBody
                (etIdNumber.getText().toString()));
        hashMap.put(Constant.SERVER_TOKEN, ApiClient.makeTextRequestBody
                (preferenceHelper.getServerToken()));
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<DocumentResponse> responseCall;
        if (TextUtils.isEmpty(currentPhotoPath)) {
            responseCall = apiInterface.uploadDocument(null, hashMap);
        } else {
            responseCall = apiInterface.uploadDocument(ApiClient.makeMultipartRequestBody
                    (this, currentPhotoPath, Constant
                            .IMAGE_URL), hashMap);
        }
        responseCall.enqueue(new Callback<DocumentResponse>() {
            @Override
            public void onResponse(Call<DocumentResponse> call, Response<DocumentResponse>
                    response) {
                if (parseContent.isSuccessful(response)) {
                    Utilities.hideCustomProgressDialog();
                    Utilities.printLog("DOCUMENT", ApiClient.JSONResponse(response.body()));
                    if (response.body().isSuccess()) {
                        uri = null;
                        currentPhotoPath = "";
                        Documents documents = documentList.get(position);
                        documents.setImageUrl(response.body().getImageUrl());
                        documents.setUniqueCode(response.body().getUniqueCode());
                        documents.setExpiredDate(response.body().getExpiredDate());
                        documentList.set(position, documents);
                        preferenceHelper.putIsUserAllDocumentsUpload(response.body()
                                .isIsDocumentUploaded());
                        documentAdapter.notifyDataSetChanged();
                    } else {
                        uri = null;
                        currentPhotoPath = "";
                        ParseContent.getParseContentInstance().showErrorMessage(DocumentActivity
                                        .this,
                                response.body().getErrorCode(), false);
                    }
                }

            }

            @Override
            public void onFailure(Call<DocumentResponse> call, Throwable t) {
                uri = null;

            }
        });

    }

    private void getExtraData() {
        if (getIntent().getExtras() != null) {
            isApplicationStart = getIntent().getExtras().getBoolean(Constant.DOCUMENT_ACTIVITY);
        }

    }


    @Override
    public void onBackPressed() {

        if (isApplicationStart) {
            openLogoutDialog();
        } else {
            super.onBackPressed();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }


    }


    private void submitDocument() {
        if (preferenceHelper.getIsUserAllDocumentsUpload()) {
            goToHomeActivity();
        } else {
            Utilities.showToast(this, getResources().getString(R.string
                    .msg_plz_upload_document));
        }
    }
}
