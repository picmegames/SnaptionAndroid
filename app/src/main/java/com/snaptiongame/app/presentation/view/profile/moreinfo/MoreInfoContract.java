package com.snaptiongame.app.presentation.view.profile.moreinfo;

import com.snaptiongame.app.presentation.BasePresenter;
import com.snaptiongame.app.presentation.BaseView;

/**
 * @author Tyler Wong
 */

public class MoreInfoContract {
    interface View extends BaseView<Presenter> {
        void showUserInfo(String rank, int experience);
    }

    interface Presenter extends BasePresenter {

    }
}
