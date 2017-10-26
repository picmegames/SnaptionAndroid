package com.snaptiongame.app.presentation.view.shop

import com.snaptiongame.app.data.models.Offer
import com.snaptiongame.app.presentation.BasePresenter
import com.snaptiongame.app.presentation.BaseView

/**
 * @author Tyler Wong
 */

class ShopContract {
    /**
     * This is a template for a view.
     */
    interface View : BaseView<Presenter> {
        fun showSpecialOffer(offer: Offer)

        fun showSoftOffer(offer: Offer)

        fun showHardOffer(offer: Offer)

        fun onCompleted()
    }

    /**
     * This is a template for a presenter.
     */
    interface Presenter : BasePresenter {
        fun getOffers()
    }
}