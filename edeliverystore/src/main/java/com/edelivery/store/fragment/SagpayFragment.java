package com.edelivery.store.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.edelivery.store.adapter.SagpayCardAdapter;
import com.edelivery.store.component.CustomAlterDialog;
import com.edelivery.store.models.datamodel.Card;
import com.edelivery.store.models.responsemodel.CardsResponse;
import com.edelivery.store.models.responsemodel.IsSuccessResponse;
import com.edelivery.store.parse.ApiClient;
import com.edelivery.store.parse.ApiInterface;
import com.edelivery.store.parse.ParseContent;
import com.edelivery.store.utils.Constant;
import com.edelivery.store.utils.PreferenceHelper;
import com.edelivery.store.utils.Utilities;
import com.edelivery.store.widgets.CustomInputEditText;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;
import com.google.android.material.textfield.TextInputLayout;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentAuthConfig;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.SetupIntentResult;
import com.stripe.android.Stripe;
import com.stripe.android.model.SetupIntent;
import com.stripe.android.view.CardMultilineWidget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by elluminati on 01-Apr-17.
 */

public class SagpayFragment extends BasePaymentFragments {


    public static final String TAG = com.edelivery.store.fragment.SagpayFragment.class.getName();
    private static final Pattern CODE_PATTERN = Pattern
            .compile("([0-9]{0,4})|([0-9]{4}-)+|([0-9]{4}-[0-9]{0,4})+");
    public ArrayList<Card> cardList;
    private RecyclerView rcvStripCards;
    private SagpayCardAdapter cardAdapter;
    private Dialog addCardDialog;
    private CardMultilineWidget stripeCard;
    private Stripe stripe;
    private CustomTextView tvAddCard;
    private PreferenceHelper preferenceHelper;
    private CustomInputEditText etCardHolderName;
    private TextInputLayout tiCardholderName;
    private CheckBox cbSaveCard;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View stripeFragView = inflater.inflate(R.layout.fragment_sagpay, container, false);
        rcvStripCards = (RecyclerView) stripeFragView.findViewById(R.id.rcvStripCards);
        tvAddCard = (CustomTextView) stripeFragView.findViewById(R.id.tvAddCard);

        return stripeFragView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        cardList = new ArrayList<>();
        tvAddCard.setOnClickListener(this);
        preferenceHelper = PreferenceHelper.getPreferenceHelper(paymentActivity);
        //initStripePayment();
        getAllCards();

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

        PaymentConfiguration.init(paymentActivity,
                paymentActivity.paymentKeyId);
        stripe = new Stripe(paymentActivity,
                PaymentConfiguration.getInstance(paymentActivity).getPublishableKey());
    }

    @Override
    public void onClick(View view) {
        // do somethings
        switch (view.getId()) {
            case R.id.tvAddCard:
                openAddCardDialog(false);
                break;
            default:
                // do with default
                break;
        }

    }

    /**
     * this method called webservice for get all save credit card in stripe
     */
    public void getAllCards() {
        Utilities.showCustomProgressDialog(paymentActivity, false);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.SERVER_TOKEN, paymentActivity.preferenceHelper
                    .getServerToken());
            jsonObject.put(Constant.USER_ID, paymentActivity
                    .preferenceHelper
                    .getStoreId());
            jsonObject.put(Constant.TYPE, Constant.Type.STORE);
        } catch (JSONException e) {
            Utilities.handleThrowable(TAG, e);
        }

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<CardsResponse> responseCall = apiInterface.getAllCreditCards(ApiClient
                .makeJSONRequestBody(jsonObject));
        responseCall.enqueue(new Callback<CardsResponse>() {
            @Override
            public void onResponse(Call<CardsResponse> call, Response<CardsResponse> response) {
                if (paymentActivity.parseContent.isSuccessful(response)) {
                    Utilities.hideCustomProgressDialog();
                    if (response.body().isSuccess()) {
                        cardList.clear();
                        cardList.addAll(response.body().getCards());
                        setSelectCreditCart();
                        initRcvCards();
                    } else {
                        ParseContent.getParseContentInstance().showErrorMessage(paymentActivity,
                                response.body()
                                        .getErrorCode(), false);
                    }
                }
            }

            @Override
            public void onFailure(Call<CardsResponse> call, Throwable t) {
                Utilities.handleThrowable(TAG, t);
            }
        });

    }

    private void initRcvCards() {
        if (cardAdapter != null) {
            cardAdapter.notifyDataSetChanged();
        } else {
            rcvStripCards.setLayoutManager(new LinearLayoutManager(paymentActivity));
            cardAdapter = new SagpayCardAdapter(this, cardList);
            rcvStripCards.setAdapter(cardAdapter);
            rcvStripCards.addItemDecoration(new DividerItemDecoration(paymentActivity,
                    LinearLayoutManager.VERTICAL));
        }
    }

    public void openAddCardDialog(boolean isWallet) {

        if (addCardDialog != null && addCardDialog.isShowing()) {
            return;
        }

        addCardDialog = new Dialog(paymentActivity);
        addCardDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        addCardDialog.setContentView(R.layout.dialog_add_credit_card);
        addCardDialog.findViewById(
                R.id.btnDialogAlertRight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidate()) {
                    saveCreditCard(paymentActivity.gatewayItem.getId(), isWallet);

                    /*try {
                        Log.d("STRRR", stripeCard.getCard().getNumber());
                            JSONObject obj = new JSONObject();
                        obj.put(Constant.SagpayParam.CARDHOLDERNAME, etCardHolderName.getText().toString());
                        obj.put(Constant.SagpayParam.CARDNUMBER,stripeCard.getCard().getNumber());
                        String year = String.valueOf(stripeCard.getCard().getExpYear()).substring(2,String.valueOf(stripeCard.getCard().getExpYear()).length());
                        String month = stripeCard.getCard().getExpMonth() <= 9 ? "0"+ String.valueOf(stripeCard.getCard().getExpMonth()) : stripeCard.getCard().getExpMonth()+"";
                        obj.put(Constant.SagpayParam.EXPIRYDATE,month+year);
                        obj.put(Constant.SagpayParam.SECURITYCODE,stripeCard.getCard().getCVC());

                        JSONObject card_obj = new JSONObject().put(Constant.SagpayParam.CARDDETAILS,obj);
                        AppLog.Log("CARD_OBJ",card_obj.toString());;
                        getSagpay_sessionKay(card_obj,isWallet);

                    }catch (Exception e){}*/

                }
            }
        });

        addCardDialog.findViewById(
                R.id.btnDialogAlertLeft).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCardDialog.dismiss();
            }
        });

        stripeCard = addCardDialog.findViewById(R.id.stripeCard);
        etCardHolderName = addCardDialog.findViewById(R.id.etCardHolderName);
        tiCardholderName = addCardDialog.findViewById(R.id.tiCardholderName);
        cbSaveCard = addCardDialog.findViewById(R.id.cbSaveCard);

        WindowManager.LayoutParams params = addCardDialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        addCardDialog.getWindow().setAttributes(params);
        addCardDialog.setCancelable(false);
        addCardDialog.show();
    }

    private void getSagpay_Cardidentifiers(JSONObject cardObj,String merchantSessionKey,boolean iswallet) {

        ApiInterface apiInterface = new ApiClient().sagpayApi(paymentActivity,merchantSessionKey).create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.get_sagpay_card_identifiers(ApiClient
                .makeJSONRequestBody(cardObj));

        call.enqueue(new Callback<ResponseBody>() {
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                Utilities.hideCustomProgressDialog();
                try {
                    if(response.isSuccessful()) {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        if (jsonObject.has(Constant.SagpayParam.CARDIDENTIFIER)) {
                            String cardIdentifier = jsonObject.getString(Constant.SagpayParam.CARDIDENTIFIER);
                            String cardExpiryDate = jsonObject.getString(Constant.SagpayParam.EXPIRY);
                            paymentActivity.addWalletAmount("",
                                    merchantSessionKey, cardExpiryDate, cardIdentifier, stripeCard.getCard().getLast4(), etCardHolderName.getText().toString().trim(), cbSaveCard.isChecked() ? 1 : 2);
                            //save_cardIdentifier(cardIdentifier,merchantSessionKey);
                            closedAddCardDialog();

                        } else {

                            Utilities.showToast(paymentActivity, jsonObject.getString(Constant.SagpayParam.DESCRIPTION));
                            Utilities.hideCustomProgressDialog();
                        }
                    }else {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONArray errors = jsonObject.getJSONArray(Constant.SagpayParam.ERRORS);
                        Utilities.showToast(paymentActivity, ((JSONObject)errors.get(0)).
                                getString(Constant.SagpayParam.CLIENTMESSAGE));

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Utilities.showToast(paymentActivity, e.toString());
                    Utilities.hideCustomProgressDialog();
                }
            }
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Log.d("Cardidentifiers",t.toString());
            }
        });

    }

    private void closedAddCardDialog() {
        if (addCardDialog != null && addCardDialog.isShowing()) {
            addCardDialog.dismiss();
        }
    }

    private String getCreditCardType(String number) {
        if (!Utilities.isBlank(number)) {
            if (Utilities.hasAnyPrefix(number, com.stripe.android.model.Card
                    .PREFIXES_AMERICAN_EXPRESS)) {
                return com.stripe.android.model.Card.CardBrand.AMERICAN_EXPRESS;
            } else if (Utilities.hasAnyPrefix(number, com.stripe.android.model.Card
                    .PREFIXES_DISCOVER)) {
                return com.stripe.android.model.Card.CardBrand.DISCOVER;
            } else if (Utilities.hasAnyPrefix(number, com.stripe.android.model.Card.PREFIXES_JCB)) {
                return com.stripe.android.model.Card.CardBrand.JCB;
            } else if (Utilities.hasAnyPrefix(number, com.stripe.android.model.Card
                    .PREFIXES_DINERS_CLUB)) {
                return com.stripe.android.model.Card.CardBrand.DINERS_CLUB;
            } else if (Utilities.hasAnyPrefix(number, com.stripe.android.model.Card.PREFIXES_VISA)) {
                return com.stripe.android.model.Card.CardBrand.VISA;
            } else if (Utilities.hasAnyPrefix(number, com.stripe.android.model.Card
                    .PREFIXES_MASTERCARD)) {
                return com.stripe.android.model.Card.CardBrand.MASTERCARD;
            } else {
                return com.stripe.android.model.Card.CardBrand.UNKNOWN;
            }
        }
        return com.stripe.android.model.Card.CardBrand.UNKNOWN;

    }


    private boolean isValidate() {
        String msg = null;
        if (etCardHolderName.getText().toString().trim().isEmpty()) {
            tiCardholderName.setError(getString(R.string.msg_please_enter_valid_name));
            etCardHolderName.requestFocus();
            return false;
        } else if (!stripeCard.validateAllFields()) {
            msg = getString(R.string.msg_card_invalid);
        }

        tiCardholderName.setError(null);

        if (msg != null) {
            Utilities.showToast(paymentActivity, msg);
            return false;

        }

        return true;

    }



    private void saveCreditCard(String paymentId, boolean isWallet) {
        Utilities.showCustomProgressDialog(paymentActivity, false);
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put(Constant.PAYMENT_ID, paymentId);

        } catch (JSONException e) {
            Utilities.handleThrowable(TAG, e);
        }
        Call<CardsResponse> call =
                ApiClient.getClient().create(ApiInterface.class).getStripSetupIntent
                        (ApiClient.makeJSONRequestBody(jsonObject));
        call.enqueue(new Callback<CardsResponse>() {
            @Override
            public void onResponse(Call<CardsResponse> call, Response<CardsResponse> response) {
                if (response.isSuccessful() && response.body().isSuccess()) {
                    try {
                        Log.d("STRRR", stripeCard.getCard().getNumber());
                        JSONObject obj = new JSONObject();
                        obj.put(Constant.SagpayParam.CARDHOLDERNAME, etCardHolderName.getText().toString());
                        obj.put(Constant.SagpayParam.CARDNUMBER,stripeCard.getCard().getNumber());
                        String year = String.valueOf(stripeCard.getCard().getExpYear()).substring(2,String.valueOf(stripeCard.getCard().getExpYear()).length());
                        String month = stripeCard.getCard().getExpMonth() <= 9 ? "0"+ String.valueOf(stripeCard.getCard().getExpMonth()) : stripeCard.getCard().getExpMonth()+"";
                        obj.put(Constant.SagpayParam.EXPIRYDATE,month+year);
                        obj.put(Constant.SagpayParam.SECURITYCODE,stripeCard.getCard().getCVC());

                        JSONObject card_obj = new JSONObject().put(Constant.SagpayParam.CARDDETAILS,obj);
                        Utilities.printLog("CARD_OBJ",card_obj.toString());;
//                        getSagpay_sessionKay(card_obj,isWallet);
                        getSagpay_Cardidentifiers(card_obj, response.body().getMerchantSessionKey(),isWallet);

                    }catch (Exception e){}

                   /* PaymentMethodCreateParams createPaymentParams =
                            PaymentMethodCreateParams.create(stripeCard.getCard().toPaymentMethodParamsCard(), new PaymentMethod.BillingDetails.Builder()
                                    .setName(etCardHolderName.getText().toString().trim())
                                    .setEmail(preferenceHelper.getEmail())
                                    .setPhone(preferenceHelper.getPhoneCountyCodeCode() + preferenceHelper.getPhoneNumber()).build());
                    stripe.confirmSetupIntent(SagpayFragment.this,
                            ConfirmSetupIntentParams.create(createPaymentParams,
                                    response.body().getClientSecret()));*/
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


    /**
     /**
     * this method call a webservice for save credit card
     */
    private void addCreditCard(String paymentMethod, String lastFourDigits) {
        Utilities.showCustomProgressDialog(paymentActivity, false);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.SERVER_TOKEN, paymentActivity.preferenceHelper
                    .getServerToken());
            jsonObject.put(Constant.USER_ID, paymentActivity
                    .preferenceHelper
                    .getStoreId());
            jsonObject.put(Constant.PAYMENT_METHOD, paymentMethod);
            jsonObject.put(Constant.LAST_FOUR, lastFourDigits);
            jsonObject.put(Constant.CARD_TYPE, getCreditCardType(Objects.requireNonNull(
                    stripeCard.getCard()).getNumber()));
            jsonObject.put(Constant.PAYMENT_ID, paymentActivity.gatewayItem.getId());
            jsonObject.put(Constant.TYPE, Constant.Type.STORE);
            jsonObject.put(Constant.NAME, etCardHolderName.getText().toString().trim());
        } catch (JSONException e) {
            Utilities.handleThrowable(TAG, e);
        }

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<CardsResponse> responseCall = apiInterface.getAddCreditCard(ApiClient
                .makeJSONRequestBody(jsonObject));
        responseCall.enqueue(new Callback<CardsResponse>() {
            @Override
            public void onResponse(Call<CardsResponse> call, Response<CardsResponse> response) {
                if (paymentActivity.parseContent.isSuccessful(response)) {
                    Utilities.hideCustomProgressDialog();
                    if (response.body().isSuccess()) {
                        cardList.add(response.body().getCard());
                        initRcvCards();
                        setSelectCreditCart();
                        closedAddCardDialog();
                    } else {
                        ParseContent.getParseContentInstance().showErrorMessage(paymentActivity,
                                response.body()
                                        .getErrorCode(), false);
                    }
                }
            }

            @Override
            public void onFailure(Call<CardsResponse> call, Throwable t) {
                Utilities.handleThrowable(TAG, t);
            }
        });

    }

    public void selectCreditCard(final int selectedCardPosition) {
        Utilities.showCustomProgressDialog(paymentActivity, false);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.SERVER_TOKEN, paymentActivity.preferenceHelper
                    .getServerToken());
            jsonObject.put(Constant.USER_ID, paymentActivity
                    .preferenceHelper
                    .getStoreId());
            jsonObject.put(Constant.TYPE, Constant.Type.STORE);
            jsonObject.put(Constant.CARD_ID, cardList.get
                    (selectedCardPosition).getId());
        } catch (JSONException e) {
            Utilities.handleThrowable(TAG, e);
        }
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<CardsResponse> responseCall = apiInterface.selectCreditCard(ApiClient
                .makeJSONRequestBody(jsonObject));
        responseCall.enqueue(new Callback<CardsResponse>() {
            @Override
            public void onResponse(Call<CardsResponse> call, Response<CardsResponse> response) {
                if (paymentActivity.parseContent.isSuccessful(response)) {
                    Utilities.hideCustomProgressDialog();
                    if (response.body().isSuccess()) {
                        for (Card card : cardList) {
                            card.setIsDefault(false);
                        }
                        cardList.get(selectedCardPosition).setIsDefault(response.body()
                                .getCard()
                                .isIsDefault());
                        setSelectCreditCart();
                        cardAdapter.notifyDataSetChanged();
                    } else {
                        ParseContent.getParseContentInstance().showErrorMessage(paymentActivity,
                                response.body()
                                        .getErrorCode(), false);
                    }
                }
            }

            @Override
            public void onFailure(Call<CardsResponse> call, Throwable t) {
                Utilities.handleThrowable(TAG, t);
            }
        });
    }

    private void deleteCreditCard(final int deleteCardPosition) {
        Utilities.showCustomProgressDialog(paymentActivity, false);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.SERVER_TOKEN, paymentActivity.preferenceHelper
                    .getServerToken());
            jsonObject.put(Constant.USER_ID, paymentActivity
                    .preferenceHelper
                    .getStoreId());
            jsonObject.put(Constant.CARD_ID, cardList.get
                    (deleteCardPosition).getId());
            jsonObject.put(Constant.TYPE, Constant.Type.STORE);
        } catch (JSONException e) {
            Utilities.handleThrowable(TAG, e);
        }

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<IsSuccessResponse> responseCall = apiInterface.deleteCreditCard(ApiClient
                .makeJSONRequestBody(jsonObject));
        responseCall.enqueue(new Callback<IsSuccessResponse>() {
            @Override
            public void onResponse(Call<IsSuccessResponse> call, Response<IsSuccessResponse>
                    response) {
                if (paymentActivity.parseContent.isSuccessful(response)) {
                    Utilities.hideCustomProgressDialog();
                    if (response.body().isSuccess()) {
                        if (cardList.get(deleteCardPosition).isIsDefault()) {
                            cardList.remove(deleteCardPosition);
                            if (!cardList.isEmpty()) {
                                selectCreditCard(0);
                            } else {
                                setSelectCreditCart();
                            }
                        } else {
                            cardList.remove(deleteCardPosition);
                            setSelectCreditCart();
                        }
                        cardAdapter.notifyDataSetChanged();
                    } else {
                        ParseContent.getParseContentInstance().showErrorMessage(paymentActivity,
                                response.body()
                                        .getErrorCode(), false);
                    }
                }
            }

            @Override
            public void onFailure(Call<IsSuccessResponse> call, Throwable t) {
                Utilities.handleThrowable(TAG, t);
            }
        });

    }

    public void openDeleteCard(final int position) {
        CustomAlterDialog customDialogAlert = new CustomAlterDialog(paymentActivity, paymentActivity
                .getResources()
                .getString(R.string.text_delete_card), paymentActivity.getResources()
                .getString(R.string.text_are_you_sure), true, paymentActivity.getResources()
                .getString(R.string.text_ok), paymentActivity.getResources()
                .getString(R.string.text_cancel)) {
            @Override
            public void btnOnClick(int btnId) {
                if (R.id.btnPositive == btnId) {
                    deleteCreditCard(position);
                }
                dismiss();
            }
        };
        customDialogAlert.show();

    }


    private void setSelectCreditCart() {
        paymentActivity.selectCard = null;
        for (Card card : cardList) {
            if (card.isIsDefault()) {
                paymentActivity.selectCard = card;
                return;
            }
        }

    }

    public void addWalletAmount() {
        if (paymentActivity.selectCard != null) {
            paymentActivity.createStripePaymentIntent();
        } else {
            Utilities.showToast(paymentActivity, paymentActivity.getResources().getString(R.string
                    .msg_plz_add_card_first));
        }
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        stripe.onSetupResult(requestCode, data, new ApiResultCallback<SetupIntentResult>() {
            @Override
            public void onSuccess(@NonNull SetupIntentResult result) {
                final SetupIntent setupIntent = result.getIntent();
                final SetupIntent.Status status = setupIntent.getStatus();
                if (status == SetupIntent.Status.Succeeded) {
                    addCreditCard(setupIntent.getPaymentMethodId(), Objects.requireNonNull(stripeCard.getCard()).getLast4());
                } else if (status == SetupIntent.Status.Canceled) {
                    Utilities.hideCustomProgressDialog();
                    Utilities.showToast(paymentActivity,
                            getString(R.string.error_payment_cancel) );
                } else if (status == SetupIntent.Status.Processing) {
                    Utilities.hideCustomProgressDialog();
                    Utilities.showToast(paymentActivity,
                            getString(R.string.error_payment_processing));
                } else if (status == SetupIntent.Status.RequiresPaymentMethod) {
                    Utilities.hideCustomProgressDialog();
                    Utilities.showToast(paymentActivity,
                            getString(R.string.error_payment_auth));
                } else if (status == SetupIntent.Status.RequiresAction || status ==
                        SetupIntent.Status.RequiresConfirmation) {
                    Utilities.hideCustomProgressDialog();
                    Utilities.showToast(paymentActivity,
                            getString(R.string.error_payment_action));
                } else if (status == SetupIntent.Status.RequiresCapture) {
                    Utilities.hideCustomProgressDialog();
                    Utilities.showToast(paymentActivity,
                            getString(R.string.error_payment_capture));
                } else {
                    Utilities.hideCustomProgressDialog();
                }
            }

            @Override
            public void onError(@NonNull Exception e) {
                Utilities.hideCustomProgressDialog();
                Utilities.showToast(paymentActivity,
                        e.getMessage());
            }
        });

    }

}
