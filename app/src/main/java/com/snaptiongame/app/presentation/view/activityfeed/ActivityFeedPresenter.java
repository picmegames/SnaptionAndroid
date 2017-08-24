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

    private ActivityFeedContract.View activityFeedView;

    private CompositeDisposable disposables;

    public ActivityFeedPresenter(ActivityFeedContract.View activityFeedView) {
        this.activityFeedView = activityFeedView;
        this.activityFeedView.setPresenter(this);
        disposables = new CompositeDisposable();
    }

    @Override
    public void loadActivityFeed(int page) {
        Disposable disposable = ActivityFeedProvider.getActivityFeed(page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        items -> {
                            activityFeedView.addActivityFeedItems(items);
                            activityFeedView.setRefreshing(false);
                        },
                        e -> {
                            Timber.e(e);
                            activityFeedView.setRefreshing(false);
                            activityFeedView.showEmptyView();
                        }
                );
        disposables.add(disposable);
    }

    @Override
    public void subscribe() {
        activityFeedView.setRefreshing(true);
        loadActivityFeed(1);
    }

    @Override
    public void unsubscribe() {
        disposables.clear();
    }
}
