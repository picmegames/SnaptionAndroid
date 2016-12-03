package com.snaptiongame.snaptionapp.data.authentication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
   private static AuthenticationManager authManager;

   private CallbackManager callbackManager;
   private GoogleApiClient googleApiClient;
   private SharedPreferences preferences;
   private AuthenticationCallback authCallback;

   private static final String LOGGED_IN = "logged in";
   private static final String FACEBOOK_LOGIN = "facebook";
   private static final String GOOGLE_SIGN_IN = "google";

   private static final String FB_FIELDS = "fields";
   private static final String FB_REQUEST_FIELDS = "id, name, email, picture.type(large)";

   private static final String PROFILE_IMAGE_URL = "image_url";
   private static final String FULL_NAME = "full_name";
   private static final String EMAIL = "email";

   private AuthenticationManager(Context context) {
      // Init Facebook SDK
      FacebookSdk.sdkInitialize(context);

      // Get Shared Preferences Editor
      preferences = context.getSharedPreferences(context.getPackageName(),
            Context.MODE_PRIVATE);

      // Init Facebook Login Callbacks
      callbackManager = CallbackManager.Factory.create();

      // Init Google Sign In APIs
      GoogleSignInOptions signInOptions = new GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build();

      googleApiClient = new GoogleApiClient.Builder(context)
            .enableAutoManage((FragmentActivity) context, connectionResult -> {
            })
            .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
            .build();
   }

   public static AuthenticationManager getInstance(Context context) {
      if (authManager == null) {
         authManager = new AuthenticationManager(context);
      }

      return authManager;
   }

   public void googleActivityResult(Intent data) {
      GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
      handleGoogleSignInResult(result);
   }

   public void facebookActivityResult(int requestCode, int resultCode, Intent data) {
      callbackManager.onActivityResult(requestCode, resultCode, data);
   }

   public void setFacebookCallback(Context context, LoginButton facebookButton) {
      // Set Facebook Login Permissions
      facebookButton.setReadPermissions(
            context.getString(R.string.fb_permission_profile),
            context.getString(R.string.fb_permission_friends),
            context.getString(R.string.fb_permission_email)
      );

      facebookButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
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

                        saveLoginInfo(profileImageUrl, name, email);
                     }
                     catch (JSONException e) {
                        Log.v("Exception!", "Couldn't complete Graph Request");
                     }

                     if (authCallback != null) {
                        authCallback.onSuccess();
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
      return Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
   }

   public void connectGoogleApi() {
      googleApiClient.connect();
   }

   public void disconnectGoogleApi() {
      googleApiClient.disconnect();
   }

   public boolean isLoggedIn() {
      return preferences.getBoolean(LOGGED_IN, false);
   }

   public void registerCallback(AuthenticationCallback callback) {
      this.authCallback = callback;
   }

   public void unregisterCallback() {
      this.authCallback = null;
   }

   public void logout() {
      SharedPreferences.Editor editor = preferences.edit();
      boolean isFacebook = preferences.getBoolean(FACEBOOK_LOGIN, false);
      boolean isGoogle = preferences.getBoolean(GOOGLE_SIGN_IN, false);

      if (isFacebook && !isGoogle) {
         LoginManager.getInstance().logOut();
      }
      else if (isGoogle && !isFacebook) {
         Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(status -> {
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

      clearLoginInfo();

      if (authCallback != null) {
         authCallback.onSuccess();
      }
   }

   private void saveLoginInfo(String imageUrl, String name, String email) {
      SharedPreferences.Editor editor = preferences.edit();
      editor.putString(PROFILE_IMAGE_URL, imageUrl);
      editor.putString(FULL_NAME, name);
      editor.putString(EMAIL, email);
      editor.apply();
   }

   private void clearLoginInfo() {
      SharedPreferences.Editor editor = preferences.edit();
      editor.putString(PROFILE_IMAGE_URL, "");
      editor.putString(FULL_NAME, "");
      editor.putString(EMAIL, "");
      editor.apply();
   }

   public String getProfileImageUrl() {
      return preferences.getString(PROFILE_IMAGE_URL, "");
   }

   public String getUserFullName() {
      return preferences.getString(FULL_NAME, "");
   }

   public String getEmail() {
      return preferences.getString(EMAIL, "");
   }

   private void setFacebookLoginState() {
      SharedPreferences.Editor editor = preferences.edit();
      editor.putBoolean(LOGGED_IN, true);
      editor.putBoolean(FACEBOOK_LOGIN, true);
      editor.putBoolean(GOOGLE_SIGN_IN, false);
      editor.apply();
   }

   private void setGoogleLoginState() {
      SharedPreferences.Editor editor = preferences.edit();
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
         String profileImageUrl = "";
         String username = "";
         String email = "";

         if (profileResult != null) {
            if (profileResult.getPhotoUrl() != null) {
               profileImageUrl = profileResult.getPhotoUrl().toString();
            }

            username = profileResult.getDisplayName();
            email = profileResult.getEmail();
         }

         saveLoginInfo(profileImageUrl, username, email);
         setGoogleLoginState();

         if (authCallback != null) {
            authCallback.onSuccess();
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
