package com.snaptiongame.app.presentation.view.activityfeed;

import com.snaptiongame.app.data.providers.ActivityFeedProvider;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

/**
 * @author Tyler Wong
 */

public class ActivityFeedPresenter implements ActivityFeedContract.Presenter {

    private ActivityFeedContract.View mActivityFeedView;

    private CompositeDisposable mDisposables;

    public ActivityFeedPresenter(ActivityFeedContract.View activityFeedView) {
        mActivityFeedView = activityFeedView;
        mActivityFeedView.setPresenter(this);
        mDisposables = new CompositeDisposable();
    }

    @Override
    public void loadActivityFeed() {
        Disposable disposable = ActivityFeedProvider.getActivityFeed()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        mActivityFeedView::addActivityFeedItems,
                        Timber::e
                );
        mDisposables.add(disposable);
    }

    @Override
    public void subscribe() {
        loadActivityFeed();
    }

    @Override
    public void unsubscribe() {
        mDisposables.clear();
    }
}
