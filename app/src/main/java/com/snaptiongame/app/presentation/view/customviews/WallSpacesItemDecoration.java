package com.snaptiongame.app.presentation.view.customviews;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * @author Tyler Wong
 */

public class WallSpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int mSpace;
    private boolean mIsList;

    private static final int RIGHT = 1;

    public WallSpacesItemDecoration(int space, boolean isList) {
        this.mSpace = space;
        this.mIsList = isList;
    }

    public void setIsList(boolean isList) {
        this.mIsList = isList;
    }

    public void setSpacing(int spacing) {
        this.mSpace = spacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (!mIsList) {
            int spanIndex = ((StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams()).getSpanIndex();

            if (spanIndex == RIGHT) {
                outRect.left = mSpace / 2;
                outRect.right = mSpace;
            }
            else {
                outRect.left = mSpace;
                outRect.right = mSpace / 2;
            }
            outRect.bottom = mSpace * 4;
            outRect.top = mSpace;
        }
        else {
            outRect.bottom = mSpace * 2;
        }
    }
}
