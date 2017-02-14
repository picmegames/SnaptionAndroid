package com.snaptiongame.snaptionapp.presentation.view.creategame;

import android.content.ContentResolver;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.snaptiongame.snaptionapp.data.models.Snaption;
import com.snaptiongame.snaptionapp.data.providers.FriendProvider;
import com.snaptiongame.snaptionapp.data.providers.SnaptionProvider;
import com.snaptiongame.snaptionapp.data.utils.ImageConverter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @author Tyler Wong
 */

public class CreateGamePresenter implements CreateGameContract.Presenter {
   @NonNull
   private CreateGameContract.View mCreateGameView;
   @NonNull
   private CompositeDisposable mDisposables;

   private int mUserId;
   private String mEncodedImage;

   public CreateGamePresenter(int userId, @NonNull CreateGameContract.View createGameView) {
      mUserId = userId;
      mCreateGameView = createGameView;
      mDisposables = new CompositeDisposable();
      mCreateGameView.setPresenter(this);
   }

   @Override
   public void createGame(Drawable drawable, String type, int userId, boolean isPublic) {
      if (drawable != null) {
         mDisposables.clear();
         Disposable disposable = SnaptionProvider.addSnaption(
               new Snaption(userId, !isPublic, 1, mEncodedImage, type))
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(snaption -> {
               }, e -> {
                  Timber.e(e);
                  mCreateGameView.showCreateFailure();
               }, () -> {
                  mCreateGameView.onBackPressed();
                  mCreateGameView.showCreateSuccess();
               });
         mDisposables.add(disposable);
      }
   }

   @Override
   public void convertImage(ContentResolver resolver, Uri uri, Drawable drawable, int userId, boolean isPublic) {
      ImageConverter.convertImage(resolver, uri)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(s -> mEncodedImage = s,
                  Timber::e,
                  () -> createGame(drawable, resolver.getType(uri), userId, isPublic));
   }

   private void loadFriends() {
      mDisposables.clear();
      Disposable disposable = FriendProvider.loadFriends(mUserId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                  mCreateGameView::setFriends,
                  Timber::e,
                  () -> Timber.i("Getting friends was successful"));
      mDisposables.add(disposable);
   }

   @Override
   public void subscribe() {
      loadFriends();
   }

   @Override
   public void unsubscribe() {
      mDisposables.clear();
   }
}
