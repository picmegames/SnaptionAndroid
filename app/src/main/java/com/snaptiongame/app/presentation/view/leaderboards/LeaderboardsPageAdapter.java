package com.snaptiongame.app.presentation.view.leaderboards;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * @author Tyler Wong
 */

public class LeaderboardsPageAdapter extends FragmentPagerAdapter {

    private Fragment mCurrentFragment;

    private static final int PAGE_COUNT = 2;
    public static final int EXPERIENCE_PAGE = 0;
    public static final int RANK_PAGE = 1;
    private static final String tabTitles[] = new String[]{"Experience", "Rank"};

    public LeaderboardsPageAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case EXPERIENCE_PAGE:
                mCurrentFragment = LeaderboardListFragment.getInstance(EXPERIENCE_PAGE);
                break;

            case RANK_PAGE:
                mCurrentFragment = LeaderboardListFragment.getInstance(RANK_PAGE);
                break;
        }
        return mCurrentFragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
