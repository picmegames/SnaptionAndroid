package com.snaptiongame.app.data.models

/**
 * @author Brian Gouldsberry
 */

data class GameInvite(val inviteToken: String?, val gameId: Int?) {
    companion object {
        const val INVITE_TOKEN = "inviteToken"
        const val GAME_ID = "gameId"
    }
}
