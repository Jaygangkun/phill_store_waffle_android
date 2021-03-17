package com.edelivery.store.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edelivery.store.models.datamodel.OrderPaymentDetail;
import com.edelivery.store.parse.ParseContent;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;

import java.util.List;

/**
 * Created by elluminati on 28-Jun-17.
 */

public class OrderDayEarningAdaptor extends RecyclerView.Adapter<OrderDayEarningAdaptor
        .OrderDayView> {

    private List<Object> orderPaymentsItemList;
    private Context context;
    private ParseContent parseContent;

    public OrderDayEarningAdaptor(Context context, List<Object> orderPaymentsItemList) {
        this.orderPaymentsItemList = orderPaymentsItemList;
        this.context = context;
        parseContent = ParseContent.getParseContentInstance();
    }

    @Override
    public OrderDayView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout
                        .item_payment_and_earning,
                parent, false);
        return new OrderDayView(view);
    }

    @Override
    public void onBindViewHolder(OrderDayView holder, int position) {
        OrderPaymentDetail orderPayment = (OrderPaymentDetail) orderPaymentsItemList.get(position);
        holder.tvOderNumber.setText(String.valueOf(orderPayment.getOrderUniqueId()));
        holder.tvTotal.setText(parseContent.decimalTwoDigitFormat.format(orderPayment.getTotal()));
        holder.tvOrderFees.setText(parseContent.decimalTwoDigitFormat.format(orderPayment
                .getTotalOrderPrice()));
        holder.tvProfit.setText(parseContent.decimalTwoDigitFormat.format(orderPayment
                .getTotalStoreIncome()));
        holder.tvPaidShare.setText(parseContent.decimalTwoDigitFormat.format(orderPayment
                .getStoreHaveServicePayment()));
        holder.tvPaidOrder.setText(parseContent.decimalTwoDigitFormat.format(orderPayment
                .getStoreHaveOrderPayment()));
        holder.tvEarn.setText(parseContent.decimalTwoDigitFormat.format(orderPayment
                .getPayToStore()));
//        if (orderPayment.isIsPaymentModeCash()) {
//            holder.tvPayBy.setText(context.getResources().getString(R.string.text_cash));
//        } else {
//            holder.tvPayBy.setText(context.getResources().getString(R.string.text_card));
//        }


    }

    @Override
    public int getItemCount() {
        return orderPaymentsItemList.size();
    }


    protected class OrderDayView extends RecyclerView.ViewHolder {

        CustomTextView tvOderNumber, tvPayBy, tvTotal, tvServiceFees, tvProfit, tvPaidShare,
                tvPaidOrder, tvEarn, tvOrderFees;

        public OrderDayView(View itemView) {
            super(itemView);
            tvOderNumber = (CustomTextView) itemView.findViewById(R.id.tvOderNumber);
            tvPayBy = (CustomTextView) itemView.findViewById(R.id.tvPayBy);
            tvTotal = (CustomTextView) itemView.findViewById(R.id.tvTotal);
            tvServiceFees = (CustomTextView) itemView.findViewById(R.id.tvServiceFees);
            tvProfit = (CustomTextView) itemView.findViewById(R.id.tvProfit);
            tvPaidShare = (CustomTextView) itemView.findViewById(R.id.tvPaidShare);
            tvPaidOrder = (CustomTextView) itemView.findViewById(R.id.tvPaidOrder);
            tvEarn = (CustomTextView) itemView.findViewById(R.id.tvEarn);
            tvOrderFees = (CustomTextView) itemView.findViewById(R.id.tvOrderFees);

        }
    }
}
