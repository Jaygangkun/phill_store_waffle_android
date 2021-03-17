package com.edelivery.store.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edelivery.store.models.datamodel.EarningData;
import com.edelivery.store.utils.SectionedRecyclerViewAdapter;
import com.edelivery.store.widgets.CustomFontTextViewTitle;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;

import java.util.ArrayList;

;

/**
 * Created by elluminati on 28-Jun-17.
 */

public class OrderEarningAdapter extends SectionedRecyclerViewAdapter<RecyclerView.ViewHolder> {

    private ArrayList<ArrayList<EarningData>> arrayListForEarning;

    public OrderEarningAdapter(ArrayList<ArrayList<EarningData>> arrayListForEarning) {
        this.arrayListForEarning = arrayListForEarning;
    }

    @Override
    public int getSectionCount() {
        return arrayListForEarning.size();
    }

    @Override
    public int getItemCount(int section) {
        return arrayListForEarning.get(section).size();
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int section) {

        OrderEarningHeading heading = (OrderEarningHeading) holder;
        heading.tvEarningHeader.setText(arrayListForEarning.get(section).get(0)
                .getTitleMain());
      /*  if (arrayListForEarning.size() - 1 == section) {
            heading.tvEarningHeader.setText("");
            heading.tvEarningHeader.setPadding(0, 5, 0, 0);
            heading.tvEarningHeader.setVisibility(View.INVISIBLE);
        } else {
            heading.tvEarningHeader.setVisibility(View.VISIBLE);
        }*/

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int section, int
            relativePosition, int absolutePosition) {
        OrderEarningItem item = (OrderEarningItem) holder;
        item.tvName.setText(arrayListForEarning.get(section).get(relativePosition).getTitle());
        item.tvPrice.setText(arrayListForEarning.get(section).get(relativePosition).getPrice());
        item.tvName.setAllCaps(arrayListForEarning.size() - 1 == section);


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                return new OrderEarningHeading(LayoutInflater.from(parent.getContext()).inflate(R
                        .layout.item_earning_header, parent, false));
            case VIEW_TYPE_ITEM:
                return new OrderEarningItem(LayoutInflater.from(parent.getContext()).inflate(R
                        .layout.item_earning_item, parent, false));
            default:
                // do with default
                break;
        }
        return null;
    }


    protected class OrderEarningHeading extends RecyclerView.ViewHolder {
        CustomFontTextViewTitle tvEarningHeader;

        public OrderEarningHeading(View itemView) {
            super(itemView);
            tvEarningHeader = (CustomFontTextViewTitle) itemView.findViewById(R.id.tvEarningHeader);
        }
    }

    protected class OrderEarningItem extends RecyclerView.ViewHolder {
        CustomTextView tvName, tvPrice;

        public OrderEarningItem(View itemView) {
            super(itemView);
            tvName = (CustomTextView) itemView.findViewById(R.id.tvName);
            tvPrice = (CustomTextView) itemView.findViewById(R.id.tvPrice);
        }
    }
}
