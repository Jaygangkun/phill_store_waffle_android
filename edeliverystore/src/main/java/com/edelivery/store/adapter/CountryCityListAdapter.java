package com.edelivery.store.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.edelivery.store.models.datamodel.Category;
import com.edelivery.store.models.datamodel.City;
import com.edelivery.store.models.datamodel.Country;
import com.edelivery.store.utils.Constant;
import com.edelivery.store.utils.GlideApp;
import com.elluminati.edelivery.store.R;

import java.util.ArrayList;

import static com.elluminati.edelivery.store.BuildConfig.IMAGE_URL;

/**
 * Adapter to display list of country, city and store category on 01-02-2017.
 */

public class CountryCityListAdapter extends RecyclerView.Adapter<CountryCityListAdapter.ViewHolder> {

    private ArrayList<Country> countryList;
    private ArrayList<City> cityList;
    private ArrayList<Category> deliveryList;
    private Context context;
    private boolean isCountryList;
    private int deliveryCode;

    public CountryCityListAdapter(Context context, ArrayList<Country> countryList,
                                  boolean isCountryList) {
        this.countryList = countryList;
        this.context = context;
        this.isCountryList = isCountryList;
    }

    public CountryCityListAdapter(Context context, ArrayList<City> cityList) {
        this.cityList = cityList;
        this.context = context;
    }

    public CountryCityListAdapter(Context context, ArrayList<Category> deliveryList,
                                  int deliveryCode) {
        this.context = context;
        this.deliveryList = deliveryList;
        this.deliveryCode = deliveryCode;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_general, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (deliveryCode != 0) {
            Category category = deliveryList.get(position);
            holder.tvName.setText(category.getDeliveryName());
            GlideApp.with(context).load(IMAGE_URL + category.getImageUrl()).into(holder.ivType);
            holder.tvCode.setVisibility(View.GONE);
            if (category.getDeliveryType() == Constant.DeliveryType.COURIER) {
                holder.itemView.setVisibility(View.GONE);
            } else {
                holder.itemView.setVisibility(View.VISIBLE);
            }

        } else {

            if (isCountryList) {
                Country country = countryList.get(position);
                holder.tvName.setText(country.getCountryName());
                holder.tvCode.setText(country.getCountryPhoneCode());
                holder.tvCode.setVisibility(View.VISIBLE);
                holder.ivType.setVisibility(View.GONE);
            } else {
                City city = cityList.get(position);
                holder.tvName.setText(city.getCityName());
                holder.tvCode.setVisibility(View.GONE);
                holder.ivType.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (deliveryCode > 0) {
            return deliveryList.size();
        } else {
            if (isCountryList) {
                return countryList.size();
            } else {
                return cityList.size();
            }
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvCode, tvName;
        ImageView ivType;

        ViewHolder(View itemView) {
            super(itemView);

            tvCode = (TextView) itemView.findViewById(R.id.tvCode);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            ivType = (ImageView) itemView.findViewById(R.id.ivType);
        }
    }
}
