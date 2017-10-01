package com.snaptiongame.app.presentation.view.login

/**
 * @author Tyler Wong
 */

class LoginPresenter(loginView: LoginContract.View) : LoginContract.Presenter {

    init {
        loginView.setPresenter(this)
    }

    override fun subscribe() {

    }

    override fun unsubscribe() {

    }
}
