package com.edelivery.store.adapter;

import android.content.Context;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edelivery.store.widgets.CustomFontTextViewTitle;
import com.elluminati.edelivery.store.R;

import java.util.ArrayList;

/**
 * Created by elluminati on 13-Oct-17.
 */

public class StoreDayAdapter extends RecyclerView.Adapter<StoreDayAdapter.StoreDayHolder> {


    private ArrayList<String> stringArrayList;
    private int selected=-1;
    private Context context;

    public StoreDayAdapter(Context context, ArrayList<String> stringArrayList) {
        this.stringArrayList = stringArrayList;
        this.context = context;
    }

    @Override
    public StoreDayHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_day, parent,
                false);
        return new StoreDayHolder(view);
    }

    @Override
    public void onBindViewHolder(StoreDayHolder holder, int position) {
        holder.tvDay.setText(stringArrayList.get(position));
        if (selected == position) {
            holder.tvDay.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color
                    .color_app_heading, null));
        } else {
            holder.tvDay.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color
                    .color_app_text, null));
        }

    }

    @Override
    public int getItemCount() {
        return stringArrayList.size();
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    protected class StoreDayHolder extends RecyclerView.ViewHolder {
        private CustomFontTextViewTitle tvDay;

        public StoreDayHolder(View itemView) {
            super(itemView);
            tvDay = (CustomFontTextViewTitle) itemView.findViewById(R.id.tvDay);
        }
    }
}
