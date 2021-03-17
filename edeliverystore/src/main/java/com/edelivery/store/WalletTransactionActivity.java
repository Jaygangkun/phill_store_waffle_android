package com.edelivery.store;

import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.edelivery.store.adapter.ViewPagerAdapter;
import com.edelivery.store.fragment.WalletHistoryFragment;
import com.edelivery.store.fragment.WalletTransactionFragment;
import com.edelivery.store.utils.Utilities;
import com.elluminati.edelivery.store.R;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

public class WalletTransactionActivity extends BaseActivity {
    private TabLayout orderHistoryTabsLayout;
    private ViewPagerAdapter viewPagerAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_transaction);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbar(toolbar, R.drawable.ic_back, R.color.color_app_theme);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utilities.hideSoftKeyboard(WalletTransactionActivity.this);
                onBackPressed();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(getResources().getDimension(R.dimen
                    .dimen_app_tab_elevation));
        }
        ((TextView) findViewById(R.id.tvToolbarTitle)).setText(getString(R.string
                .text_history));
        orderHistoryTabsLayout = (TabLayout) findViewById(R.id
                .transTabsLayout);
        viewPager = (ViewPager) findViewById(R.id.transViewpager);
        initTabLayout();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        setToolbarEditIcon(false, 0);
        setToolbarCameraIcon(false);
        return true;
    }
    private void initTabLayout() {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new WalletHistoryFragment(), getResources()
                .getString(R.string.text_wallet_history));
        viewPagerAdapter.addFragment(new WalletTransactionFragment(), getResources()
                .getString(R.string.text_wallet_transaction));
        viewPager.setAdapter(viewPagerAdapter);
        orderHistoryTabsLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
