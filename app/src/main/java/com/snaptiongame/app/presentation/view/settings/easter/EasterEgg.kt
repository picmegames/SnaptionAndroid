package com.snaptiongame.app.presentation.view.settings.easter

import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView

/**
 * @author Nick Romero
 */

class EasterEgg {
    private var puffinCounter = 0

    fun update(puffinLogo: ImageView) {

        if (++puffinCounter == PUFFIN_THRESHOLD) {
            puffinCounter = 0

            puffinLogo.visibility = View.INVISIBLE
            puffinLogo.bringToFront()
            val fadeIn = AlphaAnimation(TRANSPARENT.toFloat(), SOLID.toFloat())
            fadeIn.interpolator = DecelerateInterpolator()
            fadeIn.duration = ONE_SECOND.toLong()

            val fadeOut = AlphaAnimation(SOLID.toFloat(), TRANSPARENT.toFloat())
            fadeOut.interpolator = AccelerateInterpolator()
            fadeOut.startOffset = ONE_SECOND.toLong()
            fadeOut.duration = ONE_SECOND.toLong()

            val animation = AnimationSet(false)
            animation.addAnimation(fadeIn)
            animation.addAnimation(fadeOut)
            puffinLogo.animation = animation
        }
    }

    companion object {
        private const val PUFFIN_THRESHOLD = 6
        private const val ONE_SECOND = 1000
        private const val TRANSPARENT = 0
        private const val SOLID = 1
    }
}
