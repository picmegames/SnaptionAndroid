package com.snaptiongame.app.presentation.view.utils;

import android.app.Activity;
import android.view.View;

import com.snaptiongame.app.R;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

/**
 * @author Tyler Wong
 */

public class ShowcaseUtils {
    private static final int SHOWCASE_DELAY = 1000;

    public static void showShowcase(Activity activity, View target, int title, int content) {
        new MaterialShowcaseView.Builder(activity)
                .setTarget(target)
                .setDismissText(R.string.got_it)
                .setTitleText(title)
                .setContentText(content)
                .setDelay(SHOWCASE_DELAY)
                .singleUse(activity.getString(title))
                .show();
    }
}
