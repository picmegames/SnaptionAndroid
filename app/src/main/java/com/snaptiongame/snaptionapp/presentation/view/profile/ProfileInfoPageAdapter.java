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

   private Context context;

   private static final int PAGE_COUNT = 2;
   private static final String tabTitles[] = new String[] {"History", "More Info"};

   public ProfileInfoPageAdapter(FragmentManager manager, Context context) {
      super(manager);
      this.context = context;
   }

   @Override
   public int getCount() {
      return PAGE_COUNT;
   }

   @Override
   public Fragment getItem(int position) {
      return WallFragment.newInstance(position);
   }

   @Override
   public CharSequence getPageTitle(int position) {
      // Generate title based on item position
      return tabTitles[position];
   }
}
