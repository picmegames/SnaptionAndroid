package com.snaptiongame.snaptionapp.presentation.view;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.snaptiongame.snaptionapp.R;
import com.snaptiongame.snaptionapp.data.authentication.AuthenticationManager;
import com.snaptiongame.snaptionapp.presentation.view.login.LoginActivity;
import com.snaptiongame.snaptionapp.presentation.view.profile.ProfileActivity;
import com.snaptiongame.snaptionapp.presentation.view.wall.WallFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * The Main Activity and entry point for the application.
 *
 * @author Tyler Wong
 */

public class MainActivity extends AppCompatActivity
      implements NavigationView.OnNavigationItemSelectedListener {
   @BindView(R.id.toolbar)
   Toolbar mToolbar;
   @BindView(R.id.drawer)
   DrawerLayout mDrawerLayout;
   @BindView(R.id.navigation_view)
   NavigationView mNavigationView;

   CircleImageView mProfilePicture;
   TextView mNameView;
   TextView mEmailView;

   private AuthenticationManager mAuthManager;
   private Fragment mCurrentFragment;
   private String fragTag;

   @Override
   protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      mAuthManager = AuthenticationManager.getInstance(this);

      setContentView(R.layout.activity_main);
      ButterKnife.bind(this);

      setSupportActionBar(mToolbar);

      View headerView = mNavigationView.getHeaderView(0);
      mProfilePicture = (CircleImageView) headerView.findViewById(R.id.profile_image);
      mNameView = (TextView) headerView.findViewById(R.id.username);
      mEmailView = (TextView) headerView.findViewById(R.id.email);

      mProfilePicture.setOnClickListener(view -> {
         Intent profileIntent = new Intent(this, ProfileActivity.class);

         if (Build.VERSION.SDK_INT >= 21) {
            String transitionName = getString(R.string.shared_transition);
            ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(this,
                  mProfilePicture, transitionName);
            startActivity(profileIntent, transitionActivityOptions.toBundle());
         }
         else {
            startActivity(profileIntent);
         }
      });

      fragTag = WallFragment.class.getSimpleName();
      mCurrentFragment = getSupportFragmentManager().findFragmentByTag(fragTag);
      if (mCurrentFragment == null) {
         mCurrentFragment = new WallFragment();
      }

      getSupportFragmentManager().beginTransaction()
            .replace(R.id.frame, mCurrentFragment, fragTag).commit();

      mNavigationView.setNavigationItemSelectedListener(this);

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

   private void setDefaultHeader() {
      Glide.with(this)
            .load(R.mipmap.ic_launcher)
            .into(mProfilePicture);
      mNameView.setText(getString(R.string.welcome_message));
      mEmailView.setText("");
   }

   private void setUserHeader() {
      String profileImageUrl = mAuthManager.getProfileImageUrl();
      String name = mAuthManager.getUserFullName();
      String email = mAuthManager.getEmail();

      Glide.with(this)
            .load(profileImageUrl)
            .into(mProfilePicture);
      mNameView.setText(name);
      mEmailView.setText(email);
   }

   @Override
   protected void onResume() {
      super.onResume();

      if (mAuthManager.isLoggedIn()) {
         setUserHeader();
      }
      else {
         setDefaultHeader();
      }
   }

   @Override
   protected void onStart() {
      super.onStart();
      mAuthManager.connectGoogleApi();
   }

   @Override
   protected void onStop() {
      super.onStop();
      mAuthManager.disconnectGoogleApi();
   }

   @Override
   protected void onDestroy() {
      super.onStop();
      mAuthManager.unregisterCallback();
   }

   @Override
   public boolean onNavigationItemSelected(@NonNull MenuItem item) {
      mDrawerLayout.closeDrawers();

      switch (item.getItemId()) {
         case R.id.log_out:
            if (mAuthManager.isLoggedIn()) {
               mAuthManager.logout();
               goToLogin();
            }
            break;

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

   @Override
   public void onBackPressed() {
      // Don't allow back button in MainActivity
   }

   private void goToLogin() {
      Intent loginIntent = new Intent(this, LoginActivity.class);
      startActivity(loginIntent);
   }

   private void hideKeyboard() {
      View view = getCurrentFocus();

      if (view != null) {
         InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
         imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
      }
   }
}
