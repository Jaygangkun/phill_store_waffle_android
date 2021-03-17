package com.edelivery.store.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.edelivery.store.models.datamodel.ProductSpecification;
import com.edelivery.store.parse.ParseContent;
import com.edelivery.store.utils.PreferenceHelper;
import com.edelivery.store.utils.Utilities;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Adapter to add item sub specification from list of product specification on 23-02-2017.
 */

public class AddItemSpeciAdapter extends RecyclerView.Adapter<AddItemSpeciAdapter.ViewHolder>
        implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    private Context context;
    private ArrayList<ProductSpecification> newSpeciList;
    private boolean isSingle;
    private int lastSelcted = -1;
    private ParseContent parseContent;


    public AddItemSpeciAdapter(Context context, ArrayList<ProductSpecification> productSpeciList) {
        this.context = context;
        this.newSpeciList = productSpeciList;
        parseContent = ParseContent.getParseContentInstance();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_add_item_specification,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Utilities.printLog("tag", "newSpeciList-- " + newSpeciList.size());
        final ProductSpecification productSpecification = newSpeciList.get(holder
                .getAdapterPosition());
        holder.tvSpeciName.setText(productSpecification.getName());
        holder.tvDefault.setSelected(productSpecification.isIsDefaultSelected());
        if (productSpecification.getPrice() > 0) {
            holder.etSpeciPrice.setText(parseContent.decimalTwoDigitFormat.format
                    (productSpecification.getPrice()));
        }
        holder.checkboxSpecification.setOnCheckedChangeListener(null);
        holder.checkboxSpecification.setChecked(productSpecification.isIsUserSelected());
        holder.tvDefault.setTag(position);
        holder.checkboxSpecification.setOnCheckedChangeListener(this);
        holder.checkboxSpecification.setTag(position);

        holder.etSpeciPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Utilities.isDecimalAndGraterThenZero(s.toString())) {
                    newSpeciList.get(holder.getAdapterPosition()).setPrice(Utilities.roundDecimal
                            (Double.valueOf(s.toString())));
                }
            }
        });


        if (isSingle) {
            if (lastSelcted == -1 && productSpecification.isIsDefaultSelected()) {
                lastSelcted = position;
            }
            if (lastSelcted == position && holder.checkboxSpecification.isChecked()) {
                holder.tvDefault.setTextColor(ContextCompat.getColor(context, R.color
                        .color_app_text));
                holder.tvDefault.setSelected(true);
                productSpecification.setIsDefaultSelected(true);

            } else {
                holder.tvDefault.setTextColor(ContextCompat.getColor(context, R.color
                        .color_app_gray));
                holder.tvDefault.setSelected(false);
                productSpecification.setIsDefaultSelected(false);
            }
        } else {
            if (productSpecification.isIsDefaultSelected() && holder.checkboxSpecification
                    .isChecked()) {
                holder.tvDefault.setTextColor(ContextCompat.getColor(context, R.color
                        .color_app_text));
                holder.tvDefault.setSelected(true);
                productSpecification.setIsDefaultSelected(true);
            } else {
                holder.tvDefault.setTextColor(ContextCompat.getColor(context, R.color
                        .color_app_gray));
                holder.tvDefault.setSelected(false);
                productSpecification.setIsDefaultSelected(false);
            }
        }

        holder.etSpeciPrice.setEnabled(holder.checkboxSpecification.isChecked());
        holder.tvDefault.setEnabled(holder.checkboxSpecification.isChecked());
        holder.tvSpeciName.setEnabled(holder.checkboxSpecification.isChecked());
        holder.tvCurrency.setText(PreferenceHelper.getPreferenceHelper(context).getCurrency());


    }

    @Override
    public int getItemCount() {

        return newSpeciList.size();
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.tvDefault) {


            Utilities.printLog("tag", "is Single == " + isSingle);
            TextView textView = (TextView) v;
            if (textView.isSelected()) {
                textView.setTextColor(ContextCompat.getColor(context, R.color.color_app_gray));
                newSpeciList.get((int) textView.getTag()).setIsDefaultSelected(false);
                textView.setSelected(false);
            } else {
                if (newSpeciList.get((int) textView.getTag()).isIsUserSelected()) {

                    if (!isSingle) {
                        newSpeciList.get((int) textView.getTag()).setIsDefaultSelected(true);
                    }
                    textView.setTextColor(ContextCompat.getColor(context, R.color.color_app_text));
                    textView.setSelected(true);
                    if (isSingle) {
                        lastSelcted = (int) textView.getTag();
                        notifyDataSetChanged();
                    }

                }


            }

        }
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        CheckBox cb = (CheckBox) buttonView;
        newSpeciList.get((int) cb.getTag()).setIsUserSelected(cb.isChecked());
        notifyItemChanged((Integer) cb.getTag());
    }

    public ArrayList<ProductSpecification> getUpdatedList() {
        return newSpeciList;
    }

    public void setIsSingleSelctedType(boolean isSingle) {
        this.isSingle = isSingle;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private CheckBox checkboxSpecification;
        private TextView tvDefault, tvCurrency;
        private TextInputEditText etSpeciPrice;

        private CustomTextView tvSpeciName;

        ViewHolder(View itemView) {
            super(itemView);
            tvSpeciName = (CustomTextView) itemView.findViewById(R.id.tvSpeciName);
            checkboxSpecification = (CheckBox) itemView.findViewById(R.id.checkboxSpecification);
            tvDefault = (TextView) itemView.findViewById(R.id.tvDefault);
            etSpeciPrice = (TextInputEditText) itemView.findViewById(R.id.etSpeciPrice);
            tvDefault.setOnClickListener(AddItemSpeciAdapter.this);
            tvCurrency = (TextView) itemView.findViewById(R.id.tvCurrency);


        }
    }
}
