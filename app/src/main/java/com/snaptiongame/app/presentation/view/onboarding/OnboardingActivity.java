package com.snaptiongame.app.presentation.view.onboarding;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

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
    Button mSkipPrevButton;
    @BindView(R.id.next_button)
    Button mNextButton;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.dot_tabs)
    TabLayout mDotTabLayout;

    private List<OnboardingInfo> mOnboardingInfo;
    private SharedPreferences mPreferences;

    public static final String FIRST_RUN = "firstRun";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        boolean isFirstRun = mPreferences.getBoolean(FIRST_RUN, true);

        if (isFirstRun) {
            setContentView(R.layout.activity_onboarding);
            ButterKnife.bind(this);

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
                        mNextButton.setText(getString(R.string.done));
                    }
                    else {
                        mNextButton.setText(getString(R.string.next));
                    }

                    if (position == 0) {
                        mSkipPrevButton.setText(getString(R.string.skip));
                    }
                    else {
                        mSkipPrevButton.setText(getString(R.string.previous));
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
        int currItem = mViewPager.getCurrentItem();

        if (currItem == 0) {
            mPreferences.edit().putBoolean(FIRST_RUN, false).apply();
            goToMain();
        }
        else {
            mViewPager.setCurrentItem(currItem - 1);
        }
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

    private void goToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
