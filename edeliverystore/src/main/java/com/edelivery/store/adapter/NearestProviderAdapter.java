package com.edelivery.store.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.edelivery.store.models.datamodel.ProviderDetail;
import com.elluminati.edelivery.store.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.elluminati.edelivery.store.BuildConfig.IMAGE_URL;

/**
 * Created by Ravi Bhalodi on 03,July,2020 in Elluminati
 */
public class NearestProviderAdapter extends RecyclerView.Adapter<NearestProviderAdapter.ProviderViewHolder> implements Filterable {


    private List<ProviderDetail> providerDetails;
    private List<ProviderDetail> providerDetailsFilter;
    private int selectedPosition = -1;
    private Context context;

    public void setProviderDetails(List<ProviderDetail> providerDetails) {
        this.providerDetails = providerDetails;
        this.providerDetailsFilter = providerDetails;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProviderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ProviderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_deliveryman, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ProviderViewHolder holder, final int position) {
        final ProviderDetail providerDetail = providerDetailsFilter.get(position);
        holder.div.setVisibility(getItemCount() - 1 == position ? View.GONE : View.VISIBLE);
        holder.cbProvider.setChecked(selectedPosition == position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.cbProvider.setChecked(true);
                selectedPosition = position;
                notifyDataSetChanged();
            }
        });
        holder.tvProviderName.setText(String.format("%s %s", providerDetail.getFirstName(),
                providerDetail.getLastName()));
        holder.tvProviderNumber.setText(String.format("%s%s", providerDetail.getCountryPhoneCode(),
                providerDetail.getPhone()));
        Glide.with(context).load(IMAGE_URL + providerDetail.getImageUrl()).placeholder(R
                .drawable.placeholder).dontAnimate().fallback(R.drawable.placeholder).into
                (holder.ivProvider);


    }

    @Override
    public int getItemCount() {
        return providerDetailsFilter == null ? 0 : providerDetailsFilter.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    providerDetailsFilter = providerDetails;
                } else {
                    List<ProviderDetail> filteredList = new ArrayList<>();
                    for (ProviderDetail providerDetail : providerDetails) {
                        if (providerDetail.getFirstName().concat(providerDetail.getLastName()).toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(providerDetail);
                        }
                    }
                    providerDetailsFilter = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = providerDetailsFilter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                selectedPosition = -1;
                providerDetailsFilter = (List<ProviderDetail>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    protected class ProviderViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProvider;
        TextView tvProviderName, tvProviderNumber;
        CheckBox cbProvider;
        View div;

        public ProviderViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProvider = itemView.findViewById(R.id.ivProvider);
            tvProviderName = itemView.findViewById(R.id.tvProviderName);
            tvProviderNumber = itemView.findViewById(R.id.tvProviderNumber);
            cbProvider = itemView.findViewById(R.id.cbProvider);
            div = itemView.findViewById(R.id.div);
            cbProvider.setClickable(false);

        }
    }

    public ProviderDetail getSelectedProvider() {
        if (selectedPosition == -1) {
            return null;
        } else {
            return providerDetailsFilter.get(selectedPosition);
        }

    }
}
