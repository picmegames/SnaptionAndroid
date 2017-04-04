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
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.v7.graphics.Palette;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

/**
 * Utility methods for working with Views.
 */
public class ViewUtils {

    private ViewUtils() {
    }

    public static RippleDrawable createRipple(@NonNull Palette palette,
                                              @FloatRange(from = 0f, to = 1f) float darkAlpha,
                                              @FloatRange(from = 0f, to = 1f) float lightAlpha,
                                              @ColorInt int fallbackColor,
                                              boolean bounded) {
        int rippleColor = fallbackColor;
        if (palette != null) {
            // try the named swatches in preference order
            if (palette.getVibrantSwatch() != null) {
                rippleColor =
                        ColorUtils.modifyAlpha(palette.getVibrantSwatch().getRgb(), darkAlpha);

            }
            else if (palette.getLightVibrantSwatch() != null) {
                rippleColor = ColorUtils.modifyAlpha(palette.getLightVibrantSwatch().getRgb(),
                        lightAlpha);
            }
            else if (palette.getDarkVibrantSwatch() != null) {
                rippleColor = ColorUtils.modifyAlpha(palette.getDarkVibrantSwatch().getRgb(),
                        darkAlpha);
            }
            else if (palette.getMutedSwatch() != null) {
                rippleColor = ColorUtils.modifyAlpha(palette.getMutedSwatch().getRgb(), darkAlpha);
            }
            else if (palette.getLightMutedSwatch() != null) {
                rippleColor = ColorUtils.modifyAlpha(palette.getLightMutedSwatch().getRgb(),
                        lightAlpha);
            }
            else if (palette.getDarkMutedSwatch() != null) {
                rippleColor =
                        ColorUtils.modifyAlpha(palette.getDarkMutedSwatch().getRgb(), darkAlpha);
            }
        }
        return new RippleDrawable(ColorStateList.valueOf(rippleColor), null,
                bounded ? new ColorDrawable(Color.WHITE) : null);
    }

    public static void setLightStatusBar(@NonNull View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
        }
    }

    public static void clearLightStatusBar(@NonNull View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
        }
    }
}

