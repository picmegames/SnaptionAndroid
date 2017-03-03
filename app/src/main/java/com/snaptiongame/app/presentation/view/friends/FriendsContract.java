package com.snaptiongame.app.presentation.view.friends;

import com.snaptiongame.app.data.models.Friend;
import com.snaptiongame.app.presentation.BasePresenter;
import com.snaptiongame.app.presentation.BaseView;

import java.util.List;

/**
 * Created by nickromero on 1/30/17.
 */

public class FriendsContract {
    interface View extends BaseView<Presenter> {
        void processFriends(List<Friend> friends);
    }

    interface Presenter extends BasePresenter {
        void loadFriends();
    }
}