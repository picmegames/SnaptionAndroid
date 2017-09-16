package com.snaptiongame.app.presentation.view.profile;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.snaptiongame.app.R;
import com.snaptiongame.app.SnaptionApplication;
import com.snaptiongame.app.data.auth.AuthManager;
import com.snaptiongame.app.data.models.AddFriendRequest;
import com.snaptiongame.app.data.models.User;
import com.snaptiongame.app.data.providers.FriendProvider;
import com.snaptiongame.app.data.providers.UserProvider;
import com.snaptiongame.app.data.utils.ImageUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;
import timber.log.Timber;

/**
 * @author Tyler Wong
 */

public class ProfilePresenter implements ProfileContract.Presenter {
    @NonNull
    private ProfileContract.View profileView;
    @NonNull
    private CompositeDisposable disposables;

    private AuthManager authManager;
    private String encodedImage;

    public ProfilePresenter(@NonNull ProfileContract.View profileView) {
        this.profileView = profileView;
        disposables = new CompositeDisposable();
        this.profileView.setPresenter(this);
        authManager = AuthManager.getInstance();
    }

    @Override
    public void updateProfilePicture(User user) {
        Disposable disposable = UserProvider.updateUser(user)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        newUser -> {
                            profileView.saveProfilePicture(newUser.getImageUrl());
                            profileView.showProfilePictureSuccess();
                        },
                        e -> {
                            Timber.e(e);
                            profileView.showProfilePictureFailure();
                        }
                );
        disposables.add(disposable);
    }

    @Override
    public void addFriend(int userId) {
        Disposable disposable = FriendProvider.addFriend(new AddFriendRequest(userId))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        request -> {
                            profileView.showHideAddFriend(false);
                            profileView.showAddFriendResult(true);
                        },
                        e -> {
                            Timber.e(e);
                            profileView.showHideAddFriend(true);
                            profileView.showAddFriendResult(false);
                        }
                );
        disposables.add(disposable);
    }

    @Override
    public void removeFriend(int userId) {
        Disposable disposable = FriendProvider.removeFriend(new AddFriendRequest(userId))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            profileView.showHideAddFriend(true);
                            profileView.showRemoveFriendResult(true);
                        },
                        e -> {
                            profileView.showHideAddFriend(false);
                            profileView.showRemoveFriendResult(false);
                        }
                );
        disposables.add(disposable);
    }

    @Override
    public void loadUser(int userId) {
        Disposable disposable = UserProvider.getUser(userId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        profileView::showUser,
                        Timber::e
                );
        disposables.add(disposable);
    }

    @Override
    public void loadShouldHideAddFriend(int userId) {
        Disposable disposable = FriendProvider.isFriend(userId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        isFriend -> profileView.showHideAddFriend(!isFriend),
                        Timber::e
                );
        disposables.add(disposable);
    }

    @Override
    public void updateUsername(String oldUsername, User user) {
        Disposable disposable = UserProvider.updateUser(user)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        nextUser -> {
                            profileView.saveUsername(nextUser.getUsername());
                            profileView.showUsernameSuccess(oldUsername, user);
                        },
                        e -> {
                            Timber.e(e);
                            String msg = SnaptionApplication.Companion.getContext().getString(
                                    R.string.update_failure);
                            if (e instanceof HttpException) {
                                //Show the invalid character message if the error code is 500
                                if (((HttpException) e).code() == 500) {
                                    msg = SnaptionApplication.Companion.getContext().getString(
                                            R.string.invalid_char);
                                }
                            }
                            profileView.showUsernameFailure(msg);
                        }
                );
        disposables.add(disposable);
    }

    @Override
    public void convertImage(String type, Uri uri) {
        Disposable disposable = ImageUtils.getCompressedImage(uri)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        s -> encodedImage = s,
                        Timber::e,
                        () -> updateProfilePicture(new User(encodedImage, type))
                );
        disposables.add(disposable);
    }

    @Override
    public void logout() {
        if (AuthManager.isLoggedIn()) {
            authManager.logout();
            profileView.goToLogin();
        }
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        disposables.clear();
    }
}
