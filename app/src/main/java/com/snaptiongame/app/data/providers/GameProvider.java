package com.snaptiongame.app.data.providers;

import com.snaptiongame.app.data.api.SnaptionApi;
import com.snaptiongame.app.data.models.Game;
import com.snaptiongame.app.data.models.GameAction;
import com.snaptiongame.app.data.providers.api.ApiProvider;

import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;

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

    public static Observable<List<Game>> getUserGames(List<String> tags) {
        return apiService.getUserGames(tags)
                .map(GameProvider::reverseGames);
    }

    public static Observable<List<Game>> getDiscoverGames(List<String> tags) {
        return apiService.getDiscoverGames(tags);
    }

    public static Observable<List<Game>> getPopularGames(List<String> tags) {
        return apiService.getPopularGames(tags);
    }

    public static Observable<List<Game>> getUserGameHistory(int userId) {
        return apiService.getUserGameHistory(userId)
                .map(GameProvider::reverseGames);
    }

    public static Observable<Game> getGame(int gameId, String token) {
        return apiService.getGame(gameId, token);
    }

    public static Observable<GameAction> upvoteOrFlagGame(GameAction request) {
        return apiService.upvoteOrFlagGame(request);
    }

    public static Observable<Game> addGame(Game snaption) {
        return apiService.addGame(snaption);
    }
}
