package com.edelivery.store.fragment;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.edelivery.store.adapter.CalenderWeekAdaptor;
import com.edelivery.store.adapter.OrderAnalyticAdapter;
import com.edelivery.store.adapter.OrderEarningAdapter;
import com.edelivery.store.models.datamodel.Analytic;
import com.edelivery.store.models.datamodel.EarningData;
import com.edelivery.store.models.datamodel.WeekData;
import com.edelivery.store.models.responsemodel.DayEarningResponse;
import com.edelivery.store.parse.ApiClient;
import com.edelivery.store.parse.ApiInterface;
import com.edelivery.store.parse.ParseContent;
import com.edelivery.store.utils.CalenderHelper;
import com.edelivery.store.utils.ClickListener;
import com.edelivery.store.utils.Constant;
import com.edelivery.store.utils.RecyclerTouchListener;
import com.edelivery.store.utils.Utilities;
import com.edelivery.store.widgets.CustomFontTextViewTitle;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by elluminati on 27-Jun-17.
 */

public class WeekEarningFragment extends BaseEarningFragment {


    private CustomTextView tvOrderDate;
    private CustomFontTextViewTitle tvOrderTotal, tvPrice, tvOrderTitle, tvDayEarning;
    private RecyclerView rcvOrderEarning, rcvProviderAnalytic;
    private ArrayList<ArrayList<EarningData>> arrayListForEarning;
    private ArrayList<Analytic> arrayListProviderAnalytic;
    private List<Object> orderPaymentsItemList;
    private LinearLayout llData, ivEmpty;
    private CalenderHelper calenderHelper;
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
        tvOrderTitle = (CustomFontTextViewTitle) fragEarView.findViewById(R.id.tvOrdersTitle);
        fragEarView.findViewById(R.id.llOrderPayment).setVisibility(View.GONE);
        llData = (LinearLayout) fragEarView.findViewById(R.id.llData);
        ivEmpty = (LinearLayout) fragEarView.findViewById(R.id.ivEmpty);
        barChart = fragEarView.findViewById(R.id.barChart);
        tvDayEarning = fragEarView.findViewById(R.id.tvDayEarning);
        return fragEarView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        barChart.setVisibility(View.VISIBLE);
        tvDayEarning.setVisibility(View.VISIBLE);
        calenderHelper = new CalenderHelper();
        tvOrderTitle.setText(earningActivity.getResources().getString(R.string.text_daily_earning));
        tvOrderTitle.setVisibility(View.GONE);
        arrayListForEarning = new ArrayList<>();
        arrayListProviderAnalytic = new ArrayList<>();
        orderPaymentsItemList = new ArrayList<>();
        tvOrderDate.setOnClickListener(this);
        tvOrderDate.setText(earningActivity.parseContent.dateFormat.format(new Date()));
        initChart();
        ArrayList<Date> dateArrayList = calenderHelper.getCurrentWeekDates();
        getWeeklyEarning(dateArrayList.get(0), dateArrayList.get(1));
    }


    private void getWeeklyEarning(Date start, Date end) {
        String date1 = earningActivity.parseContent.dateFormat3.format(start);
        String date2 = earningActivity.parseContent.dateFormat3.format(end);
        tvOrderDate.setText(date1 + " " + "-" + " " + date2);
        Utilities.showCustomProgressDialog(earningActivity, false);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.SERVER_TOKEN, earningActivity.preferenceHelper
                    .getServerToken());
            jsonObject.put(Constant.STORE_ID, earningActivity.preferenceHelper
                    .getStoreId());
            jsonObject.put(Constant.START_DATE, earningActivity.parseContent
                    .dateFormat2.format(start));
            jsonObject.put(Constant.END_DATE, earningActivity.parseContent
                    .dateFormat2.format(end));
        } catch (JSONException e) {
            Utilities.handleException(WeekEarningFragment.class.getName(), e);
        }


        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<DayEarningResponse> responseCall = apiInterface.getWeeklyEarning(ApiClient
                .makeJSONRequestBody(jsonObject));
        responseCall.enqueue(new Callback<DayEarningResponse>() {
            @Override
            public void onResponse(Call<DayEarningResponse> call, Response<DayEarningResponse>
                    response) {
                Utilities.hideCustomProgressDialog();
                if (response.body().isSuccess()) {
                    Utilities.printLog("WEEK_EARNING", ApiClient.JSONResponse(response.body()));
                    arrayListForEarning.clear();
                    arrayListProviderAnalytic.clear();
                    orderPaymentsItemList.clear();
                    earningActivity.parseContent.parseEarning(response, arrayListForEarning,
                            arrayListProviderAnalytic, orderPaymentsItemList, true);
                    initEarningOrderRcv();
                    initAnalyticRcv();
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
                    setBarChartData(orderPaymentsItemList);

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
                Utilities.printLog(WeekEarningFragment.class.getName(), t.getMessage());
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvOrderDate:
                openWeekCalender();
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


    private void selectDayDate(String date) {
        DayEarningFragment dayEarningFragment = (DayEarningFragment) earningActivity.adapter
                .getItem(0);
        try {
            Date orderDate = earningActivity.parseContent.webFormat.parse(date);
            Date currentDate = new Date();
            if (orderDate.before(currentDate)) {
                dayEarningFragment.getDailyEarning(earningActivity.parseContent.dateFormat2.format
                        (orderDate));
                String dateString = Utilities.getDayOfMonthSuffix(Integer.valueOf
                        (earningActivity.parseContent.day
                                .format(orderDate))) + " " + earningActivity.parseContent
                        .dateFormatMonth
                        .format
                                (orderDate);
                dayEarningFragment.tvOrderDate.setText(dateString);
                earningActivity.earningViewpager.setCurrentItem(0);
            }
        } catch (ParseException e) {
            Utilities.handleException(WeekEarningFragment.class.getName(), e);
        }

    }

    private void openWeekCalender() {
        final CustomTextView tvYear;
        final CalenderWeekAdaptor calenderWeekAdaptor;
        final ArrayList<WeekData> weekDatas = new ArrayList<>();
        final Calendar calendar = Calendar.getInstance();
        final Calendar calendar1 = Calendar.getInstance();
        weekDatas.addAll(calenderHelper.getCurrentYearCalender(calendar.get(Calendar.YEAR)));
        RecyclerView rcvCalender;
        final Dialog dialog = new Dialog(earningActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_calender_weekly);
        rcvCalender = (RecyclerView) dialog.findViewById(R.id.rcvCalender);
        rcvCalender.setLayoutManager(new LinearLayoutManager(earningActivity));
//        rcvCalender.addItemDecoration(new DividerItemDecoration(earningActivity,
//                DividerItemDecoration.VERTICAL));
        calenderWeekAdaptor = new CalenderWeekAdaptor(earningActivity, weekDatas);
        rcvCalender.setAdapter(calenderWeekAdaptor);
        rcvCalender.addOnItemTouchListener(new RecyclerTouchListener(earningActivity, rcvCalender,
                new ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        calenderWeekAdaptor.toggleSelection(position);


                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));
        tvYear = (CustomTextView) dialog.findViewById(R.id.tvYear);
        tvYear.setText(String.valueOf(calendar.get(Calendar.YEAR)));
        dialog.findViewById(R.id.ivMax).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year = calendar.get(Calendar.YEAR) + 1;
                if (year <= calendar1.get(Calendar.YEAR)) {
                    calendar.set(Calendar.YEAR, year);
                    tvYear.setText(String.valueOf(calendar.get(Calendar.YEAR)));
                    weekDatas.clear();
                    weekDatas.addAll(calenderHelper.getCurrentYearCalender(year));
                    calenderWeekAdaptor.notifyDataSetChanged();
                }


            }
        });
        dialog.findViewById(R.id.ivMin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year = calendar.get(Calendar.YEAR) - 1;
                if (year > calendar1.get(Calendar.YEAR) - 3) {
                    calendar.set(Calendar.YEAR, year);
                    tvYear.setText(String.valueOf(calendar.get(Calendar.YEAR)));
                    weekDatas.clear();
                    weekDatas.addAll(calenderHelper.getCurrentYearCalender(year));
                    calenderWeekAdaptor.notifyDataSetChanged();
                }
            }
        });
        dialog.findViewById(R.id.btnPositive).setOnClickListener(new View.OnClickListener
                () {
            @Override
            public void onClick(View view) {

                if (calenderWeekAdaptor.getDate() != null) {

                    ArrayList<Date> dates = calenderWeekAdaptor.getDate().getParticularDate();
                    getWeeklyEarning(dates.get(0), dates.get(1));
                    dialog.dismiss();
                } else {
                    Utilities.showToast(earningActivity, earningActivity.getResources().getString(R
                            .string.msg_plz_select_date));
                }

            }
        });
        dialog.findViewById(R.id.btnNegative).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.setCancelable(false);
        dialog.show();
    }


    private void initChart() {
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        barChart.setMaxVisibleValueCount(60);
        // scaling can now only be done on x- and y-axis separately
        barChart.setPinchZoom(false);
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.setScaleEnabled(false);
        barChart.setDrawGridBackground(false);
        // barChart.setDrawYLabels(false);
        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {


                Analytic analytic = (Analytic) orderPaymentsItemList.get(Math
                        .round(e.getX()));

                selectDayDate(analytic.getTitle());

            }

            @Override
            public void onNothingSelected() {

            }
        });

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(new CustomAxisValueFormatter(barChart));


        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setLabelCount(8, false);
        leftAxis.setDrawGridLines(false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setEnabled(false);
        rightAxis.setDrawGridLines(false);
        rightAxis.setLabelCount(8, false);
        rightAxis.setSpaceTop(15f);


        Legend l = barChart.getLegend();
        l.setEnabled(false);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);


    }

    private void setChartWithNegativeValue(boolean isHaveNegativeValue) {
        if (isHaveNegativeValue) {
            barChart.getAxisRight().resetAxisMinimum();
            barChart.getAxisLeft().resetAxisMinimum();
        } else {
            barChart.getAxisRight().setAxisMinimum(0f);
            barChart.getAxisLeft().setAxisMinimum(0f);
        }
    }

    private void setBarChartData(List<Object> orderPaymentsItemList) {


        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        boolean isHaveNegativeValue = false;
        for (int i = 0; i < orderPaymentsItemList.size(); i++) {
            Analytic analytic = (Analytic) orderPaymentsItemList.get(i);
            float value = Float.parseFloat(analytic.getValue());
            if (!isHaveNegativeValue && value < 0) {
                isHaveNegativeValue = true;
            }
            yVals1.add(new BarEntry(i, value));
        }

        BarDataSet set1;
        setChartWithNegativeValue(isHaveNegativeValue);
        if (barChart.getData() != null &&
                barChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) barChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            barChart.getData().notifyDataChanged();
            barChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "");

            set1.setDrawIcons(false);
            set1.setValueFormatter(new MyValueFormatter());
            set1.setHighLightAlpha(0);
            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(0.9f);

            barChart.setData(data);

        }
        barChart.invalidate();
        barChart.animateY(2000);
    }

    private String getDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(" d MMM", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat webFormat = new SimpleDateFormat(Constant.DATE_TIME_FORMAT_WEB, Locale.US);
        webFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            Date date1 = webFormat.parse(date);
            return dateFormat.format(date1);
        } catch (ParseException e) {
            Utilities.handleException(WeekEarningFragment.class.getSimpleName(), e);
        }
        return "";
    }

    private class CustomAxisValueFormatter implements IAxisValueFormatter {

        private BarLineChartBase<?> chart;

        public CustomAxisValueFormatter(BarLineChartBase<?> chart) {
            this.chart = chart;
        }


        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            Utilities.printLog("VALUE", value + "");
            if (Math.round(value) > orderPaymentsItemList.size() - 1) {
                Analytic analytic = (Analytic) orderPaymentsItemList.get(Math.round(value) - (
                        (Math.round(value) - orderPaymentsItemList
                                .size() - 1)));
                return getDate(analytic.getTitle());
            } else {
                Analytic analytic = (Analytic) orderPaymentsItemList.get(Math.round(value));
                return getDate(analytic.getTitle());
            }
        }

    }

    public class MyValueFormatter implements IValueFormatter {


        public MyValueFormatter() {

        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex,
                                        ViewPortHandler viewPortHandler) {
            return earningActivity.parseContent.decimalTwoDigitFormat.format(value);
        }
    }
}
