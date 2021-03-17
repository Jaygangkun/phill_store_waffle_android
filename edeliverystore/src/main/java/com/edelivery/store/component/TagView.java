package com.edelivery.store.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.edelivery.store.utils.Utilities;
import com.elluminati.edelivery.store.R;

import java.util.ArrayList;
import java.util.List;

public class TagView extends RelativeLayout {
//use dp and sp, not px

    public static final String TAG = "TagView";
    //----------------- separator TagView-----------------//
    public static final float DEFAULT_LINE_MARGIN = 5;
    public static final float DEFAULT_TAG_MARGIN = 5;
    public static final float DEFAULT_TAG_TEXT_PADDING_LEFT = 8;
    public static final float DEFAULT_TAG_TEXT_PADDING_TOP = 5;
    public static final float DEFAULT_TAG_TEXT_PADDING_RIGHT = 8;
    public static final float DEFAULT_TAG_TEXT_PADDING_BOTTOM = 5;
    public static final float LAYOUT_WIDTH_OFFSET = 40;
    //----------------- separator Tag Item-----------------//
    public static final float DEFAULT_TAG_TEXT_SIZE = 14f;
    public static final float DEFAULT_TAG_DELETE_INDICATOR_SIZE = 14f;
    public static final float DEFAULT_TAG_LAYOUT_BORDER_SIZE = 0f;
    public static final float DEFAULT_TAG_RADIUS = 100;
    public static final int DEFAULT_TAG_LAYOUT_COLOR = Color.parseColor("#AED374");
    public static final int DEFAULT_TAG_LAYOUT_COLOR_PRESS = Color.parseColor("#88363636");
    public static final int DEFAULT_TAG_TEXT_COLOR = Color.parseColor("#ffffff");
    public static final int DEFAULT_TAG_DELETE_INDICATOR_COLOR = Color.parseColor("#ffffff");
    public static final int DEFAULT_TAG_LAYOUT_BORDER_COLOR = Color.parseColor("#ffffff");
    public static final String DEFAULT_TAG_DELETE_ICON = "×";
    public static final boolean DEFAULT_TAG_IS_DELETABLE = false;
    private Context context;
    /**
     * tag list
     */
    private List<String> mTags = new ArrayList<>();

    /**
     * System Service
     */
    private LayoutInflater mInflater;
    private ViewTreeObserver mViewTreeObserber;

    /**
     * listeners
     */
    private OnTagClickListener mClickListener;


    /**
     * view size param
     */
    private int mWidth;

    /**
     * layout initialize flag
     */
    private boolean mInitialized = false;

    /**
     * custom layout param
     */
    private int lineMargin;
    private int tagMargin;
    private int textPaddingLeft;
    private int textPaddingRight;
    private int textPaddingTop;
    private int textPaddingBottom;


    /**
     * constructor
     *
     * @param ctx
     */
    public TagView(Context ctx) {
        super(ctx, null);
        context = ctx;
        mWidth = getMaxWidth(context);
        initialize(ctx, null, 0);
    }

    /**
     * constructor
     *
     * @param ctx
     * @param attrs
     */
    public TagView(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        context = ctx;
        mWidth = getMaxWidth(context);
        initialize(ctx, attrs, 0);
    }

    /**
     * constructor
     *
     * @param ctx
     * @param attrs
     * @param defStyle
     */
    public TagView(Context ctx, AttributeSet attrs, int defStyle) {
        super(ctx, attrs, defStyle);
        context = ctx;
        mWidth = getMaxWidth(context);
        initialize(ctx, attrs, defStyle);
    }

    /**
     * initalize instance
     *
     * @param ctx
     * @param attrs
     * @param defStyle
     */
    private void initialize(Context ctx, AttributeSet attrs, int defStyle) {
        mInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewTreeObserber = getViewTreeObserver();
        mViewTreeObserber.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (!mInitialized) {
                    mInitialized = true;
                    drawTags();
                }
            }
        });

        // get AttributeSet
        TypedArray typeArray = ctx.obtainStyledAttributes(attrs, R.styleable.TagView, defStyle,
                defStyle);
        this.lineMargin = (int) typeArray.getDimension(R.styleable.TagView_lineMargin, Utilities
                .dipToPx(this.getContext(), DEFAULT_LINE_MARGIN));
        this.tagMargin = (int) typeArray.getDimension(R.styleable.TagView_tagMargin, Utilities
                .dipToPx(this.getContext(), DEFAULT_TAG_MARGIN));
        this.textPaddingLeft = (int) typeArray.getDimension(R.styleable.TagView_textPaddingLeft,
                Utilities.dipToPx(this.getContext(), DEFAULT_TAG_TEXT_PADDING_LEFT));
        this.textPaddingRight = (int) typeArray.getDimension(R.styleable
                .TagView_textPaddingRight, Utilities.dipToPx(this.getContext(),
                DEFAULT_TAG_TEXT_PADDING_RIGHT));
        this.textPaddingTop = (int) typeArray.getDimension(R.styleable.TagView_textPaddingTop,
                Utilities.dipToPx(this.getContext(), DEFAULT_TAG_TEXT_PADDING_TOP));
        this.textPaddingBottom = (int) typeArray.getDimension(R.styleable
                .TagView_textPaddingBottom, Utilities.dipToPx(this.getContext(),
                DEFAULT_TAG_TEXT_PADDING_BOTTOM));
        typeArray.recycle();
    }

    /**
     * onSizeChanged
     *
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawTags();
    }

    /**
     * tag draw
     */
    private void drawTags() {

        if (!mInitialized) {
            return;
        }

        // clear all tag
        removeAllViews();

        // layout padding left & layout padding right
        float total = getPaddingLeft() + getPaddingRight();

        int listIndex = 1;// List Index
        int indexBottom = 1;// The Tag to add below
        int indexHeader = 1;// The header tag of this line
        String tagPre = null;
        for (String item : mTags) {
            final int position = listIndex - 1;
            final String tag = item;

            // inflate tag layout
            View tagLayout = mInflater.inflate(R.layout.item_tag, null);
            tagLayout.setId(listIndex);


            // tag text
            TextView tagView = (TextView) tagLayout.findViewById(R.id.tv_tag_item_contain);
            tagView.setText(tag);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tagView
                    .getLayoutParams();
            params.setMargins(textPaddingLeft, textPaddingTop, textPaddingRight, textPaddingBottom);
            tagView.setLayoutParams(params);

            tagLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClickListener != null) {
                        mClickListener.onTagClick(v, position);
                    }
                }
            });

            tagLayout.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    return true;
                }
            });

            // calculate　of tag layout width
            float tagWidth = tagView.getPaint().measureText(tag) + textPaddingLeft +
                    textPaddingRight;
            // tagView padding (left & right)


            LayoutParams tagParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            //add margin of each line
            tagParams.bottomMargin = lineMargin;
            int partOfWidth = mWidth / 6;
            int offset = partOfWidth > tagWidth ? partOfWidth : (int) tagWidth;

            Log.i(TAG, "Main Width= " + mWidth);
            Log.i(TAG, "Custom Width= " + (mWidth - offset));
            Log.i(TAG, "Total Tag Width= " + (total + tagWidth + Utilities.dipToPx(this
                            .getContext(),
                    LAYOUT_WIDTH_OFFSET)));
            if (mWidth - offset <= total + tagWidth + Utilities.dipToPx(this
                            .getContext(),
                    LAYOUT_WIDTH_OFFSET)) {
                //need to add in new line
                if (tagPre != null) tagParams.addRule(RelativeLayout.BELOW, indexBottom);
                // initialize total param (layout padding left & layout padding right)
                total = getPaddingLeft() + getPaddingRight();
                indexBottom = listIndex;
                indexHeader = listIndex;
            } else {
                //no need to new line
                tagParams.addRule(RelativeLayout.ALIGN_TOP, indexHeader);
                //not header of the line
                if (listIndex != indexHeader) {
                    tagParams.addRule(RelativeLayout.RIGHT_OF, listIndex - 1);
                    tagParams.leftMargin = tagMargin;
                    total += tagMargin;
                    if (tagPre.length() < tag.length()) {
                        indexBottom = listIndex;
                    }
                }


            }
            total += tagWidth;
            addView(tagLayout, tagParams);
            tagPre = tag;
            listIndex++;
        }

    }


    //public methods
    //----------------- separator  -----------------//

    /**
     * @param tag
     */
    public void addTag(String tag) {

        mTags.add(tag);
        drawTags();
    }

    public void addTags(List<String> tags) {
        if (tags == null) return;
        mTags = new ArrayList<>();
        if (tags.isEmpty())
            drawTags();
        for (String item : tags) {
            mTags.add(item);
        }
        drawTags();
    }


    public void addTags(String[] tags) {
        if (tags == null) return;
        for (String item : tags) {
            mTags.add(item);
        }
        drawTags();
    }


    /**
     * get tag list
     *
     * @return mTags TagObject List
     */
    public List<String> getTags() {
        return mTags;
    }

    /**
     * remove tag
     *
     * @param position
     */
    public void remove(int position) {
        if (position < mTags.size()) {
            mTags.remove(position);
            drawTags();
        }
    }

    /**
     * remove all views
     */
    public void removeAll() {
        mTags.clear(); //clear all of tags
        removeAllViews();
    }

    public int getLineMargin() {
        return lineMargin;
    }

    public void setLineMargin(float lineMargin) {
        this.lineMargin = Utilities.dipToPx(getContext(), lineMargin);
    }

    public int getTagMargin() {
        return tagMargin;
    }

    public void setTagMargin(float tagMargin) {
        this.tagMargin = Utilities.dipToPx(getContext(), tagMargin);
    }

    public int getTextPaddingLeft() {
        return textPaddingLeft;
    }

    public void setTextPaddingLeft(float textPaddingLeft) {
        this.textPaddingLeft = Utilities.dipToPx(getContext(), textPaddingLeft);
    }

    public int getTextPaddingRight() {
        return textPaddingRight;
    }

    public void setTextPaddingRight(float textPaddingRight) {
        this.textPaddingRight = Utilities.dipToPx(getContext(), textPaddingRight);
    }

    public int getTextPaddingTop() {
        return textPaddingTop;
    }

    public void setTextPaddingTop(float textPaddingTop) {
        this.textPaddingTop = Utilities.dipToPx(getContext(), textPaddingTop);
    }

    public int gettextPaddingBottom() {
        return textPaddingBottom;
    }

    public void settextPaddingBottom(float textPaddingBottom) {
        this.textPaddingBottom = Utilities.dipToPx(getContext(), textPaddingBottom);
    }

    /**
     * setter for OnTagSelectListener
     *
     * @param clickListener
     */
    public void setOnTagClickListener(OnTagClickListener clickListener) {
        mClickListener = clickListener;
    }

    private int getMaxWidth(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        return width;
    }

    public interface OnTagClickListener {
        void onTagClick(View v, int position);
    }
}