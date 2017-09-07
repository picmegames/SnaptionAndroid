package com.snaptiongame.app.presentation.view.creategame;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.snaptiongame.app.data.models.Friend;
import com.snaptiongame.app.data.models.Game;
import com.snaptiongame.app.data.providers.FriendProvider;
import com.snaptiongame.app.data.providers.GameProvider;
import com.snaptiongame.app.data.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @author Tyler Wong
 */

public class CreateGamePresenter implements CreateGameContract.Presenter {
    @NonNull
    private CreateGameContract.View createGameView;
    @NonNull
    private CompositeDisposable disposables;

    private List<Friend> friends;
    private String encodedImage;

    private static final int INVALID_FRIEND = -1;

    public CreateGamePresenter(@NonNull CreateGameContract.View createGameView) {
        this.createGameView = createGameView;
        disposables = new CompositeDisposable();
        this.createGameView.setPresenter(this);
        friends = new ArrayList<>();
    }

    @Override
    public void createGame(String type, Uri uri, boolean isPublic, long gameDuration) {
        Disposable disposable = ImageUtils.getCompressedImage(uri)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        image -> encodedImage = image,
                        e -> {
                            Timber.e(e);
                            createGameView.showImageCompressionFailure();
                        },
                        () -> uploadGame(isPublic, type, gameDuration)
                );
        disposables.add(disposable);
    }

    @Override
    public void createGameFromId(int gameId, boolean isPublic, long gameDuration) {
        Disposable disposable = GameProvider.addGame(
                new Game(gameId, isPublic, createGameView.getTags(),
                        getFriendIds(createGameView.getAddedFriends()), gameDuration))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        createGameView::showUploadComplete,
                        e -> {
                            Timber.e(e);
                            createGameView.showUploadFailure();
                        }
                );
        disposables.add(disposable);
    }

    private void uploadGame(boolean isPublic, String type, long gameDuration) {
        Disposable disposable = GameProvider.addGame(
                new Game(isPublic, encodedImage, type, createGameView.getTags(),
                        getFriendIds(createGameView.getAddedFriends()), gameDuration))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        createGameView::showUploadComplete,
                        e -> {
                            Timber.e(e);
                            createGameView.showUploadFailure();
                        }
                );
        disposables.add(disposable);
    }

    private List<Integer> getFriendIds(List<String> friendNames) {
        List<Integer> friendIds = new ArrayList<>();
        for (String name : friendNames) {
            int id = getFriendIdByName(name);

            // IF the friend id is valid
            if (id != INVALID_FRIEND) {
                friendIds.add(id);
            }
        }
        return friendIds;
    }

    @Override
    public int getFriendIdByName(String name) {
        if (friends != null) {
            for (Friend friend : friends) {
                if (friend.getUsername().equals(name)) {
                    return friend.getId();
                }
            }
        }
        return INVALID_FRIEND;
    }

    @Override
    public List<Friend> getFriends() {
        return friends;
    }

    @Override
    public void loadFriends() {
        Disposable disposable = FriendProvider.getAllFriends()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::processFriends,
                        Timber::e,
                        () -> Timber.i("Friends loaded successfully!")
                );
        disposables.add(disposable);
    }

    private void processFriends(List<Friend> friends) {
        this.friends.addAll(friends);
        List<String> names = new ArrayList<>();
        for (Friend friend : friends) {
            names.add(friend.getUsername());
        }
        createGameView.addFriendNames(names);
    }

    @Override
    public void subscribe() {
        friends.clear();
        loadFriends();
    }

    @Override
    public void unsubscribe() {
        disposables.clear();
    }
}
