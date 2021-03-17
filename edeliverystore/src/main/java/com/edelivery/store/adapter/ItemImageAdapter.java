package com.edelivery.store.adapter;

import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.edelivery.store.AddItemActivity;
import com.edelivery.store.utils.GlideApp;
import com.elluminati.edelivery.store.R;

import java.util.ArrayList;

import static com.elluminati.edelivery.store.BuildConfig.IMAGE_URL;

/**
 * Created by elluminati on 10-Jun-17.
 */

public class ItemImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public SparseBooleanArray selectedItems;
    private AddItemActivity addItemActivity;
    private ArrayList<String> itemImageListForAdapter;
    private ArrayList<String> deleteItemImage;
    private boolean isEnable;
    private ArrayList<String> itemImageList;

    public ItemImageAdapter(AddItemActivity addItemActivity, ArrayList<String>
            itemImageListForAdapter, ArrayList<String>
                                    deleteItemImage, ArrayList<String> itemImageList) {
        this.addItemActivity = addItemActivity;
        this.itemImageListForAdapter = itemImageListForAdapter;
        this.deleteItemImage = deleteItemImage;
        this.itemImageList = itemImageList;
        selectedItems = new SparseBooleanArray();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemImageHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout
                .item_image, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ItemImageHolder imageHolder = (ItemImageHolder) holder;
        if (!itemImageListForAdapter.isEmpty()) {
            String imageUrl;
            if (isUrl(itemImageListForAdapter.get(position))) {
                imageUrl = IMAGE_URL + itemImageListForAdapter.get(position);

            } else {
                imageUrl = itemImageListForAdapter.get(position);
            }
            GlideApp.with(addItemActivity).load(imageUrl)
                    .dontAnimate
                            ().listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                            Target<Drawable> target, boolean isFirstResource) {
                    imageHolder.ivProduct.setVisibility(View.GONE);
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model,
                                               Target<Drawable> target, DataSource dataSource,
                                               boolean isFirstResource) {
                    imageHolder.ivProduct.setVisibility(View.VISIBLE);
                    return false;
                }
            }).into(
                    imageHolder.ivProduct);
        }
        if (isEnable) {
            imageHolder.itemImageDelete.setVisibility(View.VISIBLE);
        } else {
            imageHolder.itemImageDelete.setVisibility(View.GONE);
        }
        if (isEnable && (itemImageListForAdapter.size() - 1 == position ||
                itemImageListForAdapter.size() == 0)) {
            if (itemImageListForAdapter.size() == 0) {
                imageHolder
                        .ivProduct.setVisibility(View.GONE);
                imageHolder.itemImageDelete.setVisibility(View.GONE);
            } else {
                imageHolder
                        .ivProduct.setVisibility(View.VISIBLE);
                imageHolder.itemImageDelete.setVisibility(View.VISIBLE);
            }
            imageHolder.ivAddImage.setVisibility(View.VISIBLE);
        } else {
            imageHolder.ivAddImage.setVisibility(View.GONE);
        }


    }


    @Override
    public int getItemCount() {
        if (itemImageListForAdapter.isEmpty()) {
            return 1;
        } else {
            return itemImageListForAdapter.size();
        }

    }

    private boolean isUrl(String s) {
        String[] split = s.split("/");
        return TextUtils.equals("store_items", split[0]);

    }

    public void setIsEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }

    protected class ItemImageHolder extends RecyclerView.ViewHolder implements View
            .OnClickListener {

        ImageView ivProduct, itemImageDelete;
        ImageButton ivAddImage;

        public ItemImageHolder(View itemView) {
            super(itemView);
            ivProduct = (ImageView) itemView.findViewById(R.id.ivProduct);
            itemImageDelete = (ImageView) itemView.findViewById(R.id.itemImageDelete);
            ivAddImage = (ImageButton) itemView.findViewById(R.id.ivAddImage);
            itemImageDelete.setOnClickListener(this);
            ivAddImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.itemImageDelete:
                    if (isEnable) {
                        for (int i = 0; i < itemImageList.size(); i++) {
                            if (itemImageList.get(i).equals(itemImageListForAdapter.get
                                    (getAdapterPosition()))) {
                                itemImageList.remove(i);
                                itemImageListForAdapter.remove(getAdapterPosition());
                                notifyDataSetChanged();
                                return;
                            }
                        }
                        deleteItemImage.add(itemImageListForAdapter.get(getAdapterPosition()));
                        itemImageListForAdapter.remove(getAdapterPosition());
                        notifyDataSetChanged();
                    }

                    break;
                case R.id.ivAddImage:
                    addItemActivity.addItemImage();
                    break;
                default:
                    // do with default
                    break;
            }

        }
    }


}
