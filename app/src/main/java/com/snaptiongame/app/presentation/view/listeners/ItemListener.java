package com.snaptiongame.app.presentation.view.listeners;

import android.support.v7.widget.RecyclerView;

/**
 * @author Brian Gouldsberry
 */

public interface ItemListener {
    void updateUpvote(boolean value, int index);

    void updateFlag(int index, RecyclerView.ViewHolder holder);
}
