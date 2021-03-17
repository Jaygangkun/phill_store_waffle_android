package com.edelivery.store.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edelivery.store.AddItemActivity;
import com.edelivery.store.HomeActivity;
import com.edelivery.store.ItemDetailActivity;
import com.edelivery.store.models.datamodel.Item;
import com.edelivery.store.models.datamodel.Product;
import com.edelivery.store.models.responsemodel.IsSuccessResponse;
import com.edelivery.store.models.singleton.CurrentProduct;
import com.edelivery.store.parse.ApiClient;
import com.edelivery.store.parse.ApiInterface;
import com.edelivery.store.parse.ParseContent;
import com.edelivery.store.utils.Constant;
import com.edelivery.store.utils.PinnedHeaderItemDecoration;
import com.edelivery.store.utils.PreferenceHelper;
import com.edelivery.store.utils.SectionedRecyclerViewAdapter;
import com.edelivery.store.utils.Utilities;
import com.edelivery.store.widgets.CustomFontTextViewTitle;
import com.elluminati.edelivery.store.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.elluminati.edelivery.store.BuildConfig.IMAGE_URL;

/**
 * Created by elluminati on 08-Jun-17.
 */

public class ProductItemAdapter extends SectionedRecyclerViewAdapter implements Filterable,
        PinnedHeaderItemDecoration.PinnedHeaderAdapter {

    private ArrayList<Product> productList;
    private Context context;
    private StoreItemFilter filter;
    private List<Item> itemsItemFilter;
    private ArrayList<Product> filterList;

    public ProductItemAdapter(Context context, ArrayList<Product> productList) {
        this.context = context;
        this.productList = productList;
        itemsItemFilter = new ArrayList<>();
        filterList = new ArrayList<>();
    }

    public void setProductList(ArrayList<Product> productList) {
        this.productList = productList;
    }

    @Override
    public int getSectionCount() {
        return productList.size();
    }

    @Override
    public int getItemCount(int section) {
        return productList.get(section).getItems().size();
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, final int section) {
        ProductItemHeaderView viewHolderSection = (ProductItemHeaderView) holder;
        viewHolderSection.tvSection.setText(productList.get(section).getName());
        Utilities.setTagBackgroundRtlView(context, viewHolderSection.tvSection);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //((HomeActivity) context).itemListFragment.gotoEditProductActivity(productList
                // .get(section));
            }
        });

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int section, final int
            relativePosition, int absolutePosition) {
        final Item item = productList.get(section).getItems().get(relativePosition);
        final ProductItemView viewHolderData = (ProductItemView) holder;
        viewHolderData.tvItemName.setText(item.getName());
        viewHolderData.tvItemDetail.setText(item.getDetails());
        viewHolderData.tvItemSpecification.setText(context.getResources().getString(R.string
                .text_specification));
        if (item.getPrice() > 0) {
            viewHolderData.tvItemPrice.setVisibility(View.VISIBLE);
            viewHolderData.tvItemPrice.setText(PreferenceHelper.getPreferenceHelper(context)
                    .getCurrency().concat(ParseContent.getParseContentInstance()
                            .decimalTwoDigitFormat.format(item.getPrice())));
        } else {
            viewHolderData.tvItemPrice.setVisibility(View.INVISIBLE);
        }
        //viewHolderData.switchCompat.setOnCheckedChangeListener(null);
        Utilities.printLog("tag", "Switch status " + relativePosition + " ==" + item
                .isIsVisibleInStore());
        viewHolderData.switchCompat.setChecked(item.isItemInStock());
        viewHolderData.switchCompat.setTag(holder.getAdapterPosition());
        //viewHolderData.switchCompat.setOnCheckedChangeListener(this);
        viewHolderData.switchCompat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isItemInStock(section, relativePosition, viewHolderData.switchCompat);
            }
        });
        viewHolderData.llSpeciNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ItemDetailActivity.class);
                ((HomeActivity) context).itemListFragment.gotoAddItemActivity(intent, item,
                        productList.get(section)
                                .getName(), item.getProductId(), ((ProductItemView) holder)
                                .ivItem,
                        false, IMAGE_URL + productList.get(section).getImageUrl());
            }
        });

        viewHolderData.llItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, AddItemActivity.class);
                ((HomeActivity) context).itemListFragment.gotoAddItemActivity(intent, item,
                        productList.get(section)
                                .getName(), item.getProductId(), ((ProductItemView) holder)
                                .ivItem,
                        false, IMAGE_URL + productList.get(section).getImageUrl());
            }
        });

        if (item.isItemInStock()) {
            viewHolderData.tvInStock.setText(context.getResources().getString(R.string
                    .text_item_in_stock));
        } else {
            viewHolderData.tvInStock.setText(context.getResources().getString(R.string
                    .text_item_out_stock));
        }


        if (productList.size() - 1 == section && productList.get(section).getItems().size() - 1
                == relativePosition) {
            viewHolderData.llBlank.setVisibility(View.VISIBLE);
        } else {
            viewHolderData.llBlank.setVisibility(View.GONE);
        }
        if (productList.get(section).getItems().size() - 1
                == relativePosition) {
            viewHolderData.ivListDivider.setVisibility(View.GONE);
        } else {
            viewHolderData.ivListDivider.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        switch (viewType) {
            case VIEW_TYPE_HEADER:
                view = LayoutInflater.from(context).inflate(R.layout.adapter_item_section, parent,
                        false);
                return new ProductItemHeaderView(view);
            case VIEW_TYPE_ITEM:
                view = LayoutInflater.from(context).inflate(R.layout.adapter_product_list, parent,
                        false);
                return new ProductItemView(view);
            default:
                // do with default
                break;
        }
        return null;
    }

    private void isItemInStock(final int section, final int position, final SwitchCompat
            switchCompat) {
        Utilities.showProgressDialog(context);
        final Item item = productList.get(section).getItems().get(position);

        HashMap<String, RequestBody> map = new HashMap<>();
        map.put(Constant.ITEM_ID, ApiClient.makeTextRequestBody(
                item.getId()));
        Utilities.printLog("ItemListFragment", "Switch status - " + switchCompat.isChecked());
        map.put(Constant.IS_ITEM_IN_STOCK, ApiClient.makeTextRequestBody(
                String.valueOf(switchCompat.isChecked())));

        Call<IsSuccessResponse> call = ApiClient.getClient().create(ApiInterface.class)
                .isItemInStock(map);
        call.enqueue(new Callback<IsSuccessResponse>() {
            @Override
            public void onResponse(Call<IsSuccessResponse> call, Response<IsSuccessResponse>
                    response) {

                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        Utilities.printLog("ItemListFragment", new Gson().toJson(response.body()));
                        item.setItemInStock(switchCompat.isChecked());
                        CurrentProduct.getInstance().setProductDataList(productList);
                        notifyDataSetChanged();
                    } else {
                        switchCompat.toggle();
                        ParseContent.getParseContentInstance().showErrorMessage(context, response
                                .body().getErrorCode(), false);
                    }
                } else {
                    Utilities.showHttpErrorToast(response.code(), context);
                }
                Utilities.removeProgressDialog();
            }

            @Override
            public void onFailure(Call<IsSuccessResponse> call, Throwable t) {
                Utilities.printLog("ItemListAdapter", t.getMessage());
            }
        });
    }

    @Override
    public Filter getFilter() {
        if (filter == null)
            filter = new StoreItemFilter(productList);
        return filter;
    }

    @Override
    public boolean isPinnedViewType(int viewType) {
        return viewType == VIEW_TYPE_HEADER;
    }

    protected class ProductItemHeaderView extends RecyclerView.ViewHolder {
        TextView tvSection;

        public ProductItemHeaderView(View itemView) {
            super(itemView);
            tvSection = (TextView) itemView.findViewById(R.id.tvSection);
        }
    }

    protected class ProductItemView extends RecyclerView.ViewHolder {
        private TextView tvItemDetail, tvItemSpecification, tvInStock;
        private ImageView ivItem;
        private SwitchCompat switchCompat;
        private LinearLayout llItem, llSpeciNum;
        private View llBlank, ivListDivider;
        private CustomFontTextViewTitle tvItemName, tvItemPrice;

        public ProductItemView(View itemView) {
            super(itemView);
            tvItemName = (CustomFontTextViewTitle) itemView.findViewById(R.id.tvProductName);
            tvItemDetail = (TextView) itemView.findViewById(R.id.tvProductDescription);
            tvItemSpecification = (TextView) itemView.findViewById(R.id.tvSpecificationNum);
            tvItemPrice = (CustomFontTextViewTitle) itemView.findViewById(R.id.tvItemPrice);
            tvItemPrice.setVisibility(View.VISIBLE);
            switchCompat = (SwitchCompat) itemView.findViewById(R.id.switchProduct);
            ivItem = (ImageView) itemView.findViewById(R.id.ivProduct);
            itemView.findViewById(R.id.tvVisibility).setVisibility(View.VISIBLE);
            llSpeciNum = (LinearLayout) itemView.findViewById(R.id.llSpeciNum);
            tvInStock = (TextView) itemView.findViewById(R.id.tvVisibility);
            ivListDivider = itemView.findViewById(R.id.ivListDivider);
            llBlank = itemView.findViewById(R.id.llBlank);
            llItem = (LinearLayout) itemView.findViewById(R.id.llItem);

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
            productList = (ArrayList<Product>) results.values;
            notifyDataSetChanged();
        }
    }


}
