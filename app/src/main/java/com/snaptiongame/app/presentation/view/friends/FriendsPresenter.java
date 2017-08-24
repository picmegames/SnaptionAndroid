package com.snaptiongame.app.presentation.view.friends;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.snaptiongame.app.R;
import com.snaptiongame.app.SnaptionApplication;
import com.snaptiongame.app.data.auth.AuthManager;
import com.snaptiongame.app.data.models.AddFriendRequest;
import com.snaptiongame.app.data.models.Friend;
import com.snaptiongame.app.data.providers.FriendProvider;
import com.snaptiongame.app.data.providers.UserProvider;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

/**
 * @author Nick Romero
 */

public class FriendsPresenter implements FriendsContract.Presenter {

    @NonNull
    private final FriendsContract.View friendsView;

    @NonNull
    private CompositeDisposable disposables;

    public FriendsPresenter(@NonNull FriendsContract.View friendView) {
        friendsView = friendView;
        disposables = new CompositeDisposable();
        friendsView.setPresenter(this);
    }

    @Override
    public void findFriends(String query, int page) {
        disposables.clear();

        Disposable disposable = UserProvider.searchUsers(query, query, query, query, page)
                .flatMapIterable(users -> users)
                .map(Friend::new)
                .distinct(friend -> friend.id)
                .filter(friend -> friend.id != AuthManager.getUserId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        friendsView::addFriend,
                        Timber::e
                );

        this.disposables.add(disposable);
    }

    @Override
    public void loadFriends(int page) {
        disposables.clear();

        Disposable disposable = FriendProvider.getFriends(page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        friendsView::processFriends,
                        e -> {
                            Timber.e(e);
                            friendsView.showEmptyView();
                            friendsView.setRefreshing(false);
                        },
                        () -> {
                            Timber.i("Getting friends was successful");
                            friendsView.setRefreshing(false);
                        });
        this.disposables.add(disposable);
    }

    @Override
    public void removeFriend(String name, int friendId) {
        FriendProvider.removeFriend(new AddFriendRequest(friendId))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> Toast.makeText(SnaptionApplication.getContext(),
                                String.format(SnaptionApplication.getContext().getString(R.string.remove_friend_success), name),
                                Toast.LENGTH_SHORT).show(),
                        e -> {
                            Timber.e(e);
                            Toast.makeText(SnaptionApplication.getContext(),
                                    String.format(SnaptionApplication.getContext().getString(R.string.remove_friend_failure), name),
                                    Toast.LENGTH_SHORT).show();
                        }
                );
    }

    @Override
    public void addFriend(String name, int friendId) {
        FriendProvider.addFriend(new AddFriendRequest(friendId))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> Toast.makeText(SnaptionApplication.getContext(),
                                String.format(SnaptionApplication.getContext().getString(R.string.add_friend_success), name),
                                Toast.LENGTH_SHORT).show(),
                        e -> {
                            Timber.e(e);
                            Toast.makeText(SnaptionApplication.getContext(),
                                    String.format(SnaptionApplication.getContext().getString(R.string.add_friend_failure), name),
                                    Toast.LENGTH_SHORT).show();
                        }
                );
    }

    @Override
    public void subscribe() {
        friendsView.setRefreshing(true);
        loadFriends(1);
    }

    @Override
    public void unsubscribe() {
        disposables.clear();
    }
}
