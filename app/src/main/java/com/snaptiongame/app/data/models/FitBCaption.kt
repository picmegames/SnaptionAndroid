package com.snaptiongame.app.data.models

import com.google.gson.annotations.SerializedName


/**
 * @author Nick Romero
 */

class FitBCaption(@SerializedName(ID) var id: Int,
                  @SerializedName(CHAR_LIMIT) var charLimit: Int,
                  @SerializedName(BEFORE) var beforeBlank: String,
                  @SerializedName(AFTER) var afterBlank: String,
                  @SerializedName(SET_ID) var setId: Int) {

    @SerializedName(USER_ENTRY)
    var userEntry: String = ""

    override fun equals(other: Any?): Boolean {
        return other is FitBCaption && other.id == id
    }

    companion object {
        const val ID = "id"
        const val BEFORE = "before"
        const val AFTER = "after"
        const val CHAR_LIMIT = "charLimit"
        const val SET_ID = "set_id"
        const val USER_ENTRY = "userEntry"
        const val PLACEHOLDER = "______"
    }
}
