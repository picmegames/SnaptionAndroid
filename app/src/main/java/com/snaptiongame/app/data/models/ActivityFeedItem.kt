package com.snaptiongame.app.data.models

import com.google.gson.annotations.SerializedName

/**
 * @author Tyler Wong
 */

data class ActivityFeedItem(@SerializedName(ID) val id: Int,
                            @SerializedName(DATE) val date: Long,
                            @SerializedName(TYPE) val type: Int,
                            @SerializedName(FRIEND) val friend: User?,
                            @SerializedName(GAME) val game: Game?,
                            @SerializedName(CAPTION) val caption: Caption?) {

    companion object {
        const val ID = "id"
        const val DATE = "date"
        const val TYPE = "type"
        const val FRIEND = "friend"
        const val GAME = "game"
        const val CAPTION = "caption"
    }
}
