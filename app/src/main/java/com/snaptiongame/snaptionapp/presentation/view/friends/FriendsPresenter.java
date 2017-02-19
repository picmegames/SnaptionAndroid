package com.snaptiongame.snaptionapp.presentation.view.friends;

import android.support.annotation.NonNull;

import com.snaptiongame.snaptionapp.data.providers.FriendProvider;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

/**
 * Created by nickromero on 1/30/17.
 */

public class FriendsPresenter implements FriendsContract.Presenter {

    @NonNull
    private final FriendsContract.View mFriendView;

    @NonNull
    private CompositeDisposable mDisposables;

    private int mSnaptionUserId;

    public FriendsPresenter(@NonNull FriendsContract.View friendView, int userId) {
        mFriendView = friendView;
        mDisposables = new CompositeDisposable();
        mSnaptionUserId = userId;
        mFriendView.setPresenter(this);
    }

    @Override
    public void loadFriends() {
        Disposable disposable = FriendProvider
                .loadFriends(mSnaptionUserId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                      mFriendView::processFriends,
                      Timber::e,
                      () -> Timber.i("Getting friends was successful"));
        mDisposables.add(disposable);
    }

    @Override
    public void subscribe() { loadFriends();}

    @Override
    public void unsubscribe() { mDisposables.clear();}
}
