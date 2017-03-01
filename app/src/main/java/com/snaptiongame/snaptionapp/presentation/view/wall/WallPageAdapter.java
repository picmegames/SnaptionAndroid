package com.snaptiongame.snaptionapp.presentation.view.wall;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * @author Tyler Wong
 */

public class WallPageAdapter extends FragmentPagerAdapter {

    private Fragment mCurrentFragment;

    private static final int PAGE_COUNT = 3;
    private static final int MY_WALL = 0;
    private static final int DISCOVER = 1;
    private static final int POPULAR = 2;

    public WallPageAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case MY_WALL:
                mCurrentFragment = WallFragment.getInstance(false);
                break;

            case DISCOVER:
                mCurrentFragment = WallFragment.getInstance(true);
                break;

            case POPULAR:
                mCurrentFragment = WallFragment.getInstance(false);
        }
        return mCurrentFragment;
    }
}
