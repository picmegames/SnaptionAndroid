package com.snaptiongame.app.data.providers;

import com.snaptiongame.app.data.api.SnaptionApi;
import com.snaptiongame.app.data.models.ActivityFeedItem;
import com.snaptiongame.app.data.providers.api.ApiProvider;

import java.util.List;

import io.reactivex.Single;

/**
 * @author Tyler Wong
 */

public class ActivityFeedProvider {
    private static SnaptionApi apiService = ApiProvider.getApiService();

    public static Single<List<ActivityFeedItem>> getActivityFeed(int page) {
        return apiService.getActivityFeed(page);
    }
}
