package com.edelivery.store.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elluminati.edelivery.store.R;
import com.edelivery.store.models.datamodel.Product;
import com.edelivery.store.widgets.CustomTextView;

import java.util.ArrayList;

/**
 * Created by Elluminati Mohit on 4/22/2017.
 */

public class ProductSelectedDialogAdapter extends RecyclerView.Adapter<ProductSelectedDialogAdapter.MyViewHolder> {

    private ArrayList<Product> productList;
    private Context context;
    public ProductSelectedDialogAdapter(Context context,ArrayList<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.select_iteam_raw, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.productName.setText(productList.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        CustomTextView productName;
        public MyViewHolder(View itemView) {
            super(itemView);
            productName = (CustomTextView) itemView.findViewById(R.id.tvItemName_root);
        }
    }
}
