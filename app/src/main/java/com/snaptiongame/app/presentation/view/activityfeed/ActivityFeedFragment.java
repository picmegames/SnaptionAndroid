package com.snaptiongame.app.presentation.view.activityfeed;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.snaptiongame.app.R;
import com.snaptiongame.app.data.models.ActivityFeedItem;
import com.snaptiongame.app.presentation.view.decorations.InsetDividerDecoration;
import com.snaptiongame.app.presentation.view.listeners.InfiniteRecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Tyler Wong
 */

public class ActivityFeedFragment extends Fragment implements ActivityFeedContract.View {

    @BindView(R.id.activity)
    RecyclerView mActivityFeed;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.empty_view)
    LinearLayout mEmptyView;

    private ActivityFeedContract.Presenter mPresenter;
    private Unbinder mUnbinder;
    private ActivityFeedAdapter mAdapter;
    private InsetDividerDecoration mDecoration;
    private InfiniteRecyclerViewScrollListener mScrollListener;

    public static final String TAG = ActivityFeedFragment.class.getSimpleName();

    public static ActivityFeedFragment getInstance() {
        return new ActivityFeedFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_feed_fragment, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        mPresenter = new ActivityFeedPresenter(this);

        mDecoration = new InsetDividerDecoration(
                ActivityFeedItemViewHolder.class,
                getResources().getDimensionPixelSize(R.dimen.divider_height),
                getResources().getDimensionPixelSize(R.dimen.keyline_1),
                ContextCompat.getColor(getContext(), R.color.divider));
        mActivityFeed.addItemDecoration(mDecoration);

        mAdapter = new ActivityFeedAdapter(new ArrayList<>());
        mActivityFeed.setAdapter(mAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mActivityFeed.setLayoutManager(layoutManager);

        mScrollListener = new InfiniteRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                mPresenter.loadActivityFeed(page);
            }
        };

        mActivityFeed.addOnScrollListener(mScrollListener);

        mRefreshLayout.setOnRefreshListener(() -> {
            setRefreshing(true);
            mAdapter.clear();
            mScrollListener.resetState();
            mPresenter.loadActivityFeed(1);
        });

        mRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getContext(), R.color.colorAccent)
        );

        mPresenter.subscribe();

        return view;
    }

    @Override
    public void setRefreshing(boolean isRefreshing) {
        mRefreshLayout.setRefreshing(isRefreshing);
    }

    @Override
    public void setPresenter(ActivityFeedContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showEmptyView() {
        mEmptyView.setVisibility(View.VISIBLE);
        mAdapter.clear();
    }

    @Override
    public void showActivityFeed() {
        mEmptyView.setVisibility(View.GONE);
        mActivityFeed.setVisibility(View.VISIBLE);
    }

    @Override
    public void addActivityFeedItems(List<ActivityFeedItem> items) {
        mAdapter.addActivityItems(items);

        if (!mAdapter.isEmpty()) {
            showActivityFeed();
        }
        else {
            showEmptyView();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
        mPresenter.unsubscribe();
    }
}
