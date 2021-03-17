package com.edelivery.store.component;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.edelivery.store.adapter.ProductSelectedDialogAdapter;
import com.edelivery.store.models.datamodel.Product;
import com.edelivery.store.utils.RecyclerOnItemListener;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;

import java.util.ArrayList;

/**
 * Created by Elluminati Mohit on 4/22/2017.
 */

public abstract class CustomSelectProductDialog extends Dialog implements RecyclerOnItemListener.OnItemClickListener {
    private ArrayList<Product> productList;
    private CustomTextView txDialogTitle;
    private RecyclerView recyclerView;
    private Context context;

    public CustomSelectProductDialog(@NonNull Context context, ArrayList<Product> productList) {
        super(context);
        this.productList = productList;
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_select_product);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;

        txDialogTitle = (CustomTextView) findViewById(R.id.txDialogTitle);
        recyclerView = (RecyclerView) findViewById(R.id.root_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnItemTouchListener(new RecyclerOnItemListener(context, this));

        ProductSelectedDialogAdapter adapter = new ProductSelectedDialogAdapter(context,productList);
        recyclerView.setAdapter(adapter);

    }


    @Override
    public void onItemClick(View view, int position) {
        onProductIteamSelected(productList.get(position));
    }

    public abstract void onProductIteamSelected(Product product);


}
