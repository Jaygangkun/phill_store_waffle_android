package com.edelivery.store;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edelivery.store.adapter.ProductFilterAdapter;
import com.edelivery.store.adapter.StoreProductAdapter;
import com.edelivery.store.models.datamodel.CartProducts;
import com.edelivery.store.models.datamodel.Item;
import com.edelivery.store.models.datamodel.Product;
import com.edelivery.store.models.responsemodel.ProductResponse;
import com.edelivery.store.models.singleton.CurrentBooking;
import com.edelivery.store.parse.ApiClient;
import com.edelivery.store.parse.ApiInterface;
import com.edelivery.store.parse.ParseContent;
import com.edelivery.store.utils.Constant;
import com.edelivery.store.utils.PinnedHeaderItemDecoration;
import com.edelivery.store.utils.PreferenceHelper;
import com.edelivery.store.utils.ResizeAnimation;
import com.edelivery.store.utils.Utilities;
import com.edelivery.store.widgets.CustomButton;
import com.edelivery.store.widgets.CustomInputEditText;
import com.elluminati.edelivery.store.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.elluminati.edelivery.store.R.id.rcvStoreProduct;

public class StoreOrderProductActivity extends BaseActivity {
    private ArrayList<Product> productList;
    private CardView cvProductFilter;
    private ImageView ivClearDeliveryAddressTextMap;
    private LinearLayout llProductFilter;
    private CustomButton btnApplyProductFilter, btnGotoCart;
    private CustomInputEditText etProductSearch;
    private RecyclerView rcvFilterList, rcItemList;
    private StoreProductAdapter storeProductAdapter;
    private ProductFilterAdapter productFilterAdapter;
    private PinnedHeaderItemDecoration pinnedHeaderItemDecoration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_product);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbar(toolbar, R.drawable.ic_back, R.color.color_app_theme);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utilities.hideSoftKeyboard(StoreOrderProductActivity.this);
                onBackPressed();
            }
        });
        ((TextView) findViewById(R.id.tvToolbarTitle)).setText(getString(R.string
                .text_products));
        productList = new ArrayList<>();
        rcItemList = (RecyclerView) findViewById(rcvStoreProduct);
        rcvFilterList = (RecyclerView) findViewById(R.id.rcvFilterList);
        etProductSearch = (CustomInputEditText) findViewById(R.id.etProductSearch);
        rcvFilterList = (RecyclerView) findViewById(R.id.rcvFilterList);
        cvProductFilter = (CardView) findViewById(R.id.cvProductFilter);
        cvProductFilter.setVisibility(View.GONE);
        llProductFilter = (LinearLayout) findViewById(R.id.llProductFilter);
        llProductFilter.setVisibility(View.GONE);
        btnApplyProductFilter = (CustomButton) findViewById(R.id.btnApplyProductFilter);
        ivClearDeliveryAddressTextMap = (ImageView) findViewById(R.id
                .ivClearDeliveryAddressTextMap);
        btnGotoCart = (CustomButton) findViewById(R.id.btnGotoCart);
        btnApplyProductFilter.setOnClickListener(this);
        ivClearDeliveryAddressTextMap.setOnClickListener(this);
        btnGotoCart.setOnClickListener(this);
        getItemList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setCartItem();
    }

    private void setCartItem() {
        int cartCount = 0;
        for (CartProducts cartProducts : CurrentBooking.getInstance().getCartProductList()) {
            cartCount = cartCount + cartProducts.getItems().size();
        }
        if (cartCount > 0) {
            btnGotoCart.setVisibility(View.VISIBLE);
        } else {
            btnGotoCart.setVisibility(View.GONE);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu1) {
        super.onCreateOptionsMenu(menu1);
        menu = menu1;
        setToolbarEditIcon(true, R.drawable.filter_store);
        setToolbarCameraIcon(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.ivEditMenu) {
            slidFilterView();
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btnApplyProductFilter:
                if (productList != null && !productList.isEmpty()) {
                    pinnedHeaderItemDecoration.disableCache();
                    storeProductAdapter.getFilter().filter(etProductSearch.getText().toString().trim());
                }
                slidFilterView();
                break;
            case R.id.ivClearDeliveryAddressTextMap:
                etProductSearch.getText().clear();
                break;
            case R.id.btnGotoCart:
                goToCartActivity();
                break;
            default:
                // do with default
                break;
        }
    }

    protected void goToCartActivity() {
        Intent intent = new Intent(this, CartActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    /**
     * this method call webservice for get all item in store
     */
    public void getItemList() {

        HashMap<String, RequestBody> map = new HashMap<>();
        map.put(Constant.STORE_ID, ApiClient.makeTextRequestBody(
                PreferenceHelper.getPreferenceHelper
                        (this).getStoreId()));
        map.put(Constant.SERVER_TOKEN, ApiClient.makeTextRequestBody(PreferenceHelper
                .getPreferenceHelper
                        (this).getServerToken()));

        Call<ProductResponse> call = ApiClient.getClient().create(ApiInterface.class).getItemList
                (map);

        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        productList.clear();
                        productList.addAll(response.body().getProducts());
                        for (Product product : productList) {
                            Collections.sort(product.getItems());
                        }
                        Collections.sort(productList);
                        Utilities.printLog("ItemListFragment", "ItemListFragment -- Data " +
                                new Gson().toJson(response.body().getProducts()));

                        initRevStoreProductList();
                    } else {
                        ParseContent.getParseContentInstance().showErrorMessage
                                (StoreOrderProductActivity.this,
                                        response.body().getErrorCode(), false);
                    }
                } else {
                    Utilities.showHttpErrorToast(response.code(), StoreOrderProductActivity.this);
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                Utilities.printLog("ItemListFragment", t.getMessage());
            }
        });
    }

    private void initRevStoreProductList() {
        if (storeProductAdapter != null) {
            storeProductAdapter.notifyDataSetChanged();
            productFilterAdapter.notifyDataSetChanged();
        } else {
            storeProductAdapter = new StoreProductAdapter(this, productList);
            rcItemList.setAdapter(storeProductAdapter);
            rcItemList.setLayoutManager(new LinearLayoutManager(this));
            pinnedHeaderItemDecoration = new PinnedHeaderItemDecoration();
            rcItemList.addItemDecoration(pinnedHeaderItemDecoration);


            rcvFilterList.setLayoutManager(new LinearLayoutManager(this));
            rcvFilterList.addItemDecoration(new DividerItemDecoration(this,
                    LinearLayoutManager.VERTICAL));
            productFilterAdapter = new ProductFilterAdapter(productList);
            rcvFilterList.setAdapter(productFilterAdapter);

        }
    }

    public void slidFilterView() {
        if (cvProductFilter.getVisibility() == View.GONE) {
            etProductSearch.getText().clear();
            llProductFilter.setVisibility(View.VISIBLE);
            ResizeAnimation resizeAnimation = new ResizeAnimation(cvProductFilter, getResources()
                    .getDimensionPixelSize(R.dimen.dimen_filter_height));
            resizeAnimation.setInterpolator(new LinearInterpolator());
            resizeAnimation.setDuration(300);
            cvProductFilter.startAnimation(resizeAnimation);
            cvProductFilter.setVisibility(View.VISIBLE);
            setToolbarEditIcon(true, R.drawable.ic_cancel);
        } else {
            etProductSearch.getText().clear();
            setToolbarEditIcon(true, R.drawable.filter_store);
            ResizeAnimation resizeAnimation = new ResizeAnimation(cvProductFilter, 1);
            resizeAnimation.setInterpolator(new LinearInterpolator());
            resizeAnimation.setDuration(300);
            cvProductFilter.startAnimation(resizeAnimation);
            cvProductFilter.getAnimation().setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    cvProductFilter.setVisibility(View.GONE);
                    llProductFilter.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void goToSpecificationActivity(Product product,
                                          Item productItemsItem) {
        Intent intent;
        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().getBoolean(Constant.IS_ORDER_UPDATE)) {
                intent = new Intent(this, UpdateOrderProductSpecificationActivity.class);
                intent.putExtra(Constant.UPDATE_ITEM_INDEX, -1);
                intent.putExtra(Constant.UPDATE_ITEM_INDEX_SECTION, -1);
            } else {
                intent = new Intent(this, StoreOrderProductSpecificationActivity.class);
            }
            intent.putExtra(Constant.PRODUCT_DETAIL, product);
            intent.putExtra(Constant.PRODUCT_SPECIFICATION, productItemsItem);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }


    }

}
