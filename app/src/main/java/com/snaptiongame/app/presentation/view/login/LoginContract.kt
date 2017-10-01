package com.snaptiongame.app.presentation.view.login

import com.snaptiongame.app.presentation.BasePresenter
import com.snaptiongame.app.presentation.BaseView

/**
 * @author Tyler Wong
 */

class LoginContract {
    interface View : BaseView<Presenter>

    interface Presenter : BasePresenter
}
