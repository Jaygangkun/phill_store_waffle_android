package com.edelivery.store.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.edelivery.store.BaseActivity;
import com.edelivery.store.DeliveryDetailActivity;
import com.edelivery.store.adapter.OrderListAdapter;
import com.edelivery.store.models.datamodel.Order;
import com.edelivery.store.models.responsemodel.OrderResponse;
import com.edelivery.store.models.responsemodel.OrderStatusResponse;
import com.edelivery.store.models.singleton.CurrentBooking;
import com.edelivery.store.parse.ApiClient;
import com.edelivery.store.parse.ApiInterface;
import com.edelivery.store.parse.ParseContent;
import com.edelivery.store.utils.Constant;
import com.edelivery.store.utils.PreferenceHelper;
import com.edelivery.store.utils.RecyclerOnItemListener;
import com.edelivery.store.utils.Utilities;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * -
 */

public class DeliveriesListFragment extends BaseFragment implements RecyclerOnItemListener
        .OnItemClickListener, BaseActivity.OrderListener {

    private OrderListAdapter orderListAdapter;
    private ArrayList<Order> deliveryList = new ArrayList<>();
    private String TAG = "DeliveriesListFragment";
    private LinearLayout llEmpty;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.toolbar.setElevation(getResources().getDimensionPixelSize(R.dimen
                    .dimen_app_toolbar_elevation));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_deliverys, container, false);

        RecyclerView rcOrderList = (RecyclerView) view.findViewById(R.id.recyclerView);
        rcOrderList.setLayoutManager(new LinearLayoutManager(activity));
        rcOrderList.addOnItemTouchListener(new RecyclerOnItemListener(activity, this));
        rcOrderList.addItemDecoration(new DividerItemDecoration(activity,
                LinearLayoutManager.VERTICAL));
        orderListAdapter = new OrderListAdapter(activity, deliveryList, false);
        rcOrderList.setAdapter(orderListAdapter);
        llEmpty = (LinearLayout) view.findViewById(R.id.ivEmpty);
        CustomTextView text = (CustomTextView) view.findViewById(R.id.tvEmptyText);
        text.setText(activity.getResources().getString(R.string.text_no_delivery));
        swipeLayoutSetup();
        return view;
    }

    private void swipeLayoutSetup() {
        activity.mainSwipeLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color
                .colorBlack));
        activity.mainSwipeLayout.setEnabled(true);
        activity.mainSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDeliveryList();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        activity.mainSwipeLayout.setRefreshing(true);
        getDeliveryList();

    }

    @Override
    public void onStart() {
        super.onStart();
        activity.setOrderListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        activity.setOrderListener(null);
    }

    /**
     * this method call a webservice for get order or delivery list
     */
    public void getDeliveryList() {

        HashMap<String, RequestBody> map = new HashMap<>();
        map.put(Constant.STORE_ID, ApiClient.makeTextRequestBody(
                PreferenceHelper.getPreferenceHelper(activity).getStoreId()));
        map.put(Constant.SERVER_TOKEN, ApiClient.makeTextRequestBody(PreferenceHelper
                .getPreferenceHelper(activity).getServerToken
                        ()));

        Call<OrderResponse> call = ApiClient.getClient().create(ApiInterface.class)
                .getDeliveryList(map);

        call.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                activity.mainSwipeLayout.setRefreshing(false);
                Utilities.printLog(TAG, new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        deliveryList.clear();
                        deliveryList.addAll(response.body().getDeliveryList());
                        if (response.body().getVehicles() != null) {
                            CurrentBooking.getInstance().setVehicleDetails(response.body()
                                    .getVehicles());
                        }
                        if (response.body().getAdminVehicles() != null) {
                            CurrentBooking.getInstance().setAdminVehicleDetails(response.body()
                                    .getAdminVehicles());
                        }
                        orderListAdapter.notifyDataSetChanged();

                    } else {
                        deliveryList.clear();
                        orderListAdapter.notifyDataSetChanged();

                    }
                    if (response.body().getDeliveryList() != null && response.body()
                            .getDeliveryList().isEmpty()) {
                        llEmpty.setVisibility(View.VISIBLE);
                    } else {
                        llEmpty.setVisibility(View.GONE);
                    }
                } else {
                    Utilities.showHttpErrorToast(response.code(), activity);
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                Utilities.printLog(TAG, t.getMessage());
            }
        });
    }


    public void gotoDeliveryDetails(int position) {


        Intent intent = new Intent(activity, DeliveryDetailActivity.class);
        intent.putExtra(Constant.ORDER_DETAIL, deliveryList.get(position));
        startActivity(intent);
        activity.overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    /**
     * this method call webservice for create order pickup request to delivery man
     *
     * @param orderId
     * @param vehicleId
     */
    public void assignDeliveryMan(String orderId, String vehicleId,String providerId) {
        Utilities.showProgressDialog(activity);
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put(Constant.STORE_ID, ApiClient.makeTextRequestBody(
                PreferenceHelper.getPreferenceHelper(activity).getStoreId()));
        map.put(Constant.SERVER_TOKEN, ApiClient.makeTextRequestBody(PreferenceHelper
                .getPreferenceHelper(activity).getServerToken
                        ()));
        map.put(Constant.ORDER_ID, ApiClient.makeTextRequestBody(
                orderId));
        map.put(Constant.VEHICLE_ID, ApiClient.makeTextRequestBody(vehicleId));
        if (!TextUtils.isEmpty(providerId)) {
            map.put(Constant.PROVIDER_ID, ApiClient.makeTextRequestBody(providerId));
        }
        Call<OrderStatusResponse> call = ApiClient.getClient().create(ApiInterface.class)
                .assignProvider(map);

        call.enqueue(new Callback<OrderStatusResponse>() {
            @Override
            public void onResponse(Call<OrderStatusResponse> call, Response<OrderStatusResponse>
                    response) {
                Utilities.removeProgressDialog();
                Utilities.printLog("assignDeliveryMan", "response - " + new Gson().toJson
                        (response.body()));
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        getDeliveryList();
                    } else {
                        ParseContent.getParseContentInstance().showErrorMessage(activity,
                                response.body().getErrorCode(), true);
                    }
                } else {
                    Utilities.showHttpErrorToast(response.code(), activity);
                }
            }

            @Override
            public void onFailure(Call<OrderStatusResponse> call, Throwable t) {
                Utilities.printLog(TAG, t.getMessage());
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    public void makePhoneCall(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone));
        startActivity(intent);
    }


    @Override
    public void onOrderReceive() {
        activity.mainSwipeLayout.setRefreshing(true);
        getDeliveryList();
    }
}
