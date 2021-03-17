package com.edelivery.store.component;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.edelivery.store.adapter.NearestProviderAdapter;
import com.edelivery.store.models.datamodel.ProviderDetail;
import com.edelivery.store.models.responsemodel.NearestProviderResponse;
import com.edelivery.store.parse.ApiClient;
import com.edelivery.store.parse.ApiInterface;
import com.edelivery.store.parse.ParseContent;
import com.edelivery.store.utils.Constant;
import com.edelivery.store.utils.PreferenceHelper;
import com.edelivery.store.utils.Utilities;
import com.elluminati.edelivery.store.R;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ravi Bhalodi on 03,July,2020 in Elluminati
 */
public class NearestProviderDialog extends Dialog {
    private NearestProviderAdapter nearestProviderAdapter;

    public NearestProviderDialog(@NonNull Context context, String orderId, String vehicleId) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_nearest_provider);
        nearestProviderAdapter = new NearestProviderAdapter();
        RecyclerView rcvProvider = findViewById(R.id.rcvProvider);
        rcvProvider.setAdapter(nearestProviderAdapter);
        EditText searchProvider = findViewById(R.id.etSearchProvider);
        searchProvider.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nearestProviderAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(params);
        getNearestProvider(context, orderId, vehicleId);
    }

    private void getNearestProvider(final Context context, String orderId, String vehicleId) {
        Utilities.showCustomProgressDialog(context, false);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.STORE_ID, PreferenceHelper.getPreferenceHelper(context)
                    .getStoreId());
            jsonObject.put(Constant.SERVER_TOKEN, PreferenceHelper.getPreferenceHelper(context)
                    .getServerToken());
            jsonObject.put(Constant.ORDER_ID, orderId);
            jsonObject.put(Constant.VEHICLE_ID, vehicleId);
        } catch (JSONException e) {
            Utilities.handleException(NearestProviderDialog.class.getSimpleName(), e);
        }

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<NearestProviderResponse> responseCall = apiInterface.getNearestProviders(ApiClient
                .makeJSONRequestBody(jsonObject));
        responseCall.enqueue(new Callback<NearestProviderResponse>() {
            @Override
            public void onResponse(Call<NearestProviderResponse> call,
                                   Response<NearestProviderResponse> response) {
                Utilities.hideCustomProgressDialog();
                if (ParseContent.getParseContentInstance().isSuccessful(response)) {
                    if (response.body().isSuccess()) {
                        nearestProviderAdapter.setProviderDetails(response.body().getProviders());
                    } else {
                        ParseContent.getParseContentInstance().showErrorMessage(context,
                                response.body()
                                        .getErrorCode(), false);
                    }
                }

            }

            @Override
            public void onFailure(Call<NearestProviderResponse> call, Throwable t) {
                Utilities.handleThrowable(NearestProviderDialog.class.getSimpleName(), t);
            }
        });


    }

    public ProviderDetail getSelectedProvider() {
        return nearestProviderAdapter.getSelectedProvider();
    }

}
