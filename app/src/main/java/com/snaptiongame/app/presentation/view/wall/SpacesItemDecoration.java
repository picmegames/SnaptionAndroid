package com.snaptiongame.app.presentation.view.wall;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * @author Tyler Wong
 */

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private final int mSpace;

    private static final int RIGHT = 1;

    public SpacesItemDecoration(int space) {
        this.mSpace = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int spanIndex = ((StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams()).getSpanIndex();

        if (spanIndex == RIGHT) {
            outRect.left = mSpace / 2;
            outRect.right = mSpace;
        }
        else {
            outRect.left = mSpace;
            outRect.right = mSpace / 2;
        }

        outRect.bottom = mSpace * 2;
        outRect.top = mSpace;
    }
}
