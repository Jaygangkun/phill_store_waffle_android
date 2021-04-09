package com.edelivery.store;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.edelivery.store.component.VehicleDialog;
import com.edelivery.store.models.datamodel.Addresses;
import com.edelivery.store.models.datamodel.CartOrder;
import com.edelivery.store.models.datamodel.CartProducts;
import com.edelivery.store.models.datamodel.StoreData;
import com.edelivery.store.models.datamodel.UserDetail;
import com.edelivery.store.models.datamodel.VehicleDetail;
import com.edelivery.store.models.responsemodel.AddCartResponse;
import com.edelivery.store.models.responsemodel.InvoiceResponse;
import com.edelivery.store.models.responsemodel.StoreDataResponse;
import com.edelivery.store.models.responsemodel.VehiclesResponse;
import com.edelivery.store.models.singleton.CurrentBooking;
import com.edelivery.store.parse.ApiClient;
import com.edelivery.store.parse.ApiInterface;
import com.edelivery.store.parse.ParseContent;
import com.edelivery.store.utils.Constant;
import com.edelivery.store.utils.PreferenceHelper;
import com.edelivery.store.utils.Utilities;
import com.edelivery.store.widgets.CustomButton;
import com.edelivery.store.widgets.CustomFontTextViewTitle;
import com.edelivery.store.widgets.CustomInputEditText;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InstantOrderActivity extends BaseActivity {
    public static final String TAG = "InstantOrderInvoiceActivity";
    private CustomInputEditText etCustomerFistName, etCustomerMobile, etCustomerDeliveryAddress,
            etDeliveryAddressNote, etPromoCode, etCustomerLastName, etCustomerEmail,
            etDeliveryPrice, etCustomerCountryCode;
    private CustomButton btnInvoice;
    private CustomFontTextViewTitle tvInvoiceOderTotal;
    private CustomTextView tvPromoCodeApply;
    private ImageView ivDeliveryLocation, ivFreeShipping;
    private List<VehicleDetail> vehicleDetails = new ArrayList<>();
    private String vehicleId;

    private StoreData storeData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instant_order);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbar(toolbar, R.drawable.ic_back, R.color.color_app_theme);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utilities.hideSoftKeyboard(InstantOrderActivity.this);
                onBackPressed();
            }
        });
        ((TextView) findViewById(R.id.tvToolbarTitle)).setText(getString(R.string
                .text_instant_order));
        etDeliveryPrice = (CustomInputEditText) findViewById(R.id.etDeliveryPrice);
        etCustomerFistName = (CustomInputEditText) findViewById(R.id.etCustomerFirstName);
        etCustomerLastName = (CustomInputEditText) findViewById(R.id.etCustomerLastName);
        etCustomerEmail = (CustomInputEditText) findViewById(R.id.etCustomerEmail);
        etCustomerMobile = (CustomInputEditText) findViewById(R.id.etCustomerMobile);
        etCustomerDeliveryAddress = (CustomInputEditText) findViewById(R.id
                .etCustomerDeliveryAddress);
        etDeliveryAddressNote = (CustomInputEditText) findViewById(R.id.etDeliveryAddressNote);
        btnInvoice = (CustomButton) findViewById(R.id.btnInvoice);
        tvInvoiceOderTotal = (CustomFontTextViewTitle) findViewById(R.id.tvInvoiceOderTotal);
        etPromoCode = (CustomInputEditText) findViewById(R.id.etPromoCode);
        tvPromoCodeApply = (CustomTextView) findViewById(R.id.tvPromoCodeApply);
        ivDeliveryLocation = (ImageView) findViewById(R.id.ivDeliveryLocation);
        ivFreeShipping = (ImageView) findViewById(R.id.ivFreeShipping);
        etCustomerCountryCode = (CustomInputEditText) findViewById(R.id.etCustomerCountryCode);
        btnInvoice.setOnClickListener(this);
        tvPromoCodeApply.setOnClickListener(this);
        ivDeliveryLocation.setOnClickListener(this);
        etCustomerDeliveryAddress.setOnClickListener(this);
        setContactNoLength(preferenceHelper.getMaxPhoneNumberLength());
        etCustomerCountryCode.setText(preferenceHelper.getCountryPhoneCode());
        getDeliveryVehicleList();

        getSettingsData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        setToolbarCameraIcon(false);
        setToolbarEditIcon(false, 0);
        return true;
    }

    private void addEmptyCartOnServer() {

        Utilities.showCustomProgressDialog(this, false);


        CartOrder cartOrder = new CartOrder();
        cartOrder.setServerToken("");
        cartOrder.setUserId("");
        cartOrder.setUserType(Constant.Type.STORE);
        cartOrder.setStoreId(preferenceHelper.getStoreId());
        cartOrder.setProducts(new ArrayList<CartProducts>());
        cartOrder.setAndroidId(preferenceHelper.getAndroidId());
        if (!TextUtils.isEmpty(preferenceHelper.getCartId())) {
            cartOrder.setCartId(preferenceHelper.getCartId());
        }

        if (CurrentBooking.getInstance().getDestinationAddresses().isEmpty()) {
            Addresses addresses = new Addresses();
            addresses.setAddress(CurrentBooking.getInstance().getDeliveryAddress());
            addresses.setCity(CurrentBooking.getInstance().getCity1());
            addresses.setAddressType(Constant.Type.DESTINATION);
            addresses.setNote(etDeliveryAddressNote.getText().toString());
            addresses.setUserType(Constant.Type.USER);
            ArrayList<Double> location = new ArrayList<>();
            location.add(CurrentBooking.getInstance().getDeliveryLatLng().latitude);
            location.add(CurrentBooking.getInstance().getDeliveryLatLng().longitude);
            addresses.setLocation(location);
            UserDetail cartUserDetail = new UserDetail();
            cartUserDetail.setEmail(etCustomerEmail.getText().toString());
            cartUserDetail.setCountryPhoneCode(etCustomerCountryCode.getText().toString());
            cartUserDetail.setName(etCustomerFistName.getText().toString() + " " +
                    etCustomerLastName
                            .getText().toString());
            cartUserDetail.setPhone(etCustomerMobile.getText().toString());
            addresses.setUserDetails(cartUserDetail);
            CurrentBooking.getInstance().setDestinationAddresses(addresses);
        }


        if (CurrentBooking.getInstance().getPickupAddresses().isEmpty()) {
            Addresses addresses = new Addresses();
            addresses.setAddress(preferenceHelper.getAddress());
            addresses.setCity("");
            addresses.setAddressType(Constant.Type.PICKUP);
            addresses.setNote("");
            addresses.setUserType(Constant.Type.STORE);
            ArrayList<Double> location = new ArrayList<>();
            location.add(Double.valueOf(preferenceHelper.getLatitude()));
            location.add(Double.valueOf(preferenceHelper.getLongitude()));
            addresses.setLocation(location);
            UserDetail cartUserDetail = new UserDetail();
            cartUserDetail.setEmail(preferenceHelper.getEmail());
            cartUserDetail.setCountryPhoneCode(preferenceHelper.getCountryPhoneCode());
            cartUserDetail.setName(preferenceHelper.getName());
            cartUserDetail.setPhone(preferenceHelper.getPhone());
            addresses.setUserDetails(cartUserDetail);
            CurrentBooking.getInstance().setPickupAddresses(addresses);
        }
        cartOrder.setDestinationAddresses(CurrentBooking.getInstance().getDestinationAddresses());
        cartOrder.setPickupAddresses(CurrentBooking.getInstance().getPickupAddresses());

        // add filed on 4_Aug_2018

        cartOrder.setCartOrderTotalPrice(Double.valueOf(etDeliveryPrice.getText().toString()));
        cartOrder.setCartOrderTotalTaxPrice(0);
        Utilities.printLog("ADD_ITEM_CART", ApiClient.JSONResponse(cartOrder));

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<AddCartResponse> responseCall = apiInterface.addItemInCart(ApiClient
                .makeGSONRequestBody(cartOrder));
        responseCall.enqueue(new Callback<AddCartResponse>() {
            @Override
            public void onResponse(Call<AddCartResponse> call, Response<AddCartResponse>
                    response) {

                if (parseContent.isSuccessful(response)) {
                    if (response.body().isSuccess()) {
                        preferenceHelper.putCartId(response.body().getCartId());
                        CurrentBooking.getInstance().setCartCityId(response.body().getCityId());
                        CurrentBooking.getInstance().setCurrency(preferenceHelper.getCurrency());
                        getDistanceMatrix();
                    } else {
                        Utilities.hideCustomProgressDialog();
                        ParseContent.getParseContentInstance().showErrorMessage(
                                InstantOrderActivity.this, response
                                        .body()
                                        .getErrorCode(), false);
                    }


                }
            }

            @Override
            public void onFailure(Call<AddCartResponse> call, Throwable t) {
                Utilities.handleThrowable("PRODUCT_SPE_ACTIVITY", t);
            }
        });
    }

    private void setContactNoLength(int length) {
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(length);
        etCustomerMobile.setFilters(FilterArray);
    }

    protected boolean isValidate() {
        String msg = null;

        if (TextUtils.isEmpty(etCustomerFistName.getText().toString().trim())) {
            msg = getString(R.string.msg_please_enter_valid_name);
            etCustomerFistName.setError(msg);
            etCustomerFistName.requestFocus();
        } else if (TextUtils.isEmpty(etCustomerLastName.getText().toString().trim())) {
            msg = getString(R.string.msg_please_enter_valid_name);
            etCustomerLastName.setError(msg);
            etCustomerLastName.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(etCustomerEmail.getText().toString().trim())
                .matches()) {
            msg = getString(R.string.msg_please_enter_valid_email);
            etCustomerEmail.setError(msg);
            etCustomerEmail.requestFocus();
        } else if (TextUtils.isEmpty(etCustomerMobile.getText().toString().trim())) {
            msg = getString(R.string.msg_please_enter_valid_mobile_number);
            etCustomerMobile.setError(msg);
            etCustomerMobile.requestFocus();
        } else if (etCustomerMobile.getText().toString().trim().length()
                > preferenceHelper.getMaxPhoneNumberLength() || etCustomerMobile.getText()
                .toString().trim().length
                        () < preferenceHelper.getMinPhoneNumberLength()) {
            msg = getString(R.string.msg_please_enter_valid_mobile_number) + " " +
                    "" + preferenceHelper.getMaxPhoneNumberLength() + getString(R.string.text_or)
                    + preferenceHelper.getMinPhoneNumberLength() + " " + getString(R.string
                    .text_digits);
            etCustomerMobile.setError(msg);
            etCustomerMobile.requestFocus();
        } else if (!Utilities.isDecimalAndGraterThenZero(etDeliveryPrice.getText().toString().trim
                ())) {
            msg = getString(R.string.msg_enter_valid_amount);
            etDeliveryPrice.setError(msg);
            etDeliveryPrice.requestFocus();
        } else if (TextUtils.isEmpty(etCustomerDeliveryAddress
                .getText()
                .toString().trim()) ||
                CurrentBooking.getInstance().getDeliveryLatLng() == null) {
            msg = getResources().getString(R.string
                    .msg_plz_enter_valid_delivery_address);
            etCustomerDeliveryAddress.setError(msg);
            etCustomerDeliveryAddress.requestFocus();
        }


        return TextUtils.isEmpty(msg);
    }

    @Override
    public void onClick(View view) {
        // do somethings
        switch (view.getId()) {
            case R.id.btnInvoice:
//                if (isValidate()) {
                    openVehicleSelectDialog();

//                }
                break;
            case R.id.ivDeliveryLocation:
            case R.id.etCustomerDeliveryAddress:

                goToCheckoutDeliveryLocationActivity();
                break;
            default:
                // do with default
                break;
        }
    }

    private void goToCheckoutDeliveryLocationActivity() {
        Intent intent = new Intent(this, CheckoutDeliveryLocationActivity.class);
        startActivityForResult(intent, Constant.DELIVERY_LIST_CODE);
    }

    private void goToInstantOrderInvoiceActivity(InvoiceResponse invoiceResponse, String
            vehicleId) {
        Intent intent = new Intent(this, InstantOrderInvoiceActivity.class);
        intent.putExtra(Constant.INVOICE, invoiceResponse);
        intent.putExtra(Constant.VEHICLE_ID, vehicleId);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Constant.DELIVERY_LIST_CODE:
                    if (CurrentBooking.getInstance().getStoreLatLng() != null) {
                        etCustomerDeliveryAddress.setError(null);
                        etCustomerDeliveryAddress.setText(CurrentBooking.getInstance()
                                .getDeliveryAddress());
                    }
                    break;

                default:
                    // do with default
                    break;
            }
        }


    }

    /**
     * this method called a webservice for get distance and time witch is provided by Google
     */
    private void getDistanceMatrix() {
        HashMap<String, String> hashMap = new HashMap<>();
        String origins = String.valueOf(CurrentBooking.getInstance().getStoreLatLng().latitude) +
                "," +
                CurrentBooking.getInstance().getStoreLatLng().longitude;
        hashMap.put(Constant.google.ORIGINS, origins);
        String destination = String.valueOf(CurrentBooking.getInstance().getDeliveryLatLng()
                .latitude) +
                "," +
                CurrentBooking.getInstance().getDeliveryLatLng().longitude;
        hashMap.put(Constant.google.DESTINATIONS, destination);
        hashMap.put(Constant.google.KEY, preferenceHelper.getGoogleKey());
        Utilities.printLog("DISTANCE_PARAM_GOOGLE", hashMap.toString());


        ApiInterface apiInterface = new ApiClient().changeApiBaseUrl(Constant.GOOGLE_API_URL)
                .create

                        (ApiInterface
                                .class);
        Call<ResponseBody> call = apiInterface.getGoogleDistanceMatrix(hashMap);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                HashMap<String, String> map = parseContent.parsDistanceMatrix(response);
                if (map != null && !map.isEmpty()) {
                    String distance = map.get(Constant.google.DISTANCE);
                    String timeSecond = map.get(Constant.google.DURATION);
                    Utilities.printLog("DISTANCE_MATRIX", "Distance=" + distance + " " + "Time=" +
                            timeSecond);
                    Double tripDistance = Double.valueOf(distance);
                    getDeliveryInvoice(Integer.valueOf(timeSecond), tripDistance);
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Utilities.handleThrowable("CHECKOUT_ACTIVITY", t);

            }
        });

    }

    /**
     * this method called a webservice to get delivery invoice or bill
     */
    private void getDeliveryInvoice(int timeSeconds, double tripDistance) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.USER_ID, "");
            jsonObject.put(Constant.ORDER_TYPE, Constant.Type.STORE);
            jsonObject.put(Constant.SERVER_TOKEN, preferenceHelper
                    .getServerToken());
            jsonObject.put(Constant.STORE_ID, preferenceHelper.getStoreId());
            jsonObject.put(Constant.TOTAL_ITEM_COUNT, 1);
            jsonObject.put(Constant.TOTAL_CART_PRICE, Double.valueOf(etDeliveryPrice.getText()
                    .toString()));
            jsonObject.put(Constant.TOTAL_ITEM_PRICE, Double.valueOf(etDeliveryPrice.getText()
                    .toString()));
            jsonObject.put(Constant.TOTAL_DISTANCE, tripDistance);
            jsonObject.put(Constant.TOTAL_TIME, timeSeconds);
            jsonObject.put(Constant.ORDER_TYPE, Constant.Type.STORE);
            jsonObject.put(Constant.VEHICLE_ID, vehicleId);
            jsonObject.put(Constant.CART_UNIQUE_TOKEN, preferenceHelper.getAndroidId());
        } catch (JSONException e) {
            Utilities.handleThrowable("CHECKOUT_ACTIVITY", e);
        }

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<InvoiceResponse> responseCall = apiInterface.getDeliveryInvoice(ApiClient
                .makeJSONRequestBody(jsonObject));
        responseCall.enqueue(new Callback<InvoiceResponse>() {
            @Override
            public void onResponse(Call<InvoiceResponse> call, Response<InvoiceResponse>
                    response) {
                if (parseContent.isSuccessful(response)) {
                    Utilities.hideCustomProgressDialog();
                    if (response.body().isSuccess()) {
                        Utilities.printLog("CHECKOUT_INVOICE", ApiClient.JSONResponse(response
                                .body()));
                        goToInstantOrderInvoiceActivity(response.body(), vehicleId);
                    } else {
                        parseContent.showErrorMessage(InstantOrderActivity
                                .this, response.body().getErrorCode(), false);
                    }
                }
            }

            @Override
            public void onFailure(Call<InvoiceResponse> call, Throwable t) {
                Utilities.handleThrowable("CHECKOUT_ACTIVITY", t);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void getDeliveryVehicleList() {
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
        Call<VehiclesResponse> responseCall = apiInterface.getVehiclesList(ApiClient
                .makeJSONRequestBody(jsonObject));
        responseCall.enqueue(new Callback<VehiclesResponse>() {
            @Override
            public void onResponse(Call<VehiclesResponse> call, Response<VehiclesResponse>
                    response) {
                if (parseContent.isSuccessful(response)) {
                    Utilities.hideCustomProgressDialog();
                    if (response.body().isSuccess()) {
                        vehicleDetails.clear();
                        if (preferenceHelper.getIsStoreCanCompleteOrder() || preferenceHelper
                                .getIsStoreCanAddProvider()) {
                            vehicleDetails.addAll(response.body().getVehicles());
                        } else {
                            vehicleDetails.addAll(response.body().getAdminVehicles());
                        }


                    } else {
                        parseContent.showErrorMessage(InstantOrderActivity
                                .this, response.body().getErrorCode(), false);
                    }
                }
            }

            @Override
            public void onFailure(Call<VehiclesResponse> call, Throwable t) {
                Utilities.handleThrowable("CHECKOUT_ACTIVITY", t);
            }
        });
    }

    private void openVehicleSelectDialog() {
        if (vehicleDetails.isEmpty()) {
            Utilities.showToast(this, getResources().getString(R
                    .string.text_vehicle_not_found));
        } else {
            final VehicleDialog vehicleDialog = new VehicleDialog(this, vehicleDetails);
            vehicleDialog.findViewById(R.id.btnNegative).setOnClickListener(new View
                    .OnClickListener() {
                @Override
                public void onClick(View view) {
                    vehicleDialog.dismiss();

                }
            });
            vehicleDialog.findViewById(R.id.btnPositive).setOnClickListener(new View
                    .OnClickListener() {
                @Override
                public void onClick(View view) {
                    vehicleId = vehicleDialog.getVehicleId();
                    if (TextUtils.isEmpty(vehicleId)) {
                        Utilities.showToast(InstantOrderActivity.this, getResources().getString(R
                                .string.msg_select_vehicle));
                    } else {
                        vehicleDialog.dismiss();
                        addEmptyCartOnServer();
                    }


                }
            });
            vehicleDialog.show();
            vehicleDialog.hideProviderManualAssign();
        }
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
                        etCustomerDeliveryAddress.setText(storeData.getAddress());
                        preferenceHelper.putIsStoreCanCreateGroup(storeData.
                                getDeliveryDetails().isStoreCanCreateGroup());
                    } else {
                        ParseContent.getParseContentInstance().showErrorMessage(InstantOrderActivity
                                .this, response.body().getErrorCode(), true);
                    }
                } else {
                    Utilities.showHttpErrorToast(response.code(), InstantOrderActivity.this);
                }
            }

            @Override
            public void onFailure(Call<StoreDataResponse> call, Throwable t) {
                Utilities.printLog("BaseActivity", t.getMessage());
            }
        });

        etDeliveryPrice.setText("0");
        etCustomerFistName.setText("instant");
        etCustomerLastName.setText("order");
        etCustomerMobile.setText("00000000000");
        etCustomerEmail.setText("instant@yetidelivery.co.uk");
    }
}
