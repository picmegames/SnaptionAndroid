package com.snaptiongame.app.data.providers;

import com.snaptiongame.app.data.api.SnaptionApi;
import com.snaptiongame.app.data.models.User;
import com.snaptiongame.app.data.providers.api.ApiProvider;

import io.reactivex.Single;

/**
 * @author Tyler Wong
 */

public class UserProvider {
    private static SnaptionApi apiService = ApiProvider.getApiService();

    public static Single<User> getUser(int userId) {
        return apiService.getUser(userId);
    }

    public static Single<User> getUserWithEmail(String email) {
        return apiService.getUserByEmail(email);
    }

    public static Single<User> getUserWithFacebook(String id) {
        return apiService.getUserByFacebook(id);
    }

    public static Single<User> updateUser(User user) {
        return apiService.updateUser(user);
    }
}
