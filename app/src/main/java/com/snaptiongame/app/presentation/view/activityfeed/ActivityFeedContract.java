package com.snaptiongame.app.presentation.view.activityfeed;

import com.snaptiongame.app.data.models.ActivityFeedItem;
import com.snaptiongame.app.presentation.BasePresenter;
import com.snaptiongame.app.presentation.BaseView;

import java.util.List;

/**
 * @author Tyler Wong
 */

public class ActivityFeedContract {
    interface View extends BaseView<Presenter> {
        void addActivityFeedItems(List<ActivityFeedItem> items);

        void showEmptyView();

        void showActivityFeed();

        void setRefreshing(boolean isRefreshing);
    }

    interface Presenter extends BasePresenter {
        void loadActivityFeed(int page);
    }
}
