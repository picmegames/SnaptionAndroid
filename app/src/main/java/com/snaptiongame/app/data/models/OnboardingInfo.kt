package com.snaptiongame.app.data.models

/**
 * @author Tyler Wong
 */

data class OnboardingInfo(val titleId: Int, val descriptionId: Int, val animationId: Int) {
    companion object {
        const val TITLE_ID = "title"
        const val DESCRIPTION_ID = "description"
        const val ANIMATION = "animation"
    }
}
