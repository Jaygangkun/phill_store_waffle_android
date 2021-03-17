package com.edelivery.store;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.edelivery.store.adapter.BankSpinnerAdapter;
import com.edelivery.store.models.datamodel.BankDetail;
import com.edelivery.store.models.datamodel.Withdrawal;
import com.edelivery.store.models.responsemodel.BankDetailResponse;
import com.edelivery.store.models.responsemodel.IsSuccessResponse;
import com.edelivery.store.parse.ApiClient;
import com.edelivery.store.parse.ApiInterface;
import com.edelivery.store.parse.ParseContent;
import com.edelivery.store.utils.Constant;
import com.edelivery.store.utils.Utilities;
import com.edelivery.store.widgets.CustomButton;
import com.edelivery.store.widgets.CustomInputEditText;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;

import java.util.ArrayList;

import androidx.annotation.IdRes;
import androidx.appcompat.widget.Toolbar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WithdrawalActivity extends BaseActivity {

    public static final String TAG = WithdrawalActivity.class.getName();
    private ArrayList<BankDetail> bankDetailArrayList;
    private RadioButton rbBankAccount, rbCash;
    private RadioGroup radioGroup2;
    private CustomInputEditText etAmount, etDescription;
    private Spinner spinnerBank;
    private CustomTextView tvAddBankAccount;
    private BankSpinnerAdapter bankSpinnerAdapter;
    private LinearLayout llSelectBank;
    private boolean isPaymentModeCash;
    private CustomButton btnWithdrawal;
    private BankDetail bankDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawal);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbar(toolbar, R.drawable.ic_back, R.color.color_app_theme);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utilities.hideSoftKeyboard(WithdrawalActivity.this);
                onBackPressed();
            }
        });
        ((TextView) findViewById(R.id.tvToolbarTitle)).setText(getString(R.string
                .text_withdrawal));
        rbBankAccount = (RadioButton) findViewById(R.id.rbBankAccount);
        rbCash = (RadioButton) findViewById(R.id.rbCash);
        radioGroup2 = (RadioGroup) findViewById(R.id.radioGroup2);
        etAmount = (CustomInputEditText) findViewById(R.id.etAmount);
        etDescription = (CustomInputEditText) findViewById(R.id.etDescription);
        spinnerBank = (Spinner) findViewById(R.id.spinnerBank);
        llSelectBank = (LinearLayout) findViewById(R.id.llSelectBank);
        btnWithdrawal = (CustomButton) findViewById(R.id.btnWithdrawal);
        tvAddBankAccount = (CustomTextView) findViewById(R.id.tvAddBankAccount);
        Utilities.setTagBackgroundRtlView(this, findViewById(R.id.tvSelectMethod));
        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rbBankAccount:
                        isPaymentModeCash = false;
                        llSelectBank.setVisibility(View.VISIBLE);
                        break;
                    case R.id.rbCash:
                        isPaymentModeCash = true;
                        llSelectBank.setVisibility(View.GONE);
                        break;

                    default:
                        // do with default
                        break;
                }
            }
        });
        rbBankAccount.setChecked(true);
        btnWithdrawal.setOnClickListener(this);

        initSpinnerBank();
        getBankDetail();
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
        switch (v.getId()) {
            case R.id.btnWithdrawal:
                if (isValidate()) {
                    createWithdrawalRequest();
                }
                break;

            default:
                // do with default
                break;
        }
    }

    private void initSpinnerBank() {
        bankDetailArrayList = new ArrayList<>();
        bankSpinnerAdapter = new BankSpinnerAdapter(this, R.layout.item_spinner_bank,
                bankDetailArrayList);
        spinnerBank.setAdapter(bankSpinnerAdapter);
        spinnerBank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bankDetail = bankDetailArrayList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    protected boolean isValidate() {
        String msg = null;

        if (!Utilities.isDecimalAndGraterThenZero(etAmount.getText().toString())) {
            msg = (getResources().getString(R.string
                    .msg_plz_enter_valid_amount));
            etAmount.setError(msg);
            etAmount.requestFocus();
        } else if (TextUtils.isEmpty(etDescription.getText().toString())) {
            msg = (getResources().getString(R.string
                    .msg_enter_description));
            etDescription.setError(msg);
            etDescription.requestFocus();
        } else if (isPaymentModeCash) {
            return true;
        } else if (bankDetail == null) {
            msg = getResources().getString(R.string.msg_plz_add_bank_detail);
            Utilities.showToast(WithdrawalActivity.this, msg);
        }
        return TextUtils.isEmpty(msg);
    }

    /**
     * this method  call webservice for get bank detail
     */
    private void getBankDetail() {
        Utilities.showCustomProgressDialog(this, false);
        final BankDetail bankDetail = new BankDetail();
        bankDetail.setBankHolderId(preferenceHelper.getStoreId());
        bankDetail.setBankHolderType(Constant.TYPE_STORE);
        bankDetail.setServerToken(preferenceHelper.getServerToken());

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<BankDetailResponse> responseCall = apiInterface.getBankDetail(ApiClient
                .makeGSONRequestBody(bankDetail));
        responseCall.enqueue(new Callback<BankDetailResponse>() {
            @Override
            public void onResponse(Call<BankDetailResponse> call, Response<BankDetailResponse>
                    response) {
                if (parseContent.isSuccessful(response)) {
                    Utilities.hideCustomProgressDialog();
                    if (response.body().isSuccess()) {
                        bankDetailArrayList.clear();
                        bankDetailArrayList.addAll(response.body().getBankDetail());
                        bankSpinnerAdapter.notifyDataSetChanged();


                    } else {
                        ParseContent.getParseContentInstance().showErrorMessage
                                (WithdrawalActivity.this, response.body().getErrorCode(), true);
                    }
                    updateUiBankAccount(bankDetailArrayList.isEmpty());
                }
            }

            @Override
            public void onFailure(Call<BankDetailResponse> call, Throwable t) {
                Utilities.handleThrowable(TAG, t);
            }
        });

    }

    private void createWithdrawalRequest() {
        Utilities.showCustomProgressDialog(this, false);
        final Withdrawal withdrawal = new Withdrawal();
        withdrawal.setProviderId(preferenceHelper.getStoreId());
        withdrawal.setServerToken(preferenceHelper.getServerToken());
        withdrawal.setBankDetail(bankDetail);
        withdrawal.setIsPaymentModeCash(isPaymentModeCash);
        withdrawal.setDescriptionForRequestWalletAmount(etDescription.getText().toString().trim());
        withdrawal.setType(Constant.TYPE_STORE);
        withdrawal.setRequestedWalletAmount(Double.valueOf(etAmount.getText().toString()));


        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<IsSuccessResponse> responseCall = apiInterface.createWithdrawalRequest
                (ApiClient
                        .makeGSONRequestBody(withdrawal));
        responseCall.enqueue(new Callback<IsSuccessResponse>() {
            @Override
            public void onResponse(Call<IsSuccessResponse> call,
                                   Response<IsSuccessResponse>
                                           response) {
                if (parseContent.isSuccessful(response)) {
                    Utilities.hideCustomProgressDialog();
                    if (response.body().isSuccess()) {
                        onBackPressed();
                    } else {
                        ParseContent.getParseContentInstance().showErrorMessage
                                (WithdrawalActivity.this, response.body().getErrorCode(), true);
                    }
                }
            }

            @Override
            public void onFailure(Call<IsSuccessResponse> call, Throwable t) {
                Utilities.handleThrowable(TAG, t);
            }
        });

    }

    private void updateUiBankAccount(boolean isUpdate) {
        if (isUpdate) {
            spinnerBank.setVisibility(View.GONE);
            tvAddBankAccount.setVisibility(View.VISIBLE);
            tvAddBankAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(WithdrawalActivity.this, AddBankDetailActivity
                            .class);
                    startActivityForResult(intent, Constant.REQUEST_UPDATE_BANK_DETAIL);
                }
            });
        } else {
            spinnerBank.setVisibility(View.VISIBLE);
            tvAddBankAccount.setVisibility(View.GONE);
            tvAddBankAccount.setOnClickListener(null);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Constant.REQUEST_UPDATE_BANK_DETAIL:
                    getBankDetail();
                    break;

                default:
                    // do with default
                    break;
            }

        }
    }
}
