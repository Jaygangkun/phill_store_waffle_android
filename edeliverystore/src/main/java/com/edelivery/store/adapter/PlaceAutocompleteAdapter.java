/*
 * Copyright (C) 2015 Google Inc. All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.edelivery.store.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.edelivery.store.models.datamodel.AutocompleteAddress1;
import com.edelivery.store.utils.Utilities;
import com.elluminati.edelivery.store.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;


public class PlaceAutocompleteAdapter
        extends BaseAdapter implements Filterable {

    private static final String TAG = "PlaceAutocompleteAdapter";
    private CharacterStyle styleBold = new StyleSpan(Typeface.BOLD);
    private Context context;
    private ViewHolder holder;
    private LayoutInflater inflater;
    PlacesClient placesClient;
    private RectangularBounds bounds;
    private String countryCode;
    private AutocompleteSessionToken token;
    private List<Place.Field> placeFields;
    private List<AutocompleteAddress1> searchAddresses;
    /**
     * Current results returned by this adapter.
     */
    private ArrayList<AutocompletePrediction> mResultList;


    /**
     * Initializes with a resource for text rows and autocomplete query bounds.
     *
     * @see ArrayAdapter#ArrayAdapter(Context, int)
     */
    public PlaceAutocompleteAdapter(Context context) {
        /*Places.initialize(context,
                context.getResources().getString(R.string.GOOGLE_ANDROID_API_KEY));*/
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        placesClient = Places.createClient(context);
//        token = AutocompleteSessionToken.newInstance();
        mResultList = new ArrayList<>();
        searchAddresses = new ArrayList<>();
//        placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
    }

    public void setSearchAddresses(List<AutocompleteAddress1> searchAddresses){
        this.searchAddresses.clear();
        if(!searchAddresses.isEmpty())
            this.searchAddresses.addAll(searchAddresses);
        notifyDataSetChanged();
    }

    public List<AutocompleteAddress1> getSearchAddresses() {
        return searchAddresses;
    }

    public void setPlaceFilter(String countryCode) {
        this.countryCode = countryCode;
    }

    /**
     * Sets the bounds for all subsequent queries.
     */
    public void setBounds(RectangularBounds bounds) {
        this.bounds = bounds;
    }

    /**
     * Returns the number of results received in the last autocomplete query.
     */
    @Override
    public int getCount() {
        return searchAddresses.size();
    }

    /**
     * Returns an item from the last autocomplete query.
     */
    @Override
    public AutocompleteAddress1 getItem(int position) {
        return searchAddresses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        // Sets the primary and secondary text for a row.
        // Note that getPrimaryText() and getSecondaryText() return a CharSequence that may contain
        // styling based on the given CharacterStyle.
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_autocomplete_list, parent, false);
            holder = new ViewHolder();
            holder.tvPlaceName = convertView.findViewById(R.id.tvPlaceName);
            holder.tvPlaceAddress = convertView.findViewById(R.id.tvPlaceAddress);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

      /*  AutocompletePrediction item = getItem(position);
        holder.tvPlaceName.setText(item.getPrimaryText(styleBold));
        holder.tvPlaceAddress.setText(item.getSecondaryText(styleBold));*/

        AutocompleteAddress1 item = getItem(position);
        holder.tvPlaceName.setText(item.getAddress());
        holder.tvPlaceAddress.setText("");

        return convertView;
    }

    /**
     * Returns the filter for the current set of autocomplete results.
     */
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                // Skip the autocomplete query if no constraints are given.
               /* if (constraint != null) {
                    // Query the autocomplete API for the (constraint) search string.
                    getFindAutocompletePredictionsRequest(constraint);
                    if (mResultList != null) {
                        // The API successfully returned results.
                        results.values = mResultList;
                        results.count = mResultList.size();
                    }
                }*/
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    // The API returned at least one result, update the data.
//                    notifyDataSetChanged();
                } else {
                    // The API did not return any results, invalidate the data set.
//                    notifyDataSetInvalidated();
                }
            }

            @Override
            public CharSequence convertResultToString(Object resultValue) {
                // Override this method to display a readable result in the AutocompleteTextView
                // when clicked.
                if (resultValue instanceof AutocompletePrediction) {
                    return ((AutocompletePrediction) resultValue).getFullText(null);
                } else {
                    return super.convertResultToString(resultValue);
                }
            }
        };
    }


    public String getPlaceId(int position) {
        if (mResultList != null && !mResultList.isEmpty()) {
            return mResultList.get(position).getPlaceId();
        } else {
            return "";
        }

    }

    private class ViewHolder {
        TextView tvPlaceName, tvPlaceAddress;

    }

    private void getFindAutocompletePredictionsRequest(CharSequence constraint) {

        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                // Call either setLocationBias() OR setLocationRestriction().
                .setLocationBias(bounds)
                //.setLocationRestriction(bounds)
                .setCountry(countryCode)
                //.setTypeFilter(TypeFilter.GEOCODE)
                .setSessionToken(token)
                .setQuery(constraint.toString())
                .build();

        placesClient.findAutocompletePredictions(request).addOnSuccessListener(new OnSuccessListener<FindAutocompletePredictionsResponse>() {
            @Override
            public void onSuccess(FindAutocompletePredictionsResponse response) {
                mResultList.clear();
                mResultList.addAll(response.getAutocompletePredictions());
                notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Utilities.handleException(TAG, e);
            }
        });
    }

    public void getFetchPlaceRequest(String placeId,
                                     OnSuccessListener<FetchPlaceResponse> responseOnSuccessListener) {
        FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields).setSessionToken(token).build();
        placesClient.fetchPlace(request).addOnSuccessListener(responseOnSuccessListener);
    }
}
