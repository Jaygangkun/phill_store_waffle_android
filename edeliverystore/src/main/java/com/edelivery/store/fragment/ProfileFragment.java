package com.edelivery.store.fragment;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.edelivery.store.BankDetailActivity;
import com.edelivery.store.HelpActivity;
import com.edelivery.store.HistoryActivity;
import com.edelivery.store.InstantOrderActivity;
import com.edelivery.store.NotificationActivity;
import com.edelivery.store.PaymentActivity;
import com.edelivery.store.ProductGroupsActivity;
import com.edelivery.store.ProductListActivity;
import com.edelivery.store.PromoCodeActivity;
import com.edelivery.store.ReviewActivity;
import com.edelivery.store.SettingsActivity;
import com.edelivery.store.SpecificationGroupActivity;
import com.edelivery.store.StoreOrderProductActivity;
import com.edelivery.store.SubStoresActivity;
import com.edelivery.store.UpdateProfileActivity;
import com.edelivery.store.adapter.ProfileMenuAdapter;
import com.edelivery.store.component.CustomLanguageDialog;
import com.edelivery.store.models.singleton.Language;
import com.edelivery.store.utils.Constant;
import com.edelivery.store.utils.RecyclerOnItemListener;
import com.edelivery.store.utils.Utilities;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.BuildConfig;
import com.elluminati.edelivery.store.R;

/**
 * Fragment to display menu options on 16-02-2017.
 */

public class ProfileFragment extends BaseFragment implements RecyclerOnItemListener
        .OnItemClickListener, CompoundButton.OnCheckedChangeListener {


    CustomTextView tvLanguage;
    CustomLanguageDialog customLanguageDialog;
    private SwitchCompat switchPushNotificationSound;
    LinearLayout llLanguage, llNotification;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View view = LayoutInflater.from(activity).inflate(R.layout.fragment_profile, container,
                false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        ((TextView) view.findViewById(R.id.tvVersion)).setText(activity.getResources().getString
                (R.string.text_app_version).concat(BuildConfig.VERSION_NAME));
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));


        recyclerView.setAdapter(new ProfileMenuAdapter(activity));
        recyclerView.addOnItemTouchListener(new RecyclerOnItemListener(activity, this));
        switchPushNotificationSound = (SwitchCompat) view.findViewById(R.id
                .switchPushNotificationSound);
        swipeLayoutSetup();
        tvLanguage = (CustomTextView) view.findViewById(R.id.tvLanguage);
        llLanguage = view.findViewById(R.id.llLanguage);
        llNotification = view.findViewById(R.id.llNotification);
        return view;
    }

    private void swipeLayoutSetup() {
        activity.mainSwipeLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R
                .color.colorBlack));
        activity.mainSwipeLayout.setEnabled(false);
        activity.mainSwipeLayout.setOnRefreshListener(null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.toolbar.setElevation(getResources().getDimensionPixelSize(R.dimen
                    .dimen_app_toolbar_elevation));
        }
        switchPushNotificationSound.setChecked(activity.preferenceHelper
                .getIsPushNotificationSoundOn());
        switchPushNotificationSound.setOnCheckedChangeListener(this);
        llLanguage.setOnClickListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            llNotification.setVisibility(View.GONE);
        } else {
            llNotification.setVisibility(View.VISIBLE);
        }

        setLanguageName();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.llLanguage:
                openLanguageDialog();
                break;

            default:
                // do with default
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.switchPushNotificationSound:
                activity.preferenceHelper.putIsPushNotificationSoundOn(isChecked);
                break;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onItemClick(View view, int position) {

        switch (position) {

            case 0:
                activity.startActivity(new Intent(activity, UpdateProfileActivity.class));
                break;

            case 1:
                activity.startActivity(new Intent(activity, HistoryActivity.class));
                break;

            case 2:
                activity.startActivity(new Intent(activity, ProductGroupsActivity.class));
                break;

            case 3:
                activity.startActivity(new Intent(activity, SpecificationGroupActivity.class));
                break;

            case 4:
                activity.goToDocumentActivity(false);
                break;
            case 5:
                goToBankDetailActivity();
                break;
            case 6:
                activity.goToEarningActivity();
                break;
            case 7:
                goToPaymentsActivity();
                break;
            case 8:
                goToProductListActivity();
                break;
            case 9:
                activity.startActivity(new Intent(activity, SettingsActivity.class));
                break;
            case 10:
                goToSubStoreActivity();
                break;
            case 11:
                activity.goToReferralShareActivity();
                break;
            case 12:
                goToStoreOrderProductActivity();
                break;
            case 13:
                goToInstantOrderActivity();
                break;
            case 14:
                goToReviewActivity();
                break;
            case 15:
                goToPromoActivity();
                break;
            case 16:
                goToNotificationActivity();
                break;
            case 17:
                goToHelpActivity();
                break;
            default:
                activity.openLogoutDialog();
                break;
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
        TypedArray array = getResources().obtainTypedArray(R.array.language_code);
        TypedArray array2 = getResources().obtainTypedArray(R.array.language_name);
        int size = array.length();
        for (int i = 0; i < size; i++) {
            if (TextUtils.equals(activity.preferenceHelper.getLanguageCode(), array.getString
                    (i))) {
                tvLanguage.setText(array2.getString(i));
                break;
            }
        }

    }

    private void goToBankDetailActivity() {
        Intent intent = new Intent(activity, BankDetailActivity.class);
        startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void goToPaymentsActivity() {
        Intent intent = new Intent(activity, PaymentActivity.class);
        startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void goToProductListActivity() {
        Intent intent = new Intent(activity, ProductListActivity.class);
        startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void goToStoreOrderProductActivity() {
        Intent intent = new Intent(activity, StoreOrderProductActivity.class);
        intent.putExtra(Constant.IS_ORDER_UPDATE, false);
        startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void goToReviewActivity() {
        Intent intent = new Intent(activity, ReviewActivity.class);
        startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void goToPromoActivity() {
        Intent intent = new Intent(activity, PromoCodeActivity.class);
        startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void goToNotificationActivity() {
        Intent intent = new Intent(activity, NotificationActivity.class);
        startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void goToHelpActivity() {
        Intent intent = new Intent(activity, HelpActivity.class);
        startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void goToInstantOrderActivity() {
        Intent intent = new Intent(activity, InstantOrderActivity.class);
        startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void goToSubStoreActivity() {
        Intent intent = new Intent(activity, SubStoresActivity.class);
        startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
