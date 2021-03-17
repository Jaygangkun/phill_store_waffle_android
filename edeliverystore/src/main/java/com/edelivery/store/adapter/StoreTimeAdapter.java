package com.edelivery.store.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.edelivery.store.StoreTimeActivity;
import com.edelivery.store.models.datamodel.DayTime;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;

import java.util.ArrayList;

/**
 * Created by Elluminati Mohit on 5/11/2017.
 */

public class StoreTimeAdapter extends RecyclerView.Adapter<StoreTimeAdapter.ViewHolder>
        implements View.OnClickListener {

    private ArrayList<DayTime> storeTimeList;
    private StoreTimeActivity storeTimeActivity;

    public StoreTimeAdapter(ArrayList<DayTime> storeOpenTimeList, StoreTimeActivity
            storeTimeActivity) {
        this.storeTimeList = storeOpenTimeList;
        this.storeTimeActivity = storeTimeActivity;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(storeTimeActivity).inflate(R.layout.adapter_add_new_time,
                parent,
                false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        DayTime storeTime = storeTimeList.get(position);
        holder.tvFromTime.setText(storeTime.getStoreOpenTime());
        holder.tvToTime.setText(storeTime.getStoreCloseTime());
        holder.ivBullet.setTag(position);

    }


    @Override
    public int getItemCount() {
        return storeTimeList.size();
    }

    @Override
    public void onClick(View view) {

        if (storeTimeActivity.isEditable) {
            switch (view.getId()) {
                case R.id.ivBullet:
                    ImageView imageView = (ImageView) view;
                    int position = (int) imageView.getTag();
                    storeTimeActivity.deleteSpecificTime(storeTimeList.get(position));
                    notifyDataSetChanged();
                    break;
            }
        }

    }

    public void setStoreTimeList(ArrayList<DayTime> storeOpenTimeList) {
        storeTimeList = storeOpenTimeList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CustomTextView tvFromTime, tvToTime;
        ImageView ivBullet;

        public ViewHolder(View itemView) {
            super(itemView);
            tvFromTime = (CustomTextView) itemView.findViewById(R.id.tvFromTime);
            tvToTime = (CustomTextView) itemView.findViewById(R.id.tvToTime);
            ivBullet = (ImageView) itemView.findViewById(R.id.ivBullet);
            ivBullet.setOnClickListener(StoreTimeAdapter.this);
        }
    }
}
