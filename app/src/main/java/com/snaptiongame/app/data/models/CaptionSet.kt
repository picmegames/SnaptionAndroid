package com.snaptiongame.app.data.models


import com.google.gson.annotations.SerializedName


/**
 * @author Nick Romero
 */

class CaptionSet {

    @SerializedName(ID)
    var id: Int = 0
    @SerializedName(TITLE)
    var setName: String? = null
    @SerializedName(ACTIVE)
    var isCaptionSetActive: Boolean = false

    companion object {
        const val TITLE = "title"
        const val ID = "id"
        const val ACTIVE = "active"
        const val MAX_CAPTIONS = 10
        const val MIN_CAPTIONS = 0
    }
}
