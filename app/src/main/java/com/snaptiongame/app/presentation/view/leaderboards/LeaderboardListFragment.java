package com.snaptiongame.app.presentation.view.leaderboards;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.snaptiongame.app.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Tyler Wong
 */

public class LeaderboardListFragment extends Fragment implements LeaderboardsContract.View {

    private LeaderboardsContract.Presenter presenter;
    private Unbinder unbinder;

    private int type;

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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.leaderboard_list_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        presenter = new LeaderboardsPresenter(this, getArguments().getInt(TYPE));

        presenter.subscribe();

        return view;
    }

    @Override
    public void setPresenter(LeaderboardsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.unsubscribe();
    }
}
