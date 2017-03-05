package com.snaptiongame.app.presentation.view.creategame;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.snaptiongame.app.data.models.Friend;
import com.snaptiongame.app.data.models.Snaption;
import com.snaptiongame.app.data.providers.FriendProvider;
import com.snaptiongame.app.data.providers.SnaptionProvider;
import com.snaptiongame.app.data.utils.ImageConverter;

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
    private int mUserId;
    // private byte[] mEncodedImage;
    private String mEncodedImage;

    public CreateGamePresenter(int userId, @NonNull CreateGameContract.View createGameView) {
        mUserId = userId;
        mCreateGameView = createGameView;
        mDisposables = new CompositeDisposable();
        mCreateGameView.setPresenter(this);
    }

    @Override
    public void createGame(String type, Uri uri, int userId, boolean isPublic) {
        Disposable disposable = ImageConverter.getCompressedImage(uri)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        s -> mEncodedImage = s,
                        Timber::e,
                        () -> uploadGame(userId, isPublic, type)
                );
        mDisposables.add(disposable);
    }

    private void uploadGame(int userId, boolean isPublic, String type) {
        Disposable disposable = SnaptionProvider.addSnaption(
                new Snaption(userId, isPublic, 1, mEncodedImage, type, mCreateGameView.getTags(),
                        getFriendIds(mCreateGameView.getAddedFriends())))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        snaption -> {
                        },
                        Timber::e,
                        mCreateGameView::showUploadComplete
                );
        mDisposables.add(disposable);
    }

//    private void startUploadService(int userId, boolean isPublic, String type) {
//        Context context = mCreateGameView.getContext();
//        Bundle uploadBundle = new Bundle();
//        uploadBundle.putInt(Snaption.ID, userId);
//        uploadBundle.putBoolean(Snaption.IS_PUBLIC, isPublic);
//        uploadBundle.putByteArray(Snaption.PICTURE, mEncodedImage);
//        uploadBundle.putString(Snaption.IMG_TYPE, type);
//        uploadBundle.putIntegerArrayList(Snaption.FRIENDS, getFriendIds(mCreateGameView.getAddedFriends()));
//        Intent uploadIntent = new Intent(context, SnaptionUploadService.class);
//        uploadIntent.putExtras(uploadBundle);
//        context.startService(uploadIntent);
//        mCreateGameView.onBackPressed();
//    }

    private List<Integer> getFriendIds(List<String> friendNames) {
        List<Integer> friendIds = new ArrayList<>();
        for (String friendName : friendNames) {
            friendIds.add(getFriendIdByName(friendName));
        }
        return friendIds;
    }

    @Override
    public int getFriendIdByName(String name) {
        for (Friend friend : mFriends) {
            if (friend.userName.equals(name)) {
                return friend.id;
            }
        }
        return -1;
    }

    @Override
    public List<Friend> getFriends() {
        return mFriends;
    }

    @Override
    public void loadFriends() {
        Disposable disposable = FriendProvider.loadFriends(mUserId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::processFriends,
                        Timber::e,
                        mCreateGameView::showFriendsDialog
                );
        mDisposables.add(disposable);
    }

    private void processFriends(List<Friend> friends) {
        mFriends = friends;
        String[] names = new String[friends.size()];
        for (int index = 0; index < names.length; index++) {
            names[index] = friends.get(index).userName;
        }
        mCreateGameView.setFriendNames(names);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        mDisposables.clear();
    }
}
