package com.snaptiongame.snaptionapp.presentation.view.profile;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import com.snaptiongame.snaptionapp.data.models.User;
import com.snaptiongame.snaptionapp.data.providers.UserProvider;
import com.snaptiongame.snaptionapp.data.utils.ImageConverter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @author Tyler Wong
 */

public class ProfilePresenter implements ProfileContract.Presenter {
   @NonNull
   private ProfileContract.View mProfileView;
   @NonNull
   private CompositeDisposable mDisposables;

   private String mEncodedImage;

   public ProfilePresenter(@NonNull ProfileContract.View profileView) {
      mProfileView = profileView;
      mDisposables = new CompositeDisposable();
      mProfileView.setPresenter(this);
   }

   @Override
   public void updateProfilePicture(int snaptionUserId, User user) {
      mDisposables.clear();
      Disposable disposable = UserProvider.updateUser(snaptionUserId, user)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(newUser -> mProfileView.saveProfilePicture(newUser.picture),
                  Timber::e,
                  () -> mProfileView.showProfilePictureSuccess());
      mDisposables.add(disposable);
   }

   @Override
   public void updateUsername(int snaptionUserId, String oldUsername, User user) {
      mDisposables.clear();
      Disposable disposable = UserProvider.updateUser(snaptionUserId, user)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(nextUser -> mProfileView.saveUsername(nextUser.username),
                  Timber::e,
                  () -> mProfileView.showUsernameSuccess(oldUsername, user));
      mDisposables.add(disposable);
   }

   @Override
   public void convertImage(int snaptionUserId, Drawable drawable, String type) {
      mDisposables.clear();
      Disposable disposable = ImageConverter.convertImage(drawable)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(s -> mEncodedImage = s,
                  Timber::e,
                  () -> updateProfilePicture(snaptionUserId, new User(mEncodedImage, type)));
      mDisposables.add(disposable);
   }

   @Override
   public void subscribe() {

   }

   @Override
   public void unsubscribe() {
      mDisposables.clear();
   }
}
