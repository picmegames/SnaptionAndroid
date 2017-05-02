package com.snaptiongame.app.presentation.view.profile;

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

import com.snaptiongame.app.R;
import com.snaptiongame.app.data.providers.FriendProvider;
import com.snaptiongame.app.presentation.view.friends.FriendsAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

/**
 * @author Tyler Wong
 */

public class MoreInfoFragment extends Fragment {
    @BindView(R.id.follower_list)
    RecyclerView mFollowerList;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;

    private Unbinder mUnbinder;
    private FriendsAdapter mAdapter;
    private int mUserId;

    public static final String USER_ID = "userId";

    public static MoreInfoFragment getInstance(int userId) {
        Bundle args = new Bundle();
        args.putInt(USER_ID, userId);
        MoreInfoFragment moreFragment = new MoreInfoFragment();
        moreFragment.setArguments(args);
        return moreFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.more_info_fragment, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        mAdapter = new FriendsAdapter(new ArrayList<>());
        mFollowerList.setAdapter(mAdapter);
        mFollowerList.setLayoutManager(new LinearLayoutManager(getContext()));

        mRefreshLayout.setOnRefreshListener(this::loadFollowers);
        mRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getContext(), R.color.colorAccent)
        );

        mUserId = getArguments().getInt(USER_ID);
        return view;
    }

    // I know this is bad. It'll be removed anyways once we get user stats
    private void loadFollowers() {
        FriendProvider.getFollowers()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mAdapter::setFriends, Timber::e, () -> mRefreshLayout.setRefreshing(false));
    }

    @Override
    public void onResume() {
        super.onResume();
        loadFollowers();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
