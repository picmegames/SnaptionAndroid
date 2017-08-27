package com.snaptiongame.app.data.models

import com.google.gson.annotations.SerializedName

/**
 * @author Tyler Wong
 */

class Rank {
    @SerializedName(ID)
    var id: Int = 0
    @SerializedName(TITLE)
    var title: String? = null

    companion object {
        const val ID = "id"
        const val TITLE = "title"
    }
}
