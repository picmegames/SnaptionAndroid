package com.snaptiongame.app.data.providers;

import com.snaptiongame.app.data.models.Caption;
import com.snaptiongame.app.data.models.Like;
import com.snaptiongame.app.data.api.SnaptionApi;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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
    private Like like;

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
        when(service.getCaptions(TEST_GAME_ID)).thenReturn(Observable.just(captions));
        like = new Like(0, false, Like.UPVOTE, Like.CAPTION_ID);
        when(service.upvoteOrFlagCaption(like)).thenReturn(Observable.just(like));
        caption = new Caption(0, "test0");
        when(service.addCaption(TEST_GAME_ID, caption)).thenReturn(Observable.just(caption));
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
//      CaptionProvider.upvoteOrFlagCaption(like)
//            .subscribeOn(Schedulers.io())
//            .subscribe(
//                  returnedLike -> assertTrue(returnedLike.equals(like)),
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