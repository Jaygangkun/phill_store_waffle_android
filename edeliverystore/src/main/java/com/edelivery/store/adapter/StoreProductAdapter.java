package com.edelivery.store.adapter;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.edelivery.store.StoreOrderProductActivity;
import com.edelivery.store.models.datamodel.Item;
import com.edelivery.store.models.datamodel.ItemSpecification;
import com.edelivery.store.models.datamodel.Product;
import com.edelivery.store.models.datamodel.ProductSpecification;
import com.edelivery.store.parse.ParseContent;
import com.edelivery.store.utils.GlideApp;
import com.edelivery.store.utils.PinnedHeaderItemDecoration;
import com.edelivery.store.utils.PreferenceHelper;
import com.edelivery.store.utils.SectionedRecyclerViewAdapter;
import com.edelivery.store.utils.Utilities;
import com.edelivery.store.widgets.CustomFontTextViewTitle;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import static com.elluminati.edelivery.store.BuildConfig.IMAGE_URL;


/**
 * Created by elluminati on 16-Feb-17.
 */

public class StoreProductAdapter extends SectionedRecyclerViewAdapter<RecyclerView.ViewHolder>
        implements Filterable, PinnedHeaderItemDecoration.PinnedHeaderAdapter {
    private ArrayList<Product> filterList;
    private ArrayList<Product> storeProductList;
    private StoreOrderProductActivity storeProductActivity;
    private ParseContent parseContent;
    private StoreItemFilter filter;
    private List<Item> itemsItemFilter;

    public StoreProductAdapter(StoreOrderProductActivity storeProductActivity, ArrayList<Product>
            storeProductList) {
        this.storeProductList = storeProductList;
        this.storeProductActivity = storeProductActivity;
        parseContent = ParseContent.getParseContentInstance();
        itemsItemFilter = new ArrayList<>();
        filterList = new ArrayList<>();
    }

    @Override
    public int getSectionCount() {
        return storeProductList.size();
    }

    @Override
    public int getItemCount(int section) {
        return storeProductList.get(section).getItems().size();
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int section) {
        StoreProductHeaderHolder storeProductHeaderHolder = (StoreProductHeaderHolder) holder;
        storeProductHeaderHolder.tvStoreProductName.setText(storeProductList.get(section)
                .getName());
        Utilities.setTagBackgroundRtlView(storeProductActivity, storeProductHeaderHolder
                .tvStoreProductName);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int section, int
            relativePosition, int absolutePosition) {
        final StoreProductItemHolder storeProductItemHolder = (StoreProductItemHolder) holder;
        List<Item> productItemsItemList = storeProductList.get(section).getItems();
        final Item productsItem = productItemsItemList.get(relativePosition);

        if (productsItem.getImageUrl() != null && !productsItem.getImageUrl().isEmpty()) {
            GlideApp.with(storeProductActivity).load(IMAGE_URL + productsItem.getImageUrl().get
                    (0))
                    .dontAnimate().listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                            Target<Drawable> target, boolean isFirstResource) {
                    storeProductItemHolder.ivProductImage.setVisibility(View.GONE);
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model,
                                               Target<Drawable> target, DataSource dataSource,
                                               boolean isFirstResource) {
                    storeProductItemHolder.ivProductImage.setVisibility(View.VISIBLE);
                    return false;
                }
            }).into(storeProductItemHolder.ivProductImage);
        }
        storeProductItemHolder.tvProductName.setText(productsItem.getName());
        storeProductItemHolder.tvProductDescription.setText(String.valueOf(productsItem
                .getDetails()));
        if (productsItem.getPrice() > 0) {
            String price = PreferenceHelper.getPreferenceHelper(storeProductActivity).getCurrency
                    () +
                    parseContent
                            .decimalTwoDigitFormat.format(productsItem
                            .getPrice());
            storeProductItemHolder.tvProductPricing.setText(price);
            storeProductItemHolder.tvProductPricing.setVisibility(View.VISIBLE);
        } else {
            double price = 0;
            for (ItemSpecification specificationsItem : productsItem.getSpecifications()) {
                for (ProductSpecification listItem : specificationsItem.getList()) {
                    if (listItem.isIsDefaultSelected()) {
                        price = price + listItem.getPrice();

                    }
                }
            }
            storeProductItemHolder.tvProductPricing.setText(PreferenceHelper.getPreferenceHelper
                    (storeProductActivity).getCurrency() + parseContent
                    .decimalTwoDigitFormat.format(price));
            storeProductItemHolder.tvProductPricing.setVisibility(View.VISIBLE);
        }
        if (productItemsItemList.size() - 1 == relativePosition) {
            storeProductItemHolder.viewDivProductItem.setVisibility(View.GONE);
        } else {
            storeProductItemHolder.viewDivProductItem.setVisibility(View.VISIBLE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storeProductActivity.goToSpecificationActivity(storeProductList.get
                                (section),
                        productsItem);

            }
        });
        if (productsItem.isItemInStock()) {
            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            holder.itemView.setVisibility(View.VISIBLE);
            holder.itemView.setLayoutParams(layoutParams);
        } else {
            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
            layoutParams.height = 0;
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(layoutParams);
        }


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                return new StoreProductHeaderHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout
                                .adapter_item_section, parent, false));
            case VIEW_TYPE_ITEM:
                return new StoreProductItemHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_store_product_item, parent, false));
            default:
                // do somethings
                break;

        }
        return null;
    }

    @Override
    public Filter getFilter() {
        if (filter == null)
            filter = new StoreItemFilter(storeProductList);
        return filter;
    }

    @Override
    public boolean isPinnedViewType(int viewType) {
        return viewType == VIEW_TYPE_HEADER;
    }


    protected class StoreProductHeaderHolder extends RecyclerView.ViewHolder {

        CustomTextView tvStoreProductName;

        public StoreProductHeaderHolder(View itemView) {
            super(itemView);
            tvStoreProductName = (CustomTextView) itemView.findViewById(R.id
                    .tvSection);
        }
    }

    protected class StoreProductItemHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImage;
        View viewDivProductItem;
        CustomFontTextViewTitle tvProductName, tvProductPricing;
        CustomTextView tvProductDescription;

        public StoreProductItemHolder(View itemView) {
            super(itemView);
            ivProductImage = (ImageView) itemView.findViewById(R.id.ivProductImage);
            tvProductName = (CustomFontTextViewTitle) itemView.findViewById(R.id.tvProductName);
            tvProductDescription = (CustomTextView) itemView.findViewById(R.id
                    .tvProductDescription);
            tvProductPricing = (CustomFontTextViewTitle) itemView.findViewById(R.id
                    .tvProductPricing);
            viewDivProductItem = itemView.findViewById(R.id.viewDivProductItem);
        }


    }

    private class StoreItemFilter extends Filter {
        private ArrayList<Product> sourceList;

        StoreItemFilter(ArrayList<Product> storesItemArrayList) {
            sourceList = new ArrayList<>();
            synchronized (this) {
                sourceList.addAll(storesItemArrayList);
            }
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterSeq = constraint.toString();
            FilterResults result = new FilterResults();
            filterList.clear();
            for (int i = 0; i < sourceList.size(); i++) {
                itemsItemFilter.clear();
                if (sourceList.get(i).isProductFiltered()) {
                    if (TextUtils.isEmpty(constraint)) {
                        itemsItemFilter.addAll(sourceList.get(i).getItems());
                    } else {
                        for (Item itemsItem : sourceList.get(i).getItems()) {
                            if (itemsItem.getName().toUpperCase().contains(filterSeq.toUpperCase
                                    ())) {
                                itemsItemFilter.add(itemsItem);
                            }
                        }
                    }
                    if (!itemsItemFilter.isEmpty()) {
                        Product productItem = sourceList.get(i).copy();
                        productItem.getItems().clear();
                        productItem.getItems().addAll(itemsItemFilter);
                        filterList.add(productItem);
                    }
                }
            }
            result.count = filterList.size();
            result.values = filterList;
            return result;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            storeProductList = (ArrayList<Product>) results.values;
            notifyDataSetChanged();
        }
    }

}
