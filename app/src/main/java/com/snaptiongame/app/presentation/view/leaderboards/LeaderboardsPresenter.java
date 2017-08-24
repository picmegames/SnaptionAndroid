package com.snaptiongame.app.presentation.view.leaderboards;

import io.reactivex.disposables.CompositeDisposable;

/**
 * @author Tyler Wong
 */

public class LeaderboardsPresenter implements LeaderboardsContract.Presenter {

    private CompositeDisposable disposables;
    private int type;

    public LeaderboardsPresenter(LeaderboardsContract.View leaderboardView, int type) {
        leaderboardView.setPresenter(this);
        this.type = type;
    }

    @Override
    public void loadLeaderboard(int type) {

    }

    @Override
    public void subscribe() {
        loadLeaderboard(type);
    }

    @Override
    public void unsubscribe() {
        disposables.clear();
    }
}
