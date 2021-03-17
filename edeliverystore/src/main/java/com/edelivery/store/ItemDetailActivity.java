package com.edelivery.store;

import android.os.Bundle;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.edelivery.store.adapter.ItemSpecificationAdapter;
import com.edelivery.store.models.datamodel.Item;
import com.edelivery.store.models.datamodel.ItemSpecification;
import com.edelivery.store.utils.Constant;
import com.elluminati.edelivery.store.R;

import java.util.ArrayList;

import static com.elluminati.edelivery.store.BuildConfig.IMAGE_URL;

public class ItemDetailActivity extends BaseActivity {

    private TextView tvItemName, tvItemDetail, tvToolbarTitle, tvSpecificationGroup;
    private ImageView ivItem;
    private ItemSpecificationAdapter itemSpecificationAdapterList;
    private CollapsingToolbarLayout collapsingLayout;
    private NestedScrollView scrollView;
    private ArrayList<ItemSpecification> itemSpecifications=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_product_item_detail);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbar(toolbar, R.drawable.ic_back_white, 0);
        toolbar.setBackground(AppCompatResources.getDrawable(this, R.drawable.toolbar_transparent));
        tvToolbarTitle = (TextView) findViewById(R.id.tvToolbarTitle);
        tvItemName = (TextView) findViewById(R.id.tvName);
        tvItemDetail = (TextView) findViewById(R.id.tvDetail);
        ivItem = (ImageView) findViewById(R.id.iv);
        RecyclerView rcItemSpecification = (RecyclerView) findViewById(R.id.rcSpecification);
        findViewById(R.id.tvAddSpecification).setVisibility(View.GONE);
        findViewById(R.id.llSwitch).setVisibility(View.GONE);
        findViewById(R.id.ivIncludeDivider).setVisibility(View.GONE);
        findViewById(R.id.ivEditSpecification).setVisibility(View.INVISIBLE);

        rcItemSpecification.setNestedScrollingEnabled(false);
        rcItemSpecification.setLayoutManager(new LinearLayoutManager(this));
        itemSpecificationAdapterList = new ItemSpecificationAdapter(this, itemSpecifications);
        rcItemSpecification.setAdapter(itemSpecificationAdapterList);

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appBar);
        setToolbarTitleTextColor(appBarLayout);
        setItemData();
        initCollapsingToolbar();
        findViewById(R.id.tvSpecificationGroup).setVisibility(View.GONE);
        scrollView = (NestedScrollView) findViewById(R.id.scrollViewItem);
        scrollView.getParent().requestChildFocus(scrollView, scrollView);
    }

    private void initCollapsingToolbar() {
        collapsingLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingLayout);
        collapsingLayout.setTitleEnabled(false);
        collapsingLayout.setContentScrimColor(ResourcesCompat.getColor(getResources(), R
                .color.color_app_theme, null));
        collapsingLayout.setStatusBarScrimColor(ResourcesCompat.getColor(getResources(), R
                .color.color_app_theme, null));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        setToolbarEditIcon(false, 0);
        setToolbarCameraIcon(false);
        return true;
    }

    private void setToolbarTitleTextColor(AppBarLayout appBarLayout) {
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    tvToolbarTitle.setTextColor(ResourcesCompat.getColor(getResources(),
                            android.R.color
                                    .transparent, null));
                    toolbar.setBackground(AppCompatResources.getDrawable
                            (ItemDetailActivity.this, R.drawable
                                    .toolbar_transparent));
                    toolbar.setNavigationIcon(AppCompatResources.getDrawable
                            (ItemDetailActivity.this, R.drawable.ic_back_white));

                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    tvToolbarTitle.setTextColor(ContextCompat.getColor(ItemDetailActivity.this, R
                            .color.colorWhite));

                    tvToolbarTitle.setTextColor(ResourcesCompat.getColor(getResources(), R.color
                            .colorBlack, null));
                    toolbar.setBackground(null);
                    toolbar.setNavigationIcon(AppCompatResources.getDrawable
                            (ItemDetailActivity.this, R.drawable.ic_back));
                }
            }
        });
    }

    private void setItemData() {
        if (getIntent() != null && getIntent().getParcelableExtra(Constant.ITEM) != null) {
            Item item = (Item) getIntent().getParcelableExtra(Constant.ITEM);
            itemSpecifications.addAll(item.getSpecifications());

            tvItemName.setText(item.getName());
            tvItemDetail.setText(item.getDetails());
            if (item.getImageUrl() != null && !item.getImageUrl().isEmpty()) {
                Glide.with(this).load(IMAGE_URL + item.getImageUrl().get(0)).into(ivItem);
            }
            itemSpecificationAdapterList.notifyDataSetChanged();
            tvToolbarTitle.setText(item.getName());
        }
    }
}
