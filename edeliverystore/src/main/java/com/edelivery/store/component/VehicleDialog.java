package com.edelivery.store.component;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.edelivery.store.adapter.VehicleAdapter;
import com.edelivery.store.models.datamodel.VehicleDetail;
import com.edelivery.store.models.singleton.SubStoreAccess;
import com.edelivery.store.utils.ClickListener;
import com.edelivery.store.utils.RecyclerTouchListener;
import com.elluminati.edelivery.store.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class VehicleDialog extends Dialog {
    private RecyclerView rcvVehicle;
    private String vehicleId;
    private VehicleAdapter vehicleAdapter;
    private TextView tvDriverAssign;
    private RadioGroup radioGroup;

    public VehicleDialog(@NonNull Context context, final List<VehicleDetail> vehicleDetailList) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_vehicle_selecte);
        for (VehicleDetail vehicleDetail : vehicleDetailList) {
            vehicleDetail.setSelected(false);
        }
        rcvVehicle = findViewById(R.id.rcvVehicle);
        vehicleAdapter = new VehicleAdapter(context, vehicleDetailList);
        rcvVehicle.setLayoutManager(new LinearLayoutManager(context));
        rcvVehicle.setAdapter(vehicleAdapter);
        rcvVehicle.addOnItemTouchListener(new RecyclerTouchListener(context, rcvVehicle, new
                ClickListener() {


                    @Override
                    public void onClick(View view, int position) {
                        for (VehicleDetail vehicleDetail : vehicleDetailList) {
                            vehicleDetail.setSelected(false);
                        }
                        vehicleId = vehicleDetailList.get(position).getId();
                        vehicleDetailList.get(position).setSelected(true);
                        vehicleAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));
        rcvVehicle.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration
                .VERTICAL));
        radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setEnabled(SubStoreAccess.getInstance().isAccess(SubStoreAccess.DELIVERIES));
        tvDriverAssign = findViewById(R.id.tvDriverAssign);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(params);

    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void hideProviderManualAssign() {
        tvDriverAssign.setVisibility(View.GONE);
        radioGroup.setVisibility(View.GONE);
    }

    public boolean isManualAssign() {
        return radioGroup.getCheckedRadioButtonId() == R.id.rbManualAssign;
    }
}
