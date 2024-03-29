@file:JvmName("CaptionProvider")

package com.snaptiongame.app.data.providers

import com.snaptiongame.app.data.models.Caption
import com.snaptiongame.app.data.models.CaptionSet
import com.snaptiongame.app.data.models.FitBCaption
import com.snaptiongame.app.data.models.GameAction
import com.snaptiongame.app.data.providers.api.ApiProvider

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * @author Tyler Wong
 */

private val apiService = ApiProvider.getApiService()

fun getCaptions(gameId: Int, page: Int): Single<List<Caption>> =
    apiService.getCaptions(gameId, page)
            .flatMapIterable { captions -> captions }
            .filter { caption -> !caption.beenFlagged }
            .toList()

fun getFitBCaptions(setId: Int): Flowable<List<FitBCaption>> = apiService.getFitBCaptions(setId)

fun upvoteOrFlagCaption(request: GameAction): Completable = apiService.upvoteOrFlagCaption(request)

fun addCaption(gameId: Int, caption: Caption): Completable = apiService.addCaption(gameId, caption)

fun getCaptionSets(): Flowable<List<CaptionSet>> = apiService.getCaptionSets()

fun getAllCaptions(): Flowable<List<FitBCaption>> = getCaptionSets()
        .flatMapIterable { it }
        .flatMap { getFitBCaptions(it.id) }