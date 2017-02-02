package com.snaptiongame.snaptionapp.presentation.view.game;

import android.support.annotation.NonNull;

import com.snaptiongame.snaptionapp.data.models.Caption;
import com.snaptiongame.snaptionapp.data.providers.CaptionProvider;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.realm.Realm;
import timber.log.Timber;

/**
 * @author Tyler Wong
 */

public class GamePresenter implements GameContract.Presenter {
   @NonNull
   private GameContract.View mGameView;
   @NonNull
   private CompositeDisposable mDisposables;
   private int mGameId;

   public GamePresenter(int gameId, @NonNull GameContract.View view) {
      mGameId = gameId;
      mGameView = view;
      mDisposables = new CompositeDisposable();
      mGameView.setPresenter(this);
   }

   @Override
   public void loadCaptions() {
      mDisposables.clear();
      Disposable disposable = CaptionProvider.getCaptions(mGameId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                  this::processCaptions,
                  Timber::e,
                  () -> Timber.i("Loading captions completed successfully."));
      mDisposables.add(disposable);
   }

   private void processCaptions(List<Caption> captions) {
      try (Realm realmInstance = Realm.getDefaultInstance()) {
         realmInstance.executeTransaction(realm ->
               realm.copyToRealmOrUpdate(captions)
         );
      }
      mGameView.showCaptions(captions);
   }

   @Override
   public void addCaption(String caption) {
      CaptionProvider.addCaption(mGameId,
            new Caption(1, caption))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(mGameView::addCaption, Timber::e, () -> Timber.i("Added caption"));
   }

   @Override
   public void subscribe() {
      loadCaptions();
   }

   @Override
   public void unsubscribe() {
      mDisposables.clear();
   }
}