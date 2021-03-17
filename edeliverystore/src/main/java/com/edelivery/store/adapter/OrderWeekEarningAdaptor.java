package com.edelivery.store.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edelivery.store.models.datamodel.Analytic;
import com.edelivery.store.parse.ParseContent;
import com.edelivery.store.utils.Utilities;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Created by elluminati on 28-Jun-17.
 */

public class OrderWeekEarningAdaptor extends RecyclerView.Adapter<OrderWeekEarningAdaptor
        .OrderDayView> {

    private List<Object> orderPaymentsItemList;
    private Context context;
    private ParseContent parseContent;

    public OrderWeekEarningAdaptor(Context context, List<Object> orderPaymentsItemList) {
        this.orderPaymentsItemList = orderPaymentsItemList;
        this.context = context;
        parseContent = ParseContent.getParseContentInstance();
    }

    @Override
    public OrderDayView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_earning_item,
                parent, false);
        return new OrderDayView(view);
    }

    @Override
    public void onBindViewHolder(OrderDayView holder, int position) {
        Analytic analytic = (Analytic) orderPaymentsItemList.get(position);

        try {
            Date date = parseContent.dateFormat.parse(analytic.getTitle());
            holder.tvName.setText(parseContent.weekDay.format
                    (date));
            holder.tvPrice.setText(parseContent.decimalTwoDigitFormat.format(Double.valueOf(analytic
                    .getValue())));
        } catch (ParseException e) {
            Utilities.handleException(OrderWeekEarningAdaptor.class.getName(), e);
        }

    }

    @Override
    public int getItemCount() {
        return orderPaymentsItemList.size();
    }


    protected class OrderDayView extends RecyclerView.ViewHolder {

        CustomTextView tvName, tvPrice;

        public OrderDayView(View itemView) {
            super(itemView);
            tvName = (CustomTextView) itemView.findViewById(R.id.tvName);
            tvPrice = (CustomTextView) itemView.findViewById(R.id.tvPrice);

        }
    }
}
