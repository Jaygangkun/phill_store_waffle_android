package com.edelivery.store.adapter;

import android.content.Context;
import androidx.core.content.res.ResourcesCompat;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.edelivery.store.utils.GlideApp;
import com.elluminati.edelivery.store.R;

import java.util.List;

import static com.elluminati.edelivery.store.BuildConfig.IMAGE_URL;

/**
 * Created by elluminati on 18-Apr-17.
 */

public abstract class ProductItemItemAdapter extends PagerAdapter {

    private List<String> stringList;
    private Context context;
    private int id;
    private boolean isItemClick;

    public ProductItemItemAdapter(Context context, List<String>
            stringList, int layoutId, boolean isItemClick) {
        this.stringList = stringList;
        this.context = context;
        this.id = layoutId;
        this.isItemClick = isItemClick;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(id, container, false);
        GlideApp.with(context).load(IMAGE_URL + stringList.get(position))
                .dontAnimate()
                .placeholder(ResourcesCompat.getDrawable
                        (context
                                .getResources(), R.drawable.placeholder, null)).fallback
                (ResourcesCompat
                        .getDrawable
                                (context.getResources(), R.drawable.placeholder,
                                        null)).into
                ((ImageView) view);
        if (isItemClick) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClick(position);
                }
            });
        }

        container.addView(view);
        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }

    @Override
    public int getCount() {
        return stringList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public abstract void onItemClick(int position);

}
