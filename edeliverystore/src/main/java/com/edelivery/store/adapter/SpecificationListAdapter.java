package com.edelivery.store.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edelivery.store.AddItemActivity;
import com.elluminati.edelivery.store.R;
import com.edelivery.store.models.datamodel.ItemSpecification;
import com.edelivery.store.models.datamodel.ProductSpecification;
import com.edelivery.store.utils.Constant;
import com.edelivery.store.utils.PreferenceHelper;

import java.util.ArrayList;

/**
 * Adapter to display product specification and item specification at the time to add item on
 * 15-02-2017.
 */

public class SpecificationListAdapter extends RecyclerView.Adapter<SpecificationListAdapter
        .ViewHolder> implements View.OnClickListener {

    private ArrayList<ProductSpecification> specificationList;
    private ArrayList<ItemSpecification> itemSpecificationList;
    private Context context;
    private boolean isItemSpecification;
    private String listType;
    private boolean isOnClick;

    public SpecificationListAdapter(Context context, ArrayList<ProductSpecification>
            specificationList, String listType) {
        this.context = context;
        this.specificationList = specificationList;
        this.listType = listType;
    }

    public SpecificationListAdapter(Context context, ArrayList<ItemSpecification>
            itemSpecificationList, boolean isItemSpecification) {
        this.context = context;
        this.itemSpecificationList = itemSpecificationList;
        this.isItemSpecification = isItemSpecification;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_display_specification,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (isItemSpecification) {
            ItemSpecification itemSpecification = itemSpecificationList.get(position);
            holder.tvSpecification.setText(itemSpecification.getName());
            changeBulletIcon(holder.ivBullet, ((AddItemActivity) context).isEditable);
            holder.tvSubSpeciPrice.setVisibility(View.GONE);
        } else {
            if (listType.equals(Constant.ITEM)) {
                ProductSpecification productSpecification = specificationList.get(position);
                holder.tvSpecification.setText(productSpecification.getName());
                holder.tvSubSpeciPrice.setText(PreferenceHelper.getPreferenceHelper(context)
                        .getCurrency().concat(String.valueOf(productSpecification.getPrice())));
                holder.tvSubSpeciPrice.setVisibility(View.VISIBLE);
            }
        }

        holder.ivBullet.setTag(position);
        holder.llSpecification.setTag(position);
    }

    @Override
    public int getItemCount() {
        if (isItemSpecification) {
            return itemSpecificationList.size();
        } else {
            return specificationList.size();
        }
    }

    public void setClickableView(boolean isOnClick) {
        this.isOnClick = isOnClick;
    }

    @Override
    public void onClick(View v) {

        if (isOnClick) {


            switch (v.getId()) {

                case R.id.ivBullet:
                    ImageView imageView = (ImageView) v;
                    int position = (int) imageView.getTag();

                    if (isItemSpecification) {
                        ((AddItemActivity) context).deleteSpecification(itemSpecificationList.get
                                (position));
                    }
                    this.notifyDataSetChanged();
                    break;

                case R.id.llSpecification:
                    if (isItemSpecification) {
                        LinearLayout llSpecification = (LinearLayout) v;
                        Constant.updateSpeciPosition = (int) llSpecification.getTag();
                        ((AddItemActivity) context).gotoAddItemSpecification(null, (int)
                                llSpecification.getTag());

                    }
                    break;

                default:
                    break;
            }
        }
    }

    private void changeBulletIcon(ImageView ivBullet, boolean boolValue) {
        if (boolValue) {
            ivBullet.setImageResource(R.drawable.ic_cross);
            ivBullet.setPadding((int) context.getResources().getDimension(R.dimen
                    .general_small_margin), (int) context.getResources().getDimension(R.dimen
                    .general_small_margin), (int) context.getResources().getDimension(R.dimen
                    .general_small_margin), (int) context.getResources().getDimension(R.dimen
                    .general_small_margin));
        } else {
            ivBullet.setImageResource(R.drawable.ic_black);
            ivBullet.setPadding((int) context.getResources().getDimension(R.dimen
                    .general_top_margin), (int) context.getResources().getDimension(R.dimen
                    .general_top_margin), (int) context.getResources().getDimension(R.dimen
                    .general_top_margin), (int) context.getResources().getDimension(R.dimen
                    .general_top_margin));
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvSpecification, tvSubSpeciPrice;
        private ImageView ivBullet;
        private LinearLayout llSpecification;

        ViewHolder(View itemView) {
            super(itemView);

            tvSubSpeciPrice = (TextView) itemView.findViewById(R.id.tvSubSpeciPrice);
            tvSpecification = (TextView) itemView.findViewById(R.id.tvSpecification);
            ivBullet = (ImageView) itemView.findViewById(R.id.ivBullet);
            ivBullet.setOnClickListener(SpecificationListAdapter.this);
            llSpecification = (LinearLayout) itemView.findViewById(R.id.llSpecification);
            llSpecification.setOnClickListener(SpecificationListAdapter.this);
        }
    }
}
