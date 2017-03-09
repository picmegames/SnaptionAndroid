package com.snaptiongame.app.presentation.view.wall;

import android.support.annotation.NonNull;

import com.snaptiongame.app.data.models.Game;
import com.snaptiongame.app.data.providers.GameProvider;

import java.util.List;

import io.reactivex.Observable;
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

    private int mUserId;
    private int mType;

    /**
     * This constructor creates a new instance of a Wall Presenter
     * and initializes a Composite Disposable.
     *
     * @param wallView The view that it will present to
     */
    public WallPresenter(@NonNull WallContract.View wallView, int userId, int type) {
        mWallView = wallView;
        mDisposables = new CompositeDisposable();
        mWallView.setPresenter(this);
        mUserId = userId;
        mType = type;
    }

    /**
     * This method sends a network request to get a new list
     * of games from the server. It will send the request on
     * an IO thread and handle the result on the UI thread.
     */
    @Override
    public void loadGames(int type) {
        Observable<List<Game>> gameRequest;
        switch (type) {
            case WallContract.DISCOVER:
                //gameRequest = GameProvider.getDiscoverGames();
                gameRequest = GameProvider.getGames(false);
                break;
            case WallContract.POPULAR:
                //gameRequest = GameProvider.getPopularGames();
                gameRequest = GameProvider.getGames(false);
                break;
            case WallContract.HISTORY:
                gameRequest = GameProvider.getUserGameHistory(mUserId);
                break;
            default:
                gameRequest = GameProvider.getGames(true);
                break;
        }

        Disposable disposable = gameRequest
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        mWallView::showGames,
                        e -> {
                            Timber.e(e);
                            mWallView.setRefreshing(false);
                        },
                        () -> {
                            Timber.i("Getting Snaptions completed successfully");
                            mWallView.setRefreshing(false);
                        }
                );
        mDisposables.add(disposable);
    }

    /**
     * This method calls the method loadGames to be loaded
     * from the server.
     */
    @Override
    public void subscribe() {
        loadGames(mType);
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
