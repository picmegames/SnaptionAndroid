package com.snaptiongame.app.data.providers;

import com.snaptiongame.app.data.api.SnaptionApi;
import com.snaptiongame.app.data.models.AddFriendRequest;
import com.snaptiongame.app.data.models.Friend;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Tyler Wong
 */

public class FriendProviderTest {
    private SnaptionApi service;
    private List<Friend> friends;
    private AddFriendRequest request;

    @Before
    public void setup() {
        service = mock(SnaptionApi.class);
        friends = new ArrayList<>();
        friends.add(new Friend(0, "", "", "", "", "", "", ""));
        friends.add(new Friend(0, "", "", "", "", "", "", ""));
        friends.add(new Friend(0, "", "", "", "", "", "", ""));
        when(service.getFriends(0)).thenReturn(Observable.just(friends));
        request = new AddFriendRequest(0);
        when(service.addFriend(0, request)).thenReturn(Single.just(request));
        when(service.deleteFriend(0, request)).thenReturn(Completable.complete());
    }

    @Test
    public void testLoadFriends() {
        // TODO Fix test
//      FriendProvider.loadFriends(0)
//            .subscribeOn(Schedulers.io())
//            .subscribe(
//                  returnedFriends -> assertTrue(returnedFriends.equals(friends)),
//                  e -> {
//                  },
//                  () -> {
//                  }
//            );
    }

    @Test
    public void testAddFriend() {
        // TODO Fix test
//      FriendProvider.addFriend(0, request)
//            .subscribeOn(Schedulers.io())
//            .subscribe(
//                  returnedRequest -> assertTrue(returnedRequest.equals(request)),
//                  e -> {
//                  },
//                  () -> {
//                  }
//            );
    }

    @Test
    public void testDeleteFriend() {
        // TODO Fix test
//      FriendProvider.removeFriend(0, request)
//            .subscribeOn(Schedulers.io())
//            .subscribe(
//                  returnedRequest -> assertTrue(returnedRequest.equals(request)),
//                  e -> {
//                  },
//                  () -> {
//                  }
//            );
    }
}
