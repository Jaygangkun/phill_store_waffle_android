package com.edelivery.store;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edelivery.store.adapter.SpecificationGroupItemAdapter;
import com.edelivery.store.models.datamodel.Languages;
import com.edelivery.store.models.datamodel.SetSpecificationGroup;
import com.edelivery.store.models.datamodel.SetSpecificationList;
import com.edelivery.store.models.datamodel.SpecificationGroup;
import com.edelivery.store.models.datamodel.Specifications;
import com.edelivery.store.models.responsemodel.AddOrDeleteSpecificationResponse;
import com.edelivery.store.models.responsemodel.IsSuccessResponse;
import com.edelivery.store.models.singleton.Language;
import com.edelivery.store.parse.ApiClient;
import com.edelivery.store.parse.ApiInterface;
import com.edelivery.store.parse.ParseContent;
import com.edelivery.store.utils.Constant;
import com.edelivery.store.utils.PreferenceHelper;
import com.edelivery.store.utils.Utilities;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpecificationGroupItemActivity extends BaseActivity {
    private LinearLayout llNewSpecification;
    private CustomTextView tvAddSpecification;
    private RecyclerView rcSpecification;
    private SpecificationGroupItemAdapter specificationGroupItemAdapter;
    private FloatingActionButton floatingBtn;
    private SpecificationGroup specificationGroup;
    private ArrayList<Specifications> specificationsArrayList = new ArrayList<>();
    private Dialog addDetailInMultiLanguageDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specification_group_item);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbar(toolbar, R.drawable.ic_back, R.color.color_app_theme);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        llNewSpecification = findViewById(R.id.llNewSpecification);
        tvAddSpecification = (CustomTextView) findViewById(R.id.tvAddSpecification);
        tvAddSpecification.setOnClickListener(this);
        tvAddSpecification.setVisibility(View.GONE);
        rcSpecification = (RecyclerView) findViewById(R.id.rcSpecification);
        floatingBtn = findViewById(R.id.floatingBtn);
        floatingBtn.setOnClickListener(this);
        loadExtraData();
        initRcvSpecificationGroup();
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

    private void addEditText() {
        View view = LayoutInflater.from(this).inflate(R.layout.new_specification_itam, null);
        view.findViewById(R.id.etSpecificationName).setOnClickListener(this);
        llNewSpecification.addView(view);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tvAddSpecification:
                addORUpdateSpecificationDialog(0, null, false);
                break;
            case R.id.floatingBtn:
                if (isEdited()) {
                    floatingBtn.setImageResource(R.drawable.ic_plus);
                    tvAddSpecification.setVisibility(View.GONE);
                    specificationGroupItemAdapter.setEdited(false);
                    addOrDeleteSpecification(specificationGroup.getId());
                } else {
                    floatingBtn.setImageResource(R.drawable.ic_check_black_24dp);
                    tvAddSpecification.setVisibility(View.VISIBLE);
                    specificationGroupItemAdapter.setEdited(true);
                }
                break;

        }
    }

    private void loadExtraData() {
        if (getIntent().getExtras() != null) {
            specificationGroup = getIntent().getExtras().getParcelable(Constant.SPECIFICATIONS);
            ((TextView) findViewById(R.id.tvToolbarTitle)).setText(specificationGroup.getName());
            specificationsArrayList.addAll(specificationGroup.getSpecifications());
        }

    }

    private void initRcvSpecificationGroup() {
        specificationGroupItemAdapter = new SpecificationGroupItemAdapter(specificationsArrayList,
                this);
        rcSpecification.setNestedScrollingEnabled(false);
        rcSpecification.setLayoutManager(new LinearLayoutManager(this));
        rcSpecification.setAdapter(specificationGroupItemAdapter);
        rcSpecification.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration
                .VERTICAL));
    }


    public void addOrDeleteSpecification(String groupId) {
        ArrayList<Specifications.NameAndPrice> addSpecificationGroupItem = new ArrayList<>();
        if (llNewSpecification != null) {
           /* int count = llNewSpecification.getChildCount();
            if (count > 0) {
                Utilities.showCustomProgressDialog(this, false);
                for (int i = 0; i < count; i++) {
                    LinearLayout llSpecification = (LinearLayout) llNewSpecification.getChildAt(i);
                    if (llSpecification != null) {
                        EditText etSpecificationName = (EditText) llSpecification.getChildAt(0);
                        EditText etSpecificationPrice = (EditText) llSpecification.getChildAt(1);
                        if (!TextUtils.isEmpty(etSpecificationName.getText().toString().trim())) {
                            double price = TextUtils.isEmpty(etSpecificationPrice.getText()
                                    .toString()) ? 0 : Double.valueOf(etSpecificationPrice.getText
                                    ().toString().trim());
                            addSpecificationGroupItem.add(new Specifications().new
                                    NameAndPrice(price,
                                    (List<String>) etSpecificationName.getTag(), 0))
                            ;

                        }
                    }


                }
                llNewSpecification.removeAllViews();
            }*/
            if (!addSpecificationGroupItem.isEmpty()) {
                addSpecification(groupId, addSpecificationGroupItem);
            } else if (!specificationGroupItemAdapter.getSpecificationIds().isEmpty()) {
                deleteSpecification(specificationGroup.getId());
            } else {
                Utilities.hideCustomProgressDialog();
            }
        }
    }

    /**
     * this method call a webservice for add specification in
     * group
     *
     * @param groupId in string
     */
    private void addSpecification(final String groupId, ArrayList<Specifications.NameAndPrice>
            addSpecificationGroupItem) {
        Utilities.showCustomProgressDialog(this, false);
        SetSpecificationList setSpecificationList = new SetSpecificationList();
        setSpecificationList.setStoreId(PreferenceHelper.getPreferenceHelper(this)
                .getStoreId
                        ());
        setSpecificationList.setServerToken(PreferenceHelper.getPreferenceHelper(this)
                .getServerToken());
        setSpecificationList.setSpecificationName(addSpecificationGroupItem);
        setSpecificationList.setSpecificationGroupId(groupId);

        Call<AddOrDeleteSpecificationResponse> call = ApiClient.getClient().create
                (ApiInterface.class).
                addSpecification(ApiClient.makeGSONRequestBody(setSpecificationList));
        call.enqueue(new Callback<AddOrDeleteSpecificationResponse>() {
            @Override
            public void onResponse(Call<AddOrDeleteSpecificationResponse> call,
                                   Response<AddOrDeleteSpecificationResponse>
                                           response) {

                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        if (specificationGroupItemAdapter.getSpecificationIds().isEmpty()) {
                            specificationsArrayList.clear();
                            specificationsArrayList.addAll(response.body()
                                    .getSpecifications());
                            specificationGroup.setSpecifications(response.body()
                                    .getSpecifications());
                            specificationGroupItemAdapter.notifyDataSetChanged();
                            Utilities.hideCustomProgressDialog();
                        } else {
                            deleteSpecification(groupId);
                        }
                    } else {
                        ParseContent.getParseContentInstance().showErrorMessage
                                (SpecificationGroupItemActivity.this, response.body()
                                                .getErrorCode(),
                                        true);
                        Utilities.hideCustomProgressDialog();
                    }

                } else {
                    Utilities.showHttpErrorToast(response.code(),
                            SpecificationGroupItemActivity.this);
                }
            }

            @Override
            public void onFailure(Call<AddOrDeleteSpecificationResponse> call,
                                  Throwable t) {

            }
        });
    }

    /**
     * this method call webservice for delete particular specification in group
     *
     * @param groupId in string
     */
    private void deleteSpecification(String groupId) {
        Utilities.showCustomProgressDialog(this, false);
        SetSpecificationList setSpecificationList = new SetSpecificationList();
        setSpecificationList.setStoreId(PreferenceHelper.getPreferenceHelper(this)
                .getStoreId
                        ());
        setSpecificationList.setServerToken(PreferenceHelper.getPreferenceHelper(this)
                .getServerToken());
        setSpecificationList.setSpecificationGroupId(groupId);
        setSpecificationList.setSpecificationId(specificationGroupItemAdapter.getSpecificationIds
                ());
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<AddOrDeleteSpecificationResponse> responseCall = apiInterface.deleteSpecification(
                (ApiClient.makeGSONRequestBody(setSpecificationList)));
        responseCall.enqueue(new Callback<AddOrDeleteSpecificationResponse>() {
            @Override
            public void onResponse(Call<AddOrDeleteSpecificationResponse> call,
                                   Response<AddOrDeleteSpecificationResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        specificationsArrayList.clear();
                        specificationsArrayList.addAll(response.body()
                                .getSpecifications());
                        specificationGroup.setSpecifications(response.body()
                                .getSpecifications());
                        specificationGroupItemAdapter.notifyDataSetChanged();
                        specificationGroupItemAdapter.getSpecificationIds().clear();
                    } else {
                        ParseContent.getParseContentInstance().showErrorMessage
                                (SpecificationGroupItemActivity.this, response.body()
                                                .getErrorCode(),
                                        true);
                    }
                } else {
                    Utilities.showHttpErrorToast(response.code(), SpecificationGroupItemActivity
                            .this);
                }
                Utilities.hideCustomProgressDialog();
            }

            @Override
            public void onFailure(Call<AddOrDeleteSpecificationResponse> call, Throwable t) {
                Utilities.printLog("ProductDetail", t.getMessage());
            }
        });
    }

    /**
     * this method call webservice for add number of new specification group
     */
    private void updateSpecificationName(final CustomTextView tvSpecificationGroupName,
                                         final int position, final List<String> detailList,
                                         final String sequenceNumber) {

        Utilities.showCustomProgressDialog(this, false);

        if (!detailList.isEmpty()) {
            SetSpecificationGroup setSpecificationGroup = new SetSpecificationGroup();
            setSpecificationGroup.setStoreId(PreferenceHelper.getPreferenceHelper(this)
                    .getStoreId());
            setSpecificationGroup.setServerToken(PreferenceHelper.getPreferenceHelper(this)
                    .getServerToken());
            setSpecificationGroup.setSpId(specificationsArrayList.get(position).getId());
            setSpecificationGroup.setSequenceNumber(TextUtils.isEmpty(sequenceNumber) ? 0 :
                    Long.parseLong(sequenceNumber));
            setSpecificationGroup.setName(detailList);
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<IsSuccessResponse> responseCall = apiInterface.updateSpecificationName
                    (ApiClient
                            .makeGSONRequestBody(setSpecificationGroup));
            responseCall.enqueue(new Callback<IsSuccessResponse>() {
                @Override
                public void onResponse(Call<IsSuccessResponse> call, Response<IsSuccessResponse>
                        response) {
                    if (response.isSuccessful()) {
                        if (response.body().isSuccess()) {
                            Utilities.hideCustomProgressDialog();
                            specificationsArrayList.get(position).setName(detailList);
                            specificationsArrayList.get(position).setSequenceNumber(TextUtils.isEmpty(sequenceNumber) ? 0 :
                                    Long.parseLong(sequenceNumber));
                            specificationGroupItemAdapter.notifyDataChange();
                            tvSpecificationGroupName.setTag(detailList);
                            tvSpecificationGroupName.setText(Utilities.getDetailStringFromList(detailList,
                                    Language.getInstance().getStoreLanguageIndex()));
                        }
                    } else {
                        Utilities.showHttpErrorToast(response.code(),
                                SpecificationGroupItemActivity
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


    public void addORUpdateSpecificationDialog(final int position,
                                               final CustomTextView tvSpecificationGroupName,
                                               final boolean isUpdate) {

        if (addDetailInMultiLanguageDialog != null && addDetailInMultiLanguageDialog.isShowing()) {
            return;
        }
        addDetailInMultiLanguageDialog = new Dialog(this);
        addDetailInMultiLanguageDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        addDetailInMultiLanguageDialog.setContentView(R.layout.dialog_specification_in_multi_language);
        final EditText etSpecificationPrice =
                addDetailInMultiLanguageDialog.findViewById(R.id.etSpecificationPrice);
        final EditText etSpecificationSequenceNumber =
                addDetailInMultiLanguageDialog.findViewById(R.id.etSpecificationSequenceNumber);
        if (isUpdate) {
            etSpecificationPrice.setVisibility(View.GONE);
            etSpecificationSequenceNumber.setText(String.valueOf(specificationsArrayList.get(position).getSequenceNumber()));
        } else {
            etSpecificationPrice.setVisibility(View.VISIBLE);
        }


        List<String> detailMap = null;
        if (tvSpecificationGroupName != null) {
            detailMap = (List<String>) tvSpecificationGroupName.getTag();
        }
        TextView txDialogTitle = addDetailInMultiLanguageDialog.findViewById(R.id.txDialogTitle);
        txDialogTitle.setText(R.string.text_specification_group);
        LinearLayout llContainer = addDetailInMultiLanguageDialog.findViewById(R.id.llContainer);
        ArrayList<Languages> languages;
        languages = Language.getInstance().getStoreLanguages();
        if (languages != null && !languages.isEmpty()) {

            for (int i = 0; i < languages.size(); i++) {
                Languages language = languages.get(i);
                View view =
                        LayoutInflater.from(this).inflate(R.layout.item_multi_language_detail,
                                null);
                TextInputLayout textInputLayout = view.findViewById(R.id.tilLanguage);
                textInputLayout.setHint(language.getName());
                textInputLayout.setTag(language.getCode());
                if (detailMap != null && !detailMap.isEmpty() && i < detailMap.size()) {
                    EditText editText = textInputLayout.getEditText();
                    editText.setText(detailMap.get(i));
                }
                if (!language.isVisible()) {
                    textInputLayout.setVisibility(View.GONE);
                } else {
                    textInputLayout.setVisibility(View.VISIBLE);
                }

                llContainer.addView(view);
            }
        }
        addDetailInMultiLanguageDialog.findViewById(R.id.btnNegative).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDetailInMultiLanguageDialog.dismiss();
            }
        });
        final LinearLayout finalLlContainer = llContainer;
        addDetailInMultiLanguageDialog.findViewById(R.id.btnPositive).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isDefaultDataNotSet = false;
                if (finalLlContainer.getChildCount() > 0) {
                    List<String> detailList = new ArrayList<>();
                    int size = finalLlContainer.getChildCount();
                    for (int i = 0; i < size; i++) {
                        TextInputLayout textInputLayout =
                                (TextInputLayout) finalLlContainer.getChildAt(i);
                        EditText editText = textInputLayout.getEditText();
                        if (i == 0 && TextUtils.isEmpty(editText.getText().toString().trim())) {
                            isDefaultDataNotSet = true;
                            editText.setError(getResources().getString(R.string.msg_enter_detail_for_default_language));
                            break;
                        }
                        if (!TextUtils.isEmpty(editText.getText().toString().trim())) {
                            detailList.add(editText.getText().toString().trim());
                        }

                    }
                    if (isUpdate) {
                        updateSpecificationName(tvSpecificationGroupName,
                                position, detailList,
                                etSpecificationSequenceNumber.getText().toString());
                    } else {
                        ArrayList<Specifications.NameAndPrice> addSpecificationGroupItem =
                                new ArrayList<>();
                        double price = TextUtils.isEmpty(etSpecificationPrice.getText()
                                .toString()) ? 0 : Double.valueOf(etSpecificationPrice.getText
                                ().toString().trim());
                        addSpecificationGroupItem.add(new Specifications().new
                                NameAndPrice(price,
                                detailList,
                                TextUtils.isEmpty(etSpecificationSequenceNumber.getText().toString()) ? 0 : Long.parseLong(etSpecificationSequenceNumber.getText().toString())));

                        addSpecification(specificationGroup.getId(), addSpecificationGroupItem);
                    }
                    addDetailInMultiLanguageDialog.dismiss();


                }
                if (!isDefaultDataNotSet)
                    addDetailInMultiLanguageDialog.dismiss();
            }
        });
        WindowManager.LayoutParams params =
                addDetailInMultiLanguageDialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        addDetailInMultiLanguageDialog.setCancelable(true);
        addDetailInMultiLanguageDialog.show();
    }
}

