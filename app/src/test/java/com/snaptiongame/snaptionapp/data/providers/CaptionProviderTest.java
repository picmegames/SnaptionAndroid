package com.snaptiongame.snaptionapp.data.providers;

import com.snaptiongame.snaptionapp.data.models.Caption;
import com.snaptiongame.snaptionapp.data.models.Like;
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

public class CaptionProviderTest {
   private SnaptionApiService service;
   private List<Caption> captions;
   private Caption caption;
   private Like like;

   private static final int TEST_GAME_ID = 1;

   @Before
   public void setup() {
      service = mock(SnaptionApiService.class);
      captions = new ArrayList<>();
      captions.add(new Caption(0, "test0", 1));
      captions.add(new Caption(1, "test1", 1));
      captions.add(new Caption(2, "test2", 1));
      captions.add(new Caption(3, "test3", 1));
      captions.add(new Caption(4, "test4", 1));
      when(service.getCaptions(TEST_GAME_ID)).thenReturn(Observable.just(captions));
      like = new Like(0, 0, false, false, "");
      when(service.upvoteCaption(like)).thenReturn(Observable.just(like));
      caption = new Caption(0, "test0", 1);
      when(service.addCaption(TEST_GAME_ID, caption)).thenReturn(Observable.just(caption));
   }

   @Test
   public void testGetCaptions() {
      CaptionProvider.getCaptions(TEST_GAME_ID)
            .subscribeOn(Schedulers.io())
            .subscribe(
                  returnedCaptions -> assertTrue(returnedCaptions.containsAll(captions)),
                  e -> {
                  },
                  () -> {
                  }
            );
   }

   @Test
   public void testUpvoteCaption() {
      CaptionProvider.upvoteCaption(like)
            .subscribeOn(Schedulers.io())
            .subscribe(
                  returnedLike -> assertTrue(returnedLike.equals(like)),
                  e -> {
                  },
                  () -> {
                  }
            );
   }

   @Test
   public void testAddCaption() {
      CaptionProvider.addCaption(TEST_GAME_ID, caption)
            .subscribeOn(Schedulers.io())
            .subscribe(
                  returnedCaption -> assertTrue(returnedCaption.equals(caption)),
                  e -> {
                  },
                  () -> {
                  }
            );
   }
}
