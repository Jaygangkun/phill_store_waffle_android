package com.edelivery.store.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.edelivery.store.models.singleton.SubStoreAccess;
import com.edelivery.store.utils.PreferenceHelper;
import com.elluminati.edelivery.store.R;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Adapter to display menu in ProfileFragment tab on 18-02-2017.
 */

public class ProfileMenuAdapter extends RecyclerView.Adapter<ProfileMenuAdapter.ViewHolder> {

    private Context context;
    private TypedArray menuItemList;
    private TypedArray menuItemListName;

    public ProfileMenuAdapter(Context context) {
        this.context = context;
        menuItemList = context.getResources().obtainTypedArray(R.array.profileMenuIcon);
        menuItemListName = context.getResources().obtainTypedArray(R.array.profileMenuItem);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_profile_menu, parent,
                false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.ivMenuIcon.setImageDrawable(menuItemList.getDrawable(position));
        holder.tvMenuName.setText(menuItemListName.getString(position));

        if (isBankDetailTabHide(position) ||
                isPaymentTabHide(position) || isDocumentTabHide(position) || isProfileTabHide(position) || isSubStoreTabHide(position) || isPromoTabHide(position) || isCreateOrderTabHide(position) || isGroupTabHide(position) || isSettingsTabHide(position) || isHistoryTabHide(position) || isEarningTabHide(position)) {
            holder.itemView.setVisibility(View.GONE);
            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
            layoutParams.height = 0;
            holder.itemView.setLayoutParams(layoutParams);
        } else {
            holder.itemView.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            holder.itemView.setLayoutParams(layoutParams);
        }

    }

    @Override
    public int getItemCount() {
        return menuItemList.length();
    }

    private boolean isPromoTabHide(int position) {
        return TextUtils.equals(menuItemListName.getString(position), context.getResources()
                .getString(R.string.text_promo)) && !(PreferenceHelper.getPreferenceHelper
                (context).getIsStoreAddPromoCode() && SubStoreAccess.getInstance().isAccess(SubStoreAccess.PROMO_CODE));

    }

    private boolean isCreateOrderTabHide(int position) {

        return (TextUtils.equals(menuItemListName.getString(position), context.getResources()
                .getString(R.string.text_create_order)) || TextUtils.equals(menuItemListName
                .getString(position), context.getResources()
                .getString(R.string.text_instant_order))) && !(PreferenceHelper.getPreferenceHelper
                (context).getIsStoreCreateOrder() && SubStoreAccess.getInstance().isAccess(SubStoreAccess.CREATE_ORDER));

    }

    private boolean isGroupTabHide(int position) {

        return TextUtils.equals(menuItemListName.getString(position), context.getResources()
                .getString(R.string.text_group_of_category)) && !(PreferenceHelper.getPreferenceHelper
                (context).getIsStoreCanCreateGroup() && SubStoreAccess.getInstance().isAccess(SubStoreAccess.GROUP));

    }

    private boolean isHistoryTabHide(int position) {

        return TextUtils.equals(menuItemListName.getString(position), context.getResources()
                .getString(R.string.text_history)) && !SubStoreAccess.getInstance().isAccess(SubStoreAccess.HISTORY);

    }

    private boolean isEarningTabHide(int position) {

        return TextUtils.equals(menuItemListName.getString(position), context.getResources()
                .getString(R.string.text_earning)) && !SubStoreAccess.getInstance().isAccess(SubStoreAccess.EARNING);

    }

    private boolean isSettingsTabHide(int position) {

        return TextUtils.equals(menuItemListName.getString(position), context.getResources()
                .getString(R.string.text_settings)) && !SubStoreAccess.getInstance().isAccess(SubStoreAccess.SETTING);

    }

    private boolean isProfileTabHide(int position) {

        return TextUtils.equals(menuItemListName.getString(position), context.getResources()
                .getString(R.string.text_profile)) && !SubStoreAccess.getInstance().isAccess(SubStoreAccess.SUB_STORE);

    }

    private boolean isSubStoreTabHide(int position) {

        return TextUtils.equals(menuItemListName.getString(position), context.getResources()
                .getString(R.string.text_sub_store)) && !SubStoreAccess.getInstance().isAccess(SubStoreAccess.SUB_STORE);

    }

    private boolean isDocumentTabHide(int position) {

        return TextUtils.equals(menuItemListName.getString(position), context.getResources()
                .getString(R.string.text_document)) && !SubStoreAccess.getInstance().isAccess(SubStoreAccess.SUB_STORE);

    }

    private boolean isPaymentTabHide(int position) {

        return TextUtils.equals(menuItemListName.getString(position), context.getResources()
                .getString(R.string.text_payments)) && !SubStoreAccess.getInstance().isAccess(SubStoreAccess.SUB_STORE);

    }

    private boolean isBankDetailTabHide(int position) {

        return TextUtils.equals(menuItemListName.getString(position), context.getResources()
                .getString(R.string.text_bank_details)) /*&& !SubStoreAccess.getInstance().isAccess(SubStoreAccess.SUB_STORE)*/;

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvMenuName;
        ImageView ivMenuIcon;

        ViewHolder(View itemView) {
            super(itemView);

            tvMenuName = (TextView) itemView.findViewById(R.id.tvMenuName);
            ivMenuIcon = (ImageView) itemView.findViewById(R.id.ivMenuIcon);
        }
    }
}
