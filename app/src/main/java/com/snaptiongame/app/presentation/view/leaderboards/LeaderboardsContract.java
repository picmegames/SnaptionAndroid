package com.snaptiongame.app.presentation.view.leaderboards;

import com.snaptiongame.app.data.models.Friend;
import com.snaptiongame.app.presentation.BasePresenter;
import com.snaptiongame.app.presentation.BaseView;

import java.util.List;

/**
 * @author Tyler Wong
 */

public class LeaderboardsContract {

    interface View extends BaseView<Presenter> {
        void setLeaderboard(List<Friend> leaderboard);
    }

    interface Presenter extends BasePresenter {
        void loadLeaderboard(int type);
    }
}
