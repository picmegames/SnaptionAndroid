@file:JvmName("OfferProvider")

package com.snaptiongame.app.data.providers

import com.snaptiongame.app.data.models.Offer
import com.snaptiongame.app.data.providers.api.ApiProvider
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * @author Tyler Wong
 */

private val apiService = ApiProvider.getApiService()

//fun getAllOffers(): Flowable<Offer> = apiService.getAllOffers()
//        .flatMapIterable { offer -> offer }

fun getAllOffers(): Flowable<Offer> {
    val offer1 = Offer("Special Offer!", true, 10, 10, 100)
    val offer2 = Offer("Soft Offer!", false, 10, 0, 50)
    val offer3 = Offer("Hard Offer!", false, 0, 10, 100)
    return Flowable.just(offer1, offer2, offer3, offer1, offer2, offer3, offer1, offer2, offer3)
}
