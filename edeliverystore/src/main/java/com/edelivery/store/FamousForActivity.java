package com.edelivery.store;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.edelivery.store.adapter.FamousTagAdapter;
import com.edelivery.store.models.datamodel.StoreData;
import com.edelivery.store.utils.Constant;
import com.edelivery.store.widgets.CustomButton;
import com.elluminati.edelivery.store.R;

import java.util.ArrayList;
import java.util.List;

public class FamousForActivity extends BaseActivity {

    private RecyclerView rcvFamousTag;
    private CustomButton btnDone;
    private FamousTagAdapter famousTagAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_famous_for);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbar(toolbar, R.drawable.ic_back, R.color.color_app_theme);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ((TextView) findViewById(R.id.tvToolbarTitle)).setText(getString(R.string.text_famous_for));
        btnDone = (CustomButton) findViewById(R.id.btnDone);
        btnDone.setOnClickListener(this);
        initRcvTag();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        setToolbarEditIcon(false, 0);
        setToolbarCameraIcon(false);
        return true;
    }

    private void initRcvTag() {
        StoreData storeData = getIntent().getExtras().getParcelable(Constant.BUNDLE);
        ArrayList<List<String>> deliveryTag = new ArrayList<>();
        ArrayList<List<String>> storeTag = new ArrayList<>();
        if (storeData.getDeliveryDetails().getFamousProductsTags() != null) {
            deliveryTag.addAll(storeData.getDeliveryDetails().getFamousProductsTags());
        }
        if (storeData.getFamousProductsTags() != null) {
            storeTag.addAll(storeData.getFamousProductsTags());
        }


        rcvFamousTag = (RecyclerView) findViewById(R.id.rcvFamousTag);
        rcvFamousTag.setLayoutManager(new LinearLayoutManager(this));
        famousTagAdapter = new FamousTagAdapter(deliveryTag, storeTag);
        rcvFamousTag.setAdapter(famousTagAdapter);
        rcvFamousTag.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration
                .VERTICAL));


    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btnDone:
                setResult(Activity.RESULT_OK, new Intent().putExtra(Constant.BUNDLE,
                        famousTagAdapter.getStoreTagList()));
                finish();
                break;

            default:
                // do with default
                break;
        }
    }

}
