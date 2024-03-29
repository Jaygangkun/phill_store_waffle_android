package com.edelivery.store.utils;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Region;
import android.util.LayoutDirection;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PinnedHeaderItemDecoration extends RecyclerView.ItemDecoration {
    private final static String TAG = PinnedHeaderItemDecoration.class.getSimpleName();
    RecyclerView.Adapter mAdapter = null;
    // cached data
    // pinned header view
    View mPinnedHeaderView = null;
    int mHeaderPosition = -1;
    Map<Integer, Boolean> mPinnedViewTypes = new HashMap<Integer, Boolean>();
    private int mPinnedHeaderTop;
    private Rect mClipBounds;

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State
            state) {
        super.getItemOffsets(outRect, view, parent, state);

    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        createPinnedHeader(parent);

        if (mPinnedHeaderView != null) {
            // check overlap section view.
            //TODO support only vertical header currently.
            final int headerEndAt = mPinnedHeaderView.getTop() + mPinnedHeaderView.getHeight();
            final View v = parent.findChildViewUnder(c.getWidth() / 2, headerEndAt + 1);

            if (isHeaderView(parent, v)) {
                mPinnedHeaderTop = v.getTop() - mPinnedHeaderView.getHeight();
            } else {
                mPinnedHeaderTop = 0;
            }

            mClipBounds = c.getClipBounds();
//            mClipBounds.top = mPinnedHeaderTop + mPinnedHeaderView.getHeight();
            c.clipRect(mClipBounds);
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mPinnedHeaderView != null) {
            c.save();

            mClipBounds.top = 0;
            c.clipRect(mClipBounds, Region.Op.INTERSECT);
            if (parent.getContext().getResources().getConfiguration().getLayoutDirection() ==
                    LayoutDirection.RTL) {
                int trans = parent.getWidth() - mPinnedHeaderView.getWidth();
                c.translate(trans, mPinnedHeaderTop);
            } else {
                c.translate(0, mPinnedHeaderTop);
            }
            mPinnedHeaderView.draw(c);

            c.restore();
        }
    }

    private void createPinnedHeader(RecyclerView parent) {
        checkCache(parent);

        // get LinearLayoutManager.
        final LinearLayoutManager linearLayoutManager;
        final RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            linearLayoutManager = (LinearLayoutManager) layoutManager;
        } else {
            return;
        }

        final int firstVisiblePosition = linearLayoutManager.findFirstVisibleItemPosition();
        final int headerPosition = findPinnedHeaderPosition(firstVisiblePosition);

        if (headerPosition >= 0 && mHeaderPosition != headerPosition) {
            mHeaderPosition = headerPosition;
            final int viewType = mAdapter.getItemViewType(headerPosition);

            final RecyclerView.ViewHolder pinnedViewHolder = mAdapter.createViewHolder(parent,
                    viewType);
            mAdapter.bindViewHolder(pinnedViewHolder, headerPosition);
            mPinnedHeaderView = pinnedViewHolder.itemView;

            // read layout parameters
            ViewGroup.LayoutParams layoutParams = mPinnedHeaderView.getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup
                                .LayoutParams.WRAP_CONTENT);
                mPinnedHeaderView.setLayoutParams(layoutParams);
            }

            int heightMode = View.MeasureSpec.getMode(layoutParams.height);
            int heightSize = View.MeasureSpec.getSize(layoutParams.height);

            if (heightMode == View.MeasureSpec.UNSPECIFIED) {
                heightMode = View.MeasureSpec.EXACTLY;
            }

            final int maxHeight = parent.getHeight() - parent.getPaddingTop() - parent
                    .getPaddingBottom();
            if (heightSize > maxHeight) {
                heightSize = maxHeight;
            }

            // measure & layout
            final int ws = View.MeasureSpec.makeMeasureSpec(parent.getWidth() - parent
                    .getPaddingLeft() - parent.getPaddingRight(), View.MeasureSpec.UNSPECIFIED);
            final int hs = View.MeasureSpec.makeMeasureSpec(heightSize, heightMode);
            mPinnedHeaderView.measure(ws, hs);

            mPinnedHeaderView.layout(0, 0, mPinnedHeaderView.getMeasuredWidth(),
                    mPinnedHeaderView.getMeasuredHeight());


        }
    }

    private int findPinnedHeaderPosition(int fromPosition) {
        if (fromPosition > mAdapter.getItemCount()) {
            return -1;
        }

        for (int position = fromPosition; position >= 0; position--) {
            if (mAdapter != null) {
                final int viewType = mAdapter.getItemViewType(position);
                if (isPinnedViewType(viewType)) {
                    return position;
                }
            }


        }

        return -1;
    }

    private boolean isPinnedViewType(int viewType) {
        if (!mPinnedViewTypes.containsKey(viewType)) {
            mPinnedViewTypes.put(viewType, ((PinnedHeaderAdapter) mAdapter).isPinnedViewType
                    (viewType));
        }

        return mPinnedViewTypes.get(viewType);
    }

    private boolean isHeaderView(RecyclerView parent, View v) {
        final int position = parent.getChildPosition(v);
        if (position == RecyclerView.NO_POSITION) {
            return false;
        }
        final int viewType = mAdapter.getItemViewType(position);

        return isPinnedViewType(viewType);
    }

    private void checkCache(RecyclerView parent) {
        RecyclerView.Adapter adapter = parent.getAdapter();
        if (mAdapter != adapter) {
            disableCache();
            if (adapter instanceof PinnedHeaderAdapter) {
                mAdapter = adapter;
            } else {
                mAdapter = null;
            }
        }
    }

    public void disableCache() {
        mPinnedHeaderView = null;
        mHeaderPosition = -1;
        mPinnedViewTypes.clear();

    }

    public interface PinnedHeaderAdapter {
        boolean isPinnedViewType(int viewType);
    }

}