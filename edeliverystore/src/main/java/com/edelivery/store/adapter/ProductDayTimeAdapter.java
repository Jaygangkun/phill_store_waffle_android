package com.edelivery.store.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.edelivery.store.AddProductActivity;
import com.edelivery.store.models.datamodel.ProductDayTime;
import com.edelivery.store.models.datamodel.ProductTime;
import com.elluminati.edelivery.store.R;

import java.util.ArrayList;

/**
 * Created by Elluminati Mohit on 5/11/2017.
 */

public class ProductDayTimeAdapter extends RecyclerView.Adapter<ProductDayTimeAdapter.ViewHolder> {

    private ArrayList<ProductTime> productTimeArrayList;
    private AddProductActivity addProductActivity;
    private ProductTimeAdapter productTimeAdapter;
    private ArrayList<String> dayList = new ArrayList<String>() {{
        add("Sunday");
        add("Monday");
        add("Tuesday");
        add("Wednesday");
        add("Thursday");
        add("Friday");
        add("Saturday");
    }};
//    private dayTimes;

    public ProductDayTimeAdapter(ArrayList<ProductTime> storeOpenTimeList, AddProductActivity
            addProductActivity) {
        this.productTimeArrayList = storeOpenTimeList;
        this.addProductActivity = addProductActivity;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(addProductActivity).inflate(R.layout.adapter_add_new_product_time,
                parent,
                false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        ProductTime storeTime = productTimeArrayList.get(position);
        holder.tvWeekDay.setText(dayList.get(storeTime.getDay()));
        ArrayList<ProductDayTime> dayTimes = (ArrayList<ProductDayTime>) storeTime.getDayTime();

        productTimeAdapter = new ProductTimeAdapter(addProductActivity, dayTimes);
        holder.rcvDayTime.setLayoutManager(new LinearLayoutManager(addProductActivity));
        holder.rcvDayTime.setAdapter(productTimeAdapter);
        if (dayTimes != null && dayTimes.size() > 0) {
            holder.llProductTime.setVisibility(View.VISIBLE);
            holder.tvWeekDay.setVisibility(View.VISIBLE);
            productTimeArrayList.get(position).setProductOpen(true);
        } else {
            if (!productTimeArrayList.get(position).isProductOpenFullTime()) {
                productTimeArrayList.get(position).setProductOpen(false);
            }
            holder.llProductTime.setVisibility(View.GONE);
            holder.tvWeekDay.setVisibility(View.GONE);
        }

    }


    @Override
    public int getItemCount() {
        return productTimeArrayList.size();
    }


    public void setProductTimeArrayList(ArrayList<ProductTime> productDayTimes) {
        productTimeArrayList = productDayTimes;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvWeekDay;
        RecyclerView rcvDayTime;
        LinearLayout llProductTime;

        public ViewHolder(View itemView) {
            super(itemView);
            tvWeekDay = itemView.findViewById(R.id.tvWeekDay);
            rcvDayTime = (RecyclerView) itemView.findViewById(R.id.rcvDayTime);
            llProductTime = itemView.findViewById(R.id.llProductTime);
        }
    }


}
