package com.snaptiongame.app.presentation.view.listeners;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * @author Tyler Wong
 */

public abstract class InfiniteRecyclerViewScrollListener extends RecyclerView.OnScrollListener {
    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 5;
    // The current offset index of data you have loaded
    private int currentPage = START_PAGE;
    // The total number of items in the dataset after the last load
    private int previousTotalItemCount = 0;
    // True if we are still waiting for the last set of data to load.
    private boolean loading = true;

    // Sets the starting page index
    private static final int START_PAGE = 1;

    private RecyclerView.LayoutManager layoutManager;

    public InfiniteRecyclerViewScrollListener(RecyclerView.LayoutManager layoutManager) {
        this.layoutManager = layoutManager;

        if (layoutManager instanceof StaggeredGridLayoutManager) {
            visibleThreshold *= ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        }
    }

    public int getLastVisibleItem(int[] lastVisibleItemPositions) {
        int maxSize = 0;
        for (int index = 0; index < lastVisibleItemPositions.length; index++) {
            if (index == 0) {
                maxSize = lastVisibleItemPositions[index];
            }
            else if (lastVisibleItemPositions[index] > maxSize) {
                maxSize = lastVisibleItemPositions[index];
            }
        }
        return maxSize;
    }

    // This happens many times a second during a scroll, so be wary of the code you place here.
    // We are given a few useful parameters to help us work out if we need to load some more data,
    // but first we check if we are waiting for the previous load to finish.
    @Override
    public void onScrolled(RecyclerView view, int dx, int dy) {
        if (dy > 0) {
            int lastVisibleItemPosition = 0;
            int totalItemCount = layoutManager.getItemCount();

            if (layoutManager instanceof StaggeredGridLayoutManager) {
                int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(null);
                // get maximum element within the list
                lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions);
            }
            else if (layoutManager instanceof LinearLayoutManager) {
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            }

            // If the total item count is zero and the previous isn't, assume the
            // list is invalidated and should be reset back to initial state
            if (totalItemCount < previousTotalItemCount) {
                currentPage = START_PAGE;
                previousTotalItemCount = totalItemCount;
                if (totalItemCount == 0) {
                    loading = true;
                }
            }

            // If it’s still loading, we check to see if the dataset count has
            // changed, if so we conclude it has finished loading and update the current page
            // number and total item count.
            if (loading && totalItemCount > previousTotalItemCount) {
                loading = false;
                previousTotalItemCount = totalItemCount;
            }

            // If it isn’t currently loading, we check to see if we have breached
            // the visibleThreshold and need to reload more data.
            // If we do need to reload some more data, we execute onLoadMore to fetch the data.
            // threshold should reflect how many total columns there are too
            if (!loading && lastVisibleItemPosition + visibleThreshold > totalItemCount) {
                currentPage++;
                onLoadMore(currentPage, totalItemCount, view);
                loading = true;
            }
        }
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    // Call this method whenever performing new searches
    public void resetState() {
        currentPage = START_PAGE;
        previousTotalItemCount = 0;
        loading = true;
    }

    // Defines the process for actually loading more data based on page
    public abstract void onLoadMore(int page, int totalItemsCount, RecyclerView view);
}
