package com.snaptiongame.app.data.providers;

import com.snaptiongame.app.data.api.SnaptionApi;
import com.snaptiongame.app.data.models.OAuthRequest;
import com.snaptiongame.app.data.models.Session;
import com.snaptiongame.app.data.providers.api.ApiProvider;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * @author Tyler Wong
 */

public class SessionProvider {
    private static SnaptionApi apiService = ApiProvider.getApiService();

    public static Single<Session> userOAuthFacebook(OAuthRequest request) {
        return apiService.userOAuthFacebook(request);
    }

    public static Single<Session> userOAuthGoogle(OAuthRequest request) {
        return apiService.userOAuthGoogle(request);
    }

    public static Completable logout() {
        return apiService.logout();
    }
}
