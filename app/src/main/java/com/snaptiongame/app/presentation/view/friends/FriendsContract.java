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

        void addFriend(Friend friend);

        void showEmptyView();

        void showFriendList();

        void setRefreshing(boolean isRefreshing);
    }

    interface Presenter extends BasePresenter {
        void loadFriends();

        void removeFriend(int friendId);

        void addFriend(int friendId);

        void searchFriends(String query);

        void findFriends(String query);

        void addFriendTemp(Friend friend);
    }
}
