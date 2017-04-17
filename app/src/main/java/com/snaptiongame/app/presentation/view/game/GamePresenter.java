package com.snaptiongame.app.presentation.view.game;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.snaptiongame.app.data.models.Caption;
import com.snaptiongame.app.data.models.CaptionSet;
import com.snaptiongame.app.data.models.DeepLinkRequest;
import com.snaptiongame.app.data.models.FitBCaption;
import com.snaptiongame.app.data.models.GameAction;
import com.snaptiongame.app.data.providers.CaptionProvider;
import com.snaptiongame.app.data.providers.DeepLinkProvider;
import com.snaptiongame.app.data.providers.FacebookShareProvider;
import com.snaptiongame.app.data.providers.GameProvider;
import com.snaptiongame.app.data.providers.UserProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    private int mGameId;
    private int mPickerId;
    private List<FitBCaption> mCaptions;

    public static final int MAX_FITBS_SHOWN = 8;

    public GamePresenter(int gameId, int pickerId, @NonNull GameContract.View view) {
        mGameId = gameId;
        mPickerId = pickerId;
        mGameView = view;
        mDisposables = new CompositeDisposable();
        mGameView.setPresenter(this);
        mCaptions = new ArrayList<>();
    }

    @Override
    public void loadCaptions() {
        Disposable disposable = CaptionProvider.getCaptions(mGameId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::processCaptions,
                        e -> {
                            Timber.e(e);
                            mGameView.setRefreshing(false);
                        },
                        () -> Timber.i("Loading captions completed successfully.")
                );
        mDisposables.add(disposable);
    }

    private void loadPickerInfo() {
        Disposable disposable = UserProvider.getUser(mPickerId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        user -> mGameView.setPickerInfo(user.imageUrl, user.username),
                        Timber::e
                );
        mDisposables.add(disposable);
    }

    @Override
    public void upvoteOrFlagGame(GameAction request) {
        Disposable disposable = GameProvider.upvoteOrFlagGame(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> mGameView.updateGame(request.choiceType),
                        Timber::e
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
                        this::loadCaptions,
                        e -> {
                            Timber.e(e);
                            mGameView.setRefreshing(false);
                        }
                );
        mDisposables.add(disposable);
    }

    @Override
    public void loadCaptionSets() {
        Disposable disposable = CaptionProvider.getCaptionSets()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        mGameView::showCaptionSets,
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
                        mGameView::showFitBCaptions,
                        Timber::e,
                        () -> Timber.i("Successfully got Fitb's!")
                );
        mDisposables.add(disposable);
    }

    @Override
    public void loadRandomFITBCaptions() {
        Disposable disposable = CaptionProvider.getCaptionSets()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::countSets,
                        Timber::e,
                        () -> Timber.i("Loading caption sets worked")
                );
        mDisposables.add(disposable);
    }

    private void countSets(List<CaptionSet> sets) {
        int numSets = sets.size();
        List<FitBCaption> captions = new ArrayList<>();
        getRandomCaptions(numSets, captions, 0);
    }

    private void buildRandomCaptions(List<FitBCaption> captions) {
        Random random = new Random();
        List<FitBCaption> randomCaptions = new ArrayList<>();

        for (int i = 0; i < MAX_FITBS_SHOWN; i++) {
            if (!captions.isEmpty()) {
                int nextCaption = random.nextInt(captions.size());
                randomCaptions.add(captions.remove(nextCaption));
            }
        }

        mGameView.showRandomCaptions(randomCaptions);
    }

    private void getRandomCaptions(int numSets, List<FitBCaption> captions, int start) {
        if (start == numSets) {
            buildRandomCaptions(mCaptions);
        }
        else {
            if (start == 0)
                mCaptions = new ArrayList<>();
            for (FitBCaption c : captions) {
                mCaptions.add(c);
            }
            final int nextStart = ++start;
            Disposable disposable = CaptionProvider.getFitBCaptions(start)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            fitbs -> getRandomCaptions(numSets, fitbs, nextStart),
                            Timber::e,
                            () -> Timber.i("Successfully got Fitb's!")
                    );
            mDisposables.add(disposable);
        }
    }

    @Override
    public void getBranchToken(int gameId) {
        DeepLinkRequest linkRequest = new DeepLinkRequest(gameId);
        DeepLinkProvider.getToken(linkRequest)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        token -> mGameView.generateInviteUrl(token),
                        Timber::e
                );
    }

    @Override
    public void refreshCaptions() {
        buildRandomCaptions(new ArrayList<>(mCaptions));
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
