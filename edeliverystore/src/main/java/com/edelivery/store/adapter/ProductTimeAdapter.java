package com.edelivery.store.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.edelivery.store.AddProductActivity;
import com.edelivery.store.models.datamodel.ProductDayTime;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;

import java.util.ArrayList;

/**
 * Created by Elluminati Mohit on 5/11/2017.
 */

public class ProductTimeAdapter extends RecyclerView.Adapter<ProductTimeAdapter.ViewHolder>
        implements View.OnClickListener {


    private ArrayList<ProductDayTime> dayTimeList;
    private AddProductActivity addProductActivity;
    private ArrayList<ProductDayTime> storeTimeList;
    private int position;


    public ProductTimeAdapter(AddProductActivity context, ArrayList<ProductDayTime> storeTimeList) {
        this.storeTimeList = storeTimeList;
        this.addProductActivity = context;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBullet:
                ImageView imageView = (ImageView) v;
                int position = (int) imageView.getTag();
                storeTimeList.remove(storeTimeList.get(position));
//                addProductActivity.deleteSpecificTime(storeTimeList.get(position));
                notifyDataSetChanged();
                break;
        }
    }

    @NonNull
    @Override
    public ProductTimeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(addProductActivity).inflate(R.layout.adapter_add_new_time,
                parent,
                false);
        return new ProductTimeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (storeTimeList != null && storeTimeList.size() > 0) {

            holder.llNewTime.setVisibility(View.VISIBLE);

            ProductDayTime storeTime = storeTimeList.get(position);
            if (storeTime != null) {

                holder.tvFromTime.setText(storeTime.getProductOpenTime());
                holder.tvToTime.setText(storeTime.getProductCloseTime());
                holder.ivBullet.setTag(position);
            }
        } else {
            holder.llNewTime.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return storeTimeList.size();
    }

    public void setStoreTimeList(ArrayList<ProductDayTime> storeOpenTimeList) {
        storeTimeList = storeOpenTimeList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CustomTextView tvFromTime, tvToTime;
        ImageView ivBullet;
        LinearLayout llNewTime;

        public ViewHolder(View itemView) {
            super(itemView);
            tvFromTime = (CustomTextView) itemView.findViewById(R.id.tvFromTime);
            tvToTime = (CustomTextView) itemView.findViewById(R.id.tvToTime);
            llNewTime = itemView.findViewById(R.id.llNewTime);
            ivBullet = (ImageView) itemView.findViewById(R.id.ivBullet);
            ivBullet.setOnClickListener(ProductTimeAdapter.this);
        }
    }
}
