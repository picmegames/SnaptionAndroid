@file:JvmName("DeepLinkProvider")

package com.snaptiongame.app.data.providers

import com.snaptiongame.app.data.models.DeepLinkRequest
import com.snaptiongame.app.data.providers.api.ApiProvider

import io.reactivex.Single

/**
 * @author Brian Gouldsberry
 */

private val apiService = ApiProvider.getApiService()

fun getToken(invite: DeepLinkRequest): Single<String> {
    return apiService.getToken(invite)
}
