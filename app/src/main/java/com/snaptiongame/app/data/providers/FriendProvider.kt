package com.snaptiongame.app.data.providers

import com.snaptiongame.app.data.models.AddFriendRequest
import com.snaptiongame.app.data.models.Friend
import com.snaptiongame.app.data.providers.api.ApiProvider

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Single

/**
 * @author Tyler Wong
 */

object FriendProvider {
    private val apiService = ApiProvider.getApiService()

    @JvmStatic
    fun getFriends(page: Int): Observable<List<Friend>> {
        return apiService.getFriends(page)
    }

    @JvmStatic
    val followers: Observable<List<Friend>>
        get() = apiService.followers

    @JvmStatic
    val facebookFriends: Observable<List<Friend>>
        get() = apiService.facebookFriends

    @JvmStatic
    fun addFriend(request: AddFriendRequest): Single<AddFriendRequest> {
        return apiService.addFriend(request)
    }

    @JvmStatic
    fun removeFriend(request: AddFriendRequest): Completable {
        return apiService.deleteFriend(request)
    }

    @JvmStatic
    val allFriends: Observable<List<Friend>>
        get() = Observable.range(1, Integer.MAX_VALUE)
                .concatMap { getFriends(it) }
                .takeWhile { friends -> !friends.isEmpty() }

    @JvmStatic
    fun isFriend(userId: Int): Single<Boolean> {
        return allFriends
                .flatMapIterable { friend -> friend }
                .map { friend -> friend.id }
                .contains(userId)
    }
}
