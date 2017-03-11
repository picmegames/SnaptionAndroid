package com.snaptiongame.app.presentation.view.main;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;

import timber.log.Timber;

/**
 * @author Tyler Wong
 */
@SuppressWarnings("unused")
public final class FABScrollBehavior<V extends View> extends VerticalScrollingBehavior<V> {
    private static final Interpolator INTERPOLATOR = new LinearOutSlowInInterpolator();
    private final ViewWithSnackbar mWithSnackBarImpl = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ?
            new LollipopFABWithSnackBarImpl() : new PreLollipopFABWithSnackBarImpl();
    private int mTabLayoutId;
    private boolean hidden = false;
    private ViewPropertyAnimatorCompat mOffsetValueAnimator;
    private View mLayout;
    private View mTabsHolder;
    private int mSnackbarHeight = -1;
    private boolean scrollingEnabled = true;
    private boolean hideAlongSnackbar = false;
    private int bottomBarHeight;

    int[] attrsArray = new int[]{android.R.attr.id};

    private static final int FAB_SPEED = 200;
    private static final int BOTTOM_MARGIN = 16;
    private static final int BOTTOM_BAR_HEIGHT = 56;

    public FABScrollBehavior() {
        super();
    }

    public FABScrollBehavior(Context context, AttributeSet attrs, boolean isWall) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, attrsArray);
        mTabLayoutId = a.getResourceId(0, View.NO_ID);
        a.recycle();

        if (isWall) {
            bottomBarHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, BOTTOM_MARGIN + BOTTOM_BAR_HEIGHT,
                    context.getResources().getDisplayMetrics());
        }
        else {
            bottomBarHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, BOTTOM_MARGIN,
                    context.getResources().getDisplayMetrics());
        }
    }

    public static <V extends View> FABScrollBehavior<V> from(@NonNull V view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (!(params instanceof CoordinatorLayout.LayoutParams)) {
            Timber.e("The view is not a child of CoordinatorLayout");
        }
        CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) params).getBehavior();
        if (!(behavior instanceof FABScrollBehavior)) {
            Timber.e("The view is not associated with FABScrollBehavior");
        }
        return (FABScrollBehavior<V>) behavior;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, V child, View dependency) {
        mWithSnackBarImpl.updateSnackbar(parent, dependency, child);
        return dependency instanceof Snackbar.SnackbarLayout;
    }

    @Override
    public void onDependentViewRemoved(CoordinatorLayout parent, V child, View dependency) {
        updateScrollingForSnackbar(dependency, child, true);
        super.onDependentViewRemoved(parent, child, dependency);
    }

    private void updateScrollingForSnackbar(View dependency, V child, boolean enabled) {
        if (dependency instanceof Snackbar.SnackbarLayout) {
            scrollingEnabled = enabled;
            if (!hideAlongSnackbar && ViewCompat.getTranslationY(child) != 0) {
                ViewCompat.setTranslationY(child, 0);
                hidden = false;
                hideAlongSnackbar = true;
            }
            else if (hideAlongSnackbar) {
                hidden = true;
                animateOffset(child, -child.getHeight());
            }
        }
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, V child, View dependency) {
        updateScrollingForSnackbar(dependency, child, false);
        return super.onDependentViewChanged(parent, child, dependency);
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, V child, int layoutDirection) {
        boolean layoutChild = super.onLayoutChild(parent, child, layoutDirection);
        if (mLayout == null && mTabLayoutId != View.NO_ID) {
            mLayout = findTabLayout(child);
            getTabsHolder();
        }

        return layoutChild;
    }

    @Nullable
    private View findTabLayout(@NonNull View child) {
        if (mTabLayoutId == 0) {
            return null;
        }
        return child.findViewById(mTabLayoutId);
    }

    @Override
    public void onNestedVerticalOverScroll(CoordinatorLayout coordinatorLayout, V child,
                                           @ScrollDirection int direction, int currentOverScroll,
                                           int totalOverScroll) {
    }

    @Override
    public void onDirectionNestedPreScroll(CoordinatorLayout coordinatorLayout, V child,
                                           View target, int dx, int dy, int[] consumed,
                                           @ScrollDirection int scrollDirection) {
        handleDirection(child, scrollDirection);
    }

    private void handleDirection(V child, @ScrollDirection int scrollDirection) {
        if (!scrollingEnabled) {
            return;
        }
        if (scrollDirection == ScrollDirection.SCROLL_DIRECTION_DOWN && hidden) {
            hidden = false;
            animateOffset(child, 0);
        }
        else if (scrollDirection == ScrollDirection.SCROLL_DIRECTION_UP && !hidden) {
            hidden = true;
            animateOffset(child, child.getHeight() + bottomBarHeight);
        }
    }

    @Override
    protected boolean onNestedDirectionFling(CoordinatorLayout coordinatorLayout, V child, View target,
                                             float velocityX, float velocityY, @ScrollDirection int scrollDirection) {
        handleDirection(child, scrollDirection);
        return true;
    }

    private void animateOffset(final V child, final int offset) {
        ensureOrCancelAnimator(child);
        mOffsetValueAnimator.translationY(offset).start();
        animateTabsHolder(offset);
    }

    private void animateTabsHolder(int offset) {
        if (mTabsHolder != null) {
            ViewCompat.animate(mTabsHolder).setDuration(FAB_SPEED).start();
        }
    }

    private void ensureOrCancelAnimator(V child) {
        if (mOffsetValueAnimator == null) {
            mOffsetValueAnimator = ViewCompat.animate(child);
            mOffsetValueAnimator.setDuration(FAB_SPEED);
            mOffsetValueAnimator.setInterpolator(INTERPOLATOR);
        }
        else {
            mOffsetValueAnimator.cancel();
        }
    }

    private void getTabsHolder() {
        if (mLayout != null) {
            mTabsHolder = mLayout;
        }
    }

    public boolean isScrollingEnabled() {
        return scrollingEnabled;
    }

    public void setScrollingEnabled(boolean scrollingEnabled) {
        this.scrollingEnabled = scrollingEnabled;
    }

    public void setHidden(V view, boolean bottomLayoutHidden) {
        if (!bottomLayoutHidden && hidden) {
            animateOffset(view, 0);
        }
        else if (bottomLayoutHidden && !hidden) {
            animateOffset(view, -view.getHeight());
        }
        hidden = bottomLayoutHidden;
    }

    private interface ViewWithSnackbar {
        void updateSnackbar(CoordinatorLayout parent, View dependency, View child);
    }

    private class PreLollipopFABWithSnackBarImpl implements ViewWithSnackbar {
        @Override
        public void updateSnackbar(CoordinatorLayout parent, View dependency, View child) {
            if (dependency instanceof Snackbar.SnackbarLayout) {
                if (mSnackbarHeight == -1) {
                    mSnackbarHeight = dependency.getHeight();
                }

                int targetPadding = child.getMeasuredHeight();

                int shadow = (int) ViewCompat.getElevation(child);
                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) dependency.getLayoutParams();
                layoutParams.bottomMargin = targetPadding - shadow;
                child.bringToFront();
            }
        }
    }

    private class LollipopFABWithSnackBarImpl implements ViewWithSnackbar {
        @Override
        public void updateSnackbar(CoordinatorLayout parent, View dependency, View child) {
            if (dependency instanceof Snackbar.SnackbarLayout) {
                if (mSnackbarHeight == -1) {
                    mSnackbarHeight = dependency.getHeight();
                }
                int targetPadding = (mSnackbarHeight + child.getMeasuredHeight());
                dependency.setPadding(dependency.getPaddingLeft(),
                        dependency.getPaddingTop(), dependency.getPaddingRight(), targetPadding
                );
            }
        }
    }
}
