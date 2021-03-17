package com.edelivery.store.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edelivery.store.WalletTransactionActivity;
import com.edelivery.store.WalletTransactionDetailActivity;
import com.edelivery.store.adapter.WalletTransactionAdapter;
import com.edelivery.store.component.CustomAlterDialog;
import com.edelivery.store.models.datamodel.WalletRequestDetail;
import com.edelivery.store.models.responsemodel.IsSuccessResponse;
import com.edelivery.store.models.responsemodel.WalletTransactionResponse;
import com.edelivery.store.parse.ApiClient;
import com.edelivery.store.parse.ApiInterface;
import com.edelivery.store.parse.ParseContent;
import com.edelivery.store.utils.Constant;
import com.edelivery.store.utils.Utilities;
import com.elluminati.edelivery.store.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.edelivery.store.utils.Utilities.handleException;

/**
 * Created by elluminati on 01-Nov-17.
 */

public class WalletTransactionFragment extends Fragment {
    public static final String TAG = WalletTransactionFragment.class.getName();
    private RecyclerView rcvWalletData;
    private WalletTransactionActivity activity;
    private CustomAlterDialog customDialogAlert;

    private ArrayList<WalletRequestDetail> walletRequestDetails;
    private WalletTransactionAdapter transactionAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (WalletTransactionActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet_transection, container, false);
        rcvWalletData = (RecyclerView) view.findViewById(R.id.rcvWalletData);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initRcvWalletTransaction();
        getWalletTransaction();

    }

    private void initRcvWalletTransaction() {
        walletRequestDetails = new ArrayList<>();
        rcvWalletData.setLayoutManager(new LinearLayoutManager(activity));
        transactionAdapter = new WalletTransactionAdapter(this, walletRequestDetails);
        rcvWalletData.setAdapter(transactionAdapter);
    }

    private void getWalletTransaction() {

        Utilities.showCustomProgressDialog(activity, false);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.SERVER_TOKEN, activity.preferenceHelper.getServerToken());
            jsonObject.put(Constant.ID, activity.preferenceHelper.getStoreId());
            jsonObject.put(Constant.TYPE, Constant.TYPE_STORE);
        } catch (JSONException e) {
            Utilities.handleException(TAG, e);
        }

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<WalletTransactionResponse> call = apiInterface.getWalletTransaction(ApiClient
                .makeJSONRequestBody(jsonObject));
        call.enqueue(new Callback<WalletTransactionResponse>() {
            @Override
            public void onResponse(Call<WalletTransactionResponse> call,
                                   Response<WalletTransactionResponse> response) {
                Utilities.hideCustomProgressDialog();
                if (activity.parseContent.isSuccessful(response)) {
                    if (response.body().isSuccess()) {
                        walletRequestDetails.clear();
                        walletRequestDetails.addAll(response.body().getWalletRequestDetail());
                        Collections.sort(walletRequestDetails, new
                                Comparator<WalletRequestDetail>() {
                                    @Override
                                    public int compare(WalletRequestDetail lhs,
                                                       WalletRequestDetail rhs) {
                                        return compareTwoDate(lhs.getCreatedAt(), rhs
                                                .getCreatedAt());
                                    }
                                });
                        transactionAdapter.notifyDataSetChanged();
                    } else {
                        ParseContent.getParseContentInstance().showErrorMessage
                                (activity, response.body().getErrorCode(), true);
                    }


                }

            }

            @Override
            public void onFailure(Call<WalletTransactionResponse> call, Throwable t) {
                Utilities.handleThrowable(TAG, t);

            }
        });
    }

    private void canceledWithdrawalRequest(String walletId) {
        Utilities.showCustomProgressDialog(activity, false);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.WALLET_STATUS, Constant.Wallet.WALLET_STATUS_CANCELLED);
            jsonObject.put(Constant.ID, walletId);
            jsonObject.put(Constant.TYPE, Constant.TYPE_STORE);
        } catch (JSONException e) {
            handleException(TAG, e);
        }

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<IsSuccessResponse> call = apiInterface.cancelWithdrawalRequest(ApiClient
                .makeJSONRequestBody(jsonObject));
        call.enqueue(new Callback<IsSuccessResponse>() {
            @Override
            public void onResponse(Call<IsSuccessResponse> call,
                                   Response<IsSuccessResponse> response) {
                Utilities.hideCustomProgressDialog();
                if (activity.parseContent.isSuccessful(response)) {
                    if (response.body().isSuccess()) {
                        getWalletTransaction();
                    } else {
                        ParseContent.getParseContentInstance().showErrorMessage
                                (activity, response.body().getErrorCode(), true);
                    }

                }

            }

            @Override
            public void onFailure(Call<IsSuccessResponse> call, Throwable t) {
                Utilities.handleThrowable(TAG, t);

            }
        });

    }

    public void goToWalletTransactionActivity(WalletRequestDetail walletRequestDetail) {
        Intent intent = new Intent(activity, WalletTransactionDetailActivity.class);
        intent.putExtra(Constant.BUNDLE, walletRequestDetail);
        startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }


    public void openCancelWithdrawalRequestDialog(final String walletId) {


        if (customDialogAlert != null && customDialogAlert.isShowing()) {
            return;
        }
        customDialogAlert = new CustomAlterDialog(activity, getResources().getString(R.string
                .text_cancel_wallet_request),
                getResources().getString(R.string.text_are_you_sure), true, getResources()
                .getString(R
                        .string.text_ok), getResources().getString(R.string.text_cancel)) {

            @Override
            public void btnOnClick(int btnId) {
                if (btnId == R.id.btnPositive) {
                    dismiss();
                    canceledWithdrawalRequest(walletId);
                } else {
                    dismiss();
                }
            }
        };
        customDialogAlert.setCancelable(false);
        customDialogAlert.show();

    }

    private int compareTwoDate(String firstStrDate, String secondStrDate) {
        try {
            SimpleDateFormat webFormat = activity.parseContent.webFormat;
            SimpleDateFormat dateFormat = activity.parseContent.dateTimeFormat;
            String date2 = dateFormat.format(webFormat.parse(secondStrDate));
            String date1 = dateFormat.format(webFormat.parse(firstStrDate));
            return date2.compareTo(date1);
        } catch (ParseException e) {
            Utilities.handleException(WalletTransactionFragment.class.getName(), e);
        }
        return 0;
    }

}
