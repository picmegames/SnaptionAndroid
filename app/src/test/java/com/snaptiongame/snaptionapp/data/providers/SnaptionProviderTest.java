package com.snaptiongame.snaptionapp.data.providers;

import com.snaptiongame.snaptionapp.data.models.Like;
import com.snaptiongame.snaptionapp.data.models.Snaption;
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

public class SnaptionProviderTest {
   private SnaptionApiService service;
   private List<Snaption> snaptions;
   private Snaption snaption;
   private Like like;

   @Before
   public void setup() {
      service = mock(SnaptionApiService.class);
      snaptions = new ArrayList<>();
      snaptions.add(new Snaption(0, false, 0, "picture0", ""));
      snaptions.add(new Snaption(1, false, 1, "picture1", ""));
      snaptions.add(new Snaption(2, true, 2, "picture2", ""));
      snaptions.add(new Snaption(3, false, 3, "picture3", ""));
      when(service.getSnaptions()).thenReturn(Observable.just(snaptions));
      like = new Like(0, 0, false, false, "");
      when(service.upvoteSnaption(like)).thenReturn(Observable.just(like));
      snaption = new Snaption(0, false, 0, "picture0", "");
      when(service.addSnaption(snaption)).thenReturn(Observable.just(snaption));
   }

   @Test
   public void testGetAllSnaptions() {
      SnaptionProvider.getAllSnaptions()
            .subscribeOn(Schedulers.io())
            .subscribe(
                  returnedSnaptions -> assertTrue(returnedSnaptions.containsAll(snaptions)),
                  e -> {
                  },
                  () -> {
                  }
            );
   }

   @Test
   public void testUpvoteSnaption() {
      SnaptionProvider.upvoteSnaption(like)
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
   public void testAddSnaption() {
      SnaptionProvider.addSnaption(snaption)
            .subscribeOn(Schedulers.io())
            .subscribe(
                  returnedSnaption -> assertTrue(returnedSnaption.equals(snaption)),
                  e -> {
                  },
                  () -> {
                  }
            );
   }
}
