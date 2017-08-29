package com.snaptiongame.app.data.providers

import com.snaptiongame.app.data.models.User
import com.snaptiongame.app.data.models.UserStats
import com.snaptiongame.app.data.providers.api.ApiProvider

import io.reactivex.Observable
import io.reactivex.Single

/**
 * @author Tyler Wong
 */

object UserProvider {
    private val apiService = ApiProvider.getApiService()

    @JvmStatic
    fun getUser(userId: Int): Single<User> {
        return apiService.getUser(userId)
    }

    @JvmStatic
    fun searchUsers(email: String, facebookId: String,
                    username: String, fullName: String, page: Int): Observable<List<User>> {
        return apiService.searchUsers(email, facebookId, username, fullName, page)
    }

    @JvmStatic
    fun updateUser(user: User): Single<User> {
        return apiService.updateUser(user)
    }

    @JvmStatic
    fun getUserStats(userId: Int): Single<UserStats> {
        return apiService.getUserStats(userId)
    }
}
