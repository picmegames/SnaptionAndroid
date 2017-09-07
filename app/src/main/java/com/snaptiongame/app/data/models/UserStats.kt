package com.snaptiongame.app.data.models

import com.google.gson.annotations.SerializedName

/**
 * @author Quang Ngo
 */

class UserStats {
    @SerializedName(RANK)
    var rank: String? = null
    @SerializedName(GAMES_CREATED)
    var gamesCreated: Int = 0
    @SerializedName(HIGHEST_GAME_UPVOTE)
    var highestGameUpvote: Int = 0
    @SerializedName(CAPTIONS_CREATED)
    var captionsCreated: Int = 0
    @SerializedName(CAPTION_UPVOTES)
    var captionUpvotes: Int = 0
    @SerializedName(TOP_CAPTION_COUNT)
    var topCaptionCount: Int = 0
    @SerializedName(EXP)
    var exp: Int = 0

    companion object {
        const val RANK = "rank"
        const val GAMES_CREATED = "gamesCreated"
        const val HIGHEST_GAME_UPVOTE = "highestGameUpvote"
        const val CAPTIONS_CREATED = "captionsCreated"
        const val CAPTION_UPVOTES = "captionUpvotes"
        const val TOP_CAPTION_COUNT = "topCaptionCount"
        const val EXP = "exp"
    }
}
