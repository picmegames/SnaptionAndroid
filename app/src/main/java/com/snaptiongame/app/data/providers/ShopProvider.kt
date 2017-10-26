package com.snaptiongame.app.data.providers

import com.snaptiongame.app.data.models.Offer
import com.snaptiongame.app.data.providers.api.ApiProvider
import io.reactivex.Single

/**
 * @author Tyler Wong
 */

private val apiService = ApiProvider.getApiService()

fun getOffers(): Single<List<Offer>> = apiService.getOffers()
