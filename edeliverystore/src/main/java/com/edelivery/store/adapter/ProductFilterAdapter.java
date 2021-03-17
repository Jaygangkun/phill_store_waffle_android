package com.edelivery.store.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.edelivery.store.models.datamodel.Product;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;

import java.util.ArrayList;

/**
 * Created by elluminati on 02-Oct-17.
 */

public class ProductFilterAdapter extends RecyclerView.Adapter<ProductFilterAdapter
        .ProductFilterViewHolder> {
    private ArrayList<Product>
            storeProductList;

    public ProductFilterAdapter(ArrayList<Product>
                                        storeProductList) {
        this.storeProductList = storeProductList;
    }

    @Override
    public ProductFilterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ProductFilterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R
                .layout.item_product_filter, parent, false));
    }

    @Override
    public void onBindViewHolder(ProductFilterViewHolder holder, int position) {
        holder.tvProductNameFilter.setText(storeProductList.get(position).getName());
        holder.rbSelectProductFilter.setChecked(storeProductList.get(position).isProductFiltered());

    }

    @Override
    public int getItemCount() {
        return storeProductList.size();
    }

    protected class ProductFilterViewHolder extends RecyclerView.ViewHolder {
        private CustomTextView tvProductNameFilter;
        private CheckBox rbSelectProductFilter;

        public ProductFilterViewHolder(View itemView) {
            super(itemView);
            tvProductNameFilter = (CustomTextView) itemView.findViewById(R.id
                    .tvProductNameFilter);
            rbSelectProductFilter = (CheckBox) itemView.findViewById(R.id
                    .rbSelectProductFilter);
            rbSelectProductFilter.setOnCheckedChangeListener(new CompoundButton
                    .OnCheckedChangeListener() {


                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    storeProductList.get(getAdapterPosition()).setProductFiltered(isChecked);
                }
            });
        }
    }
}
