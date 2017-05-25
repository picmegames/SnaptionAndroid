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
import com.snaptiongame.app.data.models.Game;
import com.snaptiongame.app.data.models.User;
import com.snaptiongame.app.presentation.view.decorations.InsetDividerDecoration;
import com.snaptiongame.app.presentation.view.listeners.InfiniteRecyclerViewScrollListener;
import com.snaptiongame.app.presentation.view.utils.ActivityFeedUtils;

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

        // TESTING
        List<ActivityFeedItem> testItems = new ArrayList<>();
        User friend1 = new User();
        friend1.imageUrl = "https://img.pokemondb.net/artwork/bulbasaur.jpg";
        friend1.username = "bulbasaur";
        Game game1 = new Game();
        game1.imageUrl = "https://typeset-beta.imgix.net/rehost%2F2016%2F9%2F13%2F39a6deb0-8ff2-4ec5-854b-1b197d81df0a.jpg";
        User friend2 = new User();
        friend2.imageUrl = "http://vignette4.wikia.nocookie.net/pokemon/images/5/5f/025Pikachu_OS_anime_11.png/revision/latest?cb=20150717063951";
        friend2.username = "pikachu";
        Game game2 = new Game();
        game2.imageUrl = "https://vignette2.wikia.nocookie.net/pokemon/images/7/74/Red_Mewtwo_PO.png/revision/latest?cb=20141008193632";
        User friend3 = new User();
        friend3.imageUrl = "https://img.pokemondb.net/artwork/squirtle.jpg";
        friend3.username = "squirtle";
        Game game3 = new Game();
        game3.imageUrl = "http://cdn.bulbagarden.net/upload/thumb/b/b1/Misty_AG.png/250px-Misty_AG.png";
        User friend4 = new User();
        friend4.imageUrl = "http://vignette4.wikia.nocookie.net/pokemon/images/5/5f/025Pikachu_OS_anime_11.png/revision/latest?cb=20150717063951";
        friend4.username = "ash_ketchum";
        friend4.fullName = "Ash Ketchum";
        Game game4 = new Game();
        game4.imageUrl = "https://vignette2.wikia.nocookie.net/pokemon/images/7/74/Red_Mewtwo_PO.png/revision/latest?cb=20141008193632";
        ActivityFeedItem item1 = new ActivityFeedItem(0, 1495702207, ActivityFeedUtils.CAPTIONED_GAME,
                null, friend1, game1, null);
        ActivityFeedItem item2 = new ActivityFeedItem(0, 1495270189, ActivityFeedUtils.FRIENDED_YOU,
                null, friend2, game2, null);
        ActivityFeedItem item3 = new ActivityFeedItem(0, 1495700449, ActivityFeedUtils.FRIEND_MADE_GAME,
                null, friend3, game3, null);
        ActivityFeedItem item4 = new ActivityFeedItem(0, 1495710449, ActivityFeedUtils.NEW_FACEBOOK_FRIEND,
                null, friend4, game4, null);
        ActivityFeedItem item5 = new ActivityFeedItem(0, 1495700449, ActivityFeedUtils.FRIEND_INVITED_GAME,
                null, friend3, game3, null);
        testItems.add(item1);
        testItems.add(item2);
        testItems.add(item3);
        testItems.add(item2);
        testItems.add(item1);
        testItems.add(item2);
        testItems.add(item3);
        testItems.add(item1);
        testItems.add(item4);
        testItems.add(item4);
        testItems.add(item5);
        testItems.add(item5);

        mDecoration = new InsetDividerDecoration(
                ActivityFeedItemViewHolder.class,
                getResources().getDimensionPixelSize(R.dimen.divider_height),
                getResources().getDimensionPixelSize(R.dimen.keyline_1),
                ContextCompat.getColor(getContext(), R.color.divider));
        mActivityFeed.addItemDecoration(mDecoration);

        mAdapter = new ActivityFeedAdapter(testItems);
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
    public void onResume() {
        super.onResume();
        //mPresenter.subscribe();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
        mPresenter.unsubscribe();
    }
}
