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

    private Context context;

    private int customFinalHeight;
    private int startXPosition;
    private float startToolbarPosition;
    private int startYPosition;
    private int finalYPosition;
    private int startHeight;
    private int finalXPosition;
    private float changeBehaviorPoint;

    private static final int CUSTOM_Y_OFFSET = 12;
    private static final int CUSTOM_X_OFFSET = 30;

    private static final String STATUS_BAR_HEIGHT = "status_bar_height";
    private static final String DIMEN = "dimen";
    private static final String ANDROID = "android";

    public ProfileImageBehavior(Context context, AttributeSet attrs) {
        this.context = context;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, ImageView child, View dependency) {
        return dependency instanceof Toolbar;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, ImageView child, View dependency) {
        maybeInitProperties(child, dependency);

        final int maxScrollDistance = (int) startToolbarPosition;
        float expandedPercentageFactor = dependency.getY() / maxScrollDistance;

        if (expandedPercentageFactor < changeBehaviorPoint) {
            float heightFactor = (changeBehaviorPoint - expandedPercentageFactor) / changeBehaviorPoint;

            float distanceXToSubtract = ((startXPosition - finalXPosition)
                    * heightFactor) + (child.getHeight() / 2);
            float distanceYToSubtract = ((startYPosition - finalYPosition)
                    * (1f - expandedPercentageFactor)) + (child.getHeight() / 2);

            child.setX(startXPosition - distanceXToSubtract);
            child.setY(startYPosition - distanceYToSubtract);

            float heightToSubtract = ((startHeight - customFinalHeight) * heightFactor);

            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            lp.width = (int) (startHeight - heightToSubtract);
            lp.height = (int) (startHeight - heightToSubtract);
            child.setLayoutParams(lp);
        }
        else {
            float distanceYToSubtract = ((startYPosition - finalYPosition)
                    * (1f - expandedPercentageFactor)) + (startHeight / 2);

            child.setX(startXPosition - child.getWidth() / 2);
            child.setY(startYPosition - distanceYToSubtract);

            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            lp.width = startHeight;
            lp.height = startHeight;
            child.setLayoutParams(lp);
        }
        return true;
    }

    private void maybeInitProperties(ImageView child, View dependency) {
        if (startYPosition == 0) {
            startYPosition = (int) dependency.getY();
        }

        if (finalYPosition == 0) {
            finalYPosition = dependency.getHeight() / 2 + CUSTOM_Y_OFFSET;
        }

        if (startHeight == 0) {
            startHeight = child.getHeight();
        }

        if (startXPosition == 0) {
            startXPosition = (int) (child.getX() + child.getWidth() / 2);
        }

        if (finalXPosition == 0) {
            finalXPosition = context.getResources().getDimensionPixelOffset(
                    R.dimen.abc_action_bar_content_inset_material) + customFinalHeight / 2 +
                    Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, CUSTOM_X_OFFSET,
                            dependency.getContext().getResources().getDisplayMetrics()));
        }

        if (startToolbarPosition == 0) {
            startToolbarPosition = dependency.getY();
        }

        if (changeBehaviorPoint == 0) {
            changeBehaviorPoint = (child.getHeight() - customFinalHeight) / (2f * (startYPosition - finalYPosition));
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
