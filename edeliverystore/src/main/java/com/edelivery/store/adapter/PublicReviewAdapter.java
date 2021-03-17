package com.edelivery.store.adapter;

import androidx.core.content.res.ResourcesCompat;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.edelivery.store.ReviewActivity;
import com.edelivery.store.models.datamodel.StoreReview;
import com.edelivery.store.parse.ParseContent;
import com.edelivery.store.utils.GlideApp;
import com.edelivery.store.utils.Utilities;
import com.edelivery.store.widgets.CustomFontTextViewTitle;
import com.edelivery.store.widgets.CustomTextView;
import com.elluminati.edelivery.store.R;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import static com.elluminati.edelivery.store.BuildConfig.IMAGE_URL;


/**
 * Created by elluminati on 21-Nov-17.
 */

public abstract class PublicReviewAdapter extends RecyclerView.Adapter<PublicReviewAdapter
        .PublicReviewHolder> {
    private List<StoreReview> storeReview;
    private ReviewActivity reviewFragment;

    public PublicReviewAdapter(List<StoreReview> storeReview, ReviewActivity
            reviewFragment) {
        this.storeReview = storeReview;
        this.reviewFragment = reviewFragment;
    }

    @Override
    public PublicReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()
        ).inflate(R.layout.item_review_2,
                parent, false);
        return new PublicReviewHolder(view);
    }

    @Override
    public void onBindViewHolder(PublicReviewHolder holder, final int position) {
        final StoreReview reviewListItem = storeReview.get(position);
        GlideApp.with(reviewFragment
        ).load(IMAGE_URL + reviewListItem
                .getUserDetail().getImageUrl())
                .dontAnimate()
                .placeholder
                        (ResourcesCompat.getDrawable(reviewFragment

                                .getResources(), R.drawable.placeholder, null)).fallback
                (ResourcesCompat
                        .getDrawable
                                (reviewFragment

                                        .getResources(), R.drawable.placeholder, null)).into
                (holder.ivUserImage);
        holder.tvUserName.setText(reviewListItem.getUserDetail().getFirstName() + " " +
                "" + reviewListItem.getUserDetail().getLastName());
        holder.tvRate.setText(String.valueOf(reviewListItem.getUserRatingToStore()));
        if (TextUtils.isEmpty(reviewListItem.getUserReviewToStore())) {
            holder.llLike.setVisibility(View.GONE);
            holder.tvUserComment.setVisibility(View.GONE);
        } else {
            holder.tvUserComment.setText(reviewListItem.getUserReviewToStore());
            holder.tvUserComment.setVisibility(View.VISIBLE);
            holder.llLike.setVisibility(View.VISIBLE);
            holder.tvLike.setCompoundDrawablesRelativeWithIntrinsicBounds(AppCompatResources
                            .getDrawable(reviewFragment
                                    , R.drawable
                                            .ic_thumbs_up_01_unselect),
                    null, null, null);
            holder.tvDisLike.setCompoundDrawablesRelativeWithIntrinsicBounds(AppCompatResources
                            .getDrawable(reviewFragment
                                    , R.drawable
                                            .ic_thumbs_down_01_unselect),
                    null, null, null);

        }
        holder.tvLike.setText(String.valueOf(reviewListItem.getIdOfUsersLikeStoreComment().size()));
        holder.tvDisLike.setText(String.valueOf(reviewListItem.getIdOfUsersDislikeStoreComment()
                .size()));
        try {
            Date date = ParseContent.getParseContentInstance().webFormat.parse(reviewListItem
                    .getCreatedAt());
            holder.tvDate.setText(ParseContent.getParseContentInstance().dateFormat3.format(date));
        } catch (ParseException e) {
            Utilities.handleException(PublicReviewAdapter.class.getName(), e);
        }
        holder.tvOrderId.setText(reviewFragment.getResources().getString(R.string.text_order_no)
                + "" + reviewListItem
                .getOrderUniqueId());

    }

    @Override
    public int getItemCount() {
        return storeReview.size();
    }

    public abstract void onLike(int position);

    public abstract void onDislike(int position);

    protected class PublicReviewHolder extends RecyclerView.ViewHolder {
        CustomFontTextViewTitle tvUserName;
        CustomTextView tvRate, tvDate, tvUserComment, tvLike, tvDisLike, tvOrderId;
        ImageView ivUserImage;
        LinearLayout llLike;


        public PublicReviewHolder(View itemView) {
            super(itemView);
            tvUserName = (CustomFontTextViewTitle) itemView.findViewById(R.id.tvUserName);
            tvRate = (CustomTextView) itemView.findViewById(R.id.tvRate);
            tvDate = (CustomTextView) itemView.findViewById(R.id.tvDate);
            tvUserComment = (CustomTextView) itemView.findViewById(R.id.tvUserComment);
            tvLike = (CustomTextView) itemView.findViewById(R.id.tvLike);
            tvDisLike = (CustomTextView) itemView.findViewById(R.id.tvDisLike);
            ivUserImage = (ImageView) itemView.findViewById(R.id.ivUserImage);
            llLike = (LinearLayout) itemView.findViewById(R.id.llLike);
            tvOrderId = (CustomTextView) itemView.findViewById(R.id.tvOrderId);


        }


    }
}
