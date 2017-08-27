package com.snaptiongame.app.data.providers;

import com.snaptiongame.app.data.api.SnaptionApi;
import com.snaptiongame.app.data.providers.api.ApiProvider;

import io.reactivex.Completable;

/**
 * @author Tyler Wong
 */

public class NotificationProvider {
    private static SnaptionApi apiService = ApiProvider.getApiService();

    public static Completable refreshNotificationToken(String deviceToken) {
        return apiService.refreshNotificationToken(deviceToken);
    }
}
