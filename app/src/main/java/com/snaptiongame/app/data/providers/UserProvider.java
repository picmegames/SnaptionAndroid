package com.snaptiongame.app.data.providers;

import com.snaptiongame.app.data.api.SnaptionApi;
import com.snaptiongame.app.data.models.Rank;
import com.snaptiongame.app.data.models.User;
import com.snaptiongame.app.data.models.UserStats;
import com.snaptiongame.app.data.providers.api.ApiProvider;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * @author Tyler Wong
 */

public class UserProvider {
    private static SnaptionApi apiService = ApiProvider.getApiService();

    public static Single<User> getUser(int userId) {
        return apiService.getUser(userId);
    }

    public static Observable<List<User>> getUsersWithEmail(String email) {
        return apiService.getUsersByEmail(email);
    }

    public static Observable<List<User>> getUsersWithFacebook(String facebookId) {
        return apiService.getUsersByFacebookID(facebookId);
    }

    public static Observable<Rank> getRank(int rankId) {
        return apiService.getRanks()
                .flatMapIterable(rank -> rank)
                .filter(rank -> rank.id == rankId);
    }

    public static Single<User> updateUser(User user) {
        return apiService.updateUser(user);
    }

    public static Observable<List<User>> getUsersByUsername(String username) {
        return apiService.getUsersByUsername(username);
    }

    public static Observable<List<User>> getUsersByFullName(String fullName) {
        return apiService.getUsersByFullName(fullName);
    }

    public static Single<UserStats> getUserStats(int userId) {
        return apiService.getUserStats(userId);
    }
}
