package com.edelivery.store.component;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edelivery.store.models.datamodel.Languages;
import com.edelivery.store.models.singleton.Language;
import com.elluminati.edelivery.store.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

/**
 * Created by Ravi Bhalodi on 13,January,2020 in Elluminati
 */
public class AddDetailInMultiLanguageDialog extends Dialog {
    private String dialogTitle;
    private Context context;
    private LinearLayout llContainer;
    private SaveDetails saveDetails;
    private List<String> detailList;
    private boolean isAdminLanguage;

    public AddDetailInMultiLanguageDialog(@NonNull Context context
            , @NonNull String dialogTitle, @NonNull SaveDetails saveDetails,
                                          List<String> detailList, boolean isAdminLanguage) {
        super(context);
        this.dialogTitle = dialogTitle;
        this.context = context;
        this.saveDetails = saveDetails;
        this.detailList = detailList;
        this.isAdminLanguage = isAdminLanguage;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_deatil_in_multi_language);
        TextView txDialogTitle = findViewById(R.id.txDialogTitle);
        txDialogTitle.setText(dialogTitle);
        llContainer = findViewById(R.id.llContainer);
        ArrayList<Languages> languages;
        if(isAdminLanguage)
            languages = Language.getInstance().getAdminLanguages();
        else
            languages = Language.getInstance().getStoreLanguages();
        if (languages != null && !languages.isEmpty()) {
            /*for (Languages language : languages) {
                View view =
                        LayoutInflater.from(context).inflate(R.layout.item_multi_language_detail,
                                null);
                TextInputLayout textInputLayout = view.findViewById(R.id.tilLanguage);
                textInputLayout.setHint(language.getName());
                textInputLayout.setTag(language.getCode());
                if (detailMap != null) {
                    EditText editText = textInputLayout.getEditText();
                    if (!TextUtils.isEmpty(detailMap.get(language.getCode()))) {
                        editText.setText(detailMap.get(language.getCode()));
                    }
                }
                llContainer.addView(view);
            }*/


            for (int i=0; i< languages.size() ; i++) {
                Languages language = languages.get(i);
                View view =
                        LayoutInflater.from(context).inflate(R.layout.item_multi_language_detail,
                                null);
                TextInputLayout textInputLayout = view.findViewById(R.id.tilLanguage);
                textInputLayout.setHint(language.getName());
                textInputLayout.setTag(language.getCode());
                if (detailList != null && !detailList.isEmpty() && i < detailList.size()) {
                    EditText editText = textInputLayout.getEditText();
                    editText.setText(detailList.get(i));
                }
                if(!language.isVisible() && !isAdminLanguage){
                    textInputLayout.setVisibility(View.GONE);
                }else {
                    textInputLayout.setVisibility(View.VISIBLE);
                }

                llContainer.addView(view);
            }
        }
        findViewById(R.id.btnNegative).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        findViewById(R.id.btnPositive).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isDefaultDataNotSet = false;
                if (llContainer.getChildCount() > 0) {
                    List<String> detailList = new ArrayList<>();
                    int size = llContainer.getChildCount();
                    for (int i = 0; i < size; i++) {
                        TextInputLayout textInputLayout =
                                (TextInputLayout) llContainer.getChildAt(i);
                        EditText editText = textInputLayout.getEditText();
                        if(i == 0 && TextUtils.isEmpty(editText.getText().toString().trim())){
                            isDefaultDataNotSet = true;
                            editText.setError(context.getResources().getString(R.string.msg_enter_detail_for_default_language));
                            break;
                        }
                        if (!TextUtils.isEmpty(editText.getText().toString().trim())) {
                            detailList.add(editText.getText().toString().trim());
                        }

                    }
                    saveDetails.onSave(detailList);


                }
                if(!isDefaultDataNotSet)
                    dismiss();
            }
        });
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        setCancelable(true);

    }

    public interface SaveDetails {
//        void onSave(Map<String, String> detailMap);
        void onSave(List<String> detailList);
    }

}
