package com.snaptiongame.app.presentation.view.leaderboards;

import com.snaptiongame.app.data.providers.LeaderboardProvider;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

/**
 * @author Tyler Wong
 */

public class LeaderboardsPresenter implements LeaderboardsContract.Presenter {

    private LeaderboardsContract.View leaderboardView;
    private CompositeDisposable disposables;
    private int type;
    private boolean friendsOnly = false;

    public LeaderboardsPresenter(LeaderboardsContract.View leaderboardView, int type) {
        this.leaderboardView = leaderboardView;
        this.type = type;
        friendsOnly = this.type != LeaderboardsContract.FRIENDS;
        leaderboardView.setPresenter(this);
        disposables = new CompositeDisposable();
    }

    @Override
    public void loadLeaderboard() {
        Disposable disposable = LeaderboardProvider.getUserLeaderboard(friendsOnly)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(leaderboard -> {
                    leaderboardView.setLeaderboard(leaderboard);
                    leaderboardView.setRefreshing(false);
                }, e -> {
                    Timber.e(e);
                    leaderboardView.setRefreshing(false);
                });
        disposables.add(disposable);
    }

    @Override
    public void subscribe() {
        loadLeaderboard();
    }

    @Override
    public void unsubscribe() {
        disposables.clear();
    }
}
