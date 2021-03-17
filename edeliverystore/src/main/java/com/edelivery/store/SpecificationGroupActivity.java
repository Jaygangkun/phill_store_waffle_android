package com.edelivery.store;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edelivery.store.adapter.SpecificationGroupAdapter;
import com.edelivery.store.component.AddDetailInMultiLanguageDialog;
import com.edelivery.store.models.datamodel.SetSpecificationGroup;
import com.edelivery.store.models.datamodel.SetSpecificationList;
import com.edelivery.store.models.datamodel.SpecificationGroup;
import com.edelivery.store.models.responsemodel.IsSuccessResponse;
import com.edelivery.store.models.responsemodel.SpecificationGroupResponse;
import com.edelivery.store.models.singleton.Language;
import com.edelivery.store.parse.ApiClient;
import com.edelivery.store.parse.ApiInterface;
import com.edelivery.store.parse.ParseContent;
import com.edelivery.store.utils.Constant;
import com.edelivery.store.utils.PreferenceHelper;
import com.edelivery.store.utils.Utilities;
import com.edelivery.store.widgets.CustomFontTextViewTitle;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpecificationGroupActivity extends BaseActivity {
    private LinearLayout llNewSpecification;
    private CustomTextView tvAddSpecification;
    private RecyclerView rcSpecification;
    private ArrayList<SpecificationGroup> specificationGroups = new ArrayList<>();
    private SpecificationGroupAdapter group2Adapter;
    private FloatingActionButton floatingBtn;
    private AddDetailInMultiLanguageDialog addDetailInMultiLanguageDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specificatrion_group);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbar(toolbar, R.drawable.ic_back, R.color.color_app_theme);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ((TextView) findViewById(R.id.tvToolbarTitle)).setText(getString(R.string
                .text_product_specification_group));
        llNewSpecification = findViewById(R.id.llNewSpecification);
        tvAddSpecification = (CustomTextView) findViewById(R.id.tvAddSpecification);
        tvAddSpecification.setOnClickListener(this);
        tvAddSpecification.setVisibility(View.GONE);
        rcSpecification = (RecyclerView) findViewById(R.id.rcSpecification);
        floatingBtn = findViewById(R.id.floatingBtn);
        floatingBtn.setOnClickListener(this);
        initRcvSpecificationGroup();
    }


    @Override
    protected void onResume() {
        super.onResume();
        getSpecificationGroup();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        setToolbarEditIcon(false, 0);
        setToolbarCameraIcon(false);
        return true;
    }

    private boolean isEdited() {
        return tvAddSpecification.getVisibility() == View.VISIBLE;
    }

    private void initRcvSpecificationGroup() {
        group2Adapter = new SpecificationGroupAdapter(specificationGroups, this);
        rcSpecification.setNestedScrollingEnabled(false);
        rcSpecification.setLayoutManager(new LinearLayoutManager(this));
        rcSpecification.setAdapter(group2Adapter);
        rcSpecification.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration
                .VERTICAL));
    }

    /**
     * this method call webservice for all specification group for particular product
     */
    private void getSpecificationGroup() {
        Utilities.showCustomProgressDialog(this, false);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.SERVER_TOKEN, PreferenceHelper.getPreferenceHelper(this)
                    .getServerToken());
            jsonObject.put(Constant.STORE_ID, PreferenceHelper.getPreferenceHelper(this)
                    .getStoreId());
        } catch (JSONException e) {
            Utilities.printLog("ProductDetail", e.getMessage());
        }

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<SpecificationGroupResponse> responseCall = apiInterface.getSpecificationGroup
                (ApiClient.makeJSONRequestBody(jsonObject));
        responseCall.enqueue(new Callback<SpecificationGroupResponse>() {
            @Override
            public void onResponse(Call<SpecificationGroupResponse> call,
                                   Response<SpecificationGroupResponse> response) {

                if (response.isSuccessful()) {
                    Utilities.hideCustomProgressDialog();
                    specificationGroups.clear();
                    if (response.body().isSuccess()) {
                        Utilities.printLog("SPECIFICATION_GROUP", ApiClient.JSONResponse(response
                                .body()));
                        specificationGroups.addAll(response.body().getSpecificationGroup());
                        Collections.sort(specificationGroups);
                    } else {
                        ParseContent.getParseContentInstance().showErrorMessage
                                (SpecificationGroupActivity.this, response.body().getErrorCode(),
                                        true);
                    }
                } else {
                    Utilities.showHttpErrorToast(response.code(), SpecificationGroupActivity
                            .this);
                }
                if (group2Adapter != null) {
                    group2Adapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onFailure(Call<SpecificationGroupResponse> call, Throwable t) {
                Utilities.printLog("ProductDetail", t.getMessage());
            }
        });
    }

    /**
     * this method call a webservice for delete Specification Group
     *
     * @param position list item position
     */
    public void deleteSpecificationGroup(final int position) {
        Utilities.showCustomProgressDialog(this, false);
        SetSpecificationList setSpecificationList = new SetSpecificationList();
        setSpecificationList.setStoreId(PreferenceHelper.getPreferenceHelper(this)
                .getStoreId
                        ());
        setSpecificationList.setServerToken(PreferenceHelper.getPreferenceHelper(this)
                .getServerToken());
        setSpecificationList.setSpecificationGroupId(specificationGroups.get(position).getId());
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<IsSuccessResponse> responseCall = apiInterface.deleteSpecificationGroup(
                (ApiClient.makeGSONRequestBody(setSpecificationList)));
        responseCall.enqueue(new Callback<IsSuccessResponse>() {
            @Override
            public void onResponse(Call<IsSuccessResponse> call, Response<IsSuccessResponse>
                    response) {
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        specificationGroups.remove(position);
                        group2Adapter.notifyDataSetChanged();
                        parseContent.showMessage(SpecificationGroupActivity
                                .this, response.body().getMessage());
                    } else {
                        ParseContent.getParseContentInstance().showErrorMessage
                                (SpecificationGroupActivity.this, response.body().getErrorCode(),
                                        true);
                    }
                } else {
                    Utilities.showHttpErrorToast(response.code(), SpecificationGroupActivity
                            .this);
                }
                Utilities.hideCustomProgressDialog();
            }

            @Override
            public void onFailure(Call<IsSuccessResponse> call, Throwable t) {
            }
        });

    }


    private void addEditText() {
        View view = LayoutInflater.from(this).inflate(R.layout.new_specification, null);
        view.setOnClickListener(this);
        llNewSpecification.addView(view);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tvAddSpecification:
                if (isEdited()) {
                    addEditText();
                }
                break;
            case R.id.floatingBtn:
                if (isEdited()) {
                    floatingBtn.setImageResource(R.drawable.ic_plus);
                    tvAddSpecification.setVisibility(View.GONE);
                    group2Adapter.setEdited(false);
                    addSpecificationGroup();
                } else {
                    floatingBtn.setImageResource(R.drawable.ic_check_black_24dp);
                    tvAddSpecification.setVisibility(View.VISIBLE);
                    group2Adapter.setEdited(true);
                }
                break;
            default:
                if (v instanceof EditText) {
                    final EditText editText = (EditText) v;
                    addMultiLanguageDetail(editText.getHint().toString(),
                            (List<String>) editText.getTag(),
                            new AddDetailInMultiLanguageDialog.SaveDetails() {
                                @Override
                                public void onSave(List<String> detailList) {
                                    editText.setTag(detailList);
                                    editText.setText(Utilities.getDetailStringFromList(detailList,
                                            Language.getInstance().getStoreLanguageIndex()));
                                }
                            });
                }
                break;
        }
    }

    /**
     * this method call webservice for add number of new specification group
     */
    private void addSpecificationGroup() {

        ArrayList<List<String>> specificationGroup = new ArrayList<>();
        int count = llNewSpecification.getChildCount();
        if (count > 0) {
            Utilities.showCustomProgressDialog(this, false);
            for (int i = 0; i < count; i++) {
                EditText editText = (EditText) llNewSpecification.getChildAt(i);
                if (editText != null && editText.getTag() != null) {
                    specificationGroup.add((List<String>) editText.getTag());
                }
            }
            llNewSpecification.removeAllViews();
            if (!specificationGroup.isEmpty()) {
                SetSpecificationGroup setSpecificationGroup = new SetSpecificationGroup();
                setSpecificationGroup.setStoreId(PreferenceHelper.getPreferenceHelper(this)
                        .getStoreId());
                setSpecificationGroup.setServerToken(PreferenceHelper.getPreferenceHelper(this)
                        .getServerToken());
                setSpecificationGroup.setSpecificationGroup(specificationGroup);
                ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                Call<IsSuccessResponse> responseCall = apiInterface.addSpecificationGroup
                        (ApiClient
                                .makeGSONRequestBody(setSpecificationGroup));
                responseCall.enqueue(new Callback<IsSuccessResponse>() {
                    @Override
                    public void onResponse(Call<IsSuccessResponse> call, Response<IsSuccessResponse>
                            response) {
                        if (response.isSuccessful()) {
                            if (response.body().isSuccess()) {
                                getSpecificationGroup();
                            }
                        } else {
                            Utilities.showHttpErrorToast(response.code(),
                                    SpecificationGroupActivity
                                            .this);
                        }
                    }

                    @Override
                    public void onFailure(Call<IsSuccessResponse> call, Throwable t) {
                        Utilities.printLog("ProductDetail", t.getMessage());
                    }
                });
            } else {
                Utilities.hideCustomProgressDialog();
            }


        }

    }

    /**
     * this method call webservice for add number of new specification group
     */
    private void updateSpecificationGroupName(final CustomFontTextViewTitle tvSpecificationGroupName,
                                              final int position, final List<String> detailList) {

        Utilities.showCustomProgressDialog(this, false);


        if (!detailList.isEmpty()) {
            SetSpecificationGroup setSpecificationGroup = new SetSpecificationGroup();
            setSpecificationGroup.setStoreId(PreferenceHelper.getPreferenceHelper(this)
                    .getStoreId());
            setSpecificationGroup.setServerToken(PreferenceHelper.getPreferenceHelper(this)
                    .getServerToken());
            setSpecificationGroup.setSpId(specificationGroups.get(position).getId());
            setSpecificationGroup.setName(detailList);
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<IsSuccessResponse> responseCall = apiInterface.updateSpecificationGroupName
                    (ApiClient
                            .makeGSONRequestBody(setSpecificationGroup));
            responseCall.enqueue(new Callback<IsSuccessResponse>() {
                @Override
                public void onResponse(Call<IsSuccessResponse> call, Response<IsSuccessResponse>
                        response) {
                    if (response.isSuccessful()) {
                        if (response.body().isSuccess()) {
                            Utilities.hideCustomProgressDialog();
                            specificationGroups.get(position).setName(detailList);
                            tvSpecificationGroupName.setTag(detailList);
                            tvSpecificationGroupName.setText(Utilities.getDetailStringFromList(detailList,
                                    Language.getInstance().getStoreLanguageIndex()));
                        }
                    } else {
                        Utilities.showHttpErrorToast(response.code(),
                                SpecificationGroupActivity
                                        .this);
                    }
                }

                @Override
                public void onFailure(Call<IsSuccessResponse> call, Throwable t) {
                    Utilities.printLog("ProductDetail", t.getMessage());
                }
            });
        } else {
            Utilities.hideCustomProgressDialog();
        }
    }


    public void updateSpecification(final int position,
                                    final CustomFontTextViewTitle tvSpecificationGroupName) {
        addMultiLanguageDetail(getResources().getString(R.string.text_specification_group_name),
                (List<String>) tvSpecificationGroupName.getTag(),
                new AddDetailInMultiLanguageDialog.SaveDetails() {
                    @Override
                    public void onSave(List<String> detailList) {
                        updateSpecificationGroupName(tvSpecificationGroupName,
                                position, detailList);
                    }
                });
    }

    public void goToSpecificationGroupItemActivity(int position) {
        Intent intent = new Intent(this, SpecificationGroupItemActivity.class);
        intent.putExtra(Constant.SPECIFICATIONS, specificationGroups.get(position));
        startActivity(intent);
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
