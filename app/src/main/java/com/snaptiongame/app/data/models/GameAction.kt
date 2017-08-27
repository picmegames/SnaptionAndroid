package com.snaptiongame.app.data.models

/**
 * @author Tyler Wong
 */

class GameAction(var targetId: Int, var choice: Boolean, var choiceType: String, var type: String) {
    companion object {
        const val GAME_ID = "gameId"
        const val CAPTION_ID = "captionId"
        const val UPVOTE = "upvote"
        const val FLAGGED = "flagged"
    }
}
