package com.edelivery.store.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;

import com.edelivery.store.adapter.OrderAnalyticAdapter;
import com.edelivery.store.adapter.OrderDayEarningAdaptor;
import com.edelivery.store.adapter.OrderEarningAdapter;
import com.edelivery.store.models.datamodel.Analytic;
import com.edelivery.store.models.datamodel.EarningData;
import com.edelivery.store.models.responsemodel.DayEarningResponse;
import com.edelivery.store.parse.ApiClient;
import com.edelivery.store.parse.ApiInterface;
import com.edelivery.store.parse.ParseContent;
import com.edelivery.store.utils.Constant;
import com.edelivery.store.utils.Utilities;
import com.edelivery.store.widgets.CustomFontTextViewTitle;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;
import com.github.mikephil.charting.charts.BarChart;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by elluminati on 27-Jun-17.
 */

public class DayEarningFragment extends BaseEarningFragment {


    public CustomTextView tvOrderDate;
    private CustomFontTextViewTitle tvOrderTotal, tvPrice,tvDayEarning;
    private RecyclerView rcvOrderEarning, rcvProviderAnalytic, rcvOrders;
    private ArrayList<ArrayList<EarningData>> arrayListForEarning;
    private ArrayList<Analytic> arrayListProviderAnalytic;
    private List<Object> orderPaymentsItemList;
    private LinearLayout llData, ivEmpty;
    private Calendar calendar;
    private int day;
    private int month;
    private int year;
    private DatePickerDialog.OnDateSetListener fromDateSet;
    private BarChart barChart;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View fragEarView = inflater.inflate(R.layout.fragment_earning, container, false);
        tvOrderDate = (CustomTextView) fragEarView.findViewById(R.id.tvOrderDate);
        tvOrderTotal = (CustomFontTextViewTitle) fragEarView.findViewById(R.id.tvOrderTotal);
        tvPrice = (CustomFontTextViewTitle) fragEarView.findViewById(R.id.tvPrice);
        rcvOrderEarning = (RecyclerView) fragEarView.findViewById(R.id.rcvOrderEarning);
        rcvProviderAnalytic = (RecyclerView) fragEarView.findViewById(R.id.rcvProviderAnalytic);
        rcvOrders = (RecyclerView) fragEarView.findViewById(R.id.rcvOrders);
        llData = (LinearLayout) fragEarView.findViewById(R.id.llData);
        ivEmpty = (LinearLayout) fragEarView.findViewById(R.id.ivEmpty);
        barChart = fragEarView.findViewById(R.id.barChart);
        tvDayEarning = fragEarView.findViewById(R.id.tvDayEarning);
        return fragEarView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        barChart.setVisibility(View.GONE);
        tvDayEarning.setVisibility(View.GONE);
        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        arrayListForEarning = new ArrayList<>();
        arrayListProviderAnalytic = new ArrayList<>();
        orderPaymentsItemList = new ArrayList<>();
        getDailyEarning(earningActivity.parseContent.dateFormat2.format(new Date()));
        tvOrderDate.setText(Utilities.getDayOfMonthSuffix(Integer.valueOf
                (earningActivity.parseContent.day.format(new Date()))) + earningActivity
                .parseContent.dateFormatMonth.format(new Date()));
        tvOrderDate.setOnClickListener(this);
        fromDateSet = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendar.clear();
                calendar.set(year, monthOfYear, dayOfMonth);
                tvOrderDate.setText(Utilities.getDayOfMonthSuffix(Integer.valueOf
                        (dayOfMonth)) + " " + earningActivity.parseContent.dateFormatMonth.format
                        (new
                                Date
                                (calendar.getTimeInMillis())));

                getDailyEarning(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            }
        };

    }


    public void getDailyEarning(final String data) {
        Utilities.showCustomProgressDialog(earningActivity, false);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.SERVER_TOKEN, earningActivity.preferenceHelper
                    .getServerToken());
            jsonObject.put(Constant.STORE_ID, earningActivity.preferenceHelper
                    .getStoreId());
            jsonObject.put(Constant.START_DATE, data);
        } catch (JSONException e) {
            Utilities.handleException(DayEarningFragment.class.getName(), e);
        }


        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<DayEarningResponse> responseCall = apiInterface.getDailyEarning(ApiClient
                .makeJSONRequestBody(jsonObject));
        responseCall.enqueue(new Callback<DayEarningResponse>() {
            @Override
            public void onResponse(Call<DayEarningResponse> call, Response<DayEarningResponse>
                    response) {
                Utilities.hideCustomProgressDialog();
                if (response.body().isSuccess()) {
                    arrayListForEarning.clear();
                    arrayListProviderAnalytic.clear();
                    orderPaymentsItemList.clear();
                    earningActivity.parseContent.parseEarning(response, arrayListForEarning,
                            arrayListProviderAnalytic, orderPaymentsItemList, false);
                    initEarningOrderRcv();
                    initAnalyticRcv();
                    initOrdersRcv();
                    tvOrderTotal.setText(response.body().getCurrency() + earningActivity
                            .parseContent
                            .decimalTwoDigitFormat
                            .format(response.body()
                                    .getOrderTotal().getTotalEarning()));
                    tvPrice.setText(earningActivity
                            .parseContent
                            .decimalTwoDigitFormat
                            .format(response.body()
                                    .getOrderTotal().getPayToStore()));
                    llData.setVisibility(View.VISIBLE);
                    ivEmpty.setVisibility(View.GONE);

                } else {
                    ParseContent.getParseContentInstance().showErrorMessage(earningActivity,
                            response.body()
                            .getErrorCode(), false);
                    llData.setVisibility(View.GONE);
                    ivEmpty.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onFailure(Call<DayEarningResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvOrderDate:
                openFromDatePicker();
                break;

            default:
                // do with default
                break;
        }
    }


    private void initEarningOrderRcv() {
        rcvOrderEarning.setLayoutManager(new LinearLayoutManager(earningActivity));
        rcvOrderEarning.setAdapter(new OrderEarningAdapter(arrayListForEarning));
        rcvOrderEarning.setNestedScrollingEnabled(false);
    }

    private void initAnalyticRcv() {
        rcvProviderAnalytic.setLayoutManager(new GridLayoutManager(earningActivity, 2,
                LinearLayoutManager.VERTICAL, false));
        rcvProviderAnalytic.addItemDecoration(new DividerItemDecoration(earningActivity,
                DividerItemDecoration.HORIZONTAL));
        rcvProviderAnalytic.setAdapter(new OrderAnalyticAdapter(arrayListProviderAnalytic));
        rcvProviderAnalytic.setNestedScrollingEnabled(false);
    }

    private void initOrdersRcv() {
        rcvOrders.setLayoutManager(new LinearLayoutManager(earningActivity));
        rcvOrders.addItemDecoration(new DividerItemDecoration(earningActivity,
                DividerItemDecoration.VERTICAL));
        rcvOrders.setAdapter(new OrderDayEarningAdaptor(earningActivity, orderPaymentsItemList));
        rcvOrders.setNestedScrollingEnabled(false);
    }

    private void openFromDatePicker() {
        DatePickerDialog fromPiker = new DatePickerDialog(earningActivity, fromDateSet, year,
                month, day);
        fromPiker.setTitle(getResources().getString(R.string.text_select_from_date));
        fromPiker.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
        fromPiker.show();
    }

}
