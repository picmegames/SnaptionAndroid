package com.snaptiongame.app.presentation.view.behaviors;

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
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;

import timber.log.Timber;

/**
 * @author Tyler Wong
 */
@SuppressWarnings("unused")
public final class BottomNavigationScrollBehavior<V extends View> extends VerticalScrollingBehavior<V> {

    private int tabLayoutId;
    private boolean hidden = false;
    private ViewPropertyAnimatorCompat offsetValueAnimator;
    private View layout;
    private View tabsHolder;
    private int snackbarHeight = -1;
    private boolean scrollingEnabled = true;
    private boolean hideAlongSnackbar = false;

    private final ViewWithSnackbar withSnackbarImpl = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ?
            new LollipopBottomNavWithSnackBarImpl() : new PreLollipopBottomNavWithSnackBarImpl();

    private static final Interpolator INTERPOLATOR = new LinearOutSlowInInterpolator();
    private static final int BOTTOM_NAV_SPEED = 500;

    public BottomNavigationScrollBehavior() {
        super();
    }

    public BottomNavigationScrollBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, new int[]{android.R.attr.id});
        tabLayoutId = a.getResourceId(0, View.NO_ID);
        a.recycle();
    }

    public static <V extends View> BottomNavigationScrollBehavior<V> from(@NonNull V view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (!(params instanceof CoordinatorLayout.LayoutParams)) {
            Timber.e("The view is not a child of CoordinatorLayout");
        }
        CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) params).getBehavior();
        if (!(behavior instanceof BottomNavigationScrollBehavior)) {
            Timber.e("The view is not associated with BottomNavigationScrollBehavior");
        }
        return (BottomNavigationScrollBehavior<V>) behavior;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, V child, View dependency) {
        withSnackbarImpl.updateSnackbar(parent, dependency, child);
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
        if (layout == null && tabLayoutId != View.NO_ID) {
            layout = findTabLayout(child);
            getTabsHolder();
        }

        return layoutChild;
    }

    @Nullable
    private View findTabLayout(@NonNull View child) {
        if (tabLayoutId == 0) {
            return null;
        }
        return child.findViewById(tabLayoutId);
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
            animateOffset(child, child.getHeight());
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
        offsetValueAnimator.translationY(offset).start();
        animateTabsHolder(offset);
    }

    private void animateTabsHolder(int offset) {
        if (tabsHolder != null) {
            ViewCompat.animate(tabsHolder).setDuration(BOTTOM_NAV_SPEED).start();
        }
    }

    private void ensureOrCancelAnimator(V child) {
        if (offsetValueAnimator == null) {
            offsetValueAnimator = ViewCompat.animate(child);
            offsetValueAnimator.setDuration(BOTTOM_NAV_SPEED);
            offsetValueAnimator.setInterpolator(INTERPOLATOR);
        }
        else {
            offsetValueAnimator.cancel();
        }
    }

    private void getTabsHolder() {
        if (layout != null) {
            tabsHolder = layout;
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

    private class PreLollipopBottomNavWithSnackBarImpl implements ViewWithSnackbar {
        @Override
        public void updateSnackbar(CoordinatorLayout parent, View dependency, View child) {
            if (dependency instanceof Snackbar.SnackbarLayout) {
                if (snackbarHeight == -1) {
                    snackbarHeight = dependency.getHeight();
                }

                int targetPadding = child.getMeasuredHeight();

                int shadow = (int) ViewCompat.getElevation(child);
                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) dependency.getLayoutParams();
                layoutParams.bottomMargin = targetPadding - shadow;
                child.bringToFront();
            }
        }
    }

    private class LollipopBottomNavWithSnackBarImpl implements ViewWithSnackbar {
        @Override
        public void updateSnackbar(CoordinatorLayout parent, View dependency, View child) {
            if (dependency instanceof Snackbar.SnackbarLayout) {
                if (snackbarHeight == -1) {
                    snackbarHeight = dependency.getHeight();
                }
                int targetPadding = (snackbarHeight + child.getMeasuredHeight());
                dependency.setPadding(dependency.getPaddingLeft(),
                        dependency.getPaddingTop(), dependency.getPaddingRight(), targetPadding
                );
            }
        }
    }
}
