package com.edelivery.store.utils;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * 13/8/16.
 */
public class RecyclerOnItemListener implements RecyclerView.OnItemTouchListener {


    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    private OnItemClickListener onItemClickListener;
    private GestureDetector gestureDetector;

    public RecyclerOnItemListener(Context context, OnItemClickListener onItemClickListener1){
        onItemClickListener = onItemClickListener1;
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View v = rv.findChildViewUnder(e.getX(), e.getY());
        if(v != null && onItemClickListener != null && gestureDetector.onTouchEvent(e)){
            onItemClickListener.onItemClick(v, rv.getChildAdapterPosition(v));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
