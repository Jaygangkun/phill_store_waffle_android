package com.edelivery.store;

import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.edelivery.store.adapter.ViewPagerAdapter;
import com.edelivery.store.fragment.MassNotificationFragment;
import com.edelivery.store.fragment.OrderNotificationFragment;
import com.elluminati.edelivery.store.R;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

public class NotificationActivity extends BaseActivity {
    public ViewPager notificationViewpager;
    public ViewPagerAdapter adapter;
    private TabLayout notificationTabsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(getResources().getDimension(R.dimen
                    .dimen_app_tab_elevation));
        }
        setToolbar(toolbar, R.drawable.ic_back, R.color.color_app_theme);
        ((TextView) findViewById(R.id.tvToolbarTitle)).setText(getString(R.string
                .text_notification));
        notificationTabsLayout = (TabLayout) findViewById(R.id.notificationTabsLayout);
        notificationViewpager = (ViewPager) findViewById(R.id.notificationViewpager);
        initTabLayout();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        setToolbarCameraIcon(false);
        setToolbarEditIcon(false, 0);
        return true;
    }

    @Override
    public void onClick(View view) {
        // do somethings

    }


    private void initTabLayout() {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OrderNotificationFragment(),
                getResources().getString(R.string.text_order_notification));
        adapter.addFragment(new MassNotificationFragment(), getResources().getString(R.string
                .text_mass_notification));
        notificationViewpager.setAdapter(adapter);
        notificationTabsLayout.setupWithViewPager(notificationViewpager);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}