package com.snaptiongame.app.data.models

/**
 * @author Brian Gouldsberry
 */

data class GameInvite(var inviteToken: String?, var gameId: Int?) {

    override fun equals(other: Any?): Boolean {
        return other is GameInvite && this.gameId == other.gameId && this.inviteToken == other.inviteToken
    }

    companion object {
        const val INVITE_TOKEN = "inviteToken"
        const val GAME_ID = "gameId"
    }
}
