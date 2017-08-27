package com.snaptiongame.app.data.models

import com.google.gson.annotations.SerializedName

/**
 * @author Tyler Wong
 */
class Game {

    @SerializedName(ID)
    var id: Int = 0
    @SerializedName(USER_ID)
    var userId: Int = 0
    @SerializedName(START_DATE)
    var startDate: Long = 0
    @SerializedName(END_DATE)
    var endDate: Long = 0
    @SerializedName(IS_PUBLIC)
    var isPublic: Boolean = false
    @SerializedName(IMG_TYPE)
    var type: String? = null
    @SerializedName(CREATOR_NAME)
    var creatorName: String? = null
    @SerializedName(IMAGE_URL)
    var creatorImage: String? = null
    @SerializedName(CREATOR_ID)
    var creatorId: Int = 0
    @SerializedName(PICTURE)
    var picture: String? = null
    @SerializedName(IMAGE_URL)
    var imageUrl: String? = null
    @SerializedName(IMAGE_WIDTH)
    var imageWidth: Int = 0
    @SerializedName(IMAGE_HEIGHT)
    var imageHeight: Int = 0
    @SerializedName(TOP_CAPTION)
    var topCaption: Caption? = null
    @SerializedName(USERS)
    var users: List<User>? = null
    @SerializedName(TAGS)
    var tags: List<Tag>? = null
    var sendTags: List<String>? = null
    @SerializedName(FRIENDS)
    var friendIds: List<Int>? = null
    @SerializedName(BEEN_UPVOTED)
    var beenUpvoted: Boolean = false
    @SerializedName(BEEN_FLAGGED)
    var beenFlagged: Boolean = false
    @SerializedName(GAME_DURATION)
    var gameDuration: Long = 0
    @SerializedName(NUM_UPVOTES)
    var numUpvotes: Int = 0
    @SerializedName(IS_CLOSED)
    var isClosed: Boolean = false
    @SerializedName(GAME_ID)
    var gameId: Int = 0

    var isFromAnotherGame: Boolean = false

    constructor() {

    }

    // Constructor for creating Game with new picture
    constructor(isPublic: Boolean, picture: String, type: String, tags: List<String>,
                friendIds: List<Int>, gameDuration: Long) {
        this.isPublic = isPublic
        this.picture = picture
        this.type = type
        this.sendTags = tags
        this.friendIds = friendIds
        this.gameDuration = gameDuration
        this.isFromAnotherGame = false
    }

    // Constructor for creating Game from gameId
    constructor(gameId: Int, isPublic: Boolean, tags: List<String>, friendIds: List<Int>,
                gameDuration: Long) {
        this.gameId = gameId
        this.isPublic = isPublic
        this.sendTags = tags
        this.friendIds = friendIds
        this.gameDuration = gameDuration
        this.isFromAnotherGame = true
    }

    // Constructor for Game from server
    constructor(id: Int, startDate: Long, isPublic: Boolean, creatorId: Int, picture: String, type: String) {
        this.id = id
        this.startDate = startDate
        this.isPublic = isPublic
        this.creatorId = creatorId
        this.picture = picture
        this.type = type
    }

    override fun equals(other: Any?): Boolean {
        return other is Game
                && this.id == other.id
                && this.beenUpvoted == other.beenUpvoted
                && this.beenFlagged == other.beenFlagged
                && (this.topCaption == null && other.topCaption == null || this.topCaption != null
                && this.topCaption == other.topCaption)
    }

    companion object {
        const val ID = "id"
        const val USER_ID = "userId"
        const val START_DATE = "creationDate"
        const val END_DATE = "completedDate"
        const val IS_PUBLIC = "isPublic"
        const val PICKER = "picker"
        const val CREATOR_ID = "creator_id"
        const val CREATOR_NAME = "picker_name"
        const val IMG_TYPE = "imgType"
        const val PICTURE = "picture"
        const val IMAGE_URL = "url"
        const val IMAGE_WIDTH = "width"
        const val IMAGE_HEIGHT = "height"
        const val TOP_CAPTION = "topCaption"
        const val FITB_BEFORE = "fitbBefore"
        const val FITB_AFTER = "fitbAfter"
        const val TAGS = "tags"
        const val FRIENDS = "invitedUsers"
        const val USERS = "users"
        const val BEEN_UPVOTED = "beenUpvoted"
        const val BEEN_FLAGGED = "beenFlagged"
        const val GAME_DURATION = "gameDuration"
        const val NUM_UPVOTES = "numUpvotes"
        const val CREATOR_IMAGE = "creatorImage"
        const val IS_CLOSED = "isClosed"
        const val GAME_ID = "gameId"
    }
}
