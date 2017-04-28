package com.snaptiongame.app.presentation.view.friends;

import android.support.annotation.NonNull;

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
    private List<Friend> mMyFriendsSaved;

    private static final int EMAIL_QUERY = 1;
    private static final int USERNAMES_QUERY = 2;

    public FriendsPresenter(@NonNull FriendsContract.View friendView) {
        mFriendView = friendView;
        mDisposables = new CompositeDisposable();
        mFriendView.setPresenter(this);
        mFriends = new ArrayList<>();
        mMyFriendsSaved = new ArrayList<>();
    }

    @Override
    public void findFriends(String query) {
        Observable<Friend> friends = FriendProvider.loadFriends()
                .flatMapIterable(friend -> friend)
                .filter(friend -> checkMyFriendsWithQuery(query, friend));

        Observable<Friend> email = UserProvider.getUserWithEmail(query)
                .filter(user -> checkMyFriendsForDuplicate(user, EMAIL_QUERY))
                .map(this::convertPossibleFriend)
                .toObservable()
                .defaultIfEmpty(new Friend(-1));

        Observable<Friend> usernames = UserProvider.loadUsers(query)
                .flatMapIterable(user -> user)
                .filter(user -> checkMyFriendsForDuplicate(user, USERNAMES_QUERY))
                .map(Friend::new);

        //Observable emailXusernames = Observable.concat(usernames.defaultIfEmpty(new Friend(-1)), email.defaultIfEmpty(new Friend(-1)));

        //Note the order of concat matter
        //If an observable ends up being empty, it will trash the entire call. That is dumb
        //This is solved by using .defaultIsEmpty(new Friend(-1))
        Disposable disposable = Observable.concat(usernames, friends, email)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        friend -> {
                            //Handle the defaultIfEmpty case
                            if (friend.id != -1) {
                                mFriendView.addFriend(friend);
                            }
                        },
                        Timber::e
                );
        mDisposables.add(disposable);
    }

    /**
     * This filter is used to determine if a user received from an email call is already a friend.
     * Currently when we loadFriends() we do not get emails with the request. So we have to check if
     * the id from the email user matches any ids in our friends list. If it does we can create a new
     * Friend with the appropriate isSnaptionFriend bool set.
     *
     * @param posFriend Each friend emitted by the observable
     * @return a new friend with the correct isSnaptionFriend bool
     */
    private Friend convertPossibleFriend(User posFriend) {
        Friend newFriend = new Friend(posFriend);

        //Go through all of our friends
        for (Friend friend : mMyFriendsSaved) {

            //If we find a matching user then we want to use that user's info.
            if (friend.id == posFriend.id) {
                newFriend.isSnaptionFriend = true;
                return newFriend;
            }
        }
        return newFriend;
    }

    /**
     * This filter will scan our friends list with the entered query from the SearchActivity. At the
     * moment we want to check if the query can be found in EITHER the username or the email.
     *
     * @param query     The entered text in SearchActivity
     * @param posFriend friends emmitted by the loadFriends() Observable
     * @return true if a user matches the search query
     */
    private boolean checkMyFriendsWithQuery(String query, Friend posFriend) {

        //We have to check that the searchable fields are not null as certain network calls
        //don't initialize username/email fields
        return (posFriend.username != null && posFriend.username.contains(query)) ||
                (posFriend.email != null) && posFriend.email.contains(query);
    }

    /**
     * This filter will scan our friends to see if they would have already been loaded y an observable.
     * We want to check this because if we have duplicate users, we want to only display the ones that
     * are our friends.
     *
     * @param user       User emitted by an observable
     * @param whichQuery The type of observable we are checking against.
     * @return true if a user is not in our friends list
     */
    private boolean checkMyFriendsForDuplicate(User user, int whichQuery) {
        for (Friend friend : mMyFriendsSaved) {
            //True if we already have this queried user in our friends list
            if (friend.id == user.id) {
                //If this is an email query we return true because our friends list does not contain any
                //users with emails. The filtering of this will be handled in convertPossibleFriend
                return whichQuery == EMAIL_QUERY;
            }
        }
        return true;
    }

    @Override
    public void loadFriends() {
        Disposable disposable = FriendProvider.loadFriends()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        friends -> {
                            mFriendView.processFriends(friends);
                            mFriends = friends;

                            if (mMyFriendsSaved.isEmpty()) {
                                mMyFriendsSaved = new ArrayList<>(mFriends);
                            }
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
    public void addFriendTemp(Friend friend) {
        mMyFriendsSaved.add(friend);
    }


    public void removeTempFriend(int id) {
        for (int i = 0; i < mMyFriendsSaved.size(); i++)
            if (mMyFriendsSaved.get(i).id == id)
                mMyFriendsSaved.remove(i);
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
