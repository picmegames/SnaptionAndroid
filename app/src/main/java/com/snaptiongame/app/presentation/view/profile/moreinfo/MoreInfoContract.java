package com.snaptiongame.app.presentation.view.profile.moreinfo;

import com.snaptiongame.app.data.models.UserStats;
import com.snaptiongame.app.presentation.BasePresenter;
import com.snaptiongame.app.presentation.BaseView;

/**
 * @author Tyler Wong
 */

public class MoreInfoContract {
    interface View extends BaseView<Presenter> {
        void showUserInfo(String rank, int experience);

        void showMoreInfo(UserStats userStats);
    }

    interface Presenter extends BasePresenter {

    }
}
