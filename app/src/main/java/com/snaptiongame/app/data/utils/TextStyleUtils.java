package com.snaptiongame.app.data.utils;

import android.text.SpannableString;
import android.text.style.UnderlineSpan;

import com.hootsuite.nachos.NachoTextView;

/**
 * @author Tyler Wong
 */

public class TextStyleUtils {
    public static SpannableString getTextUnderlined(String target) {
        SpannableString content = new SpannableString(target);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        return content;
    }
}
