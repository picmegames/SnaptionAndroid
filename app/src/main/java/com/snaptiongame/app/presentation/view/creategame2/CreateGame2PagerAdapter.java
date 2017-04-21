package com.snaptiongame.app.presentation.view.creategame2;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * @author Tyler Wong
 */

public class CreateGame2PagerAdapter extends FragmentStatePagerAdapter {

    private Fragment mCurrentFragment;

    public static final int PHOTO_PAGE = 0;
    public static final int TAG_PAGE = 1;
    public static final int FRIENDS_PAGE = 2;
    public static final int DATE_PAGE = 3;
    public static final int REVIEW_PAGE = 4;
    public static final int NUM_PAGES = 5;

    public CreateGame2PagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case PHOTO_PAGE:
                mCurrentFragment = CreateGamePhotoFragment.newInstance();
                break;
            case TAG_PAGE:
                mCurrentFragment = CreateGamePhotoFragment.newInstance();
                break;
            case FRIENDS_PAGE:
                mCurrentFragment = CreateGamePhotoFragment.newInstance();
                break;
            case DATE_PAGE:
                mCurrentFragment = CreateGamePhotoFragment.newInstance();
                break;
            case REVIEW_PAGE:
                mCurrentFragment = CreateGamePhotoFragment.newInstance();
                break;
        }
        return mCurrentFragment;
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }
}
