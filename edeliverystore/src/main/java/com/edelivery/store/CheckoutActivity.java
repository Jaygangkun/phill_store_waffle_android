package com.edelivery.store;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edelivery.store.component.CustomFontCheckBox;
import com.edelivery.store.models.datamodel.Addresses;
import com.edelivery.store.models.datamodel.CartOrder;
import com.edelivery.store.models.datamodel.CartProductItems;
import com.edelivery.store.models.datamodel.CartProducts;
import com.edelivery.store.models.datamodel.StoreData;
import com.edelivery.store.models.datamodel.UserDetail;
import com.edelivery.store.models.responsemodel.AddCartResponse;
import com.edelivery.store.models.responsemodel.StoreDataResponse;
import com.edelivery.store.models.singleton.CurrentBooking;
import com.edelivery.store.parse.ApiClient;
import com.edelivery.store.parse.ApiInterface;
import com.edelivery.store.parse.ParseContent;
import com.edelivery.store.utils.Constant;
import com.edelivery.store.utils.PreferenceHelper;
import com.edelivery.store.utils.Utilities;
import com.edelivery.store.widgets.CustomButton;
import com.edelivery.store.widgets.CustomInputEditText;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CheckoutActivity extends BaseActivity {

    public static final String TAG = CheckoutActivity.class.getName();
    private CustomInputEditText etCustomerFistName, etCustomerMobile, etCustomerDeliveryAddress,
            etDeliveryAddressNote, etPromoCode, etCustomerLastName, etCustomerEmail,
            etCustomerCountryCode;
    private CustomButton btnInvoice;
    private CustomTextView tvPromoCodeApply;
    private ImageView ivDeliveryLocation;
    private CustomFontCheckBox cbSelfDelivery;
    private LinearLayout llSelfDelivery, llDestinationAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbar(toolbar, R.drawable.ic_back, R.color.color_app_theme);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utilities.hideSoftKeyboard(CheckoutActivity.this);
                onBackPressed();
            }
        });
        ((TextView) findViewById(R.id.tvToolbarTitle)).setText(getString(R.string
                .text_checkout));
        llDestinationAddress = findViewById(R.id.llDestinationAddress);
        etCustomerFistName = (CustomInputEditText) findViewById(R.id.etCustomerFirstName);
        etCustomerLastName = (CustomInputEditText) findViewById(R.id.etCustomerLastName);
        etCustomerEmail = (CustomInputEditText) findViewById(R.id.etCustomerEmail);
        etCustomerMobile = (CustomInputEditText) findViewById(R.id.etCustomerMobile);
        etCustomerDeliveryAddress = (CustomInputEditText) findViewById(R.id
                .etCustomerDeliveryAddress);
        etDeliveryAddressNote = (CustomInputEditText) findViewById(R.id.etDeliveryAddressNote);
        btnInvoice = (CustomButton) findViewById(R.id.btnInvoice);
        etPromoCode = (CustomInputEditText) findViewById(R.id.etPromoCode);
        tvPromoCodeApply = (CustomTextView) findViewById(R.id.tvPromoCodeApply);
        ivDeliveryLocation = (ImageView) findViewById(R.id.ivDeliveryLocation);
        cbSelfDelivery = (CustomFontCheckBox) findViewById(R.id.cbSelfDelivery);
        cbSelfDelivery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ivDeliveryLocation.setEnabled(!isChecked);
                etCustomerDeliveryAddress.setEnabled(!isChecked);
                etDeliveryAddressNote.getText().clear();
                etDeliveryAddressNote.setVisibility(isChecked ? View.GONE : View.VISIBLE);
                llDestinationAddress.setVisibility(isChecked ? View.GONE : View.VISIBLE);
            }
        });
        etCustomerCountryCode = (CustomInputEditText) findViewById(R.id.etCustomerCountryCode);
        llSelfDelivery = findViewById(R.id.llSelfDelivery);
        btnInvoice.setOnClickListener(this);
        tvPromoCodeApply.setOnClickListener(this);
        ivDeliveryLocation.setOnClickListener(this);
        etCustomerDeliveryAddress.setOnClickListener(this);
        setContactNoLength(preferenceHelper.getMaxPhoneNumberLength());
        etCustomerCountryCode.setText(preferenceHelper.getCountryPhoneCode());
        llSelfDelivery.setVisibility(preferenceHelper.getIsProvidePickupDelivery() ? View
                .VISIBLE : View.GONE);

    }

    private void setContactNoLength(int length) {
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(length);
        etCustomerMobile.setFilters(FilterArray);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu1) {
        super.onCreateOptionsMenu(menu1);
        menu = menu1;
        setToolbarEditIcon(false, R.drawable.filter_store);
        setToolbarCameraIcon(false);
        return true;
    }

    @Override
    public void onClick(View view) {
        // do somethings
        switch (view.getId()) {
            case R.id.btnInvoice:
                if (isValidate()) {
                    addItemInServerCart();
                }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Constant.DELIVERY_LIST_CODE:
                    if (CurrentBooking.getInstance().getDeliveryLatLng() != null) {
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

    private void goToCheckoutDeliveryLocationActivity() {
        Intent intent = new Intent(this, CheckoutDeliveryLocationActivity.class);
        startActivityForResult(intent, Constant.DELIVERY_LIST_CODE);
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
        }
        else if (!cbSelfDelivery.isChecked() && (TextUtils.isEmpty(etCustomerDeliveryAddress
                .getText()
                .toString().trim()) ||
                CurrentBooking.getInstance().getDeliveryLatLng() == null)) {
            msg = getResources().getString(R.string
                    .msg_plz_enter_valid_delivery_address);
            Utilities.showToast(this, msg);

        }


        return TextUtils.isEmpty(msg);
    }

    /**
     * this method called webservice for add product in cart
     */
    private void addItemInServerCart() {

        Utilities.showCustomProgressDialog(this, false);

        CartOrder cartOrder = new CartOrder();
        cartOrder.setServerToken(preferenceHelper.getServerToken());
        cartOrder.setUserId("");
        cartOrder.setUserType(Constant.Type.STORE);
        cartOrder.setStoreId(preferenceHelper.getStoreId());
        cartOrder.setProducts(CurrentBooking.getInstance().getCartProductList());
        cartOrder.setAndroidId(preferenceHelper.getAndroidId());
        if (!TextUtils.isEmpty(preferenceHelper.getCartId())) {
            cartOrder.setCartId(preferenceHelper.getCartId());
        }

        // set destination address
        Addresses addresses = new Addresses();
        addresses.setCity(CurrentBooking.getInstance().getCity1());
        addresses.setAddressType(Constant.Type.DESTINATION);
        addresses.setNote(etDeliveryAddressNote.getText().toString());
        addresses.setUserType(Constant.Type.USER);
        if (!cbSelfDelivery.isChecked()) {
            addresses.setAddress(CurrentBooking.getInstance().getDeliveryAddress());
            ArrayList<Double> location = new ArrayList<>();
            location.add(CurrentBooking.getInstance().getDeliveryLatLng().latitude);
            location.add(CurrentBooking.getInstance().getDeliveryLatLng().longitude);
            addresses.setLocation(location);
        } else {
            addresses.setAddress("self pickup");
            ArrayList<Double> location = new ArrayList<>();
            location.add(Double.valueOf(preferenceHelper.getLatitude()));
            location.add(Double.valueOf(preferenceHelper.getLongitude()));
            addresses.setLocation(location);
            addresses.setLocation(location);
        }
        UserDetail cartUserDetail = new UserDetail();
        cartUserDetail.setEmail(etCustomerEmail.getText().toString());
        cartUserDetail.setCountryPhoneCode(etCustomerCountryCode.getText().toString());
        cartUserDetail.setName(etCustomerFistName.getText().toString() + " " +
                etCustomerLastName
                        .getText().toString());
        cartUserDetail.setPhone(etCustomerMobile.getText().toString());
        addresses.setUserDetails(cartUserDetail);
        CurrentBooking.getInstance().setDestinationAddresses(addresses);


        // set pickup address
        Addresses addresses1 = new Addresses();
        addresses1.setAddress(preferenceHelper.getAddress());
        addresses1.setCity("");
        addresses1.setAddressType(Constant.Type.PICKUP);
        addresses1.setNote("");
        addresses1.setUserType(Constant.Type.STORE);
        ArrayList<Double> location = new ArrayList<>();
        location.add(Double.valueOf(preferenceHelper.getLatitude()));
        location.add(Double.valueOf(preferenceHelper.getLongitude()));
        addresses1.setLocation(location);
        UserDetail cartUserDetail1 = new UserDetail();
        cartUserDetail1.setEmail(preferenceHelper.getEmail());
        cartUserDetail1.setCountryPhoneCode(preferenceHelper.getCountryPhoneCode());
        cartUserDetail1.setName(preferenceHelper.getName());
        cartUserDetail1.setImageUrl(preferenceHelper.getProfilePic());
        cartUserDetail1.setPhone(preferenceHelper.getPhone());
        addresses1.setUserDetails(cartUserDetail1);
        CurrentBooking.getInstance().setPickupAddresses(addresses1);


        cartOrder.setDestinationAddresses(CurrentBooking.getInstance().getDestinationAddresses());
        cartOrder.setPickupAddresses(CurrentBooking.getInstance().getPickupAddresses());

        // add filed on 4_Aug_2018
        double totalCartPrice = 0, totalCartTaxPrice = 0;
        for (CartProducts products : CurrentBooking.getInstance().getCartProductList()) {
            double totalItemPrice = 0, totalTaxPrice = 0;
            for (CartProductItems cartProductItems : products.getItems()) {
                totalTaxPrice = totalTaxPrice + cartProductItems.getTotalItemTax();
                totalItemPrice = totalItemPrice + cartProductItems
                        .getTotalItemAndSpecificationPrice();
            }
            products.setTotalItemTax(totalTaxPrice);
            products.setTotalProductItemPrice(totalItemPrice);
            totalCartPrice = totalCartPrice + totalItemPrice;
            totalCartTaxPrice = totalCartTaxPrice + totalTaxPrice;

        }
        cartOrder.setCartOrderTotalPrice(totalCartPrice);
        cartOrder.setCartOrderTotalTaxPrice(totalCartTaxPrice);
        Utilities.printLog("ADD_ITEM_CART", ApiClient.JSONResponse(cartOrder));

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<AddCartResponse> responseCall = apiInterface.addItemInCart(ApiClient
                .makeGSONRequestBody(cartOrder));
        responseCall.enqueue(new Callback<AddCartResponse>() {
            @Override
            public void onResponse(Call<AddCartResponse> call, Response<AddCartResponse>
                    response) {
                if (parseContent.isSuccessful(response)) {
                    Utilities.hideCustomProgressDialog();
                    if (response.body().isSuccess()) {
                        preferenceHelper.putCartId(response.body().getCartId());
                        CurrentBooking.getInstance().setCartCityId(response.body().getCityId());
                        CurrentBooking.getInstance().setCurrency(preferenceHelper.getCurrency());
                        goToOrderInvoiceActivity(response.body().getUserId());
                    } else {
                        ParseContent.getParseContentInstance().showErrorMessage(
                                CheckoutActivity.this, response
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

    private void goToOrderInvoiceActivity(String userId) {
        Intent intent = new Intent(this, OrderInvoiceActivity.class);
        intent.putExtra(Constant.IS_USER_PICK_UP_ORDER, cbSelfDelivery.isChecked());
        intent.putExtra(Constant.USER_ID, userId);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

}
