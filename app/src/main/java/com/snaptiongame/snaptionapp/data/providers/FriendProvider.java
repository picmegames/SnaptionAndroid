package com.snaptiongame.snaptionapp.data.providers;

import com.snaptiongame.snaptionapp.data.models.AddFriendRequest;
import com.snaptiongame.snaptionapp.data.models.Friend;
import com.snaptiongame.snaptionapp.data.providers.api.ApiProvider;
import com.snaptiongame.snaptionapp.data.services.SnaptionApiService;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author Tyler Wong
 */

public class FriendProvider {
   private static SnaptionApiService apiService = ApiProvider.getApiService();

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
