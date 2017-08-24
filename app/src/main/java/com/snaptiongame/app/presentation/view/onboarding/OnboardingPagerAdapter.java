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

    private List<OnboardingInfo> onboardingInfo;

    public OnboardingPagerAdapter(FragmentManager manager, List<OnboardingInfo> onboardingInfo) {
        super(manager);
        this.onboardingInfo = onboardingInfo;
    }

    @Override
    public Fragment getItem(int position) {
        return OnboardingInfoFragment.newInstance(onboardingInfo.get(position));
    }

    @Override
    public int getCount() {
        return onboardingInfo.size();
    }
}
