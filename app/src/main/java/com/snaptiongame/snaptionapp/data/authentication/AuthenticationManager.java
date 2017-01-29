package com.snaptiongame.snaptionapp.data.authentication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.snaptiongame.snaptionapp.R;
import com.snaptiongame.snaptionapp.data.models.OAuthRequest;
import com.snaptiongame.snaptionapp.data.models.Session;
import com.snaptiongame.snaptionapp.data.models.User;
import com.snaptiongame.snaptionapp.data.providers.FriendProvider;
import com.snaptiongame.snaptionapp.data.providers.SessionProvider;
import com.snaptiongame.snaptionapp.data.providers.UserProvider;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Realm;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

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

   private static final String FB_FIELDS = "fields";
   private static final String FB_REQUEST_FIELDS = "id, name, email, picture.type(large), cover.type(large)";

   public static final String SNAPTION_USER_ID = "snaption_user_id";
   public static final String SNAPTION_USERNAME = "snaption_username";
   public static final String PROFILE_IMAGE_URL = "image_url";
   public static final String COVER_PHOTO_URL = "cover_photo";
   public static final String FULL_NAME = "full_name";
   public static final String EMAIL = "email";

   public static final String FACEBOOK_LOGIN = "facebook";
   public static final String GOOGLE_SIGN_IN = "google";

   private AuthenticationManager(Context context) {

      // INIT Shared Preferences Editor
      preferences = context.getSharedPreferences(context.getPackageName(),
            Context.MODE_PRIVATE);

      // INIT Facebook Login Callbacks
      callbackManager = CallbackManager.Factory.create();

      // INIT Google Sign In APIs
      GoogleSignInOptions signInOptions = new GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.google_client_id))
            .requestEmail()
            .build();

      // INIT Google API Client
      googleApiClient = new GoogleApiClient.Builder(context)
            .enableAutoManage((FragmentActivity) context, connectionResult -> {
            })
            .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
            .build();
   }

   public static AuthenticationManager getInstance(Context context) {
      // IF we haven't initialized an instance of Authentication Manager
      if (authManager == null) {
         // INIT instance of Authentication Manager
         authManager = new AuthenticationManager(context);
      }
      // RETURN an instance of Authentication Manager
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
            setFacebookLoginState();

            GraphRequest request = GraphRequest.newMeRequest(
                  loginResult.getAccessToken(), (JSONObject object, GraphResponse response) -> {
                     String profileImageUrl = "";
                     String coverPhotoUrl = "";
                     String name = "";
                     String email = "";

                     try {
                        profileImageUrl = object.getJSONObject("picture")
                              .getJSONObject("data")
                              .getString("url");
                        coverPhotoUrl = object.getJSONObject("cover")
                              .getString("source");
                        name = object.getString("name");
                        email = object.getString("email");

                        saveLoginInfo(profileImageUrl, coverPhotoUrl, name, email);
                        FriendProvider.loadUserFriends();
                     }
                     catch (JSONException e) {
                        Timber.e(e);
                     }

                     SessionProvider.userOAuthFacebook(new OAuthRequest(loginResult.getAccessToken().getToken(),
                           FirebaseInstanceId.getInstance().getToken(), FACEBOOK_LOGIN))
                           .observeOn(AndroidSchedulers.mainThread())
                           .subscribe(new Subscriber<Session>() {
                              @Override
                              public void onCompleted() {
                                 Timber.i("OAuth session successful");
                                 handleSnaptionLogIn(getSnaptionUserId());
                              }

                              @Override
                              public void onError(Throwable e) {
                                 Timber.e(e);
                              }

                              @Override
                              public void onNext(Session session) {
                                 saveSnaptionUserId(session.userId);
                              }
                           });

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
            Timber.i("Login canceled");
         }

         @Override
         public void onError(FacebookException error) {
            Timber.e(error);
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
      // IF we are logged in with Facebook
      if (isFacebook && !isGoogle) {
         // Call Facebook's logout method
         LoginManager.getInstance().logOut();
      }
      else if (isGoogle && !isFacebook) {
         // ELSE Call Google's sign out method
         Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(status -> {
            if (status.isSuccess()) {
               Timber.i("Sign out of Google success");
            }
            else {
               Timber.e("Could not sign out of Google");
            }
         });
      }
      editor.putBoolean(LOGGED_IN, false);
      editor.apply();

      clearLoginInfo();
   }

   private void saveSnaptionUserId(int snaptionUserId) {
      SharedPreferences.Editor editor = preferences.edit();
      editor.putInt(SNAPTION_USER_ID, snaptionUserId);
      editor.apply();
   }

   public void saveSnaptionUsername(String username) {
      SharedPreferences.Editor editor = preferences.edit();
      editor.putString(SNAPTION_USERNAME, username);
      editor.apply();
   }

   private void saveLoginInfo(String imageUrl, String coverUrl, String name, String email) {
      SharedPreferences.Editor editor = preferences.edit();
      editor.putString(PROFILE_IMAGE_URL, imageUrl);
      editor.putString(COVER_PHOTO_URL, coverUrl);
      editor.putString(FULL_NAME, name);
      editor.putString(EMAIL, email);
      editor.apply();
   }

   private void clearLoginInfo() {
      SharedPreferences.Editor editor = preferences.edit();
      editor.putString(SNAPTION_USER_ID, "");
      editor.putString(SNAPTION_USERNAME, "");
      editor.putString(PROFILE_IMAGE_URL, "");
      editor.putString(COVER_PHOTO_URL, "");
      editor.putString(FULL_NAME, "");
      editor.putString(EMAIL, "");
      editor.apply();
      Realm realm = Realm.getDefaultInstance();
      realm.beginTransaction();
      realm.deleteAll();
      realm.commitTransaction();
   }

   public int getSnaptionUserId() {
      return preferences.getInt(SNAPTION_USER_ID, 0);
   }

   public String getSnaptionUsername() {
      return preferences.getString(SNAPTION_USERNAME, "");
   }

   public String getProfileImageUrl() {
      return preferences.getString(PROFILE_IMAGE_URL, "");
   }

   public String getCoverPhotoUrl() {
      return preferences.getString(COVER_PHOTO_URL, "");
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

   private void handleSnaptionLogIn(int snaptionUserId) {
      UserProvider.getUser(snaptionUserId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<User>() {
               @Override
               public void onCompleted() {
                  Timber.i("Successfully got user info");
               }

               @Override
               public void onError(Throwable e) {
                  Timber.e(e);
               }

               @Override
               public void onNext(User user) {
                  saveSnaptionUsername(user.username);
               }
            });
   }

   private void handleGoogleSignInResult(GoogleSignInResult result) {
      if (result.isSuccess()) {
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

            SessionProvider.userOAuthGoogle(new OAuthRequest(profileResult.getIdToken(),
                  FirebaseInstanceId.getInstance().getToken(), GOOGLE_SIGN_IN))
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(new Subscriber<Session>() {
                     @Override
                     public void onCompleted() {
                        Timber.i("OAuth session successful");
                        handleSnaptionLogIn(getSnaptionUserId());
                     }

                     @Override
                     public void onError(Throwable e) {
                        Timber.e(e);
                     }

                     @Override
                     public void onNext(Session session) {
                        saveSnaptionUserId(session.userId);
                     }
                  });
         }

         saveLoginInfo(profileImageUrl, "", username, email);
         setGoogleLoginState();

         if (authCallback != null) {
            authCallback.onSuccess();
         }
      }
      else {
         Timber.e("Google login failed :(");
      }
   }
}
