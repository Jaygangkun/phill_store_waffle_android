package com.edelivery.store;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.edelivery.store.adapter.ProductDayTimeAdapter;
import com.edelivery.store.component.AddDetailInMultiLanguageDialog;
import com.edelivery.store.models.datamodel.ImageSetting;
import com.edelivery.store.models.datamodel.Product;
import com.edelivery.store.models.datamodel.ProductDayTime;
import com.edelivery.store.models.datamodel.ProductTime;
import com.edelivery.store.models.responsemodel.ImageSettingsResponse;
import com.edelivery.store.models.responsemodel.IsSuccessResponse;
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
import com.soundcloud.android.crop.Crop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.elluminati.edelivery.store.BuildConfig.IMAGE_URL;

public class AddProductActivity extends BaseActivity {

    private TextInputEditText etProductName, etProductDetail, etProSequenceNumber;
    private ImageView ivProductLogo;
    private SwitchCompat switchMakeVisible, switchAlwaysVisible;
    private boolean isNewItem = true, isEditable;
    private Uri uri;
    private Product product;
    private ImageHelper imageHelper;
    private ImageSetting imageSetting;
    private String currentPhotoPath;
    private AddDetailInMultiLanguageDialog addDetailInMultiLanguageDialog;
    private ArrayList<ProductDayTime> dayTimeList = new ArrayList<>();
    private ArrayList<ProductTime> productTimeList = new ArrayList<>();
    private RecyclerView rcvProductTime;
    private ArrayList<String> dayList = new ArrayList<String>() {{
        add("SUNDAY");
        add("MONDAY");
        add("TUESDAY");
        add("WEDNESDAY");
        add("THURSDAY");
        add("FRIDAY");
        add("SATURDAY");
    }};
    private ProductDayTime dayTime;
    private TextView tvAddTime, tvFromTime, tvToTime, tvSelectDay;
    private ProductDayTimeAdapter productDayTimeAdapter;
    private int selectedDay = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbar(toolbar, R.drawable.ic_back_white, android.R.color.transparent);

        ivProductLogo = (ImageView) findViewById(R.id.ivProductLogo);
        etProductName = (TextInputEditText) findViewById(R.id.etProName);
        etProductDetail = (TextInputEditText) findViewById(R.id.etProDetail);
        etProSequenceNumber = findViewById(R.id.etProSequenceNumber);

        tvFromTime = findViewById(R.id.tvFromTime);
        tvFromTime.setOnClickListener(this);

        tvSelectDay = findViewById(R.id.tvSelectDay);
        tvSelectDay.setOnClickListener(this);

        tvToTime = findViewById(R.id.tvToTime);
        tvToTime.setOnClickListener(this);

        tvAddTime = findViewById(R.id.tvAddTime);
        tvAddTime.setOnClickListener(this);

        switchMakeVisible = (SwitchCompat) findViewById(R.id.switchMakeVisible);
        switchMakeVisible.setChecked(true);

        switchAlwaysVisible = (SwitchCompat) findViewById(R.id.switchAlwaysVisible);
        switchAlwaysVisible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    for (ProductTime productTime : productTimeList) {
                        productTime.setProductOpenFullTime(true);
                    }
                } else {
                    for (ProductTime productTime : productTimeList) {
                        productTime.setProductOpenFullTime(false);
                    }
                }
            }
        });


        imageHelper = new ImageHelper(this);
        if (getIntent() != null && getIntent().getParcelableExtra(Constant.PRODUCT_ITEM) != null) {
            product = (Product) getIntent().getExtras().getParcelable(Constant.PRODUCT_ITEM);
            Glide.with(this).load(IMAGE_URL + product.getImageUrl()).into(ivProductLogo);
            etProductName.setText(product.getName());
            etProductName.setTag(product.getNameList());
            etProductDetail.setText(product.getDetails());
            etProSequenceNumber.setText(String.valueOf(product.getSequenceNumber()));
            switchMakeVisible.setChecked(product.isIsVisibleInStore());
            isNewItem = false;
            etProductName.setEnabled(false);
            etProductDetail.setEnabled(false);
            switchMakeVisible.setEnabled(false);
            switchAlwaysVisible.setEnabled(false);
            etProSequenceNumber.setEnabled(false);

            productTimeList = (ArrayList<ProductTime>) product.getProductTime();

            boolean isFullTIme = true;
            for (ProductTime productTime : productTimeList) {
                if (!productTime.isProductOpenFullTime()) {
                    switchAlwaysVisible.setChecked(false);
                    isFullTIme = false;
                    break;
                }
            }
            if (isFullTIme) {
                switchAlwaysVisible.setChecked(true);
            }

            tvFromTime.setEnabled(false);
            tvToTime.setEnabled(false);
            tvSelectDay.setEnabled(false);
            tvAddTime.setEnabled(false);

//            productTimeList.addAll(product.getProductTime());

        } else {
            for (int i = 0; i < dayList.size(); i++) {
                ProductTime productTime = new ProductTime();
                productTime.setDay(i);
                productTime.setProductOpen(true);
                productTime.setDayTime(new ArrayList<>());
                productTime.setProductOpenFullTime(true);
                productTimeList.add(productTime);
            }
        }


        etProductName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMultiLanguageDetail(etProductName.getHint().toString(),
                        (List<String>) etProductName.getTag(),
                        new AddDetailInMultiLanguageDialog.SaveDetails() {
                            @Override
                            public void onSave(List<String> detailList) {
                                etProductName.setTag(detailList);
                                etProductName.setText(Utilities.getDetailStringFromList(detailList,
                                        Language.getInstance().getStoreLanguageIndex()));

                            }
                        });
            }
        });

        getImageSettings();
        initRcvProductTime();
        initToolbarButton();
    }

    private void initRcvProductTime() {
        rcvProductTime = findViewById(R.id.rcvProductTime);
        rcvProductTime.setLayoutManager(new LinearLayoutManager(this));
        productDayTimeAdapter = new ProductDayTimeAdapter(productTimeList, AddProductActivity.this);
        rcvProductTime.setAdapter(productDayTimeAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        setToolbarEditIcon(false, 0);
        setToolbarCameraIcon(true);
        return true;
    }

    private void initToolbarButton() {
        CustomTextView tvtoolbarbtn = (CustomTextView) findViewById(R.id.tvtoolbarbtn);
        tvtoolbarbtn.setOnClickListener(v -> {
            if (!isEditable) {
                isEditable = true;
                tvtoolbarbtn.setText(R.string.text_save);
                etProductName.setEnabled(true);
                etProductDetail.setEnabled(true);
                switchMakeVisible.setEnabled(true);
                switchAlwaysVisible.setEnabled(true);
                etProSequenceNumber.setEnabled(true);
                tvFromTime.setEnabled(true);
                tvToTime.setEnabled(true);
                tvAddTime.setEnabled(true);
                tvSelectDay.setEnabled(true);
            } else {
                validate();
            }
        });
        tvtoolbarbtn.setVisibility(View.VISIBLE);
        if (isNewItem) {
            isEditable = true;
            tvtoolbarbtn.setText(R.string.text_save);
        } else {
            tvtoolbarbtn.setText(R.string.text_edit);
        }
    }

    /**
     * this method check that all data which selected by store user is valid or not
     */
    private void validate() {
        if (etProductName.getTag() == null) {
            etProductName.setError(getString(R.string.msg_empty_product_name));
        } else {
            Call<IsSuccessResponse> call;
            if (isEditable && !isNewItem) {

                call = ApiClient.getClient().create(ApiInterface.class).updateProduct
                        (ApiClient.makeJSONRequestBody(getMapWithData()));
//                if (!TextUtils.isEmpty(currentPhotoPath)) {
//
//                    call = ApiClient.getClient().create(ApiInterface.class).updateProduct
//                            (getMapWithData(),
//                                    ApiClient.makeMultipartRequestBody(this, currentPhotoPath,
//                                            Constant.IMAGE_URL));
//                } else {
//                    call = ApiClient.getClient().create(ApiInterface.class).updateProduct
//                            (getMapWithData());
//                }
            } else {
//                if (!TextUtils.isEmpty(currentPhotoPath)) {
//                    call = ApiClient.getClient().create(ApiInterface.class).addProduct
//                            (getMapWithData(),
//                                    ApiClient.makeMultipartRequestBody(this, currentPhotoPath,
//                                            Constant.IMAGE_URL));
//                } else {
//                    call = ApiClient.getClient().create(ApiInterface.class).addProduct
//                            (getMapWithData());
//                }

                call = ApiClient.getClient().create(ApiInterface.class).addProduct
                        (ApiClient.makeJSONRequestBody(getMapWithData()));

            }
            addNEditProduct(call);
        }
    }

    private HashMap<String, RequestBody> getMapWithData1() {
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put(Constant.STORE_ID, ApiClient.makeTextRequestBody(
                PreferenceHelper.getPreferenceHelper(this).getStoreId()));
        map.put(Constant.SERVER_TOKEN, ApiClient.makeTextRequestBody(PreferenceHelper
                .getPreferenceHelper(this).getServerToken()));
        map.put(Constant.NAME, ApiClient.makeTextRequestBody(
                new JSONArray((List<String>) etProductName.getTag())));
        map.put(Constant.DETAILS, ApiClient.makeTextRequestBody(
                etProductDetail.getText().toString().trim()));
        map.put(Constant.IS_VISIBLE_IN_STORE, ApiClient.makeTextRequestBody(String.valueOf
                (switchMakeVisible.isChecked())));
        map.put(Constant.SEQUENCE_NUMBER,
                ApiClient.makeTextRequestBody(TextUtils.isEmpty(etProSequenceNumber.getText().toString()) ? 0 : Long.parseLong(etProSequenceNumber.getText().toString())));


//        Gson gson = new Gson();
//        JsonElement element = gson.toJsonTree(productTimeList, new TypeToken<List<ProductTime>>() {
//        }.getType());

//        JSONArray jsonArray = element.get();
        map.put(Constant.PRODUCT_TIME, ApiClient.makeTextRequestBody(
                new JSONArray(productTimeList)));

        JSONArray result = new JSONArray(productTimeList);


        if (isEditable && !isNewItem) {
            map.put(Constant.PRODUCT_ID, ApiClient.makeTextRequestBody(String.valueOf(product
                    .getId())));
        }
        return map;
    }

    private JSONObject getMapWithData() {
        JSONObject map = new JSONObject();
        try {
            map.put(Constant.STORE_ID, (
                    PreferenceHelper.getPreferenceHelper(this).getStoreId()));
            map.put(Constant.SERVER_TOKEN, (PreferenceHelper
                    .getPreferenceHelper(this).getServerToken()));
            map.put(Constant.NAME, (
                    new JSONArray((List<String>) etProductName.getTag())));
            map.put(Constant.DETAILS, (
                    etProductDetail.getText().toString().trim()));
            map.put(Constant.IS_VISIBLE_IN_STORE, (String.valueOf
                    (switchMakeVisible.isChecked())));
            map.put(Constant.SEQUENCE_NUMBER,
                    (TextUtils.isEmpty(etProSequenceNumber.getText().toString()) ? 0 : Long.parseLong(etProSequenceNumber.getText().toString())));


//            ArrayList<ProductDayTime> productDayTimes=productTimeList.get()

            Gson gson = new Gson();
            String s14 = gson.toJson(productTimeList);


            map.put(Constant.PRODUCT_TIME, (
                    new JSONArray(s14)));

            if (isEditable && !isNewItem) {
                map.put(Constant.PRODUCT_ID, (String.valueOf(product
                        .getId())));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return map;
    }

    /**
     * this method call a webservice for Add and Update Product in Store
     *
     * @param call
     */
    private void addNEditProduct(Call<IsSuccessResponse> call) {
        Utilities.showProgressDialog(this);

        call.enqueue(new Callback<IsSuccessResponse>() {
            @Override
            public void onResponse(Call<IsSuccessResponse> call, Response<IsSuccessResponse>
                    response) {
                Utilities.removeProgressDialog();
                if (response.isSuccessful()) {
                    Utilities.printLog("add_product", new Gson().toJson(response.body()));
                    if (response.body().isSuccess()) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            finishAfterTransition();
                        } else {
                            finish();
                        }
                    } else {
                        ParseContent.getParseContentInstance().showErrorMessage
                                (AddProductActivity.this, response.body().getErrorCode(), true);
                    }
                } else {
                    Utilities.showHttpErrorToast(response.code(), AddProductActivity.this);
                }
            }

            @Override
            public void onFailure(Call<IsSuccessResponse> call, Throwable t) {
                Utilities.printLog("AddProduct", t.getMessage());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvFromTime:
                openTimePickerDialog();
                break;
            case R.id.tvToTime:
                openTimePickerDialog();
                break;
            case R.id.tvSelectDay:
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(AddProductActivity.this);

                builderSingle.setTitle("Select day");

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(AddProductActivity.this,
                        R.layout.item_spinner_simple, R.id.tvBankName, dayList);


                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);
                        selectedDay = which;
                        tvSelectDay.setText(strName);
                        if (productTimeList.size() > 0)
                            dayTimeList = (ArrayList<ProductDayTime>) productTimeList.get(which).getDayTime();
                    }
                });
                builderSingle.show();
                break;
            case R.id.tvAddTime:
                if (dayTime != null) {
//                    dayTimeList.add(dayTime);
                    if (productTimeList.size() > 0) {

                        productTimeList.get(selectedDay).getDayTime().add(dayTime);
                        productTimeList.get(selectedDay).setProductOpenFullTime(false);
                    } else {
                        ProductTime productTime = new ProductTime();
                        productTime.setDay(selectedDay);
                        productTime.setProductOpen(true);
                        productTime.setDayTime(new ArrayList<>());
                        productTime.getDayTime().add(dayTime);
                        productTimeList.add(productTime);
                    }
                    productDayTimeAdapter.setProductTimeArrayList(productTimeList);
                    productDayTimeAdapter.notifyDataSetChanged();
                    tvToTime.setText("To Time");
                    dayTime = null;
                    tvFromTime.setText("From Time");
                } else {
                    Utilities.showToast(this, getResources().getString(R.string
                            .msg_plz_select_valid_time));
                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {

            case R.id.ivCamera:
                if (isNewItem || isEditable) {
                    showPictureDialog();
                }
                return true;

        }

        return true;
    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle(getResources().getString(
                R.string.text_choosePicture));
        String[] pictureDialogItems = {
                getResources().getString(R.string.text_gallery),
                getResources().getString(R.string.text_camera)};

        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog3, int which3) {
                        switch (which3) {

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
                        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission
                                .READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                                ContextCompat.checkSelfPermission(this, android.Manifest
                                        .permission.READ_EXTERNAL_STORAGE) != PackageManager
                                        .PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(this, new String[]{android.Manifest
                                    .permission.READ_EXTERNAL_STORAGE}, Constant
                                    .PERMISSION_CHOOSE_PHOTO);
                        } else {
                          /*  Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                            startActivityForResult(galleryIntent, Constant.PERMISSION_CHOOSE_PHOTO);
*/
                            Intent galleryIntent = new Intent();
                            galleryIntent.setType("image/*");
                            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(galleryIntent, Constant.PERMISSION_CHOOSE_PHOTO);
                        }
                    }
                }
                break;
            case Constant.PERMISSION_TAKE_PHOTO:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        takePhotoFromCamera();
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
                        if (Utilities.checkImageFileType(AddProductActivity.this, imageSetting
                                .getImageType(), uri)) {
                            if (Utilities.checkIMGSize(uri, imageSetting
                                    .getProductImageMaxWidth(), imageSetting
                                    .getProductImageMaxHeight
                                            (), imageSetting.getProductImageMinWidth(), imageSetting
                                    .getProductImageMinHeight(), imageSetting
                                    .getProductImageRatio())) {
                                setImage(uri);
                            } else {
                                beginCrop(uri);
                            }
                        }
                    }
                    break;
                case Constant.PERMISSION_TAKE_PHOTO:
                    if (uri != null) {
                        if (Utilities.checkImageFileType(AddProductActivity.this, imageSetting
                                .getImageType(), uri)) {
                            if (Utilities.checkIMGSize(uri,
                                    imageSetting
                                            .getProductImageMaxWidth(), imageSetting
                                            .getProductImageMaxHeight
                                                    (), imageSetting.getProductImageMinWidth(),
                                    imageSetting
                                            .getProductImageMinHeight(), imageSetting
                                            .getProductImageRatio())) {
                                setImage(uri);
                            } else {
                                beginCrop(uri);
                            }
                        }
                    }
                    break;
                case Crop.REQUEST_CROP:
                    setImage(Crop.getOutput(data));
                    break;
                default:
                    break;
            }
        }
    }

    private void setImage(final Uri uri) {
        date = new Date();
        imageName = simpleDateFormat.format(date);
        currentPhotoPath = ImageHelper.getFromMediaUriPfd(this, getContentResolver
                (), uri).getPath();
        new ImageCompression(this).setImageCompressionListener(new ImageCompression
                .ImageCompressionListener() {
            @Override
            public void onImageCompression(String compressionImagePath) {
                currentPhotoPath = compressionImagePath;
                GlideApp.with(AddProductActivity.this).load(uri)
                        .error(R
                                .drawable
                                .icon_default_profile).into(ivProductLogo);
            }
        }).execute(currentPhotoPath);
    }

    private void getImageSettings() {
        Utilities.showCustomProgressDialog(this, false);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ImageSettingsResponse> call = apiInterface.getImageSettings();
        call.enqueue(new Callback<ImageSettingsResponse>() {
            @Override
            public void onResponse(Call<ImageSettingsResponse> call,
                                   Response<ImageSettingsResponse> response) {
                Utilities.hideCustomProgressDialog();
                if (parseContent.isSuccessful(response)) {
                    if (response.body().isSuccess()) {
                        imageSetting = response.body().getImageSetting();
                    }

                }
            }

            @Override
            public void onFailure(Call<ImageSettingsResponse> call, Throwable t) {

            }
        });
    }


    /**
     * This method is used for crop the placeholder which selected or captured
     */
    public void beginCrop(Uri sourceUri) {

        Uri outputUri = Uri.fromFile(imageHelper.createImageFile());
        Crop.of(sourceUri, outputUri).withAspect(imageSetting
                .getProductImageMaxWidth
                        (), imageSetting.getProductImageMaxHeight())
                .checkValidIMGCrop(true).setCropData(imageSetting.getProductImageMaxWidth(),
                imageSetting.getProductImageMaxHeight(), imageSetting.getProductImageMinWidth(),
                imageSetting.getProductImageMinHeight(), imageSetting.getProductImageRatio()).start
                (this);


    }

    private void addMultiLanguageDetail(String title, List<String> detailMap,
                                        @NonNull AddDetailInMultiLanguageDialog.SaveDetails saveDetails) {

        if (addDetailInMultiLanguageDialog != null && addDetailInMultiLanguageDialog.isShowing()) {
            return;
        }
        addDetailInMultiLanguageDialog = new AddDetailInMultiLanguageDialog(this, title,
                saveDetails, detailMap, false);
        addDetailInMultiLanguageDialog.show();
    }


    private void openTimePickerDialog() {

        final DecimalFormat numberFormat = new DecimalFormat("00");
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,

                new TimePickerDialog.OnTimeSetListener() {

                    int count = 0;

                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int
                            selectedMinute) {
                        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            dayTime = new ProductDayTime();
                            dayTime.setProductOpenTime(numberFormat.format(selectedHour).concat(":")
                                    .concat(numberFormat.format(selectedMinute)));

                            if (isValidOpeningTime(dayTime)) {
                                openCloseTimePickerDialog(numberFormat.format(selectedHour),
                                        numberFormat.format(selectedMinute), dayTime);
                            } else {
                                Utilities.showToast(AddProductActivity.this, getString(R.string
                                        .text_not_valid_Time));
                            }
                        } else {
                            if (count == 0) {
                                dayTime = new ProductDayTime();
                                dayTime.setProductOpenTime(numberFormat.format(selectedHour)
                                        .concat(":")
                                        .concat(numberFormat.format(selectedMinute)));

                                if (isValidOpeningTime(dayTime)) {
                                    openCloseTimePickerDialog(numberFormat.format(selectedHour),
                                            numberFormat.format(selectedMinute), dayTime);
                                } else {
                                    Utilities.showToast(AddProductActivity.this, getString(R.string
                                            .text_not_valid_Time));
                                }
                            }

                            count++;
                        }

                    }
                }, hour, minute, true);
        timePickerDialog.setCustomTitle(getCustomTitleView(getString(R.string.text_opening_time),
                false,
                null));

        timePickerDialog.show();
    }

    private void openCloseTimePickerDialog(final String openingTimeHours, final String
            openingTimeMinute, final ProductDayTime storeTime) {

        final DecimalFormat numberFormat = new DecimalFormat("00");
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);


        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    int count = 0;

                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int
                            selectedMinute) {
                        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            storeTime.setProductCloseTime(numberFormat.format(selectedHour).concat
                                    (":")
                                    .concat(numberFormat.format(selectedMinute)));
                            if (isValidClosingTime(storeTime)) {
                                tvFromTime.setText(storeTime.getProductOpenTime());
                                tvToTime.setText(storeTime.getProductCloseTime());
                            } else {
                                Utilities.showToast(AddProductActivity.this, getString(R.string
                                        .text_not_valid_Time));
                            }
                        } else {
                            if (count == 0) {
                                storeTime.setProductCloseTime(numberFormat.format(selectedHour)
                                        .concat(":")
                                        .concat(numberFormat.format(selectedMinute)));
                                if (isValidClosingTime(storeTime)) {
                                    tvFromTime.setText(storeTime.getProductOpenTime());
                                    tvToTime.setText(storeTime.getProductCloseTime());
                                } else {
                                    Utilities.showToast(AddProductActivity.this, getString(R.string
                                            .text_not_valid_Time));
                                }
                            }
                            count++;
                        }
                    }
                }, hour, minute, true);
        timePickerDialog.setCustomTitle(getCustomTitleView(getString(R.string.text_closing_time),
                true,
                openingTimeHours + ":" + openingTimeMinute));

        timePickerDialog.show();
    }


    private View getCustomTitleView(String dialogTitle, boolean showPreviousTime, String
            previousTime) {
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogTitleView = inflater.inflate(R.layout.custom_time_picker_title, null);
        TextView titleView = (TextView) dialogTitleView.findViewById(R.id.tvTimePickerTitle);
        TextView openingTime = (TextView) dialogTitleView.findViewById(R.id
                .tvTimePickerOpeningTime);
        titleView.setText(dialogTitle);
        if (showPreviousTime) {
            openingTime.setVisibility(View.VISIBLE);
            openingTime.setText(getString(R.string.text_opening_time) + "  " + previousTime);
        }

        return dialogTitleView;
    }

    private boolean isValidOpeningTime(ProductDayTime storeTime) {

        if (dayTimeList.isEmpty()) {
            return true;
        } else {
            for (int i = 0; i < dayTimeList.size(); i++) {
                try {
                    String oldStoreOpenTime = dayTimeList.get(i).getProductOpenTime();
                    Date oldOpenTime = parseContent.timeFormat2.parse
                            (oldStoreOpenTime);

                    String oldStoreCloseTime = dayTimeList.get(i).getProductCloseTime();
                    Date oldClosedTime = parseContent.timeFormat2.parse
                            (oldStoreCloseTime);

                    String openTime = storeTime.getProductOpenTime();
                    Date newOpenTime = parseContent.timeFormat2.parse
                            (openTime);


                    if (newOpenTime.after(oldOpenTime) && newOpenTime.before(oldClosedTime)) {
                        return false;
                    }


                } catch (ParseException e) {
                    Utilities.printLog(SettingsActivity.class.getName(), e + "");
                }

            }
            return true;
        }
    }


    private boolean isValidClosingTime(ProductDayTime storeTime) {
        if (dayTimeList.isEmpty()) {
            try {
                String storeOpenTime = storeTime.getProductOpenTime();
                Date selectedOpenTime = parseContent.timeFormat2.parse
                        (storeOpenTime);

                String storeCloseTime = storeTime.getProductCloseTime();
                Date newClosedTime = parseContent.timeFormat2.parse
                        (storeCloseTime);

                return (newClosedTime.after(selectedOpenTime));


            } catch (ParseException e) {
                Utilities.handleException("time_compare", e);
            }
        } else {
            for (int i = 0; i < dayTimeList.size(); i++) {
                try {
                    String oldStoreOpenTime = dayTimeList.get(i).getProductOpenTime();
                    Date oldOpenTime = parseContent.timeFormat2.parse
                            (oldStoreOpenTime);

                    String oldStoreCloseTime = dayTimeList.get(i).getProductCloseTime();
                    Date oldClosedTime = parseContent.timeFormat2.parse
                            (oldStoreCloseTime);

                    String storeOpenTime = storeTime.getProductOpenTime();
                    Date selectedOpenTime = parseContent.timeFormat2.parse
                            (storeOpenTime);


                    String storeCloseTime = storeTime.getProductCloseTime();
                    Date newClosedTime = parseContent.timeFormat2.parse
                            (storeCloseTime);

                    if (newClosedTime.after(selectedOpenTime)) {
                        if (newClosedTime.after(oldOpenTime) && newClosedTime.before
                                (oldClosedTime)) {
                            return false;
                        } else if (selectedOpenTime.before(oldOpenTime) && newClosedTime.after
                                (oldClosedTime)) {
                            return false;
                        }
                    } else {
                        return false;
                    }


                } catch (ParseException e) {
                    Utilities.printLog(SettingsActivity.class.getName(), e + "");
                }
            }

        }
        return true;

    }

    public void deleteSpecificTime(ProductDayTime productDayTime) {
        dayTimeList.remove(productDayTime);
    }
}
