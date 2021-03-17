package com.edelivery.store.adapter;

import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edelivery.store.PromoCodeActivity;
import com.edelivery.store.models.datamodel.PromoCodes;
import com.edelivery.store.parse.ParseContent;
import com.edelivery.store.utils.Constant;
import com.edelivery.store.utils.PreferenceHelper;
import com.edelivery.store.utils.Utilities;
import com.edelivery.store.widgets.CustomFontTextViewTitle;
import com.edelivery.store.widgets.CustomInputEditText;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Created by elluminati on 05-Dec-17.
 */

public class PromoCodeAdapter extends RecyclerView.Adapter<PromoCodeAdapter.PromoCodeItemView> {
    private List<PromoCodes> codesItemList;
    private PromoCodeActivity promoCodeActivity;


    public PromoCodeAdapter(PromoCodeActivity promoCodeActivity, List<PromoCodes>
            codesItemList) {
        this.codesItemList = codesItemList;
        this.promoCodeActivity = promoCodeActivity;
    }

    @Override
    public PromoCodeItemView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_promo_code,
                parent, false);
        return new PromoCodeItemView(view);
    }

    @Override
    public void onBindViewHolder(PromoCodeItemView holder, int position) {
        final PromoCodes promoCodes = codesItemList.get(position);
        holder.tvPromoName.setText(promoCodes.getPromoCodeName());
        holder.tvPromoDescription.setText(promoCodes.getPromoDetails());
        if (promoCodes.getPromoCodeType() == Constant.Type.ABSOLUTE) {
            holder.tvPromoPricing.setText(PreferenceHelper.getPreferenceHelper(promoCodeActivity)
                    .getCurrency() + promoCodes.getPromoCodeValue());
        } else {
            holder.tvPromoPricing.setText(+promoCodes.getPromoCodeValue() + "%");
        }

        try {

            if (promoCodes.isPromoHaveDate()) {
                Date startDate = ParseContent.getParseContentInstance().webFormat.parse
                        (promoCodes.getPromoStartDate());
                holder.etPromoStartDate.setText(ParseContent.getParseContentInstance().dateFormat
                        .format(startDate));
                Date expDate = ParseContent.getParseContentInstance().webFormat.parse
                        (promoCodes.getPromoExpireDate());
                holder.etPromoExpDate.setText(ParseContent.getParseContentInstance().dateFormat
                        .format(expDate));
                holder.etPromoExpDate.setVisibility(View.VISIBLE);
                holder.etPromoStartDate.setVisibility(View.VISIBLE);
            } else {
                holder.etPromoExpDate.setVisibility(View.GONE);
                holder.etPromoStartDate.setVisibility(View.GONE);
            }

        } catch (ParseException e) {
            Utilities.handleException(PromoCodeAdapter.class.getName(), e);
        }
        holder.switchActivePromo.setChecked(promoCodes.isIsActive());
        holder.switchActivePromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promoCodes.setIsActive(!promoCodes.isIsActive());
                promoCodeActivity.updatePromoCode(promoCodes);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promoCodeActivity.goToAddPromoActivity(promoCodes);
            }
        });

    }

    @Override
    public int getItemCount() {
        return codesItemList.size();
    }

    protected class PromoCodeItemView extends RecyclerView.ViewHolder {
        CustomFontTextViewTitle tvPromoName, tvPromoPricing;
        CustomTextView tvPromoDescription;
        CustomInputEditText etPromoStartDate, etPromoExpDate;
        SwitchCompat switchActivePromo;

        public PromoCodeItemView(View itemView) {
            super(itemView);
            tvPromoName = (CustomFontTextViewTitle) itemView.findViewById(R.id.tvPromoCode);
            tvPromoPricing = (CustomFontTextViewTitle) itemView.findViewById(R.id.tvPromoPricing);
            tvPromoDescription = (CustomTextView) itemView.findViewById(R.id.tvPromoDescription);
            etPromoStartDate = (CustomInputEditText) itemView.findViewById(R.id.etPromoStartDate);
            etPromoExpDate = (CustomInputEditText) itemView.findViewById(R.id.etPromoExpDate);
            switchActivePromo = (SwitchCompat) itemView.findViewById(R.id.switchActivePromo);
        }
    }
}
