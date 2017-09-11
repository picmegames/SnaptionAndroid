package com.snaptiongame.app.presentation.view.utils

import android.graphics.Color
import android.graphics.Typeface
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan

/**
 * @author Tyler Wong
 */

object TextStyleUtils {
    @JvmStatic
    fun getTextUnderlined(target: String): SpannableString {
        val content = SpannableString(target)
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        return content
    }

    @JvmStatic
    fun getTextBolded(target: String): SpannableString {
        val content = SpannableString(target)
        content.setSpan(StyleSpan(Typeface.BOLD), 0, content.length, 0)
        return content
    }

    @JvmStatic
    fun getTextGray(target: String): SpannableString {
        val content = SpannableString(target)
        content.setSpan(ForegroundColorSpan(Color.GRAY), 0, content.length, 0)
        return content
    }
}
