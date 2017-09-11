package com.snaptiongame.app.presentation.view.utils

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

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable

import com.bumptech.glide.load.resource.gif.GifDrawable

/**
 * Utility methods for working with Glide.
 */
object GlideUtils {
    @JvmStatic
    fun getBitmap(glideDrawable: Drawable): Bitmap? {
        if (glideDrawable is BitmapDrawable) {
            return glideDrawable.bitmap
        }
        else if (glideDrawable is GifDrawable) {
            return glideDrawable.firstFrame
        }
        return null
    }
}

