package com.snaptiongame.app.presentation.view.friends;

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
import com.snaptiongame.app.data.models.Friend;
import com.snaptiongame.app.presentation.view.decorations.InsetDividerDecoration;
import com.snaptiongame.app.presentation.view.listeners.InfiniteRecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Brian Gouldsberry
 */

public class FriendsFragment extends Fragment implements FriendsContract.View {
    @BindView(R.id.friend_list)
    RecyclerView mFriendsList;
    @BindView(R.id.refresh_layout_friends)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.empty_view)
    LinearLayout mEmptyView;

    protected FriendsContract.Presenter mPresenter;
    private InfiniteRecyclerViewScrollListener mScrollListener;

    private FriendsAdapter mAdapter;
    private InsetDividerDecoration mDecoration;

    private Unbinder mUnbinder;
    public static final String TAG = FriendsFragment.class.getSimpleName();

    public static FriendsFragment getInstance() {
        return new FriendsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.friends_fragment, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        mPresenter = new FriendsPresenter(this);

        mDecoration = new InsetDividerDecoration(
                FriendViewHolder.class,
                getResources().getDimensionPixelSize(R.dimen.divider_height),
                getResources().getDimensionPixelSize(R.dimen.keyline_1),
                ContextCompat.getColor(getContext(), R.color.divider));
        mFriendsList.addItemDecoration(mDecoration);

        mFriendsList.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mFriendsList.setLayoutManager(layoutManager);
        mAdapter = new FriendsAdapter(new ArrayList<>());
        mAdapter.setPresenter(mPresenter);
        mAdapter.setShouldDisplayAddRemoveOption(true);

        mScrollListener = new InfiniteRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                mPresenter.loadFriends(page);
            }
        };
        mFriendsList.addOnScrollListener(mScrollListener);

        mFriendsList.setAdapter(mAdapter);

        mRefreshLayout.setOnRefreshListener(() -> {
            setRefreshing(true);
            mAdapter.clear();
            mScrollListener.resetState();
            mPresenter.loadFriends(1);
        });
        mRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getContext(), R.color.colorAccent)
        );

        mPresenter.subscribe();

        return view;
    }

    public void refreshFriends() {
        mAdapter.clear();
        mScrollListener.resetState();
        mPresenter.subscribe();
    }

    @Override
    public void showEmptyView() {
        mEmptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showFriendList() {
        mEmptyView.setVisibility(View.GONE);
        mFriendsList.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
        mPresenter.unsubscribe();
    }

    @Override
    public void setRefreshing(boolean isRefreshing) {
        mRefreshLayout.setRefreshing(isRefreshing);
    }

    @Override
    public void processFriends(List<Friend> friends) {
        mAdapter.addFriends(friends);
        setRefreshing(false);

        if (!mAdapter.isEmpty()) {
            showFriendList();
        }
        else {
            showEmptyView();
        }
    }

    @Override
    public void addFriend(Friend friend) {
    }

    @Override
    public void setPresenter(FriendsContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
