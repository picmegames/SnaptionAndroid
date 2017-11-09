package com.snaptiongame.app.presentation.view.leaderboards;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * @author Tyler Wong
 */

public class LeaderboardsPageAdapter extends FragmentPagerAdapter {

    private static final int PAGE_COUNT = 2;
    private static final String tabTitles[] = new String[]{"Friends", "World"};

    public LeaderboardsPageAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        return LeaderboardListFragment.getInstance(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
