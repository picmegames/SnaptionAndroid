@file:JvmName("ActivityFeedProvider")

package com.snaptiongame.app.data.providers

import com.snaptiongame.app.data.models.ActivityFeedItem
import com.snaptiongame.app.data.providers.api.ApiProvider

import io.reactivex.Single

/**
 * @author Tyler Wong
 */

private val apiService = ApiProvider.getApiService()

fun getActivityFeed(page: Int): Single<List<ActivityFeedItem>> = apiService.getActivityFeed(page)
