package com.snaptiongame.app.data.providers;

import com.snaptiongame.app.data.api.SnaptionApi;
import com.snaptiongame.app.data.models.AddFriendRequest;
import com.snaptiongame.app.data.models.Friend;
import com.snaptiongame.app.data.providers.api.ApiProvider;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * @author Tyler Wong
 */

public class FriendProvider {
    private static SnaptionApi apiService = ApiProvider.getApiService();

    public static Observable<List<Friend>> getFriends() {
        return apiService.getFriends();
    }

    public static Observable<List<Friend>> getFollowers() {
        return apiService.getFollowers();
    }

    public static Observable<List<Friend>> getFacebookFriends() {
        return apiService.getFacebookFriends();
    }

    public static Single<AddFriendRequest> addFriend(AddFriendRequest request) {
        return apiService.addFriend(request);
    }

    public static Completable removeFriend(AddFriendRequest request) {
        return apiService.deleteFriend(request);
    }

    public static Single<Boolean> isFriend(int userId) {
        return getFriends()
                .flatMapIterable(friend -> friend)
                .map(friend -> friend.id)
                .contains(userId);
    }
}
