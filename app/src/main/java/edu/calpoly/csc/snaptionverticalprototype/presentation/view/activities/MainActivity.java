package edu.calpoly.csc.snaptionverticalprototype.presentation.view.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.calpoly.csc.snaptionverticalprototype.R;
import edu.calpoly.csc.snaptionverticalprototype.presentation.view.fragments.WallFragment;

/**
 * The Main Activity and entry point for the application.
 *
 * @author Tyler Wong
 */

public class MainActivity extends AppCompatActivity {
   @BindView(R.id.toolbar)
   Toolbar mToolbar;
   @BindView(R.id.drawer)
   DrawerLayout mDrawerLayout;
   @BindView(R.id.navigation_view)
   NavigationView mNavigationView;

   private Fragment mCurrentFragment;
   private int mMenuId;
   private String fragTag;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      ButterKnife.bind(this);

      setSupportActionBar(mToolbar);

      fragTag = WallFragment.class.getSimpleName();
      mCurrentFragment = getSupportFragmentManager().findFragmentByTag(fragTag);
      if (mCurrentFragment == null) {
         mCurrentFragment = new WallFragment();
      }

      getSupportFragmentManager().beginTransaction()
            .replace(R.id.frame, mCurrentFragment, fragTag).commit();

      mNavigationView.setNavigationItemSelectedListener(menuItem -> {
            mDrawerLayout.closeDrawers();
            mMenuId = menuItem.getItemId();

            switch (mMenuId) {
               default:
                  fragTag = WallFragment.class.getSimpleName();
                  mCurrentFragment = getSupportFragmentManager().findFragmentByTag(fragTag);
                  if (mCurrentFragment == null) {
                     mCurrentFragment = new WallFragment();
                  }

                  break;
            }

            getSupportFragmentManager().beginTransaction()
                  .replace(R.id.frame, mCurrentFragment, fragTag).commit();

            return true;
         }
      );

      ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
            this, mDrawerLayout, mToolbar, R.string.open_drawer, R.string.close_drawer) {

         @Override
         public void onDrawerClosed(View drawerView) {
            super.onDrawerClosed(drawerView);
            hideKeyboard();
         }

         @Override
         public void onDrawerOpened(View drawerView) {
            super.onDrawerOpened(drawerView);
            hideKeyboard();
         }
      };

      mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
      actionBarDrawerToggle.syncState();
   }

   private void hideKeyboard() {
      View view = getCurrentFocus();

      if (view != null) {
         InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
         imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
      }
   }
}
