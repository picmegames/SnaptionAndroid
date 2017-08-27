package com.snaptiongame.app.data.models

/**
 * @author Tyler Wong
 */

data class OAuthRequest(var token: String, var deviceToken: String, var linkToken: String) {
    var deviceType: String = ANDROID

    companion object {
        const val TOKEN = "token"
        const val DEVICE_TOKEN = "device_token"
        const val DEVICE_TYPE = "device_type"
        const val ANDROID = "Android"
        const val LINK_TOKEN = "linkToken"
    }
}
