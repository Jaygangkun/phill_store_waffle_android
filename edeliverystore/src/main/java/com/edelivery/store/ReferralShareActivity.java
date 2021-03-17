package com.edelivery.store;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.os.Parcelable;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.edelivery.store.utils.Constant;
import com.edelivery.store.utils.Utilities;
import com.edelivery.store.widgets.CustomButton;
import com.edelivery.store.widgets.CustomFontTextViewTitle;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class ReferralShareActivity extends BaseActivity {

    private CustomTextView tvWalletAmount, tvTagWallet, tvTagShare;
    private CustomFontTextViewTitle tvReferralCode;
    private CustomButton btnShareReferral;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referral_share);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbar(toolbar, R.drawable.ic_back, R.color.colorBlack);
        ((TextView) findViewById(R.id.tvToolbarTitle))
                .setText(getIntent().getStringExtra(Constant.TITLE));

        tvWalletAmount = (CustomTextView) findViewById(R.id.tvWalletAmount);
        tvReferralCode = (CustomFontTextViewTitle) findViewById(R.id.tvReferralCode);
        btnShareReferral = (CustomButton) findViewById(R.id.btnShareReferral);
        tvTagWallet = (CustomTextView) findViewById(R.id.tvTagWallet);
        tvTagShare = (CustomTextView) findViewById(R.id.tvTagShare);
        Utilities.setTagBackgroundRtlView(this, tvTagWallet);
        Utilities.setTagBackgroundRtlView(this, tvTagShare);
        btnShareReferral.setOnClickListener(this);
        tvWalletAmount.setText(parseContent.decimalTwoDigitFormat.format(preferenceHelper
                .getWalletAmount()) + " " + preferenceHelper.getWalletCurrencyCode());
        tvReferralCode.setText(preferenceHelper.getReferralCode());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        setToolbarCameraIcon(false);
        setToolbarEditIcon(false, R.drawable.ic_filter);
        return true;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btnShareReferral:
                shareAppAndReferral();
                break;

            default:
                // do with default
                break;
        }
    }

    private void shareAppAndReferral() {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, getResources().getString
                (R.string.msg_use_referral_code) + " " + preferenceHelper
                .getReferralCode());
        startActivity(customChooserIntentNoFb(sharingIntent, getResources().getString
                (R.string.msg_share_referral), this));
    }

    // Method to exclude facebook app from share Intent:
    public static Intent customChooserIntentNoFb(Intent target, String shareTitle, Context context) {

        String[] blacklist = new String[]{"com.facebook.katana"};
        List<Intent> targetedShareIntents = new ArrayList<Intent>();
        List<HashMap<String, String>> intentMetaInfo = new ArrayList<HashMap<String, String>>();
        Intent chooserIntent;

        Intent dummy = new Intent(target.getAction());
        dummy.setType(target.getType());
        List<ResolveInfo> resInfo = context.getPackageManager().queryIntentActivities(dummy, 0);

        if (!resInfo.isEmpty()) {
            for (ResolveInfo resolveInfo : resInfo) {
                if (resolveInfo.activityInfo == null || Arrays.asList(blacklist).contains(resolveInfo.activityInfo.packageName))
                    continue;

                HashMap<String, String> info = new HashMap<String, String>();
                info.put("packageName", resolveInfo.activityInfo.packageName);
                info.put("className", resolveInfo.activityInfo.name);
                info.put("simpleName", String.valueOf(resolveInfo.activityInfo.loadLabel(context.getPackageManager())));
                intentMetaInfo.add(info);
            }

            if (!intentMetaInfo.isEmpty()) {
                // sorting for nice readability
                Collections.sort(intentMetaInfo, new Comparator<HashMap<String, String>>() {
                    @Override
                    public int compare(HashMap<String, String> map, HashMap<String, String> map2) {
                        return map.get("simpleName").compareTo(map2.get("simpleName"));
                    }
                });

                // create the custom intent list
                for (HashMap<String, String> metaInfo : intentMetaInfo) {
                    Intent targetedShareIntent = (Intent) target.clone();
                    targetedShareIntent.setPackage(metaInfo.get("packageName"));
                    targetedShareIntent.setClassName(metaInfo.get("packageName"), metaInfo.get("className"));
                    targetedShareIntents.add(targetedShareIntent);
                }

                chooserIntent = Intent.createChooser(targetedShareIntents.remove(targetedShareIntents.size() - 1), shareTitle);
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[]{}));
                return chooserIntent;
            }
        }
        return Intent.createChooser(target, shareTitle);

    }
}
