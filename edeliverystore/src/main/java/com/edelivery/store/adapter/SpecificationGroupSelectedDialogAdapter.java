package com.edelivery.store.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edelivery.store.models.datamodel.ItemSpecification;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;

import java.util.ArrayList;

/**
 * Created by Elluminati Mohit on 4/22/2017.
 */

public class SpecificationGroupSelectedDialogAdapter extends RecyclerView
        .Adapter<SpecificationGroupSelectedDialogAdapter.MyViewHolder> {

    private ArrayList<ItemSpecification> specificationGroupItems;
    private Context context;

    public SpecificationGroupSelectedDialogAdapter(Context context,
                                                   ArrayList<ItemSpecification>
                                                           specificationGroupItems) {
        this.context = context;
        this.specificationGroupItems = specificationGroupItems;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.select_iteam_raw, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.productName.setText(specificationGroupItems.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return specificationGroupItems.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        CustomTextView productName;

        public MyViewHolder(View itemView) {
            super(itemView);
            productName = (CustomTextView) itemView.findViewById(R.id.tvItemName_root);
        }
    }
}
