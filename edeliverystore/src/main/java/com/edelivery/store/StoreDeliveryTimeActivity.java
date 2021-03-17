package com.edelivery.store;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.edelivery.store.adapter.StoreDayAdapter;
import com.edelivery.store.adapter.StoreDeliveryTimeAdapter;
import com.edelivery.store.models.datamodel.DayTime;
import com.edelivery.store.models.datamodel.StoreTime;
import com.edelivery.store.models.datamodel.UpdateStoreTime;
import com.edelivery.store.models.responsemodel.IsSuccessResponse;
import com.edelivery.store.parse.ApiClient;
import com.edelivery.store.parse.ApiInterface;
import com.edelivery.store.parse.ParseContent;
import com.edelivery.store.utils.ClickListener;
import com.edelivery.store.utils.Constant;
import com.edelivery.store.utils.RecyclerTouchListener;
import com.edelivery.store.utils.Utilities;
import com.edelivery.store.widgets.CustomButton;
import com.edelivery.store.widgets.CustomInputEditText;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreDeliveryTimeActivity extends BaseActivity {
    public static final String TAG = StoreDeliveryTimeActivity.class.getName();

    public boolean isEditable;
    private RecyclerView rcvDay, rcvStoreTime;
    private StoreDayAdapter storeDayAdapter;
    private StoreDeliveryTimeAdapter storeTimeAdapter;
    private ArrayList<String> dayArrayList;
    private SwitchCompat switchStore24HrsOpen, switchStoreOpen;
    private CustomInputEditText etStartTime, etEndTime;
    private CustomButton btnAddTime;
    private ArrayList<StoreTime> storeTimeList;
    private ArrayList<DayTime> dayTimeList;
    private StoreTime storeTime;
    private DayTime dayTime;
    private LinearLayout llSelectTime;
    private Dialog dialog;
    private EditText etVerifyPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_time);
        storeTimeList = new ArrayList<>();
        getExtraData();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbar(toolbar, R.drawable.ic_back, R.color.color_app_theme);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ((TextView) findViewById(R.id.tvToolbarTitle)).setText(getString(R.string.text_store_delivery_time));
        initToolbarButton();
        rcvDay = (RecyclerView) findViewById(R.id.rcvDay);
        rcvStoreTime = (RecyclerView) findViewById(R.id.rvStoreTime);
        switchStore24HrsOpen = (SwitchCompat) findViewById(R.id.switchStore24HrsOpen);
        switchStoreOpen = (SwitchCompat) findViewById(R.id.switchStoreOpen);
        etStartTime = (CustomInputEditText) findViewById(R.id.etStartTime);
        etEndTime = (CustomInputEditText) findViewById(R.id.etEndTime);
        btnAddTime = (CustomButton) findViewById(R.id.btnAddTime);
        llSelectTime = (LinearLayout) findViewById(R.id.llSelectTime);
        switchStore24HrsOpen.setOnClickListener(this);
        switchStoreOpen.setOnClickListener(this);
        llSelectTime.setOnClickListener(this);
        btnAddTime.setOnClickListener(this);
        etStartTime.setOnClickListener(this);
        etEndTime.setOnClickListener(this);
        initDayRcv();
        setEnableView(false);


    }

    private void setEnableView(boolean isEditable) {

        if (isEditable) {
            switchStore24HrsOpen.setEnabled(true);
            if (switchStore24HrsOpen.isChecked()) {
                storeTime.setStoreOpen(true);
                switchStoreOpen.setChecked(storeTime.isStoreOpen());
                switchStoreOpen.setEnabled(false);
                etStartTime.setEnabled(false);
                etEndTime.setEnabled(false);
                btnAddTime.setEnabled(false);
                llSelectTime.setEnabled(false);
            } else {
                switchStoreOpen.setEnabled(true);
                etStartTime.setEnabled(true);
                etEndTime.setEnabled(true);
                btnAddTime.setEnabled(true);
                llSelectTime.setEnabled(true);
            }
        } else {
            switchStore24HrsOpen.setEnabled(false);
            switchStoreOpen.setEnabled(false);
            etStartTime.setEnabled(false);
            etEndTime.setEnabled(false);
            btnAddTime.setEnabled(false);
            llSelectTime.setEnabled(false);
        }


    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.switchStore24HrsOpen:
                storeTime.setStoreOpenFullTime(switchStore24HrsOpen.isChecked());
                loadStoreDayData();
                break;
            case R.id.switchStoreOpen:
                storeTime.setStoreOpen(switchStoreOpen.isChecked());
                loadStoreDayData();
                break;
            case R.id.etStartTime:
            case R.id.etEndTime:
            case R.id.llSelectTime:
                openTimePickerDialog();
                break;
            case R.id.btnAddTime:
                if (!TextUtils.isEmpty(etStartTime.getText().toString()) && !TextUtils.isEmpty
                        (etEndTime.getText().toString())) {
                    dayTimeList.add(dayTime);
                    storeTimeAdapter.setStoreTimeList(dayTimeList);
                    storeTimeAdapter.notifyDataSetChanged();
                    etEndTime.getText().clear();
                    etStartTime.getText().clear();
                } else {
                    Utilities.showToast(this, getResources().getString(R.string
                            .msg_plz_select_valid_time));
                }
                break;
            default:
                // do with default
                break;
        }
    }

    private void initDayRcv() {
        dayArrayList = new ArrayList<>();
        dayArrayList.add(getResources().getString(R.string.text_sun));
        dayArrayList.add(getResources().getString(R.string.text_mon));
        dayArrayList.add(getResources().getString(R.string.text_tue));
        dayArrayList.add(getResources().getString(R.string.text_wed));
        dayArrayList.add(getResources().getString(R.string.text_thu));
        dayArrayList.add(getResources().getString(R.string.text_fri));
        dayArrayList.add(getResources().getString(R.string.text_sat));
        storeDayAdapter = new StoreDayAdapter(this, dayArrayList);
        rcvDay.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
                false));
        rcvDay.setAdapter(storeDayAdapter);
        rcvDay.addOnItemTouchListener(new RecyclerTouchListener(this, rcvDay, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                storeDayAdapter.setSelected(position);
                storeDayAdapter.notifyDataSetChanged();
                storeTime = storeTimeList.get(position);
                loadStoreDayData();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        if (!storeTimeList.isEmpty()) {
            storeDayAdapter.setSelected(0);
            storeDayAdapter.notifyDataSetChanged();
            storeTime = storeTimeList.get(0);
            loadStoreDayData();
        }


    }

    private void openTimePickerDialog() {

        final DecimalFormat numberFormat = new DecimalFormat("00");
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,

                new TimePickerDialog.OnTimeSetListener() {

                    int count = 0;

                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int
                            selectedMinute) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            dayTime = new DayTime();
                            dayTime.setStoreOpenTime(numberFormat.format(selectedHour).concat(":")
                                    .concat(numberFormat.format(selectedMinute)));

                            if (isValidOpeningTime(dayTime)) {
                                openCloseTimePickerDialog(numberFormat.format(selectedHour),
                                        numberFormat.format(selectedMinute), dayTime);
                            } else {
                                Utilities.showToast(StoreDeliveryTimeActivity.this,
                                        getString(R.string
                                                .text_not_valid_Time));
                            }
                        } else {
                            if (count == 0) {
                                dayTime = new DayTime();
                                dayTime.setStoreOpenTime(numberFormat.format(selectedHour)
                                        .concat(":")
                                        .concat(numberFormat.format(selectedMinute)));

                                if (isValidOpeningTime(dayTime)) {
                                    openCloseTimePickerDialog(numberFormat.format(selectedHour),
                                            numberFormat.format(selectedMinute), dayTime);
                                } else {
                                    Utilities.showToast(StoreDeliveryTimeActivity.this,
                                            getString(R.string
                                                    .text_not_valid_Time));
                                }
                            }

                            count++;
                        }

                    }
                }, hour, minute, true);
        timePickerDialog.setCustomTitle(getCustomTitleView(getString(R.string.text_opening_time),
                false,
                null));

        timePickerDialog.show();
    }

    private View getCustomTitleView(String dialogTitle, boolean showPreviousTime, String
            previousTime) {
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogTitleView = inflater.inflate(R.layout.custom_time_picker_title, null);
        TextView titleView = (TextView) dialogTitleView.findViewById(R.id.tvTimePickerTitle);
        TextView openingTime = (TextView) dialogTitleView.findViewById(R.id
                .tvTimePickerOpeningTime);
        titleView.setText(dialogTitle);
        if (showPreviousTime) {
            openingTime.setVisibility(View.VISIBLE);
            openingTime.setText(getString(R.string.text_opening_time) + "  " + previousTime);
        }

        return dialogTitleView;
    }

    private void openCloseTimePickerDialog(final String openingTimeHours, final String
            openingTimeMinute, final DayTime storeTime) {

        final DecimalFormat numberFormat = new DecimalFormat("00");
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);


        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    int count = 0;

                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int
                            selectedMinute) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            storeTime.setStoreCloseTime(numberFormat.format(selectedHour).concat
                                    (":")
                                    .concat(numberFormat.format(selectedMinute)));
                            if (isValidClosingTime(storeTime)) {
                                etStartTime.setText(storeTime.getStoreOpenTime());
                                etEndTime.setText(storeTime.getStoreCloseTime());
                            } else {
                                Utilities.showToast(StoreDeliveryTimeActivity.this,
                                        getString(R.string
                                                .text_not_valid_Time));
                            }
                        } else {
                            if (count == 0) {
                                storeTime.setStoreCloseTime(numberFormat.format(selectedHour)
                                        .concat(":")
                                        .concat(numberFormat.format(selectedMinute)));
                                if (isValidClosingTime(storeTime)) {
                                    etStartTime.setText(storeTime.getStoreOpenTime());
                                    etEndTime.setText(storeTime.getStoreCloseTime());
                                } else {
                                    Utilities.showToast(StoreDeliveryTimeActivity.this,
                                            getString(R.string
                                                    .text_not_valid_Time));
                                }
                            }
                            count++;
                        }
                    }
                }, hour, minute, true);
        timePickerDialog.setCustomTitle(getCustomTitleView(getString(R.string.text_closing_time),
                true,
                openingTimeHours + ":" + openingTimeMinute));

        timePickerDialog.show();
    }

    private boolean isValidOpeningTime(DayTime storeTime) {

        if (dayTimeList.isEmpty()) {
            return true;
        } else {
            for (int i = 0; i < dayTimeList.size(); i++) {
                try {
                    String oldStoreOpenTime = dayTimeList.get(i).getStoreOpenTime();
                    Date oldOpenTime = parseContent.timeFormat2.parse
                            (oldStoreOpenTime);

                    String oldStoreCloseTime = dayTimeList.get(i).getStoreCloseTime();
                    Date oldClosedTime = parseContent.timeFormat2.parse
                            (oldStoreCloseTime);

                    String openTime = storeTime.getStoreOpenTime();
                    Date newOpenTime = parseContent.timeFormat2.parse
                            (openTime);


                    if (newOpenTime.after(oldOpenTime) && newOpenTime.before(oldClosedTime)) {
                        return false;
                    }


                } catch (ParseException e) {
                    Utilities.printLog(SettingsActivity.class.getName(), e + "");
                }

            }
            return true;
        }
    }


    private boolean isValidClosingTime(DayTime storeTime) {
        if (dayTimeList.isEmpty()) {
            try {
                String storeOpenTime = storeTime.getStoreOpenTime();
                Date selectedOpenTime = parseContent.timeFormat2.parse
                        (storeOpenTime);

                String storeCloseTime = storeTime.getStoreCloseTime();
                Date newClosedTime = parseContent.timeFormat2.parse
                        (storeCloseTime);

                return (newClosedTime.after(selectedOpenTime));


            } catch (ParseException e) {
                Utilities.handleException("time_compare", e);
            }
        } else {
            for (int i = 0; i < dayTimeList.size(); i++) {
                try {
                    String oldStoreOpenTime = dayTimeList.get(i).getStoreOpenTime();
                    Date oldOpenTime = parseContent.timeFormat2.parse
                            (oldStoreOpenTime);

                    String oldStoreCloseTime = dayTimeList.get(i).getStoreCloseTime();
                    Date oldClosedTime = parseContent.timeFormat2.parse
                            (oldStoreCloseTime);

                    String storeOpenTime = storeTime.getStoreOpenTime();
                    Date selectedOpenTime = parseContent.timeFormat2.parse
                            (storeOpenTime);


                    String storeCloseTime = storeTime.getStoreCloseTime();
                    Date newClosedTime = parseContent.timeFormat2.parse
                            (storeCloseTime);

                    if (newClosedTime.after(selectedOpenTime)) {
                        if (newClosedTime.after(oldOpenTime) && newClosedTime.before
                                (oldClosedTime)) {
                            return false;
                        } else if (selectedOpenTime.before(oldOpenTime) && newClosedTime.after
                                (oldClosedTime)) {
                            return false;
                        }
                    } else {
                        return false;
                    }


                } catch (ParseException e) {
                    Utilities.printLog(SettingsActivity.class.getName(), e + "");
                }
            }

        }
        return true;

    }

    public void deleteSpecificTime(DayTime storeTime) {
        dayTimeList.remove(storeTime);
    }


    private void getExtraData() {
        if (getIntent().getExtras() != null) {
            storeTimeList = getIntent().getExtras().getParcelableArrayList(Constant.STORE_TIME);
        }
    }

    public void loadStoreDayData() {
        dayTimeList = (ArrayList<DayTime>) storeTime.getDayTime();
        if (storeTimeAdapter == null) {
            storeTimeAdapter = new StoreDeliveryTimeAdapter(dayTimeList, this);
            rcvStoreTime.setLayoutManager(new LinearLayoutManager(this));
            rcvStoreTime.setAdapter(storeTimeAdapter);
            rcvStoreTime.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration
                    .VERTICAL));
        } else {
            storeTimeAdapter.setStoreTimeList(dayTimeList);
            storeTimeAdapter.notifyDataSetChanged();
        }
        switchStore24HrsOpen.setChecked(storeTime.isStoreOpenFullTime());
        switchStoreOpen.setChecked(storeTime.isStoreOpen());
        setEnableView(isEditable);
    }

    private void updateStoreTime(String pass, String socialId) {
        Utilities.showCustomProgressDialog(this, false);
        UpdateStoreTime updateStoreTime = new UpdateStoreTime();
        updateStoreTime.setStoreId(preferenceHelper.getStoreId());
        updateStoreTime.setServerToken(preferenceHelper.getServerToken());
        updateStoreTime.setStoreDeliveryTime(storeTimeList);
        updateStoreTime.setOldPassword(pass);
        updateStoreTime.setSocialId(socialId);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<IsSuccessResponse> responseCall = apiInterface.updateStoreTime(ApiClient
                .makeGSONRequestBody(updateStoreTime));
        responseCall.enqueue(new Callback<IsSuccessResponse>() {
            @Override
            public void onResponse(Call<IsSuccessResponse> call, Response<IsSuccessResponse>
                    response) {
                Utilities.hideCustomProgressDialog();
                if (parseContent.isSuccessful(response)) {
                    if (response.body().isSuccess()) {
                        ParseContent.getParseContentInstance().showMessage(StoreDeliveryTimeActivity
                                        .this,
                                response.body().getMessage());
                        setResult(Activity.RESULT_OK);
                        finish();
                    } else {
                        ParseContent.getParseContentInstance().showErrorMessage(StoreDeliveryTimeActivity
                                .this, response.body().getErrorCode(), true);
                    }
                }

            }

            @Override
            public void onFailure(Call<IsSuccessResponse> call, Throwable t) {
                Utilities.printLog(TAG, t + "");
            }
        });

    }

    private void showVerificationDialog() {

        if (TextUtils.isEmpty(preferenceHelper.getSocialId())) {
            if (dialog == null) {
                dialog = new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                dialog.setContentView(R.layout.dialog_account_verification);
                etVerifyPassword = (TextInputEditText) dialog.findViewById(R.id.etCurrentPassword);

                dialog.findViewById(R.id.btnPositive).setOnClickListener(new View.OnClickListener
                        () {


                    @Override
                    public void onClick(View v) {

                        if (!TextUtils.isEmpty(etVerifyPassword.getText().toString())) {
                            dialog.dismiss();
                            updateStoreTime(etVerifyPassword.getText().toString(), "");
                        } else {
                            etVerifyPassword.setError(getString(R.string.msg_empty_password));
                        }

                    }
                });
                dialog.findViewById(R.id.btnNegative).setOnClickListener(new View.OnClickListener
                        () {


                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog1) {
                        dialog = null;
                    }
                });
                WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                params.width = WindowManager.LayoutParams.MATCH_PARENT;
                dialog.show();
            }
        } else {
            updateStoreTime("", preferenceHelper.getSocialId());
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        setToolbarEditIcon(false, 0);
        setToolbarCameraIcon(false);
        return true;
    }

    private void initToolbarButton() {
        CustomTextView tvtoolbarbtn = (CustomTextView) findViewById(R.id.tvtoolbarbtn);
        tvtoolbarbtn.setOnClickListener(v -> {
            if (!isEditable) {
                isEditable = true;
                setEnableView(true);
                tvtoolbarbtn.setText(R.string.text_save);
            } else {
                showVerificationDialog();
            }
        });
        tvtoolbarbtn.setVisibility(View.VISIBLE);
        tvtoolbarbtn.setText(R.string.text_edit);
    }
}
