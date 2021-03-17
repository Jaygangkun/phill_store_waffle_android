package com.edelivery.store;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.edelivery.store.adapter.AddItemSpeciAdapter;
import com.edelivery.store.component.AddDetailInMultiLanguageDialog;
import com.edelivery.store.component.CustomAlterDialog;
import com.edelivery.store.models.datamodel.ItemSpecification;
import com.edelivery.store.models.datamodel.ProductSpecification;
import com.edelivery.store.parse.ApiClient;
import com.edelivery.store.utils.Constant;
import com.edelivery.store.utils.Utilities;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.edelivery.store.utils.Constant.PRODUCT_SPECIFICATION;

public class AddItemSpeciActivity extends BaseActivity {

    private boolean isTypeSingle;
    private TextInputEditText etSpeciName, etStartRange, etEndRange;
    private AddItemSpeciAdapter addItemSpeciAdapter;
    private ArrayList<ProductSpecification> specificationArrayList = new ArrayList<>();
    private ItemSpecification itemSpecification;
    private SwitchCompat switchRequired;
    private ItemSpecification specificationGroupForAddItem;
    private String specificationGroupId;
    private Spinner spinnerSpecificationRange;
    private CustomTextView tvTo;
    private AddDetailInMultiLanguageDialog addDetailInMultiLanguageDialog;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item_specification);
        etSpeciName = (TextInputEditText) findViewById(R.id.etSpeciName);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbar(toolbar, R.drawable.ic_back, R.color.color_app_theme);
        ((TextView) findViewById(R.id.tvToolbarTitle)).setText(getString(R.string
                .text_add_item_specification));
        findViewById(R.id.btnSaveItemSpeci).setOnClickListener(this);
        spinnerSpecificationRange = findViewById(R.id.spinnerSpecificationRange);
        etStartRange = findViewById(R.id.etStartRange);
        etEndRange = findViewById(R.id.etEndRange);
        tvTo = findViewById(R.id.tvTo);
        switchRequired = (SwitchCompat) findViewById(R.id.switchRequired);
        RecyclerView rcProSpecification = (RecyclerView) findViewById(R.id.rcProSpecification);
        rcProSpecification.setLayoutManager(new LinearLayoutManager(this));
        addItemSpeciAdapter = new AddItemSpeciAdapter(this, specificationArrayList);
        rcProSpecification.setAdapter(addItemSpeciAdapter);
        initSpinnerSpecificationRange();
        etStartRange.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void afterTextChanged(Editable editable) {
                setDataForSelectRangeMax();
            }
        });
        etEndRange.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                setDataForSelectRangeMax();
            }
        });
        if (getIntent().getParcelableExtra(Constant.SPECIFICATIONS) != null) {
            itemSpecification = getIntent().getParcelableExtra(Constant
                    .SPECIFICATIONS);

            setSpecificationDetail(itemSpecification);
            Utilities.printLog("SPECIFICATION_ADDED", ApiClient.JSONResponse
                    (itemSpecification.getList()));
        }
        if (getIntent().getParcelableExtra(PRODUCT_SPECIFICATION) != null) {
            specificationGroupForAddItem =
                    getIntent()
                            .getParcelableExtra(PRODUCT_SPECIFICATION);
            for (ProductSpecification productSpecification : specificationGroupForAddItem.getList
                    ()) {
                productSpecification.setIsUserSelected(true);
            }
            setSpecificationDetail(specificationGroupForAddItem);
            Utilities.printLog("SPECIFICATION_ALL", ApiClient.JSONResponse
                    (specificationGroupForAddItem.getList()));
        }
        etSpeciName.setOnClickListener(this);

    }


    /**
     * this method set data according to specification and update UI
     */
    private void setSpecificationDetail(ItemSpecification itemSpecification) {

        if (itemSpecification != null) {
            specificationGroupId = itemSpecification.getId();
            etSpeciName.setText(itemSpecification.getName());
            etSpeciName.setTag(itemSpecification.getNameList());
            if (itemSpecification.getMaxRange() == 0 && itemSpecification.getRange() == 0) {
                etStartRange.setText(String.valueOf(0));
                etEndRange.setText(String.valueOf(0));
                spinnerSpecificationRange.setSelection(0);
                updateUIForSelectRangeMax(false);
                isTypeSingle = false;
                switchRequired.setChecked(false);
            } else {
                spinnerSpecificationRange.setSelection(itemSpecification.getMaxRange() > 0 ?
                        1 : 0);
                updateUIForSelectRangeMax(itemSpecification.getMaxRange() > 0);
                etStartRange.setText(String.valueOf(itemSpecification.getRange()));
                etEndRange.setText(String.valueOf(itemSpecification.getMaxRange()));
                isTypeSingle = itemSpecification.getType() == Constant.TYPE_SPECIFICATION_SINGLE;
                switchRequired.setChecked(itemSpecification.isRequired());
            }
            specificationArrayList.addAll(itemSpecification.getList());
            Collections.sort(specificationArrayList);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        setToolbarCameraIcon(false);
        setToolbarEditIcon(false, 0);
        return true;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        if (v.getId() == R.id.btnSaveItemSpeci) {
            validate();
        } else if (v.getId() == R.id.etSpeciName) {
            addMultiLanguageDetail(etSpeciName.getHint().toString(),
                    (List<String>) etSpeciName.getTag(),
                    new AddDetailInMultiLanguageDialog.SaveDetails() {
                        @Override
                        public void onSave(List<String> detailList) {
                            etSpeciName.setTag(detailList);

                        }
                    });
        }

    }

    /**
     * this method check that all data which selected by store user is valid or not
     */
    private void validate() {
        setDataForSelectRangeMax();
        if (TextUtils.isEmpty(etSpeciName.getText().toString())) {
            new CustomAlterDialog(this, null, getString(R.string.msg_empty_item_speci_name)) {
                @Override
                public void btnOnClick(int btnId) {
                    dismiss();
                }
            }.show();
        } else if (etStartRange.getText().toString().isEmpty()) {
            etStartRange.setError(getString(R.string.msg_plz_enter_valid_range));

        } else if (spinnerSpecificationRange.getSelectedItemPosition() == 1 && Integer.valueOf
                (etEndRange.getText().toString()) <= Integer.valueOf
                (etStartRange.getText().toString())) {
            etEndRange.setError(getString(R.string.msg_plz_enter_valid_range));
        } else {
            addSpecification();
        }
    }


    /**
     * this method save a selected specification in constant object
     */
    public void addSpecification() {
        int defaultSelectedCount = 0;
        Utilities.printLog("addItemSpeciAdapter", "Get Updated List -" + new Gson().toJson
                (addItemSpeciAdapter.getUpdatedList()));
        boolean oneSpecificationUserSelected = false;
        for (ProductSpecification productSpecification : addItemSpeciAdapter.getUpdatedList()) {
            if (productSpecification.isIsDefaultSelected()) {
                defaultSelectedCount++;
            }
            if (productSpecification.isIsUserSelected()) {
                oneSpecificationUserSelected = true;
            }

        }

        int maxDefaultCount = spinnerSpecificationRange.getSelectedItemPosition() == 1 ? Integer
                .valueOf(etEndRange.getText().toString()) :
                (Integer.valueOf(etStartRange.getText()
                        .toString()) ==
                        0) ? Integer.MAX_VALUE :
                        Integer.valueOf(etStartRange.getText()
                                .toString());
        if (!oneSpecificationUserSelected || addItemSpeciAdapter.getUpdatedList().size() <
                Integer.valueOf(etStartRange
                        .getText()
                        .toString())) {
            new CustomAlterDialog(this, null, getString(R.string.msg_single_type)) {
                @Override
                public void btnOnClick(int btnId) {
                    dismiss();
                }
            }.show();
        } else if (defaultSelectedCount > maxDefaultCount) {
            new CustomAlterDialog(this, null, getString(R.string
                    .msg_plz_enter_valid_default_selection)) {
                @Override
                public void btnOnClick(int btnId) {
                    dismiss();
                }
            }.show();
        } else {
            itemSpecification = new ItemSpecification();
            itemSpecification.setNameList((List<String>) etSpeciName.getTag());
            itemSpecification.setId(specificationGroupId);
            itemSpecification.setType(isTypeSingle ? Constant.TYPE_SPECIFICATION_SINGLE :
                    Constant.TYPE_SPECIFICATION_MULTIPLE);
            itemSpecification.setRequired(switchRequired.isChecked());
            itemSpecification.setRange(Integer.valueOf(etStartRange.getText().toString()));
            itemSpecification.setMaxRange(Integer.valueOf(etEndRange.getText().toString()));
            itemSpecification.setList(addItemSpeciAdapter.getUpdatedList());
            Constant.itemSpeci = itemSpecification;
            onBackPressed();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void initSpinnerSpecificationRange() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.specification_range, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSpecificationRange.setAdapter(adapter);
        spinnerSpecificationRange.setOnItemSelectedListener(new AdapterView
                .OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                updateUIForSelectRangeMax(TextUtils.equals(adapterView.getItemAtPosition(i)
                                .toString(),
                        getString(R.string
                                .text_selected_range_max)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private void updateUIForSelectRangeMax(boolean isSelectRangeMax) {
        if (isSelectRangeMax) {
            tvTo.setVisibility(View.VISIBLE);
            etEndRange.setVisibility(View.VISIBLE);
        } else {
            tvTo.setVisibility(View.GONE);
            etEndRange.setVisibility(View.GONE);
            etEndRange.setText(String.valueOf(0));
        }

    }

    private void setDataForSelectRangeMax() {
        int startRange = TextUtils.isEmpty(etStartRange.getText().toString()) ? 0 : Integer
                .valueOf(etStartRange.getText().toString());
        int endRange = TextUtils.isEmpty(etEndRange.getText().toString()) ? 0 : Integer.valueOf
                (etEndRange.getText().toString());
        isTypeSingle = startRange == 1 && endRange == 0;
        switchRequired.setChecked(startRange != 0);
    }

    private void addMultiLanguageDetail(String title, List<String> detailMap,
                                        @NonNull AddDetailInMultiLanguageDialog.SaveDetails saveDetails) {

        if (addDetailInMultiLanguageDialog != null && addDetailInMultiLanguageDialog.isShowing()) {
            return;
        }
        addDetailInMultiLanguageDialog = new AddDetailInMultiLanguageDialog(this, title,
                saveDetails, detailMap, false);
        addDetailInMultiLanguageDialog.show();
    }
}
