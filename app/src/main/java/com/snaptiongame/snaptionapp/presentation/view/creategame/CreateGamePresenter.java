package com.snaptiongame.snaptionapp.presentation.view.creategame;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.snaptiongame.snaptionapp.data.models.Friend;
import com.snaptiongame.snaptionapp.data.models.Snaption;
import com.snaptiongame.snaptionapp.data.providers.FriendProvider;
import com.snaptiongame.snaptionapp.data.services.SnaptionUploadService;
import com.snaptiongame.snaptionapp.data.utils.ImageConverter;

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
    private byte[] mEncodedImage;

    public CreateGamePresenter(int userId, @NonNull CreateGameContract.View createGameView) {
        mUserId = userId;
        mCreateGameView = createGameView;
        mDisposables = new CompositeDisposable();
        mCreateGameView.setPresenter(this);
    }

    @Override
    public void createGame(ContentResolver resolver, Uri uri, Drawable drawable, int userId, boolean isPublic) {
        Disposable disposable = ImageConverter.convertImageByteArray(resolver, uri)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        s -> mEncodedImage = s,
                        Timber::e,
                        () -> startUploadService(userId, isPublic, resolver.getType(uri))
                );
        mDisposables.add(disposable);
    }

    private void startUploadService(int userId, boolean isPublic, String type) {
        Context context = mCreateGameView.getContext();
        Bundle uploadBundle = new Bundle();
        uploadBundle.putInt(Snaption.ID, userId);
        uploadBundle.putBoolean(Snaption.IS_PUBLIC, isPublic);
        uploadBundle.putByteArray(Snaption.PICTURE, mEncodedImage);
        uploadBundle.putString(Snaption.IMG_TYPE, type);
        uploadBundle.putIntegerArrayList(Snaption.FRIENDS, getFriendIds(mCreateGameView.getAddedFriends()));
        Intent uploadIntent = new Intent(context, SnaptionUploadService.class);
        uploadIntent.putExtras(uploadBundle);
        context.startService(uploadIntent);
        mCreateGameView.onBackPressed();
    }

    private ArrayList<Integer> getFriendIds(List<String> friendNames) {
        ArrayList<Integer> friendIds = new ArrayList<>();
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
        return 0;
    }

    private void loadFriends() {
        Disposable disposable = FriendProvider.loadFriends(mUserId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::processFriends,
                        Timber::e,
                        () -> Timber.i("Getting friends was successful")
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
        loadFriends();
    }

    @Override
    public void unsubscribe() {
        mDisposables.clear();
    }
}
