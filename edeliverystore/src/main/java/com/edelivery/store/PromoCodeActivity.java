package com.edelivery.store;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.edelivery.store.adapter.PromoCodeAdapter;
import com.edelivery.store.models.datamodel.PromoCodes;
import com.edelivery.store.models.responsemodel.IsSuccessResponse;
import com.edelivery.store.models.responsemodel.PromoCodeResponse;
import com.edelivery.store.parse.ApiClient;
import com.edelivery.store.parse.ApiInterface;
import com.edelivery.store.parse.ParseContent;
import com.edelivery.store.utils.Constant;
import com.edelivery.store.utils.Utilities;
import com.elluminati.edelivery.store.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PromoCodeActivity extends BaseActivity {
    private RecyclerView rcvPromoCode;
    private PromoCodeAdapter promoCodeAdapter;
    private ArrayList<PromoCodes> promoCodes;
    private FloatingActionButton floatingBtnAddPromo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo_code);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbar(toolbar, R.drawable.ic_back, R.color.color_app_theme);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ((TextView) findViewById(R.id.tvToolbarTitle)).setText(getString(R.string
                .text_promo_detail));
        promoCodes = new ArrayList<>();
        rcvPromoCode = (RecyclerView) findViewById(R.id.rcvPromoCode);
        floatingBtnAddPromo = (FloatingActionButton) findViewById(R.id
                .floatingBtnAddPromo);
        floatingBtnAddPromo.setOnClickListener(this);
        initRcvPromo();
        getPromoCodeDetail();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        setToolbarCameraIcon(false);
        setToolbarEditIcon(false,0);
        return true;
    }

    private void initRcvPromo() {
        rcvPromoCode.setLayoutManager(new LinearLayoutManager(this));
        promoCodeAdapter = new PromoCodeAdapter(this, promoCodes);
        rcvPromoCode.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration
                .VERTICAL));
        rcvPromoCode.setAdapter(promoCodeAdapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.floatingBtnAddPromo:
                goToAddPromoActivity(null);
                break;

            default:
                // do with default
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Constant.REQUEST_PROMO_CODE:
                    getPromoCodeDetail();
                    break;

                default:
                    // do with default
                    break;
            }

        }

    }

    private void getPromoCodeDetail() {
        Utilities.showCustomProgressDialog(this, false);
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(Constant.SERVER_TOKEN, preferenceHelper.getServerToken());
            jsonObject.put(Constant.STORE_ID, preferenceHelper.getStoreId());
        } catch (JSONException e) {
            Utilities.handleException(PromoCodeActivity.class.getName(), e);
        }

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<PromoCodeResponse> responseCall = apiInterface.getPromoCodes(ApiClient
                .makeJSONRequestBody(jsonObject));
        responseCall.enqueue(new Callback<PromoCodeResponse>() {
            @Override
            public void onResponse(Call<PromoCodeResponse> call, Response<PromoCodeResponse>
                    response) {
                Utilities.hideCustomProgressDialog();
                if (parseContent.isSuccessful(response)) {
                    if (response.body().isSuccess()) {
                        promoCodes.clear();
                        promoCodes.addAll(response.body().getPromoCodes());
                        promoCodeAdapter.notifyDataSetChanged();

                    }
                }
            }

            @Override
            public void onFailure(Call<PromoCodeResponse> call, Throwable t) {
                Utilities.handleThrowable(PromoCodeActivity.class.getName(), t);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void goToAddPromoActivity(PromoCodes promoCodes) {
        Intent intent = new Intent(this, AddPromoCodeActivity.class);
        intent.putExtra(Constant.PROMO_DETAIL, promoCodes);
        startActivityForResult(intent, Constant.REQUEST_PROMO_CODE);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void updatePromoCode(final PromoCodes promoCodes) {
        Utilities.showCustomProgressDialog(this, false);
        PromoCodes promoCodes2 = new PromoCodes();
        promoCodes2.setIsPromoForDeliveryService(false);
        promoCodes2.setServerToken(preferenceHelper.getServerToken());
        promoCodes2.setStoreId(preferenceHelper.getStoreId());
        promoCodes2.setPromoStartDate(promoCodes.getPromoStartDate());
        promoCodes2.setPromoCodeName(null);
        promoCodes2.setPromoDetails(promoCodes.getPromoDetails());
        promoCodes2.setPromoCodeType(promoCodes.getPromoCodeType());
        promoCodes2.setPromoCodeValue(promoCodes.getPromoCodeValue());
        promoCodes2.setIsActive(promoCodes.isIsActive());
        promoCodes2.setIsPromoHaveMinimumAmountLimit(promoCodes
                .isIsPromoHaveMinimumAmountLimit());
        promoCodes2.setIsPromoRequiredUses(promoCodes.isIsPromoRequiredUses());
        promoCodes2.setIsPromoHaveMaxDiscountLimit(promoCodes
                .isIsPromoHaveMaxDiscountLimit());
        promoCodes2.setPromoExpireDate(promoCodes.getPromoExpireDate());
        promoCodes2.setPromoCodeApplyOnMinimumAmount(promoCodes
                .getPromoCodeApplyOnMinimumAmount());
        promoCodes2.setPromoCodeUses(promoCodes.getPromoCodeUses());
        promoCodes2.setPromoCodeMaxDiscountAmount(promoCodes
                .getPromoCodeMaxDiscountAmount());
        promoCodes2.setPromoId(promoCodes.getId());
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<IsSuccessResponse> responseCall;
        responseCall = apiInterface.updatePromoCodes(ApiClient
                .makeGSONRequestBody(promoCodes2));
        responseCall.enqueue(new Callback<IsSuccessResponse>() {
            @Override
            public void onResponse(Call<IsSuccessResponse> call, Response<IsSuccessResponse>
                    response) {
                if (parseContent.isSuccessful(response)) {
                    Utilities.hideCustomProgressDialog();
                    if (response.body().isSuccess()) {
                        promoCodeAdapter.notifyDataSetChanged();
                    } else {
                        ParseContent.getParseContentInstance().showErrorMessage
                                (PromoCodeActivity.this, response.body().getErrorCode(), true);
                        promoCodes.setIsActive(!promoCodes.isIsActive());
                        promoCodeAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<IsSuccessResponse> call, Throwable t) {
                Utilities.handleThrowable(PromoCodes.class.getName(), t);
            }
        });
    }
}
