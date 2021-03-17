package com.edelivery.store.component;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.edelivery.store.adapter.ProductsAdapter;
import com.edelivery.store.models.datamodel.Product;
import com.elluminati.edelivery.store.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class SelectProductsDialog extends Dialog {

    private Context context;
    private List<Product> productList;
    private List<Product> selectedProductList;
    RecyclerView recyclerView;


    public SelectProductsDialog(@NonNull Context context
            , List<Product> productList) {
        super(context);
        this.context = context;
        this.productList = productList;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_select_product_list);

        recyclerView = (RecyclerView) findViewById(R.id.rcvProducts);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        ProductsAdapter productsAdapter = new ProductsAdapter(productList);
        recyclerView.setAdapter(productsAdapter);

        selectedProductList = new ArrayList<>();
        findViewById(R.id.btnNegative).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        findViewById(R.id.btnPositive).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Product product: productList){
                    if(product.isSelected()){
                        selectedProductList.add(product);
                    }
                }
                onSelect(selectedProductList);
                dismiss();
            }
        });

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;

       /* tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                storeIds.clear();
                storeNames.clear();
                String stores = "";
                for(StoresItem storesItem : storeList){
                    if(storesItem.isSelected()){
                        storeIds.add(storesItem.getId());
                        storeNames.add(storesItem.getName());
                        if (TextUtils.isEmpty(stores)){
                            stores = stores.concat(storesItem.getName());
                        }else {
                            stores = stores.concat(", ").concat(storesItem.getName());
                        }
                    }
                }

                etSelectStore.setText(stores);
                Utils.hideSoftKeyboard(loginActivity);
                etSelectStore.setError(null);
                etRegisterMobileNumber.requestFocus();
                customStoreDialog.dismiss();
            }
        });*/
    }

    public abstract void onSelect(List<Product> productList);
}
