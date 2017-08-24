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

public class LeaderboardsFragment extends Fragment {

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    private Unbinder unbinder;

    public static final String TAG = LeaderboardsFragment.class.getSimpleName();

    public static LeaderboardsFragment getInstance() {
        return new LeaderboardsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.leaderboards_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        viewPager.setAdapter(new LeaderboardsPageAdapter(getActivity().getSupportFragmentManager()));

        tabLayout.setupWithViewPager(viewPager);
        int white = ContextCompat.getColor(getContext(), android.R.color.white);
        tabLayout.setTabTextColors(white, white);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
