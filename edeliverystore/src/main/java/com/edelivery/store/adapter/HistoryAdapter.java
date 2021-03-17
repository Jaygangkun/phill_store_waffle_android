package com.edelivery.store.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.edelivery.store.HistoryActivity;
import com.edelivery.store.HistoryDetailActivity;
import com.edelivery.store.models.datamodel.OrderData;
import com.edelivery.store.models.datamodel.UserDetail;
import com.edelivery.store.parse.ParseContent;
import com.edelivery.store.utils.Constant;
import com.edelivery.store.utils.PinnedHeaderItemDecoration;
import com.edelivery.store.utils.PreferenceHelper;
import com.edelivery.store.utils.Utilities;
import com.edelivery.store.widgets.CustomFontTextViewTitle;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TreeSet;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import static com.elluminati.edelivery.store.BuildConfig.IMAGE_URL;

/**
 * Created by Elluminati Mohit on 5/2/2017.
 */

public class HistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements
        PinnedHeaderItemDecoration.PinnedHeaderAdapter {

    public static final String TAG = "TripHistoryAdaptor";
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;
    private ArrayList<OrderData> orderLists;
    private TreeSet<Integer> separatorsSet;
    private ViewHolderSeparator viewHolderSeparator;
    private ParseContent parseContent;
    private SimpleDateFormat dateFormat;
    private HistoryActivity historyActivity;
    private Context context;

    public HistoryAdapter(HistoryActivity historyActivity, ArrayList<OrderData>
            orderLists, TreeSet<Integer> separatorsSet, Context context) {
        this.historyActivity = historyActivity;
        this.orderLists = orderLists;
        this.separatorsSet = separatorsSet;
        parseContent = ParseContent.getParseContentInstance();
        dateFormat = parseContent.dateFormat;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.adapter_history, parent, false);
            return new ViewHolderHistory(v);
        } else if (viewType == TYPE_SEPARATOR) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.adapter_item_section, parent, false);
            return new ViewHolderSeparator(v);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        OrderData orderData = orderLists.get(position);
        if (holder instanceof ViewHolderHistory) {
            ViewHolderHistory viewHolder = (ViewHolderHistory) holder;
            viewHolder.tvOrderNo.setText(String.valueOf(orderData.getUniqueId()));
            viewHolder.tvProfit.setText(context.getResources().getString(R.string.text_profit) +
                    " " +
                    ": " + orderData.getCurrency() + parseContent.decimalTwoDigitFormat.format(
                    orderData
                            .getStoreProfit()));
            UserDetail userdetails = orderData.getUserDetail();
            if (userdetails != null) {

                String[] strings = userdetails.getName().split(" ");
                if (strings != null && strings.length == 2) {
                    viewHolder.tvClientName.setText(strings[0]);
                } else {
                    viewHolder.tvClientName.setText(userdetails.getName());
                }

                Glide.with(historyActivity).load(IMAGE_URL + userdetails.getImageUrl()).placeholder(R
                        .drawable.placeholder).dontAnimate().fallback(R.drawable.placeholder).into
                        (viewHolder.ivClientHistory);
            }
            if (orderLists.get(position).getOrderStatus() != Constant.FINAL_ORDER_COMPLETED) {
                viewHolder.ivCanceled.setVisibility(View.VISIBLE);
                viewHolder.ivClientHistory.setColorFilter(ResourcesCompat
                        .getColor(context
                                .getResources(), R.color.color_app_transparent_white, null));
            } else {
                viewHolder.ivCanceled.setVisibility(View.GONE);
                viewHolder.ivClientHistory.setColorFilter(ResourcesCompat
                        .getColor(context
                                .getResources(), android.R.color.transparent, null));
            }
            viewHolder.tvOrderPrice.setText(PreferenceHelper.getPreferenceHelper(context).getCurrency().
                    concat(parseContent.decimalTwoDigitFormat.format(orderData
                            .getTotal())));
            try {
                viewHolder.tvOrderTime.setText(parseContent.timeFormat_am.format(parseContent
                        .webFormat.parse(orderData.getCompletedAt())).toUpperCase());
            } catch (ParseException e) {
                Utilities.printLog(TAG, e + "");
            }
            if (separatorsSet.contains(position + 1)) {
                viewHolder.ivIncludeDivider.setVisibility(View.GONE);
            }

        } else {

            Utilities.printLog("HistoryAdapter", "into else");
            ViewHolderSeparator viewHolderSeparator = (ViewHolderSeparator) holder;
            Date getDate;
            Date currentDate = new Date();
            String date = dateFormat.format(currentDate);
            Utilities.setTagBackgroundRtlView(context, viewHolderSeparator.tvSection);
            if (orderData.getCompletedAt().equals(date)) {
                viewHolderSeparator.tvSection.setText(historyActivity.getResources().getString(R
                        .string.text_today));
            } else if (orderData.getCompletedAt().equals(getYesterdayDateString())) {
                viewHolderSeparator.tvSection.setText(historyActivity.getResources().getString(R
                        .string.text_yesterday));
            } else {
                try {
                    getDate = dateFormat.parse(orderData.getCompletedAt());
                    String daySuffix = Utilities.getDayOfMonthSuffix(
                            Integer.valueOf(parseContent.day.format(getDate)));

                    viewHolderSeparator.tvSection.setText(daySuffix + " " + parseContent
                            .dateFormatMonth.format(getDate));

                } catch (ParseException e) {
                    Utilities.handleException("History_Adapter", e);
                }
            }

        }

    }

    @Override
    public int getItemCount() {
        return orderLists.size();
    }


    @Override
    public int getItemViewType(int position) {
        return separatorsSet.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    private String getYesterdayDateString() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return dateFormat.format(cal.getTime());
    }

    private void gotoHistoryDetailsActivity(String orderId) {

        Intent historyDetailsIntent = new Intent(historyActivity, HistoryDetailActivity.class);
        historyDetailsIntent.putExtra(Constant.ORDER_ID, orderId);
        historyActivity.startActivity(historyDetailsIntent);
    }

    @Override
    public boolean isPinnedViewType(int viewType) {
        return viewType == TYPE_SEPARATOR;
    }

    private class ViewHolderHistory extends RecyclerView.ViewHolder implements View
            .OnClickListener {
        LinearLayout layoutHistory;
        private CustomTextView tvOrderTime, tvOrderNo, tvProfit;
        private CustomFontTextViewTitle tvClientName, tvOrderPrice;
        private ImageView ivClientHistory, ivCanceled;
        private ImageView ivIncludeDivider;

        ViewHolderHistory(View iteamView) {
            super(iteamView);
            tvClientName = (CustomFontTextViewTitle) iteamView.findViewById(R.id.tvClientName);
            tvOrderPrice = (CustomFontTextViewTitle) iteamView.findViewById(R.id.tvOrderPrice);
            tvOrderTime = (CustomTextView) iteamView.findViewById(R.id.tvOrderTime);
            ivClientHistory = (ImageView) iteamView.findViewById(R.id.ivClientHistory);
            ivIncludeDivider = (ImageView) iteamView.findViewById(R.id.ivIncludeDivider);
            layoutHistory = (LinearLayout) iteamView.findViewById(R.id.layoutHistory);
            layoutHistory.setOnClickListener(this);
            tvOrderNo = (CustomTextView) iteamView.findViewById(R.id.tvOrderNo);
            ivCanceled = (ImageView) iteamView.findViewById(R.id.ivCanceled);
            tvProfit = (CustomTextView) iteamView.findViewById(R.id.tvProfit);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.layoutHistory) {
                int position = getAdapterPosition();
                gotoHistoryDetailsActivity(orderLists.get(position).getId());

            }
        }
    }

    private class ViewHolderSeparator extends RecyclerView.ViewHolder {
        private CustomTextView tvSection;

        public ViewHolderSeparator(View iteamView) {
            super(iteamView);
            tvSection = (CustomTextView) iteamView.findViewById(R.id.tvSection);
        }
    }

}
