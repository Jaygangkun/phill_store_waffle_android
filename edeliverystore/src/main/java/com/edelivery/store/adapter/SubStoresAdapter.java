package com.edelivery.store.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edelivery.store.models.datamodel.SubStore;
import com.elluminati.edelivery.store.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Ravi Bhalodi on 14,July,2020 in Elluminati
 */
public abstract class SubStoresAdapter extends RecyclerView.Adapter<SubStoresAdapter.SubStoreHolder> {

    private List<SubStore> subStoreList;

    public void setSubStoreList(List<SubStore> subStoreList) {
        this.subStoreList = subStoreList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SubStoreHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SubStoreHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sub_store, parent, false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull final SubStoreHolder holder, final int position) {
        SubStore subStore = subStoreList.get(position);
        holder.tvSubStoreName.setText(subStore.getName());
        holder.tvSubStoreEmail.setText(subStore.getEmail());
        holder.tvSubStorePhone.setText(subStore.getPhone());
        holder.div.setVisibility(getItemCount() - 1 == position ? View.GONE : View.VISIBLE);
        holder.tvSubStoreName.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0,
                subStore.isIsApproved() ? R.drawable.ic_dot_green : R.drawable.ic_dot_red, 0);

    }

    @Override
    public int getItemCount() {
        return subStoreList == null ? 0 : subStoreList.size();
    }

    protected class SubStoreHolder extends RecyclerView.ViewHolder {
        TextView tvSubStoreName, tvSubStoreEmail, tvSubStorePhone;
        View div;

        public SubStoreHolder(@NonNull View itemView) {
            super(itemView);
            tvSubStoreName = itemView.findViewById(R.id.tvSubStoreName);
            tvSubStoreEmail = itemView.findViewById(R.id.tvSubStoreEmail);
            tvSubStorePhone = itemView.findViewById(R.id.tvSubStorePhone);
            div = itemView.findViewById(R.id.div);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onStoreSelect(subStoreList.get(getAdapterPosition()));
                }
            });


        }
    }


    public abstract void onStoreSelect(SubStore subStore);

}
