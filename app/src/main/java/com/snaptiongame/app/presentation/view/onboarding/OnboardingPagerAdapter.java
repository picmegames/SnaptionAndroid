package com.snaptiongame.app.presentation.view.onboarding;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.snaptiongame.app.data.models.OnboardingInfo;

import java.util.List;

/**
 * @author Tyler Wong
 */

public class OnboardingPagerAdapter extends FragmentStatePagerAdapter {

    private List<OnboardingInfo> mOnboardingInfo;

    public OnboardingPagerAdapter(FragmentManager fm, List<OnboardingInfo> onboardingInfo) {
        super(fm);
        mOnboardingInfo = onboardingInfo;
    }

    @Override
    public Fragment getItem(int position) {
        return OnboardingInfoFragment.newInstance(mOnboardingInfo.get(position));
    }

    @Override
    public int getCount() {
        return mOnboardingInfo.size();
    }
}
