package com.snaptiongame.app.data.providers;

import com.snaptiongame.app.data.api.SnaptionApi;
import com.snaptiongame.app.data.models.Game;
import com.snaptiongame.app.data.models.GameAction;
import com.snaptiongame.app.data.providers.api.ApiProvider;

import java.util.Collections;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * @author Tyler Wong
 */

public class GameProvider {
    private static SnaptionApi apiService = ApiProvider.getApiService();

    private static List<Game> reverseGames(List<Game> games) {
        Collections.reverse(games);
        return games;
    }

    public static Observable<List<Game>> getGames(boolean isPublic) {
        return apiService.getGames(isPublic)
                .map(GameProvider::reverseGames);
    }

    public static Single<List<Game>> getUserGames(List<String> tags) {
        return apiService.getUserGames(tags)
                .map(GameProvider::reverseGames)
                .flatMapIterable(games -> games)
                .filter(game -> !game.beenFlagged)
                .toList();
    }

    public static Single<List<Game>> getDiscoverGames(List<String> tags) {
        return apiService.getDiscoverGames(tags)
                .flatMapIterable(games -> games)
                .filter(game -> !game.beenFlagged)
                .toList();
    }

    public static Single<List<Game>> getPopularGames(List<String> tags) {
        return apiService.getPopularGames(tags)
                .flatMapIterable(games -> games)
                .filter(game -> !game.beenFlagged)
                .toList();
    }

    public static Single<List<Game>> getUserGameHistory(int userId) {
        return apiService.getUserGameHistory(userId)
                .map(GameProvider::reverseGames)
                .flatMapIterable(games -> games)
                .filter(game -> !game.beenFlagged)
                .toList();
    }

    public static Single<Game> getGame(int gameId, String token) {
        return apiService.getGame(gameId, token);
    }

    public static Completable upvoteOrFlagGame(GameAction request) {
        return apiService.upvoteOrFlagGame(request);
    }

    public static Completable addGame(Game snaption) {
        return apiService.addGame(snaption);
    }
}
