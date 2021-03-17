package com.edelivery.store.component;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.elluminati.edelivery.store.R;
import com.edelivery.store.widgets.CustomTextView;

/**
 * A class to show alter dialog on 07-02-2017.
 */

public abstract class CustomAlterDialog extends Dialog implements View.OnClickListener {

    private String message, positiveBtnText, negativeBtnText;
    private boolean isShowNegativeButton;
    private String title;
    private CustomTextView tvMessage,tvTitle;
    public CustomAlterDialog(Context context,String title, String message) {
        super(context);
        this.message = message;
        this.title = title;
        isShowNegativeButton = false;
    }

    public CustomAlterDialog(Context context,String title, String message, boolean isShowNegativeButton, String positiveBtnText,
                             String negativeBtnText) {
        super(context);
        this.message = message;
        this.title = title;
        this.isShowNegativeButton = isShowNegativeButton;
        this.positiveBtnText = positiveBtnText;
        this.negativeBtnText = negativeBtnText;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button btnPositive, btnNegative;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_alter);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;

        tvMessage = (CustomTextView) findViewById(R.id.tvMessage);
        tvTitle = (CustomTextView) findViewById(R.id.tvTitle);
        btnPositive = (Button) findViewById(R.id.btnPositive);
        btnNegative = (Button) findViewById(R.id.btnNegative);

        tvMessage.setText(message);
        if(TextUtils.isEmpty(title)){
            tvTitle.setVisibility(View.GONE);
        }else {
            tvTitle.setText(title);
        }

        btnPositive.setOnClickListener(this);
        btnNegative.setOnClickListener(this);

        if (!isShowNegativeButton) {
            btnNegative.setVisibility(View.GONE);
        } else {
            btnNegative.setVisibility(View.VISIBLE);
            btnPositive.setText(positiveBtnText);
            btnNegative.setText(negativeBtnText);
        }
    }




    @Override
    public void onClick(View v) {
        btnOnClick(v.getId());
    }

    public abstract void btnOnClick(int btnId);


}
