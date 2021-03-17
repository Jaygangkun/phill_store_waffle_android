package com.edelivery.store;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.edelivery.store.adapter.SubStoresAdapter;
import com.edelivery.store.models.datamodel.SubStore;
import com.edelivery.store.models.responsemodel.SubStoresResponse;
import com.edelivery.store.parse.ApiClient;
import com.edelivery.store.parse.ApiInterface;
import com.edelivery.store.utils.Constant;
import com.edelivery.store.utils.Utilities;
import com.elluminati.edelivery.store.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubStoresActivity extends BaseActivity {

    private FloatingActionButton floatingBtn;
    private SubStoresAdapter subStoresAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_stores);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbar(toolbar, R.drawable.ic_back, R.color.color_app_theme);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ((TextView) findViewById(R.id.tvToolbarTitle)).setText(getString(R.string
                .text_sub_store));
        floatingBtn = findViewById(R.id.floatingBtn);
        floatingBtn.setOnClickListener(this);
        RecyclerView rcvSubStore = findViewById(R.id.rcvSubStore);
        subStoresAdapter = new SubStoresAdapter() {
            @Override
            public void onStoreSelect(SubStore subStore) {
                goToSubStoreActivity(subStore);
            }
        };
        rcvSubStore.setAdapter(subStoresAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getSubStores();
    }

    private void getSubStores() {
        Utilities.showCustomProgressDialog(this, false);
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(Constant.SERVER_TOKEN, preferenceHelper.getServerToken());
            jsonObject.put(Constant.STORE_ID, preferenceHelper.getStoreId());
        } catch (JSONException e) {
            Utilities.handleException(PromoCodeActivity.class.getName(), e);
        }

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SubStoresResponse> responseCall = apiInterface.getSubStores(ApiClient
                .makeJSONRequestBody(jsonObject));
        responseCall.enqueue(new Callback<SubStoresResponse>() {
            @Override
            public void onResponse(Call<SubStoresResponse> call, Response<SubStoresResponse>
                    response) {
                Utilities.hideCustomProgressDialog();
                if (parseContent.isSuccessful(response)) {
                    if (response.body().isSuccess()) {
                        subStoresAdapter.setSubStoreList(response.body().getSubStoreList());
                    }
                }
            }

            @Override
            public void onFailure(Call<SubStoresResponse> call, Throwable t) {
                Utilities.hideCustomProgressDialog();
                Utilities.handleThrowable(SubStoresActivity.class.getName(), t);
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.floatingBtn) {
            goToSubStoreActivity(null);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        setToolbarEditIcon(false, R.drawable.ic_history_time);
        setToolbarCameraIcon(false);
        return true;
    }

    private void goToSubStoreActivity(SubStore subStore) {
        Intent intent = new Intent(this, AddSubStoreActivity.class);
        if (subStore != null) {
            intent.putExtra(Constant.SUB_STORE, (Parcelable) subStore);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}