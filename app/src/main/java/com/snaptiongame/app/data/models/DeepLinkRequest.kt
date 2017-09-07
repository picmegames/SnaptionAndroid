package com.snaptiongame.app.data.models

import com.google.gson.annotations.SerializedName

/**
 * @author Brian Gouldsberry
 */

class DeepLinkRequest {
    @SerializedName(GAME_ID)
    var gameId: Int = 0
    @SerializedName(EMAIL)
    var email: String? = null
    @SerializedName(PHONE)
    var phone: String? = null
    @SerializedName(FACEBOOK_ID)
    var facebookId: String? = null
    @SerializedName(GOOGLE_ID)
    var googleId: String? = null

    constructor(gameId: Int) {
        this.gameId = gameId
        this.email = null
        this.phone = null
        this.facebookId = null
        this.googleId = null
    }

    constructor(gameId: Int, email: String, phone: String, facebookId: String, googleId: String) {
        this.gameId = gameId
        this.email = email
        this.phone = phone
        this.facebookId = facebookId
        this.googleId = googleId
    }

    companion object {
        const val GAME_ID = "gameId"
        const val EMAIL = "email"
        const val PHONE = "phone"
        const val FACEBOOK_ID = "facebookId"
        const val GOOGLE_ID = "googleId"
    }
}
