package com.snaptiongame.snaptionapp.presentation.view.friends;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;

/**
 * Created by BrianGouldsberry on 2/12/17.
 */

public class FriendsTouchListener implements RecyclerView.OnItemTouchListener {
    private GestureDetector mGestureDetector;
    private FriendsAdapter mAdapter;

    public FriendsTouchListener(Context context, FriendsAdapter adapter) {
        mAdapter = adapter;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) { }
        });
    }

    @Override public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        View childView = view.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && mGestureDetector.onTouchEvent(e)) {
            onItemClick(childView, view.getChildAdapterPosition(childView));
            return true;
        }
        return false;
    }

    @Override public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) { }

    @Override
    public void onRequestDisallowInterceptTouchEvent (boolean disallowIntercept){}

    private void onItemClick(View view, int position) {
        if (mAdapter.isSelected(position)) {
            view.setAlpha(FriendsAdapter.DIM);
            mAdapter.deselectFriend(position);
        } else {
            view.setAlpha(FriendsAdapter.BRIGHT);
            mAdapter.selectFriend(position);
        }
    }
}
