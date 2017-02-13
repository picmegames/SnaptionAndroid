package com.snaptiongame.snaptionapp.presentation.view.wall;

import android.support.annotation.NonNull;

import com.snaptiongame.snaptionapp.data.models.Snaption;
import com.snaptiongame.snaptionapp.data.providers.SnaptionProvider;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

/**
 * The Wall Presenter handles all of the network calls
 * to retrieve a new list of games and present it to the
 * Wall Fragment.
 *
 * @author Tyler Wong
 * @version 1.0
 */
public class WallPresenter implements WallContract.Presenter {
   @NonNull
   private final WallContract.View mWallView;
   @NonNull
   private CompositeDisposable mDisposables;

   /**
    * This constructor creates a new instance of a Wall Presenter
    * and initializes a Composite Disposable.
    *
    * @param wallView The view that it will present to
    */
   public WallPresenter(@NonNull WallContract.View wallView) {
      mWallView = wallView;
      mDisposables = new CompositeDisposable();
      mWallView.setPresenter(this);
   }

   /**
    * This method sends a network request to get a new list
    * of games from the server. It will send the request on
    * an IO thread and handle the result on the UI thread.
    */
   @Override
   public void loadGames() {
      mDisposables.clear();
      Disposable disposable = SnaptionProvider.getAllSnaptions()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                  this::processSnaptions,
                  Timber::e,
                  () -> Timber.i("Getting Snaptions completed successfully")
            );
      mDisposables.add(disposable);
   }

   /**
    * This method hands off the list to the view to be shown.
    *
    * @param snaptions The list of games from the server
    */
   private void processSnaptions(List<Snaption> snaptions) {
      mWallView.showGames(snaptions);
   }

   /**
    * This method calls the method loadGames to be loaded
    * from the server.
    */
   @Override
   public void subscribe() {
      loadGames();
   }

   /**
    * This method clears any disposables when the view is
    * destroyed.
    */
   @Override
   public void unsubscribe() {
      mDisposables.clear();
   }
}
