package com.snaptiongame.app.presentation.view.listeners;

import android.support.v7.widget.RecyclerView;

/**
 * Created by BrianGouldsberry on 4/13/17.
 */

public interface ItemListener {
    void updateUpvote(boolean value, int index);

    void updateFlag(int index, RecyclerView.ViewHolder holder);
}
