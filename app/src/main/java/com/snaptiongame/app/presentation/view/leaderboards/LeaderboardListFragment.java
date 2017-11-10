package com.snaptiongame.app.presentation.view.leaderboards;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.snaptiongame.app.R;
import com.snaptiongame.app.data.models.Friend;
import com.snaptiongame.app.presentation.view.friends.FriendsAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Tyler Wong
 */

public class LeaderboardListFragment extends Fragment implements LeaderboardsContract.View {

    @BindView(R.id.leaderboard)
    RecyclerView leaderboard;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;

    private LeaderboardsContract.Presenter presenter;
    private FriendsAdapter adapter;
    private Unbinder unbinder;

    public static final String TYPE = "type";
    public static final String TAG = LeaderboardListFragment.class.getSimpleName();

    public static LeaderboardListFragment getInstance(int type) {
        Bundle args = new Bundle();
        args.putInt(TYPE, type);
        LeaderboardListFragment leaderboardListFragment = new LeaderboardListFragment();
        leaderboardListFragment.setArguments(args);
        return leaderboardListFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.leaderboard_list_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        presenter = new LeaderboardsPresenter(this, getArguments().getInt(TYPE));
        adapter = new FriendsAdapter(new ArrayList<>());
        leaderboard.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        leaderboard.setLayoutManager(layoutManager);
        leaderboard.setAdapter(adapter);

        refreshLayout.setOnRefreshListener(presenter::loadLeaderboard);

        presenter.subscribe();
        refreshLayout.setRefreshing(true);
        return view;
    }

    @Override
    public void setPresenter(LeaderboardsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setLeaderboard(List<Friend> leaderboard) {
        adapter.addFriends(leaderboard);
    }

    @Override
    public void setRefreshing(boolean isRefreshing) {
        refreshLayout.setRefreshing(isRefreshing);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.unsubscribe();
    }
}
