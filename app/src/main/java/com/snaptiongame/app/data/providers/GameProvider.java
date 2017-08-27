package com.snaptiongame.app.data.providers;

import com.snaptiongame.app.data.api.SnaptionApi;
import com.snaptiongame.app.data.auth.AuthManager;
import com.snaptiongame.app.data.models.Friend;
import com.snaptiongame.app.data.models.Game;
import com.snaptiongame.app.data.models.GameAction;
import com.snaptiongame.app.data.models.Tag;
import com.snaptiongame.app.data.providers.api.ApiProvider;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * @author Tyler Wong
 */

public class GameProvider {
    private static SnaptionApi apiService = ApiProvider.INSTANCE.getApiService();

    public static Single<List<Game>> getGamesMine(List<String> tags, String status, int page) {
        return apiService.getGamesMine(tags, status, page)
                .flatMapIterable(games -> games)
                .filter(game -> !game.getBeenFlagged())
                .toList();
    }

    public static Single<List<Game>> getGamesDiscover(List<String> tags, String status, int page) {
        return apiService.getGamesDiscover(tags, status, page)
                .flatMapIterable(games -> games)
                .filter(game -> !game.getBeenFlagged())
                .toList();
    }
  
    public static Single<List<Game>> getGamesPopular(List<String> tags, String status, int page) {
        return apiService.getGamesPopular(tags, status, page)
                .flatMapIterable(games -> games)
                .filter(game -> !game.getBeenFlagged())
                .toList();
    }

    public static Single<List<Game>> getGamesHistory(int userId, int page) {
        return apiService.getGamesHistory(userId, page)
                .flatMapIterable(games -> games)
                .filter(game -> !game.getBeenFlagged())
                .toList();
    }

    public static Single<Game> getGame(int gameId, String token) {
        return apiService.getGame(gameId, token);
    }

    public static Single<List<Friend>> getPrivateGameUsers(int gameId) {
        return getGame(gameId, null)
                .map(game -> game.getUsers())
                .toObservable()
                .flatMapIterable(users -> users)
                .filter(user -> user.id != AuthManager.getUserId())
                .map(Friend::new)
                .toList();
    }

    public static Single<List<Tag>> getGameTags(int gameId) {
        return getGame(gameId, null)
                .map(game -> game.getTags());
    }

    public static Completable upvoteOrFlagGame(GameAction request) {
        return apiService.upvoteOrFlagGame(request);
    }

    public static Completable addGame(Game snaption) {
        return apiService.addGame(snaption);
    }
}
