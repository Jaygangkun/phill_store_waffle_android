package com.edelivery.store;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edelivery.store.adapter.OrderUpdateAdapter;
import com.edelivery.store.models.datamodel.Item;
import com.edelivery.store.models.datamodel.ItemSpecification;
import com.edelivery.store.models.datamodel.OrderDetails;
import com.edelivery.store.models.datamodel.ProductSpecification;
import com.edelivery.store.models.responsemodel.IsSuccessResponse;
import com.edelivery.store.models.responsemodel.ItemsResponse;
import com.edelivery.store.models.responsemodel.OrderDetailResponse;
import com.edelivery.store.models.singleton.UpdateOrder;
import com.edelivery.store.parse.ApiClient;
import com.edelivery.store.parse.ApiInterface;
import com.edelivery.store.parse.ParseContent;
import com.edelivery.store.utils.Constant;
import com.edelivery.store.utils.Utilities;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateOrderActivity extends BaseActivity {

    public static final String TAG = UpdateOrderActivity.class.getName();
    private RecyclerView rcvOrder;
    private CustomTextView tvOrderTotal;
    private LinearLayout btnCheckOut;
    private OrderUpdateAdapter orderUpdateAdapter;
    private LinearLayout ivEmpty;
    private CustomTextView tvtoolbarbtn;
    private ItemsResponse itemsResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_order);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbar(toolbar, R.drawable.ic_back, R.color.color_app_theme);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ((TextView) findViewById(R.id.tvToolbarTitle)).setText(getString(R.string
                .text_update_order));
        tvtoolbarbtn = (CustomTextView) findViewById(R.id.tvtoolbarbtn);
        tvtoolbarbtn.setText(getString(R.string.text_add_new_item));
        tvtoolbarbtn.setVisibility(View.VISIBLE);
        tvtoolbarbtn.setOnClickListener(this);

        rcvOrder = (RecyclerView) findViewById(R.id.rcvUpdateOrder);
        tvOrderTotal = (CustomTextView) findViewById(R.id.tvOrderTotal);
        btnCheckOut = (LinearLayout) findViewById(R.id.btnCheckOut);
        ivEmpty = (LinearLayout) findViewById(R.id.ivEmpty);
        btnCheckOut.setOnClickListener(this);
        initRcvOrder();
        getOrderDetail();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (orderUpdateAdapter != null) {
            modifyTotalAmount();
            orderUpdateAdapter.notifyDataSetChanged();
        }
        updateUiOrderItemList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu1) {
        super.onCreateOptionsMenu(menu1);
        menu = menu1;
        setToolbarEditIcon(false, R.drawable.filter_store);
        setToolbarCameraIcon(false);
        return true;
    }


    @Override
    public void onClick(View view) {
        // do something
        switch (view.getId()) {
            case R.id.btnCheckOut:
                updateOrder();
                break;
            case R.id.tvtoolbarbtn:
                goToStoreOrderProductActivity();
                break;
            default:
                // do with default
                break;
        }
    }

    private void initRcvOrder() {
        orderUpdateAdapter = new OrderUpdateAdapter(this, UpdateOrder.getInstance()
                .getOrderDetails());
        rcvOrder.setLayoutManager(new LinearLayoutManager(this));
        rcvOrder.setAdapter(orderUpdateAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    /**
     * this method id used to increase particular item quantity in cart
     *
     * @param item
     */
    public void increaseItemQuantity(Item item) {
        int quantity = item.getQuantity();
        quantity++;
        item.setQuantity(quantity);
        item.setTotalItemAndSpecificationPrice((item
                .getTotalSpecificationPrice()
                + item.getItemPrice()) * quantity);
        item.setTotalItemTax(item.getTotalTax() * quantity);
        orderUpdateAdapter.notifyDataSetChanged();
        modifyTotalAmount();
    }

    /**
     * this method id used to decrease particular item quantity in cart
     *
     * @param item
     */
    public void decreaseItemQuantity(Item item) {
        int quantity = item.getQuantity();
        if (quantity > 1) {
            quantity--;
            item.setQuantity(quantity);
            item.setTotalItemAndSpecificationPrice((item
                    .getTotalSpecificationPrice()
                    + item.getItemPrice()) * quantity);
            item.setTotalItemTax(item.getTotalTax() * quantity);
            orderUpdateAdapter.notifyDataSetChanged();
            modifyTotalAmount();
        }
    }

    /**
     * this method id used to remove particular item quantity in cart
     *
     * @param position
     * @param relativePosition
     */
    public void removeItem(int position, int relativePosition) {
        UpdateOrder.getInstance().getOrderDetails().get(position).getItems().remove
                (relativePosition);
        if (UpdateOrder.getInstance().getOrderDetails().get(position).getItems().isEmpty()) {
            UpdateOrder.getInstance().getOrderDetails().remove(position);
        }
        updateUiOrderItemList();
        orderUpdateAdapter.notifyDataSetChanged();
        modifyTotalAmount();
    }


    /**
     * this method id used to modify or change  total cart item  amount
     */
    public void modifyTotalAmount() {
        double totalAmount = 0;
        for (OrderDetails orderDetails : UpdateOrder.getInstance().getOrderDetails()) {
            for (Item item : orderDetails.getItems()) {
                totalAmount = totalAmount + item.getTotalItemAndSpecificationPrice();
            }

        }
        tvOrderTotal.setText(preferenceHelper.getCurrency() + parseContent
                .decimalTwoDigitFormat
                .format(totalAmount));
    }


    /**
     * this method called webservice for add product in cart
     */
    private void updateOrder() {

        Utilities.showCustomProgressDialog(this, false);
        UpdateOrder.getInstance().setServerToken(preferenceHelper.getServerToken());
        UpdateOrder.getInstance().setStoreId(preferenceHelper.getStoreId());
        double totalCartPrice = 0, totalCartTaxPrice = 0;
        int totalItemCount = 0, totalSpecificationCount = 0;
        for (OrderDetails orderDetails : UpdateOrder.getInstance().getOrderDetails()) {
            double totalItemPrice = 0, totalTaxPrice = 0;
            for (Item cartProductItems : orderDetails.getItems()) {
                totalItemCount = totalItemCount + cartProductItems.getQuantity();

                for (ItemSpecification specifications : cartProductItems.getSpecifications()) {
                    totalSpecificationCount = totalSpecificationCount + specifications.getList()
                            .size();
                }
                totalItemPrice = totalItemPrice + cartProductItems
                        .getTotalItemAndSpecificationPrice();
                totalTaxPrice = totalTaxPrice + cartProductItems.getTotalItemTax();

            }
            orderDetails.setTotalProductItemPrice(totalItemPrice);
            orderDetails.setTotalItemTax(totalTaxPrice);
            totalCartPrice = totalCartPrice + totalItemPrice;
            totalCartTaxPrice = totalCartTaxPrice + totalTaxPrice;
        }
        UpdateOrder.getInstance().setCartOrderTotalPrice(totalCartPrice);
        UpdateOrder.getInstance().setCartOrderTotalTaxPrice(totalCartTaxPrice);
        UpdateOrder.getInstance().setTotalItemCount(totalItemCount);
        UpdateOrder.getInstance().setTotalSpecificationCount(totalSpecificationCount);
        Utilities.printLog("UPDATE_ORDER", ApiClient.JSONResponse(UpdateOrder.getInstance()));

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<IsSuccessResponse> responseCall = apiInterface.updateOrder(ApiClient
                .makeGSONRequestBody(UpdateOrder.getInstance()));
        responseCall.enqueue(new Callback<IsSuccessResponse>() {
            @Override
            public void onResponse(Call<IsSuccessResponse> call, Response<IsSuccessResponse>
                    response) {
                if (parseContent.isSuccessful(response)) {
                    Utilities.hideCustomProgressDialog();
                    if (response.body().isSuccess()) {
                        ParseContent.getParseContentInstance().showMessage(
                                UpdateOrderActivity.this, response
                                        .body()
                                        .getMessage());
                        onBackPressed();
                    } else {
                        ParseContent.getParseContentInstance().showErrorMessage(
                                UpdateOrderActivity.this, response
                                        .body()
                                        .getErrorCode(), false);
                    }


                }
            }

            @Override
            public void onFailure(Call<IsSuccessResponse> call, Throwable t) {
                Utilities.handleThrowable(TAG, t);
            }
        });
    }


    private void getOrderDetail() {
        Utilities.showCustomProgressDialog(this, false);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.STORE_ID, preferenceHelper
                    .getStoreId());
            jsonObject.put(Constant.SERVER_TOKEN, preferenceHelper
                    .getServerToken());
            jsonObject.put(Constant.ORDER_ID, UpdateOrder.getInstance().getOrderId());
        } catch (JSONException e) {
            Utilities.handleException(TAG, e);
        }

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<OrderDetailResponse> responseCall = apiInterface.getOrderDetail(ApiClient
                .makeJSONRequestBody(jsonObject));
        responseCall.enqueue(new Callback<OrderDetailResponse>() {
            @Override
            public void onResponse(Call<OrderDetailResponse> call,
                                   Response<OrderDetailResponse> response) {
                Utilities.hideCustomProgressDialog();
                if (parseContent.isSuccessful(response)) {
                    if (response.body().isSuccess()) {
                        UpdateOrder.getInstance().setOrderDetails(response.body().getOrderDetail().getCartDetail().getOrderDetails());
                        getStoreProductItems();

                    } else {
                        parseContent.showErrorMessage(UpdateOrderActivity.this, response.body()
                                .getErrorCode(), false);
                    }
                }


            }

            @Override
            public void onFailure(Call<OrderDetailResponse> call, Throwable t) {
                Utilities.handleThrowable(TAG, t);
            }
        });


    }


    private void getStoreProductItems() {
        Utilities.showCustomProgressDialog(this, false);
        ArrayList<String> itemIds = new ArrayList<>();
        for (OrderDetails orderDetails : UpdateOrder.getInstance().getOrderDetails()) {
            for (Item item : orderDetails.getItems()) {
                itemIds.add(item.getItemId());
            }
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.TYPE, Constant.Type.STORE);
            jsonObject.put(Constant.ID, preferenceHelper.getStoreId());
            jsonObject.put(Constant.SERVER_TOKEN, preferenceHelper.getServerToken());
            jsonObject.put(Constant.ITEM_ARRAY, new JSONArray(String.valueOf(new Gson().toJsonTree
                    (itemIds).getAsJsonArray())));
        } catch (JSONException e) {
            Utilities.handleException(TAG, e);
        }

        Utilities.printLog("GET_ITEMS", jsonObject.toString());
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ItemsResponse> call = apiInterface.getItems(ApiClient.makeJSONRequestBody
                (jsonObject));
        call.enqueue(new Callback<ItemsResponse>() {
            @Override
            public void onResponse(Call<ItemsResponse> call, Response<ItemsResponse> response) {
                Utilities.hideCustomProgressDialog();
                if (parseContent.isSuccessful(response)) {
                    if (response.body().isSuccess()) {
                        itemsResponse = response.body();
                        updateUiOrderItemList();
                        orderUpdateAdapter.notifyDataSetChanged();
                    } else {
                        ParseContent.getParseContentInstance().showErrorMessage(
                                UpdateOrderActivity.this, response
                                        .body()
                                        .getErrorCode(), false);
                    }

                }

            }

            @Override
            public void onFailure(Call<ItemsResponse> call, Throwable t) {
                Utilities.handleThrowable(TAG, t);
            }
        });
    }

    private Item matchSelectedItem(Item item) {
        itemsResponse.getItems().addAll(UpdateOrder.getInstance().getSaveNewItem());
        for (Item responseItem : itemsResponse.getItems()) {
            if (responseItem.getUniqueId() == item.getUniqueId()) {
                item.setTax(responseItem.getTax());
                for (ItemSpecification itemResSpecification : responseItem.getSpecifications()) {
                    for (ItemSpecification specification : item.getSpecifications()) {
                        if (itemResSpecification.getUniqueId() == specification.getUniqueId()) {
                            for (ProductSpecification productResSpecification : itemResSpecification
                                    .getList()) {
                                productResSpecification.setIsDefaultSelected(false);
                                for (ProductSpecification productSpecification : specification
                                        .getList()) {
                                    if (productResSpecification.getUnique_id() ==
                                            productSpecification.getUnique_id()) {
                                        productResSpecification.setIsDefaultSelected(true);
                                    }

                                }
                            }
                        }

                    }
                }
                UpdateOrder.getInstance().clearSaveNewItem();
                return responseItem;
            }
        }
        UpdateOrder.getInstance().clearSaveNewItem();
        return null;
    }


    public void goToUpdateOrderProductSpecificationActivity(int section, int position) {
        Intent intent = new Intent(this, UpdateOrderProductSpecificationActivity.class);
        intent.putExtra(Constant.UPDATE_ITEM_INDEX, position);
        intent.putExtra(Constant.UPDATE_ITEM_INDEX_SECTION, section);
        intent.putExtra(Constant.ITEM, matchSelectedItem(UpdateOrder.getInstance().getOrderDetails
                ().get(section).getItems().get(position)));
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }

    private void goToStoreOrderProductActivity() {
        Intent intent = new Intent(this, StoreOrderProductActivity.class);
        intent.putExtra(Constant.IS_ORDER_UPDATE, true);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void updateUiOrderItemList() {
        if (UpdateOrder.getInstance().getOrderDetails().isEmpty()) {
            ivEmpty.setVisibility(View.VISIBLE);
            rcvOrder.setVisibility(View.GONE);
        } else {
            ivEmpty.setVisibility(View.GONE);
            rcvOrder.setVisibility(View.VISIBLE);
        }

    }

}
