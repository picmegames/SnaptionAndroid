package com.snaptiongame.snaptionapp.data.providers;

import com.snaptiongame.snaptionapp.data.models.OAuthRequest;
import com.snaptiongame.snaptionapp.data.models.Session;
import com.snaptiongame.snaptionapp.data.providers.api.SnaptionApiProvider;
import com.snaptiongame.snaptionapp.data.services.SnaptionApiService;

import rx.Observable;

/**
 * @author Tyler Wong
 */

public class SessionProvider {
   private static SnaptionApiService apiService = SnaptionApiProvider.getApiService();

   public static Observable<Session> userOAuthFacebook(OAuthRequest request) {
      return apiService.userOAuthFacebook(request);
   }

   public static Observable<Session> userOAuthGoogle(OAuthRequest request) {
      return apiService.userOAuthGoogle(request);
   }
}
