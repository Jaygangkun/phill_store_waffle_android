package com.edelivery.store.Section;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edelivery.store.models.datamodel.Item;
import com.edelivery.store.parse.ParseContent;
import com.edelivery.store.utils.PreferenceHelper;
import com.edelivery.store.utils.SectionedRecyclerViewAdapter;
import com.edelivery.store.utils.Utilities;
import com.edelivery.store.widgets.CustomFontTextViewTitle;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

/**
 * Created by Elluminati Mohit on 4/4/2017.
 */

public class ChildRecyclerViewAdapter extends SectionedRecyclerViewAdapter<RecyclerView
        .ViewHolder> {

    ArrayList<Item> itemList;
    private Context context;
    private boolean isShowImage;

    public ChildRecyclerViewAdapter(Context context, ArrayList<Item> itemList, boolean
            isShowImage) {
        this.context = context;
        this.itemList = itemList;
        this.isShowImage = isShowImage;
    }

    @Override
    public int getSectionCount() {
        return itemList.size();
    }

    @Override
    public int getItemCount(int section) {

        Utilities.printLog("ChildRecyclerViewAdapter", "get iteam count - " + itemList.get
                (section).getSpecifications().size());
        return 1;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int section) {

        final IteamChildHeader iteamChildHeader = (IteamChildHeader) holder;
        Item individualIteam = itemList.get(section);
        iteamChildHeader.tvItemName.setText(individualIteam.getItemName());
        if (individualIteam.getTotalItemAndSpecificationPrice() > 0) {
            iteamChildHeader.tvItemsPrice.setText(PreferenceHelper.getPreferenceHelper(context)
                    .getCurrency() +
                    String.valueOf(ParseContent.getParseContentInstance().decimalTwoDigitFormat
                            .format(individualIteam
                                    .getTotalItemAndSpecificationPrice())));
        }

        if (individualIteam.getItemPrice()+individualIteam.getTotalSpecificationPrice() > 0) {
            iteamChildHeader.tvItemCounts.setText(context.getResources().getString(R.string
                    .text_qty)
                    + " " +
                    String.valueOf(individualIteam.getQuantity()) + " X " +
                    PreferenceHelper.getPreferenceHelper(context)
                            .getCurrency() +
                    String.valueOf(ParseContent.getParseContentInstance().decimalTwoDigitFormat
                            .format(individualIteam.getItemPrice()+individualIteam.getTotalSpecificationPrice())));
        } else {
            iteamChildHeader.tvItemCounts.setText(context.getResources().getString(R.string
                    .text_qty)
                    + " "
                    + String.valueOf(individualIteam.getQuantity()));
        }

        if (0 == section) {
            iteamChildHeader.ivListDivider.setVisibility(View.GONE);
        } else {
            iteamChildHeader.ivListDivider.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int section, int
            relativePosition, int absolutePosition) {

        IteamFooter iteamFooter = (IteamFooter) holder;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);

        ChildSpecificationViewAdapter adapter = new ChildSpecificationViewAdapter(context,
                itemList.get(section).getSpecifications());
        iteamFooter.root_recycler.setLayoutManager(layoutManager);
        iteamFooter.root_recycler.setAdapter(adapter);

        String itemNote = itemList.get(section).getNoteForItem();
        if (TextUtils.isEmpty(itemNote)) {
            iteamFooter.tvItemNote.setVisibility(View.GONE);
            iteamFooter.tvItemNoteHeading.setVisibility(View.GONE);
        } else {
            iteamFooter.tvItemNote.setVisibility(View.VISIBLE);
            iteamFooter.tvItemNoteHeading.setVisibility(View.VISIBLE);
            iteamFooter.tvItemNote.setText(itemNote);
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                return new IteamChildHeader(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout
                                .adapter_item_oreder_detail, parent, false));
            case VIEW_TYPE_ITEM:
                return new IteamFooter(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout
                                .root_recycler, parent, false));

        }
        return null;
    }

    protected class IteamChildHeader extends RecyclerView.ViewHolder {

        RoundedImageView ivItems;
        View ivListDivider;
        CustomTextView tvItemCounts;
        CustomFontTextViewTitle tvItemName, tvItemsPrice;

        public IteamChildHeader(View itemView) {
            super(itemView);
            ivItems = (RoundedImageView) itemView.findViewById(R.id.ivItems);
            tvItemName = (CustomFontTextViewTitle) itemView.findViewById(R.id.tvItemName);
            tvItemCounts = (CustomTextView) itemView.findViewById(R.id.tvItemCounts);
            tvItemsPrice = (CustomFontTextViewTitle) itemView.findViewById(R.id.tvItemsPrice);
            ivListDivider = itemView.findViewById(R.id.ivListDivider);

        }
    }

    protected class IteamFooter extends RecyclerView.ViewHolder {


        RecyclerView root_recycler;
        CustomFontTextViewTitle tvItemNoteHeading;
        CustomTextView tvItemNote;

        public IteamFooter(View itemView) {
            super(itemView);
            root_recycler = (RecyclerView) itemView.findViewById(R.id.root_recycler);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
            root_recycler.setLayoutManager(layoutManager);
            tvItemNote = (CustomTextView) itemView.findViewById(R.id.tvItemNote);
            tvItemNoteHeading = (CustomFontTextViewTitle) itemView.findViewById(R.id
                    .tvItemNoteHeading);
        }
    }
}
