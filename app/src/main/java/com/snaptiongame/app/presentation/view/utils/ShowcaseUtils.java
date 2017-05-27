package com.snaptiongame.app.presentation.view.utils;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Point;
import android.support.v4.widget.NestedScrollView;
import android.view.View;

import com.snaptiongame.app.R;

import java.util.List;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

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

    public static void showShowcaseSequence(Activity activity, NestedScrollView scrollView, List<View> targets,
                                            List<Integer> titles, List<Integer> contents) {
        Resources res = activity.getResources();
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(SHOWCASE_DELAY);

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(
                activity, res.getString(titles.get(0)));
        sequence.setConfig(config);

        for (int index = 0; index < targets.size(); index++) {
            sequence.addSequenceItem(targets.get(index),
                    res.getString(titles.get(index)),
                    res.getString(contents.get(index)),
                    res.getString(R.string.got_it));
        }

        if (scrollView != null) {
            sequence.setOnItemDismissedListener((MaterialShowcaseView materialShowcaseView, int index) -> {
                if (index < targets.size() - 1) {
                    View nextView = targets.get(index + 1);
                    Point childOffset = new Point();
                    ViewUtils.getDeepChildOffset(scrollView, nextView.getParent(), nextView, childOffset);
                    scrollView.smoothScrollTo(0, childOffset.y);
                }
            });
        }

        sequence.start();
    }
}
