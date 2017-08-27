package com.snaptiongame.app.data.providers;

import com.snaptiongame.app.data.api.SnaptionApi;
import com.snaptiongame.app.data.models.Caption;
import com.snaptiongame.app.data.models.GameAction;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Tyler Wong
 */

public class CaptionProviderTest {
    private SnaptionApi service;
    private List<Caption> captions;
    private Caption caption;
    private GameAction gameAction;

    private static final int TEST_GAME_ID = 1;

    @Before
    public void setup() {
        service = mock(SnaptionApi.class);
        captions = new ArrayList<>();
        captions.add(new Caption(0, "test0"));
        captions.add(new Caption(1, "test1"));
        captions.add(new Caption(2, "test2"));
        captions.add(new Caption(3, "test3"));
        captions.add(new Caption(4, "test4"));
        when(service.getCaptions(TEST_GAME_ID, 1)).thenReturn(Observable.just(captions));
        gameAction = new GameAction(0, false, GameAction.UPVOTE, GameAction.UPVOTE);
        when(service.upvoteOrFlagCaption(gameAction)).thenReturn(Completable.complete());
        caption = new Caption(0, "test0");
        when(service.addCaption(TEST_GAME_ID, caption)).thenReturn(Completable.complete());
    }

    @Test
    public void testGetCaptions() {
        // TODO Fix test
//      CaptionProvider.getCaptions(TEST_GAME_ID)
//            .subscribeOn(Schedulers.io())
//            .subscribe(
//                  returnedCaptions -> assertTrue(returnedCaptions.containsAll(captions)),
//                  e -> {
//                  },
//                  () -> {
//                  }
//            );
    }

    @Test
    public void testUpvoteCaption() {
        // TODO Fix test
//      CaptionProvider.upvoteOrFlagCaption(gameAction)
//            .subscribeOn(Schedulers.io())
//            .subscribe(
//                  returnedLike -> assertTrue(returnedLike.equals(gameAction)),
//                  e -> {
//                  },
//                  () -> {
//                  }
//            );
    }

    @Test
    public void testAddCaption() {
        // TODO Fix test
//      CaptionProvider.addCaption(TEST_GAME_ID, caption)
//            .subscribeOn(Schedulers.io())
//            .subscribe(
//                  returnedCaption -> assertTrue(returnedCaption.equals(caption)),
//                  e -> {
//                  },
//                  () -> {
//                  }
//            );
    }
}
