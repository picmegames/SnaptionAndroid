package com.snaptiongame.app.presentation.view.wall;

import android.support.annotation.NonNull;

import com.snaptiongame.app.data.models.Game;
import com.snaptiongame.app.data.providers.GameProvider;

import java.util.List;

import io.reactivex.Single;
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
    private final WallContract.View wallView;
    @NonNull
    private CompositeDisposable disposables;

    private List<String> tags;
    private String status;
    private int userId;
    private int type;

    /**
     * This constructor creates a new instance of a Wall Presenter
     * and initializes a Composite Disposable.
     *
     * @param wallView The view that it will present to
     */
    public WallPresenter(@NonNull WallContract.View wallView, int userId, int type) {
        this.wallView = wallView;
        disposables = new CompositeDisposable();
        this.wallView.setPresenter(this);
        this.userId = userId;
        this.type = type;
    }

    /**
     * This method sends a network request to get a new list
     * of games from the server. It will send the request on
     * an IO thread and handle the result on the UI thread.
     */
    @Override
    public void loadGames(int type, List<String> tags, String status, int page) {
        if (tags != null) {
            this.tags = tags;
        }

        this.status = status;

        Single<List<Game>> gameRequest;
        switch (type) {
            case WallContract.DISCOVER:
                gameRequest = GameProvider.getGamesDiscover(this.tags, this.status, page);
                break;
            case WallContract.POPULAR:
                gameRequest = GameProvider.getGamesPopular(this.tags, this.status, page);
                break;
            case WallContract.HISTORY:
                gameRequest = GameProvider.getGamesHistory(userId, page);
                break;
            default:
                gameRequest = GameProvider.getGamesMine(this.tags, this.status, page);
                break;
        }

        Disposable disposable = gameRequest
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        games -> {
                            wallView.showGames(games);
                            Timber.i("Getting Snaptions completed successfully");
                            wallView.setRefreshing(false);
                        },
                        e -> {
                            Timber.e(e);
                            wallView.showDisconnectedView();
                            wallView.setRefreshing(false);
                        }
                );
        disposables.add(disposable);
    }

    /**
     * This method calls the method loadGames to be loaded
     * from the server.
     */
    @Override
    public void subscribe() {
        wallView.setRefreshing(true);
        loadGames(type, tags, status, 1);
    }

    /**
     * This method clears any disposables when the view is
     * destroyed.
     */
    @Override
    public void unsubscribe() {
        disposables.clear();
    }
}
