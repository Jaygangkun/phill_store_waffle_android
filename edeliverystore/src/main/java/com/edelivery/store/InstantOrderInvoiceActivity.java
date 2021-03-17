package com.edelivery.store;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.edelivery.store.adapter.OrderInvoiceAdapter;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InstantOrderInvoiceActivity extends BaseActivity {

    public static final String TAG = "InstantOrderInvoiceActivity";
    private CustomButton btnPlaceOrder;
    private CustomFontTextViewTitle tvInvoiceOderTotal;
    private RecyclerView rcvInvoice;
    private InvoiceResponse invoiceResponse;
    private String vehicleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instant_order_invoice);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbar(toolbar, R.drawable.ic_back, R.color.color_app_theme);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utilities.hideSoftKeyboard(InstantOrderInvoiceActivity.this);
                onBackPressed();
            }
        });
        ((TextView) findViewById(R.id.tvToolbarTitle)).setText(getString(R.string
                .text_invoice));
        btnPlaceOrder = (CustomButton) findViewById(R.id.btnPlaceOrder);
        tvInvoiceOderTotal = (CustomFontTextViewTitle) findViewById(R.id.tvInvoiceOderTotal);
        rcvInvoice = (RecyclerView) findViewById(R.id.rcvInvoice);
        btnPlaceOrder.setText(getResources().getString(R.string.text_place_order));
        btnPlaceOrder.setOnClickListener(this);

        if (getIntent().getExtras() != null) {
            invoiceResponse = getIntent().getExtras().getParcelable(Constant
                    .INVOICE);
            vehicleId = getIntent().getExtras().getString(Constant.VEHICLE_ID);
            setInvoiceData(invoiceResponse);
        }

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


    private void setInvoiceData(InvoiceResponse
                                        invoiceResponse) {
        OrderPaymentDetail orderPayment = invoiceResponse.getOrderPayment();
        String currency = preferenceHelper.getCurrency();

        rcvInvoice.setLayoutManager(new LinearLayoutManager(this));
        rcvInvoice.setNestedScrollingEnabled(false);
        rcvInvoice.setAdapter(new OrderInvoiceAdapter(parseContent.parseInvoice(orderPayment,
                currency)));
        tvInvoiceOderTotal.setText(currency + parseContent.decimalTwoDigitFormat.format
                (orderPayment.getTotal()));
        CurrentBooking.getInstance().setTotalInvoiceAmount(orderPayment.getTotal());
        CurrentBooking.getInstance().setOrderPaymentId(orderPayment.getId());
    }

    /**
     * this method called a webservice fro make payment for delivery order
     */
    private void payOrderPayment() {
        Utilities.showCustomProgressDialog(this, false);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.USER_ID, invoiceResponse.getOrderPayment().getUserId());
            jsonObject.put(Constant.IS_PAYMENT_MODE_CASH, true);
            jsonObject.put(Constant.ORDER_TYPE, Constant.Type.STORE);
            jsonObject.put(Constant.ORDER_PAYMENT_ID, CurrentBooking.getInstance()
                    .getOrderPaymentId());
        } catch (JSONException e) {
            Utilities.handleThrowable(TAG, e);
        }

        Utilities.printLog("payOrderPayment", jsonObject.toString());

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<IsSuccessResponse> responseCall = apiInterface.payOrderPayment(ApiClient
                .makeJSONRequestBody(jsonObject));
        responseCall.enqueue(new Callback<IsSuccessResponse>() {
            @Override
            public void onResponse(Call<IsSuccessResponse> call, Response<IsSuccessResponse>
                    response) {
                if (parseContent.isSuccessful(response)) {
                    if (response.body().isSuccess()) {
                        createOrderWithEmptyCart();
                    } else {
                        Utilities.hideCustomProgressDialog();
                        parseContent.showErrorMessage(InstantOrderInvoiceActivity
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    /**
     * this method called a webservice for create delivery order
     */
    private void createOrderWithEmptyCart() {
        Utilities.showCustomProgressDialog(this, false);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.SERVER_TOKEN, preferenceHelper.getServerToken());
            jsonObject.put(Constant.STORE_ID, preferenceHelper.getStoreId());
            jsonObject.put(Constant.ORDER_TYPE, Constant.Type.STORE);
            jsonObject.put(Constant.CART_ID, preferenceHelper.getCartId());
            jsonObject.put(Constant.VEHICLE_ID, vehicleId);

        } catch (JSONException e) {
            Utilities.handleException(PaymentActivity.class.getName(), e);
        }

        Utilities.printLog("CREATE_ORDER", jsonObject.toString());

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<IsSuccessResponse> responseCall = apiInterface.createOrderWithEmptyCart(ApiClient
                .makeJSONRequestBody(jsonObject));
        responseCall.enqueue(new Callback<IsSuccessResponse>() {
            @Override
            public void onResponse(Call<IsSuccessResponse> call, Response<IsSuccessResponse>
                    response) {
                Utilities.hideCustomProgressDialog();
                if (parseContent.isSuccessful(response)) {
                    if (response.body().isSuccess()) {
                        parseContent.showMessage(InstantOrderInvoiceActivity
                                .this, response.body().getMessage());
                        CurrentBooking.getInstance().clearCart();
                        preferenceHelper.putCartId("");
                        preferenceHelper.putAndroidId(Utilities.generateRandomString());
                        goToHomeActivity();
                    } else {
                        parseContent.showErrorMessage(InstantOrderInvoiceActivity
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
