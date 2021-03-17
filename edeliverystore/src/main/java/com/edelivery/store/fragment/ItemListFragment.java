package com.edelivery.store.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.edelivery.store.AddGroupActivity;
import com.edelivery.store.models.singleton.SubStoreAccess;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.edelivery.store.AddItemActivity;
import com.edelivery.store.AddProductActivity;
import com.edelivery.store.adapter.ProductFilterAdapter;
import com.edelivery.store.adapter.ProductItemAdapter;
import com.edelivery.store.models.datamodel.Item;
import com.edelivery.store.models.datamodel.Product;
import com.edelivery.store.models.responsemodel.ProductResponse;
import com.edelivery.store.models.singleton.CurrentProduct;
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
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fragment to display list of item on 16-02-2017.
 */

public class ItemListFragment extends BaseFragment {

    private ArrayList<Product> productList = new ArrayList<>();
    private ProductItemAdapter productItemAdapter;
    private ProductFilterAdapter productFilterAdapter;
    private CardView cvProductFilter;
    private LinearLayout llProductFilter;
    private CustomButton btnApplyProductFilter;
    private CustomInputEditText etStoreSearch;
    private RecyclerView rcvFilterList, rcItemList;
    private ImageView ivClearText;
    private FloatingActionButton fbAdd;
    private CustomTextView btnAddProduct, btnAddItem;
    private LinearLayout llFab;
    private PinnedHeaderItemDecoration pinnedHeaderItemDecoration;
    private Animation fabOpen, fabClose, rotateForward, rotateBackward;
    private boolean isFabOpen;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_item, container, false);


        swipeLayoutSetup();
        rcItemList = (RecyclerView) view.findViewById(R.id.recyclerView);
        rcvFilterList = (RecyclerView) view.findViewById(R.id.rcvFilterList);
        etStoreSearch = (CustomInputEditText) view.findViewById(R.id.etStoreSearch);
        rcvFilterList = (RecyclerView) view.findViewById(R.id.rcvFilterList);
        cvProductFilter = (CardView) view.findViewById(R.id.cvProductFilter);
        cvProductFilter.setVisibility(View.GONE);
        llProductFilter = (LinearLayout) view.findViewById(R.id.llProductFilter);
        llProductFilter.setVisibility(View.GONE);
        btnApplyProductFilter = (CustomButton) view.findViewById(R.id.btnApplyProductFilter);
        ivClearText = (ImageView) view.findViewById(R.id
                .ivClearDeliveryAddressTextMap);
        fbAdd = view.findViewById(R.id.fbAdd);
        btnAddProduct = view.findViewById(R.id.btnAddProduct);
        btnAddItem = view.findViewById(R.id.btnAddItem);
        llFab = view.findViewById(R.id.llFab);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.toolbar.setElevation(getResources().getDimensionPixelSize(R.dimen
                    .dimen_app_toolbar_elevation));
        }
        btnApplyProductFilter.setOnClickListener(this);
        ivClearText.setOnClickListener(this);
        ivClearText.setVisibility(View.GONE);
        rcItemList.setLayoutManager(new LinearLayoutManager(activity));
        productItemAdapter = new ProductItemAdapter(activity, productList);
        rcItemList.setAdapter(productItemAdapter);
        pinnedHeaderItemDecoration = new PinnedHeaderItemDecoration();
        rcItemList.addItemDecoration(pinnedHeaderItemDecoration);
        rcvFilterList.setLayoutManager(new LinearLayoutManager(activity));
        rcvFilterList.addItemDecoration(new DividerItemDecoration(activity,
                LinearLayoutManager.VERTICAL));
        productFilterAdapter = new ProductFilterAdapter(productList);
        rcvFilterList.setAdapter(productFilterAdapter);
        etStoreSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    ivClearText.setVisibility(View.VISIBLE);
                } else {
                    ivClearText.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        fabOpen = AnimationUtils.loadAnimation(activity, R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(activity, R.anim.fab_closed);
        rotateForward = AnimationUtils.loadAnimation(activity, R.anim.rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation(activity, R.anim.rotate_backward);
        fbAdd.setOnClickListener(this);
        btnAddItem.setOnClickListener(this);
        btnAddProduct.setOnClickListener(this);
        btnAddProduct.setVisibility(View.GONE);
        btnAddItem.setVisibility(View.GONE);
        fbAdd.setVisibility(PreferenceHelper.getPreferenceHelper(activity).getIsStoreEditItem()
                ? View.VISIBLE : View.GONE);

    }

    @Override
    public void onResume() {
        super.onResume();
        Utilities.printLog("ItemListFragment", "On Resume called");
        activity.mainSwipeLayout.setRefreshing(true);
        getItemList();

    }


    private void swipeLayoutSetup() {
        activity.mainSwipeLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color
                .colorBlack));
        activity.mainSwipeLayout.setEnabled(true);
        activity.mainSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getItemList();
            }
        });
    }

    /**
     * this method call webservice for get all item in store
     */
    public void getItemList() {

        HashMap<String, RequestBody> map = new HashMap<>();
        map.put(Constant.STORE_ID, ApiClient.makeTextRequestBody(
                PreferenceHelper.getPreferenceHelper
                        (activity).getStoreId()));
        map.put(Constant.SERVER_TOKEN, ApiClient.makeTextRequestBody(PreferenceHelper
                .getPreferenceHelper
                        (activity).getServerToken()));

        Call<ProductResponse> call = ApiClient.getClient().create(ApiInterface.class).getItemList
                (map);

        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                activity.mainSwipeLayout.setRefreshing(false);
                if (isAdded()) {
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

                            pinnedHeaderItemDecoration.disableCache();
                            productItemAdapter.setProductList(productList);
                            productItemAdapter.notifyDataSetChanged();
                            productFilterAdapter.notifyDataSetChanged();
                            if (productList.isEmpty()) {
                                activity.setToolbarEditIcon(false, R.drawable.filter_store);
                            } else {
                                activity.setToolbarEditIcon(true, R.drawable.filter_store);
                            }
                            CurrentProduct.getInstance().setProductDataList(productList);
                        } else {
                            ParseContent.getParseContentInstance().showErrorMessage(activity,
                                    response.body().getErrorCode(), false);
                            activity.setToolbarEditIcon(false, R.drawable.filter_store);
                        }
                    } else {
                        Utilities.showHttpErrorToast(response.code(), activity);
                    }
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                Utilities.printLog("ItemListFragment", t.getMessage());
            }
        });
    }

    public void gotoAddItemActivity(Intent intent, Item item, String productName, String
            productId, View ivItem, boolean isShowTransition, String productImage) {
        intent.putExtra(Constant.ITEM, item);
        intent.putExtra(Constant.NAME, productName);
        intent.putExtra(Constant.PRODUCT_ID, productId);
        intent.putExtra(Constant.IMAGE_URL, productImage);

        if (isShowTransition) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat
                        .makeSceneTransitionAnimation(activity, ivItem, ivItem.getTransitionName());
                ActivityCompat.startActivity(activity, intent, optionsCompat.toBundle());
            } else {
                startActivity(intent);
            }
        } else {
            startActivity(intent);

        }
    }

    public void gotoEditProductActivity(Product product) {
        Intent intent = new Intent(activity, AddProductActivity.class);
        intent.putExtra(Constant.PRODUCT_ITEM, product);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btnApplyProductFilter:
                if (!productList.isEmpty()) {
                    pinnedHeaderItemDecoration.disableCache();
                    productItemAdapter.getFilter().filter(etStoreSearch.getText().toString().trim());
                }
                slidFilterView();
                break;
            case R.id.ivClearDeliveryAddressTextMap:
                etStoreSearch.getText().clear();
                break;
            case R.id.fbAdd:
                animateFAB();
                break;
            case R.id.btnAddProduct:
                animateFAB();
                goToAddProductActivity();
                break;
            case R.id.btnAddItem:
                animateFAB();
                goToAddItemActivity();
                break;
            default:
                // do with default
                break;
        }
    }

    public void slidFilterView() {
        if (cvProductFilter.getVisibility() == View.GONE && !isFabOpen) {
            etStoreSearch.getText().clear();
            llProductFilter.setVisibility(View.VISIBLE);
            ResizeAnimation resizeAnimation = new ResizeAnimation(cvProductFilter, getResources()
                    .getDimensionPixelSize(R.dimen.dimen_filter_height));
            resizeAnimation.setInterpolator(new LinearInterpolator());
            resizeAnimation.setDuration(300);
            cvProductFilter.startAnimation(resizeAnimation);
            cvProductFilter.setVisibility(View.VISIBLE);
            activity.floatingBtn.setOnClickListener(null);

            activity.setToolbarEditIcon(true, R.drawable.ic_cancel);
        } else {
            etStoreSearch.getText().clear();
            activity.setToolbarEditIcon(true, R.drawable.filter_store);
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
                    activity.floatingBtn.setOnClickListener(activity);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

        }

    }


    private void goToAddItemActivity() {
        Intent intent = new Intent(activity, AddItemActivity.class);
        startActivity(intent);
        activity.overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    private void goToAddProductActivity() {
        Intent intent = new Intent(activity, AddProductActivity.class);
        startActivity(intent);
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void animateFAB() {

        if (btnAddProduct.getVisibility() == View.GONE) {
            btnAddItem.setVisibility(View.VISIBLE);
            btnAddProduct.setVisibility(View.VISIBLE);
        }
        if (isFabOpen) {
            llFab.setBackground(null);
            llFab.setClickable(false);
            fbAdd.startAnimation(rotateBackward);
            btnAddItem.startAnimation(fabClose);
            btnAddProduct.startAnimation(fabClose);
            btnAddItem.setClickable(false);
            btnAddProduct.setClickable(false);
            isFabOpen = false;
        } else {
            llFab.setBackgroundColor(ResourcesCompat.getColor(activity.getResources(), R.color
                    .color_app_transparent_white, null));
            llFab.setClickable(false);
            fbAdd.startAnimation(rotateForward);
            btnAddItem.startAnimation(fabOpen);
            btnAddProduct.startAnimation(fabOpen);
            btnAddItem.setClickable(true);
            btnAddProduct.setClickable(true);
            isFabOpen = true;
        }
    }
}
