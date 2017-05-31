package com.snaptiongame.app.presentation.view.utils;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;

/**
 * @author Tyler Wong
 */

public class TextStyleUtils {
    public static SpannableString getTextUnderlined(String target) {
        SpannableString content = new SpannableString(target);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        return content;
    }

    public static SpannableString getTextBolded(String target) {
        SpannableString content = new SpannableString(target);
        content.setSpan(new StyleSpan(Typeface.BOLD), 0, content.length(), 0);
        return content;
    }

    public static SpannableString getTextGray(String target) {
        SpannableString content = new SpannableString(target);
        content.setSpan(new ForegroundColorSpan(Color.GRAY), 0, content.length(), 0);
        return content;
    }
}
