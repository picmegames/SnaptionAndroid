package com.snaptiongame.snaptionapp.data.authentication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.snaptiongame.snaptionapp.R;

/**
 * @author Tyler Wong
 */

public final class AuthenticationManager {
   private static AuthenticationManager mAuthManager;

   private CallbackManager mCallbackManager;
   private GoogleApiClient mGoogleApiClient;
   private SharedPreferences mPreferences;
   private AuthenticationCallback mAuthCallback;

   private static final String LOGGED_IN = "logged in";
   private static final String FACEBOOK_LOGIN = "facebook";
   private static final String GOOGLE_SIGN_IN = "google";

   private AuthenticationManager(Context context) {
      // Init Facebook SDK
      FacebookSdk.sdkInitialize(context);

      // Get Shared Preferences Editor
      mPreferences = context.getSharedPreferences(context.getPackageName(),
            Context.MODE_PRIVATE);

      // Init Facebook Login Callbacks
      mCallbackManager = CallbackManager.Factory.create();

      // Init Google Sign In APIs
      GoogleSignInOptions signInOptions = new GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build();

      mGoogleApiClient = new GoogleApiClient.Builder(context)
            .enableAutoManage((FragmentActivity) context, connectionResult -> {
            })
            .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
            .build();
   }

   public static AuthenticationManager getInstance(Context context) {
      if (mAuthManager == null) {
         mAuthManager = new AuthenticationManager(context);
      }

      return mAuthManager;
   }

   public void googleActivityResult(Intent data) {
      GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
      handleGoogleSignInResult(result);
   }

   public void facebookActivityResult(int requestCode, int resultCode, Intent data) {
      mCallbackManager.onActivityResult(requestCode, resultCode, data);
   }

   public void setFacebookCallback(Context context, LoginButton facebookButton) {
      // Set Facebook Login Permissions
      facebookButton.setReadPermissions(
            context.getString(R.string.fb_permission_profile),
            context.getString(R.string.fb_permission_friends)
      );
//      mFacebookLoginButton.setPublishPermissions(
//            context.getString(R.string.fb_permission_publish)
//      );

      facebookButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
         @Override
         public void onSuccess(LoginResult loginResult) {
            System.out.println("Success! Logged in with access token: " + loginResult.getAccessToken());
            // Handle Facebook Login success
            // Send user e-mail and other info to server?
            // Send some access token?
            setFacebookLoginState();
            if (mAuthCallback != null) {
               mAuthCallback.onSuccess();
            }
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
   }

   public Intent getGoogleIntent() {
      return Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
   }

   public void connectGoogleApi() {
      mGoogleApiClient.connect();
   }

   public void disconnectGoogleApi() {
      mGoogleApiClient.disconnect();
   }

   public boolean isLoggedIn() {
      return mPreferences.getBoolean(LOGGED_IN, false);
   }

   public void registerCallback(AuthenticationCallback callback) {
      this.mAuthCallback = callback;
   }

   public void unregisterCallback() {
      this.mAuthCallback = null;
   }

   public void logout() {
      SharedPreferences.Editor editor = mPreferences.edit();
      boolean isFacebook = mPreferences.getBoolean(FACEBOOK_LOGIN, false);
      boolean isGoogle = mPreferences.getBoolean(GOOGLE_SIGN_IN, false);

      if (isFacebook && !isGoogle) {
         LoginManager.getInstance().logOut();
      }
      else if (isGoogle && !isFacebook) {
         Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(status -> {
            if (status.isSuccess()) {
               if (mAuthCallback != null) {
                  mAuthCallback.onSuccess();
               }
            }
            else {
               System.out.println("Could not log out :(");
            }
         });
      }
      editor.putBoolean(LOGGED_IN, false);
      editor.apply();
   }

   private void setFacebookLoginState() {
      SharedPreferences.Editor editor = mPreferences.edit();
      editor.putBoolean(LOGGED_IN, true);
      editor.putBoolean(FACEBOOK_LOGIN, true);
      editor.putBoolean(GOOGLE_SIGN_IN, false);
      editor.apply();
   }

   private void setGoogleLoginState() {
      SharedPreferences.Editor editor = mPreferences.edit();
      editor.putBoolean(LOGGED_IN, true);
      editor.putBoolean(FACEBOOK_LOGIN, false);
      editor.putBoolean(GOOGLE_SIGN_IN, true);
      editor.apply();
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
         setGoogleLoginState();
         if (mAuthCallback != null) {
            mAuthCallback.onSuccess();
         }
      }
      else {
         System.out.println("Google login failed :(");
      }
   }

   public interface AuthenticationCallback {
      void onSuccess();
   }
}
