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
    private CreateGameContract.View mCreateGameView;
    @NonNull
    private CompositeDisposable mDisposables;

    private List<Friend> mFriends;
    // private byte[] mEncodedImage;
    private String mEncodedImage;

    public CreateGamePresenter(@NonNull CreateGameContract.View createGameView) {
        mCreateGameView = createGameView;
        mDisposables = new CompositeDisposable();
        mCreateGameView.setPresenter(this);
        mFriends = new ArrayList<>();
    }

    @Override
    public void createGame(String type, Uri uri, int userId, boolean isPublic, long gameDuration) {
        Disposable disposable = ImageUtils.getCompressedImage(uri)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        image -> mEncodedImage = image,
                        e -> {
                            Timber.e(e);
                            mCreateGameView.showImageCompressionFailure();
                        },
                        () -> uploadGame(userId, isPublic, type, gameDuration)
                );
        mDisposables.add(disposable);
    }

    @Override
    public void createGameFromId(int gameId, int userId, boolean isPublic, long gameDuration) {
        Disposable disposable = GameProvider.addGame(
                new Game(gameId, userId, isPublic, mCreateGameView.getTags(),
                        getFriendIds(mCreateGameView.getAddedFriends()), gameDuration))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        mCreateGameView::showUploadComplete,
                        e -> {
                            Timber.e(e);
                            mCreateGameView.showUploadFailure();
                        }
                );
        mDisposables.add(disposable);
    }

    private void uploadGame(int userId, boolean isPublic, String type, long gameDuration) {
        Disposable disposable = GameProvider.addGame(
                new Game(userId, isPublic, mEncodedImage, type, mCreateGameView.getTags(),
                        getFriendIds(mCreateGameView.getAddedFriends()), gameDuration))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        mCreateGameView::showUploadComplete,
                        e -> {
                            Timber.e(e);
                            mCreateGameView.showUploadFailure();
                        }
                );
        mDisposables.add(disposable);
    }

//    private void startUploadService(int userId, boolean isPublic, String type) {
//        Context context = mCreateGameView.getContext();
//        Bundle uploadBundle = new Bundle();
//        uploadBundle.putInt(Game.ID, userId);
//        uploadBundle.putBoolean(Game.IS_PUBLIC, isPublic);
//        uploadBundle.putByteArray(Game.PICTURE, mEncodedImage);
//        uploadBundle.putString(Game.IMG_TYPE, type);
//        uploadBundle.putIntegerArrayList(Game.FRIENDS, getFriendIds(mCreateGameView.getAddedFriends()));
//        Intent uploadIntent = new Intent(context, GameUploadService.class);
//        uploadIntent.putExtras(uploadBundle);
//        context.startService(uploadIntent);
//        mCreateGameView.onBackPressed();
//    }

    private List<Integer> getFriendIds(List<String> friendNames) {
        List<Integer> friendIds = new ArrayList<>();
        for (String name : friendNames) {
            friendIds.add(getFriendIdByName(name));
        }
        return friendIds;
    }

    @Override
    public int getFriendIdByName(String name) {
        if (mFriends != null) {
            for (Friend friend : mFriends) {
                if (friend.username.equals(name)) {
                    return friend.id;
                }
            }
        }
        return -1;
    }

    @Override
    public boolean isValidFriends() {
        return !getFriendIds(mCreateGameView.getAddedFriends()).contains(-1);
    }

    @Override
    public List<Friend> getFriends() {
        return mFriends;
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
        mDisposables.add(disposable);
    }

    private void processFriends(List<Friend> friends) {
        mFriends.addAll(friends);
        List<String> names = new ArrayList<>();
        for (Friend friend : friends) {
            names.add(friend.username);
        }
        mCreateGameView.addFriendNames(names);
    }

    @Override
    public void subscribe() {
        mFriends.clear();
        loadFriends();
    }

    @Override
    public void unsubscribe() {
        mDisposables.clear();
    }
}
