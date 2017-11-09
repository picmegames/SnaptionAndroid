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

    public LeaderboardsPresenter(LeaderboardsContract.View leaderboardView, int type) {
        this.leaderboardView = leaderboardView;
        this.type = type;
        leaderboardView.setPresenter(this);
        disposables = new CompositeDisposable();
    }

    @Override
    public void loadLeaderboard(int type) {
        Disposable disposable = LeaderboardProvider.getExperienceLeaderboard()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(leaderboardView::setLeaderboard, Timber::e);
        disposables.add(disposable);
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
