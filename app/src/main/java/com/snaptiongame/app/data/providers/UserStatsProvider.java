package com.snaptiongame.app.data.providers;

import com.snaptiongame.app.data.api.SnaptionApi;
import com.snaptiongame.app.data.models.UserStats;
import com.snaptiongame.app.data.providers.api.ApiProvider;

import io.reactivex.Single;

/**
 * @author Quang Ngo
 */

public class UserStatsProvider {
    private static SnaptionApi apiService = ApiProvider.getApiService();

    public static Single<UserStats> getUserStats(int userId) {
        return apiService.getUserStats(userId);
    }
}
