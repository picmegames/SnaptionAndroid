package com.snaptiongame.app.data.models

import com.google.gson.annotations.SerializedName

/**
 * @author Tyler Wong
 */
class Caption {

    @SerializedName(ID)
    var id: Int = 0
    @SerializedName(GAME_ID)
    var gameId: Int = 0
    @SerializedName(NUM_VOTES)
    var numVotes: Int = 0
    @SerializedName(USER_ID)
    var creatorId: Int = 0
    @SerializedName(USER_PICTURE)
    var creatorPicture: String? = null
    @SerializedName(USERNAME)
    var creatorName: String? = null
    @SerializedName(FITB_ID_RECEIVE)
    var fitBId: Int = 0
    @SerializedName(CAPTION)
    var caption: String = ""
    @SerializedName(FITB_ID_SEND)
    var fitbIdSend: Int = 0
    @SerializedName(FITB)
    var assocFitB: FitBCaption? = null
    @SerializedName(BEEN_UPVOTED)
    var beenUpvoted: Boolean = false
    @SerializedName(BEEN_FLAGGED)
    var beenFlagged: Boolean = false

    constructor() {

    }

    constructor(fitBId: Int, caption: String) {
        this.fitbIdSend = fitBId
        this.caption = caption
    }

    constructor(id: Int, gameId: Int, numVotes: Int, creatorId: Int, fitBId: Int,
                caption: String, fitb: FitBCaption) {
        this.id = id
        this.gameId = gameId
        this.numVotes = numVotes
        this.creatorId = creatorId
        this.fitBId = fitBId
        this.caption = caption
        this.assocFitB = fitb
    }

    override fun equals(other: Any?): Boolean {
        return other is Caption
                && this.id == other.id
                && this.beenUpvoted == other.beenUpvoted
                && this.beenFlagged == other.beenFlagged
                && this.numVotes == other.numVotes
                && this.creatorName != null && this.creatorName == other.creatorName
                && this.creatorPicture != null && this.creatorPicture == other.creatorPicture
    }

    companion object {
        const val ID = "id"
        const val GAME_ID = "game_id"
        const val NUM_VOTES = "numVotes"
        const val USER_ID = "userId"
        const val USER_PICTURE = "userPic"
        const val PICTURE = "picture"
        const val USERNAME = "username"
        const val CREATOR = "creator"
        const val FITB_ID_RECEIVE = "fitb_id"
        const val FITB_ID_SEND = "fitbId"
        const val CAPTION = "userEntry"
        const val FITB = "fitB"
        const val FITB_OTHER = "fitb"
        const val BEEN_UPVOTED = "beenUpvoted"
        const val BEEN_FLAGGED = "beenFlagged"
    }
}
