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
    RecyclerView friendsList;
    @BindView(R.id.refresh_layout_friends)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.empty_view)
    LinearLayout emptyView;

    protected FriendsContract.Presenter presenter;
    private Unbinder unbinder;
    private InfiniteRecyclerViewScrollListener scrollListener;
    private FriendsAdapter adapter;
    private InsetDividerDecoration decoration;

    public static final String TAG = FriendsFragment.class.getSimpleName();

    public static FriendsFragment getInstance() {
        return new FriendsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.friends_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        presenter = new FriendsPresenter(this);

        decoration = new InsetDividerDecoration(
                FriendViewHolder.class,
                getResources().getDimensionPixelSize(R.dimen.divider_height),
                getResources().getDimensionPixelSize(R.dimen.keyline_1),
                ContextCompat.getColor(getContext(), R.color.divider));
        friendsList.addItemDecoration(decoration);

        friendsList.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        friendsList.setLayoutManager(layoutManager);
        adapter = new FriendsAdapter(new ArrayList<>());
        adapter.setPresenter(presenter);
        adapter.setShouldDisplayAddRemoveOption(true);

        scrollListener = new InfiniteRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                presenter.loadFriends(page);
            }
        };
        friendsList.addOnScrollListener(scrollListener);

        friendsList.setAdapter(adapter);

        refreshLayout.setOnRefreshListener(this::refreshFriends);

        refreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getContext(), R.color.colorAccent)
        );

        presenter.subscribe();

        return view;
    }

    public void refreshFriends() {
        adapter.clear();
        scrollListener.resetState();
        presenter.subscribe();
    }

    @Override
    public void showEmptyView() {
        emptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showFriendList() {
        emptyView.setVisibility(View.GONE);
        friendsList.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.unsubscribe();
    }

    @Override
    public void setRefreshing(boolean isRefreshing) {
        refreshLayout.setRefreshing(isRefreshing);
    }

    @Override
    public void processFriends(List<Friend> friends) {
        adapter.addFriends(friends);
        setRefreshing(false);

        if (!adapter.isEmpty()) {
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
        this.presenter = presenter;
    }
}
