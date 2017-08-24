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

    private MoreInfoContract.View moreInfoView;
    private CompositeDisposable disposables;
    private int userId;

    public MoreInfoPresenter(MoreInfoContract.View moreInfoView, int userId) {
        this.moreInfoView = moreInfoView;
        this.userId = userId;
        disposables = new CompositeDisposable();
    }

    @Override
    public void loadMoreInfo(int userId) {
        Disposable disposable = UserProvider.getUserStats(userId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        moreInfoView::showUserInfo,
                        Timber::e
                );
        disposables.add(disposable);
    }

    @Override
    public void subscribe() {
        loadMoreInfo(userId);
    }

    @Override
    public void unsubscribe() {
        disposables.clear();
    }
}
