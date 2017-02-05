package com.snaptiongame.snaptionapp.presentation.view.friends;

import android.support.annotation.NonNull;

import com.google.android.gms.auth.api.Auth;
import com.snaptiongame.snaptionapp.data.authentication.AuthenticationManager;
import com.snaptiongame.snaptionapp.data.models.Friend;
import com.snaptiongame.snaptionapp.data.providers.FriendProvider;

import java.util.List;

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

    @NonNull
    private int mSnaptionUserId;

    public FriendsPresenter(@NonNull FriendsContract.View friendView, int userId) {
        mFriendView = friendView;
        mDisposables = new CompositeDisposable();
        mSnaptionUserId = userId;
        mFriendView.setPresenter(this);
    }

    @Override
    public void loadFriends() {

        mDisposables.clear();
        Disposable disposable = FriendProvider
                .loadFriends(mSnaptionUserId)
                .subscribe(this::processFriends,
                Timber::e,
                        () -> Timber.i("Getting friends was successful"));
        mDisposables.add(disposable);
    }

    private void processFriends(List<Friend> friends) {
        mFriendView.showFriends(friends);
    }

    @Override
    public void subscribe() { loadFriends();}

    @Override
    public void unsubscribe() { mDisposables.clear();}
}
