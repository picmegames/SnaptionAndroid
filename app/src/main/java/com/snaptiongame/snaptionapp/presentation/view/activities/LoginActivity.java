package com.snaptiongame.snaptionapp.presentation.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.SignInButton;
import com.snaptiongame.snaptionapp.R;
import com.snaptiongame.snaptionapp.data.authentication.AuthenticationManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Tyler Wong
 */

public class LoginActivity extends AppCompatActivity {
   @BindView(R.id.logo)
   ImageView mLogo;
   @BindView(R.id.facebook_login_button)
   LoginButton mFacebookLoginButton;
   @BindView(R.id.google_sign_in_button)
   SignInButton mGoogleSignInButton;

   private AuthenticationManager mAuthManager;

   private static final int RC_SIGN_IN = 2222;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      // Initialize Authentication Manager
      mAuthManager = AuthenticationManager.getInstance(getApplicationContext());
      mAuthManager.registerCallback(this::onBackPressed);

      setContentView(R.layout.activity_login);
      ButterKnife.bind(this);

      int logoHeight = (int) getResources().getDimension(R.dimen.logo_height);
      int logoWidth = (int) getResources().getDimension(R.dimen.logo_width);

      // Set Logo
      Glide.with(this)
            .load(R.drawable.snaption_logo)
            .override(logoWidth, logoHeight)
            .fitCenter()
            .into(mLogo);

      mAuthManager.setFacebookCallback(this, mFacebookLoginButton);
   }

   @Override
   protected void onDestroy() {
      super.onStop();
      mAuthManager.unregisterCallback();
   }

   @OnClick(R.id.google_sign_in_button)
   public void googleLogin(View view) {
      startActivityForResult(mAuthManager.getGoogleIntent(), RC_SIGN_IN);
   }

   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);

      if (requestCode == RC_SIGN_IN) {
         mAuthManager.googleActivityResult(data);
      }
      else {
         mAuthManager.facebookActivityResult(requestCode, resultCode, data);
      }
   }
}
