package com.snaptiongame.app.presentation.view.game;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.snaptiongame.app.data.auth.AuthManager;
import com.snaptiongame.app.data.models.Caption;
import com.snaptiongame.app.data.models.CaptionSet;
import com.snaptiongame.app.data.models.DeepLinkRequest;
import com.snaptiongame.app.data.models.FitBCaption;
import com.snaptiongame.app.data.models.GameAction;
import com.snaptiongame.app.data.providers.CaptionProvider;
import com.snaptiongame.app.data.providers.DeepLinkProvider;
import com.snaptiongame.app.data.providers.FacebookShareProvider;
import com.snaptiongame.app.data.providers.GameProvider;
import com.snaptiongame.app.data.utils.DateUtils;

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
    private GameContract.View gameView;
    @NonNull
    private CompositeDisposable disposables;

    private int gameId;
    private List<FitBCaption> captions;

    public static final int MAX_FITBS_SHOWN = 8;

    public GamePresenter(@NonNull GameContract.View view) {
        gameView = view;
        disposables = new CompositeDisposable();
        gameView.setPresenter(this);
        captions = new ArrayList<>();
    }

    @Override
    public void loadCaptions(int page) {
        Disposable disposable = CaptionProvider.getCaptions(gameId, page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        gameView::showCaptions,
                        e -> {
                            Timber.e(e);
                            gameView.setRefreshing(false);
                        }
                );
        disposables.add(disposable);
    }

    @Override
    public void upvoteOrFlagGame(GameAction request) {
        Disposable disposable = GameProvider.upvoteOrFlagGame(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> gameView.onGameUpdated(request.choiceType),
                        e -> {
                            gameView.onGameErrored(request.choiceType);
                            Timber.e(e);
                        }
                );
        disposables.add(disposable);
    }

    @Override
    public void loadGame(int gameId) {
        Disposable disposable = GameProvider.getGame(gameId, AuthManager.getInviteToken())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        game -> gameView.showGame(game.imageUrl, game.id, game.creatorId,
                                game.creatorName, game.creatorImage, game.beenUpvoted, game.beenFlagged,
                                DateUtils.INSTANCE.isPastNow(game.endDate), game.isPublic),
                        Timber::e
                );
        disposables.add(disposable);
    }

    @Override
    public void shareToFacebook(AppCompatActivity activity, ImageView image) {
        FacebookShareProvider.shareToFacebook(activity, image);
    }

    @Override
    public void addCaption(int fitbId, String caption) {
        Disposable disposable = CaptionProvider.addCaption(gameId, new Caption(fitbId, caption))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            gameView.resetScrollState();
                            loadCaptions(1);
                        },
                        e -> {
                            Timber.e(e);
                            gameView.setRefreshing(false);
                            gameView.showCaptionSubmissionError();
                        }
                );
        disposables.add(disposable);
    }

    @Override
    public void loadCaptionSets() {
        Disposable disposable = CaptionProvider.getCaptionSets()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        gameView::showCaptionSets,
                        Timber::e,
                        () -> Timber.i("Loading caption sets worked")
                );
        disposables.add(disposable);
    }

    @Override
    public void loadFitBCaptions(int setId) {
        Disposable disposable = CaptionProvider.getFitBCaptions(setId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        gameView::showFitBCaptions,
                        Timber::e,
                        () -> Timber.i("Successfully got Fitb's!")
                );
        disposables.add(disposable);
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
        disposables.add(disposable);
    }

    @Override
    public void loadInvitedUsers(int gameId) {
        Disposable disposable = GameProvider.getPrivateGameUsers(gameId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        gameView::showPrivateGameDialog,
                        Timber::e
                );
        disposables.add(disposable);
    }

    @Override
    public void loadTags(int gameId) {
        Disposable disposable = GameProvider.getGameTags(gameId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        gameView::showTags,
                        Timber::e
                );
        disposables.add(disposable);
    }

    @Override
    public void setGameId(int gameId) {
        this.gameId = gameId;
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
        gameView.showRandomCaptions(randomCaptions);
    }

    private void getRandomCaptions(int numSets, List<FitBCaption> captions, int start) {
        if (start == numSets) {
            buildRandomCaptions(new ArrayList<>(this.captions));
        }
        else {
            if (start == 0)
                this.captions = new ArrayList<>();
            for (FitBCaption c : captions) {
                this.captions.add(c);
            }
            final int nextStart = ++start;
            Disposable disposable = CaptionProvider.getFitBCaptions(start)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            fitbs -> getRandomCaptions(numSets, fitbs, nextStart),
                            Timber::e,
                            () -> Timber.i("Successfully got Fitb's!")
                    );
            disposables.add(disposable);
        }
    }

    @Override
    public void getBranchToken(int gameId) {
        DeepLinkProvider.getToken(new DeepLinkRequest(gameId))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        gameView::generateInviteUrl,
                        Timber::e
                );
    }

    @Override
    public void refreshCaptions() {
        buildRandomCaptions(new ArrayList<>(captions));
    }

    @Override
    public void subscribe() {
        loadCaptions(1);
        loadRandomFITBCaptions();
    }

    @Override
    public void unsubscribe() {
        disposables.clear();
    }
}
