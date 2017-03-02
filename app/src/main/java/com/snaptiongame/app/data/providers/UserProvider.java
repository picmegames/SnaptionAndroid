package com.snaptiongame.app.data.providers;

import com.snaptiongame.app.data.models.User;
import com.snaptiongame.app.data.providers.api.ApiProvider;
import com.snaptiongame.app.data.api.SnaptionApi;

import io.reactivex.Observable;

/**
 * @author Tyler Wong
 */

public class UserProvider {
    private static SnaptionApi apiService = ApiProvider.getApiService();

    public static Observable<User> getUser(int userId) {
        return apiService.getUser(userId);
    }

    public static Observable<User> getUserWithEmail(String email) {
        return apiService.getUserByEmail(email);
    }

    public static Observable<User> getUserWithFacebook(String id) {
        return apiService.getUserByFacebook(id);
    }

    public static Observable<User> updateUser(User user) {
        return apiService.updateUser(user);
    }
}
