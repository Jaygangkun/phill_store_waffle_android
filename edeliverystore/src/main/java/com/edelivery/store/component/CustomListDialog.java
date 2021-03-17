package com.edelivery.store.component;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.edelivery.store.models.datamodel.Category;
import com.edelivery.store.models.datamodel.City;
import com.edelivery.store.models.datamodel.Country;
import com.edelivery.store.utils.RecyclerOnItemListener;
import com.elluminati.edelivery.store.R;
import com.edelivery.store.adapter.CountryCityListAdapter;

import java.util.ArrayList;

/**
 * A dialog class to display list of country, city and store category on 04-02-2017.
 */

public abstract class CustomListDialog extends Dialog implements RecyclerOnItemListener.OnItemClickListener {

    private ArrayList<Country> countryItemsList;
    private ArrayList<City> cityItemsList;
    private ArrayList<Category> deliveryList;
    private boolean isCountryList;
    private Context context;
    private int code;

    public CustomListDialog(Context context, ArrayList<Country> countryItemsList, boolean isCountryList) {
        super(context);
        this.countryItemsList = countryItemsList;
        this.isCountryList = isCountryList;
        this.context = context;
    }

    public CustomListDialog(Context context, ArrayList<City> cityItemsList) {
        super(context);
        this.cityItemsList = cityItemsList;
        this.context = context;
    }

    public CustomListDialog(Context context, ArrayList<Category> deliveryList, int code) {
        super(context);
        this.deliveryList = deliveryList;
        this.code = code;
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RecyclerView recyclerView;
        TextView tvDialogTitle;
        CountryCityListAdapter countryCityListAdapter;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_general);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;

        tvDialogTitle = (TextView) findViewById(R.id.tvDialogTitle);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addOnItemTouchListener(new RecyclerOnItemListener(context, this));

        if (code > 0) {
            countryCityListAdapter = new CountryCityListAdapter(context, deliveryList, code);
            tvDialogTitle.setText(context.getResources().getString(R.string.text_select_category));
        } else {
            if (isCountryList) {
                countryCityListAdapter = new CountryCityListAdapter(context, countryItemsList, true);
                tvDialogTitle.setText(context.getResources().getString(R.string.text_select_country));
            } else {
                countryCityListAdapter = new CountryCityListAdapter(context, cityItemsList);
                tvDialogTitle.setText(context.getResources().getString(R.string.text_select_city));
            }
        }

        recyclerView.setAdapter(countryCityListAdapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        if (code > 0) {
            onItemClickOnList(deliveryList.get(position));
        } else {
            if (isCountryList) {
                onItemClickOnList(countryItemsList.get(position));
            } else {
                onItemClickOnList(cityItemsList.get(position));
            }
        }
    }

    public abstract void onItemClickOnList(Object object);
}
