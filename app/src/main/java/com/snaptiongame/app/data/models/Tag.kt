package com.snaptiongame.app.data.models

import com.google.gson.annotations.SerializedName

/**
 * @author Tyler Wong
 */

data class Tag (@SerializedName(ID) var id: Int = 0,
                @SerializedName(NAME) var name: String? = null) {
    companion object {
        const val ID = "id"
        const val NAME = "name"
    }
}
