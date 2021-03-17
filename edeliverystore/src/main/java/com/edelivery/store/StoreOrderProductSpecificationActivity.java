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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edelivery.store.adapter.ProductItemItemAdapter;
import com.edelivery.store.adapter.ProductSpecificationItemAdapter;
import com.edelivery.store.models.datamodel.Item;
import com.edelivery.store.models.datamodel.ItemSpecification;
import com.edelivery.store.models.datamodel.Product;
import com.edelivery.store.models.datamodel.ProductSpecification;
import com.edelivery.store.models.datamodel.CartProductItems;
import com.edelivery.store.models.datamodel.CartProducts;
import com.edelivery.store.models.singleton.CurrentBooking;
import com.edelivery.store.utils.Constant;
import com.edelivery.store.utils.Utilities;
import com.edelivery.store.widgets.CustomInputEditText;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;

import java.util.ArrayList;
import java.util.List;

public class StoreOrderProductSpecificationActivity extends BaseActivity {
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView ivProductImage;
    private CustomInputEditText etAddNote;
    private CustomTextView tvProductName, tvProductDescription, btnDecrease, btnIncrease,
            tvItemQuantity, tvItemAmount;
    private LinearLayout llAddToCart;
    private RecyclerView rcvSpecificationItem;
    private ProductSpecificationItemAdapter productSpecificationItemAdapter;
    private int itemQuantity = 1;
    private double itemPriceAndSpecificationPriceTotal = 0;
    private int cartCount = 0;
    private ArrayList<ItemSpecification> specificationsItems;
    private Item productItemsItem;
    private Product productDetail;
    private ViewPager imageViewPager, imageViewPagerDialog;
    private int requiredCount = 0;
    private LinearLayout llDots, llDotsDialog;
    private TextView[] dots, dots1;
    private NestedScrollView scrollView;
    private CurrentBooking currentBooking = CurrentBooking.getInstance();
    private CustomTextView tvToolbarTitle, tvCartCount;
    private ImageView ivToolbarRightIcon3;

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
        tvCartCount = (CustomTextView) findViewById(R.id.tvCartCount);
        tvCartCount.setVisibility(View.VISIBLE);
        ivToolbarRightIcon3 = (ImageView) findViewById(R.id.ivToolbarRightIcon3);
        ivToolbarRightIcon3.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable
                .ic_shopping_bag));
        ivToolbarRightIcon3.setOnClickListener(this);

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
                addToCart();
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
        reloadAmountData(productItemsItem.getPrice());
        countIsRequiredAndDefaultSelected();
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
        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            productItemsItem = bundle.getParcelable(Constant
                    .PRODUCT_SPECIFICATION);
            productDetail = bundle.getParcelable(Constant.PRODUCT_DETAIL);
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
                        ivToolbarRightIcon3.setImageDrawable(AppCompatResources.getDrawable
                                (StoreOrderProductSpecificationActivity.this, R
                                        .drawable
                                        .ic_shopping_bag_black));
                        toolbar.setNavigationIcon(AppCompatResources.getDrawable
                                (StoreOrderProductSpecificationActivity.this, R.drawable.ic_back));
                        break;
                    case 2:
                        tvToolbarTitle.setTextColor(ResourcesCompat.getColor(getResources(),
                                android.R.color.transparent, null));
                        toolbar.setBackground(AppCompatResources.getDrawable
                                (StoreOrderProductSpecificationActivity.this, R.drawable
                                        .toolbar_transparent));
                        ivToolbarRightIcon3.setImageDrawable(AppCompatResources.getDrawable
                                (StoreOrderProductSpecificationActivity.this, R
                                        .drawable
                                        .ic_shopping_bag));
                        toolbar.setNavigationIcon(AppCompatResources.getDrawable
                                (StoreOrderProductSpecificationActivity.this, R.drawable
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
                            .getRange() && specificationsItem.getMaxRange() == 0 &&
                    specificationsItem.getSelectedCount() != 0) {
                requiredCountTemp++;
            } else if (specificationsItem.isRequired() && specificationsItem.getSelectedCount() >=
                    specificationsItem.getRange()
                    && specificationsItem.getSelectedCount() <= specificationsItem
                    .getMaxRange() && specificationsItem.getSelectedCount() != 0) {
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

    private void reloadAmountData(Double itemAmount) {
        String amount = preferenceHelper.getCurrency() + parseContent.decimalTwoDigitFormat
                .format(itemAmount);
        tvItemAmount.setText(amount);
    }

    private void setCartItem() {
        cartCount = 0;
        for (CartProducts cartProducts : currentBooking.getCartProductList()) {
            cartCount = cartCount + cartProducts.getItems().size();
        }
        tvCartCount.setText(String.valueOf(cartCount));
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
    private void addToCart() {

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


        CartProductItems cartProductItems = new CartProductItems();
        cartProductItems.setItemId(productItemsItem.getId());
        cartProductItems.setUniqueId(productItemsItem.getUniqueId());
        cartProductItems.setItemName(productItemsItem.getName());
        cartProductItems.setQuantity(itemQuantity);
        cartProductItems.setImageUrl(productItemsItem.getImageUrl());
        cartProductItems.setDetails(productItemsItem.getDetails());
        cartProductItems.setSpecifications(specificationList);
        cartProductItems.setTotalSpecificationPrice(specificationPriceTotal);
        cartProductItems.setItemPrice(productItemsItem.getPrice());
        cartProductItems.setItemNote(etAddNote.getText().toString());
        cartProductItems.setTotalItemAndSpecificationPrice(itemPriceAndSpecificationPriceTotal);

        // add new filed 0n 4_Aug_2018

        cartProductItems.setTotalPrice(cartProductItems.getItemPrice() + cartProductItems
                .getTotalSpecificationPrice());
        cartProductItems.setTax(preferenceHelper.getIsUseItemTax() ? productItemsItem.getTax()
                : (double) preferenceHelper.getStoreTax());
        cartProductItems.setItemTax(getTaxableAmount(productItemsItem.getPrice(),
                cartProductItems.getTax()));
        cartProductItems.setTotalSpecificationTax(getTaxableAmount(specificationPriceTotal,
                cartProductItems.getTax()));
        cartProductItems.setTotalTax(cartProductItems.getItemTax() + cartProductItems
                .getTotalSpecificationTax());
        cartProductItems.setTotalItemTax(cartProductItems.getTotalTax() * cartProductItems
                .getQuantity());


        if (isProductExistInLocalCart(cartProductItems)) {
            // do somethings
        } else {
            ArrayList<CartProductItems> cartProductItemsList = new ArrayList<>();
            cartProductItemsList.add(cartProductItems);
            CartProducts cartProducts = new CartProducts();
            cartProducts.setItems(cartProductItemsList);
            cartProducts.setProductId(productItemsItem.getProductId());
            cartProducts.setProductName(productDetail.getName());
            cartProducts.setUniqueId(productDetail.getUniqueId());
            cartProducts.setTotalProductItemPrice(itemPriceAndSpecificationPriceTotal);
            cartProducts.setTotalItemTax(cartProductItems.getTotalItemTax());
            currentBooking.setCartProduct(cartProducts);
        }
        setCartItem();
        Utilities.showToast(this, getResources().getString(R.string.message_952));
        onBackPressed();
    }


    /**
     * this method check product is exist in local cart
     *
     * @param cartProductItems
     * @return true if product exist otherwise false
     */
    private boolean isProductExistInLocalCart(CartProductItems cartProductItems) {
        for (CartProducts cartProducts : currentBooking.getCartProductList()) {
            if (TextUtils.equals(cartProducts.getProductId(), productItemsItem.getProductId())) {
                cartProducts.getItems().add(cartProductItems);
                itemPriceAndSpecificationPriceTotal = cartProducts
                        .getTotalProductItemPrice() +
                        itemPriceAndSpecificationPriceTotal;
                cartProducts.setTotalProductItemPrice
                        (itemPriceAndSpecificationPriceTotal);
                cartProducts.setTotalItemTax(cartProducts.getTotalItemTax() + cartProducts
                        .getTotalItemTax());
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
    protected void onResume() {
        super.onResume();
        setCartItem();
    }

    /**
     * this method return flag according to range selection
     *
     * @param range
     * @param maxRange
     * @param selectedCount
     * @param isSelected
     * @return
     */
    private boolean isValidSelection(int range, int maxRange, int selectedCount, boolean
            isSelected) {
        if (range == 0 && maxRange ==
                0) {
            return !isSelected;
        } else if (selectedCount <= range && maxRange == 0) {
            return range != selectedCount && !isSelected;
        } else if (range >= 0 && selectedCount <= maxRange) {
            return maxRange != selectedCount && !isSelected;
        } else {
            return isSelected;
        }
    }

    private double getTaxableAmount(double amount, double taxValue) {
        return amount * taxValue * 0.01;
    }

}
