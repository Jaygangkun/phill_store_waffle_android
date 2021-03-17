package com.edelivery.store.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edelivery.store.Section.OrderDetailsSection;
import com.edelivery.store.utils.PinnedHeaderItemDecoration;
import com.elluminati.edelivery.store.R;

/**
 * Created by elluminati on 29-Dec-17.
 */

public class CartHistoryFragment extends BaseHistoryFragment {

    private RecyclerView rcvOrderProductItem;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View invoiceFrag = inflater.inflate(R.layout.fragment_cart_history, container, false);
        rcvOrderProductItem = (RecyclerView) invoiceFrag.findViewById(R.id.rcvOrderProductItem);
        return invoiceFrag;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rcvOrderProductItem.setLayoutManager(new LinearLayoutManager(activity));

        OrderDetailsSection orderDetailsSection = new OrderDetailsSection(activity.detailsResponse
                .getOrder().getCartDetail().getOrderDetails(), activity, false);
        rcvOrderProductItem.setAdapter(orderDetailsSection);
        rcvOrderProductItem.addItemDecoration(new PinnedHeaderItemDecoration());
    }
}
