package com.edelivery.store;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.edelivery.store.component.CustomAlterDialog;
import com.edelivery.store.component.CustomNewOrderDialog;
import com.edelivery.store.models.datamodel.Product;
import com.edelivery.store.models.responsemodel.IsSuccessResponse;
import com.edelivery.store.parse.ApiClient;
import com.edelivery.store.parse.ApiInterface;
import com.edelivery.store.parse.ParseContent;
import com.edelivery.store.utils.Constant;
import com.edelivery.store.utils.FontsOverride;
import com.edelivery.store.utils.LanguageHelper;
import com.edelivery.store.utils.NetworkHelper;
import com.edelivery.store.utils.PreferenceHelper;
import com.edelivery.store.utils.Utilities;
import com.elluminati.edelivery.store.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    public String imageName;
    public Date date;
    public SimpleDateFormat simpleDateFormat;
    public boolean isEditable;
    public PreferenceHelper preferenceHelper;
    public ParseContent parseContent;
    public CustomAlterDialog logoutDialog;
    public Toolbar toolbar;
    protected Menu menu;
    private Fragment fragment;
    private static CustomNewOrderDialog customNewOrderDialog;
    private CustomAlterDialog storeApproveDialog;
    private AppReceiver appReceiver = new AppReceiver();
    private NetworkListener networkListener;
    private OrderListener orderListener;
    private NetworkHelper networkHelper;

    /*  static {
          AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
      }*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/ClanPro-News.otf");
        preferenceHelper = PreferenceHelper.getPreferenceHelper(this);
        parseContent = ParseContent.getParseContentInstance();
        parseContent.setContext(this);
        simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss_SSS", Locale.ENGLISH);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.Action.ACTION_NEW_ORDER_ACTION);
        intentFilter.addAction(Constant.Action.ACTION_STORE_APPROVED);
        intentFilter.addAction(Constant.Action.ACTION_STORE_DECLINED);
        intentFilter.addAction(Constant.Action.ACTION_ORDER_STATUS_ACTION);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build
                .VERSION_CODES.LOLLIPOP) {
            networkHelper = NetworkHelper.getInstance();
            networkHelper.initConnectivityManager(this);
        } else {
            intentFilter.addAction(Constant.Action.NETWORK_ACTION);
        }
        registerReceiver(appReceiver, intentFilter);
        setNetworkListener(new NetworkListener() {
            @Override
            public void onNetworkChange(boolean isEnable) {
                if (Utilities.checkInternet(BaseActivity.this)) {
                    Utilities.removeInternetDialog();
                } else {
                    Utilities.removeProgressDialog();
                    Utilities.hideCustomProgressDialog();
                    Utilities.showInternetDialog(BaseActivity.this);
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!Utilities.checkInternet(this)) {
            Utilities.showInternetDialog(this);
        }

    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(appReceiver);
        super.onDestroy();
    }

    protected void setToolbar(Toolbar toolbar, int drawableId, int toolbarColor) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (drawableId > 0) {
            toolbar.setNavigationIcon(drawableId);
        }
        if (toolbarColor > 0) {
            toolbar.setBackgroundColor(ContextCompat.getColor(this, toolbarColor));
        }
    }

    public void setToolbarEditIcon(boolean isVisible, int drawable) {
        MenuItem menuItemEdit = menu.findItem(R.id.ivEditMenu);
        menuItemEdit.setVisible(isVisible);
        if (isVisible) {
            menuItemEdit.setIcon(drawable);
        }
    }

    protected void setToolbarCameraIcon(boolean isVisible) {
        MenuItem menuItemCamera = menu.findItem(R.id.ivCamera);

        menuItemCamera.setVisible(isVisible);
    }


    @Override
    public void onClick(View v) {
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        this.menu = menu;
        setToolbarEditIcon(false, 0);
        setToolbarCameraIcon(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                super.onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * this method call webservice for update product
     *
     * @param product
     * @param isChecked
     * @return
     */
    public Call<IsSuccessResponse> getUpdateProductCall(Product product, boolean isChecked) {

        HashMap<String, RequestBody> map = new HashMap<>();
        map.put(Constant.STORE_ID, ApiClient.makeTextRequestBody(
                PreferenceHelper.getPreferenceHelper(this).getStoreId()));
        map.put(Constant.SERVER_TOKEN, ApiClient.makeTextRequestBody(PreferenceHelper
                .getPreferenceHelper(this).getServerToken()));
        map.put(Constant.NAME, ApiClient.makeTextRequestBody(
                product.getName()));
        map.put(Constant.DETAILS, ApiClient.makeTextRequestBody(
                product.getDetails()));
        map.put(Constant.IS_VISIBLE_IN_STORE, ApiClient.makeTextRequestBody(String.valueOf
                (isChecked)));
        map.put(Constant.PRODUCT_ID, ApiClient.makeTextRequestBody(String.valueOf(product.getId()
        )));

        return ApiClient.getClient().create(ApiInterface.class).updateProduct(map);
    }


    public void setNetworkListener(NetworkListener networkListener) {
        this.networkListener = networkListener;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            networkHelper.setNetworkAvailableListener(networkListener);
        }
    }


    /**
     * this method call a web service for logout from app
     */
    public void openLogoutDialog() {

        if (logoutDialog != null && logoutDialog.isShowing()) {
            return;
        }
        logoutDialog = new CustomAlterDialog(this, getResources().getString(R.string.text_log_out),
                getResources().getString(R.string.msg_lagout), true, getResources().getString(R
                .string.text_yes), getResources().getString(R.string.text_no)) {

            @Override
            public void btnOnClick(int btnId) {
                if (btnId == R.id.btnPositive) {
                    logout(logoutDialog);
                } else {
                    dismiss();
                }
            }
        };
        logoutDialog.setCancelable(false);
        logoutDialog.show();
    }

    public void logout(final Dialog dialog) {
        Utilities.showProgressDialog(BaseActivity.this);
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put(Constant.STORE_ID, ApiClient.makeTextRequestBody(
                PreferenceHelper
                        .getPreferenceHelper(BaseActivity.this).getStoreId()));
        map.put(Constant.SERVER_TOKEN, ApiClient.makeTextRequestBody(
                PreferenceHelper.getPreferenceHelper(BaseActivity.this)
                        .getServerToken()));

        Call<IsSuccessResponse> call = ApiClient.getClient().create(ApiInterface
                .class).logout
                (map);
        call.enqueue(new Callback<IsSuccessResponse>() {
            @Override
            public void onResponse(Call<IsSuccessResponse> call,
                                   Response<IsSuccessResponse> response) {
                Utilities.removeProgressDialog();
                if (response.isSuccessful()) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    if (response.body().isSuccess()) {
                        gotoMainActivity();
                    } else {
                        ParseContent.getParseContentInstance().showErrorMessage
                                (BaseActivity.this, response.body().getErrorCode(),
                                        true);
                    }
                } else {
                    Utilities.showHttpErrorToast(response.code(), BaseActivity.this);
                }
            }

            @Override
            public void onFailure(Call<IsSuccessResponse> call, Throwable t) {
                Utilities.printLog("HomeActivity", t.getMessage());
            }
        });
    }

    public void gotoMainActivity() {
        PreferenceHelper.getPreferenceHelper(this).logout();
        Intent intent = new Intent(this, RegisterLoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void goToDocumentActivity(boolean isApplicationStart) {
        Intent intent = new Intent(this, DocumentActivity.class);
        intent.putExtra(Constant.DOCUMENT_ACTIVITY, isApplicationStart);
        startActivity(intent);
    }

    public void goToReferralShareActivity() {
        Intent intent = new Intent(this, ReferralShareActivity.class);
        startActivity(intent);
    }

    public void goToEarningActivity() {
        Intent intent = new Intent(this, EarningActivity.class);
        startActivity(intent);
    }

    /**
     * this method will send email to selected email id
     *
     * @param email
     */
    protected void contactUsWithEmail(String email) {
        Uri gmmIntentUri = Uri.parse("mailto:" + email +
                "?subject=" + "Request to Admin" +
                "&body=" + "Hello sir");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.gm");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            Utilities.showToast(this, getString(R.string
                    .text_google_mail_app_not_installed));
        }
    }


    protected HashMap<String, RequestBody> getCommonParam(String ordersItemId) {
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put(Constant.STORE_ID, ApiClient.makeTextRequestBody(
                PreferenceHelper.getPreferenceHelper(this).getStoreId()));
        map.put(Constant.SERVER_TOKEN, ApiClient.makeTextRequestBody(PreferenceHelper
                .getPreferenceHelper(this).getServerToken()));
        map.put(Constant.ORDER_ID, ApiClient.makeTextRequestBody(
                ordersItemId));
        return map;
    }

    public int checkWitchOtpValidationON() {
        if (checkEmailVerification() && checkPhoneNumberVerification()) {

            return Constant.SMS_AND_EMAIL_VERIFICATION_ON;

        } else if (checkPhoneNumberVerification()) {
            return Constant.SMS_VERIFICATION_ON;
        } else if (checkEmailVerification()) {
            return Constant.EMAIL_VERIFICATION_ON;
        }
        return -1;

    }

    private boolean checkEmailVerification() {
        return preferenceHelper.getIsVerifyEmail() && !preferenceHelper.isEmailVerified();
    }

    private boolean checkPhoneNumberVerification() {
        return preferenceHelper.getIsVerifyPhone() && !preferenceHelper
                .isPhoneNumberVerified();

    }

    public void openStoreApproveDialog() {
        if (!this.isFinishing()) {
            if (storeApproveDialog != null && storeApproveDialog.isShowing()) {
                return;
            }
            storeApproveDialog = new CustomAlterDialog(this,
                    getString(R.string.text_admin_alert), getString(R.string.text_under_review),
                    true, getString(R.string.text_email),
                    getString(R.string.text_log_out)) {
                @Override
                public void btnOnClick(int btnId) {
                    if (btnId == R.id.btnPositive) {
                        contactUsWithEmail(preferenceHelper.getAdminContactEmail());
                    } else {
                        logout(storeApproveDialog);
                    }
                }
            };
            storeApproveDialog.setCancelable(false);
            storeApproveDialog.show();
        }


    }

    public void closedAdminApprovedDialog() {
        if (storeApproveDialog != null && storeApproveDialog.isShowing()) {
            storeApproveDialog.dismiss();
        }
    }

    public void goToHomeActivity() {
        Intent homeIntent = new Intent(this, HomeActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageHelper.wrapper(newBase, PreferenceHelper
                .getPreferenceHelper(newBase).getLanguageCode()));
    }

    @Override
    public void applyOverrideConfiguration(Configuration overrideConfiguration) {
        if (overrideConfiguration != null) {
            int uiMode = overrideConfiguration.uiMode;
            overrideConfiguration.setTo(getBaseContext().getResources().getConfiguration());
            overrideConfiguration.uiMode = uiMode;
        }
        super.applyOverrideConfiguration(overrideConfiguration);
    }

    public void restartApp() {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void setOrderListener(OrderListener orderListener) {
        this.orderListener = orderListener;
    }

    public interface NetworkListener {
        void onNetworkChange(boolean isEnable);

    }

    public interface OrderListener {
        void onOrderReceive();

    }

    public class AppReceiver extends BroadcastReceiver {


        @Override
        public void onReceive(final Context context, final Intent intent) {

            if (intent != null) {
                switch (intent.getAction()) {
                    case Constant.Action.ACTION_STORE_APPROVED:
                        preferenceHelper.putIsApproved(true);
                        closedAdminApprovedDialog();
                        goToHomeActivity();
                        break;
                    case Constant.Action.ACTION_STORE_DECLINED:
                        preferenceHelper.putIsApproved(false);
                        openStoreApproveDialog();
                        break;
                    case Constant.Action.NETWORK_ACTION:
                        if (networkListener != null) {
                            networkListener.onNetworkChange(Utilities.checkInternet(context));
                        }

                        break;
                    case Constant.Action.ACTION_NEW_ORDER_ACTION:
                        if (!BaseActivity.this.isFinishing() && !TextUtils.isEmpty(preferenceHelper
                                .getServerToken())) {
                            if (customNewOrderDialog != null && customNewOrderDialog.isShowing()) {
                                customNewOrderDialog.notifyDataSetChange(intent.getStringExtra
                                        (Constant.PUSH_DATA));
                                return;
                            }
                            customNewOrderDialog = new CustomNewOrderDialog(BaseActivity.this,
                                    intent.getStringExtra(Constant.PUSH_DATA));
                            customNewOrderDialog.show();
                        }
                        break;
                    case Constant.Action.ACTION_ORDER_STATUS_ACTION:
                        if (orderListener != null) {
                            orderListener.onOrderReceive();
                        }

                        break;
                    default:
                        // do with default
                        break;

                }
            }


        }


    }

    public String getVersionCode() {
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

}
