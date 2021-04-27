package com.edelivery.store;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.edelivery.store.component.CustomAlterDialog;
import com.edelivery.store.component.CustomEditTextDialog;
import com.edelivery.store.fragment.DeliveriesListFragment;
import com.edelivery.store.fragment.ItemListFragment;
import com.edelivery.store.fragment.OrderListFragment;
import com.edelivery.store.fragment.ProfileFragment;
import com.edelivery.store.models.datamodel.SubStore;
import com.edelivery.store.models.responsemodel.InvoiceResponse;
import com.edelivery.store.models.responsemodel.IsSuccessResponse;
import com.edelivery.store.models.responsemodel.OTPResponse;
import com.edelivery.store.models.responsemodel.StoreDataResponse;
import com.edelivery.store.models.singleton.SubStoreAccess;
import com.edelivery.store.parse.ApiClient;
import com.edelivery.store.parse.ApiInterface;
import com.edelivery.store.parse.ParseContent;
import com.edelivery.store.utils.Constant;
import com.edelivery.store.utils.PreferenceHelper;
import com.edelivery.store.utils.Utilities;
import com.elluminati.edelivery.store.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.edelivery.store.utils.Utilities.handleException;

public class HomeActivity extends BaseActivity {

    public DeliveriesListFragment deliveriesListFragment;
    public OrderListFragment orderListFragment;
    public ItemListFragment itemListFragment;
    public ProfileFragment profileFragment;
    public SwipeRefreshLayout mainSwipeLayout;
    public FloatingActionButton floatingBtn;
    private TextView tvToolbarTitle;
    private CustomAlterDialog customExitDialog;
    private CustomAlterDialog storeApproveDialog;
    private CustomEditTextDialog accountVerifyDialog, verifyDialog;
    private String newEmail;
    private String newPhone;
    private BottomNavigationView bottomNavigationView;
    private ScheduledExecutorService updateLocationAndOrderSchedule;
    private boolean isScheduledStart;
    private Handler handler;

    public FloatingActionButton floatingBtnInstantOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mainSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.mainSwipeLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbar(toolbar, 0, R.color.color_app_theme);
        toolbar.setNavigationOnClickListener(null);

        tvToolbarTitle = ((TextView) findViewById(R.id.tvToolbarTitle));
        tvToolbarTitle.setText(getString(R.string.text_orders));


        floatingBtn = (FloatingActionButton) findViewById(R.id.floatingBtn);
        floatingBtn.setOnClickListener(this);
        floatingBtn.setVisibility(View.GONE);

        floatingBtnInstantOrder = (FloatingActionButton) findViewById(R.id.floatingBtnInstantOrder);
        floatingBtnInstantOrder.setOnClickListener(this);
        floatingBtnInstantOrder.setVisibility(View.GONE);

        initBottomBar();
        initHandler();
        getStoreDetails();
    }

    private void getStoreDetails() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.STORE_ID, preferenceHelper.getStoreId());
            jsonObject.put(Constant.SERVER_TOKEN, preferenceHelper.getServerToken());
            jsonObject.put(Constant.APP_VERSION, getVersionCode());
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface
                    .class);
            Call<StoreDataResponse> getDetailsResponse = apiInterface.getDetails(ApiClient
                    .makeJSONRequestBody(jsonObject));
            getDetailsResponse.enqueue(new Callback<StoreDataResponse>() {
                @Override
                public void onResponse(Call<StoreDataResponse> call, Response<StoreDataResponse>
                        response) {
                    Utilities.printLog("STORE_DETAIL", new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        if (response.body().isSuccess()) {
                            ParseContent.getParseContentInstance().parseStoreData(response.body());
                            updateUiSubStoreAccess();
                            checkApproveData();
                        } else {
                            parseContent.showErrorMessage(HomeActivity.this, response.body()
                                    .getErrorCode(), false);
                        }
                    } else {
                        Utilities.showHttpErrorToast(response.code(), HomeActivity.this);
                    }
                }

                @Override
                public void onFailure(Call<StoreDataResponse> call, Throwable t) {

                }
            });

        } catch (JSONException e) {
            handleException("getStoreDetails", e);
        }


    }

    private void initBottomBar() {
        bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(

                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_orders:
                                goToOrderListFragment();
                                break;
                            case R.id.action_delivery:
                                goToDeliveriesListFragment();
                                break;
                            case R.id.action_item:
                                goToItemListFragment();
                                break;
                            case R.id.action_user:
                                goToProfileFragment();
                                break;
                            default:
                                //do something
                                break;
                        }
                        return true;
                    }
                });
        updateUiSubStoreAccess();

    }

    private void checkApproveData() {
        if (preferenceHelper.isApproved()) {
            closedAdminApprovedDialog();
            if (preferenceHelper.getIsAdminDocumentMandatory() && !preferenceHelper
                    .getIsUserAllDocumentsUpload()) {
                goToDocumentActivity(true);
            } else {
                if (preferenceHelper.getIsVerifyEmail() || preferenceHelper.getIsVerifyPhone()) {
                    openVerifyDialog();
                }
            }
        } else {
            if (preferenceHelper.getIsAdminDocumentMandatory() && !preferenceHelper
                    .getIsUserAllDocumentsUpload()) {
                goToDocumentActivity(true);
            } else {
                openStoreApproveDialog();
            }

        }
    }

    /**
     * this method called webservice for get OTP for mobile or email
     */

    private void getOtp(String email, String phone) {
        Utilities.showCustomProgressDialog(this, false);
        Utilities.printLog("getOtp", "in to otp");
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put(Constant.ID, ApiClient.makeTextRequestBody(
                preferenceHelper.getStoreId
                        ()));
        if (!TextUtils.isEmpty(email)) {
            map.put(Constant.EMAIL, ApiClient.makeTextRequestBody(email));
        }
        if (!TextUtils.isEmpty(phone)) {
            map.put(Constant.PHONE, ApiClient.makeTextRequestBody(String.valueOf(phone)));
        }
        map.put(Constant.TYPE, ApiClient.makeTextRequestBody(
                String.valueOf(Constant.Type.STORE)));
        map.put(Constant.COUNTRY_CODE, ApiClient.makeTextRequestBody(preferenceHelper
                .getCountryPhoneCode()));


        Call<OTPResponse> otpResponseCall = ApiClient.getClient().create(ApiInterface.class)
                .otpVerification
                        (map);
        otpResponseCall.enqueue(new Callback<OTPResponse>() {
            @Override
            public void onResponse(Call<OTPResponse> call, Response<OTPResponse> response) {
                Utilities.printLog("getOtp", new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    Utilities.hideCustomProgressDialog();
                    if (response.body().isSuccess()) {
                        openAccountVerifyDialog(String.valueOf(response.body().getOtpForSms()),
                                String.valueOf(response.body().getOtpForEmail()));
                    } else {
                        ParseContent.getParseContentInstance().showErrorMessage(HomeActivity
                                .this, response.body().getErrorCode(), true);
                        //getOtp();
                    }
                } else {
                    Utilities.showHttpErrorToast(response.code(), HomeActivity.this);
                }

            }

            @Override
            public void onFailure(Call<OTPResponse> call, Throwable t) {
                Utilities.printLog("getOtp", t.getMessage());
            }
        });
    }

    /**
     * this method call a webservice for set OTP verification result
     */
    private void verifyStoreOtp(boolean isVerifyphone, boolean isVerifyEmail) {
        Utilities.showCustomProgressDialog(this, false);

        JSONObject jsonObject = new JSONObject();


        try {
            jsonObject.put(Constant.SERVER_TOKEN, preferenceHelper.getServerToken());
            jsonObject.put(Constant.STORE_ID, preferenceHelper.getStoreId());
            if (isVerifyEmail) {
                jsonObject.put(Constant.IS_EMAIL_VERIFIED, preferenceHelper.isEmailVerified());
                jsonObject.put(Constant.EMAIL, newEmail);
            }
            if (isVerifyphone) {

                jsonObject.put(Constant.IS_PHONE_NUMBER_VERIFIED, preferenceHelper
                        .isPhoneNumberVerified());
                jsonObject.put(Constant.PHONE, newPhone);

                jsonObject.put(Constant.COUNTRY_CODE, preferenceHelper.getCountryPhoneCode());
            }


            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface
                    .class);
            Call<IsSuccessResponse> getStoreOtpResponse = apiInterface.storeOtpVerification
                    (ApiClient.makeJSONRequestBody(jsonObject));
            getStoreOtpResponse.enqueue(new Callback<IsSuccessResponse>() {
                @Override
                public void onResponse(Call<IsSuccessResponse> call, Response<IsSuccessResponse>
                        response) {

                    if (response.isSuccessful()) {
                        Utilities.hideCustomProgressDialog();
                        if (response.body().isSuccess()) {
                            accountVerifyDialog.dismiss();
                        } else {
                            ParseContent.getParseContentInstance().showErrorMessage(HomeActivity
                                    .this, response.body().getErrorCode(), true);

                        }
                    } else {
                        Utilities.showHttpErrorToast(response.code(), HomeActivity.this);
                    }
                }

                @Override
                public void onFailure(Call<IsSuccessResponse> call, Throwable t) {

                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void openAccountVerifyDialog(final String otpForSms, final String otpForEmail) {


        final int otpType = checkWitchOtpValidationON();

        if (otpType != -1) {
            String message = null;

            switch (otpType) {
                case Constant.EMAIL_VERIFICATION_ON:
                    message = getString(R.string.text_verify_email);
                    break;
                case Constant.SMS_VERIFICATION_ON:
                    message = getString(R.string.text_verify_sms);
                    break;
                case Constant.SMS_AND_EMAIL_VERIFICATION_ON:
                    message = getString(R.string.text_verify_email_sms);
                    break;
                default:
                    // do with default
                    break;
            }
            accountVerifyDialog = new CustomEditTextDialog(this, getString(R.string
                    .text_verify_details), message,
                    getString(R.string.text_ok), getString(R.string.text_exit),
                    otpType) {


                @Override
                public void btnOnClick(int btnId, TextInputEditText etSMSOtp, TextInputEditText
                        etEmailOtp) {
                    if (btnId == R.id.btnPositive) {
                        switch (otpType) {
                            case Constant.SMS_AND_EMAIL_VERIFICATION_ON:
                                if (TextUtils.isEmpty(etSMSOtp.getText())) {
                                    etSMSOtp.setError(getString(R.string.msg_invalid_data));
                                } else if (TextUtils.isEmpty(etEmailOtp.getText())) {
                                    etEmailOtp.setError(getString(R.string.msg_invalid_data));
                                } else {
                                    if (TextUtils.equals(otpForSms, etSMSOtp.getText().toString()
                                            .trim())
                                            && TextUtils.equals(otpForEmail, etEmailOtp.getText()
                                            .toString().trim())) {
                                        preferenceHelper.putIsEmailVerified(true);
                                        preferenceHelper.putIsPhoneNumberVerified(true);
                                        verifyStoreOtp(true, true);
                                    } else {
                                        Utilities.showToast(HomeActivity.this, getString(R.string
                                                .msg_otp_wrong));
                                    }
                                }
                                break;
                            case Constant.SMS_VERIFICATION_ON:
                                if (TextUtils.isEmpty(etSMSOtp.getText())) {
                                    etSMSOtp.setError(getString(R.string.msg_invalid_data));
                                } else {
                                    if (TextUtils.equals(otpForSms, etSMSOtp.getText().toString()
                                            .trim())) {
                                        preferenceHelper.putIsPhoneNumberVerified(true);
                                        verifyStoreOtp(true, false);
                                    } else {
                                        Utilities.showToast(HomeActivity.this, getString(R.string
                                                .msg_otp_wrong));
                                    }
                                }
                                break;
                            case Constant.EMAIL_VERIFICATION_ON:

                                if (TextUtils.isEmpty(etEmailOtp.getText())) {
                                    etEmailOtp.setError(getString(R.string.msg_invalid_data));
                                } else {
                                    if (TextUtils.equals(otpForEmail, etEmailOtp.getText()
                                            .toString().trim())) {
                                        preferenceHelper.putIsEmailVerified(true);
                                        verifyStoreOtp(false, true);
                                    } else {
                                        Utilities.showToast(HomeActivity.this, getString(R.string
                                                .msg_otp_wrong));
                                    }
                                }

                                break;
                        }
                    } else {
                        dismiss();
                        finish();
                    }
                }
            };
            accountVerifyDialog.setCancelable(false);
            accountVerifyDialog.show();
        }


    }


    private void openVerifyDialog() {


        if (verifyDialog != null && verifyDialog.isShowing()) {
            return;
        }
        final int otpType = checkWitchOtpValidationON();
        if (otpType != -1) {


            verifyDialog = new CustomEditTextDialog(this, getResources().getString(R.string
                    .text_confirm_details),
                    getResources().getString(R.string.text_please_confirm_details),
                    getResources().getString(R.string.text_ok),
                    getResources().getString(R.string.text_log_out), otpType) {


                @Override
                public void btnOnClick(int btnId, TextInputEditText etSMSOtp, TextInputEditText
                        etEmailOtp) {
                    if (btnId == R.id.btnPositive) {

                        switch (otpType) {
                            case Constant.SMS_AND_EMAIL_VERIFICATION_ON:
                                if (TextUtils.isEmpty(etSMSOtp.getText())) {
                                    etSMSOtp.setError(getString(R.string.msg_invalid_data));
                                } else if (TextUtils.isEmpty(etEmailOtp.getText())) {
                                    etEmailOtp.setError(getString(R.string.msg_invalid_data));
                                } else {
                                    newEmail = etEmailOtp.getText().toString();
                                    newPhone = etSMSOtp.getText().toString();
                                    dismiss();
                                    getOtp(newEmail, newPhone);
                                }
                                break;
                            case Constant.SMS_VERIFICATION_ON:
                                if (TextUtils.isEmpty(etSMSOtp.getText())) {
                                    etSMSOtp.setError(getString(R.string.msg_invalid_data));
                                } else {
                                    newPhone = etSMSOtp.getText().toString();
                                    dismiss();
                                    getOtp(null, newPhone);
                                }
                                break;
                            case Constant.EMAIL_VERIFICATION_ON:
                                if (TextUtils.isEmpty(etEmailOtp.getText())) {
                                    etEmailOtp.setError(getString(R.string.msg_invalid_data));
                                } else {
                                    newEmail = etEmailOtp.getText().toString();
                                    dismiss();
                                    getOtp(newEmail, null);
                                }
                                break;

                        }

                    } else {
                        dismiss();
                        openLogoutDialog();
                    }
                }
            };

            verifyDialog.setCancelable(false);
            verifyDialog.show();
            verifyDialog.textInputLayoutSMSOtp.setHint(getResources().getString(R.string
                    .text_phone));
            verifyDialog.textInputLayoutEmailOtp.setHint(getResources().getString(R.string
                    .text_email));
            verifyDialog.etSMSOtp.setText(preferenceHelper.getPhone());
            verifyDialog.etEmailOtp.setText(preferenceHelper.getEmail());

        }
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);

        if (v.getId() == R.id.floatingBtn) {
            if (orderListFragment != null && orderListFragment.isVisible() && PreferenceHelper
                    .getPreferenceHelper(this).getIsStoreCreateOrder()) {
                goToStoreOrderProductActivity();
            }
        }
        if (v.getId() == R.id.floatingBtnInstantOrder) {
//            Utilities.showProgressDialog(this);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(Constant.SERVER_TOKEN, preferenceHelper
                        .getServerToken());
                jsonObject.put(Constant.STORE_ID, preferenceHelper.getStoreId());
            } catch (JSONException e) {
                Utilities.handleThrowable("CHECKOUT_ACTIVITY", e);
            }

            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<IsSuccessResponse> responseCall = apiInterface.createInstantOrder(ApiClient
                    .makeJSONRequestBody(jsonObject));

            responseCall.enqueue(new Callback<IsSuccessResponse>() {
                @Override
                public void onResponse(Call<IsSuccessResponse> call, Response<IsSuccessResponse>
                        response) {
                    Utilities.removeProgressDialog();
                    if (parseContent.isSuccessful(response)) {
//                        Utilities.hideCustomProgressDialog();
                        Message message = handler.obtainMessage();
                        handler.sendMessage(message);
                        if (response.body().isSuccess()) {
                            Utilities.printLog("HOME_ACTIVITY", ApiClient.JSONResponse(response
                                    .body()));
                            Utilities.showToast(HomeActivity.this, "Instant Order Placed");
                        } else {
                            parseContent.showErrorMessage(HomeActivity
                                    .this, response.body().getErrorCode(), false);
                        }
                    }
                }

                @Override
                public void onFailure(Call<IsSuccessResponse> call, Throwable t) {
                    Utilities.handleThrowable("HOME_ACTIVITY", t);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu1) {
        super.onCreateOptionsMenu(menu1);
        menu = menu1;
        setToolbarEditIcon(false, R.drawable.ic_filter);
        setToolbarCameraIcon(false);
        loadFragmentAccordingStoreAccess();
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ivEditMenu:
                ItemListFragment itemListFragment = (ItemListFragment) getSupportFragmentManager
                        ().findFragmentByTag(Constant.Tag
                        .ITEM_LIST_FRAGMENT);
                if (itemListFragment != null) {
                    itemListFragment.slidFilterView();
                }
                break;

            default:
                // do with default
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        openExitDialog();
    }

    private void openExitDialog() {
        if (customExitDialog != null && customExitDialog.isShowing()) {
            return;
        }
        customExitDialog = new CustomAlterDialog(this, getResources().getString(R.string.text_exit),
                getResources().getString(R.string.text_are_you_sure), true,
                getResources().getString(R.string.text_ok),
                getResources().getString(R.string.text_cancel)) {
            @Override
            public void btnOnClick(int btnId) {
                if (btnId == R.id.btnPositive) {
                    dismiss();
                    finish();
                } else {
                    dismiss();
                }

            }
        };
        customExitDialog.show();
    }

    /**
     * this method used to transit one fragment to other fragment
     *
     * @param fragment
     * @param addToBackStack
     * @param isAnimate
     * @param tag
     */
    public void addFragment(Fragment fragment, boolean addToBackStack,
                            boolean isAnimate, String tag) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        if (isAnimate) {
            ft.setCustomAnimations(R.anim.slide_in_right,
                    R.anim.slide_out_left, R.anim.slide_in_left,
                    R.anim.slide_out_right);
        }
        if (addToBackStack) {
            ft.addToBackStack(tag);
        }
        ft.replace(R.id.contain_frame, fragment, tag);
        ft.commitAllowingStateLoss();
    }

    private void goToOrderListFragment() {
        if (SubStoreAccess.getInstance().isAccess(SubStoreAccess.ORDER)) {
            if (getSupportFragmentManager().findFragmentByTag(Constant.Tag.ORDER_LIST_FRAGMENT) ==
                    null) {
                orderListFragment = new OrderListFragment();
            } else {
                orderListFragment =
                        (OrderListFragment) getSupportFragmentManager().findFragmentByTag
                                (Constant.Tag
                                        .ORDER_LIST_FRAGMENT);
            }
            addFragment(orderListFragment, true, true, Constant.Tag.ORDER_LIST_FRAGMENT);
            setToolbarEditIcon(false, R.drawable.ic_filter);
            tvToolbarTitle.setText(getString(R.string.text_orders));
            floatingBtn.setVisibility(preferenceHelper.getIsStoreCreateOrder() ? View.VISIBLE : View
                    .GONE);
            floatingBtnInstantOrder.setVisibility(preferenceHelper.getIsStoreCreateOrder() ? View.VISIBLE : View
                    .GONE);
        }
    }

    private void goToItemListFragment() {
        if (SubStoreAccess.getInstance().isAccess(SubStoreAccess.PRODUCT)) {
            if (getSupportFragmentManager().findFragmentByTag(Constant.Tag.ITEM_LIST_FRAGMENT) ==
                    null) {
                itemListFragment = new ItemListFragment();
            } else {
                itemListFragment = (ItemListFragment) getSupportFragmentManager().findFragmentByTag
                        (Constant.Tag
                                .ITEM_LIST_FRAGMENT);
            }

            addFragment(itemListFragment, true, true, Constant.Tag.ITEM_LIST_FRAGMENT);
            setToolbarEditIcon(true, R.drawable.filter_store);
            tvToolbarTitle.setText(getString(R.string.text_items));
            floatingBtn.setVisibility(View.GONE);
            floatingBtnInstantOrder.setVisibility(View.GONE);
        }
    }

    private void goToDeliveriesListFragment() {
        if (SubStoreAccess.getInstance().isAccess(SubStoreAccess.DELIVERIES)) {
            if (getSupportFragmentManager().findFragmentByTag(Constant.Tag.DELIVERIES_LIST_FRAGMENT)
                    == null) {
                deliveriesListFragment = new DeliveriesListFragment();
            } else {
                deliveriesListFragment = (DeliveriesListFragment) getSupportFragmentManager()
                        .findFragmentByTag(Constant.Tag
                                .DELIVERIES_LIST_FRAGMENT);
            }
            addFragment(deliveriesListFragment, true, true, Constant.Tag.DELIVERIES_LIST_FRAGMENT);
            setToolbarEditIcon(false, 0);
            tvToolbarTitle.setText(getString(R.string.text_deliveries));
            floatingBtn.setVisibility(View.GONE);
            floatingBtnInstantOrder.setVisibility(View.GONE);
        }
    }

    private void goToProfileFragment() {
        if (getSupportFragmentManager().findFragmentByTag(Constant.Tag.PROFILE_FRAGMENT) == null) {
            profileFragment = new ProfileFragment();
        } else {
            profileFragment = (ProfileFragment) getSupportFragmentManager().findFragmentByTag
                    (Constant.Tag
                            .PROFILE_FRAGMENT);
        }
        addFragment(profileFragment, true, true, Constant.Tag.PROFILE_FRAGMENT);
        setToolbarEditIcon(false, 0);
        tvToolbarTitle.setText(getString(R.string.text_account));
        floatingBtn.setVisibility(View.GONE);
        floatingBtnInstantOrder.setVisibility(View.GONE);
    }

    public void startSchedule() {

        if (!isScheduledStart && preferenceHelper.isApproved()) {

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    Message message = handler.obtainMessage();
                    handler.sendMessage(message);
                }
            };
            updateLocationAndOrderSchedule = Executors.newSingleThreadScheduledExecutor();
            updateLocationAndOrderSchedule.scheduleWithFixedDelay(runnable, 0,
                    Constant.ORDER_SCHEDULED, TimeUnit
                            .SECONDS);
            Utilities.printLog(HomeActivity.class.getName(), "Schedule Start");
            isScheduledStart = true;
        }
    }

    public void stopSchedule() {
        if (isScheduledStart) {
            Utilities.printLog(HomeActivity.class.getName(), "Schedule Stop");
            updateLocationAndOrderSchedule.shutdown(); // Disable new tasks from being submitted
            // Wait a while for existing tasks to terminate
            try {
                if (!updateLocationAndOrderSchedule.awaitTermination(60, TimeUnit.SECONDS)) {
                    updateLocationAndOrderSchedule.shutdownNow(); // Cancel currently executing
                    // tasks
                    // Wait a while for tasks to respond to being cancelled
                    if (!updateLocationAndOrderSchedule.awaitTermination(60, TimeUnit.SECONDS))
                        Utilities.printLog(HomeActivity.class.getName(), "Pool did not " +
                                "terminate");

                }
            } catch (InterruptedException e) {
                Utilities.handleException(HomeActivity.class.getName(), e);
                // (Re-)Cancel if current thread also interrupted
                updateLocationAndOrderSchedule.shutdownNow();
                // Preserve interrupt status
                Thread.currentThread().interrupt();
            }
            isScheduledStart = false;
        }

    }

    private void initHandler() {
        /**
         * This handler receive a message from  requestStatusScheduledService and update provider
         * location and order status
         */
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                OrderListFragment orderListFragment = (OrderListFragment)
                        getSupportFragmentManager().findFragmentByTag
                                (Constant.Tag
                                        .ORDER_LIST_FRAGMENT);
                if (orderListFragment != null) {
                    Utilities.showCustomProgressDialog(HomeActivity.this, false);
                    orderListFragment.getOrderList();
                }

            }

        };


    }

    private void goToStoreOrderProductActivity() {
        Intent intent = new Intent(this, StoreOrderProductActivity.class);
        intent.putExtra(Constant.IS_ORDER_UPDATE, false);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }


    private void loadFragmentAccordingStoreAccess() {
        if (SubStoreAccess.getInstance().isAccess(SubStoreAccess.ORDER)) {
            goToOrderListFragment();
        } else if (SubStoreAccess.getInstance().isAccess(SubStoreAccess.DELIVERIES)) {
            goToDeliveriesListFragment();
        } else {
            goToItemListFragment();
        }


    }

    private void updateUiSubStoreAccess() {
        if (SubStoreAccess.getInstance().isAccess(SubStoreAccess.ORDER)) {
            bottomNavigationView.getMenu().findItem(R.id.action_orders).setVisible(true);
        } else {
            bottomNavigationView.getMenu().findItem(R.id.action_orders).setVisible(false);
        }

        if (SubStoreAccess.getInstance().isAccess(SubStoreAccess.DELIVERIES)) {
            bottomNavigationView.getMenu().findItem(R.id.action_delivery).setVisible(true);
        } else {
            bottomNavigationView.getMenu().findItem(R.id.action_delivery).setVisible(false);
        }

        if (SubStoreAccess.getInstance().isAccess(SubStoreAccess.PRODUCT)) {
            bottomNavigationView.getMenu().findItem(R.id.action_item).setVisible(true);
        } else {
            bottomNavigationView.getMenu().findItem(R.id.action_item).setVisible(false);
        }
    }
}
