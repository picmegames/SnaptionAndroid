package com.snaptiongame.app.data.providers;

import com.snaptiongame.app.data.models.OAuthRequest;
import com.snaptiongame.app.data.models.Session;
import com.snaptiongame.app.data.providers.api.ApiProvider;
import com.snaptiongame.app.data.api.SnaptionApi;

import io.reactivex.Observable;

/**
 * @author Tyler Wong
 */

public class SessionProvider {
    private static SnaptionApi apiService = ApiProvider.getApiService();

    public static Observable<Session> userOAuthFacebook(OAuthRequest request) {
        return apiService.userOAuthFacebook(request);
    }

    public static Observable<Session> userOAuthGoogle(OAuthRequest request) {
        return apiService.userOAuthGoogle(request);
    }
}
