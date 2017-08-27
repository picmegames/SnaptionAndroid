package com.snaptiongame.app.presentation.view.listeners

import android.support.v7.widget.RecyclerView

/**
 * @author Brian Gouldsberry
 */

interface ItemListener {
    fun updateUpvote(value: Boolean, index: Int)

    fun updateFlag(index: Int, holder: RecyclerView.ViewHolder)
}
