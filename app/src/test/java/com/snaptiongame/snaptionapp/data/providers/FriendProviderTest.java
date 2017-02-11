package com.snaptiongame.snaptionapp.data.providers;

import com.snaptiongame.snaptionapp.data.models.AddFriendRequest;
import com.snaptiongame.snaptionapp.data.models.Friend;
import com.snaptiongame.snaptionapp.data.services.SnaptionApiService;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Tyler Wong
 */

public class FriendProviderTest {
   private SnaptionApiService service;
   private List<Friend> friends;
   private AddFriendRequest request;

   @Before
   public void setup() {
      service = mock(SnaptionApiService.class);
      friends = new ArrayList<>();
      friends.add(new Friend("", "", "", "", "", "", "", ""));
      friends.add(new Friend("", "", "", "", "", "", "", ""));
      friends.add(new Friend("", "", "", "", "", "", "", ""));
      when(service.getFriends(0)).thenReturn(Observable.just(friends));
      request = new AddFriendRequest(0);
      when(service.addFriend(0, request)).thenReturn(Observable.just(request));
      when(service.deleteFriend(0, request)).thenReturn(Observable.just(request));
   }

   @Test
   public void testLoadFriends() {
      FriendProvider.loadFriends(0)
            .subscribeOn(Schedulers.io())
            .subscribe(
                  returnedFriends -> assertTrue(returnedFriends.equals(friends)),
                  e -> {
                  },
                  () -> {
                  }
            );
   }

   @Test
   public void testAddFriend() {
      FriendProvider.addFriend(0, request)
            .subscribeOn(Schedulers.io())
            .subscribe(
                  returnedRequest -> assertTrue(returnedRequest.equals(request)),
                  e -> {
                  },
                  () -> {
                  }
            );
   }

   @Test
   public void testDeleteFriend() {
      FriendProvider.removeFriend(0, request)
            .subscribeOn(Schedulers.io())
            .subscribe(
                  returnedRequest -> assertTrue(returnedRequest.equals(request)),
                  e -> {
                  },
                  () -> {
                  }
            );
   }
}
