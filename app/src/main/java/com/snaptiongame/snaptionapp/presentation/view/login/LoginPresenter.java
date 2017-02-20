package com.snaptiongame.snaptionapp.presentation.view.login;

import android.support.annotation.NonNull;

/**
 * @author Tyler Wong
 */

public class LoginPresenter implements LoginContract.Presenter {

    @NonNull
    private LoginContract.View mLoginView;

    public LoginPresenter(@NonNull LoginContract.View view) {
        mLoginView = view;
        mLoginView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }
}
