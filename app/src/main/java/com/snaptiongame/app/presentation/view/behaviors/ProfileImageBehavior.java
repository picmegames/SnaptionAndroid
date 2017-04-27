package com.snaptiongame.app.presentation.view.behaviors;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import com.snaptiongame.app.R;

/**
 * @author Tyler Wong
 */

public class ProfileImageBehavior extends CoordinatorLayout.Behavior<ImageView> {

    private Context mContext;

    private int mCustomFinalHeight;
    private int mStartXPosition;
    private float mStartToolbarPosition;
    private int mStartYPosition;
    private int mFinalYPosition;
    private int mStartHeight;
    private int mFinalXPosition;
    private float mChangeBehaviorPoint;

    private static final int CUSTOM_Y_START_OFFSET = 88;
    private static final int CUSTOM_Y_OFFSET = 12;
    private static final int CUSTOM_X_OFFSET = 30;

    private static final String STATUS_BAR_HEIGHT = "status_bar_height";
    private static final String DIMEN = "dimen";
    private static final String ANDROID = "android";

    public ProfileImageBehavior(Context context, AttributeSet attrs) {
        mContext = context;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, ImageView child, View dependency) {
        return dependency instanceof Toolbar;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, ImageView child, View dependency) {
        maybeInitProperties(child, dependency);

        final int maxScrollDistance = (int) mStartToolbarPosition;
        float expandedPercentageFactor = dependency.getY() / maxScrollDistance;

        if (expandedPercentageFactor < mChangeBehaviorPoint) {
            float heightFactor = (mChangeBehaviorPoint - expandedPercentageFactor) / mChangeBehaviorPoint;

            float distanceXToSubtract = ((mStartXPosition - mFinalXPosition)
                    * heightFactor) + (child.getHeight() / 2);
            float distanceYToSubtract = ((mStartYPosition - mFinalYPosition)
                    * (1f - expandedPercentageFactor)) + (child.getHeight() / 2);

            child.setX(mStartXPosition - distanceXToSubtract);
            child.setY(mStartYPosition - distanceYToSubtract);

            float heightToSubtract = ((mStartHeight - mCustomFinalHeight) * heightFactor);

            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            lp.width = (int) (mStartHeight - heightToSubtract);
            lp.height = (int) (mStartHeight - heightToSubtract);
            child.setLayoutParams(lp);
        }
        else {
            float distanceYToSubtract = ((mStartYPosition - mFinalYPosition)
                    * (1f - expandedPercentageFactor)) + (mStartHeight / 2);

            child.setX(mStartXPosition - child.getWidth() / 2);
            child.setY(mStartYPosition - distanceYToSubtract);

            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            lp.width = mStartHeight;
            lp.height = mStartHeight;
            child.setLayoutParams(lp);
        }
        return true;
    }

    private void maybeInitProperties(ImageView child, View dependency) {
        if (mStartYPosition == 0) {
            mStartYPosition = (int) dependency.getY();
        }

        if (mFinalYPosition == 0) {
            mFinalYPosition = dependency.getHeight() / 2 + CUSTOM_Y_OFFSET;
        }

        if (mStartHeight == 0) {
            mStartHeight = child.getHeight();
        }

        if (mStartXPosition == 0) {
            mStartXPosition = (int) (child.getX() + child.getWidth() / 2);
        }

        if (mFinalXPosition == 0) {
            mFinalXPosition = mContext.getResources().getDimensionPixelOffset(
                    R.dimen.abc_action_bar_content_inset_material) + mCustomFinalHeight / 2 +
                    Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, CUSTOM_X_OFFSET,
                            dependency.getContext().getResources().getDisplayMetrics()));
        }

        if (mStartToolbarPosition == 0) {
            mStartToolbarPosition = dependency.getY();
        }

        if (mChangeBehaviorPoint == 0) {
            mChangeBehaviorPoint = (child.getHeight() - mCustomFinalHeight) / (2f * (mStartYPosition - mFinalYPosition));
        }
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier(STATUS_BAR_HEIGHT, DIMEN, ANDROID);

        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
