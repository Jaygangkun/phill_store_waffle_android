package com.edelivery.store.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.edelivery.store.SpecificationGroupActivity;
import com.edelivery.store.models.datamodel.SpecificationGroup;
import com.edelivery.store.widgets.CustomFontTextViewTitle;
import com.elluminati.edelivery.store.R;

import java.util.List;

public class SpecificationGroupAdapter extends RecyclerView.Adapter<SpecificationGroupAdapter
        .GroupViewHolder> {

    private List<SpecificationGroup> specificationGroupList;
    private SpecificationGroupActivity gpoup2Activity;
    private boolean isEdited;

    public SpecificationGroupAdapter(List<SpecificationGroup> specificationGroupList,
                                     SpecificationGroupActivity gpoup2Activity) {
        this.specificationGroupList = specificationGroupList;
        this.gpoup2Activity = gpoup2Activity;
    }


    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GroupViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout
                .item_specification_group, parent, false));
    }

    public void setEdited(boolean edited) {
        isEdited = edited;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull final GroupViewHolder holder, final int position) {
        holder.tvSpecificationGroupName.setText(specificationGroupList.get(position).getName());
        holder.tvSpecificationGroupName.setTag(specificationGroupList.get(position).getNameList());
        holder.ivSpecGroupRemove.setVisibility(isEdited ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return specificationGroupList.size();
    }

    protected class GroupViewHolder extends RecyclerView.ViewHolder {
        CustomFontTextViewTitle tvSpecificationGroupName;
        ImageView ivSpecGroupRemove;

        public GroupViewHolder(View itemView) {
            super(itemView);
            tvSpecificationGroupName = itemView.findViewById(R.id.tvSpecificationGroupName);
            ivSpecGroupRemove = itemView.findViewById(R.id.ivSpecGroupRemove);
            ivSpecGroupRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    gpoup2Activity.deleteSpecificationGroup(getAdapterPosition());
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(isEdited){
                        gpoup2Activity.updateSpecification(getAdapterPosition(), tvSpecificationGroupName);
                    }else {
                        gpoup2Activity.goToSpecificationGroupItemActivity(getAdapterPosition());
                    }

                }
            });

        }
    }
}
