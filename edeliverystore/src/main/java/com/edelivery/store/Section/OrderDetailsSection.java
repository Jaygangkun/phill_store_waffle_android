package com.edelivery.store.Section;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edelivery.store.models.datamodel.OrderDetails;
import com.edelivery.store.utils.PinnedHeaderItemDecoration;
import com.edelivery.store.utils.SectionedRecyclerViewAdapter;
import com.edelivery.store.utils.Utilities;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;

import java.util.ArrayList;

/**
 * Created by Elluminati Mohit on 4/4/2017.
 */

public class OrderDetailsSection extends SectionedRecyclerViewAdapter<RecyclerView.ViewHolder>
        implements PinnedHeaderItemDecoration.PinnedHeaderAdapter {

    private ArrayList<OrderDetails> orderDetailsList;
    private Context context;
    private boolean isShowImage;

    public OrderDetailsSection(ArrayList<OrderDetails> orderDetailsList, Context context, boolean
            isShowImage) {
        this.orderDetailsList = orderDetailsList;
        this.context = context;
        this.isShowImage = isShowImage;
    }


    @Override
    public int getSectionCount() {
        return orderDetailsList.size();
    }

    @Override
    public int getItemCount(int section) {

        return 1;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int section) {

        IteamHeader iteamHeaderHolder = (IteamHeader) holder;
        iteamHeaderHolder.tvSection.setText(orderDetailsList.get(section).getProductName());
        iteamHeaderHolder.tvSection.setFocusable(true);
        Utilities.setTagBackgroundRtlView(context, iteamHeaderHolder.tvSection);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int section, int
            relativePosition, int absolutePosition) {
        IteamFooterSpecification iteamFooterSpecification = (IteamFooterSpecification) holder;

        ChildRecyclerViewAdapter childRecyclerViewAdapter = new ChildRecyclerViewAdapter(context,
                orderDetailsList.get(section).getItems(), isShowImage);
        iteamFooterSpecification.recyclerView.setAdapter(childRecyclerViewAdapter);

        Utilities.printLog("tag", " childRecyclerViewAdapter - called");

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {

            case VIEW_TYPE_HEADER:
                return new IteamHeader(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout
                                .adapter_item_section, parent, false));
            case VIEW_TYPE_ITEM:
                return new IteamFooterSpecification(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout
                                .include_recyclerview, parent, false));
        }

        return null;
    }

    @Override
    public boolean isPinnedViewType(int viewType) {
        return viewType == VIEW_TYPE_HEADER;
    }

    protected class IteamHeader extends RecyclerView.ViewHolder {

        CustomTextView tvSection;

        public IteamHeader(View view) {
            super(view);
            tvSection = (CustomTextView) view.findViewById(R.id.tvSection);


        }
    }

    protected class IteamFooterSpecification extends RecyclerView.ViewHolder {

        RecyclerView recyclerView;

        public IteamFooterSpecification(View view) {
            super(view);
            recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);

            recyclerView.setLayoutManager(layoutManager);


        }
    }
}
