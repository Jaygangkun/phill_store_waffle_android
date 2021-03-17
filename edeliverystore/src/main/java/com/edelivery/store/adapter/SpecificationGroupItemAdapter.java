package com.edelivery.store.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.edelivery.store.SpecificationGroupItemActivity;
import com.edelivery.store.models.datamodel.Specifications;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SpecificationGroupItemAdapter extends RecyclerView
        .Adapter<SpecificationGroupItemAdapter
        .GroupViewHolder> {

    private ArrayList<String> specificationIds;
    private List<Specifications> specificationsList;
    private SpecificationGroupItemActivity groupItemActivity;
    private boolean isEdited;

    public SpecificationGroupItemAdapter(List<Specifications> specificationsList,
                                         SpecificationGroupItemActivity groupItemActivity) {

        this.specificationsList = specificationsList;
        this.groupItemActivity = groupItemActivity;
        specificationIds = new ArrayList<>();
        Collections.sort(this.specificationsList);

    }

    public void notifyDataChange() {
        Collections.sort(specificationsList);
        notifyDataSetChanged();
    }

    public void setEdited(boolean edited) {
        isEdited = edited;
        notifyDataSetChanged();
    }

    public ArrayList<String> getSpecificationIds() {
        return specificationIds;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GroupViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout
                .item_specification_group_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        Specifications specifications = specificationsList.get(position);
        holder.tvSequenceNumber.setText(String.valueOf(specifications.getSequenceNumber()));
        holder.tvSpecificationName.setText(specifications.getName());
        holder.tvSpecificationName.setTag(specifications.getNameList());
        holder.ivSpecificationRemove.setVisibility(isEdited ? View.VISIBLE : View.GONE);
        holder.tvSpecificationPrice.setText(specifications.getPrice() > 0 ? String.valueOf
                (specifications.getPrice()) : "");
    }

    @Override
    public int getItemCount() {
        return specificationsList != null ? specificationsList.size() : 0;
    }

    protected class GroupViewHolder extends RecyclerView.ViewHolder {
        CustomTextView tvSpecificationName, tvSpecificationPrice, tvSequenceNumber;
        ImageView ivSpecificationRemove;

        public GroupViewHolder(View itemView) {
            super(itemView);
            tvSequenceNumber = itemView.findViewById(R.id.tvSequenceNumber);
            tvSpecificationName = itemView.findViewById(R.id.tvSpecificationName);
            ivSpecificationRemove = itemView.findViewById(R.id.ivSpecificationRemove);
            tvSpecificationPrice = itemView.findViewById(R.id.tvSpecificationPrice);
            ivSpecificationRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    specificationIds.add(specificationsList.get(getAdapterPosition()).getId());
                    specificationsList.remove(getAdapterPosition());
                    notifyDataSetChanged();
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    groupItemActivity.addORUpdateSpecificationDialog(getAdapterPosition(),
                            tvSpecificationName, true);
                }
            });

        }
    }
}
