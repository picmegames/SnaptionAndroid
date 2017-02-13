package com.snaptiongame.snaptionapp.presentation.view.game;

import android.support.annotation.NonNull;

import com.snaptiongame.snaptionapp.data.authentication.AuthenticationManager;
import com.snaptiongame.snaptionapp.data.models.Caption;
import com.snaptiongame.snaptionapp.data.models.CaptionSet;
import com.snaptiongame.snaptionapp.data.models.FitBCaption;
import com.snaptiongame.snaptionapp.data.providers.CaptionProvider;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

/**
 * @author Tyler Wong
 */

public class GamePresenter implements GameContract.Presenter {
   @NonNull
   private GameContract.View mGameView;
   @NonNull
   private CompositeDisposable mDisposables;
   @NonNull
   private GameContract.CaptionDialogView mGameDialogView;

   private int mGameId;
   private AuthenticationManager mAuth;


   public GamePresenter(int gameId, @NonNull GameContract.View view) {
      mGameId = gameId;
      mGameView = view;

      mDisposables = new CompositeDisposable();
      mGameView.setPresenter(this);


   }

   public GamePresenter(int gameId, @NonNull GameContract.CaptionDialogView view) {
      mGameId = gameId;
      mDisposables = new CompositeDisposable();
      mGameDialogView = view;
      mGameDialogView.setPresenter(this);
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
      mGameView.showCaptions(captions);
   }

   @Override
   public void addCaption(String caption, int userId, int fitbId) {

      CaptionProvider.addCaption(mGameId,
            new Caption(fitbId, caption, userId))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(mGameDialogView::addCaption, Timber::e, () -> Timber.i("Added caption"));
   }

   @Override
   public void loadCaptionSets() {
      mDisposables.clear();
      Disposable disposable = CaptionProvider.getCaptionSets()
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(
                      mGameDialogView::showCaptionSets,
                      Timber::e,
                      () -> Timber.i("Loading caption sets worked"));
      mDisposables.add(disposable);

   }

   @Override
   public void loadFitBCaptions() {
      mDisposables.clear();
      Disposable disposable = CaptionProvider.getFitBCaptions()
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(mGameDialogView::showFitBCaptions,
                      Timber::e,
                      () -> Timber.i("Successfully got Fitb's!"));
      mDisposables.add(disposable);
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
