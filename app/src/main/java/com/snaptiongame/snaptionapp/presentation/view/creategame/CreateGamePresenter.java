package com.snaptiongame.snaptionapp.presentation.view.creategame;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;

import com.snaptiongame.snaptionapp.R;
import com.snaptiongame.snaptionapp.SnaptionApplication;
import com.snaptiongame.snaptionapp.data.models.Friend;
import com.snaptiongame.snaptionapp.data.models.Snaption;
import com.snaptiongame.snaptionapp.data.providers.FriendProvider;
import com.snaptiongame.snaptionapp.data.providers.SnaptionProvider;
import com.snaptiongame.snaptionapp.data.utils.ImageConverter;
import com.snaptiongame.snaptionapp.presentation.view.MainActivity;

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

    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;
    private List<Friend> mFriends;
    private int mUserId;
    private String mEncodedImage;

    private static final int MAX_PROGRESS = 0;
    private static final int CURRENT_PROGRESS = 0;
    private static final int ID = 100;

    public CreateGamePresenter(int userId, @NonNull CreateGameContract.View createGameView) {
        mUserId = userId;
        mCreateGameView = createGameView;
        mDisposables = new CompositeDisposable();
        mCreateGameView.setPresenter(this);
    }

    @Override
    public void createGame(ContentResolver resolver, Uri uri, Drawable drawable, int userId, boolean isPublic) {
        ImageConverter.convertImage(resolver, uri)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        s -> mEncodedImage = s,
                        Timber::e,
                        () -> {
                            addSnaption(resolver.getType(uri), userId, isPublic);
                            showUploadProgressNotification();
                        }
                );
    }

    private void addSnaption(String type, int userId, boolean isPublic) {
        SnaptionProvider.addSnaption(new Snaption(userId, !isPublic, 1, mEncodedImage, type))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        snaption -> {
                        },
                        Timber::e,
                        this::showUploadCompleteNotification
                );
    }

    private void showUploadProgressNotification() {
        Context context = SnaptionApplication.getContext();
        mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(SnaptionApplication.getContext());
        mBuilder.setContentTitle(context.getString(R.string.upload_title))
                .setContentText(context.getString(R.string.upload_message))
                .setSmallIcon(R.drawable.ic_notification);
        mBuilder.setProgress(MAX_PROGRESS, CURRENT_PROGRESS, true);
        mNotifyManager.notify(ID, mBuilder.build());
    }

    private void showUploadCompleteNotification() {
        Context context = SnaptionApplication.getContext();
        mBuilder = new NotificationCompat.Builder(SnaptionApplication.getContext());
        mBuilder.setContentTitle(context.getString(R.string.upload_complete_title))
                .setContentText(context.getString(R.string.upload_complete_message))
                .setSmallIcon(R.drawable.ic_notification);
        Intent resultIntent = new Intent(context, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        mNotifyManager.notify(ID, mBuilder.build());
    }

    @Override
    public Friend getFriendByName(String name) {
        for (Friend friend : mFriends) {
            if (friend.userName.equals(name)) {
                return friend;
            }
        }
        return null;
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
