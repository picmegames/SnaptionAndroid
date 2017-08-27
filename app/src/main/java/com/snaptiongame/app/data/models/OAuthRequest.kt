package com.snaptiongame.app.data.models

/**
 * @author Tyler Wong
 */

data class OAuthRequest(val token: String, val deviceToken: String, val linkToken: String?) {

    val deviceType: String = ANDROID

    companion object {
        const val TOKEN = "token"
        const val DEVICE_TOKEN = "device_token"
        const val DEVICE_TYPE = "device_type"
        const val ANDROID = "Android"
        const val LINK_TOKEN = "linkToken"
    }
}
