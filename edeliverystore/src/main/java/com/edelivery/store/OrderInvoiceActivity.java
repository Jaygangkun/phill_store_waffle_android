package com.edelivery.store;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.edelivery.store.adapter.OrderInvoiceAdapter;
import com.edelivery.store.component.CustomAlterDialog;
import com.edelivery.store.models.datamodel.CartProductItems;
import com.edelivery.store.models.datamodel.CartProducts;
import com.edelivery.store.models.datamodel.ItemSpecification;
import com.edelivery.store.models.datamodel.OrderPaymentDetail;
import com.edelivery.store.models.responsemodel.InvoiceResponse;
import com.edelivery.store.models.responsemodel.IsSuccessResponse;
import com.edelivery.store.models.singleton.CurrentBooking;
import com.edelivery.store.parse.ApiClient;
import com.edelivery.store.parse.ApiInterface;
import com.edelivery.store.utils.Constant;
import com.edelivery.store.utils.Utilities;
import com.edelivery.store.widgets.CustomButton;
import com.edelivery.store.widgets.CustomFontTextViewTitle;
import com.elluminati.edelivery.store.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.edelivery.store.utils.Utilities.showCustomProgressDialog;

public class OrderInvoiceActivity extends BaseActivity {
    public static final String TAG = "OrderInvoiceActivity";
    private CustomButton btnPlaceOrder;
    private CustomFontTextViewTitle tvInvoiceOderTotal;
    private RecyclerView rcvInvoice;
    private int totalItemCount = 0;
    private int totalSpecificationCount = 0;
    private double totalItemPriceWithQuantity = 0;
    private double totalSpecificationPriceWithQuantity = 0;
    private ImageView ivFreeShipping;
    private boolean isSelfDelivery;
    private OrderPaymentDetail orderPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_invoice);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbar(toolbar, R.drawable.ic_back, R.color.color_app_theme);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utilities.hideSoftKeyboard(OrderInvoiceActivity.this);
                onBackPressed();
            }
        });
        ((TextView) findViewById(R.id.tvToolbarTitle)).setText(getString(R.string
                .text_invoice));
        btnPlaceOrder = (CustomButton) findViewById(R.id.btnPlaceOrder);
        tvInvoiceOderTotal = (CustomFontTextViewTitle) findViewById(R.id.tvInvoiceOderTotal);
        rcvInvoice = (RecyclerView) findViewById(R.id.rcvInvoice);
        btnPlaceOrder.setText(getResources().getString(R.string.text_place_order));
        ivFreeShipping = (ImageView) findViewById(R.id.ivFreeShipping);
        btnPlaceOrder.setOnClickListener(this);

        loadCheckOutData();

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
        // do somethings
        switch (view.getId()) {
            case R.id.btnPlaceOrder:
                payOrderPayment();
                break;

        }
    }

    private void loadCheckOutData() {
        if (getIntent().getExtras() != null) {
            isSelfDelivery = getIntent().getExtras().getBoolean(Constant.IS_USER_PICK_UP_ORDER);
        }


        for (CartProducts cartProducts : CurrentBooking.getInstance().getCartProductList()) {

            for (CartProductItems cartProductItems : cartProducts.getItems()) {
                totalItemPriceWithQuantity = totalItemPriceWithQuantity + (cartProductItems
                        .getItemPrice() * cartProductItems
                        .getQuantity());
                totalSpecificationPriceWithQuantity = totalSpecificationPriceWithQuantity +
                        (cartProductItems
                                .getTotalSpecificationPrice() * cartProductItems
                                .getQuantity());
                totalItemCount = totalItemCount + cartProductItems.getQuantity();
                for (ItemSpecification specificationsItem : cartProductItems.getSpecifications()) {
                    totalSpecificationCount = totalSpecificationCount + specificationsItem.getList()
                            .size();
                }
            }

        }

        checkIsPickUpDeliveryByUser(isSelfDelivery);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void checkIsPickUpDeliveryByUser(boolean isChecked) {
        if (CurrentBooking.getInstance().getStoreLatLng() != null && CurrentBooking.getInstance()
                .getDeliveryLatLng() != null && !isChecked) {
            getDistanceMatrix();
        } else {
            getDeliveryInvoice(0, 0);
        }
    }

    /**
     * this method called a webservice for get distance and time witch is provided by Google
     */
    private void getDistanceMatrix() {
        showCustomProgressDialog(this, false);
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
            if (getIntent().getExtras() != null) {
                jsonObject.put(Constant.USER_ID, getIntent().getExtras().getString(
                        Constant.USER_ID));
            }
            jsonObject.put(Constant.IS_USER_PICK_UP_ORDER, isSelfDelivery);
            jsonObject.put(Constant.SERVER_TOKEN, preferenceHelper
                    .getServerToken());
            jsonObject.put(Constant.STORE_ID, preferenceHelper.getStoreId());
            jsonObject.put(Constant.TOTAL_ITEM_COUNT, totalItemCount);
            jsonObject.put(Constant.TOTAL_CART_PRICE, CurrentBooking.getInstance()
                    .getTotalCartAmount());
            jsonObject.put(Constant.TOTAL_ITEM_PRICE, totalItemPriceWithQuantity);
            jsonObject.put(Constant.TOTAL_SPECIFICATION_PRICE,
                    totalSpecificationPriceWithQuantity);
            jsonObject.put(Constant.TOTAL_DISTANCE, tripDistance);
            jsonObject.put(Constant.TOTAL_TIME, timeSeconds);
            jsonObject.put(Constant.TOTAL_SPECIFICATION_COUNT, totalSpecificationCount);
            jsonObject.put(Constant.CART_UNIQUE_TOKEN, preferenceHelper.getAndroidId());
            jsonObject.put(Constant.ORDER_TYPE, Constant.Type.STORE);
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
                        setInvoiceData(response);
                        parseContent.showMessage(OrderInvoiceActivity
                                .this, response.body().getMessage());

                        if (response.body().getOrderPayment().isStorePayDeliveryFees()) {
                            ivFreeShipping.setVisibility(View.VISIBLE);
                        } else {
                            ivFreeShipping.setVisibility(View.GONE);
                        }
                        btnPlaceOrder.setVisibility(View.VISIBLE);
                    } else {
                        btnPlaceOrder.setVisibility(View.GONE);
                        if (557 == response.body().getErrorCode()) {
                            String message = getResources().getString(R.string
                                    .msg_minimum_order_amount) + " " + preferenceHelper
                                    .getCurrency()
                                    + response.body().getMinOrderPrice();
                            CustomAlterDialog customDialogAlert = new CustomAlterDialog(
                                    OrderInvoiceActivity.this, getResources().getString(R.string
                                    .text_alert), message, false, getResources().getString(R
                                    .string.text_ok), getResources().getString(R
                                    .string.text_ok)) {
                                @Override
                                public void btnOnClick(int btnId) {

                                    if (R.id.btnPositive == btnId) {
                                        OrderInvoiceActivity.this.onBackPressed();
                                    }
                                    dismiss();

                                }

                            };
                            customDialogAlert.show();

                        } else {
                            parseContent.showErrorMessage(OrderInvoiceActivity
                                    .this, response.body().getErrorCode(), false);
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<InvoiceResponse> call, Throwable t) {
                Utilities.handleThrowable("CHECKOUT_ACTIVITY", t);
            }
        });

    }

    private void setInvoiceData(Response<InvoiceResponse>
                                        response) {
        orderPayment = response.body().getOrderPayment();
        String currency = preferenceHelper.getCurrency();

        rcvInvoice.setLayoutManager(new LinearLayoutManager(this));
        rcvInvoice.setNestedScrollingEnabled(false);
        rcvInvoice.setAdapter(new OrderInvoiceAdapter(parseContent.parseInvoice(orderPayment,
                currency)));
        tvInvoiceOderTotal.setText(currency + parseContent.decimalTwoDigitFormat.format
                (orderPayment.getUserPayPayment()));
        CurrentBooking.getInstance().setTotalInvoiceAmount(orderPayment.getUserPayPayment());
        CurrentBooking.getInstance().setOrderPaymentId(orderPayment.getId());
    }

    /**
     * this method called a webservice fro make payment for delivery order
     */
    private void payOrderPayment() {
        Utilities.showCustomProgressDialog(this, false);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.USER_ID, orderPayment.getUserId());
            jsonObject.put(Constant.IS_PAYMENT_MODE_CASH, true);
            jsonObject.put(Constant.ORDER_TYPE, Constant.Type.STORE);
            jsonObject.put(Constant.ORDER_PAYMENT_ID, CurrentBooking.getInstance()
                    .getOrderPaymentId());
        } catch (JSONException e) {
            Utilities.handleThrowable(TAG, e);
        }

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<IsSuccessResponse> responseCall = apiInterface.payOrderPayment(ApiClient
                .makeJSONRequestBody(jsonObject));
        responseCall.enqueue(new Callback<IsSuccessResponse>() {
            @Override
            public void onResponse(Call<IsSuccessResponse> call, Response<IsSuccessResponse>
                    response) {
                if (parseContent.isSuccessful(response)) {
                    if (response.body().isSuccess()) {
                        createOrder();
                    } else {
                        Utilities.hideCustomProgressDialog();
                        parseContent.showErrorMessage(OrderInvoiceActivity
                                .this, response.body().getErrorCode(), false);
                    }
                }
            }

            @Override
            public void onFailure(Call<IsSuccessResponse> call, Throwable t) {
                Utilities.handleThrowable(TAG, t);
            }
        });
    }

    /**
     * this method called a webservice for create delivery order
     */
    private void createOrder() {
        Utilities.showCustomProgressDialog(this, false);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.USER_ID, orderPayment.getUserId());
            jsonObject.put(Constant.CART_ID, preferenceHelper.getCartId());
            jsonObject.put(Constant.ORDER_TYPE, Constant.Type.STORE);
            jsonObject.put(Constant.ORDER_PAYMENT_ID, CurrentBooking.getInstance()
                    .getOrderPaymentId());

        } catch (JSONException e) {
            Utilities.handleException(PaymentActivity.class.getName(), e);
        }

        Utilities.printLog("CREATE_ORDER", jsonObject.toString());

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<IsSuccessResponse> responseCall = apiInterface.createOrder(ApiClient
                .makeJSONRequestBody(jsonObject));
        responseCall.enqueue(new Callback<IsSuccessResponse>() {
            @Override
            public void onResponse(Call<IsSuccessResponse> call, Response<IsSuccessResponse>
                    response) {
                Utilities.hideCustomProgressDialog();
                if (parseContent.isSuccessful(response)) {
                    if (response.body().isSuccess()) {
                        parseContent.showMessage(OrderInvoiceActivity
                                .this, response.body().getMessage());
                        CurrentBooking.getInstance().clearCart();
                        preferenceHelper.putCartId("");
                        preferenceHelper.putAndroidId(Utilities.generateRandomString());
                        goToHomeActivity();
                    } else {
                        parseContent.showErrorMessage(OrderInvoiceActivity
                                .this, response.body().getErrorCode(), false);
                    }
                }

            }

            @Override
            public void onFailure(Call<IsSuccessResponse> call, Throwable t) {
                Utilities.handleThrowable("PAYMENT_ACTIVITY", t);
            }
        });
    }
}
