package com.snaptiongame.app.presentation.view.profile;

import android.net.Uri;

import com.snaptiongame.app.data.models.User;
import com.snaptiongame.app.presentation.BasePresenter;
import com.snaptiongame.app.presentation.BaseView;

/**
 * @author Tyler Wong
 */

public class ProfileContract {
    interface View extends BaseView<Presenter> {
        void showProfilePictureSuccess();

        void showProfilePictureFailure();

        void saveProfilePicture(String picture);

        void showUsernameSuccess(String oldUsername, User user);

        void showUsernameFailure(String message);

        void saveUsername(String username);

        void showAddFriendResult(boolean success);

        void showRemoveFriendResult(boolean success);

        void goToLogin();

        void showUser(User user);

        void showHideAddFriend(boolean isVisible);
    }

    interface Presenter extends BasePresenter {
        void updateProfilePicture(User user);

        void updateUsername(String oldUsername, User user);

        void convertImage(String type, Uri uri);

        void loadUser(int userId);

        void loadShouldHideAddFriend(int userId);

        void addFriend(int userId);

        void removeFriend(int userId);

        void logout();
    }
}
