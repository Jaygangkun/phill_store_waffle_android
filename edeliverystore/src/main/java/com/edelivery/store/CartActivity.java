package com.edelivery.store;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edelivery.store.adapter.CartProductAdapter;
import com.edelivery.store.models.datamodel.CartProductItems;
import com.edelivery.store.models.datamodel.CartProducts;
import com.edelivery.store.models.singleton.CurrentBooking;
import com.edelivery.store.utils.Utilities;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;

public class CartActivity extends BaseActivity {
    private RecyclerView rcvCart;
    private CustomTextView tvCartTotal, btnSubmit;
    private LinearLayout btnCheckOut;
    private CartProductAdapter cartProductAdapter;
    private LinearLayout ivEmpty;
    private double totalAmount = 0;
    private CustomTextView tvtoolbarbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbar(toolbar, R.drawable.ic_back, R.color.color_app_theme);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ((TextView) findViewById(R.id.tvToolbarTitle)).setText(getString(R.string
                .text_cart));
        tvtoolbarbtn = (CustomTextView) findViewById(R.id.tvtoolbarbtn);
        tvtoolbarbtn.setText(getString(R.string.text_remove_all));

        rcvCart = (RecyclerView) findViewById(R.id.rcvCart);
        tvCartTotal = (CustomTextView) findViewById(R.id.tvCartTotal);
        btnCheckOut = (LinearLayout) findViewById(R.id.btnCheckOut);
        ivEmpty = (LinearLayout) findViewById(R.id.ivEmpty);
        btnCheckOut.setOnClickListener(this);
        btnSubmit = (CustomTextView) findViewById(R.id.btnSubmit);
        btnSubmit.setText(getResources().getString(R.string.text_checkout));
        initRcvCart();

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
    protected void onResume() {
        super.onResume();
        updateUiCartList();
    }

    @Override
    public void onClick(View view) {
        // do something
        switch (view.getId()) {
            case R.id.btnCheckOut:
                if (CurrentBooking.getInstance().getCartProductList().isEmpty()) {
                    Utilities.showToast(this, getResources().getString(R.string.msg_no_in_cart));
                } else {
                    goToCheckoutActivity();
                }

                break;
            case R.id.tvtoolbarbtn:
                clearCart();
                break;
            default:
                // do with default
                break;
        }
    }

    private void initRcvCart() {
        cartProductAdapter = new CartProductAdapter(this, CurrentBooking.getInstance()
                .getCartProductList());
        rcvCart.setLayoutManager(new LinearLayoutManager(this));
        rcvCart.setAdapter(cartProductAdapter);
        modifyTotalAmount();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    /**
     * this method id used to increase particular item quantity in cart
     *
     * @param cartProductItems
     */
    public void increaseItemQuantity(CartProductItems cartProductItems) {
        int quantity = cartProductItems.getQuantity();
        quantity++;
        cartProductItems.setQuantity(quantity);
        cartProductItems.setTotalItemAndSpecificationPrice((cartProductItems
                .getTotalSpecificationPrice()
                + cartProductItems.getItemPrice()) * quantity);
        cartProductItems.setTotalItemTax(cartProductItems.getTotalTax() * quantity);
        cartProductAdapter.notifyDataSetChanged();
        modifyTotalAmount();
    }

    /**
     * this method id used to decrease particular item quantity in cart
     *
     * @param cartProductItems
     */
    public void decreaseItemQuantity(CartProductItems cartProductItems) {
        int quantity = cartProductItems.getQuantity();
        if (quantity > 1) {
            quantity--;
            cartProductItems.setQuantity(quantity);
            cartProductItems.setTotalItemAndSpecificationPrice((cartProductItems
                    .getTotalSpecificationPrice()
                    + cartProductItems.getItemPrice()) * quantity);
            cartProductItems.setTotalItemTax(cartProductItems.getTotalTax() * quantity);
            cartProductAdapter.notifyDataSetChanged();
            modifyTotalAmount();
        }
    }

    /**
     * this method id used to remove particular item quantity in cart
     *
     * @param position
     * @param relativePosition
     */
    public void removeItem(int position, int relativePosition) {
        CurrentBooking.getInstance().getCartProductList().get(position).getItems().remove
                (relativePosition);
        if (CurrentBooking.getInstance().getCartProductList().get(position).getItems().isEmpty()) {
            CurrentBooking.getInstance().getCartProductList().remove(position);
        }
        updateUiCartList();
        cartProductAdapter.notifyDataSetChanged();
        modifyTotalAmount();
    }


    /**
     * this method id used to modify or change  total cart item  amount
     */
    public void modifyTotalAmount() {
        totalAmount = 0;
        for (CartProducts cartProducts : CurrentBooking.getInstance().getCartProductList()) {
            for (CartProductItems cartProductItems : cartProducts.getItems()) {
                totalAmount = totalAmount + cartProductItems.getTotalItemAndSpecificationPrice();
            }

        }
        CurrentBooking.getInstance().setTotalCartAmount(totalAmount);
        tvCartTotal.setText(preferenceHelper.getCurrency() + parseContent
                .decimalTwoDigitFormat
                .format(totalAmount));
        updateUiCartList();
    }


    private void goToCheckoutActivity() {
        Intent intent = new Intent(this, CheckoutActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    /**
     * this method called a webservice for clear cart
     */
    protected void clearCart() {
        CurrentBooking.getInstance().clearCart();
        cartProductAdapter.notifyDataSetChanged();
        tvtoolbarbtn.setVisibility(View.GONE);
        updateUiCartList();
    }


    private void updateUiCartList() {
        if (CurrentBooking.getInstance().getCartProductList().isEmpty()) {
            ivEmpty.setVisibility(View.VISIBLE);
            rcvCart.setVisibility(View.GONE);
            tvtoolbarbtn.setVisibility(View.GONE);
            tvtoolbarbtn.setOnClickListener(null);
        } else {
            ivEmpty.setVisibility(View.GONE);
            rcvCart.setVisibility(View.VISIBLE);
            tvtoolbarbtn.setVisibility(View.VISIBLE);
            tvtoolbarbtn.setOnClickListener(this);
        }

    }

}
