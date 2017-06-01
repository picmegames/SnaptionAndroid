package com.snaptiongame.app.data.providers;

import com.snaptiongame.app.data.api.SnaptionApi;
import com.snaptiongame.app.data.auth.AuthManager;
import com.snaptiongame.app.data.models.Friend;
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

    public static Observable<List<User>> searchUsers(String email, String facebookId,
                                                     String username, String fullName, int page) {
        return apiService.searchUsers(email, facebookId, username, fullName, page);
    }

    public static Single<User> updateUser(User user) {
        return apiService.updateUser(user);
    }
    
    public static Single<UserStats> getUserStats(int userId) {
        return apiService.getUserStats(userId);
    }
}
