package com.snaptiongame.app.data.providers;

import com.snaptiongame.app.data.api.SnaptionApi;
import com.snaptiongame.app.data.models.DeepLinkRequest;
import com.snaptiongame.app.data.providers.api.ApiProvider;

import io.reactivex.Observable;

/**
 * Created by BrianGouldsberry on 2/27/17.
 */

public class DeepLinkProvider {
    private static SnaptionApi apiService = ApiProvider.getApiService();

    public static Observable<String> getToken(DeepLinkRequest invite) {
        return apiService.getToken(invite);
    }
}