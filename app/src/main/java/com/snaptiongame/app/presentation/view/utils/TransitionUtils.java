package com.snaptiongame.app.presentation.view.utils;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.Window;

import com.snaptiongame.app.R;

/**
 * @author Tyler Wong
 */

public class TransitionUtils {

    private static final long ARC_DURATION = 350;

    public static void setupArcTransition(AppCompatActivity activity, Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transition transition = TransitionInflater
                    .from(activity)
                    .inflateTransition(R.transition.arc_motion_transition);
            transition.setDuration(ARC_DURATION);
            window.setSharedElementEnterTransition(transition);
        }
    }
}
