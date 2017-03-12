package com.snaptiongame.app.presentation.view.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.snaptiongame.app.R;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Tyler Wong
 */

public class MoreInfoFragment extends Fragment {
    @BindView(R.id.exp)
    TextView mExperience;
    @BindView(R.id.rank)
    TextView mRank;

    private int mUserId;
    private int mUserExp;
    private String mUserRank;

    private String[] ranks = {"Noobie", "Champion", "Master", "Beginner"};

    public static final String USER_ID = "userId";
    private Unbinder mUnbinder;

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

        mUserId = getArguments().getInt(USER_ID);
        Random random = new Random(mUserId);
        mUserExp = mUserId * random.nextInt(100);
        mUserRank = ranks[mUserId%4];

        mExperience.setText(Integer.toString(mUserExp));
        mRank.setText(mUserRank);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
