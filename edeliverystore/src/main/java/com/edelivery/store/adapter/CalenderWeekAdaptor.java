package com.edelivery.store.adapter;

import android.content.Context;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.edelivery.store.models.datamodel.WeekData;
import com.edelivery.store.parse.ParseContent;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by elluminati on 28-Jun-17.
 */

public class CalenderWeekAdaptor extends RecyclerView.Adapter<CalenderWeekAdaptor
        .WeekDayView> {

    public SparseBooleanArray selectedItems;
    private ArrayList<WeekData> weekDatas;
    private Context context;
    private ParseContent parseContent;
    private WeekData date = null;
    private Calendar calendar;


    public CalenderWeekAdaptor(Context context, ArrayList<WeekData> weekDatas) {
        this.weekDatas = weekDatas;
        this.context = context;
        parseContent = ParseContent.getParseContentInstance();
        selectedItems = new SparseBooleanArray();
        calendar = Calendar.getInstance();
    }

    @Override
    public WeekDayView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_week_date,
                parent, false);
        return new WeekDayView(view);
    }

    @Override
    public void onBindViewHolder(WeekDayView holder, int position) {
        ArrayList<Date> dates = weekDatas.get(position).getParticularDate();
        String date1 = parseContent.dateFormat3.format(dates.get(0));
        String date2 = parseContent.dateFormat3.format(dates.get(1));
        holder.tvWeekStart.setText(date1);
        holder.tvWeekEnd.setText(date2);
        if (selectedItems.get(position, false)) {
            ((LinearLayout) holder.itemView).setBackgroundColor(ResourcesCompat.getColor(context
                    .getResources(), R.color.color_app_divider, null));
        } else {
            ((LinearLayout) holder.itemView).setBackgroundColor(ResourcesCompat.getColor(context
                    .getResources(), android.R.color.transparent, null));
        }


    }

    @Override
    public int getItemCount() {
        return weekDatas.size();
    }

    public void toggleSelection(int pos) {
        selectedItems.clear();
        selectedItems.put(pos, true);
        date = weekDatas.get(pos);
        notifyDataSetChanged();
    }

    public WeekData getDate() {
        return date;
    }

    protected class WeekDayView extends RecyclerView.ViewHolder {

        CustomTextView tvWeekStart, tvWeekEnd;

        public WeekDayView(View itemView) {
            super(itemView);
            tvWeekStart = (CustomTextView) itemView.findViewById(R.id.tvWeekStart);
            tvWeekEnd = (CustomTextView) itemView.findViewById(R.id.tvWeekEnd);

        }
    }

}
