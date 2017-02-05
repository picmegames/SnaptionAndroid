package com.snaptiongame.snaptionapp.presentation.view.friends;

import com.snaptiongame.snaptionapp.data.models.Friend;
import com.snaptiongame.snaptionapp.presentation.BasePresenter;
import com.snaptiongame.snaptionapp.presentation.BaseView;

import java.util.List;

/**
 * Created by nickromero on 1/30/17.
 */

public class FriendsContract {
    interface View extends BaseView<Presenter> {
        void showFriends(List<Friend> friends);
    }

    interface Presenter extends BasePresenter {
        void loadFriends();
    }
}
