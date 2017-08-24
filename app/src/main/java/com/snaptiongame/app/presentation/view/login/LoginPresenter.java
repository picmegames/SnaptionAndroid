package com.snaptiongame.app.presentation.view.login;

import android.support.annotation.NonNull;

/**
 * @author Tyler Wong
 */

public class LoginPresenter implements LoginContract.Presenter {

    @NonNull
    private LoginContract.View loginView;

    public LoginPresenter(@NonNull LoginContract.View view) {
        loginView = view;
        loginView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }
}
