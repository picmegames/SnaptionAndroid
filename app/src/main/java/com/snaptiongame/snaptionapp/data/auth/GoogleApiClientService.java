package com.snaptiongame.snaptionapp.data.auth;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * @author Tyler Wong
 */

public class GoogleApiClientService {
   private static GoogleApiClient mGoogleApiClient;

   public static GoogleApiClient getInstance(Context context) {
      if (mGoogleApiClient == null) {
         GoogleSignInOptions signInOptions = new GoogleSignInOptions
               .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
               .requestEmail()
               .build();

         mGoogleApiClient = new GoogleApiClient.Builder(context)
               .enableAutoManage((FragmentActivity) context, connectionResult -> {})
               .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
               .build();
      }

      return mGoogleApiClient;
   }
}
