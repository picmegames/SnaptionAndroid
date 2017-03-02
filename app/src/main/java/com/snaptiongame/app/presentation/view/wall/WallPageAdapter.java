package com.snaptiongame.app.presentation.view.wall;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

/**
 * @author Tyler Wong
 */

public class WallPageAdapter extends FragmentPagerAdapter {

    private ActionBar mActionBar;

    private Fragment mCurrentFragment;

    private static final int PAGE_COUNT = 3;
    private static final int MY_WALL = 0;
    private static final int DISCOVER = 1;
    private static final int POPULAR = 2;

    public WallPageAdapter(AppCompatActivity activity, FragmentManager manager) {
        super(manager);
        mActionBar = activity.getSupportActionBar();
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case MY_WALL:
                mCurrentFragment = WallFragment.getInstance(true);
                break;

            case DISCOVER:
                mCurrentFragment = WallFragment.getInstance(false);
                break;

            case POPULAR:
                mCurrentFragment = WallFragment.getInstance(false);
                break;
        }
        return mCurrentFragment;
    }


}
