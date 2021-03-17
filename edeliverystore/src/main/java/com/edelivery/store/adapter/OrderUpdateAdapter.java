package com.edelivery.store.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edelivery.store.UpdateOrderActivity;
import com.edelivery.store.models.datamodel.Item;
import com.edelivery.store.models.datamodel.OrderDetails;
import com.edelivery.store.parse.ParseContent;
import com.edelivery.store.utils.PreferenceHelper;
import com.edelivery.store.utils.SectionedRecyclerViewAdapter;
import com.edelivery.store.widgets.CustomFontTextViewTitle;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by elluminati on 16-Feb-17.
 */

public class OrderUpdateAdapter extends SectionedRecyclerViewAdapter<RecyclerView
        .ViewHolder> {
    private List<OrderDetails> cartProductList;
    private UpdateOrderActivity activity;
    private ParseContent parseContent;


    public OrderUpdateAdapter(UpdateOrderActivity activity, List<OrderDetails>
            cartProductList) {
        this.activity = activity;
        this.cartProductList = cartProductList;
        parseContent = ParseContent.getParseContentInstance();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case VIEW_TYPE_HEADER:
                return new CartHeaderHolder(LayoutInflater.from(parent.getContext()).inflate(R
                                .layout
                                .layout_divider_horizontal,
                        parent, false));
            case VIEW_TYPE_ITEM:
                return new CartHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout
                                .item_cart_product_item,
                        parent, false));
            default:
                // do with default
                break;
        }

        return null;
    }

    @Override
    public int getSectionCount() {
        return cartProductList == null ? 0 : cartProductList.size();
    }

    @Override
    public int getItemCount(int section) {
        return cartProductList.get(section).getItems().size();
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int section) {
        CartHeaderHolder cartHeaderHolder = (CartHeaderHolder) holder;
        cartHeaderHolder.itemView.setVisibility(View.GONE);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int section, final int
            relativePosition, int absolutePosition) {

        CartHolder cartHolder = (CartHolder) holder;
        OrderDetails cartProducts = cartProductList.get(section);
        Item cartProductItems = cartProducts.getItems().get(relativePosition);
        cartHolder.tvCartProductName.setText(cartProductItems.getItemName());
        cartHolder.tvCartProductDescription.setText(cartProductItems.getDetails());
        String price = PreferenceHelper.getPreferenceHelper(activity).getCurrency() +
                parseContent
                        .decimalTwoDigitFormat.format(cartProductItems
                        .getTotalItemAndSpecificationPrice());
        cartHolder.tvCartProductPricing.setText(price);
        cartHolder.tvItemQuantity.setText(String.valueOf(cartProductItems.getQuantity()));


        cartHolder.btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.increaseItemQuantity(cartProductList.get(section)
                        .getItems().get(relativePosition));
            }
        });

        cartHolder.btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.decreaseItemQuantity(cartProductList.get(section)
                        .getItems().get(relativePosition));

            }
        });
        cartHolder.tvRemoveCartItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.removeItem(section
                        , relativePosition);
            }
        });
        cartHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.goToUpdateOrderProductSpecificationActivity(section, relativePosition);
            }
        });
    }


    protected class CartHolder extends RecyclerView.ViewHolder {
        CustomFontTextViewTitle tvCartProductName, tvCartProductPricing;
        CustomTextView tvCartProductDescription,
                btnDecrease, tvItemQuantity, btnIncrease, tvRemoveCartItem;

        public CartHolder(View itemView) {
            super(itemView);
            tvCartProductName = (CustomFontTextViewTitle) itemView.findViewById(R.id
                    .tvCartProductName);
            tvCartProductDescription = (CustomTextView) itemView.findViewById(R.id
                    .tvCartProductDescription);
            tvCartProductPricing = (CustomFontTextViewTitle) itemView.findViewById(R.id
                    .tvCartProductPricing);
            btnDecrease = (CustomTextView) itemView.findViewById(R.id.btnDecrease);
            tvItemQuantity = (CustomTextView) itemView.findViewById(R.id.tvItemQuantity);
            btnIncrease = (CustomTextView) itemView.findViewById(R.id.btnIncrease);
            tvRemoveCartItem = (CustomTextView) itemView.findViewById(R.id.tvRemoveCartItem);


        }

    }

    protected class CartHeaderHolder extends RecyclerView.ViewHolder {
        CustomTextView tvStoreProductName;

        public CartHeaderHolder(View itemView) {
            super(itemView);

        }
    }
}
