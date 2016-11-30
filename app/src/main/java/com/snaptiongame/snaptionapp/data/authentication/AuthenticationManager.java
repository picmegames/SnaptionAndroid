package com.snaptiongame.snaptionapp.data.authentication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.snaptiongame.snaptionapp.R;

import org.json.JSONException;
import org.json.JSONObject;

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

   private static final String FB_FIELDS = "fields";
   private static final String FB_REQUEST_FIELDS = "id, name, email, picture.type(large)";

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
            context.getString(R.string.fb_permission_friends),
            context.getString(R.string.fb_permission_email)
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

            GraphRequest request = GraphRequest.newMeRequest(
                  loginResult.getAccessToken(), (JSONObject object, GraphResponse response) -> {
                     String profileImageUrl = "";
                     String name = "";
                     String email = "";

                     try {
                        profileImageUrl = object.getJSONObject("picture")
                              .getJSONObject("data")
                              .getString("url");
                        name = object.getString("name");
                        email = object.getString("email");
                     }
                     catch (JSONException e) {
                        Log.v("Exception!", "Couldn't complete Graph Request");
                     }

                     if (mAuthCallback != null) {
                        mAuthCallback.onSuccess(profileImageUrl, name, email);
                     }
                  }
            );
            Bundle parameters = new Bundle();
            parameters.putString(FB_FIELDS, FB_REQUEST_FIELDS);
            request.setParameters(parameters);
            request.executeAsync();
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
               System.out.println("Logged out!");
            }
            else {
               System.out.println("Could not log out :(");
            }
         });
      }
      editor.putBoolean(LOGGED_IN, false);
      editor.apply();

      if (mAuthCallback != null) {
         mAuthCallback.onSuccess("", "", "");
      }
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
         // Handle Google Sign In success
         // Send user e-mail and other info to server?
         // Send some access token?
         GoogleSignInAccount profileResult = result.getSignInAccount();
         Uri profileImageUri = profileResult.getPhotoUrl();
         String profileImageUrl = "";

         if (profileImageUri != null) {
            profileImageUrl = profileResult.getPhotoUrl().toString();
         }

         String username = profileResult.getDisplayName();
         String email = profileResult.getEmail();

         setGoogleLoginState();
         if (mAuthCallback != null) {
            mAuthCallback.onSuccess(profileImageUrl, username, email);
         }
      }
      else {
         System.out.println("Google login failed :(");
      }
   }

   public interface AuthenticationCallback {
      void onSuccess(String profileImageUrl, String name, String email);
   }
}
