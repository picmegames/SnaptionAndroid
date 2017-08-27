package com.snaptiongame.app.data.models

/**
 * @author Nick Romero
 */

data class AddFriendRequest(val friendId: Int) {
    companion object {
        const val FRIEND_ID = "friendId"
    }
}
