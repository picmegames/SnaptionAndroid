package com.snaptiongame.snaptionapp.data.providers;

import com.snaptiongame.snaptionapp.data.api.SnaptionApi;
import com.snaptiongame.snaptionapp.data.models.DeepLinkRequest;
import com.snaptiongame.snaptionapp.data.models.Friend;
import com.snaptiongame.snaptionapp.data.models.GameInvite;
import com.snaptiongame.snaptionapp.data.providers.api.ApiProvider;

import java.util.List;

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
