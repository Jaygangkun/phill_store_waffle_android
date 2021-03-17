package com.edelivery.store;

import android.content.Intent;
import android.os.Bundle;

import com.edelivery.store.fragment.SagpayFragment;
import com.edelivery.store.models.datamodel.Card;
import com.edelivery.store.models.responsemodel.CardsResponse;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.edelivery.store.adapter.ViewPagerAdapter;
import com.edelivery.store.fragment.PayPalFragment;
import com.edelivery.store.fragment.StripeFragment;
import com.edelivery.store.models.datamodel.PaymentGateway;
import com.edelivery.store.models.responsemodel.PaymentGatewayResponse;
import com.edelivery.store.models.responsemodel.WalletResponse;
import com.edelivery.store.parse.ApiClient;
import com.edelivery.store.parse.ParseContent;
import com.edelivery.store.parse.ApiInterface;
import com.edelivery.store.utils.Constant;
import com.edelivery.store.utils.Utilities;
import com.edelivery.store.widgets.CustomInputEditText;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentAuthConfig;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.Stripe;
import com.stripe.android.model.ConfirmPaymentIntentParams;
import com.stripe.android.model.PaymentIntent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends BaseActivity {
    public static final String TAG = PaymentActivity.class.getName();
    public List<PaymentGateway> paymentGateways;
    public CustomInputEditText etWalletAmount;
    public PaymentGateway gatewayItem;
    private ImageView ivWithdrawal;
    private CustomTextView tvWalletAmount, tvAddWalletAmount;
    private ViewPagerAdapter viewPagerAdapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Stripe stripe;
    public String paymentKeyId;
    private boolean isPaymentForWallet;
    public Card selectCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbar(toolbar, R.drawable.ic_back, R.color.color_app_theme);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utilities.hideSoftKeyboard(PaymentActivity.this);
                onBackPressed();
            }
        });
        ((TextView) findViewById(R.id.tvToolbarTitle)).setText(getString(R.string
                .text_payments));
        ivWithdrawal = (ImageView) findViewById(R.id.ivWithdrawal);
        tvWalletAmount = (CustomTextView) findViewById(R.id.tvWalletAmount);

        ivWithdrawal.setOnClickListener(this);
        Utilities.setTagBackgroundRtlView(this, findViewById(R.id.tvWallet));
        Utilities.setTagBackgroundRtlView(this, findViewById(R.id.tvSelectMethod));
        tabLayout = (TabLayout) findViewById(R.id.paymentTabsLayout);
        viewPager = (ViewPager) findViewById(R.id.paymentViewpager);
        tvAddWalletAmount = (CustomTextView) findViewById(R.id.tvAddWalletAmount);
        etWalletAmount = (CustomInputEditText) findViewById(R.id.etWalletAmount);
        tvAddWalletAmount.setOnClickListener(this);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setSelectedPaymentGateway(tab.getText().toString());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                // do somethings
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // do somethings

            }
        });
        getPaymentGateWays();
    }

    private void initStripePayment() {
        final PaymentAuthConfig.Stripe3ds2UiCustomization uiCustomization =
                new PaymentAuthConfig.Stripe3ds2UiCustomization.Builder()
                        .build();
        PaymentAuthConfig.init(new PaymentAuthConfig.Builder()
                .set3ds2Config(new PaymentAuthConfig.Stripe3ds2Config.Builder()
                        // set a 5 minute timeout for challenge flow
                        .setTimeout(5)
                        // customize the UI of the challenge flow
                        .setUiCustomization(uiCustomization)
                        .build())
                .build());

        PaymentConfiguration.init(this,
                paymentKeyId);
        stripe = new Stripe(this,
                PaymentConfiguration.getInstance(this).getPublishableKey());
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        setToolbarEditIcon(true, R.drawable.ic_history_time);
        setToolbarCameraIcon(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.ivEditMenu) {
            goToWalletTransaction();
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivWithdrawal:
                goToWithdrawalActivity();
                break;
            case R.id.tvAddWalletAmount:
                selectPaymentGatewayForAddWalletAmount();
                break;

            default:
                // do with default
                break;
        }
    }

    private void goToWalletTransaction() {
        Intent intent = new Intent(this, WalletTransactionActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void goToWithdrawalActivity() {
        Intent intent = new Intent(this, WithdrawalActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void initTabLayout(ViewPager viewPager) {
        if (!paymentGateways.isEmpty()) {
            viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

            for (PaymentGateway paymentGateway : paymentGateways) {
                if (TextUtils.equals(paymentGateway.getId(), Constant.Payment.STRIPE)) {
                    viewPagerAdapter.addFragment(new StripeFragment(), paymentGateway.getName
                            ());
                    paymentKeyId = paymentGateway.getPaymentKeyId();
                    initStripePayment();
                }
                if (TextUtils.equals(paymentGateway.getId(), Constant.Payment.SAGPAY)) {
                    viewPagerAdapter.addFragment(new SagpayFragment(), paymentGateway.getName());

                    preferenceHelper.putSagpay_vendorName(paymentGateway.getVendorName());
                    preferenceHelper.putSagpay_baseurl(paymentGateway.getSagpay_baseurl());
                    preferenceHelper.putSagpay_Integration_Key(paymentGateway.getIntegration_Key());
                    preferenceHelper.putSagpayIntegration_Password(paymentGateway.getIntegration_Password());
                    paymentKeyId = paymentGateway.getPaymentKeyId();
                }
                if (TextUtils.equals(paymentGateway.getId(), Constant.Payment.PAY_PAL)) {
                    viewPagerAdapter.addFragment(new PayPalFragment(), paymentGateway.getName
                            ());
                }
            }
            viewPager.setAdapter(viewPagerAdapter);
            tabLayout.setupWithViewPager(viewPager);
        }
    }

    /**
     * this method called a webservice to get All Payment Gateway witch is available from admin
     * panel
     */
    private void getPaymentGateWays() {
        Utilities.showCustomProgressDialog(this, false);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.SERVER_TOKEN, preferenceHelper.getServerToken());
            jsonObject.put(Constant.USER_ID, preferenceHelper
                    .getStoreId());
            jsonObject.put(Constant.TYPE, Constant.Type.STORE);
            jsonObject.put(Constant.CITY_ID, preferenceHelper.getCityId());

        } catch (JSONException e) {
            Utilities.handleThrowable(TAG, e);
        }
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<PaymentGatewayResponse> responseCall = apiInterface.getPaymentGateway(ApiClient
                .makeJSONRequestBody(jsonObject));
        responseCall.enqueue(new Callback<PaymentGatewayResponse>() {
            @Override
            public void onResponse(Call<PaymentGatewayResponse> call,
                                   Response<PaymentGatewayResponse> response) {
                if (parseContent.isSuccessful(response)) {
                    Utilities.hideCustomProgressDialog();
                    Utilities.printLog("PAYMENT_GATEWAY", ApiClient.JSONResponse(response.body()));
                    if (response.body().isSuccess()) {
                        paymentGateways = new ArrayList<>();
                        if (response.body().getPaymentGateway() != null) {
                            paymentGateways.addAll(response.body().getPaymentGateway());
                        }
                        initTabLayout(viewPager);
                        setWalletAmount(response.body().getWalletAmount(), response.body()
                                .getWalletCurrencyCode());
                    } else {
                        ParseContent.getParseContentInstance().showErrorMessage(PaymentActivity
                                        .this,
                                response.body()
                                        .getErrorCode(), false);
                    }
                }
            }

            @Override
            public void onFailure(Call<PaymentGatewayResponse> call, Throwable t) {
                Utilities.handleThrowable(TAG, t);
            }
        });
    }

    private void setSelectedPaymentGateway(String paymentName) {
        for (PaymentGateway gatewayItem : paymentGateways) {
            if (TextUtils.equals(gatewayItem.getName(), paymentName)) {
                this.gatewayItem = gatewayItem;
                return;
            }

        }
    }

    private void selectPaymentGatewayForAddWalletAmount() {


        if (gatewayItem != null) {
            if (etWalletAmount.getVisibility() == View.GONE) {
                updateUiForWallet(true);
            } else {

                if (!Utilities.isDecimalAndGraterThenZero(etWalletAmount.getText().toString())) {
                    Utilities.showToast(this, getResources().getString(R.string
                            .msg_plz_enter_valid_amount)
                    );
                    return;
                }
                switch (gatewayItem.getId()) {
                    case Constant.Payment.STRIPE:
                        StripeFragment stripeFragment = (StripeFragment) viewPagerAdapter.getItem
                                (tabLayout.getSelectedTabPosition());
                        stripeFragment.addWalletAmount();
                        break;
                    case Constant.Payment.SAGPAY:
                        SagpayFragment sagpayFragment = (SagpayFragment) viewPagerAdapter.getItem(tabLayout.getSelectedTabPosition());
                        if(selectCard != null){
                            saveCreditCard(gatewayItem.getId(), true);
                        }else {
                            sagpayFragment.openAddCardDialog(true);
                        }
                        break;
                    case Constant.Payment.PAY_PAL:

                        break;
                    case Constant.Payment.PAY_U_MONEY:
                        break;
                    case Constant.Payment.CASH:
                        Utilities.showToast(this, getResources().getString(R.string
                                .msg_plz_select_other_payment_gateway)
                        );
                        break;
                    default:
                        // do with default
                        break;
                }
            }
        }
    }

    private void saveCreditCard(String paymentId, boolean isWallet) {
        Utilities.showCustomProgressDialog(this, false);
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put(Constant.PAYMENT_ID, paymentId);

        } catch (JSONException e) {
            Utilities.handleThrowable(SagpayFragment.class.getSimpleName(), e);
        }
        Call<CardsResponse> call =
                ApiClient.getClient().create(ApiInterface.class).getStripSetupIntent
                        (ApiClient.makeJSONRequestBody(jsonObject));
        call.enqueue(new Callback<CardsResponse>() {
            @Override
            public void onResponse(Call<CardsResponse> call, Response<CardsResponse> response) {
                if (response.isSuccessful() && response.body().isSuccess()) {
                    try {
                        addWalletAmount("",response.body().getMerchantSessionKey(),
                                selectCard.getCardExpiryDate(),
                                selectCard.getPaymentToken(),selectCard.getLastFour(), selectCard.getCardHolderName(), 0);



                    }catch (Exception e){}
                } else {
                    Utilities.hideCustomProgressDialog();
                }

            }

            @Override
            public void onFailure(Call<CardsResponse> call, Throwable t) {
                Utilities.hideCustomProgressDialog();
                Utilities.handleThrowable(SagpayFragment.class.getSimpleName(), t);
            }
        });

    }


    private void setWalletAmount(double amount, String currency) {
        preferenceHelper.putWalletAmount((float) amount);
        preferenceHelper.putWalletCurrencyCode(currency);
        tvWalletAmount.setText(parseContent.decimalTwoDigitFormat.format(amount) + " " + currency);
    }


    private void updateUiForWallet(boolean isUpdate) {
        if (isUpdate) {
            tvWalletAmount.setVisibility(View.GONE);
            etWalletAmount.setVisibility(View.VISIBLE);
            etWalletAmount.requestFocus();
            tvAddWalletAmount.setText(getResources().getString(R.string.text_submit));
        } else {
            etWalletAmount.getText().clear();
            etWalletAmount.setVisibility(View.GONE);
            tvWalletAmount.setVisibility(View.VISIBLE);
            tvAddWalletAmount.setText(getResources().getString(R.string.text_add));
        }

    }

    /**
     * this method called a webservice to get client secret and initiate payment Intent for delivery order or add wallet
     */
    public void createStripePaymentIntent() {
        Utilities.showCustomProgressDialog(this, false);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.SERVER_TOKEN, preferenceHelper.getServerToken());
            jsonObject.put(Constant.USER_ID, preferenceHelper
                    .getStoreId());
            jsonObject.put(Constant.TYPE, Constant.Type.STORE);
            jsonObject.put(Constant.PAYMENT_ID, gatewayItem.getId());
            jsonObject.put(Constant.AMOUNT, Double.parseDouble(etWalletAmount.getText().toString()));
            Call<CardsResponse> call = ApiClient.getClient().create(ApiInterface.class)
                    .getStripPaymentIntentWallet(ApiClient.makeJSONRequestBody(jsonObject));
            call.enqueue(new Callback<CardsResponse>() {
                @Override
                public void onResponse(Call<CardsResponse> call, Response<CardsResponse>
                        response) {
                    if (parseContent.isSuccessful(response)) {
                        if (response.body().isSuccess()) {
                            ConfirmPaymentIntentParams paymentIntentParams =
                                    ConfirmPaymentIntentParams.createWithPaymentMethodId(response.body().getPaymentMethod(),
                                            response.body().getClientSecret());
                            stripe.confirmPayment(PaymentActivity.this, paymentIntentParams);
                        } else {
                            Utilities.hideCustomProgressDialog();
                            ParseContent.getParseContentInstance().showErrorMessage(PaymentActivity
                                            .this,
                                    response.body()
                                            .getErrorCode(), false);
                        }
                    }

                }

                @Override
                public void onFailure(Call<CardsResponse> call, Throwable t) {
                    Utilities.hideCustomProgressDialog();
                    Utilities.handleThrowable(TAG, t);
                }
            });
        } catch (NumberFormatException e) {

            etWalletAmount.setError(getResources().getString(R.string.msg_enter_valid_amount));
        } catch (JSONException e) {
            Utilities.handleThrowable(TAG, e);
        }
    }


    /**
     * this method called a webservice for  add wallet amount
     */
    public void addWalletAmount(String paymentIntentId,
                                String merchantSessionKey,
                                String cardExpiryDate, String cardIdentifier, String lastFour, String cardHolderName, int isSave) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constant.SERVER_TOKEN, preferenceHelper.getServerToken());
            jsonObject.put(Constant.USER_ID, preferenceHelper
                    .getStoreId());
            jsonObject.put(Constant.TYPE, Constant.Type.STORE);
            jsonObject.put(Constant.PAYMENT_ID, gatewayItem.getId());
            if (gatewayItem.getId().equals(Constant.Payment.SAGPAY)) {
                jsonObject.put(Constant.SagpayParam.MERCHANTSESSIONKEY, merchantSessionKey);
                jsonObject.put(Constant.PAYMENT_METHOD, cardIdentifier);
                jsonObject.put(Constant.SagpayParam.IS_SAVE, isSave);
                jsonObject.put(Constant.CARD_EXPIRY_DATE, cardExpiryDate);
                jsonObject.put(Constant.LAST_FOUR, lastFour);
                jsonObject.put(Constant.CARD_HOLDER_NAME, cardHolderName);
            }
            jsonObject.put(Constant.WALLET, Double.valueOf(etWalletAmount.getText().toString()));
            jsonObject.put(Constant.PAYMENT_INTENT_ID, paymentIntentId);

            jsonObject.put("merchantSessionKey", merchantSessionKey);
            jsonObject.put("cardIdentifier", cardIdentifier);
            Utilities.showCustomProgressDialog(this, false);
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<WalletResponse> responseCall = apiInterface.getAddWalletAmount(ApiClient
                    .makeJSONRequestBody(jsonObject));
            responseCall.enqueue(new Callback<WalletResponse>() {
                @Override
                public void onResponse(Call<WalletResponse> call, Response<WalletResponse>
                        response) {
                    if (parseContent.isSuccessful(response)) {
                        Utilities.hideCustomProgressDialog();
                        if (response.body().isSuccess()) {
                            updateUiForWallet(false);
                            setWalletAmount(response.body().getWallet(), response.body()
                                    .getWalletCurrencyCode());
                            if(isSave == 1){
                                SagpayFragment sagpayFragment = (SagpayFragment) viewPagerAdapter.getItem(tabLayout.getSelectedTabPosition());
                                if(sagpayFragment != null){
                                    sagpayFragment.getAllCards();
                                }
                            }
                        } else {
                            ParseContent.getParseContentInstance().showErrorMessage(PaymentActivity
                                            .this,
                                    response.body()
                                            .getErrorCode(), false);
                        }
                    }
                }

                @Override
                public void onFailure(Call<WalletResponse> call, Throwable t) {
                    Utilities.handleThrowable(TAG, t);
                }
            });

        } catch (NumberFormatException e) {

            etWalletAmount.setError(getResources().getString(R.string.msg_enter_valid_amount));
        } catch (JSONException e) {
            Utilities.handleThrowable(TAG, e);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        stripe.onPaymentResult(requestCode, data, new ApiResultCallback<PaymentIntentResult>() {
            @Override
            public void onSuccess(@NonNull PaymentIntentResult result) {
                Utilities.hideCustomProgressDialog();
                final PaymentIntent paymentIntent = result.getIntent();
                final PaymentIntent.Status status = paymentIntent.getStatus();
                if (status == PaymentIntent.Status.Succeeded ||
                        status == PaymentIntent.Status.RequiresCapture) {
//                    addWalletAmount(gatewayItem.getId(), paymentIntent.getId());
                } else if (status == PaymentIntent.Status.Canceled) {
                    Utilities.hideCustomProgressDialog();
                    Utilities.showToast(PaymentActivity.this,
                            getString(R.string.error_payment_cancel));
                } else if (status == PaymentIntent.Status.Processing) {
                    Utilities.hideCustomProgressDialog();
                    Utilities.showToast(PaymentActivity.this,
                            getString(R.string.error_payment_processing));
                } else if (status == PaymentIntent.Status.RequiresPaymentMethod) {
                    Utilities.hideCustomProgressDialog();
                    Utilities.showToast(PaymentActivity.this,
                            getString(R.string.error_payment_auth));
                } else if (status == PaymentIntent.Status.RequiresAction || status ==
                        PaymentIntent.Status.RequiresConfirmation) {
                    Utilities.hideCustomProgressDialog();
                    Utilities.showToast(PaymentActivity.this,
                            getString(R.string.error_payment_action));
                } else {
                    Utilities.hideCustomProgressDialog();
                }

            }

            @Override
            public void onError(@NonNull Exception e) {
                Utilities.hideCustomProgressDialog();
                Utilities.showToast(PaymentActivity.this, e.getMessage());
            }
        });
    }
}
