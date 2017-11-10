@file:JvmName("LeaderboardProvider")

package com.snaptiongame.app.data.providers

import com.snaptiongame.app.data.models.Friend
import com.snaptiongame.app.data.providers.api.ApiProvider

import io.reactivex.Single

/**
 * @author Tyler Wong
 */

private val apiService = ApiProvider.getApiService()

fun getUserLeaderboard(friendsOnly: Boolean): Single<List<Friend>> =
    apiService.getUserLeaderboard(friendsOnly)
            .flatMapIterable { user -> user }
            .map { user -> Friend(user) }
            .toList()
