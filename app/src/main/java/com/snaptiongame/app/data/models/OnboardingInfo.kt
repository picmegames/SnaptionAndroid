package com.snaptiongame.app.data.models

/**
 * @author Tyler Wong
 */

data class OnboardingInfo(var titleId: Int, var descriptionId: Int, var animationId: Int) {
    companion object {
        const val TITLE_ID = "title"
        const val DESCRIPTION_ID = "description"
        const val ANIMATION = "animation"
    }
}
