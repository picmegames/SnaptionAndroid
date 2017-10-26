@file:JvmName("UserProvider")

package com.snaptiongame.app.data.providers

import com.snaptiongame.app.data.models.User
import com.snaptiongame.app.data.models.UserStats
import com.snaptiongame.app.data.providers.api.ApiProvider
import io.reactivex.Flowable

import io.reactivex.Single

/**
 * @author Tyler Wong
 */

private val apiService = ApiProvider.getApiService()

fun getUser(userId: Int): Single<User> = apiService.getUser(userId)

fun searchUsers(email: String, facebookId: String,
                username: String, fullName: String, page: Int): Flowable<List<User>> =
    apiService.searchUsers(email, facebookId, username, fullName, page)

fun updateUser(user: User): Single<User> = apiService.updateUser(user)

fun getUserStats(userId: Int): Single<UserStats> = apiService.getUserStats(userId)
