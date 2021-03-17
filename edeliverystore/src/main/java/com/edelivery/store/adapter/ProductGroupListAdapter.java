package com.edelivery.store.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.edelivery.store.models.datamodel.ProductGroup;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;

import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class ProductGroupListAdapter extends RecyclerView.Adapter<ProductGroupListAdapter.ViewHolder>  {

    private List<ProductGroup> productGroups;
    private boolean isDelete;

    public ProductGroupListAdapter(List<ProductGroup> productGroups, boolean isDelete){
        this.productGroups = productGroups;
        this.isDelete = isDelete;
        Collections.sort(this.productGroups);
    }

    public void setIsDelete(boolean isDelete){
        this.isDelete = isDelete;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        this.context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_group, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.tvGroupNme.setText(productGroups.get(position).getName() + " (" +
                productGroups.get(position).getProductIds().size() + ")");

        holder.ivDelete.setVisibility(isDelete ? View.VISIBLE : View.GONE);
        holder.ivNext.setVisibility(isDelete ? View.GONE : View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return productGroups.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView tvGroupNme;
        LinearLayout llGroup;
        ImageView ivNext, ivDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGroupNme = itemView.findViewById(R.id.tvGroupNme);
            llGroup = itemView.findViewById(R.id.llGroup);
            ivNext = itemView.findViewById(R.id.ivNext);
            ivDelete = itemView.findViewById(R.id.ivDelete);

            llGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isDelete){
                        onDelete(productGroups.get(getAdapterPosition()).getId(),getAdapterPosition());
                    }else {
                        onSelect(getAdapterPosition());
                    }
                }
            });
        }
    }

    public abstract void onSelect(int position);
    public abstract void onDelete(String productGroupId, int position);

}
