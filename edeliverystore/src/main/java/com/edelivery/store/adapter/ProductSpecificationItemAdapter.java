package com.edelivery.store.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edelivery.store.component.CustomFontCheckBox;
import com.edelivery.store.component.CustomFontRadioButton;
import com.edelivery.store.models.datamodel.ItemSpecification;
import com.edelivery.store.models.datamodel.ProductSpecification;
import com.edelivery.store.parse.ParseContent;
import com.edelivery.store.utils.Constant;
import com.edelivery.store.utils.PreferenceHelper;
import com.edelivery.store.utils.SectionedRecyclerViewAdapter;
import com.edelivery.store.widgets.CustomFontTextViewTitle;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;

import java.util.ArrayList;
import java.util.Collections;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by elluminati on 02-Mar-17.
 */

public abstract class ProductSpecificationItemAdapter extends
        SectionedRecyclerViewAdapter<RecyclerView
                .ViewHolder> {


    private ArrayList<ItemSpecification> specificationsItems;
    private Context context;
    private ParseContent parseContent;

    public ProductSpecificationItemAdapter(Context
                                                   context,
                                           ArrayList<ItemSpecification> specificationsItems) {
        this.context = context;
        this.specificationsItems = specificationsItems;
        Collections.sort(this.specificationsItems);
        parseContent = ParseContent.getParseContentInstance();
    }

    @Override
    public int getSectionCount() {
        return specificationsItems.size();
    }

    @Override
    public int getItemCount(int section) {
        return specificationsItems.get(section).getList().size();
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int section) {
        SpecificationHeaderHolder specificationHeaderHolder = (SpecificationHeaderHolder) holder;
        specificationHeaderHolder.tvSpecificationName.setText(specificationsItems.get(section)
                .getName());
        if ((specificationsItems.get(section).isRequired())) {
            specificationHeaderHolder.tvRequired.setVisibility(View.VISIBLE);
        } else {
            specificationHeaderHolder.tvRequired.setVisibility(View.GONE);
        }

        /*if (0 == section) {
            specificationHeaderHolder.divProductSpecification.setVisibility(View.GONE);
        } else {
            specificationHeaderHolder.divProductSpecification.setVisibility(View.VISIBLE);
        }*/

        specificationHeaderHolder.tvChooseUpTo.setText(specificationsItems.get(section)
                .getChooseMessage());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int section, final int
            relativePosition, final int absolutePosition) {
        final SpecificationItemHolder specificationItemHolder = (SpecificationItemHolder) holder;
        final ItemSpecification specification = specificationsItems.get(section);
        final ProductSpecification specificationListItem = specification.getList().get
                (relativePosition);
        if (specificationListItem
                .getPrice() > 0) {
            final String price = PreferenceHelper.getPreferenceHelper
                    (context).getCurrency() + parseContent
                    .decimalTwoDigitFormat.format(specificationListItem
                            .getPrice());
            specificationItemHolder.tvSpecificationItemPrice.setText(price);
            specificationItemHolder.tvSpecificationItemPrice.setVisibility(View.VISIBLE);
        } else {
            specificationItemHolder.tvSpecificationItemPrice.setVisibility(View.GONE);
        }

        specificationItemHolder.tvSpecificationItemDescription.setText(specificationListItem
                .getName());

        int itemType = specificationsItems.get(section).getType();
        switch (itemType) {
            case Constant.TYPE_SPECIFICATION_SINGLE:
                specificationItemHolder.rbSingleSpecification.setVisibility(View.VISIBLE);
                specificationItemHolder.rbMultipleSpecification.setVisibility(View.GONE);
                specificationItemHolder.rbSingleSpecification.setChecked(specificationListItem
                        .isIsDefaultSelected());
                specificationItemHolder.rbSingleSpecification.setOnClickListener(new View
                        .OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        specificationListItem.setIsDefaultSelected(true);
                        onSingleItemClick(section, relativePosition, absolutePosition);

                    }
                });

                break;
            case Constant.TYPE_SPECIFICATION_MULTIPLE:
                specificationItemHolder.rbSingleSpecification.setVisibility(View.GONE);
                specificationItemHolder.rbMultipleSpecification.setVisibility(View.VISIBLE);
                specificationItemHolder.rbMultipleSpecification.setChecked(specificationListItem
                        .isIsDefaultSelected());
                specificationItemHolder.rbMultipleSpecification.setOnClickListener(new View
                        .OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        boolean checked = isValidSelection(specification.getRange(), specification
                                        .getMaxRange(), specification.getSelectedCount(),
                                specificationListItem.isIsDefaultSelected());

                        if (!specificationListItem.isIsDefaultSelected() && checked) {
                            specification.setSelectedCount(specification.getSelectedCount() +
                                    1);
                        } else if (specificationListItem.isIsDefaultSelected() && !checked) {
                            specification.setSelectedCount(specification.getSelectedCount() -
                                    1);
                        }
                        specificationListItem.setIsDefaultSelected(checked);
                        specificationItemHolder
                                .rbMultipleSpecification.setChecked(checked);
                        onMultipleItemClick();


                    }
                });
                break;
            default:
                // do with default
                break;

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                return new SpecificationHeaderHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R
                                .layout.item_specification_header, parent, false));
            case VIEW_TYPE_ITEM:
                return new SpecificationItemHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R
                                .layout.item_specification_item, parent, false));
            default:
                // do with default
                break;
        }
        return null;
    }

    public abstract void onSingleItemClick(int section, final int
            relativePosition, final int absolutePosition);

    public abstract void onMultipleItemClick();

    /**
     * this method return flag according to range selection
     *
     * @param range
     * @param maxRange
     * @param selectedCount
     * @param isSelected
     * @return
     */
    private boolean isValidSelection(int range, int maxRange, int selectedCount, boolean
            isSelected) {
        if (range == 0 && maxRange ==
                0) {
            return !isSelected;
        } else if (selectedCount <= range && maxRange == 0) {
            return range != selectedCount && !isSelected;
        } else if (range >= 0 && selectedCount <= maxRange) {
            return maxRange != selectedCount && !isSelected;
        } else {
            return isSelected;
        }
    }

    protected class SpecificationHeaderHolder extends RecyclerView.ViewHolder {

        CustomFontTextViewTitle tvSpecificationName;
        CustomTextView tvRequired, tvChooseUpTo;
        View divProductSpecification;

        public SpecificationHeaderHolder(View itemView) {
            super(itemView);
            tvSpecificationName = (CustomFontTextViewTitle) itemView.findViewById(
                    R.id.tvSpecificationName);
            tvRequired = (CustomTextView) itemView.findViewById(R.id.tvRequired);
            divProductSpecification = itemView.findViewById(R.id.divProductSpecification);
            tvChooseUpTo = itemView.findViewById(R.id.tvChooseUpTo);
        }
    }

    protected class SpecificationItemHolder extends RecyclerView.ViewHolder {
        CustomFontRadioButton rbSingleSpecification;
        CustomFontCheckBox rbMultipleSpecification;
        CustomTextView tvSpecificationItemDescription, tvSpecificationItemPrice;

        public SpecificationItemHolder(View itemView) {
            super(itemView);
            rbSingleSpecification = (CustomFontRadioButton) itemView.findViewById(R.id
                    .rbSingleSpecification);
            rbMultipleSpecification = (CustomFontCheckBox) itemView.findViewById(R.id
                    .rbMultipleSpecification);
            tvSpecificationItemDescription = (CustomTextView) itemView.findViewById(R.id
                    .tvSpecificationItemDescription);
            tvSpecificationItemPrice = (CustomTextView) itemView.findViewById(R.id
                    .tvSpecificationItemPrice);

        }


    }
}
