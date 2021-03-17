package com.edelivery.store.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.edelivery.store.models.datamodel.VehicleDetail;
import com.elluminati.edelivery.store.R;

import java.util.List;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.VehicleViewHolder> {

    private List<VehicleDetail> vehicleDetails;
    private Context context;

    public VehicleAdapter(Context context,List<VehicleDetail> vehicleDetails) {
        this.vehicleDetails = vehicleDetails;
        this.context = context;
    }

    @NonNull
    @Override
    public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VehicleViewHolder(LayoutInflater.from(context).inflate(R.layout.item_vehicle,
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleViewHolder holder, int position) {
        holder.cbVehicle.setText(vehicleDetails.get(position).getVehicleName());
        holder.cbVehicle.setChecked(vehicleDetails.get(position).isSelected());

    }

    @Override
    public int getItemCount() {
        return vehicleDetails.size();
    }

    protected class VehicleViewHolder extends RecyclerView.ViewHolder {
        CheckBox cbVehicle;

        public VehicleViewHolder(View itemView) {
            super(itemView);
            cbVehicle = itemView.findViewById(R.id.cbVehicle);
            cbVehicle.setClickable(false);
        }
    }
}
