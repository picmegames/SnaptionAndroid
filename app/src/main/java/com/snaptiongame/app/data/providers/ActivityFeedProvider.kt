package com.snaptiongame.app.data.providers

import com.snaptiongame.app.data.api.SnaptionApi
import com.snaptiongame.app.data.models.ActivityFeedItem
import com.snaptiongame.app.data.providers.api.ApiProvider

import io.reactivex.Single

/**
 * @author Tyler Wong
 */

object ActivityFeedProvider {
    private val apiService = ApiProvider.getApiService()

    @JvmStatic
    fun getActivityFeed(page: Int): Single<List<ActivityFeedItem>> {
        return apiService.getActivityFeed(page)
    }
}
