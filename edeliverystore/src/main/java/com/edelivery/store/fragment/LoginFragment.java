package com.edelivery.store.fragment;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.edelivery.store.RegisterLoginActivity;
import com.edelivery.store.component.CustomEditTextDialog;
import com.edelivery.store.component.CustomLanguageDialog;
import com.edelivery.store.models.responsemodel.IsSuccessResponse;
import com.edelivery.store.models.responsemodel.StoreDataResponse;
import com.edelivery.store.models.singleton.Language;
import com.edelivery.store.parse.ApiClient;
import com.edelivery.store.parse.ApiInterface;
import com.edelivery.store.parse.ParseContent;
import com.edelivery.store.utils.Constant;
import com.edelivery.store.utils.PreferenceHelper;
import com.edelivery.store.utils.Utilities;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.util.HashMap;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * LoginFragment on 30-01-2017.
 */

public class LoginFragment extends Fragment implements View.OnClickListener, TextView
        .OnEditorActionListener {

    private TextInputEditText etUserName, etPassword;
    private RegisterLoginActivity activity;
    private CustomEditTextDialog forgotPswDialog;
    private String emailText;
    private CustomTextView tvLanguage;
    private RegisterLoginActivity registerLoginActivity;
    private TextInputLayout inputLayoutUserName;
    private CustomLanguageDialog customLanguageDialog;
    private LinearLayout llSocialLogin;
    private Spinner spinnerLoginAs;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (RegisterLoginActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);
        registerLoginActivity = (RegisterLoginActivity) getActivity();
        etUserName = (TextInputEditText) view.findViewById(R.id.etUserName);
        etPassword = (TextInputEditText) view.findViewById(R.id.etUserPassword);
        etPassword.setOnEditorActionListener(this);
        tvLanguage = (CustomTextView) view.findViewById(R.id.tvLanguage);
        inputLayoutUserName = (TextInputLayout) view.findViewById(R.id.inputLayoutUserName);
        spinnerLoginAs = (Spinner) view.findViewById(R.id.spinnerLoginAs);
        view.findViewById(R.id.tvForgotPsw).setOnClickListener(this);
        view.findViewById(R.id.btnLogin).setOnClickListener(this);
        tvLanguage.setOnClickListener(this);
        setPlaceHolder();
        llSocialLogin = (LinearLayout) view.findViewById(R.id.llSocialButton);
        activity.initFBLogin(view);
        activity.initGoogleLogin(view);
        activity.initTwitterLogin(view);
        return view;
    }

    private void setPlaceHolder() {
        if (registerLoginActivity.preferenceHelper.getIsLoginByPhone() && registerLoginActivity
                .preferenceHelper.getIsLoginByEmail()) {
            inputLayoutUserName.setHint(registerLoginActivity.getResources().getString(R.string
                    .text_email_or_phone));

        } else if (registerLoginActivity.preferenceHelper.getIsLoginByEmail()) {
            inputLayoutUserName.setHint(registerLoginActivity.getResources().getString(R.string
                    .text_email));

        } else if (registerLoginActivity.preferenceHelper.getIsLoginByPhone()) {
            inputLayoutUserName.setHint(registerLoginActivity.getResources().getString(R.string
                    .text_phone));

        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setLanguageName();
        checkSocialLoginISOn(activity.preferenceHelper.getIsLoginBySocial());
        initStoreLoginOption();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnLogin:
                if (validate()) {
                    login("");
                }
                break;
            case R.id.tvForgotPsw:
                if (forgotPswDialog != null && forgotPswDialog.isShowing()) {
                    return;
                }

                forgotPswDialog = new CustomEditTextDialog(activity, activity.getResources()
                        .getString(R.string.text_forgot_password), activity.getResources()
                        .getString(R.string.text_forgot_psw_title), activity.getResources()
                        .getString(R.string.text_ok), activity.getResources().getString(R.string
                        .text_cancel), -1) {


                    @Override
                    public void btnOnClick(int btnId, TextInputEditText etSMSOtp,
                                           TextInputEditText etEmail) {
                        if (btnId == R.id.btnPositive) {
                            emailText = etEmail.getText().toString();
                            validateEmail(etEmail);
                        } else {
                            this.dismiss();
                        }
                    }
                };
                forgotPswDialog.show();
                break;
            case R.id.tvLanguage:
                openLanguageDialog();
                break;

        }
    }

    private void validateEmail(TextInputEditText etEmail) {

        if (TextUtils.isEmpty(etEmail.getText())) {
            etEmail.setError(activity.getResources().getString(R.string.msg_empty_email));
        } else if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()) {
            etEmail.setError(activity.getResources().getString(R.string.msg_valid_email));
        } else {
            forgetPassword();
        }
    }

    /**
     * this method call webservice for forgot password
     */
    private void forgetPassword() {
        Utilities.showProgressDialog(activity);

        HashMap<String, RequestBody> map = new HashMap<>();
        map.put(Constant.EMAIL, ApiClient.makeTextRequestBody(
                emailText));
        map.put(Constant.TYPE, ApiClient.makeTextRequestBody(
                String.valueOf(Constant.Type.STORE)));

        Call<IsSuccessResponse> responseCall =
                ApiClient.getClient().create(ApiInterface.class).forgotPassword
                        (map);
        responseCall.enqueue(new Callback<IsSuccessResponse>() {
            @Override
            public void onResponse(Call<IsSuccessResponse> call, Response<IsSuccessResponse>
                    response) {
                Utilities.removeProgressDialog();
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        forgotPswDialog.dismiss();
                        ParseContent.getParseContentInstance().showMessage(activity,
                                response.body().getMessage());
                    } else {
                        ParseContent.getParseContentInstance().showErrorMessage(activity,
                                response.body().getErrorCode(), true);
                    }
                } else {
                    Utilities.showHttpErrorToast(response.code(), activity);
                }
            }

            @Override
            public void onFailure(Call<IsSuccessResponse> call, Throwable t) {
                Utilities.printLog("Login", t.getMessage());
            }
        });

    }

    private boolean validate() {
        String msg = null;
        if (TextUtils.equals(inputLayoutUserName.getHint().toString(), getResources().getString(R
                .string
                .text_email_or_phone))) {
            if (Patterns.EMAIL_ADDRESS.matcher(etUserName.getText().toString()).matches
                    ()) {
                msg = validatePassword();
            } else if (Patterns.PHONE.matcher(etUserName.getText().toString()).matches
                    ()) {
                msg = validatePassword();
            } else {
                msg = getString(R.string.msg_please_enter_valid_email_or_phone);
                etUserName.setError(msg);
                etUserName.requestFocus();
            }

        } else if (TextUtils.equals(inputLayoutUserName.getHint().toString(), getResources()
                .getString(R
                        .string.text_phone))) {
            if (!Patterns.PHONE.matcher(etUserName.getText().toString()).matches
                    ()) {
                msg = getString(R.string.msg_please_enter_valid_mobile);
                etUserName.setError(msg);
                etUserName.requestFocus();
            } else {
                msg = validatePassword();
            }
        } else if (TextUtils.equals(inputLayoutUserName.getHint().toString(), getResources()
                .getString(R
                        .string.text_email))) {
            if (!Patterns.EMAIL_ADDRESS.matcher(etUserName.getText().toString()).matches
                    ()) {
                msg = getString(R.string.msg_valid_email);
                etUserName.setError(msg);
                etUserName.requestFocus();
            } else {
                msg = validatePassword();
            }

        }
        return TextUtils.isEmpty(msg);

    }


    private String validatePassword() {
        String msg = null;
        if (TextUtils.isEmpty(etPassword.getText().toString())) {
            msg = getString(R.string.msg_empty_password);
            etPassword.setError(msg);
            etPassword.requestFocus();
        } else if (etPassword.getText().toString().length() < 6) {
            msg = getString(R.string.msg_password_length);
            etPassword.setError(msg);
            etPassword.requestFocus();
        }
        return msg;
    }

    /**
     * this method call webservice for login to store user
     */
    public void login(String socialId) {

        Utilities.showProgressDialog(activity);

        HashMap<String, RequestBody> map = new HashMap<>();
        Utilities.printLog("Login", PreferenceHelper.getPreferenceHelper(activity).getDeviceToken
                ());
        if (TextUtils.isEmpty(socialId)) {
            map.put(Constant.EMAIL, ApiClient.makeTextRequestBody(
                    etUserName.getText().toString().trim()
                            .toLowerCase()));
            map.put(Constant.PASS_WORD, ApiClient.makeTextRequestBody(
                    etPassword.getText().toString()));
            map.put(Constant.SOCIAL_ID, ApiClient.makeTextRequestBody(
                    socialId));
            map.put(Constant.LOGIN_BY, ApiClient.makeTextRequestBody(Constant.MANUAL));
        } else {
            map.put(Constant.EMAIL, ApiClient.makeTextRequestBody(
                    ""));
            map.put(Constant.PASS_WORD, ApiClient.makeTextRequestBody(
                    ""));
            map.put(Constant.SOCIAL_ID, ApiClient.makeTextRequestBody(
                    socialId));
            map.put(Constant.LOGIN_BY, ApiClient.makeTextRequestBody(Constant.SOCIAL));
        }

        map.put(Constant.DEVICE_TOKEN, ApiClient.makeTextRequestBody(PreferenceHelper
                .getPreferenceHelper
                        (activity).getDeviceToken()));
        map.put(Constant.DEVICE_TYPE, ApiClient.makeTextRequestBody(Constant.ANDROID));
        map.put(Constant.APP_VERSION, ApiClient.makeTextRequestBody(
                activity.getVersionCode()));
        Utilities.printLog("Login", PreferenceHelper.getPreferenceHelper(activity).getVersionCode
                ());
        Utilities.printLog("Login", map.toString());
        Call<StoreDataResponse> call;
        if (TextUtils.equals(activity.getString(R.string.text_sub_store),
                spinnerLoginAs.getSelectedItem().toString())) {
            call = ApiClient.getClient().create(ApiInterface.class).subStoreLogin(map);
        } else {
            call = ApiClient.getClient().create(ApiInterface.class).login(map);
        }
        call.enqueue(new Callback<StoreDataResponse>() {
            @Override
            public void onResponse(Call<StoreDataResponse> call, Response<StoreDataResponse>
                    response) {
                Utilities.removeProgressDialog();
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        Gson gson = new Gson();
                        Utilities.printLog("login", gson.toJson(response.body().getStoreData()));
                        ParseContent.getParseContentInstance().parseStoreData(response.body());
                        PreferenceHelper.getPreferenceHelper(activity).putAndroidId(Utilities
                                .generateRandomString());
                        PreferenceHelper.getPreferenceHelper(activity).putCartId("");
                        ParseContent.getParseContentInstance().showMessage(activity,
                                response.body().getMessage());
                        activity.gotoHomeActivity(response.body().getStoreData());
                    } else {
                        ParseContent.getParseContentInstance().showErrorMessage(activity,
                                response.body().getErrorCode(), true);
                    }
                } else {
                    Utilities.showHttpErrorToast(response.code(), activity);
                }
            }

            @Override
            public void onFailure(Call<StoreDataResponse> call, Throwable t) {
                Utilities.printLog("Login", t.getMessage());
            }
        });


    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

        switch (textView.getId()) {
            case R.id.etUserPassword:

                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (validate()) {
                        login("");
                    }
                    return true;
                }
                break;

        }

        return false;
    }


    public void clearError() {
        if (etUserName != null) {
            etUserName.setError(null);
            etPassword.setError(null);
        }


    }

    private void openLanguageDialog() {
        if (customLanguageDialog != null && customLanguageDialog.isShowing()) {
            return;
        }
        customLanguageDialog = new CustomLanguageDialog(activity) {
            @Override
            public void onSelect(String languageName, String languageCode) {
                tvLanguage.setText(languageName);
                if (!TextUtils.equals(activity.preferenceHelper.getLanguageCode(),
                        languageCode)) {
                    activity.preferenceHelper.putLanguageCode(languageCode);
                    Language.getInstance().setAdminLanguageIndex(Utilities.
                            getLangIndxex(languageCode, Language.getInstance().getAdminLanguages(),
                                    false));
                    Language.getInstance().setStoreLanguageIndex(Utilities.
                            getLangIndxex(languageCode, Language.getInstance().getStoreLanguages(),
                                    true));
                    activity.finishAffinity();
                    activity.restartApp();
                }
                dismiss();
            }
        };
        customLanguageDialog.show();
    }

    private void setLanguageName() {
        TypedArray array = activity.getResources().obtainTypedArray(R.array.language_code);
        TypedArray array2 = activity.getResources().obtainTypedArray(R.array.language_name);
        int size = array.length();
        for (int i = 0; i < size; i++) {
            if (TextUtils.equals(activity.preferenceHelper.getLanguageCode(), array.getString
                    (i))) {
                tvLanguage.setText(array2.getString(i));
                break;
            }
        }

    }


    private void checkSocialLoginISOn(boolean isSocialLogin) {
        if (isSocialLogin) {
            llSocialLogin.setVisibility(View.VISIBLE);
        } else {
            llSocialLogin.setVisibility(View.GONE);
        }
    }


    private void initStoreLoginOption() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(activity,
                R.array.login_as, R.layout.item_spiner_login_as);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLoginAs.setAdapter(adapter);
    }
}
