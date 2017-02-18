package com.snaptiongame.snaptionapp.presentation.view.wall;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * @author Tyler Wong
 */

public class WallPageAdapter extends FragmentPagerAdapter {

   private Context mContext;
   private FragmentManager mManager;
   private Fragment mCurrentFragment;

   private String fragTag;

   private static final int PAGE_COUNT = 2;
   private static final int PUBLIC = 0;
   private static final int PRIVATE = 1;
   private static final String tabTitles[] = new String[] {"Public", "Private"};

   public WallPageAdapter(FragmentManager manager, Context context) {
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
         case PUBLIC:
            fragTag = WallFragment.class.getSimpleName();
            mCurrentFragment = mManager.findFragmentByTag(fragTag);
            if (mCurrentFragment == null) {
               mCurrentFragment = WallFragment.getInstance();
            }
            break;

         case PRIVATE:
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
