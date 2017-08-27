package com.snaptiongame.app.data.models

import com.google.gson.annotations.SerializedName

/**
 * @author Brian Gouldsberry
 */
class Friend {
    @SerializedName(ID)
    var id: Int = 0
    @SerializedName(FULL_NAME)
    var fullName: String = ""
    @SerializedName(USERNAME)
    var username: String = ""
    var picture: String = ""
    @SerializedName(IMAGE_URL)
    var imageUrl: String? = null
    @SerializedName(IMAGE_WIDTH)
    var imageWidth: Int = 0
    @SerializedName(IMAGE_HEIGHT)
    var imageHeight: Int = 0
    @SerializedName(COVER)
    var cover: String = ""
    @SerializedName(EMAIL)
    var email: String = ""

    var isSnaptionFriend: Boolean = false

    constructor() {

    }

    constructor(id: Int) {
        this.id = id
    }

    constructor(user: User) {
        this.id = user.id
        this.picture = user.picture
        this.username = user.username
        this.fullName = user.fullName
        this.imageWidth = user.imageWidth
        this.imageHeight = user.imageHeight
        this.imageUrl = user.imageUrl
        this.isSnaptionFriend = user.isFriend
    }

    constructor(id: Int, fullName: String, username: String, picture: String,
                cover: String, email: String) {
        this.id = id
        this.fullName = fullName
        this.username = username
        this.picture = picture
        this.cover = cover
        this.email = email
        isSnaptionFriend = true
    }

    override fun equals(other: Any?): Boolean {
        return other is Friend && this.id == other.id
    }

    companion object {
        const val ID = "id"
        const val USERNAME = "username"
        const val FULL_NAME = "fullName"
        const val PICTURE = "picture"
        const val IMAGE_URL = "url"
        const val IMAGE_WIDTH = "width"
        const val IMAGE_HEIGHT = "height"
        const val COVER = "cover"
        const val EMAIL = "email"
    }
}
