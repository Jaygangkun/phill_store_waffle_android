package com.edelivery.store.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;

/**
 * Created by elluminati on 20-Jun-17.
 */

public class LanguageAdaptor extends RecyclerView.Adapter<LanguageAdaptor
        .LanguageViewHolder> {
    private TypedArray langCode;
    private TypedArray langName;
    private Context context;

    public LanguageAdaptor(Context context) {
        this.context = context;
        langCode = context.getResources().obtainTypedArray(R.array.language_code);
        langName = context.getResources().obtainTypedArray(R.array.language_name);
    }

    @Override
    public LanguageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_language_name,
                parent, false);
        return new LanguageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LanguageViewHolder holder, int position) {
        holder.tvCityName.setText(langName.getString(position));
    }

    @Override
    public int getItemCount() {
        return langName.length();
    }

    protected class LanguageViewHolder extends RecyclerView.ViewHolder {
        CustomTextView tvCityName;

        public LanguageViewHolder(View itemView) {
            super(itemView);

            tvCityName = (CustomTextView) itemView.findViewById(R.id.tvItemCityName);

        }


    }


}
