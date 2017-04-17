package com.snaptiongame.app.presentation.view.friends;

import android.support.annotation.NonNull;

import com.snaptiongame.app.data.models.AddFriendRequest;
import com.snaptiongame.app.data.providers.FriendProvider;

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

    public FriendsPresenter(@NonNull FriendsContract.View friendView) {
        mFriendView = friendView;
        mDisposables = new CompositeDisposable();
        mFriendView.setPresenter(this);
    }

    @Override
    public void loadFriends() {
        Disposable disposable = FriendProvider.loadFriends()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        mFriendView::processFriends,
                        e -> {
                            Timber.e(e);
                            mFriendView.showEmptyView();
                            mFriendView.setRefreshing(false);
                        },
                        () -> {
                            Timber.i("Getting friends was successful");
                            mFriendView.setRefreshing(false);
                        });
        mDisposables.add(disposable);
    }

    @Override
    public void removeFriend(int friendId) {
        FriendProvider.removeFriend(new AddFriendRequest(friendId))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            Timber.i("Successfully removed friend!");
                        },
                        Timber::e
                );
    }

    @Override
    public void subscribe() {
        loadFriends();
    }

    @Override
    public void unsubscribe() {
        mDisposables.clear();
    }
}
