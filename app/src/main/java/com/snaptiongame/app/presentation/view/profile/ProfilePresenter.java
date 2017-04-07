package com.snaptiongame.app.presentation.view.profile;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.snaptiongame.app.R;
import com.snaptiongame.app.SnaptionApplication;
import com.snaptiongame.app.data.authentication.AuthenticationManager;
import com.snaptiongame.app.data.models.User;
import com.snaptiongame.app.data.providers.UserProvider;
import com.snaptiongame.app.data.utils.ImageConverter;

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
    private ProfileContract.View mProfileView;
    @NonNull
    private CompositeDisposable mDisposables;

    private AuthenticationManager mAuthManager;

    private String mEncodedImage;

    public ProfilePresenter(@NonNull ProfileContract.View profileView) {
        mProfileView = profileView;
        mDisposables = new CompositeDisposable();
        mProfileView.setPresenter(this);
        mAuthManager = AuthenticationManager.getInstance();
    }

    @Override
    public void updateProfilePicture(User user) {
        Disposable disposable = UserProvider.updateUser(user)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        newUser -> {
                            mProfileView.saveProfilePicture(newUser.imageUrl);
                            mProfileView.showProfilePictureSuccess();
                        },
                        e -> {
                            Timber.e(e);
                            mProfileView.showProfilePictureFailure();
                        }
                );
        mDisposables.add(disposable);
    }

    @Override
    public void updateUsername(String oldUsername, User user) {
        Disposable disposable = UserProvider.updateUser(user)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        nextUser -> {
                            mProfileView.saveUsername(nextUser.username);
                            mProfileView.showUsernameSuccess(oldUsername, user);
                        },
                        e -> {
                            Timber.e(e);
                            String msg = SnaptionApplication.getContext().getString(R.string.update_failure);
                            if (e instanceof HttpException) {
                                //Show the invalid character message if the error code is 500
                                if (((HttpException) e).code() == 500) {
                                    msg = SnaptionApplication.getContext().getString(R.string.invalid_char);
                                }
                            }
                            mProfileView.showUsernameFailure(msg);
                        }
                );
        mDisposables.add(disposable);
    }

    @Override
    public void convertImage(String type, Uri uri) {
        Disposable disposable = ImageConverter.getCompressedImage(uri)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        s -> mEncodedImage = s,
                        Timber::e,
                        () -> updateProfilePicture(new User(mEncodedImage, type))
                );
        mDisposables.add(disposable);
    }

    @Override
    public void logout() {
        if (mAuthManager.isLoggedIn()) {
            mAuthManager.logout();
            mProfileView.goToLogin();
        }
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        mDisposables.clear();
    }
}
