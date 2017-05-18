package com.snaptiongame.app.presentation.view.activityfeed;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.snaptiongame.app.R;
import com.snaptiongame.app.data.models.ActivityFeedItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Tyler Wong
 */

public class ActivityFeedFragment extends Fragment implements ActivityFeedContract.View {

    @BindView(R.id.activity_feed)
    RecyclerView mActivityFeed;

    private ActivityFeedContract.Presenter mPresenter;
    private Unbinder mUnbinder;

    public static final String TAG = ActivityFeedFragment.class.getSimpleName();

    public static ActivityFeedFragment getInstance() {
        return new ActivityFeedFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_feed_fragment, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        mPresenter = new ActivityFeedPresenter(this);

        return view;
    }

    @Override
    public void setPresenter(ActivityFeedContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void addActivityFeedItems(List<ActivityFeedItem> items) {

    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.loadActivityFeed(1);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
