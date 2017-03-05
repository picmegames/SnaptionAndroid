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

        void showUsernameFailure(String oldUsername, User user);

        void saveUsername(String username);

        void goToLogin();
    }

    interface Presenter extends BasePresenter {
        void updateProfilePicture(User user);

        void updateUsername(String oldUsername, User user);

        void convertImage(String type, Uri uri);

        void logout();
    }
}
