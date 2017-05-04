package com.snaptiongame.app.data.auth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

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
import com.snaptiongame.app.R;
import com.snaptiongame.app.SnaptionApplication;
import com.snaptiongame.app.data.models.OAuthRequest;
import com.snaptiongame.app.data.providers.SessionProvider;
import com.snaptiongame.app.data.providers.UserProvider;
import com.snaptiongame.app.data.providers.api.ApiProvider;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

/**
 * @author Tyler Wong
 */

public final class AuthManager {
    private static AuthManager authManager;
    private CallbackManager callbackManager;
    private GoogleApiClient googleApiClient;
    private static SharedPreferences preferences;
    private AuthCallback callback;

    private static final String LOGGED_IN = "logged in";
    private static final String FB_FIELDS = "fields";
    private static final String FB_PERMISSIONS = "id, name, email, picture.type(large)";
    private static final String USER_ID = "user_id";
    private static final String USERNAME = "username";
    private static final String PROFILE_IMAGE_URL = "image_url";
    private static final String FULL_NAME = "full_name";
    private static final String PICTURE = "picture";
    private static final String DATA = "data";
    private static final String URL = "url";
    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String FACEBOOK_LOGIN = "facebook";
    private static final String GOOGLE_SIGN_IN = "google";
    private static final String INVITE_TOKEN = "token";
    private static final String GAME_NOTIFICATIONS = "gameNotifications";
    private static final String FRIEND_NOTIFICATIONS = "friendNotifications";
    private static final String CLOSED_GAME_DIALOG = "closedGameDialog";

    private AuthManager(Context context) {
        // INIT Shared Preferences Editor
        preferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);

        // INIT Facebook Login Callbacks
        callbackManager = CallbackManager.Factory.create();

        // INIT Google Sign In APIs
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.google_client_id))
                .requestEmail()
                .build();

        // INIT Google API Client
        googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
                .build();

        // INIT Notification Preferences
        initNotifications();
    }

    public static void init(Context context) {
        // INIT an instance of an Authentication Manager
        if (authManager == null) {
            authManager = new AuthManager(context);
        }
    }

    public static AuthManager getInstance() {
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

    public void setFacebookCallback(LoginButton facebookButton) {
        Context context = SnaptionApplication.getContext();
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
                            String name = "";
                            String email = "";

                            try {
                                profileImageUrl = object.getJSONObject(PICTURE)
                                        .getJSONObject(DATA)
                                        .getString(URL);
                                name = object.getString(NAME);
                                email = object.getString(EMAIL);

                                saveLoginInfo(profileImageUrl, name, email);
                            }
                            catch (JSONException e) {
                                Timber.e(e);
                            }

                            handleOAuthFacebook(loginResult.getAccessToken().getToken(),
                                    FirebaseInstanceId.getInstance().getToken());
                        }
                );
                Bundle parameters = new Bundle();
                parameters.putString(FB_FIELDS, FB_PERMISSIONS);
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

                handleOAuthGoogle(profileResult.getIdToken(),
                        FirebaseInstanceId.getInstance().getToken());
            }

            saveLoginInfo(profileImageUrl, username, email);
            setGoogleLoginState();
        }
        else {
            Timber.e("Google login failed :(");
        }
    }

    private void handleOAuthFacebook(String accessToken, String deviceToken) {
        SessionProvider.userOAuthFacebook(new OAuthRequest(accessToken, deviceToken, getInviteToken()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        session -> {
                            saveUserId(session.userId);
                            getUserInfo(getUserId());
                        },
                        e -> {
                            Timber.e(e);
                            logout();
                            Toast.makeText(SnaptionApplication.getContext(),
                                    R.string.login_failure, Toast.LENGTH_LONG).show();
                            fireFailureCallback();
                        }
                );
    }

    private void handleOAuthGoogle(String token, String deviceToken) {
        SessionProvider.userOAuthGoogle(new OAuthRequest(token, deviceToken, getInviteToken()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        session -> {
                            saveUserId(session.userId);
                            getUserInfo(getUserId());
                        },
                        e -> {
                            Timber.e(e);
                            Toast.makeText(SnaptionApplication.getContext(),
                                    R.string.login_failure, Toast.LENGTH_LONG).show();
                            logout();
                            fireFailureCallback();
                        }
                );
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

    private void fireSuccessCallback() {
        if (callback != null) {
            callback.onAuthSuccess();
            callback = null;
        }
    }

    private void fireFailureCallback() {
        if (callback != null) {
            callback.onAuthFailure();
        }
    }

    private void initNotifications() {
        if (!preferences.contains(GAME_NOTIFICATIONS) && !preferences.contains(FRIEND_NOTIFICATIONS)) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(GAME_NOTIFICATIONS, true);
            editor.putBoolean(FRIEND_NOTIFICATIONS, true);
            editor.apply();
        }
    }

    public static boolean isGameNotificationsEnabled() {
        return preferences.getBoolean(GAME_NOTIFICATIONS, true);
    }

    public static boolean isFriendNotificationsEnabled() {
        return preferences.getBoolean(FRIEND_NOTIFICATIONS, true);
    }

    public static void setGameNotificationsEnabled(boolean isEnabled) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(GAME_NOTIFICATIONS, isEnabled);
        editor.apply();
    }

    public static void setFriendNotificationsEnabled(boolean isEnabled) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(FRIEND_NOTIFICATIONS, isEnabled);
        editor.apply();
    }

    public static boolean isClosedGameDialogEnabled() {
        return preferences.getBoolean(CLOSED_GAME_DIALOG, true);
    }

    public static void setIsClosedGameDialogEnabled(boolean isEnabled) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(CLOSED_GAME_DIALOG, isEnabled);
        editor.apply();
    }

    public static boolean isLoggedIn() {
        return preferences.getBoolean(LOGGED_IN, false);
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
            if (googleApiClient.isConnected()) {
                Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(status -> {
                    if (status.isSuccess()) {
                        Timber.i("Sign out of Google success");
                    }
                    else {
                        Timber.e("Could not sign out of Google");
                    }
                });
            }
        }
        editor.putBoolean(LOGGED_IN, false);
        editor.apply();

        setGameNotificationsEnabled(true);
        setFriendNotificationsEnabled(true);
        setIsClosedGameDialogEnabled(true);
        clearLoginInfo();
        ApiProvider.clearCookies();
    }

    public void registerCallback(AuthCallback callback) {
        this.callback = callback;
    }

    private static void saveUserId(int snaptionUserId) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(USER_ID, snaptionUserId);
        editor.apply();
    }

    public static void saveUsername(String username) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USERNAME, username);
        editor.apply();
    }

    public static void saveProfileImage(String profileImage) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PROFILE_IMAGE_URL, profileImage);
        editor.apply();
    }

    public static void saveToken(String token) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(INVITE_TOKEN, token);
        editor.apply();
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
        editor.putInt(USER_ID, 0);
        editor.putString(USERNAME, "");
        editor.putString(PROFILE_IMAGE_URL, "");
        editor.putString(FULL_NAME, "");
        editor.putString(EMAIL, "");
        editor.apply();
    }

    public static String getInviteToken() {
        return preferences.getString(INVITE_TOKEN, "");
    }

    public static int getUserId() {
        return preferences.getInt(USER_ID, 0);
    }

    public static String getUsername() {
        return preferences.getString(USERNAME, "");
    }

    public static String getProfileImageUrl() {
        return preferences.getString(PROFILE_IMAGE_URL, "");
    }

    public static String getUserFullName() {
        return preferences.getString(FULL_NAME, "");
    }

    public static String getEmail() {
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

    private void getUserInfo(int snaptionUserId) {
        UserProvider.getUser(snaptionUserId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                            saveUsername(user.username);

                            if (user.imageUrl != null) {
                                saveProfileImage(user.imageUrl);
                            }

                            fireSuccessCallback();
                        },
                        e -> {
                            Timber.e(e);
                            fireFailureCallback();
                        }
                );
    }
}
