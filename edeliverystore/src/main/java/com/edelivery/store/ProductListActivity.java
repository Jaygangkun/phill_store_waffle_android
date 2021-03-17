package com.edelivery.store;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.edelivery.store.adapter.ProductListAdapter;
import com.edelivery.store.models.datamodel.Product;
import com.edelivery.store.models.responsemodel.ProductResponse;
import com.edelivery.store.parse.ApiClient;
import com.edelivery.store.parse.ApiInterface;
import com.edelivery.store.parse.ParseContent;
import com.edelivery.store.utils.Constant;
import com.edelivery.store.utils.PreferenceHelper;
import com.edelivery.store.utils.Utilities;
import com.elluminati.edelivery.store.R;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductListActivity extends BaseActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private ProductListAdapter productListAdapter;
    private ArrayList<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbar(toolbar, R.drawable.ic_back, R.color.color_app_theme);
        ((TextView) findViewById(R.id.tvToolbarTitle)).setText(getString(R.string.text_products));

        RecyclerView rcProductList = (RecyclerView) findViewById(R.id.recyclerView);
        rcProductList.setNestedScrollingEnabled(false);
        rcProductList.setLayoutManager(new LinearLayoutManager(this));
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        productList = new ArrayList<>();
        productListAdapter = new ProductListAdapter(this, productList);
        rcProductList.setAdapter(productListAdapter);
        swipeRefreshLayout.setRefreshing(true);
        swipeLayoutSetup();

        findViewById(R.id.floatingBtn).setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        setToolbarCameraIcon(false);
        setToolbarEditIcon(false, 0);
        return true;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        if (v.getId() == R.id.floatingBtn) {
            Intent intent = new Intent(this, AddProductActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getProductList();
    }

    private void swipeLayoutSetup() {
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorBlack));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                getProductList();
            }
        });
    }

    public void getProductList() {
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put(Constant.STORE_ID, ApiClient.makeTextRequestBody(PreferenceHelper
                .getPreferenceHelper(this).getStoreId()));
        map.put(Constant.SERVER_TOKEN, ApiClient.makeTextRequestBody(PreferenceHelper
                .getPreferenceHelper(this).getServerToken()));

        Call<ProductResponse> productsCall = ApiClient.getClient().create(ApiInterface.class)
                .getProductList
                        (map);

        productsCall.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                swipeRefreshLayout.setRefreshing(false);
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        productList.clear();
                        productList.addAll(response.body().getProducts());
                        productListAdapter.notifyDataSetChanged();
                    } else {
                        ParseContent.getParseContentInstance().showErrorMessage
                                (ProductListActivity.this, response.body().getErrorCode(), false);
                    }
                } else {
                    Utilities.showHttpErrorToast(response.code(), ProductListActivity.this);
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                Utilities.printLog("ProductList", t.getMessage());
            }
        });
    }

    public void gotoProductDetail(Context context, Intent intent, int position, View
            ivProduct, boolean isShowTransition) {
//        Bundle bundle = new Bundle();
//        bundle.putParcelable(Constant.PRODUCT_ITEM, productList.get(position));
//        intent.putExtras(bundle);
        Product product = productList.get(position);
        intent.putExtra(Constant.PRODUCT_ITEM, product);
        startActivity(intent);
    }
}
