package com.snaptiongame.snaptionapp.presentation.view.game;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.snaptiongame.snaptionapp.data.models.Caption;
import com.snaptiongame.snaptionapp.data.models.Like;
import com.snaptiongame.snaptionapp.data.providers.CaptionProvider;
import com.snaptiongame.snaptionapp.data.providers.FacebookShareProvider;
import com.snaptiongame.snaptionapp.data.providers.SnaptionProvider;
import com.snaptiongame.snaptionapp.data.providers.UserProvider;

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
    private int mPickerId;


    public GamePresenter(int gameId, int pickerId, @NonNull GameContract.View view) {
        mGameId = gameId;
        mPickerId = pickerId;
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

    public void loadGame(int gameId) {
    }

    @Override
    public void loadCaptions() {
        Disposable disposable = CaptionProvider.getCaptions(mGameId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::processCaptions,
                        Timber::e,
                        () -> Timber.i("Loading captions completed successfully.")
                );
        mDisposables.add(disposable);
    }

    private void loadPickerInfo() {
        Disposable disposable = UserProvider.getUser(mPickerId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        user -> mGameView.setPickerInfo(user.picture, user.username),
                        Timber::e,
                        () -> Timber.i("Loading picker completed successfully.")
                );
        mDisposables.add(disposable);
    }

    @Override
    public void upvoteOrFlagGame(Like request) {
        Disposable disposable = SnaptionProvider.upvoteOrFlagSnaption(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        like -> {
                        },
                        Timber::e,
                        () -> Timber.i("Successfully flagged snaption")
                );
        mDisposables.add(disposable);
    }

    @Override
    public void shareToFacebook(AppCompatActivity activity, ImageView image) {
        FacebookShareProvider.shareToFacebook(activity, image);
    }

    private void processCaptions(List<Caption> captions) {
        mGameView.showCaptions(captions);
    }

    @Override
    public void addCaption(int fitbId, String caption) {
        Disposable disposable = CaptionProvider.addCaption(mGameId, new Caption(fitbId, caption))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        addedCaption -> {
                        },
                        Timber::e,
                        () -> Timber.i("Added caption")
                );
        mDisposables.add(disposable);
    }

    @Override
    public void loadCaptionSets() {
        Disposable disposable = CaptionProvider.getCaptionSets()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        mGameDialogView::showCaptionSets,
                        Timber::e,
                        () -> Timber.i("Loading caption sets worked")
                );
        mDisposables.add(disposable);

    }

    @Override
    public void loadFitBCaptions(int setId) {
        Disposable disposable = CaptionProvider.getFitBCaptions(setId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        mGameDialogView::showFitBCaptions,
                        Timber::e,
                        () -> Timber.i("Successfully got Fitb's!")
                );
        mDisposables.add(disposable);
    }

    @Override
    public void subscribe() {
        loadCaptions();
        loadPickerInfo();
    }

    @Override
    public void unsubscribe() {
        mDisposables.clear();
    }
}
