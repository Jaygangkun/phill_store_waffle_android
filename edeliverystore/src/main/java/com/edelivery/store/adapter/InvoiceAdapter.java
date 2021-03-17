package com.edelivery.store.adapter;

import android.content.Context;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.edelivery.store.models.datamodel.Invoice;
import com.edelivery.store.parse.ParseContent;
import com.edelivery.store.utils.SectionedRecyclerViewAdapter;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;

import java.util.ArrayList;

/**
 * Created by Elluminati Mohit on 5/20/2017.
 */

public class InvoiceAdapter extends SectionedRecyclerViewAdapter {

    private ArrayList<ArrayList<Invoice>> invoices;
    private Context context;
    private ParseContent parseContent;

    public InvoiceAdapter(ArrayList<ArrayList<Invoice>> invoices) {
        this.invoices = invoices;
        parseContent = ParseContent.getParseContentInstance();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                return new InvoiceViewHeader(LayoutInflater.from(parent.getContext()).inflate(R
                        .layout
                        .item_invoice_section, parent, false));
            case VIEW_TYPE_ITEM:
                return new InvoiceViewHolder(LayoutInflater.from(parent.getContext()).inflate(R
                        .layout
                        .layout_invoice_raw_item, parent, false));

            default:
                // do with default
                break;
        }
        return null;
    }


    @Override
    public int getSectionCount() {
        return invoices.size();
    }

    @Override
    public int getItemCount(int section) {
        return invoices.get(section).size();
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int section) {
        InvoiceViewHeader invoiceViewHolder = (InvoiceViewHeader) holder;
        invoiceViewHolder.tvTag.setText(invoices.get(section).get(0).getTagTitle());

        if (section == 0) {
            invoiceViewHolder.itemView.setVisibility(View.GONE);
            invoiceViewHolder.itemView.getLayoutParams().height = 0;
        } else {
            invoiceViewHolder.itemView.setVisibility(View.VISIBLE);
            invoiceViewHolder.itemView.getLayoutParams().height = WindowManager.LayoutParams
                    .WRAP_CONTENT;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int section, int
            relativePosition, int absolutePosition) {
        InvoiceViewHolder invoiceViewHolder = (InvoiceViewHolder) holder;
        invoiceViewHolder.tvInvoiceTitle.setText(invoices.get(section).get(relativePosition)
                .getTitle());
        invoiceViewHolder.tvSubInvoiceTitle.setText(invoices.get(section).get(relativePosition)
                .getSubTitle());
        invoiceViewHolder.tvInvoicePrice.setText((invoices.get(section).get(relativePosition)
                .getPrice()));

        if (TextUtils.equals(context.getResources().getString(R.string.text_total_item_cost),
                invoices.get(section).get(relativePosition).getTitle()) || TextUtils.equals
                (context.getResources()
                                .getString(R
                                        .string.text_total_service_cost),
                        invoices.get(section).get(relativePosition).getTitle())) {
            invoiceViewHolder.tvInvoicePrice.setTextColor(ResourcesCompat.getColor(context
                    .getResources(), R
                    .color.color_app_heading, null));
            invoiceViewHolder.tvInvoiceTitle.setTextColor(ResourcesCompat.getColor(context
                    .getResources(), R
                    .color.color_app_heading, null));
            invoiceViewHolder.tvInvoiceTitle.setAllCaps(true);
        }
    }


    public class InvoiceViewHolder extends RecyclerView.ViewHolder {

        CustomTextView tvInvoiceTitle, tvSubInvoiceTitle, tvInvoicePrice;

        public InvoiceViewHolder(View itemView) {
            super(itemView);
            tvInvoicePrice = (CustomTextView) itemView.findViewById(R.id.tvInvoicePrice);
            tvInvoiceTitle = (CustomTextView) itemView.findViewById(R.id.tvInvoiceTitle);
            tvSubInvoiceTitle = (CustomTextView) itemView.findViewById(R.id.tvSubInvoiceTitle);
        }
    }

    protected class InvoiceViewHeader extends RecyclerView.ViewHolder {
        CustomTextView tvTag;

        public InvoiceViewHeader(View itemView) {
            super(itemView);
            tvTag = (CustomTextView) itemView.findViewById(R.id.tvSection_root);
        }
    }
}
