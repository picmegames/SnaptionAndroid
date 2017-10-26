@file:JvmName("SessionProvider")

package com.snaptiongame.app.data.providers

import com.snaptiongame.app.data.models.OAuthRequest
import com.snaptiongame.app.data.models.Session
import com.snaptiongame.app.data.providers.api.ApiProvider

import io.reactivex.Completable
import io.reactivex.Single

/**
 * @author Tyler Wong
 */

private val apiService = ApiProvider.getApiService()

fun userOAuthFacebook(request: OAuthRequest): Single<Session> = apiService.userOAuthFacebook(request)

fun userOAuthGoogle(request: OAuthRequest): Single<Session> = apiService.userOAuthGoogle(request)

fun logout(): Completable = apiService.logout()

fun isSessionValid(): Single<Boolean> = apiService.isSessionValid()
