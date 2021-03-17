package com.edelivery.store;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.edelivery.store.adapter.PromoForAdapter;
import com.edelivery.store.adapter.PromoRecursionAdapter;
import com.edelivery.store.component.TagView;
import com.edelivery.store.models.datamodel.Product;
import com.edelivery.store.models.datamodel.PromoCodes;
import com.edelivery.store.models.datamodel.PromoRecursionData;
import com.edelivery.store.models.responsemodel.IsSuccessResponse;
import com.edelivery.store.models.responsemodel.ProductResponse;
import com.edelivery.store.parse.ApiClient;
import com.edelivery.store.parse.ApiInterface;
import com.edelivery.store.parse.ParseContent;
import com.edelivery.store.utils.ClickListener;
import com.edelivery.store.utils.Constant;
import com.edelivery.store.utils.RecyclerTouchListener;
import com.edelivery.store.utils.Utilities;
import com.edelivery.store.widgets.CustomInputEditText;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPromoCodeActivity extends BaseActivity implements CompoundButton
        .OnCheckedChangeListener {

    public static final String TAG = AddPromoCodeActivity.class.getName();
    private LinearLayout llPromoMinimumAmountLimit, llPromoMaxDiscountLimit,
            llPromoRequiredUses, llRecursion, llPromoDate, llPromoTime, llDay, llWeek, llMonth,
            llPromoCompletedOrder, llPromoItemCountLimit, llPromoMaxDiscountMain;
    private SwitchCompat switchPromoMaxDiscountLimit, switchPromoRequiredUses,
            switchPromoMinimumAmountLimit, switchPromoActive, switchPromoRecursion,
            switchCompletedOrder, switchItemCountLimit;
    private CustomInputEditText etPromoCode, etPromoDetail, etPromoStartDate, etPromoExpDate,
            etPromoMinimumAmountLimit, etPromoMaxDiscountLimit, etPromoRequiredUses,
            etPromoAmount, etPromoItemCountLimit, etPromoOnCompletedOrder, etPromoStartTime,
            etPromoEndTime;
    private Spinner spinnerPromoType, spinnerPromoFor, spinnerPromoRecursionType;
    private int promoType, promoForValue, promoRecursionType;

    private DatePickerDialog.OnDateSetListener startDateSet, expDateSet;
    private Calendar calendar, startTimeCalender, endTimeCalender;
    private int day;
    private int month;
    private int year;
    private long promoStartDate = 0, promoExpDate = 0;
    private PromoCodes promoCodes;
    private ArrayList<Product> productList = new ArrayList<>();
    private PromoForAdapter promoForAdapter;
    private RecyclerView rcvPromoForList;
    private ArrayList<String> promoForeList;
    private TagView tagGroupDay, tagGroupWeek, tagGroupMonth;
    private ArrayList<PromoRecursionData> daysList, weekList, monthList;
    private ArrayList<String> selectedDaysList, selectedWeekList, selectedMonthList;
    private CustomTextView tvSelectRecursionDay, tvSelectRecursionWeek, tvSelectRecursionMonth;
    private ImageView ivAddDays, ivAddWeek, ivAddMonth;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_promo_code);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbar(toolbar, R.drawable.ic_back, R.color.color_app_theme);
        ((TextView) findViewById(R.id.tvToolbarTitle)).setText(getString(R.string.text_promo));
        initToolbarButton();
        llPromoMaxDiscountMain = (LinearLayout) findViewById(R.id.llPromoMaxDiscountMain);
        llPromoMinimumAmountLimit = (LinearLayout) findViewById(R.id.llPromoMinimumAmountLimit);
        llPromoMaxDiscountLimit = (LinearLayout) findViewById(R.id.llPromoMaxDiscountLimit);
        llPromoRequiredUses = (LinearLayout) findViewById(R.id.llPromoRequiredUses);
        llRecursion = (LinearLayout) findViewById(R.id.llRecursion);
        llPromoDate = (LinearLayout) findViewById(R.id.llPromoDate);
        llPromoTime = (LinearLayout) findViewById(R.id.llPromoTime);
        llDay = (LinearLayout) findViewById(R.id.llDay);
        llWeek = (LinearLayout) findViewById(R.id.llWeek);
        llMonth = (LinearLayout) findViewById(R.id.llMonth);
        llPromoCompletedOrder = (LinearLayout) findViewById(R.id.llPromoCompletedOrder);
        llPromoItemCountLimit = (LinearLayout) findViewById(R.id.llPromoItemCountLimit);
        switchPromoMaxDiscountLimit = (SwitchCompat) findViewById(R.id.switchPromoMaxDiscountLimit);
        switchPromoMaxDiscountLimit.setOnCheckedChangeListener(this);
        switchPromoRequiredUses = (SwitchCompat) findViewById(R.id.switchPromoRequiredUses);
        switchPromoRequiredUses.setOnCheckedChangeListener(this);
        switchPromoMinimumAmountLimit = (SwitchCompat) findViewById(R.id
                .switchPromoMinimumAmountLimit);
        switchPromoMinimumAmountLimit.setOnCheckedChangeListener(this);
        switchPromoRecursion = (SwitchCompat) findViewById(R.id.switchPromoRecursion);
        switchPromoRecursion.setOnCheckedChangeListener(this);
        switchCompletedOrder = (SwitchCompat) findViewById(R.id.switchCompletedOrder);
        switchCompletedOrder.setOnCheckedChangeListener(this);
        switchItemCountLimit = (SwitchCompat) findViewById(R.id.switchItemCountLimit);
        switchItemCountLimit.setOnCheckedChangeListener(this);

        etPromoDetail = (CustomInputEditText) findViewById(R.id.etPromoDetail);
        etPromoStartDate = (CustomInputEditText) findViewById(R.id.etPromoStartDate);
        etPromoExpDate = (CustomInputEditText) findViewById(R.id.etPromoExpDate);
        spinnerPromoType = (Spinner) findViewById(R.id.spinnerPromoType);
        spinnerPromoFor = (Spinner) findViewById(R.id.spinnerPromoFor);
        spinnerPromoRecursionType = (Spinner) findViewById(R.id.spinnerPromoRecursionType);
        etPromoCode = (CustomInputEditText) findViewById(R.id.etPromoCode);
        etPromoMinimumAmountLimit = (CustomInputEditText) findViewById(R.id
                .etPromoMinimumAmountLimit);
        etPromoMaxDiscountLimit = (CustomInputEditText) findViewById(R.id.etPromoMaxDiscountLimit);
        etPromoRequiredUses = (CustomInputEditText) findViewById(R.id.etPromoRequiredUses);
        etPromoAmount = (CustomInputEditText) findViewById(R.id.etPromoAmount);
        etPromoItemCountLimit = (CustomInputEditText) findViewById(R.id.etPromoItemCountLimit);
        etPromoOnCompletedOrder = (CustomInputEditText) findViewById(R.id.etPromoOnCompletedOrder);
        etPromoExpDate.setOnClickListener(this);
        etPromoStartDate.setOnClickListener(this);
        etPromoStartTime = (CustomInputEditText) findViewById(R.id.etPromoStartTime);
        etPromoEndTime = (CustomInputEditText) findViewById(R.id.etPromoEndTime);
        etPromoEndTime.setOnClickListener(this);
        etPromoStartTime.setOnClickListener(this);
        switchPromoActive = (SwitchCompat) findViewById(R.id.switchPromoActive);

        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);


        startDateSet = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendar.clear();
                calendar.set(year, monthOfYear, dayOfMonth);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                promoStartDate = calendar.getTimeInMillis();
                etPromoStartDate.setText(parseContent.dateFormat.format(calendar.getTime()));

            }
        };
        expDateSet = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                calendar.clear();
                calendar.set(year, monthOfYear, dayOfMonth);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                calendar.set(Calendar.SECOND, 59);
                promoExpDate = calendar.getTimeInMillis();
                etPromoExpDate.setText(parseContent.dateFormat.format(calendar.getTime()));
            }
        };


        rcvPromoForList = (RecyclerView) findViewById(R.id.rcvPromoForList);
        rcvPromoForList.setLayoutManager(new LinearLayoutManager(this));
        rcvPromoForList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration
                .VERTICAL));
        tagGroupDay = (TagView) findViewById(R.id.tagGroupDay);
        tagGroupWeek = (TagView) findViewById(R.id.tagGroupWeek);
        tagGroupMonth = (TagView) findViewById(R.id.tagGroupMonth);
        tvSelectRecursionDay = (CustomTextView) findViewById(R.id.tvSelectRecursionDay);
        tvSelectRecursionWeek = (CustomTextView) findViewById(R.id.tvSelectRecursionWeek);
        tvSelectRecursionMonth = (CustomTextView) findViewById(R.id.tvSelectRecursionMonth);
        tvSelectRecursionDay.setOnClickListener(this);
        tvSelectRecursionWeek.setOnClickListener(this);
        tvSelectRecursionMonth.setOnClickListener(this);
        ivAddDays = (ImageView) findViewById(R.id.ivAddDays);
        ivAddMonth = (ImageView) findViewById(R.id.ivAddWeek);
        ivAddWeek = (ImageView) findViewById(R.id.ivAddMonth);
        ivAddDays.setOnClickListener(this);
        ivAddMonth.setOnClickListener(this);
        ivAddWeek.setOnClickListener(this);
        promoForeList = new ArrayList<>();
        startTimeCalender = Calendar.getInstance();
        endTimeCalender = Calendar.getInstance();
        initPromoRecursionDataList();
        initSpinnerPromoRecursionType();
        initSpinnerPromoType();
        initSpinnerPromoForType();
        loadExtraData();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.etPromoStartDate:
                openStartDatePicker();
                break;
            case R.id.etPromoExpDate:
                openExpDatePicker();
                break;
            case R.id.etPromoStartTime:
                openStartTimeDialog();
                break;
            case R.id.etPromoEndTime:
                openExpTimeDialog();
                break;
            case R.id.tvSelectRecursionDay:
            case R.id.ivAddDays:
                openPromoRecursionDialog(daysList, selectedDaysList, tagGroupDay, getResources()
                        .getString(R.string.text_daily_recursion));
                break;
            case R.id.tvSelectRecursionWeek:
            case R.id.ivAddWeek:
                openPromoRecursionDialog(weekList, selectedWeekList, tagGroupWeek, getResources()
                        .getString(R.string.text_weekly_recursion));
                break;
            case R.id.tvSelectRecursionMonth:
            case R.id.ivAddMonth:
                openPromoRecursionDialog(monthList, selectedMonthList, tagGroupMonth, getResources()
                        .getString(R.string.text_monthly_recursion));
                break;
            default:
                // do with default
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        setToolbarCameraIcon(false);
        setToolbarEditIcon(false, 0);
        return true;
    }



    private void scaleUpAndDown(final View view, boolean isShow) {
        if (isShow) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.switchPromoMaxDiscountLimit:
                scaleUpAndDown(llPromoMaxDiscountLimit, isChecked);
                break;
            case R.id.switchPromoRequiredUses:
                scaleUpAndDown(llPromoRequiredUses, isChecked);

                break;
            case R.id.switchPromoMinimumAmountLimit:
                scaleUpAndDown(llPromoMinimumAmountLimit, isChecked);

                break;
            case R.id.switchCompletedOrder:
                scaleUpAndDown(llPromoCompletedOrder, isChecked);
                break;
            case R.id.switchItemCountLimit:
                scaleUpAndDown(llPromoItemCountLimit, isChecked);
                break;
            case R.id.switchPromoRecursion:
                updateUIPromoHaveDate(isChecked);
                break;
            default:
                // do with default
                break;
        }
    }

    private void initSpinnerPromoType() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.promo_type, android.R.layout.simple_spinner_item);
        final CharSequence[] promoTypes = getResources().getTextArray(R.array
                .promo_value);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPromoType.setAdapter(adapter);
        spinnerPromoType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                promoType = Integer.valueOf((String) promoTypes[i]);
                if (promoType == Constant.Type.ABSOLUTE) {
                    llPromoMaxDiscountMain.setVisibility(View.GONE);
                    switchPromoMaxDiscountLimit.setChecked(false);
                } else {
                    llPromoMaxDiscountMain.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initSpinnerPromoForType() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.promo_for_type, android.R.layout.simple_spinner_item);
        final CharSequence[] promoFor = getResources().getTextArray(R.array
                .promo_for_value);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPromoFor.setAdapter(adapter);
        spinnerPromoFor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                promoForValue = Integer.valueOf((String) promoFor[i]);
                if (promoForValue == Constant.Promo.PROMO_FOR_STORE) {
                    hidePromoForList();
                } else {
                    getItemOrProductList(promoForValue);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initSpinnerPromoRecursionType() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.promo_recursion_type, android.R.layout.simple_spinner_item);
        final CharSequence[] promoTypes = getResources().getTextArray(R.array
                .promo_recursion_value);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPromoRecursionType.setAdapter(adapter);
        spinnerPromoRecursionType.setOnItemSelectedListener(new AdapterView
                .OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                promoRecursionType = Integer.valueOf((String) promoTypes[i]);
                updateRecursionPromoUI(promoRecursionType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void addOrUpdatePromoCode() {

        Call<IsSuccessResponse> responseCall;
        PromoCodes promoCodes = new PromoCodes();
        promoCodes.setIsPromoForDeliveryService(false);
        promoCodes.setServerToken(preferenceHelper.getServerToken());
        promoCodes.setStoreId(preferenceHelper.getStoreId());
        promoCodes.setPromoCodeName(etPromoCode.getText().toString());
        promoCodes.setPromoDetails(etPromoDetail.getText().toString());
        promoCodes.setPromoCodeType(promoType);
        promoCodes.setPromoCodeValue(Utilities.roundDecimal(Double.valueOf(etPromoAmount.getText
                ().toString())));
        promoCodes.setIsActive(switchPromoActive.isChecked());
        promoCodes.setIsPromoHaveMinimumAmountLimit(switchPromoMinimumAmountLimit
                .isChecked());
        promoCodes.setIsPromoRequiredUses(switchPromoRequiredUses.isChecked());
        promoCodes.setIsPromoHaveMaxDiscountLimit(switchPromoMaxDiscountLimit
                .isChecked());
        promoCodes.setPromoHaveDate(switchPromoRecursion.isChecked());
        promoCodes.setPromoHaveItemCountLimit(switchItemCountLimit.isChecked());
        promoCodes.setPromoApplyOnCompletedOrder(switchCompletedOrder.isChecked());

        if (switchPromoMinimumAmountLimit.isChecked()) {
            promoCodes.setPromoCodeApplyOnMinimumAmount(Double.valueOf
                    (etPromoMinimumAmountLimit
                            .getText()
                            .toString()));
        }
        if (switchPromoRequiredUses.isChecked()) {
            promoCodes.setPromoCodeUses(Integer.valueOf(etPromoRequiredUses.getText()
                    .toString()));
        }
        if (switchPromoMaxDiscountLimit.isChecked()) {
            promoCodes.setPromoCodeMaxDiscountAmount(Double.valueOf(etPromoMaxDiscountLimit
                    .getText()
                    .toString()));
        }
        if (switchCompletedOrder.isChecked()) {
            promoCodes.setPromoApplyAfterCompletedOrder(Integer.valueOf(etPromoOnCompletedOrder
                    .getText()
                    .toString()));
        }

        if (switchItemCountLimit.isChecked()) {
            promoCodes.setPromoCodeApplyOnMinimumItemCount(Integer.valueOf(etPromoItemCountLimit
                    .getText()
                    .toString()));
        }
        if (switchPromoRecursion.isChecked()) {
            promoCodes.setPromoRecursionType(promoRecursionType);

            switch (promoRecursionType) {
                case Constant.Type.NO_RECURSION:
                    promoCodes.setPromoStartDate(parseContent.webFormat.format(promoStartDate));
                    promoCodes.setPromoExpireDate(parseContent.webFormat.format(promoExpDate));
                    promoCodes.setPromoEndTime("");
                    promoCodes.setPromoStartTime("");
                    promoCodes.setDays(new ArrayList<String>());
                    promoCodes.setWeeks(new ArrayList<String>());
                    promoCodes.setMonths(new ArrayList<String>());
                    break;
                case Constant.Type.DAILY_RECURSION:
                    promoCodes.setPromoStartDate(parseContent.webFormat.format(promoStartDate));
                    promoCodes.setPromoExpireDate(parseContent.webFormat.format(promoExpDate));
                    promoCodes.setPromoStartTime(etPromoStartTime.getText().toString());
                    promoCodes.setPromoEndTime(etPromoEndTime.getText().toString());
                    promoCodes.setDays(new ArrayList<String>());
                    promoCodes.setWeeks(new ArrayList<String>());
                    promoCodes.setMonths(new ArrayList<String>());
                    break;
                case Constant.Type.WEEKLY_RECURSION:
                    promoCodes.setPromoStartDate(parseContent.webFormat.format(promoStartDate));
                    promoCodes.setPromoExpireDate(parseContent.webFormat.format(promoExpDate));
                    promoCodes.setPromoStartTime(etPromoStartTime.getText().toString());
                    promoCodes.setPromoEndTime(etPromoEndTime.getText().toString());
                    if (selectedDaysList.isEmpty()) {
                        Utilities.showToast(this, getResources().getString(R.string
                                .msg_plz_select_day));
                        return;
                    }
                    promoCodes.setDays(selectedDaysList);
                    promoCodes.setWeeks(new ArrayList<String>());
                    promoCodes.setMonths(new ArrayList<String>());
                    break;
                case Constant.Type.MONTHLY_RECURSION:
                    promoCodes.setPromoStartDate(parseContent.webFormat.format(promoStartDate));
                    promoCodes.setPromoExpireDate(parseContent.webFormat.format(promoExpDate));
                    promoCodes.setPromoStartTime(etPromoStartTime.getText().toString());
                    promoCodes.setPromoEndTime(etPromoEndTime.getText().toString());
                    if (selectedDaysList.isEmpty()) {
                        Utilities.showToast(this, getResources().getString(R.string
                                .msg_plz_select_day));
                        return;
                    }
                    if (selectedWeekList.isEmpty()) {
                        Utilities.showToast(this, getResources().getString(R.string
                                .msg_plz_select_week));
                        return;
                    }
                    promoCodes.setDays(selectedDaysList);
                    promoCodes.setWeeks(selectedWeekList);
                    promoCodes.setMonths(new ArrayList<String>());
                    break;
                case Constant.Type.ANNUALLY_RECURSION:
                    promoCodes.setPromoStartDate(parseContent.webFormat.format(promoStartDate));
                    promoCodes.setPromoExpireDate(parseContent.webFormat.format(promoExpDate));
                    promoCodes.setPromoStartTime(etPromoStartTime.getText().toString());
                    promoCodes.setPromoEndTime(etPromoEndTime.getText().toString());
                    if (selectedDaysList.isEmpty()) {
                        Utilities.showToast(this, getResources().getString(R.string
                                .msg_plz_select_day));
                        return;
                    }
                    if (selectedWeekList.isEmpty()) {
                        Utilities.showToast(this, getResources().getString(R.string
                                .msg_plz_select_week));
                        return;
                    }
                    if (selectedMonthList.isEmpty()) {
                        Utilities.showToast(this, getResources().getString(R.string
                                .msg_plz_select_month));
                        return;
                    }
                    promoCodes.setDays(selectedDaysList);
                    promoCodes.setWeeks(selectedWeekList);
                    promoCodes.setMonths(selectedMonthList);
                    break;

                default:
                    // do with default
                    break;
            }

        } else {
            promoCodes.setPromoStartDate("");
            promoCodes.setPromoExpireDate("");
            promoCodes.setPromoEndTime("");
            promoCodes.setPromoStartTime("");
            promoCodes.setDays(new ArrayList<String>());
            promoCodes.setMonths(new ArrayList<String>());
            promoCodes.setWeeks(new ArrayList<String>());
        }

        Utilities.showCustomProgressDialog(this, false);
        if (this.promoCodes == null) {
            ///  add promo code
            setPromoFor(promoForValue, promoCodes);
            responseCall = ApiClient.getClient().create(ApiInterface.class).addPromoCodes
                    (ApiClient.makeGSONRequestBody(promoCodes));
        } else {
            ///update  promo code
            promoCodes.setPromoCodeName(null);
            promoCodes.setPromoId(this.promoCodes.getId());
            setPromoFor(this.promoCodes.getPromoFor(), promoCodes);
            responseCall = ApiClient.getClient().create(ApiInterface.class).updatePromoCodes
                    (ApiClient.makeGSONRequestBody(promoCodes));
        }

        Utilities.printLog(TAG, ApiClient.JSONResponse(promoCodes));
        responseCall.enqueue(new Callback<IsSuccessResponse>() {
            @Override
            public void onResponse(Call<IsSuccessResponse> call, Response<IsSuccessResponse>
                    response) {
                if (parseContent.isSuccessful(response)) {
                    Utilities.hideCustomProgressDialog();
                    if (response.body().isSuccess()) {
                        setResult(Activity.RESULT_OK);
                        finish();
                    } else {
                        ParseContent.getParseContentInstance().showErrorMessage
                                (AddPromoCodeActivity.this, response.body().getErrorCode(), true);
                    }
                }
            }

            @Override
            public void onFailure(Call<IsSuccessResponse> call, Throwable t) {
                Utilities.handleThrowable(TAG, t);
            }
        });
    }

    public void checkPromoReused(String promoCode) {
        etPromoCode.setError(null);
        Utilities.showCustomProgressDialog(this, false);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.STORE_ID, preferenceHelper.getStoreId());
            jsonObject.put(Constant.SERVER_TOKEN, preferenceHelper.getServerToken());
            jsonObject.put(Constant.PROMO_CODE_NAME, promoCode);
        } catch (JSONException e) {
            Utilities.handleException(AddPromoCodeActivity.class.getName(), e);
        }
        Call<IsSuccessResponse> productsCall = ApiClient.getClient().create(ApiInterface.class)
                .checkPromoReuse(ApiClient.makeJSONRequestBody(jsonObject));

        productsCall.enqueue(new Callback<IsSuccessResponse>() {
            @Override
            public void onResponse(Call<IsSuccessResponse> call, Response<IsSuccessResponse>
                    response) {
                Utilities.hideCustomProgressDialog();
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        etPromoCode.setError(null);
                        if (validate()) {
                            addOrUpdatePromoCode();
                        }
                    } else {
                        etPromoCode.getText().clear();
                        etPromoCode.setError(getResources().getString(R.string
                                .msg_promo_code_already_added));

                    }
                } else {
                    Utilities.showHttpErrorToast(response.code(), AddPromoCodeActivity.this);
                }
            }

            @Override
            public void onFailure(Call<IsSuccessResponse> call, Throwable t) {
                Utilities.printLog(TAG, t.getMessage());
            }
        });
    }

    private boolean isInvalidNumber(CustomInputEditText view, boolean isRequired) {
        if (isRequired) {
            try {
                return Double.valueOf(view.getText().toString()) < 0;
            } catch (NumberFormatException e) {
                return true;
            }
        } else {
            return false;
        }


    }

    private boolean validate() {

        String msg = null;

        if (isInvalidNumber(etPromoAmount, true)) {
            msg = getResources().getString(R.string.msg_plz_enter_valid_amount);
            etPromoAmount.setError(msg);
            etPromoAmount.requestFocus();
        } else if (promoType == Constant.Type.PERCENTAGE && Double.valueOf(etPromoAmount.getText()
                .toString()) > 100) {
            msg = getResources().getString(R.string.msg_max_allowed_value);
            etPromoAmount.setError(msg);
            etPromoAmount.requestFocus();
        } else if (isInvalidNumber(etPromoMaxDiscountLimit, switchPromoMaxDiscountLimit.isChecked
                ())) {
            msg = getResources().getString(R.string.msg_plz_enter_valid_amount);
            etPromoMaxDiscountLimit.setError(msg);
            etPromoMaxDiscountLimit.requestFocus();
        } else if (isInvalidNumber(etPromoMinimumAmountLimit, switchPromoMinimumAmountLimit
                .isChecked
                        ())) {
            msg = getResources().getString(R.string.msg_plz_enter_valid_amount);
            etPromoMinimumAmountLimit.setError(msg);
            etPromoMinimumAmountLimit.requestFocus();

        } else if (isInvalidNumber(etPromoRequiredUses, switchPromoRequiredUses.isChecked
                ())) {
            msg = getResources().getString(R.string.msg_plz_enter_valid_amount);
            etPromoRequiredUses.setError(msg);
            etPromoRequiredUses.requestFocus();

        } else if (isInvalidNumber(etPromoItemCountLimit, switchItemCountLimit.isChecked
                ())) {
            msg = getResources().getString(R.string.msg_plz_enter_valid_amount);
            etPromoItemCountLimit.setError(msg);
            etPromoItemCountLimit.requestFocus();

        } else if (isInvalidNumber(etPromoOnCompletedOrder, switchCompletedOrder.isChecked
                ())) {
            msg = getResources().getString(R.string.msg_plz_enter_valid_amount);
            etPromoOnCompletedOrder.setError(msg);
            etPromoOnCompletedOrder.requestFocus();

        } else if (switchPromoRecursion.isChecked()) {
            if (TextUtils.isEmpty(etPromoStartDate.getText().toString())) {
                msg = getResources().getString(R.string.msg_plz_select_date);
                etPromoStartDate.setError(msg);
                etPromoStartDate.requestFocus();
            } else if (TextUtils.isEmpty(etPromoExpDate.getText().toString())) {
                msg = getResources().getString(R.string.msg_plz_select_date);
                etPromoExpDate.setError(msg);
                etPromoExpDate.requestFocus();
            } else if (promoRecursionType != Constant.Type.NO_RECURSION && endTimeCalender
                    .getTimeInMillis()
                    <= startTimeCalender.getTimeInMillis()) {
                msg = getResources().getString(R.string
                        .msg_plz_select_valid_time);
                Utilities.showToast(this, msg);
            }

        }
        return TextUtils.isEmpty(msg);
    }

    private void openStartDatePicker() {
        DatePickerDialog fromPiker = new DatePickerDialog(this,
                startDateSet, year,
                month, day);
        fromPiker.setTitle(getResources().getString(R.string.text_start_date));
        if (promoExpDate > 0) {
            fromPiker.getDatePicker().setMaxDate(promoExpDate - 86400000);
        } else {
            fromPiker.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis() - 10000);
        }
        fromPiker.show();
    }

    private void openExpDatePicker() {
        if (promoStartDate > 0) {
            DatePickerDialog toPiker = new DatePickerDialog(this, expDateSet,
                    year,
                    month,
                    day);
            toPiker.setTitle(getResources().getString(R.string.text_exp_date));
            if (promoStartDate > 0) {
                toPiker.getDatePicker().setMinDate(promoStartDate);
            } else {
                toPiker.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis() + 10000);
            }
            toPiker.show();
        } else {
            Utilities.showToast(this, getResources().getString(R.string
                    .msg_plz_select_start_date_first));
        }

    }

    private void openStartTimeDialog() {

        final DecimalFormat numberFormat = new DecimalFormat("00");
        int hour = startTimeCalender.get(Calendar.HOUR_OF_DAY);
        int minute = startTimeCalender.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,

                new TimePickerDialog.OnTimeSetListener() {

                    int count = 0;

                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int
                            selectedMinute) {
                        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            startTimeCalender.set(Calendar.HOUR_OF_DAY, selectedHour);
                            startTimeCalender.set(Calendar.MINUTE, selectedMinute);
                            startTimeCalender.set(Calendar.SECOND, 0);
                            etPromoStartTime.setText(parseContent.timeFormat2.format
                                    (startTimeCalender
                                            .getTime()));

                        } else {
                            if (count == 1) {
                                startTimeCalender.set(Calendar.HOUR_OF_DAY, selectedHour);
                                startTimeCalender.set(Calendar.MINUTE, selectedMinute);
                                startTimeCalender.set(Calendar.SECOND, 0);
                                etPromoStartTime.setText(parseContent.timeFormat2.format
                                        (startTimeCalender
                                                .getTime()));
                            }

                            count++;
                        }

                    }
                }, hour, minute, true);

        timePickerDialog.show();
    }

    private void openExpTimeDialog() {

        final DecimalFormat numberFormat = new DecimalFormat("00");
        int hour = endTimeCalender.get(Calendar.HOUR_OF_DAY);
        int minute = endTimeCalender.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,

                new TimePickerDialog.OnTimeSetListener() {

                    int count = 0;

                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int
                            selectedMinute) {
                        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            endTimeCalender.set(Calendar.HOUR_OF_DAY, selectedHour);
                            endTimeCalender.set(Calendar.MINUTE, selectedMinute);
                            endTimeCalender.set(Calendar.SECOND, 0);
                            etPromoEndTime.setText(parseContent.timeFormat2.format(endTimeCalender
                                    .getTime()));
                        } else {
                            if (count == 0) {
                                endTimeCalender.set(Calendar.HOUR_OF_DAY, selectedHour);
                                endTimeCalender.set(Calendar.MINUTE, selectedMinute);
                                endTimeCalender.set(Calendar.SECOND, 0);
                                etPromoEndTime.setText(parseContent.timeFormat2.format
                                        (endTimeCalender
                                                .getTime()));
                            }

                            count++;
                        }

                    }
                }, hour, minute, true);

        timePickerDialog.show();
    }

    private void loadExtraData() {
        promoCodes = getIntent().getExtras().getParcelable(Constant
                .PROMO_DETAIL);
        etPromoCode.setEnabled(true);
        if (promoCodes != null) {
            promoForeList.clear();
            promoForeList.addAll(promoCodes.getPromoApplyOn());
            etPromoCode.setText(promoCodes.getPromoCodeName());
            etPromoCode.setEnabled(false);
            etPromoAmount.setText(String.valueOf(promoCodes.getPromoCodeValue()));
            etPromoDetail.setText(promoCodes.getPromoDetails());
            etPromoMinimumAmountLimit.setText(String.valueOf(promoCodes
                    .getPromoCodeApplyOnMinimumAmount()));
            etPromoMaxDiscountLimit.setText(String.valueOf(promoCodes
                    .getPromoCodeMaxDiscountAmount()));
            etPromoRequiredUses.setText(String.valueOf(promoCodes.getPromoCodeUses()));
            etPromoItemCountLimit.setText(String.valueOf(promoCodes
                    .getPromoCodeApplyOnMinimumItemCount()));
            etPromoOnCompletedOrder.setText(String.valueOf(promoCodes
                    .getPromoApplyAfterCompletedOrder()));
            switchPromoRequiredUses.setChecked(promoCodes.isIsPromoRequiredUses());
            switchPromoMinimumAmountLimit.setChecked(promoCodes
                    .isIsPromoHaveMinimumAmountLimit());
            switchPromoMaxDiscountLimit.setChecked(promoCodes.isIsPromoHaveMaxDiscountLimit());
            switchPromoActive.setChecked(promoCodes.isIsActive());
            switchCompletedOrder.setChecked(promoCodes.isPromoApplyOnCompletedOrder());
            switchItemCountLimit.setChecked(promoCodes.isPromoHaveItemCountLimit());
            switchPromoRecursion.setChecked(promoCodes.isPromoHaveDate());
            try {
                if (promoCodes.isPromoHaveDate()) {
                    Date exp = parseContent.webFormat.parse(promoCodes.getPromoExpireDate());
                    promoExpDate = exp.getTime();
                    etPromoExpDate.setText(parseContent.dateFormat.format(exp));
                    Date start = parseContent.webFormat.parse(promoCodes.getPromoStartDate());
                    promoStartDate = start.getTime();
                    etPromoStartDate.setText(parseContent.dateFormat.format(start));

                    selectedDaysList.clear();
                    selectedMonthList.clear();
                    selectedWeekList.clear();

                    selectedDaysList.addAll(promoCodes.getDays());
                    selectedMonthList.addAll(promoCodes.getMonths());
                    selectedWeekList.addAll(promoCodes.getWeeks());

                    tagGroupDay.addTags(selectedDaysList);
                    tagGroupMonth.addTags(selectedMonthList);
                    tagGroupWeek.addTags(selectedWeekList);

                    etPromoStartTime.setText(promoCodes.getPromoStartTime());
                    etPromoEndTime.setText(promoCodes.getPromoEndTime());

                }

            } catch (ParseException e) {
                Utilities.handleException(TAG, e);
            }


            TypedArray array = getResources().obtainTypedArray(R.array.promo_value);
            for (int i = 0; i < array.length(); i++) {
                if (TextUtils.equals(String.valueOf(promoCodes.getPromoCodeType()), array
                        .getString(i)
                )) {
                    spinnerPromoType.setSelection(i);
                    break;
                }

            }
            TypedArray array1 = getResources().obtainTypedArray(R.array.promo_recursion_value);
            for (int i = 0; i < array1.length(); i++) {
                if (TextUtils.equals(String.valueOf(promoCodes.getPromoRecursionType()), array1
                        .getString(i)
                )) {
                    spinnerPromoRecursionType.setSelection(i);
                    break;
                }

            }
            TypedArray array2 = getResources().obtainTypedArray(R.array.promo_for_value);
            for (int i = 0; i < array2.length(); i++) {
                if (TextUtils.equals(String.valueOf(promoCodes.getPromoFor()),
                        array2.getString(i)
                )) {
                    spinnerPromoFor.setSelection(i);
                    break;
                }

            }
            spinnerPromoFor.setEnabled(false);
        }

    }

    /**
     * this method call webservice for get all itemList or productList in store
     */
    public void getItemOrProductList(final int selection) {


        Utilities.showCustomProgressDialog(this, false);
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put(Constant.STORE_ID, ApiClient.makeTextRequestBody(preferenceHelper.getStoreId())
        );
        map.put(Constant.SERVER_TOKEN, ApiClient.makeTextRequestBody(preferenceHelper
                .getServerToken()));

        Call<ProductResponse> call;
        if (selection == Constant.Promo.PROMO_FOR_ITEM) {
            call = ApiClient.getClient().create(ApiInterface.class).getItemList(map);
        } else {
            call = ApiClient.getClient().create(ApiInterface.class).getProductList(map);
        }


        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call,
                                   Response<ProductResponse> response) {
                Utilities.hideCustomProgressDialog();
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        if (promoCodes == null) {
                            promoForeList.clear();
                        }
                        productList.clear();
                        productList.addAll(response.body().getProducts());
                        promoForAdapter = new PromoForAdapter(productList, selection,
                                promoForeList);
                        rcvPromoForList.setAdapter(promoForAdapter);

                    } else {
                        ParseContent.getParseContentInstance().showErrorMessage
                                (AddPromoCodeActivity.this,
                                        response.body().getErrorCode(), false);
                    }
                } else {
                    Utilities.showHttpErrorToast(response.code(), AddPromoCodeActivity.this);
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                Utilities.printLog("ItemListFragment", t.getMessage());
            }
        });
    }


    private void hidePromoForList() {
        if (promoForAdapter != null) {
            productList.clear();
            promoForAdapter.notifyDataSetChanged();
        }

    }

    private void setPromoFor(int promoFor, PromoCodes promoCodes) {
        switch (promoFor) {
            case Constant.Promo.PROMO_FOR_STORE:
                promoCodes.setPromoFor(promoFor);
                promoForeList.clear();
                promoForeList.add(preferenceHelper.getStoreId());
                promoCodes.setPromoApplyOn(promoForeList);
                break;
            case Constant.Promo.PROMO_FOR_ITEM:
            case Constant.Promo.PROMO_FOR_PRODUCT:
                promoCodes.setPromoFor(promoFor);
                promoCodes.setPromoApplyOn(promoForeList);
                break;
            default:
                // do with default
                break;
        }
    }

    private void updateUIPromoHaveDate(boolean isChecked) {
        llRecursion.setVisibility(isChecked ? View.VISIBLE : View.GONE);
    }

    private void updateRecursionPromoUI(int recursionType) {
        switch (recursionType) {
            case Constant.Type.NO_RECURSION:
                llPromoDate.setVisibility(View.VISIBLE);
                llPromoTime.setVisibility(View.GONE);
                llDay.setVisibility(View.GONE);
                llWeek.setVisibility(View.GONE);
                llMonth.setVisibility(View.GONE);
                break;
            case Constant.Type.DAILY_RECURSION:
                llPromoDate.setVisibility(View.VISIBLE);
                llPromoTime.setVisibility(View.VISIBLE);
                llDay.setVisibility(View.GONE);
                llWeek.setVisibility(View.GONE);
                llMonth.setVisibility(View.GONE);
                break;
            case Constant.Type.WEEKLY_RECURSION:
                llPromoDate.setVisibility(View.VISIBLE);
                llPromoTime.setVisibility(View.VISIBLE);
                llDay.setVisibility(View.VISIBLE);
                llWeek.setVisibility(View.GONE);
                llMonth.setVisibility(View.GONE);
                break;
            case Constant.Type.MONTHLY_RECURSION:
                llPromoDate.setVisibility(View.VISIBLE);
                llPromoTime.setVisibility(View.VISIBLE);
                llDay.setVisibility(View.VISIBLE);
                llWeek.setVisibility(View.VISIBLE);
                llMonth.setVisibility(View.GONE);
                break;
            case Constant.Type.ANNUALLY_RECURSION:
                llPromoDate.setVisibility(View.VISIBLE);
                llPromoTime.setVisibility(View.VISIBLE);
                llDay.setVisibility(View.VISIBLE);
                llWeek.setVisibility(View.VISIBLE);
                llMonth.setVisibility(View.VISIBLE);
                break;
            default:
                // do with default
                break;
        }
    }

    private void initPromoRecursionDataList() {

        selectedDaysList = new ArrayList<>();
        selectedWeekList = new ArrayList<>();
        selectedMonthList = new ArrayList<>();

        daysList = new ArrayList<>();
        weekList = new ArrayList<>();
        monthList = new ArrayList<>();

        daysList.add(new PromoRecursionData(getString(R.string.text_sunday), "Sunday", false));
        daysList.add(new PromoRecursionData(getString(R.string.text_monday), "Monday", false));
        daysList.add(new PromoRecursionData(getString(R.string.text_tuesday), "Tuesday", false));
        daysList.add(new PromoRecursionData(getString(R.string.text_wednesday), "Wednesday",
                false));
        daysList.add(new PromoRecursionData(getString(R.string.text_thursday), "Thursday",
                false));
        daysList.add(new PromoRecursionData(getString(R.string.text_friday), "Friday",
                false));
        daysList.add(new PromoRecursionData(getString(R.string.text_saturday), "Saturday",
                false));

        weekList.add(new PromoRecursionData(getString(R.string.text_first), "First", false));
        weekList.add(new PromoRecursionData(getString(R.string.text_second), "Second", false));
        weekList.add(new PromoRecursionData(getString(R.string.text_third), "Third", false));
        weekList.add(new PromoRecursionData(getString(R.string.text_fourth), "Fourth", false));
        weekList.add(new PromoRecursionData(getString(R.string.text_fifth), "Fifth", false));


        monthList.add(new PromoRecursionData(getString(R.string.text_january), "January", false));
        monthList.add(new PromoRecursionData(getString(R.string.text_february), "February",
                false));
        monthList.add(new PromoRecursionData(getString(R.string.text_march), "March", false));
        monthList.add(new PromoRecursionData(getString(R.string.text_april), "April", false));
        monthList.add(new PromoRecursionData(getString(R.string.text_may), "May", false));
        monthList.add(new PromoRecursionData(getString(R.string.text_june), "June", false));
        monthList.add(new PromoRecursionData(getString(R.string.text_july), "July", false));
        monthList.add(new PromoRecursionData(getString(R.string.text_august), "August", false));
        monthList.add(new PromoRecursionData(getString(R.string.text_september), "September",
                false));
        monthList.add(new PromoRecursionData(getString(R.string.text_october), "October", false));
        monthList.add(new PromoRecursionData(getString(R.string.text_november), "November",
                false));
        monthList.add(new PromoRecursionData(getString(R.string.text_december), "December",
                false));


        tagGroupDay.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(View v, int position) {
                tagGroupDay.remove(position);
                selectedDaysList.remove(position);
            }
        });
        tagGroupWeek.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(View v, int position) {
                tagGroupWeek.remove(position);
                selectedWeekList.remove(position);
            }
        });
        tagGroupMonth.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(View v, int position) {
                tagGroupMonth.remove(position);
                selectedMonthList.remove(position);
            }
        });
    }


    private void openPromoRecursionDialog(final ArrayList<PromoRecursionData> recursionData,
                                          final ArrayList<String> selectedList, final TagView
                                                  tagView, String title) {
        for (PromoRecursionData data : recursionData) {
            data.setSelected(selectedList.contains(data.getRequestData()));
        }
        RecyclerView rcvRecursion;
        CustomTextView tvDialogTitle;
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_promo_recursion);
        rcvRecursion = (RecyclerView) dialog.findViewById(R.id.rcvRecursion);
        tvDialogTitle = (CustomTextView) dialog.findViewById(R.id.tvDialogTitle);
        rcvRecursion.setLayoutManager(new LinearLayoutManager(this));
        final PromoRecursionAdapter recursionAdapter = new PromoRecursionAdapter(this,
                recursionData, selectedList);
        rcvRecursion.setAdapter(recursionAdapter);
        rcvRecursion.addOnItemTouchListener(new RecyclerTouchListener(this, rcvRecursion, new
                ClickListener() {


                    @Override
                    public void onClick(View view, int position) {
                        recursionData.get(position).setSelected(!recursionData.get(position)
                                .isSelected());
                        recursionAdapter.notifyItemChanged(position);
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));
        dialog.findViewById(R.id.btnPositive).setOnClickListener(new View.OnClickListener
                () {
            @Override
            public void onClick(View view) {
                selectedList.clear();
                for (PromoRecursionData data : recursionData) {
                    if (data.isSelected()) {
                        selectedList.add(data.getRequestData());
                    }
                }
                tagView.addTags(selectedList);
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.btnNegative).setOnClickListener(new View
                .OnClickListener() {
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

    private void initToolbarButton() {
        CustomTextView tvtoolbarbtn = (CustomTextView) findViewById(R.id.tvtoolbarbtn);
        tvtoolbarbtn.setText(getString(R.string.text_save));
        tvtoolbarbtn.setVisibility(View.VISIBLE);
        tvtoolbarbtn.setOnClickListener(v -> {
            if (TextUtils.isEmpty(etPromoCode.getText().toString())) {
                etPromoCode.setError(getResources().getString(R.string
                        .msg_plz_enter_promo_code_first));
            } else {
                if (promoCodes == null) {
                    checkPromoReused(etPromoCode.getText().toString());
                } else {
                    addOrUpdatePromoCode();
                }


            }
        });

    }
}
