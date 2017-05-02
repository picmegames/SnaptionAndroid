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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private static final String EMOJI_REGEX = "([\\u20a0-\\u32ff\\ud83c\\udc00-\\ud83d\\udeff\\udbb9\\udce5-\\udbb9\\udcee])";

    public CreateGamePresenter(@NonNull CreateGameContract.View createGameView) {
        mCreateGameView = createGameView;
        mDisposables = new CompositeDisposable();
        mCreateGameView.setPresenter(this);
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

    public boolean containsEmojis(List<String> tags) {
        Matcher matcher;
        Pattern emojiPattern = Pattern.compile(EMOJI_REGEX);

        for (String tag : tags) {
            matcher = emojiPattern.matcher(tag);
            if (matcher.find()) {
                return true;
            }
        }
        return false;
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
    public List<Friend> getFriends() {
        return mFriends;
    }

    @Override
    public void loadFriends() {
        Disposable disposable = FriendProvider.getFriends()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::processFriends,
                        Timber::e,
                        () -> Timber.i("Friends loaded successfully!")
                );
        mDisposables.add(disposable);
    }

    private void processFriends(List<Friend> friends) {
        mFriends = friends;
        String[] names = new String[friends.size()];
        for (int index = 0; index < names.length; index++) {
            names[index] = friends.get(index).username;
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
