package com.snaptiongame.app.data.providers;

import com.snaptiongame.app.data.api.SnaptionApi;
import com.snaptiongame.app.data.models.User;
import com.snaptiongame.app.data.providers.api.ApiProvider;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

/**
 * @author Tyler Wong
 */

public class LeaderboardProvider {
    private static SnaptionApi apiService = ApiProvider.getApiService();

    public static Single<List<User>> getExperienceLeaderboard() {
        return Single.just(new ArrayList<>());
    }
}
