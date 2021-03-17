package com.edelivery.store.component;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.edelivery.store.HomeActivity;
import com.edelivery.store.models.responsemodel.IsSuccessResponse;
import com.edelivery.store.models.responsemodel.OrderStatusResponse;
import com.edelivery.store.models.responsemodel.PushDataResponse;
import com.edelivery.store.parse.ApiClient;
import com.edelivery.store.parse.ApiInterface;
import com.edelivery.store.parse.ParseContent;
import com.edelivery.store.utils.Constant;
import com.edelivery.store.utils.PreferenceHelper;
import com.edelivery.store.utils.Utilities;
import com.edelivery.store.widgets.CustomButton;
import com.edelivery.store.widgets.CustomFontTextViewTitle;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;
import com.google.gson.Gson;

import java.util.HashMap;

import androidx.annotation.NonNull;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.elluminati.edelivery.store.BuildConfig.IMAGE_URL;

/**
 * Created by Elluminati Mohit on 4/21/2017.
 */

public class CustomNewOrderDialog extends Dialog implements View.OnClickListener {


    private CustomTextView txDialogTitle,
            tvDestAddress;
    private CustomFontTextViewTitle tvClientName, tvTotalItemPrice;
    private CustomTextView tvViewMore, tvOrderNo;
    private CustomButton btnNegative, btnPositive;
    private ImageView ivUserImage;
    private Context context;

    private HashMap<String, RequestBody> map;
    private ParseContent parseContent;
    private String response, orderId;
    private int count = 0;
    private PushDataResponse pushDataResponse;
    private Gson gson;

    public CustomNewOrderDialog(@NonNull Context context, String response) {
        super(context);
        this.context = context;
        this.response = response;
        parseContent = ParseContent.getParseContentInstance();
        gson = new Gson();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_new_order);


        btnNegative = (CustomButton) findViewById(R.id.btnNegative);
        btnPositive = (CustomButton) findViewById(R.id.btnPositive);
        txDialogTitle = (CustomTextView) findViewById(R.id.txDialogTitle);
        tvClientName = (CustomFontTextViewTitle) findViewById(R.id.tvClientName);
        tvTotalItemPrice = (CustomFontTextViewTitle) findViewById(R.id.tvTotalItemPrice);
        tvDestAddress = (CustomTextView) findViewById(R.id.tvDestAddress);
        ivUserImage = (ImageView) findViewById(R.id.ivUserImage);
        tvViewMore = (CustomTextView) findViewById(R.id.tvViewMore);
        tvOrderNo = (CustomTextView) findViewById(R.id.tvOrderNo);
        tvViewMore.setOnClickListener(this);
        btnNegative.setText(context.getString(R.string.text_reject));
        btnPositive.setText(context.getString(R.string.text_accept));
        btnPositive.setOnClickListener(this);
        btnNegative.setOnClickListener(this);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        setData();
        setCancelable(true);

    }

    private void setData() {
        pushDataResponse = gson.fromJson(response, PushDataResponse
                .class);

        Glide.with(context).load(IMAGE_URL.concat(pushDataResponse.getUserImage())).into
                (ivUserImage);
        txDialogTitle.setText(context.getString(R.string.text_new_order_request));
//        tvClientName.setText(pushDataResponse.getFirstName().concat(" ").concat
//                (pushDataResponse.getLastName()));
        tvClientName.setText(pushDataResponse.getFirstName());
        tvDestAddress.setText(pushDataResponse.getDestinationAddresses().get(0).getAddress());
        orderId = pushDataResponse.getOrderId();
        tvTotalItemPrice.setText(pushDataResponse.getCurrency() + " " + ParseContent
                .getParseContentInstance()
                .decimalTwoDigitFormat
                .format(pushDataResponse
                        .getTotalOrderPrice()));
        count = pushDataResponse.getOrderCount();
        tvOrderNo.setText(String.valueOf(pushDataResponse.getUniqueId()));
        updateUI();


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnNegative:
                rejectOrder();
                break;
            case R.id.btnPositive:
                acceptOrder();
                break;
            case R.id.tvViewMore:
                dismiss();
                goToActivity();

                break;
            default:
                // do with default
                break;
        }


    }

    private void acceptOrder() {
        Utilities.showProgressDialog(context);

        map = getCommonParam();
        map.put(Constant.ORDER_STATUS, ApiClient.makeTextRequestBody(String.valueOf(Constant
                .STORE_ORDER_ACCEPTED)));
        Call<OrderStatusResponse> call = ApiClient.getClient().create(ApiInterface.class)
                .setOrderStatus(map);
        call.enqueue(new Callback<OrderStatusResponse>() {
            @Override
            public void onResponse(Call<OrderStatusResponse> call, Response<OrderStatusResponse>
                    response) {
                Utilities.removeProgressDialog();
                sendBroadcast();
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        dismiss();
                    } else {
                        ParseContent.getParseContentInstance().showErrorMessage(context, response
                                .body().getErrorCode(), false);
                    }
                } else {
                    Utilities.showHttpErrorToast(response.code(), context);
                }
            }

            @Override
            public void onFailure(Call<OrderStatusResponse> call, Throwable t) {
                Utilities.printLog("new order dialog", t.getMessage());
            }
        });
    }

    private void rejectOrder() {

        Utilities.showProgressDialog(context);
        map = getCommonParam();
        map.put(Constant.ORDER_STATUS, ApiClient.makeTextRequestBody(String.valueOf(Constant
                .STORE_ORDER_REJECTED)));

        Call<IsSuccessResponse> call = ApiClient.getClient().create(ApiInterface.class)
                .CancelOrRejectOrder(map);
        call.enqueue(new Callback<IsSuccessResponse>() {
            @Override
            public void onResponse(Call<IsSuccessResponse> call, Response<IsSuccessResponse>
                    response) {
                Utilities.removeProgressDialog();
                sendBroadcast();
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        dismiss();
                    } else {
                        ParseContent.getParseContentInstance().showErrorMessage(context, response
                                .body().getErrorCode(), false);
                    }
                } else {
                    Utilities.showHttpErrorToast(response.code(), context);
                }
            }

            @Override
            public void onFailure(Call<IsSuccessResponse> call, Throwable t) {
                Utilities.printLog("new order dialog", t.getMessage());
            }
        });
    }

    private HashMap<String, RequestBody> getCommonParam() {
        map = new HashMap<>();
        map.put(Constant.STORE_ID, ApiClient.makeTextRequestBody(
                PreferenceHelper.getPreferenceHelper(context).getStoreId()));
        map.put(Constant.SERVER_TOKEN, ApiClient.makeTextRequestBody(PreferenceHelper
                .getPreferenceHelper(context)
                .getServerToken()));
        map.put(Constant.ORDER_ID, ApiClient.makeTextRequestBody(
                orderId));
        return map;
    }

    private void sendBroadcast() {
        context.sendBroadcast(new Intent(Constant.Action.ACTION_ORDER_STATUS_ACTION));
    }

    public void notifyDataSetChange(String response) {

        pushDataResponse = gson.fromJson(response, PushDataResponse
                .class);
        count = pushDataResponse.getOrderCount();
        updateUI();

    }

    private void updateUI() {

        if (count >= 2) {
            btnNegative.setVisibility(View.GONE);
            btnPositive.setVisibility(View.GONE);
            tvViewMore.setVisibility(View.VISIBLE);
            tvViewMore.setText(context.getResources().getString(R.string.text_view_more) + " "
                    + "+"
                    + (count - 1));
        } else {
            btnNegative.setVisibility(View.VISIBLE);
            btnPositive.setVisibility(View.VISIBLE);
            tvViewMore.setVisibility(View.VISIBLE);
            tvViewMore.setText(context.getResources().getString(R.string.text_view_more));
        }


    }

    private void goToActivity() {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }
}
