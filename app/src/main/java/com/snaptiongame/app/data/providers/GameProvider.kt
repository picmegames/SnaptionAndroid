package com.snaptiongame.app.data.providers

import com.snaptiongame.app.data.auth.AuthManager
import com.snaptiongame.app.data.models.Friend
import com.snaptiongame.app.data.models.Game
import com.snaptiongame.app.data.models.GameAction
import com.snaptiongame.app.data.models.Tag
import com.snaptiongame.app.data.providers.api.ApiProvider

import io.reactivex.Completable
import io.reactivex.Single

/**
 * @author Tyler Wong
 */

object GameProvider {
    private val apiService = ApiProvider.getApiService()

    @JvmStatic
    fun getGamesMine(tags: List<String>?, status: String?, page: Int): Single<List<Game>> {
        return apiService.getGamesMine(tags, status, page)
                .flatMapIterable { games -> games }
                .filter { game -> !game.beenFlagged }
                .toList()
    }

    @JvmStatic
    fun getGamesDiscover(tags: List<String>?, status: String?, page: Int): Single<List<Game>> {
        return apiService.getGamesDiscover(tags, status, page)
                .flatMapIterable { games -> games }
                .filter { game -> !game.beenFlagged }
                .toList()
    }

    @JvmStatic
    fun getGamesPopular(tags: List<String>?, status: String?, page: Int): Single<List<Game>> {
        return apiService.getGamesPopular(tags, status, page)
                .flatMapIterable { games -> games }
                .filter { game -> !game.beenFlagged }
                .toList()
    }

    @JvmStatic
    fun getGamesHistory(userId: Int, page: Int): Single<List<Game>> {
        return apiService.getGamesHistory(userId, page)
                .flatMapIterable { games -> games }
                .filter { game -> !game.beenFlagged }
                .toList()
    }

    @JvmStatic
    fun getGame(gameId: Int, token: String?): Single<Game> {
        return apiService.getGame(gameId, token)
    }

    @JvmStatic
    fun getPrivateGameUsers(gameId: Int): Single<List<Friend>> {
        return getGame(gameId, null)
                .map { game -> game.users }
                .toObservable()
                .flatMapIterable { users -> users }
                .filter { user -> user.id != AuthManager.getUserId() }
                .map { Friend(it) }
                .toList()
    }

    @JvmStatic
    fun getGameTags(gameId: Int): Single<List<Tag>> {
        return getGame(gameId, null)
                .map { game -> game.tags }
    }

    @JvmStatic
    fun upvoteOrFlagGame(request: GameAction): Completable {
        return apiService.upvoteOrFlagGame(request)
    }

    @JvmStatic
    fun addGame(snaption: Game): Completable {
        return apiService.addGame(snaption)
    }
}
