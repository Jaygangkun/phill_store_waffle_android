package com.edelivery.store;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.edelivery.store.adapter.ProductGroupListAdapter;
import com.edelivery.store.models.datamodel.ProductGroup;
import com.edelivery.store.models.responsemodel.ProductGroupsResponse;
import com.edelivery.store.parse.ApiClient;
import com.edelivery.store.parse.ApiInterface;
import com.edelivery.store.utils.Constant;
import com.edelivery.store.utils.PreferenceHelper;
import com.edelivery.store.utils.Utilities;
import com.edelivery.store.widgets.CustomTextView;
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

public class ProductGroupsActivity extends BaseActivity {

    private RecyclerView rcvGroups;
    private ArrayList<ProductGroup> productGroups = new ArrayList<>();
    private ProductGroupListAdapter productGroupListAdapter;
    private FloatingActionButton floatingBtn;
    private boolean isSave;
    private CustomTextView tvtoolbarbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbar(toolbar, R.drawable.ic_back, R.color.color_app_theme);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ((TextView) findViewById(R.id.tvToolbarTitle)).setText(getString(R.string
                .text_group_of_category));
        tvtoolbarbtn = (CustomTextView) findViewById(R.id.tvtoolbarbtn);
        tvtoolbarbtn.setText(getString(R.string.text_delete));
        tvtoolbarbtn.setOnClickListener(this);
        rcvGroups = (RecyclerView) findViewById(R.id.rcvGroups);
        floatingBtn = findViewById(R.id.floatingBtn);
        floatingBtn.setOnClickListener(this);
        initRcvGroups();
    }


    @Override
    protected void onResume() {
        super.onResume();
        getProductGroupList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        setToolbarEditIcon(false, R.drawable.ic_delete);
        setToolbarCameraIcon(false);
        return true;
    }


    /**
     * this method call webservice for all product groups
     */
    private void getProductGroupList() {
        Utilities.showCustomProgressDialog(this, false);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.SERVER_TOKEN, PreferenceHelper.getPreferenceHelper(this)
                    .getServerToken());
            jsonObject.put(Constant.STORE_ID, PreferenceHelper.getPreferenceHelper(this)
                    .getStoreId());
        } catch (JSONException e) {
            Utilities.printLog("ProductDetail", e.getMessage());
        }

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ProductGroupsResponse> responseCall = apiInterface.getProductGroupList
                (ApiClient.makeJSONRequestBody(jsonObject));
        responseCall.enqueue(new Callback<ProductGroupsResponse>() {
            @Override
            public void onResponse(Call<ProductGroupsResponse> call,
                                   Response<ProductGroupsResponse> response) {

                if (response.isSuccessful()) {
                    Utilities.hideCustomProgressDialog();
                    productGroups.clear();
                    if (response.body().isSuccess()) {
                        Utilities.printLog("SPECIFICATION_GROUP", ApiClient.JSONResponse(response
                                .body()));
                        productGroups.addAll(response.body().getProductGroups());
                    } else {
                        parseContent.showErrorMessage(ProductGroupsActivity
                                .this, response.body().getErrorCode(), false);
                    }
                } else {
                    Utilities.showHttpErrorToast(response.code(), ProductGroupsActivity
                            .this);
                }
                if (productGroupListAdapter != null) {
                    productGroupListAdapter.notifyDataSetChanged();
                }
                if (!productGroups.isEmpty()) {
                    tvtoolbarbtn.setText(R.string.text_delete);
                    tvtoolbarbtn.setVisibility(View.VISIBLE);
                } else {
                    tvtoolbarbtn.setVisibility(View.GONE);
                }


            }

            @Override
            public void onFailure(Call<ProductGroupsResponse> call, Throwable t) {
                Utilities.printLog("ProductDetail", t.getMessage());
            }
        });
    }

    /**
     * this method call webservice for delete product group
     */
    private void deleteProductGroup(String productGroupId, final int position) {
        Utilities.showCustomProgressDialog(this, false);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.SERVER_TOKEN, PreferenceHelper.getPreferenceHelper(this)
                    .getServerToken());
            jsonObject.put(Constant.STORE_ID, PreferenceHelper.getPreferenceHelper(this)
                    .getStoreId());
            jsonObject.put(Constant.PRODUCT_GROUP_ID, productGroupId);
        } catch (JSONException e) {
            Utilities.printLog("deleteProductGroup", e.getMessage());
        }

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ProductGroupsResponse> responseCall = apiInterface.deleteProductGroup
                (ApiClient.makeJSONRequestBody(jsonObject));
        responseCall.enqueue(new Callback<ProductGroupsResponse>() {
            @Override
            public void onResponse(Call<ProductGroupsResponse> call,
                                   Response<ProductGroupsResponse> response) {

                if (response.isSuccessful()) {
                    Utilities.hideCustomProgressDialog();
                    productGroups.remove(position);
                } else {
                    Utilities.showHttpErrorToast(response.code(), ProductGroupsActivity
                            .this);
                }
                if (productGroupListAdapter != null) {
                    productGroupListAdapter.notifyDataSetChanged();
                }
                if (productGroups.isEmpty())
                    setToolbarEditIcon(false, 0);


            }

            @Override
            public void onFailure(Call<ProductGroupsResponse> call, Throwable t) {
                Utilities.printLog("ProductDetail", t.getMessage());
            }
        });
    }

    private void initRcvGroups() {
        productGroupListAdapter = new ProductGroupListAdapter(productGroups, false) {
            @Override
            public void onSelect(int position) {
                gotoAddItemActivity(productGroups.get(position));
            }

            @Override
            public void onDelete(String productGroupId, int position) {
                deleteProductGroup(productGroupId, position);
            }
        };
        rcvGroups.setLayoutManager(new LinearLayoutManager(this));
        rcvGroups.setAdapter(productGroupListAdapter);
        rcvGroups.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration
                .VERTICAL));


    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.floatingBtn:
                goToAddGroupActivity();
                break;
            case R.id.tvtoolbarbtn:
                if (productGroupListAdapter != null) {
                    productGroupListAdapter.setIsDelete(!isSave);
                    if (!isSave) {
                        isSave = true;
                        tvtoolbarbtn.setText(R.string.text_save);
                    } else {
                        isSave = false;
                        tvtoolbarbtn.setText(R.string.text_delete);
                    }
                }
                break;
        }
    }

    private void goToAddGroupActivity() {
        Intent intent = new Intent(this, AddGroupActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void gotoAddItemActivity(ProductGroup productGroup) {
        Intent intent = new Intent(this, AddGroupActivity.class);
        intent.putExtra(Constant.PRODUCT_GROUP, productGroup);
        startActivity(intent);
    }
}
