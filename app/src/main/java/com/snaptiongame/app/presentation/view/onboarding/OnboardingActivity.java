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
import com.snaptiongame.app.presentation.view.MainActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Tyler Wong
 */

public class OnboardingActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    @BindView(R.id.prev_button)
    ImageButton skipPrevButton;
    @BindView(R.id.next_button)
    ImageButton nextDoneButton;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.dot_tabs)
    TabLayout dotTabLayout;

    private List<OnboardingInfo> onboardingInfo;
    private SharedPreferences preferences;
    private boolean isLastPage = false;

    private final OvershootInterpolator interpolator = new OvershootInterpolator();

    private static final float FULL_ROTATION = 360.0f;
    private static final float REVERSE_FULL_ROTATION = 0.0f;
    private static final long LONG_ROTATION_DURATION = 1000;
    private static final int BUTTON_ALPHA = 72;

    public static final String FIRST_RUN = "firstRun";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        boolean isFirstRun = preferences.getBoolean(FIRST_RUN, true);

        if (isFirstRun) {
            setContentView(R.layout.activity_onboarding);
            ButterKnife.bind(this);

            nextDoneButton.getBackground().setAlpha(BUTTON_ALPHA);
            skipPrevButton.getBackground().setAlpha(BUTTON_ALPHA);

            onboardingInfo = new ArrayList<>();
            onboardingInfo.add(new OnboardingInfo(R.string.title_1, R.string.desc_1, R.string.anim_1));
            onboardingInfo.add(new OnboardingInfo(R.string.title_2, R.string.desc_2, R.string.anim_2));
            onboardingInfo.add(new OnboardingInfo(R.string.title_3, R.string.desc_3, R.string.anim_3));

            viewPager.setAdapter(new OnboardingPagerAdapter(getSupportFragmentManager(), onboardingInfo));
            viewPager.addOnPageChangeListener(this);

            dotTabLayout.setupWithViewPager(viewPager, true);
        }
        else {
            goToMain();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == onboardingInfo.size() - 1) {
            isLastPage = true;
            animate(nextDoneButton, R.drawable.ic_check_white_24dp, FULL_ROTATION, LONG_ROTATION_DURATION);
        }
        else {
            if (isLastPage) {
                isLastPage = false;
                animate(nextDoneButton, R.drawable.ic_arrow_forward_white_24dp, REVERSE_FULL_ROTATION, LONG_ROTATION_DURATION);
            }
        }

        if (position == 0) {
            skipPrevButton.setVisibility(View.INVISIBLE);
        }
        else {
            skipPrevButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @OnClick(R.id.prev_button)
    public void onClickPrevButton() {
        viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
    }

    @OnClick(R.id.next_button)
    public void onClickNextButton() {
        int currItem = viewPager.getCurrentItem();

        if (currItem < onboardingInfo.size() - 1) {
            viewPager.setCurrentItem(currItem + 1);
        }
        else {
            preferences.edit().putBoolean(FIRST_RUN, false).apply();
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
        finish();
    }
}
