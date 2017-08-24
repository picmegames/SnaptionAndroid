package com.snaptiongame.app.presentation.view.decorations;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * @author Tyler Wong
 */

public class WallSpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;
    private boolean isList;

    private static final int RIGHT = 1;

    public WallSpacesItemDecoration(int space, boolean isList) {
        this.space = space;
        this.isList = isList;
    }

    public void setIsList(boolean isList) {
        this.isList = isList;
    }

    public void setSpacing(int spacing) {
        this.space = spacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (!isList) {
            int spanIndex = ((StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams()).getSpanIndex();

            if (spanIndex == RIGHT) {
                outRect.left = space / 2;
                outRect.right = space;
            }
            else {
                outRect.left = space;
                outRect.right = space / 2;
            }
            outRect.bottom = space * 4;
            outRect.top = space;
        }
        else {
            outRect.bottom = space * 2;
        }
    }
}
