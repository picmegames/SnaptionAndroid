package com.snaptiongame.snaptionapp.presentation.view.wall;

import android.support.annotation.NonNull;

import com.snaptiongame.snaptionapp.data.models.Snaption;
import com.snaptiongame.snaptionapp.data.providers.SnaptionProvider;

import java.util.List;

import io.realm.Realm;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

/**
 * @author Tyler Wong
 */

public class WallPresenter implements WallContract.Presenter {
   @NonNull
   private final WallContract.View mWallView;
   @NonNull
   private CompositeSubscription mSubscriptions;

   public WallPresenter(@NonNull WallContract.View wallView) {
      mWallView = wallView;
      mSubscriptions = new CompositeSubscription();
      mWallView.setPresenter(this);
   }

   @Override
   public void loadGames() {
      mSubscriptions.clear();
      Subscription subscription = SnaptionProvider.getAllSnaptions()
            .publish(network ->
                  Observable.merge(network,
                        SnaptionProvider.getAllLocalSnaptions()
                              .takeUntil(network))
            )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                  this::processSnaptions,
                  throwable -> Timber.e(throwable, "Getting Snaptions errored."),
                  () -> Timber.i("Getting Snaptions completed successfully")
            );
      mSubscriptions.add(subscription);
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
      mSubscriptions.clear();
   }
}
