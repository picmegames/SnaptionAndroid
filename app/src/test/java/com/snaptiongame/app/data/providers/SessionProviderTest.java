package com.snaptiongame.app.data.providers;

import com.snaptiongame.app.data.api.SnaptionApi;
import com.snaptiongame.app.data.models.OAuthRequest;
import com.snaptiongame.app.data.models.Session;

import org.junit.Before;
import org.junit.Test;

import io.reactivex.Single;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Tyler Wong
 */

public class SessionProviderTest {
    private SnaptionApi service;
    private OAuthRequest request;
    private Session session;

    @Before
    public void setup() {
        service = mock(SnaptionApi.class);
        request = new OAuthRequest("", "", "");
        session = new Session(0);
        when(service.userOAuthFacebook(request)).thenReturn(Single.just(session));
        when(service.userOAuthGoogle(request)).thenReturn(Single.just(session));
    }

    @Test
    public void testUserOAuthFacebook() {
        // TODO Fix test
//      SessionProvider.userOAuthFacebook(request)
//            .subscribeOn(Schedulers.io())
//            .subscribe(
//                  returnedSession -> assertTrue(returnedSession.equals(session)),
//                  e -> {
//                  },
//                  () -> {
//                  }
//            );
    }

    @Test
    public void testUserOAuthGoogle() {
        // TODO Fix test
//      SessionProvider.userOAuthGoogle(request)
//            .subscribeOn(Schedulers.io())
//            .subscribe(
//                  returnedSession -> assertTrue(returnedSession.equals(session)),
//                  e -> {
//                  },
//                  () -> {
//                  }
//            );
    }
}
