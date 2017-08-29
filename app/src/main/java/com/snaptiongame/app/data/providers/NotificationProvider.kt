package com.snaptiongame.app.data.providers

import com.snaptiongame.app.data.providers.api.ApiProvider

import io.reactivex.Completable

/**
 * @author Tyler Wong
 */

object NotificationProvider {
    private val apiService = ApiProvider.getApiService()

    @JvmStatic
    fun refreshNotificationToken(deviceToken: String): Completable {
        return apiService.refreshNotificationToken(deviceToken)
    }
}
