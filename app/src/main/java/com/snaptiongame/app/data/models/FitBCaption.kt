package com.snaptiongame.app.data.models

import com.google.gson.annotations.SerializedName


/**
 * @author Nick Romero
 */

class FitBCaption(@SerializedName(ID) val id: Int,
                  @SerializedName(CHAR_LIMIT) val charLimit: Int,
                  @SerializedName(BEFORE) val beforeBlank: String,
                  @SerializedName(AFTER) val afterBlank: String,
                  @SerializedName(SET_ID) val setId: Int) {

    override fun equals(other: Any?): Boolean {
        return other is FitBCaption && other.id == id
    }

    companion object {
        const val ID = "id"
        const val BEFORE = "before"
        const val AFTER = "after"
        const val CHAR_LIMIT = "charLimit"
        const val SET_ID = "set_id"
        const val PLACEHOLDER = "______"
    }
}
