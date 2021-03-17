package com.edelivery.store.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edelivery.store.AddProductActivity;
import com.edelivery.store.ProductListActivity;
import com.edelivery.store.models.datamodel.Product;
import com.edelivery.store.models.responsemodel.IsSuccessResponse;
import com.edelivery.store.parse.ParseContent;
import com.edelivery.store.utils.Utilities;
import com.elluminati.edelivery.store.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Adapter to display list of product on 09-02-2017.
 */

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder>
        implements CompoundButton.OnCheckedChangeListener {

    private Context context;
    private ArrayList<Product> productList;

    public ProductListAdapter(Context context, ArrayList<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_product_list, parent,
                false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Product product = productList.get(position);
        holder.tvProductName.setText(product.getName());
        holder.tvProductDescription.setText(product.getDetails());
        if (product.getSpecificationsDetails().size() == 0) {
            holder.tvSpecification.setText(context.getResources().getString(R.string
                    .text_no_specification));
        } else {
            holder.tvSpecification.setText(String.valueOf(context.getResources().getString(R
                    .string.text_specification)));
        }
        holder.switchProduct.setOnCheckedChangeListener(null);
        if (product.isIsVisibleInStore()) {
            holder.switchProduct.setChecked(true);
            holder.tvVisibility.setText(context.getResources().getString(R.string.text_visible));
        } else {
            holder.switchProduct.setChecked(false);
            holder.tvVisibility.setText(context.getResources().getString(R.string.text_invisible));
        }
        holder.switchProduct.setOnCheckedChangeListener(this);
        holder.switchProduct.setTag(position);
        holder.llProductItem.setTag(position);

        holder.llProductItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddProductActivity.class);
                ((ProductListActivity) context).gotoProductDetail(context, intent, holder
                        .getAdapterPosition(), holder.ivProduct, true);
            }
        });

        holder.llSpeciNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  Intent intent = new Intent(context, SpecificationGroupActivity.class);
                ((ProductListActivity) context).gotoProductDetail(context, intent, holder
                        .getAdapterPosition(), holder.ivProduct, true);*/
            }
        });

        if (productList.size() - 1 == position) {
            holder
                    .llBlank.setVisibility(View.VISIBLE);
        } else {
            holder
                    .llBlank.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (buttonView instanceof SwitchCompat) {
            SwitchCompat switchCompat = (SwitchCompat) buttonView;
            try {
                if (switchCompat.getTag() != null) {
                    updateProduct((int) switchCompat.getTag(), switchCompat);
                }
            } catch (NullPointerException e) {
                Utilities.printLog("Exception", e.getMessage());
            }
        }
    }

    private void updateProduct(final int position, final SwitchCompat switchCompat) {
        final Product product = productList.get(position);
        Utilities.showProgressDialog(context);

        Call<IsSuccessResponse> call = ((ProductListActivity) context).getUpdateProductCall
                (product, switchCompat.isChecked());
        call.enqueue(new Callback<IsSuccessResponse>() {
            @Override
            public void onResponse(Call<IsSuccessResponse> call, Response<IsSuccessResponse>
                    response) {

                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        product.setIsVisibleInStore(switchCompat.isChecked());
                        notifyDataSetChanged();
                    } else {
                        switchCompat.toggle();
                        ParseContent.getParseContentInstance().showErrorMessage(context, response
                                .body().getErrorCode(), false);
                    }
                } else {
                    Utilities.showHttpErrorToast(response.code(), context);
                }
                Utilities.removeProgressDialog();
            }

            @Override
            public void onFailure(Call<IsSuccessResponse> call, Throwable t) {
                Utilities.printLog("ProductListAdapter", t.getMessage());
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivProduct;
        private TextView tvProductName, tvProductDescription, tvSpecification, tvVisibility;
        private SwitchCompat switchProduct;
        private LinearLayout llProductItem, llSpeciNum;
        private View llBlank;

        ViewHolder(View itemView) {
            super(itemView);

            ivProduct = (ImageView) itemView.findViewById(R.id.ivProduct);
            tvProductName = (TextView) itemView.findViewById(R.id.tvProductName);
            tvSpecification = (TextView) itemView.findViewById(R.id.tvSpecificationNum);
            tvProductDescription = (TextView) itemView.findViewById(R.id.tvProductDescription);
            tvVisibility = (TextView) itemView.findViewById(R.id.tvVisibility);
            switchProduct = (SwitchCompat) itemView.findViewById(R.id.switchProduct);
            llProductItem = (LinearLayout) itemView.findViewById(R.id.llProductItem);
            switchProduct.setOnCheckedChangeListener(ProductListAdapter.this);
            llSpeciNum = (LinearLayout) itemView.findViewById(R.id.llSpeciNum);
            llBlank = itemView.findViewById(R.id.llBlank);
        }
    }
}
