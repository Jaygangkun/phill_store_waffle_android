package com.edelivery.store.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edelivery.store.WalletDetailActivity;
import com.edelivery.store.WalletTransactionActivity;
import com.edelivery.store.adapter.WalletHistoryAdapter;
import com.edelivery.store.models.datamodel.WalletHistory;
import com.edelivery.store.models.responsemodel.WalletHistoryResponse;
import com.edelivery.store.parse.ApiClient;
import com.edelivery.store.parse.ParseContent;
import com.edelivery.store.parse.ApiInterface;
import com.edelivery.store.utils.ClickListener;
import com.edelivery.store.utils.Constant;
import com.edelivery.store.utils.RecyclerTouchListener;
import com.edelivery.store.utils.Utilities;
import com.elluminati.edelivery.store.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by elluminati on 01-Nov-17.
 */

public class WalletHistoryFragment extends Fragment {

    public static final String TAG = WalletHistoryFragment.class.getName();
    private RecyclerView rcvWalletData;
    private ArrayList<WalletHistory> walletHistory;
    private WalletTransactionActivity activity;
    private WalletHistoryAdapter walletHistoryAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (WalletTransactionActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet_history, container, false);
        rcvWalletData = (RecyclerView) view.findViewById(R.id.rcvWalletData);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initRcvWalletHistory();
        getWalletHistory();
    }

    private void getWalletHistory() {

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
        Call<WalletHistoryResponse> call = apiInterface.getWalletHistory(ApiClient
                .makeJSONRequestBody(jsonObject));
        call.enqueue(new Callback<WalletHistoryResponse>() {
            @Override
            public void onResponse(Call<WalletHistoryResponse> call,
                                   Response<WalletHistoryResponse> response) {
                Utilities.printLog("WALLET_HISTORY", ApiClient.JSONResponse(response.body()));
                Utilities.hideCustomProgressDialog();
                if (activity.parseContent.isSuccessful(response)) {
                    if (response.body().isSuccess()) {
                        walletHistory.clear();
                        walletHistory.addAll(response.body().getWalletHistory());
                        Collections.sort(walletHistory, new Comparator<WalletHistory>() {
                            @Override
                            public int compare(WalletHistory lhs, WalletHistory rhs) {
                                return compareTwoDate(lhs.getCreatedAt(), rhs.getCreatedAt());
                            }
                        });
                        walletHistoryAdapter.notifyDataSetChanged();
                    } else {
                        ParseContent.getParseContentInstance().showErrorMessage
                                (activity, response.body().getErrorCode(), true);
                    }


                }

            }

            @Override
            public void onFailure(Call<WalletHistoryResponse> call, Throwable t) {
                Utilities.handleThrowable(TAG, t);

            }
        });
    }

    private void initRcvWalletHistory() {
        walletHistory = new ArrayList<>();
        rcvWalletData.setLayoutManager(new LinearLayoutManager(activity));
        walletHistoryAdapter = new WalletHistoryAdapter(this, walletHistory);
        rcvWalletData.setAdapter(walletHistoryAdapter);
        rcvWalletData.addOnItemTouchListener(new RecyclerTouchListener(activity, rcvWalletData, new
                ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        goToWalletDetailActivity(walletHistory.get(position));
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));
    }

    private void goToWalletDetailActivity(WalletHistory walletHistory) {
        Intent intent = new Intent(activity, WalletDetailActivity.class);
        intent.putExtra(Constant.BUNDLE, walletHistory);
        startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
    private int compareTwoDate(String firstStrDate, String secondStrDate) {
        try {
            SimpleDateFormat webFormat = activity.parseContent.webFormat;
            SimpleDateFormat dateFormat = activity.parseContent.dateTimeFormat;
            String date2 = dateFormat.format(webFormat.parse(secondStrDate));
            String date1 = dateFormat.format(webFormat.parse(firstStrDate));
            return date2.compareTo(date1);
        } catch (ParseException e) {
            Utilities.handleException(WalletHistoryFragment.class.getName(), e);
        }
        return 0;
    }
}
