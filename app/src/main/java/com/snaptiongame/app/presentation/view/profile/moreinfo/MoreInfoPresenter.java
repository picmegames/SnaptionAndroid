package com.snaptiongame.app.presentation.view.profile.moreinfo;

import com.snaptiongame.app.data.models.User;
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

    private void loadUser() {
        Disposable disposable = UserProvider.getUser(mUserId)
                .subscribe(
                        user -> {
                            loadRank(user);
                            loadMoreInfo(user);
                        }
                        , Timber::e);
        mDisposables.add(disposable);
    }

    private void loadRank(User user) {
        Disposable disposable = UserProvider.getRank(user.rankId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        rank -> mMoreInfoView.showUserInfo(rank.title, user.exp),
                        Timber::e
                );
        mDisposables.add(disposable);
    }

    private void loadMoreInfo(User user) {
        Disposable disposable = UserProvider.getUserStats(user.id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userStats -> mMoreInfoView.showMoreInfo(userStats),
                        Timber::e
                );
        mDisposables.add(disposable);
    }

    @Override
    public void subscribe() {
        loadUser();
    }

    @Override
    public void unsubscribe() {
        mDisposables.clear();
    }
}
