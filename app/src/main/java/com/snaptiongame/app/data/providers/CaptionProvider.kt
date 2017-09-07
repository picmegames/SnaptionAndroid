@file:JvmName("CaptionProvider")

package com.snaptiongame.app.data.providers

import com.snaptiongame.app.data.models.Caption
import com.snaptiongame.app.data.models.CaptionSet
import com.snaptiongame.app.data.models.FitBCaption
import com.snaptiongame.app.data.models.GameAction
import com.snaptiongame.app.data.providers.api.ApiProvider

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

/**
 * @author Tyler Wong
 */

private val apiService = ApiProvider.getApiService()

fun getCaptions(gameId: Int, page: Int): Single<List<Caption>> {
    return apiService.getCaptions(gameId, page)
            .flatMapIterable { captions -> captions }
            .filter { caption -> !caption.beenFlagged }
            .toList()
}

fun getFitBCaptions(setId: Int): Observable<List<FitBCaption>> {
    return apiService.getFitBCaptions(setId)
}

fun upvoteOrFlagCaption(request: GameAction): Completable {
    return apiService.upvoteOrFlagCaption(request)
}

fun addCaption(gameId: Int, caption: Caption): Completable {
    return apiService.addCaption(gameId, caption)
}

val captionSets: Observable<List<CaptionSet>>
    get() = apiService.captionSets
