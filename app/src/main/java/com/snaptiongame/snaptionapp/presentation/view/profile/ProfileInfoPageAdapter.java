package com.snaptiongame.snaptionapp.presentation.view.profile;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.snaptiongame.snaptionapp.presentation.view.wall.WallFragment;

/**
 * @author Tyler Wong
 */

public class ProfileInfoPageAdapter extends FragmentPagerAdapter {

    private Fragment mCurrentFragment;

    private static final int PAGE_COUNT = 2;
    private static final int HISTORY_PAGE = 0;
    private static final int MORE_INFO_PAGE = 1;
    private static final String tabTitles[] = new String[]{"History", "More Info"};

    public ProfileInfoPageAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case HISTORY_PAGE:
                mCurrentFragment = WallFragment.getInstance(true);
                break;

            case MORE_INFO_PAGE:
                mCurrentFragment = MoreInfoFragment.getInstance();
                break;
        }
        return mCurrentFragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
