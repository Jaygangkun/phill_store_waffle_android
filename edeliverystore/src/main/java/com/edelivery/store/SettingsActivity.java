package com.edelivery.store;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.edelivery.store.component.CustomAlterDialog;
import com.edelivery.store.models.datamodel.Languages;
import com.edelivery.store.models.datamodel.StoreData;
import com.edelivery.store.models.datamodel.StoreSettings;
import com.edelivery.store.models.datamodel.StoreTime;
import com.edelivery.store.models.responsemodel.StoreDataResponse;
import com.edelivery.store.models.singleton.Language;
import com.edelivery.store.parse.ApiClient;
import com.edelivery.store.parse.ApiInterface;
import com.edelivery.store.parse.ParseContent;
import com.edelivery.store.utils.Constant;
import com.edelivery.store.utils.PreferenceHelper;
import com.edelivery.store.utils.ResizeAnimation;
import com.edelivery.store.utils.Utilities;
import com.edelivery.store.widgets.CustomInputEditText;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.edelivery.store.utils.Constant.FAMOUS_TAG_LIST;
import static com.edelivery.store.utils.Constant.REQUEST_STORE_TIME;
import static com.edelivery.store.utils.Constant.STORE_LOCATION_RESULT;

public class SettingsActivity extends BaseActivity implements CompoundButton
        .OnCheckedChangeListener
{

    public boolean isEditable;
    String priceRatings;
    private int chargeType;
    private EditText etAddress, etLat, etLng, etTax, etFreeDeliveryPrice, etVerifyPassword,
            etSlogan,
            etWebsite;
    private CustomInputEditText etCancellationChargeAbovePrice, etCancellationChargeValue,
            etMaxQuantity, etMinimumOrderPrice, etScheduleOrderCreateAfterMinute,
            etInformScheduleOrderBeforeMinute, etDeliveryRadius, etFreeDeliveryRadius,
            etDeliveryTime, etDeliveryTimeMax;
    private SwitchCompat switchBusiness, switchPayDeliveryFees, switchOrderCancellationCharge,
            switchProviderDeliveryAnywhere, switchTakingScheduleOrder, switchBusy,
            switchAskEstimatedTime, switchProvidePickupDelivery, switchIsUseItemTax;
    private SwitchCompat switchIsSetDeliveryStoreTime;
    private RecyclerView rvStoreTime;
    private Dialog dialog;
    private CustomTextView tvScheduleStoreTime, tvCurrency, tvAddNewTag;
    private ArrayList<StoreTime> storeTimeList, storeDeliveryTimeList;
    private LatLng latLng;
    private ImageView ivStoreLocation;
    private Spinner spinnerPriceRatting, spinnerChargeType;
    private LinearLayout llPayDeliveryFees, llScheduleOrder, llDeliveryAnyWhere,
            llCancellationCharge;
    private StoreData storeData;
    private CardView cvCancellationCharge;
    private LinearLayout llLangs;
    private List<Languages> selectedLanguages;
    private List<Languages> adminLanguagges;
    private TextView tvAddNewStoreDeliverySlot, tvSelectLanguages;
    private List<Languages> tempSelectedLangs = new ArrayList<>();
    private CustomTextView tvtoolbarbtn;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbar(toolbar, R.drawable.ic_back, R.color.color_app_theme);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utilities.hideSoftKeyboard(SettingsActivity.this);
                onBackPressed();
            }
        });
        ((TextView) findViewById(R.id.tvToolbarTitle)).setText(getString(R.string.text_settings));
        initToolbarButton();
        tvCurrency = (CustomTextView) findViewById(R.id.tvCurrency);
        switchPayDeliveryFees = (SwitchCompat) findViewById(R.id.switchPayDeliveryFees);
        spinnerPriceRatting = (Spinner) findViewById(R.id.spinnerPriceRatting);
        etAddress = (EditText) findViewById(R.id.etAddress);
        etLat = (EditText) findViewById(R.id.etLat);
        etLng = (EditText) findViewById(R.id.etLng);
        etTax = (EditText) findViewById(R.id.etTax);
        etSlogan = (EditText) findViewById(R.id.etSlogan);
        etWebsite = (EditText) findViewById(R.id.etWebsite);
        etFreeDeliveryPrice = (EditText) findViewById(R.id.etFreeDeliveryPrice);
        switchBusiness = (SwitchCompat) findViewById(R.id.switchBusiness);
        rvStoreTime = (RecyclerView) findViewById(R.id.rvStoreTime);
        ivStoreLocation = (ImageView) findViewById(R.id.ivStoreLocation);
        ivStoreLocation.setOnClickListener(this);
        tvScheduleStoreTime = (CustomTextView) findViewById(R.id.tvAddNewStoreTime);
        storeTimeList = new ArrayList<>();
        storeDeliveryTimeList = new ArrayList<>();
        tvScheduleStoreTime.setOnClickListener(this);
        tvCurrency.setText("(" + preferenceHelper.getCurrency() + ")");
        switchPayDeliveryFees.setChecked(preferenceHelper.getIsPushNotificationSoundOn());
        etCancellationChargeAbovePrice = (CustomInputEditText) findViewById(R.id
                .etCancellationChargeAbovePrice);
        etCancellationChargeValue = (CustomInputEditText) findViewById(R.id
                .etCancellationChargeValue);
        etMaxQuantity = (CustomInputEditText) findViewById(R.id.etMaxQuantity);
        etMinimumOrderPrice = (CustomInputEditText) findViewById(R.id.etMinimumOrderPrice);
        etScheduleOrderCreateAfterMinute = (CustomInputEditText) findViewById(R.id
                .etScheduleOrderCreateAfterMinute);
        etInformScheduleOrderBeforeMinute = (CustomInputEditText) findViewById(R.id
                .etInformScheduleOrderBeforeMinute);
        etDeliveryRadius = (CustomInputEditText) findViewById(R.id.etDeliveryRadius);
        etDeliveryTime = (CustomInputEditText) findViewById(R.id.etDeliveryTime);
        etDeliveryTimeMax = (CustomInputEditText) findViewById(R.id.etDeliveryTimeMax);
        switchOrderCancellationCharge = (SwitchCompat) findViewById(R.id
                .switchOrderCancellationCharge);
        switchProviderDeliveryAnywhere = (SwitchCompat) findViewById(R.id
                .switchProviderDeliveryAnywhere);
        switchTakingScheduleOrder = (SwitchCompat) findViewById(R.id.switchTakingScheduleOrder);
        switchIsSetDeliveryStoreTime = (SwitchCompat) findViewById(R.id.switchIsSetDeliveryTime);
        spinnerChargeType = (Spinner) findViewById(R.id.spinnerChargeType);
        llPayDeliveryFees = (LinearLayout) findViewById(R.id.llPayDeliveryFees);
        llScheduleOrder = (LinearLayout) findViewById(R.id.llScheduleOrder);
        llDeliveryAnyWhere = (LinearLayout) findViewById(R.id.llDeliveryAnyWhere);
        llCancellationCharge = (LinearLayout) findViewById(R.id.llCancellationCharge);
        etFreeDeliveryRadius = (CustomInputEditText) findViewById(R.id.etFreeDeliveryRadius);
        switchBusy = (SwitchCompat) findViewById(R.id.switchBusy);
        switchAskEstimatedTime = (SwitchCompat) findViewById(R.id.switchAskEstimatedTime);
        tvAddNewTag = (CustomTextView) findViewById(R.id.tvAddNewTag);
        switchProvidePickupDelivery = findViewById(R.id.switchProvidePickupDelivery);
        tvAddNewStoreDeliverySlot = findViewById(R.id.tvAddNewStoreDeliverySlot);
        tvSelectLanguages = findViewById(R.id.tvSelectLanguages);
        tvSelectLanguages.setOnClickListener(this);
        tvAddNewTag.setOnClickListener(this);
        switchIsSetDeliveryStoreTime.setOnCheckedChangeListener(this);
        switchIsSetDeliveryStoreTime.setChecked(true);
        switchPayDeliveryFees.setOnCheckedChangeListener(this);
        switchOrderCancellationCharge.setOnCheckedChangeListener(this);
        switchProviderDeliveryAnywhere.setOnCheckedChangeListener(this);
        switchTakingScheduleOrder.setOnCheckedChangeListener(this);
        switchAskEstimatedTime.setOnClickListener(this);

        etAddress.setOnClickListener(this);
        switchIsUseItemTax = findViewById(R.id.switchIsUseItemTax);
        cvCancellationCharge = findViewById(R.id.cvCancellationCharge);

        cvCancellationCharge.setVisibility(PreferenceHelper.getPreferenceHelper(this)
                .getIsStoreCanSetCancellationCharge() ? View.VISIBLE : View.GONE);

        selectedLanguages = Language.getInstance().getStoreLanguages();
        adminLanguagges = Language.getInstance().getAdminLanguages();


        initSpinnerCancellationChargeType();
        initSpinnerForPriceRatings();
        setEnableView(false);
        getSettingsData();

        selectedLanguages = Language.getInstance().getStoreLanguages();
        adminLanguagges = Language.getInstance().getAdminLanguages();
        tempSelectedLangs.addAll(selectedLanguages);

    }


    private void setEnableView(boolean isEnable) {
        etTax.setEnabled(isEnable);
        etFreeDeliveryPrice.setEnabled(isEnable);
        etSlogan.setEnabled(isEnable);
        etWebsite.setEnabled(isEnable);
        switchBusiness.setEnabled(isEnable);
        switchBusy.setEnabled(isEnable);
        switchPayDeliveryFees.setEnabled(isEnable);
        tvScheduleStoreTime.setEnabled(isEnable);
        tvAddNewStoreDeliverySlot.setEnabled(isEnable);
        tvSelectLanguages.setEnabled(isEnable);
        etLat.setFocusableInTouchMode(isEnable);
        etLng.setFocusableInTouchMode(isEnable);
        etTax.setFocusableInTouchMode(isEnable);
        etFreeDeliveryPrice.setFocusableInTouchMode(isEnable);
        etSlogan.setFocusableInTouchMode(isEnable);
        etWebsite.setFocusableInTouchMode(isEnable);
        etDeliveryTime.setFocusableInTouchMode(isEnable);
        etDeliveryTimeMax.setFocusableInTouchMode(isEnable);
        etDeliveryTime.setEnabled(isEnable);
        etDeliveryTimeMax.setEnabled(isEnable);
        spinnerPriceRatting.setEnabled(isEnable);
        ivStoreLocation.setEnabled(isEnable);

        etFreeDeliveryRadius.setEnabled(isEnable);
        etCancellationChargeAbovePrice.setEnabled(isEnable);
        etCancellationChargeValue.setEnabled(isEnable);
        etMaxQuantity.setEnabled(isEnable);
        etMinimumOrderPrice.setEnabled(isEnable);
        etScheduleOrderCreateAfterMinute.setEnabled(isEnable);
        etInformScheduleOrderBeforeMinute.setEnabled(isEnable);
        etDeliveryRadius.setEnabled(isEnable);
        etAddress.setEnabled(isEnable);
        etFreeDeliveryRadius.setFocusableInTouchMode(isEnable);
        etCancellationChargeAbovePrice.setFocusableInTouchMode(isEnable);
        etCancellationChargeValue.setFocusableInTouchMode(isEnable);
        etMaxQuantity.setFocusableInTouchMode(isEnable);
        etMinimumOrderPrice.setFocusableInTouchMode(isEnable);
        etScheduleOrderCreateAfterMinute.setFocusableInTouchMode(isEnable);
        etInformScheduleOrderBeforeMinute.setFocusableInTouchMode(isEnable);
        etDeliveryRadius.setFocusableInTouchMode(isEnable);


        switchOrderCancellationCharge.setEnabled(isEnable);
        switchProviderDeliveryAnywhere.setEnabled(isEnable);
        switchTakingScheduleOrder.setEnabled(isEnable);

        spinnerChargeType.setEnabled(isEnable);
        tvAddNewTag.setEnabled(isEnable);
        switchAskEstimatedTime.setEnabled(isEnable);
        switchProvidePickupDelivery.setEnabled(isEnable);
        switchIsUseItemTax.setEnabled(isEnable);
        switchIsSetDeliveryStoreTime.setEnabled(isEnable);


    }

    /**
     * this method call webservice for get setting detail
     */
    protected void getSettingsData() {
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
                Utilities.printLog("getSettingsData", new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        storeData = response.body().getStoreDetail();
                        preferenceHelper.putIsStoreCanCreateGroup(storeData.
                                getDeliveryDetails().isStoreCanCreateGroup());
                        storeTimeList.clear();
                        storeDeliveryTimeList.clear();
                        storeTimeList.addAll(response.body().getStoreDetail().getStoreTime());
                        storeDeliveryTimeList.addAll(response.body().getStoreDetail().getStoreDeliveryTime());
                        setSettingsData();
                    } else {
                        ParseContent.getParseContentInstance().showErrorMessage(SettingsActivity
                                .this, response.body().getErrorCode(), true);
                    }
                } else {
                    Utilities.showHttpErrorToast(response.code(), SettingsActivity.this);

                }
            }

            @Override
            public void onFailure(Call<StoreDataResponse> call, Throwable t) {
                Utilities.printLog("BaseActivity", t.getMessage());
            }
        });
    }

    private void setSettingsData() {

        if (storeData != null) {
            etAddress.setText(storeData.getAddress());
            etLat.setText(String.valueOf(storeData.getLocation().get(0)));
            etLng.setText(String.valueOf(storeData.getLocation().get(1)));
            etTax.setText(String.valueOf(storeData.getItemTax()));
            etSlogan.setText(storeData.getSlogan());
            etWebsite.setText(storeData.getWebsiteUrl());
            etFreeDeliveryPrice.setText(String.valueOf(storeData.getFreeDeliveryPrice()));

            priceRatings = String.valueOf(storeData.getPriceRating());

            TypedArray array2 = getResources().obtainTypedArray(R.array.price_rating);
            for (int i = 0; i < array2.length(); i++) {
                if (TextUtils.equals(priceRatings, array2.getString(i))) {
                    spinnerPriceRatting.setSelection(i);
                    break;
                }

            }
            etCancellationChargeAbovePrice.setText(String.valueOf(storeData
                    .getOrderCancellationChargeForAboveOrderPrice()));
            etCancellationChargeValue.setText(String.valueOf(storeData
                    .getOrderCancellationChargeValue()));
            etMaxQuantity.setText(String.valueOf(storeData.getMaxItemQuantityAddByUser()));
            etMinimumOrderPrice.setText(String.valueOf(storeData.getMinOrderPrice()));
            etScheduleOrderCreateAfterMinute.setText(String.valueOf(storeData
                    .getScheduleOrderCreateAfterMinute()));
            etInformScheduleOrderBeforeMinute.setText(String.valueOf(storeData
                    .getInformScheduleOrderBeforeMin()));
            etFreeDeliveryRadius.setText(String.valueOf(storeData.getFreeDeliveryWithinRadius()));
            etDeliveryRadius.setText(String.valueOf(storeData.getDeliveryRadius()));
            switchIsSetDeliveryStoreTime.setChecked(storeData.isStoreSetScheduleDeliveryTime());
            switchPayDeliveryFees.setChecked(storeData.isStorePayDeliveryFees());
            switchOrderCancellationCharge.setChecked(storeData.isIsOrderCancellationChargeApply());
            switchProviderDeliveryAnywhere.setChecked(storeData.isIsProvideDeliveryAnywhere());
            switchTakingScheduleOrder.setChecked(storeData.isIsTakingScheduleOrder());
            switchAskEstimatedTime.setChecked(storeData.isAskEstimatedTimeForReadyOrder());
            switchBusiness.setChecked(storeData.isBusiness());
            switchBusy.setChecked(storeData.isBusy());
            switchProvidePickupDelivery.setChecked(storeData.isProvidePickupDelivery());
            scaleUpAndDown(llPayDeliveryFees, storeData.isStorePayDeliveryFees(), R.dimen
                    .dimen_expand_penal_content_2);
            scaleUpAndDown(llCancellationCharge, storeData.isIsOrderCancellationChargeApply(), R
                    .dimen.dimen_expand_penal_content_3);
            scaleUpAndDown(llDeliveryAnyWhere, !storeData.isIsProvideDeliveryAnywhere(), R.dimen
                    .dimen_expand_penal_content_1);

            scaleUpAndDown(llScheduleOrder, storeData.isIsTakingScheduleOrder(), R.dimen
                    .dimen_expand_penal_content_1);
            chargeType = storeData.getOrderCancellationChargeType();
            TypedArray array4 = getResources().obtainTypedArray(R.array
                    .cancellation_charge_type_value);
            for (int i = 0; i < array4.length(); i++) {
                if (TextUtils.equals(String.valueOf(chargeType), array4.getString(i))) {
                    spinnerChargeType.setSelection(i);
                    break;
                }

            }
            etDeliveryTime.setText(String.valueOf(storeData.getDeliveryTime()));
            etDeliveryTimeMax.setText(String.valueOf(storeData.getDeliveryTimeMax()));
            switchIsUseItemTax.setChecked(storeData.isUseItemTax());

        }
    }

    private void validate() {


        Gson gson = new GsonBuilder().create();
        Utilities.printLog("store_time", String.valueOf(gson.toJsonTree(storeTimeList)
                .getAsJsonArray()));

        if (TextUtils.isEmpty(etAddress.getText().toString())) {
            etAddress.setError(this.getResources().getString(R.string.msg_empty_address));
        } else if (TextUtils.isEmpty(etLat.getText().toString())) {
            etAddress.setError(this.getResources().getString(R.string.msg_empty_address));
        } else if (TextUtils.isEmpty(etLng.getText().toString())) {
            etAddress.setError(this.getResources().getString(R.string.msg_empty_address));
        } else if (isInvalidNumber(etFreeDeliveryPrice, switchPayDeliveryFees.isChecked())) {
            etFreeDeliveryPrice.setError(getResources().getString(R.string
                    .msg_plz_enter_valid_amount));
            etFreeDeliveryPrice.requestFocus();
        } else if (isInvalidNumber(etFreeDeliveryRadius, switchPayDeliveryFees.isChecked
                ())) {
            etFreeDeliveryRadius.setError(getResources().getString(R.string
                    .msg_invalid_data));
            etFreeDeliveryRadius.requestFocus();

        } else if (isInvalidNumber(etCancellationChargeAbovePrice, switchOrderCancellationCharge
                .isChecked
                        ())) {
            etCancellationChargeAbovePrice.setError(getResources().getString(R.string
                    .msg_plz_enter_valid_amount));
            etCancellationChargeAbovePrice.requestFocus();

        } else if (isInvalidNumber(etCancellationChargeValue, switchOrderCancellationCharge
                .isChecked
                        ())) {
            etCancellationChargeValue.setError(getResources().getString(R.string
                    .msg_plz_enter_valid_amount));
            etCancellationChargeValue.requestFocus();

        } else if (isInvalidNumber(etDeliveryRadius, !switchProviderDeliveryAnywhere.isChecked
                ())) {
            etDeliveryRadius.setError(getResources().getString(R.string.msg_invalid_data));
            etDeliveryRadius.requestFocus();

        } else if (isInvalidNumber(etScheduleOrderCreateAfterMinute, switchTakingScheduleOrder
                .isChecked
                        ())) {
            etScheduleOrderCreateAfterMinute.setError(getResources().getString(R.string
                    .msg_invalid_data));
            etScheduleOrderCreateAfterMinute.requestFocus();

        } else if (isInvalidNumber(etInformScheduleOrderBeforeMinute, switchTakingScheduleOrder
                .isChecked
                        ())) {
            etInformScheduleOrderBeforeMinute.setError(getResources().getString(R.string
                    .msg_invalid_data));
            etInformScheduleOrderBeforeMinute.requestFocus();

        } else if (isInvalidNumber(etMinimumOrderPrice, true)) {
            etMinimumOrderPrice.setError(getResources().getString(R.string
                    .msg_plz_enter_valid_amount));
            etMinimumOrderPrice.requestFocus();

        } else if (isInvalidNumber(etDeliveryTime, true)) {
            etDeliveryTime.setError(getResources().getString(R.string
                    .msg_plz_enter_valid_time));
            etDeliveryTime.requestFocus();

        } else if (isInvalidNumber(etDeliveryTimeMax, true) || Integer.valueOf(etDeliveryTimeMax
                .getText().toString()) <= Integer.valueOf(etDeliveryTime
                .getText().toString())) {
            etDeliveryTimeMax.setError(getResources().getString(R.string
                    .msg_plz_enter_valid_time_max));
            etDeliveryTimeMax.requestFocus();

        } else if (isInvalidNumber(etMaxQuantity, true)) {
            etMaxQuantity.setError(getResources().getString(R.string
                    .msg_invalid_data));
            etMaxQuantity.requestFocus();

        } else if (isInvalidNumber(etTax, true)) {
            etTax.setError(getResources().getString(R.string
                    .msg_plz_enter_valid_amount));
            etTax.requestFocus();
        } else if (isInvalidNumber(etDeliveryRadius, !switchProviderDeliveryAnywhere.isChecked())
                && !switchProvidePickupDelivery.isChecked()) {
            Utilities.showToast(this, getResources().getString(R.string
                    .msg_plz_enter_valid_radius));
        } else if (Double.valueOf(etDeliveryRadius.getText().toString().trim()) <= 0 &&
                !switchProviderDeliveryAnywhere.isChecked()) {
            Utilities.showToast(this, getResources().getString(R.string
                    .msg_plz_enter_valid_radius));
        } else {
            showVerificationDialog();
        }
    }

    private boolean isInvalidNumber(EditText view, boolean isRequired) {
        if (isRequired) {
            try {
                return Double.valueOf(view.getText().toString()) < 0;
            } catch (NumberFormatException e) {
                return true;
            }
        } else {
            return false;
        }


    }


    /**
     * this method call webservice for update setting detail
     *
     * @param currentPassword
     */
    private void updateSettingsData(String currentPassword) {


        Utilities.showProgressDialog(this);

        StoreSettings storeDataSend = new StoreSettings();
        storeDataSend.setStoreSetScheduleDeliveryTime(switchIsSetDeliveryStoreTime.isChecked());
        storeDataSend.setStoreId(PreferenceHelper.getPreferenceHelper(this).getStoreId());
        storeDataSend.setServerToken(PreferenceHelper.getPreferenceHelper(this).getServerToken());
        storeDataSend.setAddress(etAddress.getText().toString().trim());
        storeDataSend.setLatitude(Double.valueOf(etLat.getText().toString()));
        storeDataSend.setLongitude(Double.valueOf(etLng.getText().toString()));
        storeDataSend.setBusiness(switchBusiness.isChecked());
        storeDataSend.setItemTax(Utilities.roundDecimal(Double.valueOf(etTax.getText().toString()
        )));
        storeDataSend.setFreeDeliveryPrice(Utilities.roundDecimal(Double.valueOf(etFreeDeliveryPrice
                .getText().toString())));
        storeDataSend.setSlogan(etSlogan.getText().toString().trim());
        storeDataSend.setWebsiteUrl(etWebsite.getText().toString().trim());
        storeDataSend.setStorePayDeliveryFees(switchPayDeliveryFees.isChecked());
        storeDataSend.setPriceRating(Integer.valueOf(priceRatings));
        storeDataSend.setMinOrderPrice(Utilities.roundDecimal(Double.valueOf(etMinimumOrderPrice
                .getText().toString())));
        storeDataSend.setInformScheduleOrderBeforeMin(Integer.valueOf
                (etInformScheduleOrderBeforeMinute.getText().toString()));
        storeDataSend.setIsTakingScheduleOrder(switchTakingScheduleOrder.isChecked
                ());
        storeDataSend.setDeliveryRadius(Utilities.roundDecimal(Double
                .valueOf(etDeliveryRadius.getText().toString())));
        storeDataSend.setIsProvideDeliveryAnywhere(switchProviderDeliveryAnywhere
                .isChecked());
        storeDataSend.setScheduleOrderCreateAfterMinute(Integer.valueOf
                (etScheduleOrderCreateAfterMinute.getText().toString()));
        storeDataSend.setMaxItemQuantityAddByUser(Integer.valueOf(etMaxQuantity.getText()
                .toString()));
        storeDataSend.setOrderCancellationChargeValue(Utilities.roundDecimal(Double.valueOf
                (etCancellationChargeValue.getText().toString())));
        storeDataSend.setOrderCancellationChargeType(chargeType);
        storeDataSend.setOrderCancellationChargeForAboveOrderPrice(Utilities.roundDecimal(Double
                .valueOf(etCancellationChargeAbovePrice.getText
                        ().toString())));
        storeDataSend.setIsOrderCancellationChargeApply(switchOrderCancellationCharge.isChecked());
        storeDataSend.setBusy(switchBusy.isChecked());
        storeDataSend.setFreeDeliveryWithinRadius(Utilities.roundDecimal(Double
                .valueOf(etFreeDeliveryRadius.getText()
                        .toString())));
        storeDataSend.setAskEstimatedTimeForReadyOrder(switchAskEstimatedTime
                .isChecked());
        storeDataSend.setDeliveryTime(Integer.valueOf(etDeliveryTime.getText()
                .toString()));
        storeDataSend.setDeliveryTimeMax(Integer.valueOf(etDeliveryTimeMax.getText
                ().toString()));
        storeDataSend.setProvidePickupDelivery(switchProvidePickupDelivery
                .isChecked());
        if (TextUtils.isEmpty(preferenceHelper.getSocialId())) {
            storeDataSend.setOldPassword(currentPassword);
            storeDataSend.setSocialId("");
            storeDataSend.setLoginBy(Constant.MANUAL);
        } else {
            storeDataSend.setOldPassword("");
            storeDataSend.setSocialId(preferenceHelper.getSocialId());
            storeDataSend.setLoginBy(Constant.SOCIAL);
        }
        storeDataSend.setStoreTime(storeTimeList);
        storeDataSend.setFamousProductsTags(storeData.getFamousProductsTags());
        storeDataSend.setUseItemTax(switchIsUseItemTax.isChecked());

        for (Languages storeLanguage : selectedLanguages) {
            storeLanguage.setVisible(false);
        }

        for (Languages language : tempSelectedLangs) {
            boolean isLanguageIncluded = false;
            for (Languages storeLanguage : selectedLanguages) {
                if (language.getCode().equals(storeLanguage.getCode())) {
                    storeLanguage.setVisible(true);
                    isLanguageIncluded = true;
                    break;
                }
            }
            if (!isLanguageIncluded)
                selectedLanguages.add(language);

        }
        storeDataSend.setLanguages(selectedLanguages);


        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<StoreDataResponse> call = apiInterface.updateSettings(ApiClient
                .makeGSONRequestBody(storeDataSend));
        call.enqueue(new Callback<StoreDataResponse>() {
            @Override
            public void onResponse(Call<StoreDataResponse> call, Response<StoreDataResponse>
                    response) {
                Utilities.removeProgressDialog();
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        parseContent.parseStoreData(response.body());
                        tvtoolbarbtn.setText(R.string.text_edit);
                        setEnableView(false);
                        isEditable = false;
                        ParseContent.getParseContentInstance().showMessage(SettingsActivity.this,
                                response.body().getMessage());
                    } else {
                        ParseContent.getParseContentInstance().showErrorMessage(SettingsActivity
                                .this, response.body().getErrorCode(), true);
                    }
                } else {
                    Utilities.showHttpErrorToast(response.code(), SettingsActivity.this);
                }
            }

            @Override
            public void onFailure(Call<StoreDataResponse> call, Throwable t) {
                Utilities.printLog("Settings", t.getMessage());
            }
        });
    }


    private void showVerificationDialog() {

        if (TextUtils.isEmpty(preferenceHelper.getSocialId())) {
            if (dialog == null) {
                dialog = new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_account_verification);
                etVerifyPassword = (TextInputEditText) dialog.findViewById(R.id.etCurrentPassword);

                dialog.findViewById(R.id.btnPositive).setOnClickListener(new View.OnClickListener
                        () {


                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(etVerifyPassword.getText().toString())) {
                            updateSettingsData(etVerifyPassword.getText().toString());
                            dialog.dismiss();
                        } else {
                            etVerifyPassword.setError(getString(R.string.msg_empty_password));
                        }
                    }
                });
                dialog.findViewById(R.id.btnNegative).setOnClickListener(new View.OnClickListener
                        () {


                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

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
            updateSettingsData("");
        }


    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {

            case R.id.tvAddNewStoreTime:
                goToStoreTimeActivity();
                break;
            case R.id.tvAddNewStoreDeliverySlot:
                goToStoreDeliveryTimeActivity();
                break;
            case R.id.tvSelectLanguages:
                openAdminLanguageDialog();
                break;
            case R.id.ivStoreLocation:
            case R.id.etAddress:
                setStoreAddressORLocation();
                break;
            case R.id.tvAddNewTag:
                goToFamousForActivity(storeData);
                break;
        }

    }


    /**
     * @deprecated
     */
    private void getLatLngFromAddress() {
        Utilities.showProgressDialog(this);
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
                            new CustomAlterDialog(SettingsActivity.this, null, getResources()
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
                    Utilities.showHttpErrorToast(response.code(), SettingsActivity.this);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Utilities.printLog(SettingsActivity.class.getName(), t.getMessage());
            }
        });
    }

    private void initSpinnerCancellationChargeType() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.cancellation_charge_type, android.R.layout.simple_spinner_item);
        final CharSequence[] chargeTypes = getResources().getTextArray(R.array
                .cancellation_charge_type_value);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerChargeType.setAdapter(adapter);
        spinnerChargeType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                chargeType = Integer.valueOf((String) chargeTypes[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private void initSpinnerForPriceRatings() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.price_rating, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriceRatting.setAdapter(adapter);
        spinnerPriceRatting.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                priceRatings = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case STORE_LOCATION_RESULT:
                    Bundle bundle = data.getExtras();
                    etAddress.setText(bundle.getString(Constant.ADDRESS));
                    etLat.setText(String.valueOf(bundle.getDouble(Constant.LATITUDE)));
                    etLng.setText(String.valueOf(bundle.getDouble(Constant.LONGITUDE)));
                    break;
                case REQUEST_STORE_TIME:
                    getSettingsData();
                    break;
                case FAMOUS_TAG_LIST:
                    Bundle bundle1 = data.getExtras();
                    storeData.setFamousProductsTags
                            ((ArrayList<List<String>>) bundle1.getSerializable(Constant.BUNDLE));
                    break;
                default:
                    // result from facebook
                    break;
            }
        }
    }

    private void setStoreAddressORLocation() {
        Intent intent = new Intent(this, StoreLocationActivity.class);
        startActivityForResult(intent, STORE_LOCATION_RESULT);

    }


    private void goToStoreTimeActivity() {
        Intent intent = new Intent(this, StoreTimeActivity.class);
        intent.putExtra(Constant.STORE_TIME, storeTimeList);
        startActivityForResult(intent, Constant.REQUEST_STORE_TIME);
    }

    private void goToStoreDeliveryTimeActivity() {
        Intent intent = new Intent(this, StoreDeliveryTimeActivity.class);
        intent.putExtra(Constant.STORE_TIME, storeDeliveryTimeList);
        startActivityForResult(intent, Constant.REQUEST_STORE_TIME);
    }

    private void scaleUpAndDown(final View view, boolean isShow, int dimension) {
        if (isShow) {
            ResizeAnimation resizeAnimation = new ResizeAnimation(view, getResources()
                    .getDimensionPixelSize(dimension));
            resizeAnimation.setInterpolator(new LinearInterpolator());
            resizeAnimation.setDuration(300);
            view.startAnimation(resizeAnimation);
            view.setVisibility(View.VISIBLE);
        } else {
            ResizeAnimation resizeAnimation = new ResizeAnimation(view, 1);
            resizeAnimation.setInterpolator(new LinearInterpolator());
            resizeAnimation.setDuration(300);
            view.startAnimation(resizeAnimation);
            view.getAnimation().setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    view.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.switchPayDeliveryFees:
                scaleUpAndDown(llPayDeliveryFees, isChecked, R.dimen.dimen_expand_penal_content_2);
                break;
            case R.id.switchOrderCancellationCharge:
                scaleUpAndDown(llCancellationCharge, isChecked, R.dimen
                        .dimen_expand_penal_content_3);
                break;
            case R.id.switchProviderDeliveryAnywhere:
                scaleUpAndDown(llDeliveryAnyWhere, !isChecked, R.dimen
                        .dimen_expand_penal_content_1);

                break;
            case R.id.switchTakingScheduleOrder:
                scaleUpAndDown(llScheduleOrder, isChecked, R.dimen.dimen_expand_penal_content_1);
                break;
            case R.id.switchIsSetDeliveryTime:
                if (isChecked) {
                    tvAddNewStoreDeliverySlot.setOnClickListener(SettingsActivity.this);
                    tvAddNewStoreDeliverySlot.setTextColor(ResourcesCompat.getColor(getResources(), R.color.color_app_heading, null));
                } else {
                    tvAddNewStoreDeliverySlot.setOnClickListener(null);
                    tvAddNewStoreDeliverySlot.setTextColor(ResourcesCompat.getColor(getResources(), R.color.color_app_divider, null));
                }
                break;
            default:
                // do with default
                break;
        }
    }

    private void goToFamousForActivity(StoreData storeData) {
        Intent intent = new Intent(this, FamousForActivity.class);
        intent.putExtra(Constant.BUNDLE, storeData);
        startActivityForResult(intent, Constant.FAMOUS_TAG_LIST);
    }

    private void openAdminLanguageDialog() {
        final Dialog languageDialog = new Dialog(this);
        languageDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        languageDialog.setContentView(R.layout.dialog_admin_language);
        llLangs = languageDialog.findViewById(R.id.llLangs);
        if (tempSelectedLangs.isEmpty()) {
            tempSelectedLangs.addAll(selectedLanguages);
        }
        for (Languages languages : adminLanguagges) {
            LinearLayout ll = new LinearLayout(this);
            ll.setOrientation(LinearLayout.VERTICAL);
            ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT
                    , LinearLayout.LayoutParams.WRAP_CONTENT));
            ll.setPadding(getResources().getDimensionPixelOffset(R.dimen.activity_horizontal_padding)
                    , 0, getResources().getDimensionPixelOffset(R.dimen.activity_horizontal_padding)
                    , 0);
            ll.setGravity(Gravity.START);
            final CheckBox cb = new CheckBox(this);
            cb.setText(languages.getName());
            cb.setPadding(getResources().getDimensionPixelOffset(R.dimen.dimen_app_edit_text_padding)
                    , 0, getResources().getDimensionPixelOffset(R.dimen.dimen_app_edit_text_padding)
                    , 0);
            cb.setTag(languages.getCode());
            cb.setEnabled(true);
            cb.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    getResources().getDimension(R.dimen.size_app_text_medium));
            for (Languages languages1 : tempSelectedLangs) {
                if (languages1.getCode().equals(languages.getCode()) && languages1.isVisible()) {
                    cb.setChecked(true);
                    if (languages1.getCode().equals("en")) {
                        cb.setEnabled(false);
                    }

                }
            }

            ll.addView(cb);
            llLangs.addView(ll);
        }
        languageDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                tempSelectedLangs.clear();
                for (int i = 0; i < llLangs.getChildCount(); i++) {
                    LinearLayout nextChildLayout = (LinearLayout) llLangs.getChildAt(i);
                    View nextChild = nextChildLayout.getChildAt(0);
                    if (nextChild instanceof CheckBox) {
                        CheckBox check = (CheckBox) nextChild;
                        for (Languages languages : adminLanguagges) {
                            if (check.getTag().equals(languages.getCode())) {
                                if (check.isChecked()) {
                                    languages.setVisible(true);
                                    tempSelectedLangs.add(languages);
                                }

                            }

                        }

                    }
                }
            }
        });
        WindowManager.LayoutParams params = languageDialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        languageDialog.getWindow().setAttributes(params);
        languageDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        setToolbarEditIcon(false, R.drawable.ic_delete);
        setToolbarCameraIcon(false);
        return true;
    }

    private void initToolbarButton() {
        tvtoolbarbtn = (CustomTextView) findViewById(R.id.tvtoolbarbtn);
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
}
