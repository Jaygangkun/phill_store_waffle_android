package com.edelivery.store;

import android.os.Build;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.widget.TextView;

import com.edelivery.store.adapter.ViewPagerAdapter;
import com.edelivery.store.fragment.CartHistoryFragment;
import com.edelivery.store.fragment.HistoryDetailFragment;
import com.edelivery.store.fragment.HistoryInvoiceFragment;
import com.edelivery.store.models.responsemodel.HistoryDetailsResponse;
import com.edelivery.store.parse.ApiClient;
import com.edelivery.store.parse.ParseContent;
import com.edelivery.store.parse.ApiInterface;
import com.edelivery.store.utils.Constant;
import com.edelivery.store.utils.PreferenceHelper;
import com.edelivery.store.utils.Utilities;
import com.elluminati.edelivery.store.R;

import java.util.Calendar;
import java.util.HashMap;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryDetailActivity extends BaseActivity {

    private static String TAG = "HISTORY_DETAILS_ACTIVITY";
    public ViewPagerAdapter adapter;
    public HistoryDetailsResponse detailsResponse;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_detail);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(getResources().getDimension(R.dimen
                    .dimen_app_tab_elevation));
        }
        setToolbar(toolbar, R.drawable.ic_back, R.color.color_app_theme);

        tabLayout = (TabLayout) findViewById(R.id.historyTabsLayout);
        viewPager = (ViewPager) findViewById(R.id.historyViewpager);
        getDataFromIntent();
    }

    private void initTabLayout(ViewPager viewPager) {
        if (adapter == null) {
            adapter = new ViewPagerAdapter(getSupportFragmentManager());
            adapter.addFragment(new HistoryDetailFragment(), getString(R.string
                    .text_other_detail));
            adapter.addFragment(new HistoryInvoiceFragment(), getString(R.string.text_invoice));
            adapter.addFragment(new CartHistoryFragment(), getString(R.string.text_cart));
            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        setToolbarCameraIcon(false);
        setToolbarEditIcon(false, 0);
        return true;
    }

    private void getDataFromIntent() {
        if (getIntent() != null && getIntent().getSerializableExtra(Constant.ORDER_ID) != null) {
            Utilities.printLog(TAG, "order id - " + getIntent().getStringExtra(Constant.ORDER_ID));
            getHistoryDetails(getIntent().getStringExtra(Constant.ORDER_ID));
        }

    }


    /**
     * this method call a webservice for get order history detail
     *
     * @param orderId in string
     */
    private void getHistoryDetails(String orderId) {

        Utilities.showCustomProgressDialog(this, false);
        HashMap<String, RequestBody> map = new HashMap<>();

        map.put(Constant.STORE_ID, ApiClient.makeTextRequestBody(
                PreferenceHelper.getPreferenceHelper(this).getStoreId
                        ()));
        map.put(Constant.SERVER_TOKEN, ApiClient.makeTextRequestBody(PreferenceHelper
                .getPreferenceHelper(this)
                .getServerToken()));
        map.put(Constant.ORDER_ID, ApiClient.makeTextRequestBody(
                orderId));

        Call<HistoryDetailsResponse> call = ApiClient.getClient().create(ApiInterface.class)
                .getHistoryDetails(map);
        call.enqueue(new Callback<HistoryDetailsResponse>() {
            @Override
            public void onResponse(Call<HistoryDetailsResponse> call,
                                   Response<HistoryDetailsResponse> response) {

                if (response.isSuccessful()) {
                    Utilities.hideCustomProgressDialog();
                    if (response.body().isSuccess()) {
                        detailsResponse = response.body();
                        Utilities.printLog(TAG, ApiClient.JSONResponse(detailsResponse));
                        initTabLayout(viewPager);
                        ((TextView) findViewById(R.id.tvToolbarTitle)).setText(getResources()
                                .getString(R.string.text_order_no) + detailsResponse.getOrder()
                                .getUniqueId());

                    } else {

                        ParseContent.getParseContentInstance().showErrorMessage
                                (HistoryDetailActivity.this,
                                        response.body().getErrorCode(), false);

                    }

                } else {
                    Utilities.showHttpErrorToast(response.code(), HistoryDetailActivity.this);
                }
            }

            @Override
            public void onFailure(Call<HistoryDetailsResponse> call, Throwable t) {
                Utilities.printLog(TAG, "Faileed response" + t.getMessage());
            }
        });
    }


    private String getYesterdayDateString() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return parseContent.dateFormat.format(cal.getTime());
    }
}
