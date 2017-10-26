@file:JvmName("GameProvider")

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

private val apiService = ApiProvider.getApiService()

fun getGamesMine(tags: List<String>?, status: String?, page: Int): Single<List<Game>> =
    apiService.getGamesMine(tags, status, page)
            .flatMapIterable { games -> games }
            .filter { game -> !game.beenFlagged }
            .toList()

fun getGamesDiscover(tags: List<String>?, status: String?, page: Int): Single<List<Game>> =
    apiService.getGamesDiscover(tags, status, page)
            .flatMapIterable { games -> games }
            .filter { game -> !game.beenFlagged }
            .toList()

fun getGamesPopular(tags: List<String>?, status: String?, page: Int): Single<List<Game>> =
    apiService.getGamesPopular(tags, status, page)
            .flatMapIterable { games -> games }
            .filter { game -> !game.beenFlagged }
            .toList()

fun getGamesHistory(userId: Int, page: Int): Single<List<Game>> =
    apiService.getGamesHistory(userId, page)
            .flatMapIterable { games -> games }
            .filter { game -> !game.beenFlagged }
            .toList()

fun getGame(gameId: Int, token: String?): Single<Game> = apiService.getGame(gameId, token)

fun getPrivateGameUsers(gameId: Int): Single<List<Friend>> =
    getGame(gameId, null)
            .map { game -> game.users }
            .toObservable()
            .flatMapIterable { users -> users }
            .filter { user -> user.id != AuthManager.getUserId() }
            .map { Friend(it) }
            .toList()

fun getGameTags(gameId: Int): Single<List<Tag>> =
    getGame(gameId, null)
            .map { game -> game.tags }

fun upvoteOrFlagGame(request: GameAction): Completable = apiService.upvoteOrFlagGame(request)

fun addGame(snaption: Game): Completable = apiService.addGame(snaption)
