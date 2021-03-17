package com.edelivery.store.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edelivery.store.component.CustomFontCheckBox;
import com.edelivery.store.models.singleton.Language;
import com.edelivery.store.utils.Utilities;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by elluminati on 08-Dec-17.
 */

public class FamousTagAdapter extends RecyclerView.Adapter<FamousTagAdapter.FamousTagView> {

    private ArrayList<List<String>> deliveryTagList;
    private ArrayList<List<String>> storeTagList;

    public FamousTagAdapter(ArrayList<List<String>> deliveryTagList, ArrayList<List<String>> storeTagList) {
        this.deliveryTagList = deliveryTagList;
        this.storeTagList = storeTagList;
    }

    @Override
    public FamousTagView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_famous_tag,
                parent, false);
        return new FamousTagView(view);
    }

    @Override
    public void onBindViewHolder(FamousTagView holder, final int position) {
        holder.tvFamousTag.setText(Utilities.getDetailStringFromList(deliveryTagList.get(position),
                Language.getInstance().getAdminLanguageIndex()));
        holder.cbTag.setChecked(storeTagList.contains(deliveryTagList.get(position)));
        holder.cbTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (storeTagList.contains(deliveryTagList.get(position))) {
                    storeTagList.remove(deliveryTagList.get(position));
                } else {
                    storeTagList.add(deliveryTagList.get(position));
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return deliveryTagList.size();
    }

    public ArrayList<List<String>> getStoreTagList() {
        return storeTagList;
    }

    protected class FamousTagView extends RecyclerView.ViewHolder {
        CustomTextView tvFamousTag;
        CustomFontCheckBox cbTag;

        public FamousTagView(View itemView) {
            super(itemView);
            tvFamousTag = (CustomTextView) itemView.findViewById(R.id.tvFamousTag);
            cbTag = (CustomFontCheckBox) itemView.findViewById(R.id.cbTag);
        }
    }
}
