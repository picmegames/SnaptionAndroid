package com.snaptiongame.app.presentation.view.utils;

/*
 * Copyright 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;

/**
 * Utility methods for working with animations.
 */
public class AnimUtils {

    private static Interpolator fastOutSlowIn;
    private static ScaleAnimation growAnim;
    private static ScaleAnimation shrinkAnim;

    private static final long ANIMATION_DURATION = 150;

    private static final float GONE_SIZE = 0f;
    private static final float HALF_SIZE = 0.5f;
    private static final float FULL_SIZE = 1.0f;

    public static Interpolator getFastOutSlowInInterpolator(Context context) {
        if (fastOutSlowIn == null) {
            fastOutSlowIn = AnimationUtils.loadInterpolator(context,
                    android.R.interpolator.fast_out_slow_in);
        }
        return fastOutSlowIn;
    }

    public static ScaleAnimation getGrowAnim() {
        growAnim = new ScaleAnimation(GONE_SIZE, FULL_SIZE, GONE_SIZE, FULL_SIZE,
                Animation.RELATIVE_TO_SELF, HALF_SIZE, Animation.RELATIVE_TO_SELF, HALF_SIZE);
        growAnim.setDuration(ANIMATION_DURATION);
        return growAnim;
    }

    public static ScaleAnimation getShrinkAnim() {
        shrinkAnim = new ScaleAnimation(FULL_SIZE, GONE_SIZE, FULL_SIZE, GONE_SIZE,
                Animation.RELATIVE_TO_SELF, HALF_SIZE, Animation.RELATIVE_TO_SELF, HALF_SIZE);
        shrinkAnim.setDuration(ANIMATION_DURATION);
        return shrinkAnim;
    }
}
