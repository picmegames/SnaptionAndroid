package com.snaptiongame.snaptionapp.data.providers;

import com.snaptiongame.snaptionapp.data.models.OAuthRequest;
import com.snaptiongame.snaptionapp.data.models.Session;
import com.snaptiongame.snaptionapp.data.services.SnaptionApiService;

import org.junit.Before;
import org.junit.Test;

import io.reactivex.Observable;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Tyler Wong
 */

public class SessionProviderTest {
    private SnaptionApiService service;
    private OAuthRequest request;
    private Session session;

    @Before
    public void setup() {
        service = mock(SnaptionApiService.class);
        request = new OAuthRequest("", "");
        session = new Session(0);
        when(service.userOAuthFacebook(request)).thenReturn(Observable.just(session));
        when(service.userOAuthGoogle(request)).thenReturn(Observable.just(session));
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
