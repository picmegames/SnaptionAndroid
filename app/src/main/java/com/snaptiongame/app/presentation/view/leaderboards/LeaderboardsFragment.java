package com.snaptiongame.app.presentation.view.leaderboards;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.snaptiongame.app.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Tyler Wong
 */

public class LeaderboardsFragment extends Fragment implements LeaderboardsContract.View {

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    private LeaderboardsContract.Presenter mPresenter;
    private Unbinder mUnbinder;

    public static final String TAG = LeaderboardsFragment.class.getSimpleName();

    public static LeaderboardsFragment getInstance() {
        return new LeaderboardsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.leaderboards_fragment, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        mViewPager.setAdapter(new LeaderboardsPageAdapter(getActivity().getSupportFragmentManager()));

        mTabLayout.setupWithViewPager(mViewPager);
        int white = ContextCompat.getColor(getContext(), android.R.color.white);
        mTabLayout.setTabTextColors(white, white);

        return view;
    }

    @Override
    public void setPresenter(LeaderboardsContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
