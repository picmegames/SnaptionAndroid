package com.snaptiongame.app.presentation.view.activityfeed;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.snaptiongame.app.R;
import com.snaptiongame.app.data.models.ActivityFeedItem;
import com.snaptiongame.app.data.models.Game;
import com.snaptiongame.app.data.models.User;
import com.snaptiongame.app.presentation.view.decorations.InsetDividerDecoration;
import com.snaptiongame.app.presentation.view.game.CaptionCardViewHolder;
import com.snaptiongame.app.presentation.view.utils.ActivityFeedUtils;

import java.util.ArrayList;
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
    private ActivityFeedAdapter mAdapter;
    private InsetDividerDecoration mDecoration;

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

        // TESTING
        List<ActivityFeedItem> testItems = new ArrayList<>();
        User friend = new User();
        friend.imageUrl = "https://img.pokemondb.net/artwork/bulbasaur.jpg";
        friend.username = "bulbasaur";
        Game game = new Game();
        game.imageUrl = "https://typeset-beta.imgix.net/rehost%2F2016%2F9%2F13%2F39a6deb0-8ff2-4ec5-854b-1b197d81df0a.jpg";
        ActivityFeedItem item1 = new ActivityFeedItem(0, 1495699613, ActivityFeedUtils.CAPTIONED_GAME,
                null, friend, game, null);
        testItems.add(item1);
        testItems.add(item1);
        testItems.add(item1);
        testItems.add(item1);
        testItems.add(item1);
        testItems.add(item1);
        testItems.add(item1);
        testItems.add(item1);

        mAdapter = new ActivityFeedAdapter(testItems);
        mActivityFeed.setAdapter(mAdapter);
        mActivityFeed.setLayoutManager(new LinearLayoutManager(getContext()));
        mActivityFeed.setHasFixedSize(true);

        mDecoration = new InsetDividerDecoration(
                CaptionCardViewHolder.class,
                getResources().getDimensionPixelSize(R.dimen.divider_height),
                getResources().getDimensionPixelSize(R.dimen.keyline_1),
                ContextCompat.getColor(getContext(), R.color.divider));
        mActivityFeed.addItemDecoration(mDecoration);

        return view;
    }

    @Override
    public void setPresenter(ActivityFeedContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void addActivityFeedItems(List<ActivityFeedItem> items) {
        mAdapter.setActivityItems(items);
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
