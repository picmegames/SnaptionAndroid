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

    public static Observable<List<Game>> getUserGames() {
        return apiService.getUserGames()
                .map(GameProvider::reverseGames);
    }

    public static Observable<List<Game>> getDiscoverGames() {
        return apiService.getDiscoverGames();
    }

    public static Observable<List<Game>> getPopularGames() {
        return apiService.getPopularGames();
    }

    public static Observable<List<Game>> getUserGameHistory(int userId) {
        return apiService.getUserGameHistory(userId);
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
