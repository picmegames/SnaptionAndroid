package com.snaptiongame.app.data.providers;

import com.snaptiongame.app.data.models.User;
import com.snaptiongame.app.data.api.SnaptionApi;

import org.junit.Before;
import org.junit.Test;

import io.reactivex.Observable;

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
        when(service.getUser(0)).thenReturn(Observable.just(user));
        when(service.getUserByEmail("")).thenReturn(Observable.just(user));
        when(service.getUserByFacebook("")).thenReturn(Observable.just(user));
        when(service.updateUser(user)).thenReturn(Observable.just(user));
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
    public void testFindUserEmail() {
        // TODO Fix test
//      UserProvider.getUserWithEmail("")
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
    public void testFindUserFacebook() {
        // TODO Fix test
//      UserProvider.getUserWithFacebook("")
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