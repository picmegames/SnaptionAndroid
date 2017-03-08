package com.snaptiongame.app.data.providers;

import com.snaptiongame.app.data.models.Game;
import com.snaptiongame.app.data.models.Like;
import com.snaptiongame.app.data.providers.api.ApiProvider;
import com.snaptiongame.app.data.api.SnaptionApi;

import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;

/**
 * @author Tyler Wong
 */

public class GameProvider {
    private static SnaptionApi apiService = ApiProvider.getApiService();

    public static Observable<List<Game>> getGames(boolean isPublic) {
        return apiService.getGames(isPublic)
                .filter(games -> {
                    Collections.reverse(games);
                    return true;
                });
    }

    public static Observable<Game> getGame(int gameId, String token) {
        return apiService.getGame(gameId, token);
    }

    public static Observable<Like> upvoteOrFlagGame(Like request) {
        return apiService.upvoteOrFlagGame(request);
    }

    public static Observable<Game> addGame(Game snaption) {
        return apiService.addGame(snaption);
    }
}
