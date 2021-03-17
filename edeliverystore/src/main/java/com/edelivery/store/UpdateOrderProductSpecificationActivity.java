package com.edelivery.store;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edelivery.store.adapter.ProductItemItemAdapter;
import com.edelivery.store.adapter.ProductSpecificationItemAdapter;
import com.edelivery.store.models.datamodel.Item;
import com.edelivery.store.models.datamodel.ItemSpecification;
import com.edelivery.store.models.datamodel.OrderDetails;
import com.edelivery.store.models.datamodel.Product;
import com.edelivery.store.models.datamodel.ProductSpecification;
import com.edelivery.store.models.singleton.UpdateOrder;
import com.edelivery.store.utils.Constant;
import com.edelivery.store.widgets.CustomInputEditText;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;

import java.util.ArrayList;
import java.util.List;

public class UpdateOrderProductSpecificationActivity extends BaseActivity {

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private CustomInputEditText etAddNote;
    private CustomTextView tvProductName, tvProductDescription, btnDecrease, btnIncrease,
            tvItemQuantity, tvItemAmount;
    private LinearLayout llAddToCart;
    private RecyclerView rcvSpecificationItem;
    private ProductSpecificationItemAdapter productSpecificationItemAdapter;
    private int itemQuantity = 1;
    private double itemPriceAndSpecificationPriceTotal = 0;
    private ArrayList<ItemSpecification> specificationsItems;
    private Item productItemsItem;
    private Product productDetail;
    private ViewPager imageViewPager, imageViewPagerDialog;
    private int requiredCount = 0;
    private LinearLayout llDots, llDotsDialog;
    private TextView[] dots, dots1;
    private NestedScrollView scrollView;
    private CustomTextView tvToolbarTitle;
    private int updateItemIndex = -1;
    private int updateItemSectionIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_order_product_specification);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbar(toolbar, R.drawable.ic_back_white, R.color.color_app_theme);
        toolbar.setBackground(AppCompatResources.getDrawable(this, R.drawable.toolbar_transparent));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        tvToolbarTitle = (CustomTextView) findViewById(R.id.tvToolbarTitle);

        tvToolbarTitle.setTextColor(ResourcesCompat.getColor(getResources(),
                android.R.color.transparent, null));
        loadExtraData();
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id
                .specificationCollapsingToolBar);
        tvProductDescription = (CustomTextView) findViewById(R.id
                .tvCollapsingProductDescription);
        tvProductName = (CustomTextView) findViewById(R.id.tvCollapsingProductName);
        rcvSpecificationItem = (RecyclerView) findViewById(R.id.rcvSpecificationItem);
        btnIncrease = (CustomTextView) findViewById(R.id.btnIncrease);
        btnDecrease = (CustomTextView) findViewById(R.id.btnDecrease);
        tvItemQuantity = (CustomTextView) findViewById(R.id.tvItemQuantity);
        llAddToCart = (LinearLayout) findViewById(R.id.llAddToCart);
        tvItemAmount = (CustomTextView) findViewById(R.id.tvItemAmount);
        imageViewPager = (ViewPager) findViewById(R.id.imageViewPager);
        llDots = (LinearLayout) findViewById(R.id.llDots);
        scrollView = (NestedScrollView) findViewById(R.id.scrollView);
        etAddNote = (CustomInputEditText) findViewById(R.id.etAddNote);
        btnDecrease.setOnClickListener(this);
        btnIncrease.setOnClickListener(this);
        initImagePager();
        initAppBar();
        initCollapsingToolbar();
        setData(itemQuantity);
        loadProductSpecification();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu1) {
        super.onCreateOptionsMenu(menu1);
        menu = menu1;
        setToolbarEditIcon(false, R.drawable.filter_store);
        setToolbarCameraIcon(false);
        return true;
    }

    @Override
    public void onClick(View view) {
        //do somethings

        switch (view.getId()) {
            case R.id.llAddToCart:
                addOrUpdateOrder();
                break;
            case R.id.btnIncrease:
                increaseItemQuality();
                break;
            case R.id.btnDecrease:
                decreaseItemQuantity();
                break;
            case R.id.ivToolbarRightIcon3:
                goToCartActivity();
                break;
            default:
                // do with default
                break;
        }
    }

    private void loadProductSpecification() {
        tvProductName.setText(productItemsItem.getName());
        tvProductDescription.setText(productItemsItem.getDetails());
        specificationsItems = productItemsItem.getSpecifications();
        countIsRequiredAndDefaultSelected();
        reloadAmountData(productItemsItem.getPrice());

        modifyTotalItemAmount();
        productSpecificationItemAdapter = new ProductSpecificationItemAdapter(this,
                specificationsItems) {
            @Override
            public void onSingleItemClick(int section, int relativePosition, int absolutePosition) {
                onSingleItemSelect(section, relativePosition, absolutePosition);
            }

            @Override
            public void onMultipleItemClick() {
                modifyTotalItemAmount();
            }
        };
        rcvSpecificationItem.setLayoutManager(new LinearLayoutManager(this));
        rcvSpecificationItem.setNestedScrollingEnabled(false);
        rcvSpecificationItem.setAdapter(productSpecificationItemAdapter);
        scrollView.getParent().requestChildFocus(scrollView, scrollView);
        tvToolbarTitle.setText(productItemsItem.getName());
    }

    private void addBottomDots(int currentPage) {
        if (productItemsItem.getImageUrl().size() > 1) {
            dots = new TextView[productItemsItem
                    .getImageUrl().size()];

            llDots.removeAllViews();
            for (int i = 0; i < dots.length; i++) {
                dots[i] = new TextView(this);
                dots[i].setText(Html.fromHtml("&#8226;"));
                dots[i].setTextSize(35);
                dots[i].setTextColor(ResourcesCompat.getColor(getResources(), R.color
                                .color_app_label
                        , null));
                llDots.addView(dots[i]);
            }

            if (dots.length > 0)
                dots[currentPage].setTextColor(ResourcesCompat.getColor(getResources(), R.color
                                .color_app_text
                        , null));
        }


    }

    private void dotColorChange(int currentPage) {
        if (llDots.getChildCount() > 0) {
            for (int i = 0; i < llDots.getChildCount(); i++) {
                TextView textView = (TextView) llDots.getChildAt(i);
                textView.setTextColor(ResourcesCompat.getColor(getResources(), R.color
                                .color_app_label
                        , null));

            }
            TextView textView = (TextView) llDots.getChildAt(currentPage);
            textView.setTextColor(ResourcesCompat.getColor(getResources(), R.color.color_app_text
                    , null));
        }


    }

    private void addBottomDotsDialog(int currentPage) {
        if (productItemsItem.getImageUrl().size() > 1) {
            dots1 = new TextView[productItemsItem
                    .getImageUrl().size()];

            llDotsDialog.removeAllViews();
            for (int i = 0; i < dots1.length; i++) {
                dots1[i] = new TextView(this);
                dots1[i].setText(Html.fromHtml("&#8226;"));
                dots1[i].setTextSize(35);
                dots1[i].setTextColor(ResourcesCompat.getColor(getResources(), R.color
                                .color_app_label
                        , null));
                llDotsDialog.addView(dots1[i]);
            }

            if (dots1.length > 0)
                dots1[currentPage].setTextColor(ResourcesCompat.getColor(getResources(), R.color
                                .color_app_text
                        , null));
        }


    }

    private void dotColorChangeDialog(int currentPage) {
        if (llDotsDialog.getChildCount() > 0) {
            for (int i = 0; i < llDotsDialog.getChildCount(); i++) {
                TextView textView = (TextView) llDotsDialog.getChildAt(i);
                textView.setTextColor(ResourcesCompat.getColor(getResources(), R.color
                                .color_app_label
                        , null));

            }
            TextView textView = (TextView) llDotsDialog.getChildAt(currentPage);
            textView.setTextColor(ResourcesCompat.getColor(getResources(), R.color.color_app_text
                    , null));
        }


    }

    private int getItem(int i) {
        return imageViewPager.getCurrentItem() + i;
    }

    private void initImagePager() {
        addBottomDots(0);
        ProductItemItemAdapter itemItemAdapter = new ProductItemItemAdapter(this, productItemsItem
                .getImageUrl(), R.layout.item_image_product, true) {
            @Override
            public void onItemClick(int position) {
                openDialogItemImage(position);
            }
        };
        imageViewPager.setAdapter(itemItemAdapter);
        imageViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int
                    positionOffsetPixels) {
                dotColorChange(position);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void loadExtraData() {
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            updateItemIndex = bundle.getInt(Constant.UPDATE_ITEM_INDEX);
            updateItemSectionIndex = bundle.getInt(Constant.UPDATE_ITEM_INDEX_SECTION);
            if (updateItemIndex > -1) {
                productItemsItem = bundle.getParcelable(Constant.ITEM);
                productDetail = new Product();
                productDetail.setName(UpdateOrder.getInstance().getOrderDetails().get
                        (updateItemSectionIndex).getProductName());
                productDetail.setUniqueId(UpdateOrder.getInstance().getOrderDetails().get
                        (updateItemSectionIndex).getUniqueId());
                itemQuantity = UpdateOrder.getInstance().getOrderDetails().get
                        (updateItemSectionIndex).getItems().get(updateItemIndex).getQuantity();

            } else {
                productItemsItem = bundle.getParcelable(Constant
                        .PRODUCT_SPECIFICATION);
                productDetail = bundle.getParcelable(Constant.PRODUCT_DETAIL);
            }


        }
    }

    private void initCollapsingToolbar() {
        collapsingToolbarLayout.setTitleEnabled(false);
        collapsingToolbarLayout.setContentScrimColor(ResourcesCompat.getColor(getResources(), R
                .color.color_app_theme, null));
        collapsingToolbarLayout.setStatusBarScrimColor(ResourcesCompat.getColor(getResources(), R
                .color.color_app_theme, null));

    }

    private void initAppBar() {

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        tvToolbarTitle.setTextColor(ResourcesCompat.getColor(getResources(), R.color
                                .colorBlack, null));
                        toolbar.setBackground(null);
                        toolbar.setNavigationIcon(AppCompatResources.getDrawable
                                (UpdateOrderProductSpecificationActivity.this, R.drawable.ic_back));
                        break;
                    case 2:
                        tvToolbarTitle.setTextColor(ResourcesCompat.getColor(getResources(),
                                android.R.color.transparent, null));
                        toolbar.setBackground(AppCompatResources.getDrawable
                                (UpdateOrderProductSpecificationActivity.this, R.drawable
                                        .toolbar_transparent));
                        toolbar.setNavigationIcon(AppCompatResources.getDrawable
                                (UpdateOrderProductSpecificationActivity.this, R.drawable
                                        .ic_back_white));
                        break;
                    default:
                        // do with default
                        break;
                }
            }
        };
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.specificationAppBarLayout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    handler.sendEmptyMessage(1);
                    isShow = true;
                } else if (isShow) {
                    handler.sendEmptyMessage(2);
                    isShow = false;
                }
            }
        });
    }

    /**
     * this method will manage total amount after change or modify
     */
    public void modifyTotalItemAmount() {
        itemPriceAndSpecificationPriceTotal = productItemsItem.getPrice();
        int requiredCountTemp = 0;
        for (ItemSpecification specificationsItem : specificationsItems) {
            for (ProductSpecification listItem : specificationsItem.getList()) {
                if (listItem.isIsDefaultSelected()) {
                    itemPriceAndSpecificationPriceTotal = itemPriceAndSpecificationPriceTotal +
                            listItem.getPrice();
                }
            }
            if (specificationsItem.isRequired() && specificationsItem.getSelectedCount() >=
                    specificationsItem
                            .getRange() && specificationsItem.getMaxRange() == 0) {
                requiredCountTemp++;
            } else if (specificationsItem.isRequired() && specificationsItem.getSelectedCount() >=
                    specificationsItem.getRange()
                    && specificationsItem.getSelectedCount() <= specificationsItem
                    .getMaxRange()) {
                requiredCountTemp++;
            }
        }
        if (requiredCountTemp == requiredCount) {
            llAddToCart.setOnClickListener(this);
            llAddToCart.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color
                    .color_app_button, null));
        } else {
            llAddToCart.setOnClickListener(null);
            llAddToCart.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color
                    .color_app_gray_trans, null));
        }

        itemPriceAndSpecificationPriceTotal = itemPriceAndSpecificationPriceTotal * itemQuantity;
        reloadAmountData(itemPriceAndSpecificationPriceTotal);
    }


    private void countIsRequiredAndDefaultSelected() {
        for (ItemSpecification specificationsItem : specificationsItems) {
            if (specificationsItem.isRequired()) {
                requiredCount++;
            }
            for (ProductSpecification specificationSubItem : specificationsItem.getList()) {
                if (specificationSubItem.isIsDefaultSelected()) {
                    specificationsItem.setSelectedCount(specificationsItem.getSelectedCount() + 1);
                }
            }
            specificationsItem.setChooseMessage(getChooseMessage(specificationsItem.getRange(),
                    specificationsItem.getMaxRange()));
        }
    }

    private void reloadAmountData(Double itemAmount) {
        String amount = preferenceHelper.getCurrency() + parseContent.decimalTwoDigitFormat
                .format(itemAmount);
        tvItemAmount.setText(amount);
    }


    private void increaseItemQuality() {
        itemQuantity++;
        setData(itemQuantity);
        modifyTotalItemAmount();
    }

    private void decreaseItemQuantity() {
        if (itemQuantity > 1) {
            itemQuantity--;
            setData(itemQuantity);
            modifyTotalItemAmount();
        }

    }

    private void setData(int itemQuantity) {
        tvItemQuantity.setText(String.valueOf(itemQuantity));
    }

    protected void goToCartActivity() {
        Intent intent = new Intent(this, CartActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    /**
     * this method to create cart structure witch is help to add item in cart
     */
    private void addOrUpdateOrder() {

        double specificationPriceTotal = 0;
        double specificationPrice = 0;

        ArrayList<ItemSpecification> specificationList = new ArrayList<>();
        for (ItemSpecification specificationListItem : specificationsItems) {
            ArrayList<ProductSpecification> specificationItemCartList = new ArrayList<>();
            for (ProductSpecification listItem : specificationListItem.getList()) {
                if (listItem.isIsDefaultSelected()) {
                    listItem.setNameListToString();
                    specificationPrice = specificationPrice + listItem.getPrice();
                    specificationPriceTotal = specificationPriceTotal + listItem.getPrice();
                    specificationItemCartList.add(listItem);
                }
            }

            if (!specificationItemCartList.isEmpty()) {
                ItemSpecification specificationsItem = new ItemSpecification();
                specificationsItem.setList(specificationItemCartList);
                specificationsItem.setName(specificationListItem.getName());
                specificationsItem.setPrice(specificationPrice);
                specificationsItem.setType(specificationListItem.getType());
                specificationsItem.setUniqueId(specificationListItem.getUniqueId());
                specificationList.add(specificationsItem);
            }
            specificationPrice = 0;
        }


        Item items = new Item();
        items.setItemId(productItemsItem.getId());
        items.setUniqueId(productItemsItem.getUniqueId());
        items.setItemName(productItemsItem.getName());
        items.setQuantity(itemQuantity);
        items.setImageUrl(productItemsItem.getImageUrl());
        items.setDetails(productItemsItem.getDetails());
        items.setSpecifications(specificationList);
        items.setItemPrice(productItemsItem.getPrice());
        items.setTotalSpecificationPrice(specificationPriceTotal);
        items.setNoteForItem("");
        items.setTotalItemAndSpecificationPrice(itemPriceAndSpecificationPriceTotal);

        // add new filed 0n 4_Aug_2018

        items.setTotalPrice(items.getItemPrice() + items
                .getTotalSpecificationPrice());
        items.setTax(preferenceHelper.getIsUseItemTax() ? productItemsItem.getTax()
                : (double) preferenceHelper.getStoreTax());
        items.setItemTax(getTaxableAmount(productItemsItem.getPrice(),
                items.getTax()));
        items.setTotalSpecificationTax(getTaxableAmount(specificationPriceTotal,
                items.getTax()));
        items.setTotalTax(items.getItemTax() + items
                .getTotalSpecificationTax());
        items.setTotalItemTax(items.getTotalTax() * items
                .getQuantity());

        if (updateItemIndex > -1) {
            UpdateOrder.getInstance().getOrderDetails().get(updateItemSectionIndex).getItems()
                    .set(updateItemIndex, items);

        } else {
            productItemsItem.setItemId(productItemsItem.getId());
            UpdateOrder.getInstance().setSaveNewItem(productItemsItem);
            if (isProductExistInOrder(items)) {
                // do somethings
            } else {
                ArrayList<Item> itemsList = new ArrayList<>();
                itemsList.add(items);
                OrderDetails orderDetails = new OrderDetails();
                orderDetails.setItems(itemsList);
                orderDetails.setProductId(productItemsItem.getProductId());
                orderDetails.setProductName(productDetail.getName());
                orderDetails.setUniqueId(productDetail.getUniqueId());
                UpdateOrder.getInstance().getOrderDetails().add(orderDetails);
            }
        }
//        Utilities.showToast(this, getResources().getString(R.string
// .msg_order_update_successfully));
        onBackPressed();
    }

    /**
     * this method check product is exist in local cart
     *
     * @param cartProductItems
     * @return true if product exist otherwise false
     */
    private boolean isProductExistInOrder(Item cartProductItems) {
        for (OrderDetails orderDetails : UpdateOrder.getInstance().getOrderDetails()) {
            if (TextUtils.equals(orderDetails.getProductId(), productItemsItem.getProductId())) {
                orderDetails.getItems().add(cartProductItems);
                return true;
            }
        }
        return false;
    }

    /**
     * this method manag single type specification click event
     *
     * @param section
     * @param position
     * @param absolutePosition
     */
    public void onSingleItemSelect(int section, int position, int
            absolutePosition) {
        List<ProductSpecification> specificationListItems = specificationsItems.get(section)
                .getList();
        for (ProductSpecification specificationListItem : specificationListItems) {
            specificationListItem.setIsDefaultSelected(false);
        }
        specificationListItems.get(position).setIsDefaultSelected(true);
        specificationsItems.get(section).setSelectedCount(1);
        productSpecificationItemAdapter.notifyItemRangeChanged(section,
                productSpecificationItemAdapter.getItemCount());
        modifyTotalItemAmount();
    }


    public void openDialogItemImage(int position) {

        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_item_image);
        llDotsDialog = (LinearLayout) dialog.findViewById(R.id.llDotsDialog);
        imageViewPagerDialog = (ViewPager) dialog.findViewById(R.id.dialogImageViewPager);
        addBottomDotsDialog(position);
        ProductItemItemAdapter itemItemAdapter = new ProductItemItemAdapter(this, productItemsItem
                .getImageUrl(), R.layout.item_image_full, false) {
            @Override
            public void onItemClick(int position) {

            }
        };
        imageViewPagerDialog.setAdapter(itemItemAdapter);
        imageViewPagerDialog.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int
                    positionOffsetPixels) {
                dotColorChangeDialog(position);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        imageViewPagerDialog.setCurrentItem(position);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(params);
        dialog.show();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }

    private double getTaxableAmount(double amount, double taxValue) {
        return amount * taxValue * 0.01;
    }

    private String getChooseMessage(int startRange, int maxRange) {
        if (maxRange == 0 && startRange > 0) {
            return getResources().getString(R.string.text_choose,
                    startRange);
        } else if (startRange > 0 && maxRange > 0) {
            return getResources().getString(R.string.text_choose_to,
                    startRange, maxRange);
        } else if (startRange == 0 && maxRange > 0) {
            return getResources().getString(R.string.text_choose_up_to,
                    maxRange);
        } else {
            return "";
        }
    }
}
