@file:JvmName("FriendProvider")

package com.snaptiongame.app.data.providers

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

fun getFriends(page: Int): Observable<List<Friend>> {
    return apiService.getFriends(page)
}

val followers: Observable<List<Friend>>
    get() = apiService.followers

val facebookFriends: Observable<List<Friend>>
    get() = apiService.facebookFriends

fun addFriend(request: AddFriendRequest): Single<AddFriendRequest> {
    return apiService.addFriend(request)
}

fun removeFriend(request: AddFriendRequest): Completable {
    return apiService.deleteFriend(request)
}

val allFriends: Observable<List<Friend>>
    get() = Observable.range(1, Integer.MAX_VALUE)
            .concatMap { getFriends(it) }
            .takeWhile { friends -> !friends.isEmpty() }

fun isFriend(userId: Int): Single<Boolean> {
    return allFriends
            .flatMapIterable { friend -> friend }
            .map { friend -> friend.id }
            .contains(userId)
}
