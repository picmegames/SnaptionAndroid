package com.snaptiongame.app.presentation.view.friends;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.snaptiongame.app.R;
import com.snaptiongame.app.SnaptionApplication;
import com.snaptiongame.app.data.models.AddFriendRequest;
import com.snaptiongame.app.data.models.Friend;
import com.snaptiongame.app.data.models.User;
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

    private static final int EMAIL_QUERY = 1;
    private static final int USERNAMES_QUERY = 2;
    private static final int FULLNAME_QUERY = 3;

    public FriendsPresenter(@NonNull FriendsContract.View friendView) {
        mFriendView = friendView;
        mDisposables = new CompositeDisposable();
        mFriendView.setPresenter(this);
        mFriends = new ArrayList<>();
    }

    @Override
    public void findFriends(String query) {
        mDisposables.clear();

        Observable<Friend> fullnameResults = Observable.empty();

        Observable<Friend> friendResults = FriendProvider.getFriends()
                .flatMapIterable(friend -> friend)
                .filter(friend -> checkMyFriendsWithQuery(query, friend));

        Observable<Friend> emailResults = UserProvider.getUsersWithEmail(query)
                .flatMapIterable(user -> user)
                .filter(user -> checkMyFriendsForDuplicate(user, EMAIL_QUERY))
                .map(Friend::new);

        Observable<Friend> usernameResults = UserProvider.getUsersByUsername(query)
                .flatMapIterable(user -> user)
                .filter(user -> checkMyFriendsForDuplicate(user, USERNAMES_QUERY))
                .map(Friend::new);

        //Don't want to load friends based on whitespace
        if (!query.isEmpty()) {
            fullnameResults = UserProvider.getUsersByFullName(query)
                    .flatMapIterable(user -> user)
                    .filter(user -> checkMyFriendsForDuplicate(user, FULLNAME_QUERY))
                    .map(Friend::new);
        }

        // Do we need to? They should come up from the username search
        // We need this if we want to specify that they came from Facebook
//        Observable<Friend> facebook = FriendProvider.getFacebookFriends()
//                .flatMapIterable(friend -> friend)
//                .filter(friend -> checkMyFriendsWithQuery(query, friend));

        //Note the order of concat matter
        //If an observable ends up being empty, it will trash the entire call. That is dumb
        //This is solved by using .defaultIsEmpty(new Friend(-1))
        Disposable disposable = Observable.concat(usernameResults, friendResults, emailResults, fullnameResults)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        mFriendView::addFriend,
                        Timber::e
                );
        mDisposables.add(disposable);
    }

    /**
     * This filter will scan our friends list with the entered query from the SearchActivity. At the
     * moment we want to check if the query can be found in EITHER the username or the email.
     *
     * @param query     The entered text in SearchActivity
     * @param posFriend friends emmitted by the getFriends() Observable
     * @return true if a user matches the search query
     */
    private boolean checkMyFriendsWithQuery(String query, Friend posFriend) {
        // We have to check that the searchable fields are not null as certain network calls
        // don't initialize username/email fields
        return (posFriend.username != null && posFriend.username.toLowerCase().contains(query.toLowerCase())) ||
                (posFriend.email != null) && posFriend.email.toLowerCase().contains(query.toLowerCase());
    }

    /**
     * This filter will scan our friends to see if they would have already been loaded y an observable.
     * We want to check this because if we have duplicate users, we want to only display the ones that
     * are our friends.
     *
     * @param user User emitted by an observable
     * @param whichQuery The type of observable we are checking against.
     * @return true if a user is not in our friends list
     */
    private boolean checkMyFriendsForDuplicate(User user, int whichQuery) {
        return !user.isFriend || whichQuery == EMAIL_QUERY;
    }

    @Override
    public void loadFriends() {
        Disposable disposable = FriendProvider.getFriends()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        friends -> {
                            mFriendView.processFriends(friends);
                            mFriends = new ArrayList<>(friends);
                        },
                        e -> {
                            Timber.e(e);
                            mFriendView.showFriendList();
                            mFriendView.setRefreshing(false);
                        },
                        () -> {
                            Timber.i("Getting friends was successful");
                            mFriendView.setRefreshing(false);
                        });
        mDisposables.add(disposable);
    }

    @Override
    public void removeFriend(String name, int friendId) {
        FriendProvider.removeFriend(new AddFriendRequest(friendId))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> Toast.makeText(SnaptionApplication.getContext(),
                                String.format(SnaptionApplication.getContext().getString(R.string.remove_friend_success), name),
                                Toast.LENGTH_LONG).show(),
                        e -> {
                            Timber.e(e);
                            Toast.makeText(SnaptionApplication.getContext(),
                                    String.format(SnaptionApplication.getContext().getString(R.string.remove_friend_failure), name),
                                    Toast.LENGTH_LONG).show();
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
                                Toast.LENGTH_LONG).show(),
                        e -> {
                            Timber.e(e);
                            Toast.makeText(SnaptionApplication.getContext(),
                                    String.format(SnaptionApplication.getContext().getString(R.string.add_friend_failure), name),
                                    Toast.LENGTH_LONG).show();
                        }
                );
    }

    @Override
    public void addFriendTemp(Friend friend) {
        mFriends.add(friend);
    }

    @Override
    public void removeTempFriend(Friend friend) {
        mFriends.remove(friend);
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
