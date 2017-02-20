package com.snaptiongame.snaptionapp.presentation.view.profile;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.snaptiongame.snaptionapp.presentation.view.wall.WallFragment;

/**
 * @author Tyler Wong
 */

public class ProfileInfoPageAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private FragmentManager mManager;
    private Fragment mCurrentFragment;

    private String fragTag;

    private static final int PAGE_COUNT = 2;
    private static final int HISTORY_PAGE = 0;
    private static final int MORE_INFO_PAGE = 1;
    private static final String tabTitles[] = new String[]{"History", "More Info"};

    public ProfileInfoPageAdapter(FragmentManager manager, Context context) {
        super(manager);
        mManager = manager;
        mContext = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case HISTORY_PAGE:
                fragTag = WallFragment.class.getSimpleName();
                mCurrentFragment = mManager.findFragmentByTag(fragTag);
                if (mCurrentFragment == null) {
                    mCurrentFragment = WallFragment.getInstance();
                }
                break;

            case MORE_INFO_PAGE:
//            fragTag = MoreInfoFragment.class.getSimpleName();
//            mCurrentFragment = mManager.findFragmentByTag(fragTag);
//            if (mCurrentFragment == null) {
//               mCurrentFragment = MoreInfoFragment.getInstance();
//            }
                fragTag = WallFragment.class.getSimpleName();
                mCurrentFragment = mManager.findFragmentByTag(fragTag);
                if (mCurrentFragment == null) {
                    mCurrentFragment = WallFragment.getInstance();
                }
                break;
        }
        return mCurrentFragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
