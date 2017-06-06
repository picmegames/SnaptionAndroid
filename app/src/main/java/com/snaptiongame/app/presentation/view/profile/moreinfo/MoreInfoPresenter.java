package com.snaptiongame.app.presentation.view.profile.moreinfo;

import com.snaptiongame.app.data.providers.UserProvider;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

/**
 * @author Tyler Wong
 */

public class MoreInfoPresenter implements MoreInfoContract.Presenter {

    private MoreInfoContract.View mMoreInfoView;
    private CompositeDisposable mDisposables;
    private int mUserId;

    public MoreInfoPresenter(MoreInfoContract.View moreInfoView, int userId) {
        this.mMoreInfoView = moreInfoView;
        this.mUserId = userId;
        mDisposables = new CompositeDisposable();
    }

    @Override
    public void loadMoreInfo(int userId) {
        Disposable disposable = UserProvider.getUserStats(userId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        mMoreInfoView::showUserInfo,
                        Timber::e
                );
        mDisposables.add(disposable);
    }

    @Override
    public void subscribe() {
        loadMoreInfo(mUserId);
    }

    @Override
    public void unsubscribe() {
        mDisposables.clear();
    }
}
