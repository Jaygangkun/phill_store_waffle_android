package com.edelivery.store;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.edelivery.store.adapter.BankDetailAdapter;
import com.edelivery.store.models.datamodel.BankDetail;
import com.edelivery.store.models.responsemodel.BankDetailResponse;
import com.edelivery.store.models.responsemodel.IsSuccessResponse;
import com.edelivery.store.parse.ApiClient;
import com.edelivery.store.parse.ApiInterface;
import com.edelivery.store.parse.ParseContent;
import com.edelivery.store.utils.Constant;
import com.edelivery.store.utils.Utilities;
import com.elluminati.edelivery.store.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BankDetailActivity extends BaseActivity {
    public static final String TAG = BankDetailActivity.class.getName();
    private RecyclerView rcvBankDetail;
    private BankDetailAdapter bankDetailAdapter;
    private ArrayList<BankDetail> bankDetails;
    private FloatingActionButton floatingBtnAddBankDetail;
    private Dialog dialog;
    private EditText etVerifyPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_detail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbar(toolbar, R.drawable.ic_back, R.color.color_app_theme);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ((TextView) findViewById(R.id.tvToolbarTitle)).setText(getString(R.string
                .text_bank_detail));
        bankDetails = new ArrayList<>();
        rcvBankDetail = (RecyclerView) findViewById(R.id.rcvBankDetail);
        floatingBtnAddBankDetail = (FloatingActionButton) findViewById(R.id
                .floatingBtnAddBankDetail);
        floatingBtnAddBankDetail.setOnClickListener(this);
        initRcvBank();
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
            case R.id.floatingBtnAddBankDetail:
                goToAddBankDetailActivity(null);
                break;

            default:
                // do with default
                break;
        }
    }

    private void initRcvBank() {
        rcvBankDetail.setLayoutManager(new LinearLayoutManager(this));
        bankDetailAdapter = new BankDetailAdapter(this, bankDetails);
        rcvBankDetail.setAdapter(bankDetailAdapter);
        rcvBankDetail.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration
                .VERTICAL));
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
                        bankDetails.clear();
                        bankDetails.addAll(response.body().getBankDetail());
                        bankDetailAdapter.notifyDataSetChanged();

                    } else {
                        ParseContent.getParseContentInstance().showErrorMessage
                                (BankDetailActivity.this, response.body().getErrorCode(), true);
                    }
                }
            }

            @Override
            public void onFailure(Call<BankDetailResponse> call, Throwable t) {
                Utilities.handleThrowable(TAG, t);
            }
        });

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

    public void goToAddBankDetailActivity(BankDetail bankDetail) {
        Intent intent = new Intent(this, AddBankDetailActivity.class);
        intent.putExtra(Constant.BUNDLE, bankDetail);
        startActivityForResult(intent, Constant.REQUEST_UPDATE_BANK_DETAIL);
    }

    public void showVerificationDialog(final BankDetail bankDetail) {

        if (TextUtils.isEmpty(preferenceHelper.getSocialId()) && dialog == null) {
            dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_account_verification);
            etVerifyPassword = (TextInputEditText) dialog.findViewById(R.id.etCurrentPassword);

            dialog.findViewById(R.id.btnPositive).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(etVerifyPassword.getText().toString())) {
                        bankDetail.setPassword(etVerifyPassword.getText().toString());
                        deleteBankDetail(bankDetail);
                        dialog.dismiss();
                    } else {
                        etVerifyPassword.setError(getString(R.string.msg_empty_password));
                    }
                }
            });
            dialog.findViewById(R.id.btnNegative).setOnClickListener(new View.OnClickListener() {
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
        } else {
            bankDetail.setPassword("");
            deleteBankDetail(bankDetail);
        }


    }


    private void deleteBankDetail(BankDetail bankDetail) {
        Utilities.showCustomProgressDialog(this, false);
        bankDetail.setBankDetailId(bankDetail.getId());
        bankDetail.setBankHolderId(preferenceHelper.getStoreId());
        bankDetail.setBankHolderType(Constant.TYPE_STORE);
        bankDetail.setSocialId(preferenceHelper.getSocialId());

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<BankDetailResponse> responseCall = apiInterface.deleteBankDetail(ApiClient
                .makeGSONRequestBody(bankDetail));
        responseCall.enqueue(new Callback<BankDetailResponse>() {
            @Override
            public void onResponse(Call<BankDetailResponse> call, Response<BankDetailResponse>
                    response) {
                if (parseContent.isSuccessful(response)) {
                    Utilities.hideCustomProgressDialog();
                    if (response.body().isSuccess()) {
                        bankDetails.clear();
                        bankDetails.addAll(response.body().getBankDetail());
                        bankDetailAdapter.notifyDataSetChanged();
                    } else {
                        ParseContent.getParseContentInstance().showErrorMessage
                                (BankDetailActivity.this,
                                        response.body()
                                                .getErrorCode(), false);
                    }
                }
            }

            @Override
            public void onFailure(Call<BankDetailResponse> call, Throwable t) {
                Utilities.handleThrowable(TAG, t);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void selectBankDetail(final BankDetail bankDetail) {
        Utilities.showCustomProgressDialog(this, false);
        bankDetail.setBankDetailId(bankDetail.getId());
        bankDetail.setBankHolderId(preferenceHelper.getStoreId());
        bankDetail.setBankHolderType(Constant.TYPE_STORE);
        bankDetail.setSocialId(preferenceHelper.getSocialId());
        bankDetail.setServerToken(preferenceHelper.getServerToken());

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<IsSuccessResponse> responseCall = apiInterface.selectBankDetail(ApiClient
                .makeGSONRequestBody(bankDetail));
        responseCall.enqueue(new Callback<IsSuccessResponse>() {
            @Override
            public void onResponse(Call<IsSuccessResponse> call, Response<IsSuccessResponse>
                    response) {
                if (parseContent.isSuccessful(response)) {
                    Utilities.hideCustomProgressDialog();
                    if (response.body().isSuccess()) {
                        for (BankDetail detail : bankDetails) {
                            detail.setSelected(false);
                        }
                        bankDetail.setSelected(true);
                        bankDetailAdapter.notifyDataSetChanged();
                    } else {
                        ParseContent.getParseContentInstance().showErrorMessage
                                (BankDetailActivity.this, response
                                        .body()
                                        .getErrorCode(), false);
                    }
                }
            }

            @Override
            public void onFailure(Call<IsSuccessResponse> call, Throwable t) {
                Utilities.handleThrowable(BankDetailActivity.class.getSimpleName(), t);
            }
        });

    }
}
