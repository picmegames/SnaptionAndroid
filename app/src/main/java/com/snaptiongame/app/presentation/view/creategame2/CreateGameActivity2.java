package com.snaptiongame.app.presentation.view.creategame2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageButton;

import com.snaptiongame.app.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Tyler Wong
 */

public class CreateGameActivity2 extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    @BindView(R.id.prev_button)
    ImageButton mPrevButton;
    @BindView(R.id.next_button)
    ImageButton mNextDoneButton;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    private boolean isLastPage = false;

    private final OvershootInterpolator interpolator = new OvershootInterpolator();

    private static final float FULL_ROTATION = 360.0f;
    private static final float REVERSE_FULL_ROTATION = 0.0f;
    private static final long LONG_ROTATION_DURATION = 1000;
    private static final int BUTTON_ALPHA = 72;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        ButterKnife.bind(this);

        mNextDoneButton.getBackground().setAlpha(BUTTON_ALPHA);
        mPrevButton.getBackground().setAlpha(BUTTON_ALPHA);

        mViewPager.setAdapter(new CreateGame2PagerAdapter(getSupportFragmentManager()));
        mViewPager.setOffscreenPageLimit(CreateGame2PagerAdapter.NUM_PAGES);
        mViewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == CreateGame2PagerAdapter.REVIEW_PAGE) {
            isLastPage = true;
            animate(mNextDoneButton, R.drawable.ic_check_white_24dp, FULL_ROTATION, LONG_ROTATION_DURATION);
        }
        else {
            if (isLastPage) {
                isLastPage = false;
                animate(mNextDoneButton, R.drawable.ic_arrow_forward_white_24dp, REVERSE_FULL_ROTATION, LONG_ROTATION_DURATION);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @OnClick(R.id.prev_button)
    public void onClickPrevButton() {
        if (mViewPager.getCurrentItem() == CreateGame2PagerAdapter.PHOTO_PAGE) {
            onBackPressed();
        }
        else {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
        }
    }

    @OnClick(R.id.next_button)
    public void onClickNextButton() {
        int currItem = mViewPager.getCurrentItem();

        if (currItem < CreateGame2PagerAdapter.REVIEW_PAGE) {
            mViewPager.setCurrentItem(currItem + 1);
        }
        else {
            // CREATE GAME
        }
    }

    private void animate(ImageButton button, int toIcon, float rotation, long duration) {
        button.setImageResource(toIcon);
        ViewCompat.animate(button)
                .rotation(rotation)
                .withLayer()
                .setDuration(duration)
                .setInterpolator(interpolator)
                .start();
    }
}
