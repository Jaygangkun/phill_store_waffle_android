package com.edelivery.store;

import android.annotation.TargetApi;
import android.app.AlertDialog;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.edelivery.store.adapter.ProductsAdapter;
import com.edelivery.store.component.AddDetailInMultiLanguageDialog;
import com.edelivery.store.component.SelectProductsDialog;
import com.edelivery.store.models.datamodel.ImageSetting;
import com.edelivery.store.models.datamodel.Product;
import com.edelivery.store.models.datamodel.ProductGroup;
import com.edelivery.store.models.responsemodel.ImageSettingsResponse;
import com.edelivery.store.models.responsemodel.IsSuccessResponse;
import com.edelivery.store.models.responsemodel.ProductListResponse;
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
import com.edelivery.store.widgets.CustomButton;
import com.elluminati.edelivery.store.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.soundcloud.android.crop.Crop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.elluminati.edelivery.store.BuildConfig.IMAGE_URL;

public class AddGroupActivity extends BaseActivity {

    private ImageView ivProductLogo;
    private CustomButton btnSave;
    private TextInputLayout inputLayoutProName, inputLayoutProGroup;
    private TextInputEditText etGroupName, etGroupProducts, etGroupSequenceNumber;
    private boolean isNewItem = true, isEditable;
    private ProductGroup productGroup;
    private List<Product> productList;
    private List<Product> selectedProductList;
    private List<String> selectedProductIds;
    private SelectProductsDialog selectProductsDialog;
    private Uri uri;
    private ImageHelper imageHelper;
    private ImageSetting imageSetting;
    private String currentPhotoPath;
    private AddDetailInMultiLanguageDialog addDetailInMultiLanguageDialog;
    private RecyclerView rcvProducts;
    private ProductsAdapter productsAdapter;
    private boolean isMultipleLanguages;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbar(toolbar, R.drawable.ic_back, R.color.color_app_theme);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ((TextView) findViewById(R.id.tvToolbarTitle)).setText(getString(R.string
                .text_add_category));
        ivProductLogo = (ImageView) findViewById(R.id.ivProductLogo);
        inputLayoutProName = (TextInputLayout) findViewById(R.id.inputLayoutProName);
        inputLayoutProGroup = (TextInputLayout) findViewById(R.id.inputLayoutProGroup);
        etGroupName = (TextInputEditText) findViewById(R.id.etGroupName);
        etGroupProducts = (TextInputEditText) findViewById(R.id.etGroupProducts);
        etGroupSequenceNumber = findViewById(R.id.etGroupSequenceNumber);
        rcvProducts = (RecyclerView) findViewById(R.id.rcvProducts);
        btnSave = (CustomButton) findViewById(R.id.btnSave);
        imageHelper = new ImageHelper(this);

        productList = new ArrayList<>();
        selectedProductList = new ArrayList<>();
        selectedProductIds = new ArrayList<>();
        ivProductLogo.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        initRcvProductGroups();

        if (getIntent() != null && getIntent().getParcelableExtra(Constant.PRODUCT_GROUP) != null) {
            productGroup = (ProductGroup) getIntent().getParcelableExtra(Constant.PRODUCT_GROUP);
            Glide.with(this).load(IMAGE_URL + productGroup.getImageUrl()).
                    placeholder(getResources().getDrawable(R.drawable.placeholder))
                    .fallback(getResources().getDrawable(R.drawable.placeholder)).into(ivProductLogo);
            etGroupName.setText(productGroup.getName());
            etGroupSequenceNumber.setText(String.valueOf(productGroup.getSequenceNumber()));
            etGroupName.setTag(productGroup.getNameList());
            selectedProductIds.addAll(productGroup.getProductIds());
            isNewItem = false;
        }

        etGroupName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMultiLanguageDetail(etGroupName.getHint().toString(),
                        (List<String>) etGroupName.getTag(),
                        new AddDetailInMultiLanguageDialog.SaveDetails() {
                            @Override
                            public void onSave(List<String> detailList) {
                                etGroupName.setTag(detailList);
                                etGroupName.setText(Utilities.getDetailStringFromList(detailList,
                                        Language.getInstance().getStoreLanguageIndex()));

                            }
                        });
            }
        });
        /*
        isMultipleLanguages = Language.getInstance().getStoreLanguages().size() > 1;

        if(isMultipleLanguages) {
            etGroupName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addMultiLanguageDetail(etGroupName.getHint().toString(),
                            (List<String>) etGroupName.getTag(),
                            new AddDetailInMultiLanguageDialog.SaveDetails() {
                                @Override
                                public void onSave(List<String> detailList) {
                                    etGroupName.setTag(detailList);
                                    etGroupName.setText(Utilities.getDetailStringFromList
                                    (detailList,
                                            Language.getInstance().getStoreLanguageIndex()));

                                }
                            });
                }
            });
        }else {
            etGroupName.setFocusableInTouchMode(true);
        }*/

        etGroupProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProductListDialog();
            }
        });

        getImageSettings();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu1) {
        super.onCreateOptionsMenu(menu1);
        menu = menu1;
        setToolbarEditIcon(false, R.drawable.filter_store);
        setToolbarCameraIcon(false);
        return true;
    }

    private void getProductGroupList() {
        Utilities.showCustomProgressDialog(this, false);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.SERVER_TOKEN, preferenceHelper
                    .getServerToken());
            jsonObject.put(Constant.STORE_ID, preferenceHelper.getStoreId());
        } catch (JSONException e) {
            Utilities.handleThrowable(InstantOrderActivity.class.getSimpleName(), e);
        }
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ProductListResponse> call = apiInterface.getProductList(ApiClient
                .makeJSONRequestBody(jsonObject));
        call.enqueue(new Callback<ProductListResponse>() {
            @Override
            public void onResponse(Call<ProductListResponse> call,
                                   Response<ProductListResponse> response) {
                Utilities.hideCustomProgressDialog();
                if (parseContent.isSuccessful(response)) {
                    if (response.body().isSuccess()) {
                        productList = response.body().getProducts();
                        selectedProductList.clear();
                        for (Product product : productList) {
                            if (selectedProductIds.contains(product.getId())) {
                                product.setSelected(true);
                                selectedProductList.add(product);

                            }
                        }

                        productsAdapter.setProducts(productList);
                        productsAdapter.notifyDataSetChanged();

                        if (!selectedProductList.isEmpty())
                            etGroupProducts.setTag(selectedProductIds);
                    }

                }
            }

            @Override
            public void onFailure(Call<ProductListResponse> call, Throwable t) {
                Utilities.handleThrowable("ADD_GROUP_ACTIVITY", t);
            }
        });
    }

    private void initRcvProductGroups() {
        rcvProducts.setLayoutManager(new LinearLayoutManager(this));
        productsAdapter = new ProductsAdapter(productList);
        rcvProducts.setAdapter(productsAdapter);
    }

    private void openProductListDialog() {
        if (selectProductsDialog != null && selectProductsDialog.isShowing()) {
            return;
        }
        selectProductsDialog = new SelectProductsDialog(this, productList) {
            @Override
            public void onSelect(List<Product> products) {
                selectedProductList.clear();
                selectedProductList = products;
                etGroupProducts.getText().clear();
                etGroupProducts.setTag(selectedProductList);

            }
        };

        selectProductsDialog.show();
    }

    private String getProducts() {
        String products = "";
        for (Product product1 : selectedProductList) {
            if (TextUtils.isEmpty(products)) {
                products += product1.getId();
            } else {
                products += "," + product1.getId();
            }
        }

        return products;
    }


    /**
     * this method check that all data which selected by store user is valid or not
     */
    private void validate() {
        clearError();
        selectedProductList.clear();
        for (Product product : productList) {
            if (product.isSelected())
                selectedProductList.add(product);

        }
        if (etGroupName.getTag() == null) {
            inputLayoutProName.setError(getString(R.string.msg_empty_group_name));
        } else if (selectedProductList.isEmpty()) {
            Utilities.showToast(this, getString(R.string.msg_empty_product_groups));
        } else {
            Call<IsSuccessResponse> call;
            if (!isNewItem) {
                if (!TextUtils.isEmpty(currentPhotoPath)) {
                    call = ApiClient.getClient().create(ApiInterface.class).updateProductGroup
                            (getMapWithData(),
                                    ApiClient.makeMultipartRequestBody(this, currentPhotoPath,
                                            Constant.IMAGE_URL));
                } else {
                    call = ApiClient.getClient().create(ApiInterface.class).updateProductGroup
                            (getMapWithData());
                }
            } else {
                if (!TextUtils.isEmpty(currentPhotoPath)) {
                    call = ApiClient.getClient().create(ApiInterface.class).addProductGroup
                            (getMapWithData(),
                                    ApiClient.makeMultipartRequestBody(this, currentPhotoPath,
                                            Constant.IMAGE_URL));
                } else {
                    call = ApiClient.getClient().create(ApiInterface.class).addProductGroup
                            (getMapWithData());
                }

            }
            addNEditGroup(call);
        }
    }

    public void clearError() {
        inputLayoutProName.setError(null);
        inputLayoutProGroup.setError(null);
    }


    private HashMap<String, RequestBody> getMapWithData() {
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put(Constant.STORE_ID, ApiClient.makeTextRequestBody(
                PreferenceHelper.getPreferenceHelper(this).getStoreId()));
        map.put(Constant.SERVER_TOKEN, ApiClient.makeTextRequestBody(PreferenceHelper
                .getPreferenceHelper(this).getServerToken()));
        map.put(Constant.NAME, ApiClient.makeTextRequestBody(
                new JSONArray((List<String>) etGroupName.getTag())));

        map.put(Constant.PRODUCT_IDS, ApiClient.makeTextRequestBody(
                getProducts()));
        map.put(Constant.SEQUENCE_NUMBER,
                ApiClient.makeTextRequestBody(TextUtils.isEmpty(etGroupSequenceNumber.getText().toString()) ? 0 : Long.parseLong(etGroupSequenceNumber.getText().toString())));

        if (!isNewItem) {
            map.put(Constant.PRODUCT_GROUP_ID,
                    ApiClient.makeTextRequestBody(String.valueOf(productGroup
                            .getId())));
        }
        return map;
    }

    /**
     * this method call a webservice for Add and Update Product in Store
     *
     * @param call
     */
    private void addNEditGroup(Call<IsSuccessResponse> call) {
        Utilities.showProgressDialog(this);

        call.enqueue(new Callback<IsSuccessResponse>() {
            @Override
            public void onResponse(Call<IsSuccessResponse> call, Response<IsSuccessResponse>
                    response) {
                Utilities.removeProgressDialog();
                if (response.isSuccessful()) {
                    Utilities.printLog("add_group", new Gson().toJson(response.body()));
                    if (response.body().isSuccess()) {
                        parseContent.showMessage(AddGroupActivity
                                .this, response.body().getMessage());
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            finishAfterTransition();
                        } else {
                            finish();
                        }
                    } else {
                        ParseContent.getParseContentInstance().showErrorMessage
                                (AddGroupActivity.this, response.body().getErrorCode(), true);
                    }
                } else {
                    Utilities.showHttpErrorToast(response.code(), AddGroupActivity.this);
                }
            }

            @Override
            public void onFailure(Call<IsSuccessResponse> call, Throwable t) {
                Utilities.printLog("AddProduct", t.getMessage());
            }
        });
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {

            case R.id.ivCamera:
                if (isNewItem || isEditable) {
                    showPictureDialog();
                }
                return true;

            case R.id.ivEditMenu:
                if (!isEditable) {
                    isEditable = true;
                    setToolbarEditIcon(true, R.drawable.ic_done_white_2);
                    etGroupName.setEnabled(true);
                    etGroupProducts.setEnabled(true);
                    productsAdapter.setEnabled(true);
                } else {
                    validate();
                }
                return true;
        }

        return true;
    }*/

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ivProductLogo:
                showPictureDialog();
                break;
            case R.id.btnSave:
                validate();
                break;
        }
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
                        if (Utilities.checkImageFileType(AddGroupActivity.this, imageSetting
                                .getImageType(), uri)) {
                            if (Utilities.checkIMGSize(uri, imageSetting
                                            .getDeliveryImageMaxWidth(), imageSetting
                                            .getDeliveryImageMaxHeight
                                                    (), imageSetting.getDeliveryImageMinWidth(),
                                    imageSetting
                                            .getDeliveryImageMinHeight(), imageSetting
                                            .getDeliveryImageRatio())) {
                                setImage(uri);
                            } else {
                                beginCrop(uri);
                            }
                        }
                    }
                    break;
                case Constant.PERMISSION_TAKE_PHOTO:
                    if (uri != null) {
                        if (Utilities.checkImageFileType(AddGroupActivity.this, imageSetting
                                .getImageType(), uri)) {
                            if (Utilities.checkIMGSize(uri,
                                    imageSetting
                                            .getDeliveryImageMaxWidth(), imageSetting
                                            .getDeliveryImageMaxHeight
                                                    (), imageSetting.getDeliveryImageMinWidth(),
                                    imageSetting
                                            .getDeliveryImageMinHeight(), imageSetting
                                            .getDeliveryImageRatio())) {
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
                GlideApp.with(AddGroupActivity.this).load(uri)
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
                        getProductGroupList();
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
                .getDeliveryImageMaxWidth
                        (), imageSetting.getDeliveryImageMaxHeight())
                .checkValidIMGCrop(true).setCropData(imageSetting.getDeliveryImageMaxWidth(),
                imageSetting.getDeliveryImageMaxHeight(), imageSetting.getDeliveryImageMinWidth(),
                imageSetting.getDeliveryImageMinHeight(), imageSetting.getDeliveryImageRatio()).start
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
}
