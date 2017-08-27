package com.snaptiongame.app.data.models

/**
 * @author Tyler Wong
 */

data class Session(var userId: Int) {
    companion object {
        const val USER = "user_id"
    }
}
