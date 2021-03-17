package com.edelivery.store.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edelivery.store.models.datamodel.ItemSpecification;
import com.edelivery.store.models.datamodel.ProductSpecification;
import com.edelivery.store.utils.PreferenceHelper;
import com.edelivery.store.utils.SectionedRecyclerViewAdapter;
import com.elluminati.edelivery.store.R;

import java.util.ArrayList;

/**
 * Adapter to display item specification list with sub specification on 25-02-2017.
 */

public class ItemSpecificationAdapter extends SectionedRecyclerViewAdapter<ViewHolder> {

    private Context context;
    private ArrayList<ItemSpecification> itemSpecifications;

    public ItemSpecificationAdapter(Context context, ArrayList<ItemSpecification>
            itemSpecifications) {
        this.context = context;
        this.itemSpecifications = itemSpecifications;
    }


    @Override
    public int getSectionCount() {
        return itemSpecifications.size();
    }

    @Override
    public int getItemCount(int section) {
        return itemSpecifications.get(section).getList().size();
    }

    @Override
    public void onBindHeaderViewHolder(ViewHolder holder, int section) {
        SectionViewHolder sectionViewHolder = (SectionViewHolder) holder;
        sectionViewHolder.tvSectionName.setText(itemSpecifications.get(section).getName());
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int section, int relativePosition, int
            absolutePosition) {


        ContentViewHolder contentViewHolder = (ContentViewHolder) holder;
        ProductSpecification productSpecification = itemSpecifications.get(section).getList().get
                (relativePosition);
        contentViewHolder.tvSubSpeciName.setText(productSpecification.getName());
        if (productSpecification.getPrice() > 0) {
            contentViewHolder.tvSubSpeciPrice.setText(PreferenceHelper.getPreferenceHelper
                    (context).getCurrency().concat(String.valueOf(productSpecification
                    .getPrice())));
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_ITEM) {
            view = LayoutInflater.from(context).inflate(R.layout.adapter_display_specification,
                    parent, false);
            return new ContentViewHolder(view);
        } else if (viewType == VIEW_TYPE_HEADER) {
            view = LayoutInflater.from(context).inflate(R.layout.adapter_item_speci_section,
                    parent, false);
            return new SectionViewHolder(view);
        }
        return null;
    }


    private class SectionViewHolder extends RecyclerView.ViewHolder {

        private TextView tvSectionName;

        private SectionViewHolder(View itemView) {
            super(itemView);

            tvSectionName = (TextView) itemView.findViewById(R.id.tvSectionSpeci);
        }
    }

    private class ContentViewHolder extends RecyclerView.ViewHolder {

        private TextView tvSubSpeciName, tvSubSpeciPrice;

        private ContentViewHolder(View itemView) {
            super(itemView);

            tvSubSpeciName = (TextView) itemView.findViewById(R.id.tvSpecification);
            tvSubSpeciPrice = (TextView) itemView.findViewById(R.id.tvSubSpeciPrice);
            tvSubSpeciPrice.setVisibility(View.VISIBLE);
        }
    }
}
