@file:JvmName("NotificationProvider")

package com.snaptiongame.app.data.providers

import com.snaptiongame.app.data.providers.api.ApiProvider

import io.reactivex.Completable

/**
 * @author Tyler Wong
 */

private val apiService = ApiProvider.getApiService()

fun refreshNotificationToken(deviceToken: String): Completable = apiService.refreshNotificationToken(deviceToken)
