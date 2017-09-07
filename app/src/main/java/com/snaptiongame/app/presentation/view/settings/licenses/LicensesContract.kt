package com.snaptiongame.app.presentation.view.settings.licenses

import android.content.Context
import com.snaptiongame.app.data.models.License
import com.snaptiongame.app.presentation.BasePresenter
import com.snaptiongame.app.presentation.BaseView

/**
 * @author Tyler Wong
 */

class LicensesContract {
    /**
     * This is a template for a view.
     */
    interface View : BaseView<Presenter> {
        fun showLicenses(licenses: List<License>)

        fun getContext(): Context
    }

    /**
     * This is a template for a presenter.
     */
    interface Presenter : BasePresenter {
        fun loadLicenses()
    }
}
