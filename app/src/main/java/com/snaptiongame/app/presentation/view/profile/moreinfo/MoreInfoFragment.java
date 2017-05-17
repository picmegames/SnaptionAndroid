package com.snaptiongame.app.presentation.view.profile.moreinfo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.snaptiongame.app.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Tyler Wong
 */

public class MoreInfoFragment extends Fragment implements MoreInfoContract.View {

    @BindView(R.id.rank)
    TextView mRank;
    @BindView(R.id.experience)
    TextView mExperience;
    @BindView(R.id.gamesCreated)
    TextView mGamesCreated;
    @BindView(R.id.captionsCreated)
    TextView mCaptionsCreated;
    @BindView(R.id.topGame)
    TextView mTopGame;
    @BindView(R.id.topCaption)
    TextView mTopCaption;
    @BindView(R.id.topCaptionCount)
    TextView mTopCaptionCount;

    private MoreInfoContract.Presenter mPresenter;
    private Unbinder mUnbinder;

    public static final String USER_ID = "userId";

    public static MoreInfoFragment getInstance(int userId) {
        Bundle args = new Bundle();
        args.putInt(USER_ID, userId);
        MoreInfoFragment moreFragment = new MoreInfoFragment();
        moreFragment.setArguments(args);
        return moreFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.more_info_fragment, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        mPresenter = new MoreInfoPresenter(this, getArguments().getInt(USER_ID));
        return view;
    }

    @Override
    public void setPresenter(MoreInfoContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showUserInfo(String name, int experience) {
        mRank.setText(name);
        mExperience.setText(String.valueOf(experience));
    }

    @Override
    public void showMoreInfo(int gamesCreated, int captionsCreated, int topGameUpvotes, int topCaptionUpvotes, int topCaptionCount) {
        mGamesCreated.setText(String.valueOf(gamesCreated));
        mCaptionsCreated.setText(String.valueOf(captionsCreated));
        mTopGame.setText(String.valueOf(topGameUpvotes));
        mTopCaption.setText(String.valueOf(topCaptionUpvotes));
        mTopCaptionCount.setText(String.valueOf(topCaptionCount));
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
        mPresenter.unsubscribe();
    }
}
