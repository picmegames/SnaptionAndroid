package com.snaptiongame.app.data.models

/**
 * @author Nick Romero
 */

data class AddFriendRequest(var friendId: Int) {
    companion object {
        const val FRIEND_ID = "friendId"
    }
}
