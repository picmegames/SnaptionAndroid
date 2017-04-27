package com.snaptiongame.app.presentation.view.friends;

import android.support.annotation.NonNull;

import com.snaptiongame.app.data.models.AddFriendRequest;
import com.snaptiongame.app.data.models.Friend;
import com.snaptiongame.app.data.providers.FriendProvider;
import com.snaptiongame.app.data.providers.UserProvider;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
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

    private List<Friend> mFriends;

    public FriendsPresenter(@NonNull FriendsContract.View friendView) {
        mFriendView = friendView;
        mDisposables = new CompositeDisposable();
        mFriendView.setPresenter(this);
        mFriends = new ArrayList<>();
    }

    @Override
    public void findFriends(String query) {
        mDisposables.clear();

        Observable friends = FriendProvider
                .loadFriends()

                .flatMapIterable(friend -> friend)
                .filter(friend -> friend.username.contains(query));

        Observable email = UserProvider.getUserWithEmail(query).map(Friend::new).toObservable();
        Observable usernames = UserProvider.loadUsers(query).flatMapIterable(user -> user).map(Friend::new);
        Observable emailXusernames = Observable.concat(usernames.defaultIfEmpty("NO_EFFECT"), email.defaultIfEmpty("NO_EFFECT"));

        //Note the order of concat matter
        //If an observable ends up being empty, it will trash the entire call. That is dumb
        //This is solved by using .defaultIsEmpty("NO_EFFECT")
        Disposable disposable =
                Observable.concat(friends.defaultIfEmpty("NO_EFFECT"), emailXusernames)
                        .distinct()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                friend ->
                                {
                                    mFriendView.addFriend((Friend) friend);
                                },
                                e -> {
                                    Timber.e((Throwable) e);

                                });
        mDisposables.add(disposable);

    }

    @Override
    public void loadFriends() {
        Disposable disposable = FriendProvider.loadFriends()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        friends -> {
                            mFriendView.processFriends(friends);
                            mFriends = friends;
                        },
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
                        () -> Timber.i("Successfully removed friend!"),
                        Timber::e
                );
    }

    @Override
    public void addFriend(int friendId) {
        FriendProvider.addFriend(new AddFriendRequest(friendId))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    @Override
    public void searchFriends(String query) {
        mFriendView.processFriends(filterList(mFriends, query));
    }

    public static List<Friend> filterList(List<Friend> friends, String query) {
        if (query != null && query.length() > 0) {
            ArrayList<Friend> filtered = new ArrayList<>();
            for (Friend pal : friends) {
                String mashedNames = pal.fullName + " " + pal.username;
                if (mashedNames.toLowerCase().contains(query.toLowerCase())) {
                    filtered.add(pal);
                }
            }
            return filtered;
        }
        return friends;
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
