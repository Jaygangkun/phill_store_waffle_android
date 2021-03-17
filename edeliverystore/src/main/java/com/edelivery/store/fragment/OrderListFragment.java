package com.edelivery.store.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.edelivery.store.BaseActivity;
import com.edelivery.store.HomeActivity;
import com.edelivery.store.OrderDetailActivity;
import com.edelivery.store.UpdateOrderActivity;
import com.edelivery.store.adapter.OrderListAdapter;
import com.edelivery.store.models.datamodel.Order;
import com.edelivery.store.models.responsemodel.OrderResponse;
import com.edelivery.store.models.singleton.CurrentBooking;
import com.edelivery.store.models.singleton.UpdateOrder;
import com.edelivery.store.parse.ApiClient;
import com.edelivery.store.parse.ApiInterface;
import com.edelivery.store.utils.Constant;
import com.edelivery.store.utils.PreferenceHelper;
import com.edelivery.store.utils.Utilities;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;
import com.google.android.material.tabs.TabLayout;

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
 * OrderListFragment on 16-02-2017.
 */

public class OrderListFragment extends BaseFragment implements BaseActivity.OrderListener {

    private OrderListAdapter orderListAdapter;
    private ArrayList<Order> orderListNormal = new ArrayList<>();
    private ArrayList<Order> orderListSchedule = new ArrayList<>();
    private boolean isLoadData;
    private RecyclerView rcOrderList;
    private LinearLayout llEmpty;
    private TabLayout orderTabsLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_list_order, container, false);

        rcOrderList = (RecyclerView) view.findViewById(R.id.recyclerView);
        rcOrderList.setLayoutManager(new LinearLayoutManager(activity));
        rcOrderList.addItemDecoration(new DividerItemDecoration(activity,
                LinearLayoutManager.VERTICAL));
        orderListAdapter = new OrderListAdapter(activity, orderListNormal, true);
        rcOrderList.setAdapter(orderListAdapter);
        llEmpty = (LinearLayout) view.findViewById(R.id.ivEmpty);
        orderTabsLayout = view.findViewById(R.id.orderTabsLayout);
        CustomTextView text = (CustomTextView) view.findViewById(R.id.tvEmptyText);
        text.setText(activity.getResources().getString(R.string.text_no_order));
        swipeLayoutSetup();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.toolbar.setElevation(0);
        }
        orderTabsLayout.addTab(orderTabsLayout.newTab().setText(getResources().getString(R.string
                .text_asps)));
        orderTabsLayout.addTab(orderTabsLayout.newTab().setText(getResources().getString(R.string
                .text_schedule)));
        orderTabsLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                orderListAdapter.setOrderList(tab.getPosition() == 1 ? orderListSchedule :
                        orderListNormal);
                orderListAdapter.notifyDataSetChanged();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        activity.startSchedule();
        activity.setOrderListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        activity.stopSchedule();
        activity.setOrderListener(null);
    }

    private void swipeLayoutSetup() {
        activity.mainSwipeLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R
                .color.colorBlack));
        activity.mainSwipeLayout.setEnabled(true);
        activity.mainSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getOrderList();
            }
        });
    }

    /**
     * this method call a webservice for get order or delivery list
     */
    public void getOrderList() {

        HashMap<String, RequestBody> map = new HashMap<>();
        activity = (HomeActivity) getActivity();
        map.put(Constant.STORE_ID, ApiClient.makeTextRequestBody(
                PreferenceHelper.getPreferenceHelper
                        (getContext()).getStoreId()));
        map.put(Constant.SERVER_TOKEN, ApiClient.makeTextRequestBody(PreferenceHelper
                .getPreferenceHelper
                        (getContext()).getServerToken()));

        Call<OrderResponse> call = ApiClient.getClient().create(ApiInterface.class).getOrderList
                (map);

        call.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {


                activity.mainSwipeLayout.setRefreshing(false);
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        if (response.body().getVehicles() != null) {
                            CurrentBooking.getInstance().setVehicleDetails(response.body()
                                    .getVehicles());
                        }
                        if (response.body().getAdminVehicles() != null) {
                            CurrentBooking.getInstance().setAdminVehicleDetails(response.body()
                                    .getAdminVehicles());
                        }
                        PreferenceHelper.getPreferenceHelper(activity).putCurrency(response.body()
                                .getCurrency());
                        orderListNormal.clear();
                        orderListSchedule.clear();
                        for (Order order : response.body().getOrderList()) {
                            if (order.isIsScheduleOrder()) {
                                orderListSchedule.add(order);
                            } else {
                                orderListNormal.add(order);
                            }
                        }
                        orderListAdapter.notifyDataSetChanged();
                    } else {
                        orderListNormal.clear();
                        orderListSchedule.clear();
                        orderListAdapter.notifyDataSetChanged();
                    }
                    if (response.body().getOrderList() != null && response.body().getOrderList()
                            .isEmpty()) {
                        llEmpty.setVisibility(View.VISIBLE);
                    } else {
                        llEmpty.setVisibility(View.GONE);
                    }

                    Utilities.hideCustomProgressDialog();
                } else {
                    Utilities.showHttpErrorToast(response.code(), activity);
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                Utilities.handleThrowable(OrderListFragment.class.getSimpleName(), t);
            }
        });
    }


    public void goToOrderDetailActivity(int position) {
        Intent intent = new Intent(activity, OrderDetailActivity.class);
        intent.putExtra(Constant.ORDER_DETAIL, orderTabsLayout.getSelectedTabPosition()
                == 1 ? orderListSchedule.get(position) : orderListNormal.get(position));
        startActivity(intent);
        activity.overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    public void goToOrderUpdateActivity(int position) {
        UpdateOrder.getInstance().setOrderId(orderListNormal.get(position).getId());
        Intent intent = new Intent(activity, UpdateOrderActivity.class);
        startActivity(intent);
        activity.overridePendingTransition(R.anim.enter, R.anim.exit);
    }


    public void makePhoneCall(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone));
        startActivity(intent);
    }



    @Override
    public void onOrderReceive() {
        Utilities.showCustomProgressDialog(activity, false);
        getOrderList();
    }
}
