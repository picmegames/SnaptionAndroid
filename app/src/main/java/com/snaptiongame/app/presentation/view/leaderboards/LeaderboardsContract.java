package com.snaptiongame.app.presentation.view.leaderboards;

import com.snaptiongame.app.data.models.Friend;
import com.snaptiongame.app.presentation.BasePresenter;
import com.snaptiongame.app.presentation.BaseView;

import java.util.List;

/**
 * @author Tyler Wong
 */

public class LeaderboardsContract {

    public static int WORLD = 0;
    public static int FRIENDS = 1;

    interface View extends BaseView<Presenter> {
        void setLeaderboard(List<Friend> leaderboard);

        void setRefreshing(boolean isRefreshing);
    }

    interface Presenter extends BasePresenter {
        void loadLeaderboard();
    }
}
