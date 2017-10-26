@file:JvmName("FriendProvider")

package com.snaptiongame.app.data.providers

import com.snaptiongame.app.data.models.AddFriendRequest
import com.snaptiongame.app.data.models.Friend
import com.snaptiongame.app.data.providers.api.ApiProvider

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * @author Tyler Wong
 */

private val apiService = ApiProvider.getApiService()

fun getFriends(page: Int): Flowable<List<Friend>> = apiService.getFriends(page)

fun getFollowers(): Flowable<List<Friend>> = apiService.getFollowers()

fun getFacebookFriends(): Flowable<List<Friend>> = apiService.getFacebookFriends()

fun addFriend(request: AddFriendRequest): Single<AddFriendRequest> = apiService.addFriend(request)

fun removeFriend(request: AddFriendRequest): Completable = apiService.deleteFriend(request)

fun getAllFriends(): Flowable<List<Friend>> =
        Flowable.range(1, Integer.MAX_VALUE)
            .concatMap { getFriends(it) }
            .takeWhile { friends -> !friends.isEmpty() }

fun isFriend(userId: Int): Single<Boolean> =
        getAllFriends()
            .flatMapIterable { friend -> friend }
            .map { friend -> friend.id }
            .contains(userId)
