package com.edelivery.store.Section;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edelivery.store.models.datamodel.ItemSpecification;
import com.edelivery.store.models.datamodel.ProductSpecification;
import com.edelivery.store.parse.ParseContent;
import com.edelivery.store.utils.PreferenceHelper;
import com.edelivery.store.utils.SectionedRecyclerViewAdapter;
import com.edelivery.store.widgets.CustomFontTextViewTitle;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;

import java.util.ArrayList;

/**
 * Created by Elluminati Mohit on 4/5/2017.
 */

public class ChildSpecificationViewAdapter extends SectionedRecyclerViewAdapter<RecyclerView
        .ViewHolder> {

    private ArrayList<ItemSpecification> itemSpecificationList;
    private Context context;

    ChildSpecificationViewAdapter(Context context, ArrayList<ItemSpecification>
            itemSpecificationList) {
        this.context = context;
        this.itemSpecificationList = itemSpecificationList;
    }

    @Override
    public int getSectionCount() {
        return itemSpecificationList.size();
    }

    @Override
    public int getItemCount(int section) {
        return itemSpecificationList.get(section).getList().size();
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int section) {

        IteamSpecificationHeader iteamSpecificationHeader = (IteamSpecificationHeader) holder;

        iteamSpecificationHeader.tvSectionSpeci_root.setText(itemSpecificationList.get(section)
                .getName());


        if (itemSpecificationList.get(section).getPrice() > 0) {
            String price = PreferenceHelper
                    .getPreferenceHelper(context).getCurrency() + ParseContent
                    .getParseContentInstance().decimalTwoDigitFormat.format(itemSpecificationList
                            .get
                                    (section).getPrice());
            iteamSpecificationHeader.tvSectionSpeciPrice.setText(price);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int section, int
            relativePosition, int absolutePosition) {
        IteamSpecificationFooter iteamSpecificationFooter = (IteamSpecificationFooter) holder;
        ProductSpecification productSpecification = itemSpecificationList.get(section).getList()
                .get(relativePosition);
        if (productSpecification.getPrice() > 0) {
            iteamSpecificationFooter.tvSpecPrice.setText(PreferenceHelper.getPreferenceHelper
                    (context).getCurrency()
                    + ParseContent.getParseContentInstance().decimalTwoDigitFormat.format
                    (productSpecification.getPrice()));
        }

        iteamSpecificationFooter.txSpeciName.setText(productSpecification.getName());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                return new IteamSpecificationHeader(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout
                                .adapter_item_speci_section, parent, false));
            case VIEW_TYPE_ITEM:
                return new IteamSpecificationFooter(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout
                                .adapter_display_specification, parent, false));

        }

        return null;
    }

    protected class IteamSpecificationHeader extends RecyclerView.ViewHolder {

        CustomFontTextViewTitle tvSectionSpeci_root, tvSectionSpeciPrice;

        public IteamSpecificationHeader(View itemView) {
            super(itemView);
            tvSectionSpeci_root = (CustomFontTextViewTitle) itemView.findViewById(R.id
                    .tvSectionSpeci);
            tvSectionSpeciPrice = (CustomFontTextViewTitle) itemView.findViewById(R.id
                    .tvSectionSpeciPrice);
        }
    }

    private class IteamSpecificationFooter extends RecyclerView.ViewHolder {


        CustomTextView txSpeciName, tvSpecPrice;

        public IteamSpecificationFooter(View itemView) {
            super(itemView);

            txSpeciName = (CustomTextView) itemView.findViewById(R.id.tvSpecification);
            tvSpecPrice = (CustomTextView) itemView.findViewById(R.id.tvSubSpeciPrice);


        }
    }
}
