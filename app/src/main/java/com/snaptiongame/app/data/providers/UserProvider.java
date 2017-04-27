package com.snaptiongame.app.data.providers;

import com.snaptiongame.app.data.api.SnaptionApi;
import com.snaptiongame.app.data.models.User;
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

    public static Single<User> getUserWithEmail(String email) {
        return apiService.getUserByEmail(email);
    }

    public static Single<User> getUserWithFacebook(String id) {
        return apiService.getUserByFacebookID(id);
    }

    public static Single<User> updateUser(User user) {
        return apiService.updateUser(user);
    }

    public static Observable<List<User>> loadUsers(String username) {
        return apiService.getUserByUsername(username);
    }
}
