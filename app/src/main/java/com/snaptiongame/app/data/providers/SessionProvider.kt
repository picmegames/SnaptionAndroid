package com.snaptiongame.app.data.providers

import com.snaptiongame.app.data.models.OAuthRequest
import com.snaptiongame.app.data.models.Session
import com.snaptiongame.app.data.providers.api.ApiProvider

import io.reactivex.Completable
import io.reactivex.Single

/**
 * @author Tyler Wong
 */

object SessionProvider {
    private val apiService = ApiProvider.getApiService()

    @JvmStatic
    fun userOAuthFacebook(request: OAuthRequest): Single<Session> {
        return apiService.userOAuthFacebook(request)
    }

    @JvmStatic
    fun userOAuthGoogle(request: OAuthRequest): Single<Session> {
        return apiService.userOAuthGoogle(request)
    }

    @JvmStatic
    fun logout(): Completable {
        return apiService.logout()
    }

    @JvmStatic
    val isSessionValid: Single<Boolean>
        get() = apiService.isSessionValid
}
