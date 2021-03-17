package com.edelivery.store;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.edelivery.store.adapter.ItemImageAdapter;
import com.edelivery.store.adapter.SpecificationListAdapter;
import com.edelivery.store.component.AddDetailInMultiLanguageDialog;
import com.edelivery.store.component.CustomAlterDialog;
import com.edelivery.store.component.CustomSelectProductDialog;
import com.edelivery.store.component.CustomSelectSpecificationGroupDialog;
import com.edelivery.store.models.datamodel.ImageSetting;
import com.edelivery.store.models.datamodel.Item;
import com.edelivery.store.models.datamodel.ItemSpecification;
import com.edelivery.store.models.datamodel.Product;
import com.edelivery.store.models.datamodel.ProductSpecification;
import com.edelivery.store.models.responsemodel.AddOrUpdateItemResponse;
import com.edelivery.store.models.responsemodel.ImageSettingsResponse;
import com.edelivery.store.models.responsemodel.IsSuccessResponse;
import com.edelivery.store.models.responsemodel.SpecificationGroupFroAddItemResponse;
import com.edelivery.store.models.singleton.CurrentProduct;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
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

public class AddItemActivity extends BaseActivity {

    public boolean isEditable;
    public boolean isNewItem = true;
    private Uri uri;
    private TextInputEditText etItemName, etItemDetail, etItemTex;
    private EditText etItemPrice, etProductName, etItemPriceWithoutOffer, etOfferMessage,
            etSequenceNumber;
    private ImageView ivItem;
    private String productId;
    private SwitchCompat switchVisibility, switchInStoke;
    private TextView tvCurrency, tvCurrency2;
    private ArrayList<ItemSpecification> itemSpecificationFinalList = new ArrayList<>();
    private ArrayList<ItemSpecification> productSpecificationGroupOriginalList = new ArrayList<>();
    private SpecificationListAdapter specificationListAdapter;
    private Item item = new Item();
    private String TAG = "AddItemActivity";
    private CustomSelectProductDialog customSelectProductDialog;
    private RecyclerView rcSpecification;
    private ArrayList<String> itemImageList = new ArrayList<>();
    private ArrayList<String> itemImageListForAdapter = new ArrayList<>();
    private ArrayList<String> deleteItemImage = new ArrayList<>();
    private RecyclerView rcvItemImage;
    private ItemImageAdapter itemImageAdapter;
    private ImageHelper imageHelper;
    private ImageSetting imageSetting;
    private AddDetailInMultiLanguageDialog addDetailInMultiLanguageDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbar(toolbar, R.drawable.ic_back_white, android.R.color.transparent);
        tvCurrency = (TextView) findViewById(R.id.tvCurrency);
        tvCurrency2 = (TextView) findViewById(R.id.tvCurrency2);
        etItemName = (TextInputEditText) findViewById(R.id.etItemName);
        rcvItemImage = (RecyclerView) findViewById(R.id.rcvItemImage);
        etItemDetail = (TextInputEditText) findViewById(R.id.etItemDetail);
        etItemPrice = (EditText) findViewById(R.id.etItemPrice);
        etSequenceNumber = findViewById(R.id.etSequenceNumber);
        etItemTex = findViewById(R.id.etItemTex);
        etItemPriceWithoutOffer = (EditText) findViewById(R.id.etItemPriceWithoutOffer);
        etOfferMessage = (EditText) findViewById(R.id.etOfferMessage);
        ivItem = (ImageView) findViewById(R.id.ivItem);
        etProductName = (EditText) findViewById(R.id.etProductName);
        etProductName.setFocusableInTouchMode(false);
        etProductName.setOnClickListener(this);
        findViewById(R.id.ivEditSpecification).setVisibility(View.GONE);
        switchVisibility = (SwitchCompat) findViewById(R.id.switchMakeVisible);
        switchVisibility.setChecked(true);
        switchInStoke = (SwitchCompat) findViewById(R.id.switchInStock);
        switchInStoke.setChecked(true);
        //etCategory = (AutoCompleteTextView) findViewById(R.id.etCategory);
        rcSpecification = (RecyclerView) findViewById(R.id.rcSpecification);
        rcSpecification.setNestedScrollingEnabled(false);
        rcSpecification.setLayoutManager(new LinearLayoutManager(this));
        specificationListAdapter = new SpecificationListAdapter(this, itemSpecificationFinalList,
                true);
        rcSpecification.setAdapter(specificationListAdapter);
        initRcvImage();
        setViewEnable(true);
        findViewById(R.id.tvAddItemSpecification).setOnClickListener(this);
        imageHelper = new ImageHelper(this);
        tvCurrency.setText(preferenceHelper.getCurrency());
        tvCurrency2.setText(preferenceHelper.getCurrency());
        if (getIntent() != null && getIntent().getParcelableExtra(Constant.ITEM) != null) {
            item = getIntent().getParcelableExtra(Constant.ITEM);
            isNewItem = false;
            etItemName.setText(item.getName());
            etItemName.setTag(item.getNameList());
            etItemDetail.setText(item.getDetails());
            etItemDetail.setTag(item.getDetailsList());
            if (item.getImageUrl() != null && !item.getImageUrl().isEmpty()) {
                GlideApp.with(this).load(getIntent().getStringExtra(Constant.IMAGE_URL)).dontAnimate()
                        .into(ivItem);
            }
            etItemPriceWithoutOffer.setText(String.valueOf(item.getItemPriceWithoutOffer()));
            etOfferMessage.setText(item.getOfferMessageOrPercentage());
            switchVisibility.setChecked(item.isIsVisibleInStore());
            switchInStoke.setChecked(item.isItemInStock());
            etProductName.setText(getIntent().getStringExtra(Constant.NAME));
            etProductName.setOnClickListener(null);
            etProductName.setEnabled(false);
            productId = getIntent().getStringExtra(Constant.PRODUCT_ID);
            etItemPrice.setText(parseContent.decimalTwoDigitFormat.format(item.getPrice()));
            etSequenceNumber.setText(String.valueOf(item.getSequenceNumber()));
            etItemTex.setText(String.valueOf(item.getTax()));
            itemImageListForAdapter.addAll(item.getImageUrl());
            setViewEnable(false);
            getProductSpecificationGroup(productId, true);
            initRcvImage();
        }
        etItemDetail.setOnClickListener(this);
        etItemName.setOnClickListener(this);
        getImageSettings();
        initToolbarButton();
    }

    private void initRcvImage() {
        if (itemImageAdapter == null) {
            rcvItemImage.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager
                    .HORIZONTAL, false));
            itemImageAdapter = new ItemImageAdapter(this, itemImageListForAdapter,
                    deleteItemImage, itemImageList);
            rcvItemImage.setNestedScrollingEnabled(false);
            rcvItemImage.setAdapter(itemImageAdapter);
        } else {
            itemImageAdapter.notifyDataSetChanged();
        }
    }

    /**
     * this method call a webservice for getProductSpecificationGroup and sub specification
     * which is add by store user
     *
     * @param productId       productId in string
     * @param isAllowToModify is true when store add new specification otherwise false
     */
    private void getProductSpecificationGroup(final String productId, final boolean
            isAllowToModify) {
        Utilities.showCustomProgressDialog(this, false);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.SERVER_TOKEN, PreferenceHelper.getPreferenceHelper(this)
                    .getServerToken());
            jsonObject.put(Constant.PRODUCT_ID, productId);
            jsonObject.put(Constant.STORE_ID, PreferenceHelper.getPreferenceHelper(this)
                    .getStoreId());
        } catch (JSONException e) {
            Utilities.printLog("ProductDetail", e.getMessage());
        }

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SpecificationGroupFroAddItemResponse> responseCall = apiInterface
                .getSpecificationGroupFroAddItem(
                        (ApiClient.makeJSONRequestBody(jsonObject)));
        responseCall.enqueue(new Callback<SpecificationGroupFroAddItemResponse>() {
            @Override
            public void onResponse(Call<SpecificationGroupFroAddItemResponse> call,
                                   Response<SpecificationGroupFroAddItemResponse> response) {

                if (response.isSuccessful()) {

                    productSpecificationGroupOriginalList.clear();
                    if (response.body().isSuccess()) {
                        Utilities.printLog("SPECIFICATION_GROUP", ApiClient.JSONResponse(response
                                .body()));
                        productSpecificationGroupOriginalList.addAll(response.body()
                                .getSpecificationGroup());
                        if (itemSpecificationFinalList.isEmpty() && isAllowToModify) {
                            //noinspection unchecked
                            new MatchSpecification().execute(productSpecificationGroupOriginalList);
                        } else {
                            Utilities.hideCustomProgressDialog();
                        }
                    } else {
                        if (isAllowToModify) {
                            loadItemSpecificationAll();
                        }
                        Utilities.hideCustomProgressDialog();
                    }


                } else {
                    Utilities.showHttpErrorToast(response.code(), AddItemActivity.this);
                }

            }

            @Override
            public void onFailure(Call<SpecificationGroupFroAddItemResponse> call, Throwable t) {
                Utilities.printLog("ProductDetail", t.getMessage());
            }
        });
    }


    /**
     * load all data if no productGroup found
     **/

    private void loadItemSpecificationAll() {
        for (ItemSpecification itemSpecification : item.getSpecifications()) {
            for (ProductSpecification productSpecification : itemSpecification.getList()) {
                productSpecification.setIsUserSelected(true);
            }
        }
        itemSpecificationFinalList.addAll(item.getSpecifications());

        specificationListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        makeFinalListItemAndSpecification();
    }


    /**
     * this method used to make final list of product item specification
     */
    private void makeFinalListItemAndSpecification() {
        Utilities.printLog(TAG, "updateSpeciPosition" + Constant.updateSpeciPosition);
        Utilities.printLog("SPECIFICATIONS_UNIQUE_ID_COUNT", "beforeCount=" + item
                .getSpecificationsUniqueIdCount());
        if (Constant.itemSpeci != null) {
            Utilities.printLog(TAG, "Product Specification List Size " + Constant.itemSpeci
                    .getList()
                    .size());
            if (Constant.updateSpeciPosition == -1) {
                item.setSpecificationsUniqueIdCount(item.getSpecificationsUniqueIdCount() + 1);
                Utilities.printLog("SPECIFICATIONS_UNIQUE_ID_COUNT", "afterCount" + item
                        .getSpecificationsUniqueIdCount());
                Constant.itemSpeci.setUniqueId(item.getSpecificationsUniqueIdCount());
                itemSpecificationFinalList.add(Constant.itemSpeci);

            } else if (Constant.updateSpeciPosition > -1 && itemSpecificationFinalList.size() > 0) {
                itemSpecificationFinalList.remove(Constant.updateSpeciPosition);
                itemSpecificationFinalList.add(Constant.updateSpeciPosition, Constant.itemSpeci);
                Constant.updateSpeciPosition = -1;
            }
            specificationListAdapter.notifyDataSetChanged();
        }
        Constant.itemSpeci = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        setToolbarEditIcon(false, 0);
        setToolbarCameraIcon(false);
        return true;
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {
            case R.id.tvAddItemSpecification:
                if (isNewItem || isEditable) {
                    if (TextUtils.isEmpty(etProductName.getText().toString())) {
                        new CustomAlterDialog(this, null, getString(R.string
                                .msg_plz_select_product_first)) {
                            @Override
                            public void btnOnClick(int btnId) {
                                dismiss();
                            }
                        }.show();
                    } else if (productSpecificationGroupOriginalList.isEmpty()) {
                        new CustomAlterDialog(this, null, getString(R.string
                                .msg_plz_specification_in_product)) {
                            @Override
                            public void btnOnClick(int btnId) {
                                dismiss();
                            }
                        }.show();
                    } else {
                        openSpecificationGroupDialog();
                    }
                }
                break;

            case R.id.etProductName:
                openProductDialog();
                break;

            case R.id.etItemName:
                addMultiLanguageDetail(etItemName.getHint().toString(),
                        (List<String>) etItemName.getTag(),
                        new AddDetailInMultiLanguageDialog.SaveDetails() {
                            @Override
                            public void onSave(List<String> detailList) {
                                etItemName.setTag(detailList);
                                item.setNameList((List<String>) etItemName.getTag());
                                etItemName.setText(item.getName());

                            }
                        });
                break;
            case R.id.etItemDetail:
                addMultiLanguageDetail(etItemDetail.getHint().toString(),
                        (List<String>) etItemDetail.getTag(),
                        new AddDetailInMultiLanguageDialog.SaveDetails() {
                            @Override
                            public void onSave(List<String> detailList) {
                                etItemDetail.setTag(detailList);
                                item.setDetailsList((List<String>) etItemDetail.getTag());
                                etItemDetail.setText(item.getDetails());

                            }
                        });
                break;
            default:
                break;
        }
    }

    private void openSpecificationGroupDialog() {
        Utilities.printLog("SPEC", ApiClient.JSONResponse(productSpecificationGroupOriginalList));
        CustomSelectSpecificationGroupDialog customSelectProductDialog = new
                CustomSelectSpecificationGroupDialog(this, productSpecificationGroupOriginalList) {


                    @Override
                    public void onIteamSelected(ItemSpecification
                                                        specificationGroupItem) {
                        gotoAddItemSpecification(specificationGroupItem, -1);
                        dismiss();
                    }
                };
        customSelectProductDialog.show();
    }

    private void openProductDialog() {

        customSelectProductDialog = new CustomSelectProductDialog(this, CurrentProduct
                .getInstance().getProductDataList()) {
            @Override
            public void onProductIteamSelected(Product product) {
                etProductName.setText(product.getName());
                productId = product.getId();
                GlideApp.with(AddItemActivity.this).load(IMAGE_URL + product.getImageUrl())
                        .dontAnimate()
                        .into(ivItem);
                getProductSpecificationGroup(productId, false);
                dismiss();
            }
        };
        customSelectProductDialog.show();
    }

    private void setViewEnable(boolean enabled) {

        if (isNewItem) {
            etProductName.setEnabled(enabled);
        }
        etItemTex.setEnabled(enabled);
        etItemName.setEnabled(enabled);
        etItemDetail.setEnabled(enabled);
        etItemPrice.setEnabled(enabled);
        etSequenceNumber.setEnabled(enabled);
        switchVisibility.setEnabled(enabled);
        switchInStoke.setEnabled(enabled);
        specificationListAdapter.setClickableView(enabled);
        isEditable = enabled;
        itemImageAdapter.setIsEnable(enabled);
        itemImageAdapter.notifyDataSetChanged();
        if (enabled) {
            rcvItemImage.setVisibility(View.VISIBLE);
        } else if (!enabled && itemImageListForAdapter.isEmpty()) {
            rcvItemImage.setVisibility(View.GONE);
        } else {
            rcvItemImage.setVisibility(View.VISIBLE);
        }
        etItemPriceWithoutOffer.setEnabled(enabled);
        etOfferMessage.setEnabled(enabled);
    }

    private void validate() {


        if (TextUtils.isEmpty(etProductName.getText().toString().trim())) {
            new CustomAlterDialog(this, null, getString(R.string.msg_product_category)) {
                @Override
                public void btnOnClick(int btnId) {
                    dismiss();
                }
            }.show();
        } else if (TextUtils.isEmpty(etItemName.getText().toString().trim())) {
            new CustomAlterDialog(this, null, getString(R.string.msg_empty_item_title)) {
                @Override
                public void btnOnClick(int btnId) {
                    dismiss();
                }
            }.show();
        } else if (TextUtils.isEmpty(etItemPrice.getText().toString().trim())) {
            new CustomAlterDialog(this, null, getString(R.string.msg_empty_price)) {
                @Override
                public void btnOnClick(int btnId) {
                    dismiss();
                }
            }.show();
        } else if (TextUtils.isEmpty(etItemTex.getText().toString().trim())) {
            new CustomAlterDialog(this, null, getString(R.string.msg_empty_tax)) {
                @Override
                public void btnOnClick(int btnId) {
                    dismiss();
                }
            }.show();
        } else {
            addAndUpdateItem();
        }
    }

    /**
     * this method call a webservice for ADD and Update  item
     */
    private void addAndUpdateItem() {
        Utilities.showProgressDialog(this);
        for (ItemSpecification itemSpecification : itemSpecificationFinalList) {
            ArrayList<ProductSpecification> productSpecificationsFinal = new ArrayList<>();
            for (ProductSpecification productSpecification : itemSpecification.getList()) {
                if (productSpecification.isIsUserSelected()) {
                    productSpecificationsFinal.add(productSpecification);
                }

            }
            itemSpecification.setList(productSpecificationsFinal);
        }
        Item item = new Item();
        item.setStoreId(preferenceHelper.getStoreId());
        item.setServerToken(preferenceHelper.getServerToken());
        item.setProductId(productId);
        item.setNameList((List<String>) etItemName.getTag());
        item.setDetailsList((List<String>) etItemDetail.getTag());
        item.setPrice(Utilities.roundDecimal(Double.valueOf(etItemPrice.getText().toString())));
        item.setSequenceNumber(TextUtils.isEmpty(etSequenceNumber.getText().toString()) ? 0 :
                Long.parseLong(etSequenceNumber.getText().toString()));
        item.setItemInStock(switchInStoke.isChecked());
        item.setIsVisibleInStore(switchVisibility.isChecked());
        item.setSpecifications(itemSpecificationFinalList);
        item.setOfferMessageOrPercentage(etOfferMessage.getText().toString());
        item.setTax(Utilities.roundDecimal(Double.valueOf(etItemTex.getText().toString())));
        if (Utilities.isDecimalAndGraterThenZero(etItemPriceWithoutOffer.getText().toString())) {
            item.setItemPriceWithoutOffer(Utilities.roundDecimal(Double.valueOf
                    (etItemPriceWithoutOffer.getText()
                            .toString())));
        }
        Call<AddOrUpdateItemResponse> call;
        if (isNewItem) {
            Utilities.printLog("ADD_ITEM", ApiClient.JSONResponse(item));
            call = ApiClient.getClient().create(ApiInterface.class).addItem(ApiClient
                    .makeGSONRequestBody
                            (item));
        } else {
            item.setItemId(this.item.getId());
            Utilities.printLog("UPDATE_ITEM", ApiClient.JSONResponse(item));
            call = ApiClient.getClient().create(ApiInterface.class).updateItem(ApiClient
                    .makeGSONRequestBody
                            (item));
        }
        call.enqueue(new Callback<AddOrUpdateItemResponse>() {
            @Override
            public void onResponse(Call<AddOrUpdateItemResponse> call,
                                   Response<AddOrUpdateItemResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        if (itemImageList.size() > 0) {
                            addAndUpdateItemImage(response.body().getItem().getId());
                        } else if (deleteItemImage.size() > 0 && !isNewItem) {
                            deleteItemImage();
                        } else {
                            Utilities.removeProgressDialog();
                            Utilities.printLog("tag", "response - " + new Gson().toJson(response
                                    .body().getItem()));
                            onBackPressed();
                        }
                    } else {
                        Utilities.removeProgressDialog();
                        ParseContent.getParseContentInstance().showErrorMessage(AddItemActivity
                                .this, response.body().getErrorCode(), true);
                    }
                } else {
                    Utilities.removeProgressDialog();
                    Utilities.showHttpErrorToast(response.code(), AddItemActivity.this);
                }
            }

            @Override
            public void onFailure(Call<AddOrUpdateItemResponse> call, Throwable t) {
                Utilities.printLog("Exception", t.getMessage());
            }
        });
    }

    /**
     * this method call a webservice for ADD and Update  item image
     */
    private void addAndUpdateItemImage(String itemId) {

        HashMap<String, RequestBody> map = new HashMap<>();
        map.put(Constant.STORE_ID, ApiClient.makeTextRequestBody(PreferenceHelper
                .getPreferenceHelper(this).getStoreId()));
        map.put(Constant.SERVER_TOKEN,
                ApiClient.makeTextRequestBody(PreferenceHelper.getPreferenceHelper(this)
                        .getServerToken()));
        map.put(Constant.ITEM_ID, ApiClient.makeTextRequestBody(
                itemId));
        Call<AddOrUpdateItemResponse> call;
        if (isNewItem) {
            call = ApiClient.getClient().create(ApiInterface.class).addItemImage(map, ApiClient
                    .addMultipleImage(itemImageListForAdapter));
        } else {
            call = ApiClient.getClient().create(ApiInterface.class).addItemImage(map, ApiClient
                    .addMultipleImage(itemImageList));
        }
        call.enqueue(new Callback<AddOrUpdateItemResponse>() {
            @Override
            public void onResponse(Call<AddOrUpdateItemResponse> call,
                                   Response<AddOrUpdateItemResponse> response) {
                Utilities.removeProgressDialog();
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        if (isNewItem) {
                            itemImageList.clear();
                            itemImageList.addAll(response.body().getItem().getImageUrl());
                            onBackPressed();
                        } else if (deleteItemImage.size() > 0) {
                            deleteItemImage();
                        } else {
                            onBackPressed();
                        }


                    } else {
                        ParseContent.getParseContentInstance().showErrorMessage(AddItemActivity
                                .this, response.body().getErrorCode(), true);
                    }
                } else {
                    Utilities.showHttpErrorToast(response.code(), AddItemActivity.this);
                }
            }

            @Override
            public void onFailure(Call<AddOrUpdateItemResponse> call, Throwable t) {
                Utilities.printLog("Exception", t.getMessage());
            }
        });
    }

    public void deleteSpecification(ItemSpecification itemSpecification) {
        itemSpecificationFinalList.remove(itemSpecification);
        specificationListAdapter.notifyDataSetChanged();
    }


    public void gotoAddItemSpecification(ItemSpecification specificationGroupItem, int
            position) {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.FRAGMENT_TYPE, Constant.FRAGMENT_ADD_ITEM_SPECIFICATION);
        bundle.putString(Constant.TOOLBAR_TITLE, getString(R.string.text_add_item_specification));
        if (position != -1) {
            bundle.putParcelable(Constant.SPECIFICATIONS, itemSpecificationFinalList.get(position));
            bundle.putParcelable(Constant.PRODUCT_SPECIFICATION, null);
        } else {
            bundle.putParcelable(Constant.SPECIFICATIONS, null);
            bundle.putParcelable(Constant.PRODUCT_SPECIFICATION, specificationGroupItem);
        }

        Intent intent = new Intent(this, AddItemSpeciActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.enter, R.anim.exit);
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
                        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission
                                .READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                                ContextCompat.checkSelfPermission(this, android.Manifest
                                        .permission.READ_EXTERNAL_STORAGE) != PackageManager
                                        .PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(this, new String[]{android.Manifest
                                    .permission.READ_EXTERNAL_STORAGE}, Constant
                                    .PERMISSION_CHOOSE_PHOTO);
                        } else {
                           /* Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                            startActivityForResult(galleryIntent, Constant
                            .PERMISSION_CHOOSE_PHOTO);*/
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
                        if (Utilities.checkImageFileType(AddItemActivity.this, imageSetting
                                .getImageType(), uri)) {
                            if (Utilities.checkIMGSize(uri, imageSetting
                                    .getItemImageMaxWidth(), imageSetting.getItemImageMaxHeight
                                    (), imageSetting.getItemImageMinWidth(), imageSetting
                                    .getItemImageMinHeight(), imageSetting.getItemImageRatio())) {
                                setImage(uri);
                            } else {
                                beginCrop(uri);
                            }
                        }
                    }
                    break;

                case Constant.PERMISSION_TAKE_PHOTO:
                    if (uri != null) {
                        if (Utilities.checkImageFileType(AddItemActivity.this, imageSetting
                                .getImageType(), uri)) {
                            if (Utilities.checkIMGSize(uri,
                                    imageSetting
                                            .getItemImageMaxWidth(), imageSetting
                                            .getItemImageMaxHeight

                                                    (), imageSetting.getItemImageMinWidth(),
                                    imageSetting
                                            .getItemImageMinHeight(), imageSetting.getItemImageRatio
                                            ())) {

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
        final String path = ImageHelper.getFromMediaUriPfd(this, getContentResolver
                (), uri).getPath();
        new ImageCompression(this).setImageCompressionListener(new ImageCompression
                .ImageCompressionListener() {
            @Override
            public void onImageCompression(String compressionImagePath) {
                if (isNewItem) {
                    GlideApp.with(AddItemActivity.this).load(uri).error
                            (R.drawable
                                    .icon_default_profile).override(300, 300).into(ivItem);
                }
                itemImageList.add(compressionImagePath);
                itemImageListForAdapter.add(compressionImagePath);
                initRcvImage();
            }
        }).execute(path);

    }

    /**
     * this method call a webservice for delete item image in list
     */
    private void deleteItemImage() {
        Item deleteItem = new Item();
        deleteItem.setServerToken(preferenceHelper.getServerToken());
        deleteItem.setImageUrl(deleteItemImage);
        deleteItem.setId(item.getId());
        deleteItem.setStoreId(preferenceHelper.getStoreId());

        Utilities.printLog("DELETE_IMAGE", ApiClient.JSONResponse(deleteItem));
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<IsSuccessResponse> responseCall = apiInterface
                .deleteItemImage(
                        (ApiClient.makeGSONRequestBody(deleteItem)));
        responseCall.enqueue(new Callback<IsSuccessResponse>() {
            @Override
            public void onResponse(Call<IsSuccessResponse> call, Response<IsSuccessResponse>
                    response) {
                Utilities.removeProgressDialog();
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        onBackPressed();
                    } else {
                        ParseContent.getParseContentInstance().showErrorMessage(AddItemActivity
                                .this, response.body().getErrorCode(), true);
                    }
                } else {
                    Utilities.showHttpErrorToast(response.code(), AddItemActivity.this);
                }
            }

            @Override
            public void onFailure(Call<IsSuccessResponse> call, Throwable t) {
                Utilities.printLog("Exception", t.getMessage());
            }
        });
    }

    public void addItemImage() {
        if (isNewItem || isEditable) {
            showPictureDialog();
        }
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
                        .getItemImageMaxWidth()
                , imageSetting.getItemImageMaxHeight())
                .checkValidIMGCrop(true).setCropData(imageSetting
                .getItemImageMaxWidth(), imageSetting.getItemImageMaxHeight(), imageSetting
                .getItemImageMinWidth(), imageSetting.getItemImageMinHeight(), imageSetting
                .getItemImageRatio()).start
                (this);
    }

    /**
     * Match specification from particular item and product specification and
     * it make a new list of specification
     */
    private class MatchSpecification extends AsyncTask<ArrayList<ItemSpecification>, Void,
            ArrayList<ItemSpecification>> {

        @SafeVarargs
        @Override
        protected final ArrayList<ItemSpecification> doInBackground(ArrayList<ItemSpecification>...
                                                                            arrayLists) {
            ArrayList<ProductSpecification> matchList = new ArrayList<>();
            ArrayList<ProductSpecification> tempList = null;
            for (ItemSpecification itemSpecification : item.getSpecifications()) {
                for (ItemSpecification productSpecification : arrayLists[0]) {
                    if (TextUtils.equals(itemSpecification.getId(),
                            productSpecification.getId())) {
                        matchList.clear();
                        for (ProductSpecification productSubSpecification : productSpecification
                                .getList()) {
                            tempList = new ArrayList<>(productSpecification.getList());
                            for (ProductSpecification itemSubSpecification : itemSpecification
                                    .getList()) {
                                itemSubSpecification.setIsUserSelected(true);
                                if (TextUtils.equals(productSubSpecification.getId(),
                                        itemSubSpecification
                                                .getId())) {
                                    matchList.add(productSubSpecification);
                                    break;
                                }
                            }
                        }
                        if (tempList != null) {
                            for (ProductSpecification match : matchList) {
                                tempList.remove(match);
                            }
                            itemSpecification.getList().addAll(tempList);
                        }
                    }
                }
            }
            return item.getSpecifications();
        }

        @Override
        protected void onPostExecute(ArrayList<ItemSpecification> itemSpecifications) {
            super.onPostExecute(itemSpecifications);

            itemSpecificationFinalList.addAll(itemSpecifications);

            specificationListAdapter.notifyDataSetChanged();
            Utilities.hideCustomProgressDialog();
        }
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

    private void initToolbarButton() {
        CustomTextView tvtoolbarbtn = (CustomTextView) findViewById(R.id.tvtoolbarbtn);
        tvtoolbarbtn.setOnClickListener(v -> {
            if (isNewItem || isEditable) {
                validate();
            } else {
                isEditable = true;
                setViewEnable(true);
                tvtoolbarbtn.setText(R.string.text_save);
                specificationListAdapter.notifyDataSetChanged();
            }
        });
        tvtoolbarbtn.setVisibility(View.VISIBLE);
        if (PreferenceHelper.getPreferenceHelper(this).getIsStoreEditItem()) {
            if (isNewItem) {
                tvtoolbarbtn.setText(R.string.text_save);
            } else {
                tvtoolbarbtn.setText(R.string.text_edit);
            }
        } else {
            tvtoolbarbtn.setText(R.string.text_edit);
        }

    }
}
