package com.snaptiongame.app.data.providers;

import com.snaptiongame.app.data.api.SnaptionApi;
import com.snaptiongame.app.data.models.Like;
import com.snaptiongame.app.data.models.Snaption;

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
public class SnaptionProviderTest {
    private SnaptionApi service;
    private List<Snaption> snaptions;
    private Snaption snaption;
    private Like like;

    @Before
    public void setup() {
        service = mock(SnaptionApi.class);
        snaptions = new ArrayList<>();
        snaptions.add(new Snaption(0, false, 0, "picture0", "", new ArrayList<>(), new ArrayList<>()));
        snaptions.add(new Snaption(1, false, 1, "picture1", "", new ArrayList<>(), new ArrayList<>()));
        snaptions.add(new Snaption(2, true, 2, "picture2", "", new ArrayList<>(), new ArrayList<>()));
        snaptions.add(new Snaption(3, false, 3, "picture3", "", new ArrayList<>(), new ArrayList<>()));
        when(service.getSnaptions(true)).thenReturn(Observable.just(snaptions));
        like = new Like(0, false, Like.UPVOTE, Like.GAME_ID);
        when(service.upvoteOrFlagSnaption(like)).thenReturn(Observable.just(like));
        snaption = new Snaption(0, false, 0, "picture0", "", new ArrayList<>(), new ArrayList<>());
        when(service.addSnaption(snaption)).thenReturn(Observable.just(snaption));
    }

    @Test
    public void testGetAllSnaptions() {
        // TODO Fix test
//      SnaptionProvider.getSnaptions(true)
//            .subscribeOn(Schedulers.io())
//            .subscribe(
//                  returnedSnaptions -> assertTrue(returnedSnaptions.containsAll(snaptions)),
//                  e -> {
//                  },
//                  () -> {
//                  }
//            );
    }

    @Test
    public void testUpvoteSnaption() {
        // TODO Fix test
//      SnaptionProvider.upvoteOrFlagSnaption(like)
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
    public void testAddSnaption() {
        // TODO Fix test
//      SnaptionProvider.addSnaption(snaption)
//            .subscribeOn(Schedulers.io())
//            .subscribe(
//                  returnedSnaption -> assertTrue(returnedSnaption.equals(snaption)),
//                  e -> {
//                  },
//                  () -> {
//                  }
//            );
    }
}
