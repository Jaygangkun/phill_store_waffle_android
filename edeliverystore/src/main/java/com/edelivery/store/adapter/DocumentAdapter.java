package com.edelivery.store.adapter;

import android.content.Context;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.edelivery.store.models.datamodel.Documents;
import com.edelivery.store.parse.ParseContent;
import com.edelivery.store.utils.GlideApp;
import com.edelivery.store.utils.Utilities;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;

import java.text.ParseException;
import java.util.List;

import static com.elluminati.edelivery.store.BuildConfig.IMAGE_URL;

/**
 * Created by elluminati on 28-Apr-17.
 */

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.DocumentViewHolder> {

    private List<Documents> documentsList;
    private Context context;
    private ParseContent parseContent;

    public DocumentAdapter(List<Documents> documentsList) {
        this.documentsList = documentsList;
        parseContent = ParseContent.getParseContentInstance();
    }

    @Override
    public DocumentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_document,
                parent, false);

        return new DocumentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DocumentViewHolder holder, int position) {
        Documents documents = documentsList.get(position);
        if (documents.getDocumentDetails().isIsUniqueCode()) {
            holder.tvIdNumber.setVisibility(View.VISIBLE);
            String id = context.getResources().getString(R.string.text_id_number);
            if (!TextUtils.isEmpty(documents.getUniqueCode())) {
                id = id + " " + documents.getUniqueCode();
                holder.tvIdNumber.setText(id);
            } else {
                holder.tvIdNumber.setText(id);
            }

        } else {
            holder.tvIdNumber.setVisibility(View.GONE);
        }
        if (documents.getDocumentDetails().isIsExpiredDate()) {
            String date = context.getResources().getString(R.string.text_expire_date);
            holder.tvExpireDate.setVisibility(View.VISIBLE);
            try {
                if (!TextUtils.isEmpty(documents.getExpiredDate())) {
                    date = date + " " + parseContent.dateFormat.format(parseContent
                            .webFormat
                            .parse(documents.getExpiredDate()));
                    holder.tvExpireDate.setText(date);
                } else {
                    holder.tvExpireDate.setText(date);
                }

            } catch (ParseException e) {
                Utilities.printLog(DocumentAdapter.class.getName(), e.toString());
            }

        } else {
            holder.tvExpireDate.setVisibility(View.GONE);
        }
        if (documents.getDocumentDetails().isIsMandatory()) {
            holder.tvOption.setVisibility(View.VISIBLE);
        } else {
            holder.tvOption.setVisibility(View.GONE);
        }
        holder.tvDocumentTittle.setText(documents.getDocumentDetails().getDocumentName());
        GlideApp.with(context).load(IMAGE_URL + documents.getImageUrl())
                .dontAnimate().placeholder
                (ResourcesCompat.getDrawable(context
                        .getResources(), R.drawable.uploading, null)).fallback(ResourcesCompat
                .getDrawable
                        (context.getResources(), R.drawable.uploading, null)).into
                (holder.ivDocumentImage);
    }

    @Override
    public int getItemCount() {
        return documentsList.size();
    }

    protected class DocumentViewHolder extends RecyclerView.ViewHolder {
        CustomTextView tvDocumentTittle, tvIdNumber, tvExpireDate, tvOption;
        ImageView ivDocumentImage;

        public DocumentViewHolder(View itemView) {
            super(itemView);
            tvOption = (CustomTextView) itemView.findViewById(R.id.tvOption);
            tvDocumentTittle = (CustomTextView) itemView.findViewById(R.id.tvDocumentTittle);
            tvIdNumber = (CustomTextView) itemView.findViewById(R.id.tvIdNumber);
            tvExpireDate = (CustomTextView) itemView.findViewById(R.id.tvExpireDate);
            ivDocumentImage = (ImageView) itemView.findViewById(R.id.ivDocumentImage);
        }
    }
}
