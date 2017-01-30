package com.snaptiongame.snaptionapp.presentation.view.profile;

import com.snaptiongame.snaptionapp.data.models.User;
import com.snaptiongame.snaptionapp.presentation.BasePresenter;
import com.snaptiongame.snaptionapp.presentation.BaseView;

/**
 * @author Tyler Wong
 */

public class ProfileContract {
   interface View extends BaseView<Presenter> {
      void showProfilePictureSuccess();
      void saveProfilePicture(String picture);
      void showUsernameSuccess(String oldUsername, User user);
      void saveUsername(String username);
   }

   interface Presenter extends BasePresenter {
      void updateProfilePicture(int snaptionUserId, User user);
      void updateUsername(int snaptionUserId, String oldUsername, User user);
   }
}
