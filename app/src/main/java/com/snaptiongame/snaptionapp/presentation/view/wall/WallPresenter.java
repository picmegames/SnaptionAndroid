package com.snaptiongame.snaptionapp.presentation.view.wall;

import android.support.annotation.NonNull;

import com.snaptiongame.snaptionapp.data.models.Snaption;
import com.snaptiongame.snaptionapp.data.providers.SnaptionProvider;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.realm.Realm;
import timber.log.Timber;

/**
 * @author Tyler Wong
 */

public class WallPresenter implements WallContract.Presenter {
   @NonNull
   private final WallContract.View mWallView;
   @NonNull
   private CompositeDisposable mDisposables;

   public WallPresenter(@NonNull WallContract.View wallView) {
      mWallView = wallView;
      mDisposables = new CompositeDisposable();
      mWallView.setPresenter(this);
   }

   @Override
   public void loadGames() {
      mDisposables.clear();
      Disposable disposable = SnaptionProvider.getAllSnaptions()
            .publish(network ->
                  Observable.merge(network,
                        SnaptionProvider.getAllLocalSnaptions()
                              .takeUntil(network))
            )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                  this::processSnaptions,
                  Timber::e,
                  () -> Timber.i("Getting Snaptions completed successfully")
            );
      mDisposables.add(disposable);
   }

   private void processSnaptions(List<Snaption> snaptions) {
      try (Realm realmInstance = Realm.getDefaultInstance()) {
         realmInstance.executeTransaction(realm ->
               realmInstance.copyToRealmOrUpdate(snaptions));
      }
      mWallView.showGames(snaptions);
   }

   @Override
   public void subscribe() {
      loadGames();
   }

   @Override
   public void unsubscribe() {
      mDisposables.clear();
   }
}
