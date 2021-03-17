package com.edelivery.store.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edelivery.store.models.datamodel.Analytic;
import com.edelivery.store.widgets.CustomFontTextViewTitle;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;

import java.util.ArrayList;

/**
 * Created by elluminati on 28-Jun-17.
 */

public class OrderAnalyticAdapter extends RecyclerView.Adapter<OrderAnalyticAdapter
        .OrderAnalyticView> {

    private ArrayList<Analytic> arrayListProviderAnalytic;

    public OrderAnalyticAdapter(ArrayList<Analytic> arrayListProviderAnalytic) {
        this.arrayListProviderAnalytic = arrayListProviderAnalytic;
    }

    @Override
    public OrderAnalyticView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_analitic_item,
                parent, false);
        return new OrderAnalyticView(view);
    }

    @Override
    public void onBindViewHolder(OrderAnalyticView holder, int position) {
        holder.tvAnalyticName.setText(arrayListProviderAnalytic.get(position).getTitle());
        holder.tvAnalyticValue.setText(arrayListProviderAnalytic.get(position).getValue());
    }

    @Override
    public int getItemCount() {
        return arrayListProviderAnalytic.size();
    }

    protected class OrderAnalyticView extends RecyclerView.ViewHolder {

        CustomTextView tvAnalyticValue;
        CustomFontTextViewTitle tvAnalyticName;

        public OrderAnalyticView(View itemView) {
            super(itemView);
            tvAnalyticValue = (CustomTextView) itemView.findViewById(R.id.tvAnalyticValue);
            tvAnalyticName = (CustomFontTextViewTitle) itemView.findViewById(R.id.tvAnalyticName);
        }
    }
}
