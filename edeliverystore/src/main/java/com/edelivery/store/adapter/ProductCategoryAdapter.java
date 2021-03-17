package com.edelivery.store.adapter;

import android.content.Context;
import androidx.annotation.Nullable;
import android.widget.ArrayAdapter;

import com.edelivery.store.models.datamodel.Product;

import java.util.ArrayList;

/**
 * Adapter to display list of product category at time of add item on 23-02-2017.
 */

public class ProductCategoryAdapter extends ArrayAdapter<String> {

    private ArrayList<String> productNameList = new ArrayList<>(), filteredList = new ArrayList<>();

    public ProductCategoryAdapter(Context context, int resource, ArrayList<Product> productList) {
        super(context, resource);

        for (Product product : productList) {
            productNameList.add(product.getName());
        }
        filteredList.addAll(productNameList);
    }

    @Nullable
    @Override
    public String getItem(int position) {
        if (filteredList.size() > position) {
            return filteredList.get(position);
        }
        return "";
    }

    @Override
    public int getCount() {
        int count = filteredList.size();
        return count > 0 ? count -1 : count;
     //  return filteredList.size();
    }

   /* @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    filteredList = getFilteredList(constraint.toString());
                    filterResults.count = filteredList.size();
                    filterResults.values = filteredList;
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
    }

    private ArrayList<String> getFilteredList(String input) {
        filteredList.clear();
        for (int i = 0; i < productNameList.size(); i++) {
            if (productNameList.get(i).toLowerCase().contains(input)) {
                filteredList.add(productNameList.get(i));
            }
        }

        return filteredList;
    }*/
}
