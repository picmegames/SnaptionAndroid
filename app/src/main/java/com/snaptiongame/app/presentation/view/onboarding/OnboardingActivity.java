package com.snaptiongame.app.presentation.view.onboarding;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageButton;

import com.snaptiongame.app.R;
import com.snaptiongame.app.data.models.OnboardingInfo;
import com.snaptiongame.app.presentation.view.main.MainActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Tyler Wong
 */

public class OnboardingActivity extends AppCompatActivity {
    @BindView(R.id.prev_button)
    ImageButton mSkipPrevButton;
    @BindView(R.id.next_button)
    ImageButton mNextDoneButton;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.dot_tabs)
    TabLayout mDotTabLayout;

    private List<OnboardingInfo> mOnboardingInfo;
    private SharedPreferences mPreferences;

    private int originalWidth;
    private boolean isLastPage = false;

    private final OvershootInterpolator interpolator = new OvershootInterpolator();

    private static final float FULL_ROTATION = 360.0f;
    private static final float REVERSE_FULL_ROTATION = 0.0f;
    private static final long SHORT_RESIZE_DURATION = 300;
    private static final long LONG_ROTATION_DURATION = 1000;
    private static final int BUTTON_ALPHA = 72;

    public static final String FIRST_RUN = "firstRun";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        boolean isFirstRun = mPreferences.getBoolean(FIRST_RUN, true);

        if (isFirstRun) {
            setContentView(R.layout.activity_onboarding);
            ButterKnife.bind(this);

            mNextDoneButton.getBackground().setAlpha(BUTTON_ALPHA);
            mSkipPrevButton.getBackground().setAlpha(BUTTON_ALPHA);

            originalWidth = mSkipPrevButton.getWidth();

            mOnboardingInfo = new ArrayList<>();
            mOnboardingInfo.add(new OnboardingInfo(R.string.app_name, R.string.snaption_description, R.drawable.snaption_icon));
            mOnboardingInfo.add(new OnboardingInfo(R.string.app_name, R.string.snaption_description, R.drawable.snaption_icon));
            mOnboardingInfo.add(new OnboardingInfo(R.string.app_name, R.string.snaption_description, R.drawable.snaption_icon));

            mViewPager.setAdapter(new OnboardingPagerAdapter(getSupportFragmentManager(), mOnboardingInfo));
            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    if (position == mOnboardingInfo.size() - 1) {
                        isLastPage = true;
                        animate(mNextDoneButton, R.drawable.ic_check_white_24dp, FULL_ROTATION, LONG_ROTATION_DURATION);
                    }
                    else {
                        if (isLastPage) {
                            isLastPage = false;
                            animate(mNextDoneButton, R.drawable.ic_arrow_forward_white_24dp, REVERSE_FULL_ROTATION, LONG_ROTATION_DURATION);
                        }
                    }

                    if (position == 0) {
                        mSkipPrevButton.setVisibility(View.INVISIBLE);
                    }
                    else {
                        mSkipPrevButton.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            mDotTabLayout.setupWithViewPager(mViewPager, true);
        }
        else {
            goToMain();
        }
    }

    @OnClick(R.id.prev_button)
    public void onClickPrevButton() {
        mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
    }

    @OnClick(R.id.next_button)
    public void onClickNextButton() {
        int currItem = mViewPager.getCurrentItem();

        if (currItem < mOnboardingInfo.size() - 1) {
            mViewPager.setCurrentItem(currItem + 1);
        }
        else {
            mPreferences.edit().putBoolean(FIRST_RUN, false).apply();
            goToMain();
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

    private void goToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
