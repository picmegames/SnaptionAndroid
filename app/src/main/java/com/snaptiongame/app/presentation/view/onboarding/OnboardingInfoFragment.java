package com.snaptiongame.app.presentation.view.onboarding;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.snaptiongame.app.R;
import com.snaptiongame.app.data.models.OnboardingInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Tyler Wong
 */

public class OnboardingInfoFragment extends Fragment {
    @BindView(R.id.title)
    TextView mTitleText;
    @BindView(R.id.description)
    TextView mDescriptionText;
    @BindView(R.id.animation_view)
    LottieAnimationView mAnimationView;

    private Unbinder unbinder;

    public static OnboardingInfoFragment newInstance(OnboardingInfo info) {
        OnboardingInfoFragment frag = new OnboardingInfoFragment();
        Bundle args = new Bundle();
        args.putInt(OnboardingInfo.TITLE_ID, info.titleId);
        args.putInt(OnboardingInfo.DESCRIPTION_ID, info.descriptionId);
        args.putInt(OnboardingInfo.ANIMATION, info.animationId);
        frag.setArguments(args);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.onboarding_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        Bundle args = getArguments();

        mTitleText.setText(args.getInt(OnboardingInfo.TITLE_ID));
        mDescriptionText.setText(args.getInt(OnboardingInfo.DESCRIPTION_ID));
        mAnimationView.setAnimation(getString(args.getInt(OnboardingInfo.ANIMATION)));
        mAnimationView.loop(true);
        mAnimationView.playAnimation();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
