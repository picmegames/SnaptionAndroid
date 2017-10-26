@file:JvmName("FriendProvider")

package com.snaptiongame.app.data.providers

import com.snaptiongame.app.R.id.friends
import com.snaptiongame.app.data.models.AddFriendRequest
import com.snaptiongame.app.data.models.Friend
import com.snaptiongame.app.data.providers.api.ApiProvider

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

/**
 * @author Tyler Wong
 */

private val apiService = ApiProvider.getApiService()

fun getFriends(page: Int): Observable<List<Friend>> = apiService.getFriends(page)

fun getFollowers(): Observable<List<Friend>> = apiService.getFollowers()

fun getFacebookFriends(): Observable<List<Friend>> = apiService.getFacebookFriends()

fun addFriend(request: AddFriendRequest): Single<AddFriendRequest> = apiService.addFriend(request)

fun removeFriend(request: AddFriendRequest): Completable = apiService.deleteFriend(request)

fun getAllFriends(): Observable<List<Friend>> =
        Observable.range(1, Integer.MAX_VALUE)
            .concatMap { getFriends(it) }
            .takeWhile { friends -> !friends.isEmpty() }

fun isFriend(userId: Int): Single<Boolean> =
        getAllFriends()
            .flatMapIterable { friend -> friend }
            .map { friend -> friend.id }
            .contains(userId)
