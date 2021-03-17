package com.edelivery.store.component;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.edelivery.store.widgets.CustomInputEditText;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;

/**
 * A Dialog class to display dialog with editText which is use in forgot passwordand  otp
 * verification on 08-02-2017.
 */

public abstract class CustomEditTextDialog extends Dialog implements View.OnClickListener {
    public CustomInputEditText etSMSOtp, etEmailOtp;
    public TextInputLayout textInputLayoutSMSOtp, textInputLayoutEmailOtp;
    public String message, textBtnOk, textBtnCancel;
    private int otpVerification;
    private Context context;
    private String title;


    public CustomEditTextDialog(Context context, String title, String message, String textBtnOk,
                                String textBtnCancel, int otpVerification) {
        super(context);
        this.message = message;
        this.otpVerification = otpVerification;
        this.context = context;
        this.textBtnOk = textBtnOk;
        this.textBtnCancel = textBtnCancel;
        this.title = title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button btnPositive, btnNegative;
        TextView tvTitle;

        CustomTextView tvDialogTitle;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_edittext);


        tvTitle = (TextView) findViewById(R.id.tvMessage);
        btnPositive = (Button) findViewById(R.id.btnPositive);
        btnNegative = (Button) findViewById(R.id.btnNegative);
        etEmailOtp = (CustomInputEditText) findViewById(R.id.etEmailOtp);
        etSMSOtp = (CustomInputEditText) findViewById(R.id.etSMSOtp);
        tvDialogTitle = (CustomTextView) findViewById(R.id.tvDialogTitle);
        textInputLayoutEmailOtp = (TextInputLayout) findViewById(R.id.textInputLayoutEmailOtp);
        textInputLayoutSMSOtp = (TextInputLayout) findViewById(R.id.textInputLayoutSMSOtp);
        if (!TextUtils.isEmpty(title)) {
            tvDialogTitle.setVisibility(View.VISIBLE);
            tvDialogTitle.setText(title);
        }

        if (otpVerification == 0) {
            textInputLayoutEmailOtp.setVisibility(View.VISIBLE);
            etEmailOtp.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            textInputLayoutSMSOtp.setVisibility(View.GONE);
        } else if (otpVerification == 1) {
            etEmailOtp.setInputType(InputType.TYPE_CLASS_TEXT | InputType
                    .TYPE_NUMBER_VARIATION_PASSWORD);
            textInputLayoutEmailOtp.setVisibility(View.VISIBLE);
            textInputLayoutSMSOtp.setVisibility(View.GONE);
        } else if (otpVerification == 2) {
            textInputLayoutSMSOtp.setVisibility(View.VISIBLE);
            textInputLayoutEmailOtp.setVisibility(View.GONE);
        } else if (otpVerification == 3) {
            etEmailOtp.setInputType(InputType.TYPE_CLASS_TEXT | InputType
                    .TYPE_NUMBER_VARIATION_PASSWORD);
            textInputLayoutSMSOtp.setVisibility(View.VISIBLE);
            textInputLayoutEmailOtp.setVisibility(View.VISIBLE);
        } else {
            textInputLayoutEmailOtp.setVisibility(View.VISIBLE);
            textInputLayoutEmailOtp.setHint(context.getResources().getString(R.string.text_email));
            etEmailOtp.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            textInputLayoutSMSOtp.setVisibility(View.GONE);
        }
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;

        btnPositive.setOnClickListener(this);
        findViewById(R.id.btnNegative).setOnClickListener(this);

        tvTitle.setText(message);

        btnPositive.setText(textBtnOk);
        btnNegative.setText(textBtnCancel);
    }

    @Override
    public void onClick(View v) {
        btnOnClick(v.getId(), etSMSOtp, etEmailOtp);
    }

    public abstract void btnOnClick(int btnId, TextInputEditText etSMSOtp, TextInputEditText
            etEmailOtp);
}
