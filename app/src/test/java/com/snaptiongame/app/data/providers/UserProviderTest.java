package com.snaptiongame.app.data.providers;

import com.snaptiongame.app.data.api.SnaptionApi;
import com.snaptiongame.app.data.models.User;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Tyler Wong
 */

public class UserProviderTest {
    private SnaptionApi service;
    private User user;

    @Before
    public void setup() {
        service = mock(SnaptionApi.class);
        user = new User("");
        List<User> users = new ArrayList<>();
        users.add(user);
        when(service.getUser(0)).thenReturn(Single.just(user));
        when(service.updateUser(user)).thenReturn(Single.just(user));
    }

    @Test
    public void testGetUser() {
        // TODO Fix test
//      UserProvider.getUser(0)
//            .subscribeOn(Schedulers.io())
//            .subscribe(
//                  returnedUser -> assertTrue(returnedUser.equals(user)),
//                  e -> {
//                  },
//                  () -> {
//                  }
//            );
    }

    @Test
    public void testUpdateUser() {
        // TODO Fix test
//      UserProvider.updateUser(0, user)
//            .subscribeOn(Schedulers.io())
//            .subscribe(
//                  returnedUser -> assertTrue(returnedUser.equals(user)),
//                  e -> {
//                  },
//                  () -> {
//                  }
//            );
    }
}
