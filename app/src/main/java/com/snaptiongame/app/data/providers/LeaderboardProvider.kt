@file:JvmName("LeaderboardProvider")

package com.snaptiongame.app.data.providers

import com.snaptiongame.app.data.models.User
import com.snaptiongame.app.data.providers.api.ApiProvider

import java.util.ArrayList

import io.reactivex.Single

/**
 * @author Tyler Wong
 */

private val apiService = ApiProvider.getApiService()

val experienceLeaderboard: Single<List<User>>
    get() = Single.just(ArrayList())
