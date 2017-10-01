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
    RecyclerView activityFeed;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.empty_view)
    LinearLayout emptyView;

    private ActivityFeedContract.Presenter presenter;
    private Unbinder unbinder;
    private ActivityFeedAdapter adapter;
    private InfiniteRecyclerViewScrollListener scrollListener;

    public static final String TAG = ActivityFeedFragment.class.getSimpleName();

    public static ActivityFeedFragment getInstance() {
        return new ActivityFeedFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_feed_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        presenter = new ActivityFeedPresenter(this);

        InsetDividerDecoration decoration = new InsetDividerDecoration(
                ActivityFeedItemViewHolder.class,
                getResources().getDimensionPixelSize(R.dimen.divider_height),
                getResources().getDimensionPixelSize(R.dimen.keyline_1),
                ContextCompat.getColor(getContext(), R.color.divider));
        activityFeed.addItemDecoration(decoration);

        adapter = new ActivityFeedAdapter(new ArrayList<>());
        activityFeed.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        activityFeed.setLayoutManager(layoutManager);

        scrollListener = new InfiniteRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                presenter.loadActivityFeed(page);
            }
        };

        activityFeed.addOnScrollListener(scrollListener);
        refreshLayout.setOnRefreshListener(this::refreshActivityFeed);

        refreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getContext(), R.color.colorAccent)
        );

        presenter.subscribe();

        return view;
    }

    @Override
    public void setRefreshing(boolean isRefreshing) {
        refreshLayout.setRefreshing(isRefreshing);
    }

    @Override
    public void setPresenter(ActivityFeedContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showEmptyView() {
        emptyView.setVisibility(View.VISIBLE);
        adapter.clear();
    }

    @Override
    public void showActivityFeed() {
        emptyView.setVisibility(View.GONE);
        activityFeed.setVisibility(View.VISIBLE);
    }

    @Override
    public void addActivityFeedItems(List<ActivityFeedItem> items) {
        adapter.addActivityItems(items);

        if (!adapter.isEmpty()) {
            showActivityFeed();
        }
        else {
            showEmptyView();
        }
    }

    public void refreshActivityFeed() {
        adapter.clear();
        scrollListener.resetState();
        presenter.subscribe();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.unsubscribe();
    }
}
