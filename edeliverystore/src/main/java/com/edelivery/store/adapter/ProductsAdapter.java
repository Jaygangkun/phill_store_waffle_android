package com.edelivery.store.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.edelivery.store.component.CustomFontCheckBox;
import com.edelivery.store.models.datamodel.Product;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;

import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder>  {

    private List<Product> products;
    private Context context;
    private boolean isEnabled;

    public ProductsAdapter(List<Product> products) {
        this.products = products;
        Collections.sort(products);
    }

    public void setProducts(List<Product> products){
        this.products = products;
        Collections.sort(products);
    }

    public void setEnabled(boolean isEnabled){
        this.isEnabled = isEnabled;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.tvProductName.setText(products.get(position).getName());
        holder.cbProduct.setChecked(products.get(position).isSelected());
//        holder.cbProduct.setEnabled(isEnabled);
       /* holder.llProduct.setEnabled(isEnabled);

        holder.llProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.cbProduct.setChecked(!products.get(holder.getAdapterPosition()).isSelected());
                products.get(holder.getAdapterPosition()).setSelected(holder.cbProduct.isChecked());
            }
        });*/

        holder.cbProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.cbProduct.setChecked(!products.get(holder.getAdapterPosition()).isSelected());
                products.get(holder.getAdapterPosition()).setSelected(holder.cbProduct.isChecked());
            }
        });

        if(position < products.size()-1){
            holder.viewDiv.setVisibility(View.VISIBLE);
        }else {
            holder.viewDiv.setVisibility(View.GONE);
        }


    }


    @Override
    public int getItemCount() {
        return products.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView tvProductName;
        CustomFontCheckBox cbProduct;
        LinearLayout llProduct;
        View viewDiv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            cbProduct = itemView.findViewById(R.id.cbProduct);
            llProduct = itemView.findViewById(R.id.llProduct);
            viewDiv = itemView.findViewById(R.id.viewDiv);
        }
    }
}
