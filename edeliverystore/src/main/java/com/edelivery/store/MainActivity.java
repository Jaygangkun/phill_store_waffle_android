package com.edelivery.store;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.edelivery.store.component.CustomAlterDialog;
import com.edelivery.store.models.datamodel.AppSetting;
import com.edelivery.store.parse.ApiClient;
import com.edelivery.store.parse.ApiInterface;
import com.edelivery.store.parse.ParseContent;
import com.edelivery.store.utils.Constant;
import com.edelivery.store.utils.PreferenceHelper;
import com.edelivery.store.utils.Utilities;
import com.elluminati.edelivery.store.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.gson.Gson;

import java.util.HashMap;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity implements BaseActivity.NetworkListener {

    boolean deviceReceiverRegister, isAllDataSet;
    private ParseContent parseContent;
    private BroadcastReceiver deviceTokenReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent != null && intent.getBooleanExtra(Constant.DEVICE_TOKEN_RECEIVED, false)) {
                if (isAllDataSet) {
                    gotoRegister();
                } else {
                    //getSettings();
                    checkAppKey();

                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ApiClient.setStoreId(preferenceHelper.getStoreId());
        ApiClient.setServerToken(preferenceHelper.getServerToken());
        ApiClient.setSubStoreId(preferenceHelper.getSubStoreId());
        parseContent = ParseContent.getParseContentInstance();
        parseContent.setContext(this);
        if (TextUtils.isEmpty(PreferenceHelper.getPreferenceHelper(this).getDeviceToken())) {
            registerReceiver(deviceTokenReceiver, new IntentFilter(Constant.DEVICE_TOKEN));
            deviceReceiverRegister = true;
        }
        checkAppKey();
        if (!Utilities.checkInternet(this)) {
            Utilities.showInternetDialog(this);
            setNetworkListener(this);
        }
        saveAndroidId();
    }


    private void checkAppKey() {

        if (checkPlayServices()) {
            HashMap<String, RequestBody> map = new HashMap<>();
            map.put(Constant.TYPE, ApiClient.makeTextRequestBody(
                    String.valueOf(Constant.TYPE_STORE)));
            map.put(Constant.DEVICE_TYPE, ApiClient.makeTextRequestBody(Constant.ANDROID));


            Call<AppSetting> call = ApiClient.getClient().create(ApiInterface.class)
                    .getAppSettingDetail(map);

            call.enqueue(new Callback<AppSetting>() {
                @Override
                public void onResponse(Call<AppSetting> call, Response<AppSetting> response) {
                    if (response.isSuccessful()) {
                        Utilities.printLog("MainActivity", "check app key --" + new Gson().toJson
                                (response.body()));

                        if (response.body().isSuccess()) {
                            if (parseContent.parseAppSettingDetails(response)) {

                                if (PreferenceHelper.getPreferenceHelper(MainActivity.this)
                                        .isForceUpdate() &&
                                        checkVersionCode(response.body()
                                                .getVersionCode())) {
                                    openUpdateAppDialog(response.body().isIsForceUpdate());
                                } else {
                                    goToActivity();
                                }
                            }

                        }
                    }
                }

                @Override
                public void onFailure(Call<AppSetting> call, Throwable t) {

                }
            });
        }

    }

    private void gotoRegister() {
        Intent intent = new Intent(MainActivity.this, RegisterLoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.enter, R.anim.exit);
        finish();
    }

    private void gotoHome() {
        startActivity(new Intent(MainActivity.this, HomeActivity.class));
        overridePendingTransition(R.anim.enter, R.anim.exit);
        finish();
    }

    @Override
    protected void onDestroy() {
        if (deviceReceiverRegister) {
            unregisterReceiver(deviceTokenReceiver);
            deviceReceiverRegister = false;
        }

        super.onDestroy();
    }

    /**
     * this method will check that is our app is updated or not ,according to admin app version
     * code
     *
     * @param code
     * @return
     */
    private boolean checkVersionCode(String code) {

        try {
            return getPackageManager().getPackageInfo(getPackageName(), 0).versionCode < Integer
                    .valueOf(code);
        } catch (PackageManager.NameNotFoundException e) {
            Utilities.handleException(MainActivity.class.getName(), e);
        }
        return false;

    }

    private void goToActivity() {
        if (!TextUtils.isEmpty(PreferenceHelper.getPreferenceHelper(MainActivity.this)
                .getStoreId())) {
            gotoHome();
        } else {
            gotoRegister();
        }
    }

    public String getAppVersion() {
        try {
            return getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Utilities.handleException(MainActivity.class.getName(), e);
        }
        return null;
    }

    void openUpdateAppDialog(final boolean isForceUpdate) {
        String btnNegative;
        if (isForceUpdate) {
            btnNegative = getResources()
                    .getString(R.string.text_exit);
        } else {
            btnNegative =
                    getResources()
                            .getString(R.string.text_later);
        }

        CustomAlterDialog customAlterDialog = new CustomAlterDialog(this, this
                .getResources()
                .getString(R.string.text_update_app), this.getResources()
                .getString(R.string.msg_new_app_update_available), true, this.getResources()
                .getString(R.string.text_update), btnNegative) {
            @Override
            public void btnOnClick(int btnId) {
                if (btnId == R.id.btnPositive) {
                    final String appPackageName = getPackageName(); // getPackageName() from Context
                    // or Activity object
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse
                                ("market://details?id="
                                        + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        Utilities.handleException(MainActivity.class.getName(), anfe);
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play" +
                                ".google" +
                                ".com/store/apps/details?id=" + appPackageName)));
                    }
                    dismiss();
                    finishAffinity();
                } else {
                    if (isForceUpdate) {
                        finishAffinity();
                    } else {
                        goToActivity();
                    }
                    dismiss();
                }

            }
        };

        customAlterDialog.show();
    }

    /**
     * this method will check play service is updated
     *
     * @return
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, 12)
                        .show();
            } else {
                Utilities.printLog("Google Paly Service", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private void saveAndroidId() {
        if (TextUtils.isEmpty(PreferenceHelper.getPreferenceHelper(this).getAndroidId())) {
            PreferenceHelper.getPreferenceHelper(this).putAndroidId(Utilities
                    .generateRandomString());
        }

    }

    @Override
    public void onNetworkChange(boolean isEnable) {
        if (isEnable) {
            checkAppKey();
        }


    }
}
