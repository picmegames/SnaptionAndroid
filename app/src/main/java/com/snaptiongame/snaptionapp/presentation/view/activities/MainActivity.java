package com.snaptiongame.snaptionapp.presentation.view.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.snaptiongame.snaptionapp.R;
import com.snaptiongame.snaptionapp.data.auth.GoogleApiClientService;
import com.snaptiongame.snaptionapp.presentation.view.fragments.WallFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

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

   private GoogleApiClient mGoogleApiClient;
   private Fragment mCurrentFragment;
   private String fragTag;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      ButterKnife.bind(this);

      mGoogleApiClient = GoogleApiClientService.getInstance(this);

      setSupportActionBar(mToolbar);

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

   @Override
   protected void onStart() {
      super.onStart();
      mGoogleApiClient.connect();
   }

   @Override
   protected void onStop() {
      super.onStop();
      mGoogleApiClient.disconnect();
   }

   @Override
   public boolean onNavigationItemSelected(@NonNull MenuItem item) {
      mDrawerLayout.closeDrawers();
      boolean isLoggingOut = false;

      switch (item.getItemId()) {
         case R.id.log_out:
            logout();
            isLoggingOut = true;
            break;

         default:
            fragTag = WallFragment.class.getSimpleName();
            mCurrentFragment = getSupportFragmentManager().findFragmentByTag(fragTag);
            if (mCurrentFragment == null) {
               mCurrentFragment = new WallFragment();
            }

            break;
      }

      if (!isLoggingOut) {
         getSupportFragmentManager().beginTransaction()
               .replace(R.id.frame, mCurrentFragment, fragTag).commit();
      }

      return true;
   }

   private void logout() {
      SharedPreferences preferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
      SharedPreferences.Editor editor = preferences.edit();
      boolean isFacebook = preferences.getBoolean(LoginActivity.FACEBOOK_LOGIN, false);
      boolean isGoogle = preferences.getBoolean(LoginActivity.GOOGLE_SIGN_IN, false);

      if (isFacebook && !isGoogle) {
         LoginManager.getInstance().logOut();
         goToLogin();
      }
      else if (isGoogle && !isFacebook) {
         Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(status -> {
            if (status.isSuccess()) {
               goToLogin();
            }
            else {
               System.out.println("Could not log out :(");
            }
         });
      }
      editor.putBoolean(LoginActivity.LOGGED_IN, false);
      editor.apply();
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
