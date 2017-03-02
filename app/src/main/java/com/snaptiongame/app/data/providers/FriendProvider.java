package com.snaptiongame.app.data.providers;

import com.snaptiongame.app.data.models.AddFriendRequest;
import com.snaptiongame.app.data.models.Friend;
import com.snaptiongame.app.data.providers.api.ApiProvider;
import com.snaptiongame.app.data.api.SnaptionApi;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author Tyler Wong
 */

public class FriendProvider {
    private static SnaptionApi apiService = ApiProvider.getApiService();

    public static Observable<List<Friend>> loadFriends(int userId) {
        return apiService.getFriends(userId);
    }

    public static Observable<List<Friend>> getFacebookFriends() {
        return apiService.getFacebookFriends();
    }

    public static Observable<AddFriendRequest> addFriend(int myId, AddFriendRequest request) {
        return apiService.addFriend(myId, request);
    }

    public static Observable<AddFriendRequest> removeFriend(int myId, AddFriendRequest request) {
        return apiService.deleteFriend(myId, request);
    }
}
