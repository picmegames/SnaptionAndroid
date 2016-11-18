package com.snaptiongame.snaptionapp.presentation.view.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.snaptiongame.snaptionapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Tyler Wong
 */

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
   @BindView(R.id.logo)
   ImageView mLogo;
   @BindView(R.id.facebook_login_button)
   LoginButton mFacebookLoginButton;
   @BindView(R.id.google_sign_in_button)
   SignInButton mGoogleSignInButton;

   private CallbackManager mCallbackManager;
   private GoogleApiClient mGoogleApiClient;
   private SharedPreferences mPreferences;

   private static final int RC_SIGN_IN = 2222;

   public static final String LOGGED_IN = "logged in";

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      // Init Facebook SDK
      FacebookSdk.sdkInitialize(getApplicationContext());

      setContentView(R.layout.activity_login);
      ButterKnife.bind(this);

      // Set Logo
      Glide.with(this).load(R.drawable.snaption_logo).into(mLogo);

      // Get Shared Preferences Editor
      mPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

      // Init Facebook Login Callbacks
      FacebookSdk.sdkInitialize(getApplicationContext());
      mCallbackManager = CallbackManager.Factory.create();
      mFacebookLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
         @Override
         public void onSuccess(LoginResult loginResult) {
            System.out.println("Success! Logged in with token: " + loginResult.getAccessToken().toString());
            // Handle Google Sign In success
            // Send user e-mail and other info to server?
            // Send some access token?
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putBoolean(LOGGED_IN, true);
            editor.apply();

            returnToMain();
         }

         @Override
         public void onCancel() {
            System.out.println("Login canceled.");
         }

         @Override
         public void onError(FacebookException error) {
            System.out.println(error.toString());
         }
      });

      // Init Google Login
      GoogleSignInOptions signInOptions = new GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build();
      mGoogleApiClient = new GoogleApiClient.Builder(this)
            .enableAutoManage(this, this)
            .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
            .build();
   }

   @OnClick(R.id.google_sign_in_button)
   public void googleLogin(View view) {
      Intent googleIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
      startActivityForResult(googleIntent, RC_SIGN_IN);
   }

   private void handleGoogleSignInResult(GoogleSignInResult result) {
      if (result.isSuccess()) {
         GoogleSignInAccount account = result.getSignInAccount();

         if (account != null) {
            System.out.println(account.getEmail());
         }

         // Handle Google Sign In success
         // Send user e-mail and other info to server?
         // Send some access token?
         SharedPreferences.Editor editor = mPreferences.edit();
         editor.putBoolean(LOGGED_IN, true);
         editor.apply();

         returnToMain();
      }
      else {
         System.out.println("Google login failed :(");
      }
   }

   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);

      if (requestCode == RC_SIGN_IN) {
         GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
         handleGoogleSignInResult(result);
      }

      mCallbackManager.onActivityResult(requestCode, resultCode, data);
   }

   private void returnToMain() {
      Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
      startActivity(mainIntent);
   }

   @Override
   public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

   }
}
