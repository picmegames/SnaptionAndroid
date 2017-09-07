package com.snaptiongame.app.presentation.view.profile.moreinfo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.snaptiongame.app.data.models.UserStats;
import com.snaptiongame.app.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Tyler Wong
 */

public class MoreInfoFragment extends Fragment implements MoreInfoContract.View {

    @BindView(R.id.rank)
    TextView rank;
    @BindView(R.id.experience)
    TextView experience;
    @BindView(R.id.games_created)
    TextView gamesCreated;
    @BindView(R.id.captions_created)
    TextView captionsCreated;
    @BindView(R.id.top_game)
    TextView topGame;
    @BindView(R.id.top_caption)
    TextView topCaption;
    @BindView(R.id.top_caption_count)
    TextView topCaptionCount;

    private MoreInfoContract.Presenter presenter;
    private Unbinder unbinder;

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
        unbinder = ButterKnife.bind(this, view);
        presenter = new MoreInfoPresenter(this, getArguments().getInt(USER_ID));
        return view;
    }

    @Override
    public void setPresenter(MoreInfoContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showUserInfo(UserStats userStats) {
        rank.setText(userStats.getRank());
        experience.setText(String.valueOf(userStats.getExp()));
        gamesCreated.setText(String.valueOf(userStats.getGamesCreated()));
        captionsCreated.setText(String.valueOf(userStats.getCaptionsCreated()));
        topGame.setText(String.valueOf(userStats.getHighestGameUpvote()));
        topCaption.setText(String.valueOf(userStats.getCaptionUpvotes()));
        topCaptionCount.setText(String.valueOf(userStats.getTopCaptionCount()));
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.subscribe();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.unsubscribe();
    }
}
