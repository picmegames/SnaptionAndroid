package com.snaptiongame.app.data.models

import com.google.gson.annotations.SerializedName

/**
 * @author Tyler Wong
 */

class User {

    @SerializedName(ID)
    var id: Int = 0
    @SerializedName(USERNAME)
    var username: String? = null
    @SerializedName(FULL_NAME)
    var fullName: String = ""
    @SerializedName(PICTURE)
    var picture: String? = null
    @SerializedName(IMAGE_URL)
    var imageUrl: String? = null
    @SerializedName(IMAGE_WIDTH)
    var imageWidth: Int = 0
    @SerializedName(IMAGE_HEIGHT)
    var imageHeight: Int = 0
    @SerializedName(TYPE)
    var type: String = ""
    @SerializedName(IS_FRIEND)
    var isFriend: Boolean = false

    constructor()

    //Convert a Friend to a User
    constructor(friend: Friend) {
        this.id = friend.id
        this.picture = friend.picture
        this.username = friend.username
        this.imageWidth = friend.imageWidth
        this.imageHeight = friend.imageHeight
        this.imageUrl = friend.imageUrl
    }

    constructor(username: String) {
        this.username = username
    }

    constructor(picture: String, type: String) {
        this.picture = picture
        this.type = type
    }

    override fun equals(other: Any?): Boolean {
        return other is User && this.id == other.id
    }

    companion object {
        const val ID = "id"
        const val USERNAME = "username"
        const val FULL_NAME = "fullName"
        const val PICTURE = "picture"
        const val IMAGE_URL = "url"
        const val IMAGE_WIDTH = "width"
        const val IMAGE_HEIGHT = "height"
        const val TYPE = "imgType"
        const val IS_FRIEND = "isFriend"
    }
}
