package com.snaptiongame.app.presentation.view.settings;

import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

/**
 * @author Nick Romero
 */

public final class EasterEgg {
    private int puffinCounter = 0;

    private static final int PUFFIN_THRESHOLD = 6;
    private static final int ONE_SECOND = 1000;
    private static final int TRANSPARENT = 0;
    private static final int SOLID = 1;

    public void update(ImageView puffinLogo) {

        if (++puffinCounter == PUFFIN_THRESHOLD) {
            puffinCounter = 0;

            puffinLogo.setVisibility(View.INVISIBLE);
            puffinLogo.bringToFront();
            Animation fadeIn = new AlphaAnimation(TRANSPARENT, SOLID);
            fadeIn.setInterpolator(new DecelerateInterpolator());
            fadeIn.setDuration(ONE_SECOND);

            Animation fadeOut = new AlphaAnimation(SOLID, TRANSPARENT);
            fadeOut.setInterpolator(new AccelerateInterpolator());
            fadeOut.setStartOffset(ONE_SECOND);
            fadeOut.setDuration(ONE_SECOND);

            AnimationSet animation = new AnimationSet(false);
            animation.addAnimation(fadeIn);
            animation.addAnimation(fadeOut);
            puffinLogo.setAnimation(animation);
        }
    }
}
