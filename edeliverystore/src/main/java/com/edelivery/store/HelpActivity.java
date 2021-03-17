package com.edelivery.store;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edelivery.store.utils.Utilities;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;

public class HelpActivity extends BaseActivity {

    private LinearLayout llMail, llCall;
    private CustomTextView tvTandC, tvPolicy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbar(toolbar, R.drawable.ic_back, R.color.color_app_theme);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });
        ((TextView) findViewById(R.id.tvToolbarTitle)).setText(getString(R.string.text_help));
        llCall = (LinearLayout) findViewById(R.id.llCall);
        llMail = (LinearLayout) findViewById(R.id.llMail);
        tvTandC = (CustomTextView) findViewById(R.id.tvTandC);
        tvPolicy = (CustomTextView) findViewById(R.id.tvPolicy);
        tvTandC.setText(Utilities.fromHtml
                ("<a href=\"" + preferenceHelper.getTermsANdConditions() + "\"" + ">" +
                        getResources().getString(R.string.text_t_and_c) +
                        "</a>"));
        tvTandC.setMovementMethod(LinkMovementMethod.getInstance());
        tvPolicy.setText(Utilities.fromHtml
                ("<a href=\"" + preferenceHelper.getPolicy() + "\"" + ">" +
                        getResources().getString(R.string.text_policy) +
                        "</a>"));
        tvPolicy.setMovementMethod(LinkMovementMethod.getInstance());
        llCall.setOnClickListener(this);
        llMail.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu1) {
        super.onCreateOptionsMenu(menu1);
        menu = menu1;
        setToolbarEditIcon(false, 0);
        setToolbarCameraIcon(false);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llCall:
                makePhoneCallToAdmin();
                break;
            case R.id.llMail:
                contactUsWithAdmin();
                break;
            default:
                // do with default
                break;
        }
    }


    public void contactUsWithAdmin() {
        Uri gmmIntentUri = Uri.parse("mailto:" + preferenceHelper
                .getAdminContactEmail() +
                "?subject=" + "Request to Admin" +
                "&body=" + "Hello sir");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.gm");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            Utilities.showToast(this, getResources().getString(R.string
                    .msg_google_mail_app_not_installed));
        }
    }

    public void makePhoneCallToAdmin() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + preferenceHelper.getAdminContact()));
        startActivity(intent);

    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
