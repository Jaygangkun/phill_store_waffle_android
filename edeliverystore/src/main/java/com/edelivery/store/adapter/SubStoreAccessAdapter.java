package com.edelivery.store.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.edelivery.store.AddSubStoreActivity;
import com.edelivery.store.models.datamodel.SubStoreAccessService;
import com.elluminati.edelivery.store.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.edelivery.store.models.singleton.SubStoreAccess.PERMISSION_GRANTED;
import static com.edelivery.store.models.singleton.SubStoreAccess.PERMISSION_NOT_GRANTED;

/**
 * Created by Ravi Bhalodi on 14,July,2020 in Elluminati
 */
public class SubStoreAccessAdapter extends RecyclerView.Adapter<SubStoreAccessAdapter.SubStoreAccessHolder> {
    private List<SubStoreAccessService> subStoreAccessServices;
    private AddSubStoreActivity addSubStoreActivity;

    public SubStoreAccessAdapter(AddSubStoreActivity addSubStoreActivity,
                                 List<SubStoreAccessService> subStoreAccessServices) {
        this.subStoreAccessServices = subStoreAccessServices;
        this.addSubStoreActivity = addSubStoreActivity;
        isHavePermission();
    }

    @NonNull
    @Override
    public SubStoreAccessHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SubStoreAccessHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sub_store_access, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final SubStoreAccessHolder holder, final int position) {
        holder.cbAccess.setText(subStoreAccessServices.get(position).getName());
        holder.cbAccess.setChecked(subStoreAccessServices.get(position).getPermission() == PERMISSION_GRANTED);
        holder.cbAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subStoreAccessServices.get(position).setPermission(holder.cbAccess.isChecked() ?
                        PERMISSION_GRANTED : PERMISSION_NOT_GRANTED);
                isHavePermission();
            }
        });

    }

    @Override
    public int getItemCount() {
        return subStoreAccessServices == null ? 0 : subStoreAccessServices.size();
    }

    protected class SubStoreAccessHolder extends RecyclerView.ViewHolder {
        CheckBox cbAccess;

        public SubStoreAccessHolder(@NonNull View itemView) {
            super(itemView);
            cbAccess = itemView.findViewById(R.id.cbAccess);
        }
    }

    public List<SubStoreAccessService> getSubStoreAccessServices() {
        return subStoreAccessServices;
    }

    private void isHavePermission() {
        boolean isHavePermission = false;
        for (SubStoreAccessService subStoreAccessService : subStoreAccessServices) {
            if (subStoreAccessService.getPermission() == PERMISSION_GRANTED) {
                isHavePermission = true;
                break;
            }
        }
        addSubStoreActivity.enableSaveButton(isHavePermission);
    }
}
