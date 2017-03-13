package com.snaptiongame.app.data.providers;

import com.snaptiongame.app.data.api.SnaptionApi;
import com.snaptiongame.app.data.models.Game;
import com.snaptiongame.app.data.models.GameAction;

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
public class GameProviderTest {
    private SnaptionApi service;
    private List<Game> games;
    private Game game;
    private GameAction gameAction;

    @Before
    public void setup() {
        service = mock(SnaptionApi.class);
        games = new ArrayList<>();
        games.add(new Game(0, false, 0, "picture0", "", new ArrayList<>(), new ArrayList<>()));
        games.add(new Game(1, false, 1, "picture1", "", new ArrayList<>(), new ArrayList<>()));
        games.add(new Game(2, true, 2, "picture2", "", new ArrayList<>(), new ArrayList<>()));
        games.add(new Game(3, false, 3, "picture3", "", new ArrayList<>(), new ArrayList<>()));
        when(service.getGames(true)).thenReturn(Observable.just(games));
        gameAction = new GameAction(0, false, GameAction.UPVOTE, GameAction.GAME_ID);
        when(service.upvoteOrFlagGame(gameAction)).thenReturn(Observable.just(gameAction));
        game = new Game(0, false, 0, "picture0", "", new ArrayList<>(), new ArrayList<>());
        when(service.addGame(game)).thenReturn(Observable.just(game));
    }

    @Test
    public void testGetAllGames() {
        // TODO Fix test
//      GameProvider.getGames(true)
//            .subscribeOn(Schedulers.io())
//            .subscribe(
//                  returnedSnaptions -> assertTrue(returnedSnaptions.containsAll(games)),
//                  e -> {
//                  },
//                  () -> {
//                  }
//            );
    }

    @Test
    public void testUpvoteGame() {
        // TODO Fix test
//      GameProvider.upvoteOrFlagGame(gameAction)
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
    public void testAddGame() {
        // TODO Fix test
//      GameProvider.addGame(game)
//            .subscribeOn(Schedulers.io())
//            .subscribe(
//                  returnedSnaption -> assertTrue(returnedSnaption.equals(game)),
//                  e -> {
//                  },
//                  () -> {
//                  }
//            );
    }
}
