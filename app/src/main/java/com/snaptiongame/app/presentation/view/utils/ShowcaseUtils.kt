package com.snaptiongame.app.presentation.view.utils

import android.app.Activity
import android.graphics.Point
import android.support.v4.widget.NestedScrollView
import android.view.View

import com.snaptiongame.app.R

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig

/**
 * @author Tyler Wong
 */

object ShowcaseUtils {
    private const val SHOWCASE_DELAY = 750

    @JvmStatic
    fun showShowcase(activity: Activity, target: View, title: Int, content: Int) {
        MaterialShowcaseView.Builder(activity)
                .setTarget(target)
                .setDismissText(R.string.got_it)
                .setTitleText(title)
                .setContentText(content)
                .setDelay(SHOWCASE_DELAY)
                .singleUse(activity.getString(title))
                .show()
    }

    @JvmStatic
    fun showShowcaseSequence(activity: Activity, scrollView: NestedScrollView?, targets: List<View>,
                             titles: List<Int>, contents: List<Int>) {
        val res = activity.resources
        val config = ShowcaseConfig()
        config.delay = SHOWCASE_DELAY.toLong()

        val sequence = MaterialShowcaseSequence(
                activity, res.getString(titles[titles.size - 1]))
        sequence.singleUse(res.getString(titles[titles.size - 1]))
        sequence.setConfig(config)

        for (index in targets.indices) {
            sequence.addSequenceItem(targets[index],
                    res.getString(titles[index]),
                    res.getString(contents[index]),
                    res.getString(R.string.got_it))
        }

        if (scrollView != null) {
            sequence.setOnItemDismissedListener { _: MaterialShowcaseView, index: Int ->
                if (index < targets.size - 1) {
                    val nextView = targets[index + 1]
                    val childOffset = Point()
                    ViewUtils.getDeepChildOffset(scrollView, nextView.parent, nextView, childOffset)
                    scrollView.smoothScrollTo(0, childOffset.y)
                }
            }
        }

        sequence.start()
    }
}
