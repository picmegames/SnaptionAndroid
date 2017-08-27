package com.snaptiongame.app.data.models

/**
 * @author Tyler Wong
 */

class GameAction(val targetId: Int, val choice: Boolean, val choiceType: String, val type: String) {
    companion object {
        const val GAME_ID = "gameId"
        const val CAPTION_ID = "captionId"
        const val UPVOTE = "upvote"
        const val FLAGGED = "flagged"
    }
}
