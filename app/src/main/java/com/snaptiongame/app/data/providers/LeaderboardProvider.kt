@file:JvmName("LeaderboardProvider")

package com.snaptiongame.app.data.providers

import com.snaptiongame.app.data.models.Friend
import com.snaptiongame.app.data.providers.api.ApiProvider

import io.reactivex.Single

/**
 * @author Tyler Wong
 */

private val apiService = ApiProvider.getApiService()

val experienceLeaderboard: Single<List<Friend>>
    get() = apiService.getUserLeaderboard()
            .flatMapIterable { user -> user }
            .map { user -> Friend(user) }
            .toList()
