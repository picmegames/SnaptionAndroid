package com.snaptiongame.app.presentation.view.utils

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.transition.TransitionInflater
import android.view.Window

import com.snaptiongame.app.R

/**
 * @author Tyler Wong
 */

object TransitionUtils {
    private const val ARC_DURATION: Long = 350

    @JvmStatic
    fun setupArcTransition(activity: AppCompatActivity, window: Window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val transition = TransitionInflater
                    .from(activity)
                    .inflateTransition(R.transition.arc_motion_transition)
            transition.duration = ARC_DURATION
            window.sharedElementEnterTransition = transition
        }
    }
}
