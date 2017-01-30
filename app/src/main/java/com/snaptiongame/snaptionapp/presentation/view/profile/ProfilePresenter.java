package com.snaptiongame.snaptionapp.presentation.view.profile;

import android.support.annotation.NonNull;

import com.snaptiongame.snaptionapp.data.models.User;
import com.snaptiongame.snaptionapp.data.providers.UserProvider;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

/**
 * @author Tyler Wong
 */

public class ProfilePresenter implements ProfileContract.Presenter {

   @NonNull
   private ProfileContract.View mProfileView;
   @NonNull
   private CompositeSubscription mSubscriptions;

   public ProfilePresenter(@NonNull ProfileContract.View profileView) {
      mProfileView = profileView;
      mSubscriptions = new CompositeSubscription();
      mProfileView.setPresenter(this);
   }

   @Override
   public void updateProfilePicture(int snaptionUserId, User user) {
      mSubscriptions.clear();
      Subscription subscription = UserProvider.updateUser(snaptionUserId, user)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(newUser -> mProfileView.saveProfilePicture(newUser.picture),
                  Timber::e,
                  () -> mProfileView.showProfilePictureSuccess());
      mSubscriptions.add(subscription);
   }

   @Override
   public void updateUsername(int snaptionUserId, String oldUsername, User user) {
      mSubscriptions.clear();
      Subscription subscription = UserProvider.updateUser(snaptionUserId, user)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(nextUser -> mProfileView.saveUsername(nextUser.username),
                  Timber::e,
                  () -> mProfileView.showUsernameSuccess(oldUsername, user));
      mSubscriptions.add(subscription);
   }

   @Override
   public void subscribe() {

   }

   @Override
   public void unsubscribe() {
      mSubscriptions.clear();
   }
}
