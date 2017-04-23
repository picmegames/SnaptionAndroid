package com.snaptiongame.app.presentation.view.creategame;

import android.content.Context;
import android.net.Uri;

import com.snaptiongame.app.data.models.Friend;
import com.snaptiongame.app.presentation.BasePresenter;
import com.snaptiongame.app.presentation.BaseView;

import java.util.List;

/**
 * @author Tyler Wong
 */

public class CreateGameContract {
    interface View extends BaseView<Presenter> {
        void createGame();

        void setFriendNames(String[] friendNames);

        void onBackPressed();

        void showUploadComplete();

        void showUploadFailure();

        void showImageCompressionFailure();

        List<String> getTags();

        List<String> getAddedFriends();

        Context getContext();

        void showFriendsDialog();
    }

    interface Presenter extends BasePresenter {
        void createGame(String type, Uri uri, int userId, boolean isPublic);

        int getFriendIdByName(String name);

        List<Friend> getFriends();

        boolean containsEmojis(List<String> tags);

        void loadFriends();
    }
}
