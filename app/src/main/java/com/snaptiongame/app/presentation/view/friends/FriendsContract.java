package com.snaptiongame.app.presentation.view.friends;

import com.snaptiongame.app.data.models.Friend;
import com.snaptiongame.app.presentation.BasePresenter;
import com.snaptiongame.app.presentation.BaseView;

import java.util.List;

/**
 * @author Nick Romero
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
        void loadFriends(int page);

        void removeFriend(String name, int friendId);

        void addFriend(String name, int friendId);

        void findFriends(String query, int page);
    }
}
