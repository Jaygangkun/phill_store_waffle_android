package com.edelivery.store.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.edelivery.store.models.datamodel.PromoRecursionData;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;

import java.util.ArrayList;

/**
 * Created by elluminati on 19-Jan-18.
 */

public class PromoRecursionAdapter extends RecyclerView.Adapter<PromoRecursionAdapter
        .PromoRecursionDataHolder> {
    private Context context;
    private ArrayList<PromoRecursionData> dataArrayList;
    private ArrayList<String> selectedList;


    public PromoRecursionAdapter(Context context, ArrayList<PromoRecursionData> dataArrayList,
                                 ArrayList<String> selectedList) {
        this.context = context;
        this.dataArrayList = dataArrayList;
        this.selectedList = selectedList;
    }

    @Override
    public PromoRecursionDataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout
                .item_promo_recursion_data, parent, false);
        return new PromoRecursionDataHolder(view);
    }

    @Override
    public void onBindViewHolder(PromoRecursionDataHolder holder, int position) {
        holder.tvRecursionData.setText(dataArrayList.get(position).getDisplayData());
        holder.cbRecursionData.setChecked(dataArrayList.get(position).isSelected());
    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    protected class PromoRecursionDataHolder extends RecyclerView.ViewHolder {
        private CustomTextView tvRecursionData;
        private CheckBox cbRecursionData;

        public PromoRecursionDataHolder(View itemView) {
            super(itemView);
            tvRecursionData = (CustomTextView) itemView.findViewById(R.id.tvRecursionData);
            cbRecursionData = (CheckBox) itemView.findViewById(R.id.cbRecursionData);
        }
    }
}
