package com.edelivery.store.adapter;

import android.content.Context;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edelivery.store.models.datamodel.Invoice;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;

import java.util.ArrayList;

/**
 * Created by elluminati on 25-Apr-17.
 */

public class OrderInvoiceAdapter extends RecyclerView.Adapter<OrderInvoiceAdapter
        .InvoiceViewHolder> {
    private ArrayList<Invoice> invoices;
    private Context context;

    public OrderInvoiceAdapter(ArrayList<Invoice> invoices) {
        this.invoices = invoices;
    }

    @Override
    public InvoiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout
                .layout_invoice_raw_item, parent, false);
        return new InvoiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(InvoiceViewHolder holder, int position) {

        holder.tvInvoiceTitle.setText(invoices.get(position).getTitle());
        holder.tvSubInvoiceTitle.setText(invoices.get(position).getSubTitle());
        holder.tvInvoicePrice.setText(invoices.get(position).getPrice());
        if (TextUtils.equals(context.getResources().getString(R.string.text_total_item_cost),
                invoices.get(position).getTitle()) || TextUtils.equals(context.getResources()
                        .getString(R
                                .string.text_total_service_cost),
                invoices.get(position).getTitle())) {
            holder.tvInvoicePrice.setTextColor(ResourcesCompat.getColor(context.getResources(), R
                    .color.color_app_heading, null));
            holder.tvInvoiceTitle.setTextColor(ResourcesCompat.getColor(context.getResources(), R
                    .color.color_app_heading, null));
            holder.tvInvoiceTitle.setAllCaps(true);
        }

    }

    @Override
    public int getItemCount() {
        return invoices.size();
    }

    protected class InvoiceViewHolder extends RecyclerView.ViewHolder {
        CustomTextView tvInvoiceTitle, tvSubInvoiceTitle, tvInvoicePrice;

        public InvoiceViewHolder(View itemView) {
            super(itemView);
            tvInvoicePrice = (CustomTextView) itemView.findViewById(R.id.tvInvoicePrice);
            tvInvoiceTitle = (CustomTextView) itemView.findViewById(R.id.tvInvoiceTitle);
            tvSubInvoiceTitle = (CustomTextView) itemView.findViewById(R.id.tvSubInvoiceTitle);
        }
    }
}
