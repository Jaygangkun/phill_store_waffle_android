package com.edelivery.store;

import android.app.DatePickerDialog;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edelivery.store.adapter.HistoryAdapter;
import com.edelivery.store.models.datamodel.OrderData;
import com.edelivery.store.models.responsemodel.HistoryResponse;
import com.edelivery.store.parse.ApiClient;
import com.edelivery.store.parse.ApiInterface;
import com.edelivery.store.parse.ParseContent;
import com.edelivery.store.utils.Constant;
import com.edelivery.store.utils.PinnedHeaderItemDecoration;
import com.edelivery.store.utils.PreferenceHelper;
import com.edelivery.store.utils.Utilities;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryActivity extends BaseActivity {

    private ArrayList<OrderData> orderItemArrayList, sortlistItemArrayList;
    private RecyclerView recyclerView;
    private HistoryAdapter historyAdapter;
    private TreeSet<Integer> separatorSet;
    private ArrayList<Date> dateList;
    private ParseContent parseContent;
    private SwipeRefreshLayout swipeRefreshLayout;

    private Calendar calendar;
    private int day;
    private int month;
    private int year;

    private CustomTextView tvFromDate, tvToDate, tvHistoryReset, tvHistoryApply;
    private LinearLayout layoutHistoryFilter;
    private DatePickerDialog.OnDateSetListener fromDateSet, toDateSet;
    private boolean isFromDateSet, isToDateSet;
    private long fromDateSetTime, toDateSetTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbar(toolbar, R.drawable.ic_back, R.color.color_app_theme);
        ((TextView) findViewById(R.id.tvToolbarTitle)).setText(getString(R.string.text_history));
        tvFromDate = (CustomTextView) findViewById(R.id.tvFromDate);
        tvToDate = (CustomTextView) findViewById(R.id.tvToDate);
        tvFromDate.setOnClickListener(this);
        tvToDate.setOnClickListener(this);
        tvHistoryReset = (CustomTextView) findViewById(R.id.tvHistoryReset);
        tvHistoryReset.setOnClickListener(this);
        tvHistoryApply = (CustomTextView) findViewById(R.id.tvHistoryApply);
        tvHistoryApply.setOnClickListener(this);
        layoutHistoryFilter = (LinearLayout) findViewById(R.id.layoutHistoryFilter);

        parseContent = ParseContent.getParseContentInstance();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_history);
        separatorSet = new TreeSet<>();
        dateList = new ArrayList<>();
        orderItemArrayList = new ArrayList<>();
        sortlistItemArrayList = new ArrayList<>();
        getHistory("", "");
        swipeLayoutSetup();

        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        fromDateSet = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int
                    dayOfMonth) {
                calendar.clear();
                calendar.set(year, monthOfYear, dayOfMonth);
                fromDateSetTime = calendar.getTimeInMillis();
                isFromDateSet = true;
                tvFromDate.setText(parseContent.dateFormat.format(calendar.getTime
                        ()));

            }
        };

        toDateSet = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.clear();
                calendar.set(year, monthOfYear, dayOfMonth);
                toDateSetTime = calendar.getTimeInMillis();
                isToDateSet = true;
                tvToDate.setText(parseContent.dateFormat.format(calendar.getTime
                        ()));
            }
        };

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {
            case R.id.tvFromDate:
                openFromDatePicker();
                break;
            case R.id.tvToDate:
                openToDatePicker();
                break;

            case R.id.tvHistoryReset:
                clearData();
                getHistory("", "");
                break;
            case R.id.tvHistoryApply:
                if (fromDateSetTime > 0 && toDateSetTime > 0) {
                    getHistory(parseContent.dateFormat2.format(new Date(fromDateSetTime)),
                            parseContent.dateFormat2.format(new Date(toDateSetTime)));
                    layoutHistoryFilter.setVisibility(View.GONE);
                    setToolbarEditIcon(true, R.drawable.ic_filter);
                    clearData();
                } else {
                    Utilities.showToast(this,
                            getResources().getString(R.string.msg_plz_select_valid_date));
                }


                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        setToolbarCameraIcon(false);
        setToolbarEditIcon(true, R.drawable.ic_filter);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.ivEditMenu:
                updateUiFiler();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }


    }

    private void updateUiFiler() {
        if (layoutHistoryFilter.getVisibility() == View.VISIBLE) {
            layoutHistoryFilter.setVisibility(View.GONE);
            setToolbarEditIcon(true, R.drawable.ic_filter);
            clearData();
        } else {
            setToolbarEditIcon(true, R.drawable.ic_filter);
            layoutHistoryFilter.setVisibility(View.VISIBLE);
            setToolbarEditIcon(true, R.drawable.ic_cancel);
        }
    }

    private void swipeLayoutSetup() {
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorBlack));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getHistory("", "");
            }
        });

    }

    private void getHistory(String fromDate, String toDate) {
        swipeRefreshLayout.setRefreshing(true);
        HashMap<String, RequestBody> map = new HashMap<>();

        map.put(Constant.STORE_ID, ApiClient.makeTextRequestBody(
                PreferenceHelper.getPreferenceHelper(this).getStoreId
                        ()));
        map.put(Constant.SERVER_TOKEN, ApiClient.makeTextRequestBody(PreferenceHelper
                .getPreferenceHelper(this)
                .getServerToken()));
        map.put(Constant.START_DATE, ApiClient.makeTextRequestBody(fromDate));
        map.put(Constant.END_DATE, ApiClient.makeTextRequestBody(
                toDate));

        Call<HistoryResponse> call = ApiClient.getClient().create(ApiInterface.class)
                .getHistoryList(map);
        call.enqueue(new Callback<HistoryResponse>() {
            @Override
            public void onResponse(Call<HistoryResponse> call, Response<HistoryResponse> response) {
                swipeRefreshLayout.setRefreshing(false);
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        orderItemArrayList.clear();
                        orderItemArrayList.addAll(response.body().getOrderList());
                        //Utilities.printLog("history", new Gson().toJson(response.body()));
                        historyAdapter = new HistoryAdapter(HistoryActivity.this,
                                getShortHistoryList(orderItemArrayList), separatorSet,
                                HistoryActivity.this);
                        recyclerView.setAdapter(historyAdapter);
                        recyclerView.addItemDecoration(new PinnedHeaderItemDecoration());
                    } else {
                        orderItemArrayList.clear();
                        ParseContent.getParseContentInstance().showErrorMessage
                                (HistoryActivity.this,
                                        response.body().getErrorCode(), false);
                    }
                } else {
                    Utilities.showHttpErrorToast(response.code(), HistoryActivity.this);
                }
            }


            @Override
            public void onFailure(Call<HistoryResponse> call, Throwable t) {

            }
        });
    }

    private ArrayList<OrderData> getShortHistoryList(ArrayList<OrderData> listItems) {
        sortlistItemArrayList.clear();
        dateList.clear();
        separatorSet.clear();
        try {
            SimpleDateFormat sdf = parseContent.dateFormat;
            Calendar calendar = Calendar.getInstance();

            Collections.sort(listItems, new Comparator<OrderData>() {
                @Override
                public int compare(OrderData orderList, OrderData orderList2) {

                    return compareTwoDate(orderList.getCompletedAt(),
                            orderList2.getCompletedAt());
                }
            });


            HashSet<Date> listToSet = new HashSet<>();
            for (int i = 0; i < listItems.size(); i++) {

                if (listToSet.add(sdf.parse(listItems.get(i).getCompletedAt()))) {
                    dateList.add(sdf.parse(listItems.get(i).getCompletedAt()));
                }

            }
            for (int i = 0; i < dateList.size(); i++) {
                calendar.setTime(dateList.get(i));
                OrderData orderList = new OrderData();
                orderList.setCompletedAt(sdf.format(dateList.get(i)));
                sortlistItemArrayList.add(orderList);

                separatorSet.add(sortlistItemArrayList.size() - 1);
                for (int j = 0; j < listItems.size(); j++) {
                    Calendar messageTime = Calendar.getInstance();
                    messageTime.setTime(sdf.parse(listItems.get(j).getCompletedAt()));
                    if (calendar.getTime().compareTo(messageTime.getTime()) == 0) {
                        sortlistItemArrayList.add(listItems.get(j));

                    }
                }
            }
        } catch (ParseException e) {
            Utilities.handleException(Constant.Tag.HISTORYACTIVITY, e);
        }

        return sortlistItemArrayList;
    }

    private int compareTwoDate(String firstStrDate, String secondStrDate) {
        try {
            SimpleDateFormat webFormat = parseContent.webFormat;
            SimpleDateFormat dateFormat = parseContent.dateTimeFormat;
            String date2 = dateFormat.format(webFormat.parse(secondStrDate));
            String date1 = dateFormat.format(webFormat.parse(firstStrDate));
            return date2.compareTo(date1);
        } catch (ParseException e) {
            Utilities.handleException(Constant.Tag.HISTORYACTIVITY, e);
        }
        return 0;
    }

    private void openFromDatePicker() {
        DatePickerDialog fromPiker = new DatePickerDialog(this, fromDateSet, year,
                month, day);
        fromPiker.setTitle(getResources().getString(R.string.text_select_from_date));
        if (isToDateSet) {
            fromPiker.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        } else {
            fromPiker.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
        }
        fromPiker.show();
    }

    private void openToDatePicker() {
        DatePickerDialog toPiker = new DatePickerDialog(this, toDateSet, year, month,
                day);
        toPiker.setTitle(getResources().getString(R.string.text_select_to_date));
        if (isFromDateSet) {
            toPiker.getDatePicker().setMaxDate(System.currentTimeMillis());
            toPiker.getDatePicker().setMinDate(fromDateSetTime);
        } else {
            toPiker.getDatePicker().setMaxDate(System.currentTimeMillis());
        }
        toPiker.show();
    }

    private void clearData() {
        isFromDateSet = false;
        isToDateSet = false;
        tvToDate.setText(getResources().getString(R.string.text_to));
        tvFromDate.setText(getResources().getString(R.string.text_from));
        fromDateSetTime = 0;
        toDateSetTime = 0;
        calendar.setTimeInMillis(System.currentTimeMillis());
    }
}
