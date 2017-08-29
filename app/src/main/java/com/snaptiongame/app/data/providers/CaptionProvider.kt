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

object CaptionProvider {
    private val apiService = ApiProvider.getApiService()

    @JvmStatic
    fun getCaptions(gameId: Int, page: Int): Single<List<Caption>> {
        return apiService.getCaptions(gameId, page)
                .flatMapIterable { captions -> captions }
                .filter { caption -> !caption.beenFlagged }
                .toList()
    }

    @JvmStatic
    fun getFitBCaptions(setId: Int): Observable<List<FitBCaption>> {
        return apiService.getFitBCaptions(setId)
    }

    @JvmStatic
    fun upvoteOrFlagCaption(request: GameAction): Completable {
        return apiService.upvoteOrFlagCaption(request)
    }

    @JvmStatic
    fun addCaption(gameId: Int, caption: Caption): Completable {
        return apiService.addCaption(gameId, caption)
    }

    @JvmStatic
    val captionSets: Observable<List<CaptionSet>>
        get() = apiService.captionSets
}