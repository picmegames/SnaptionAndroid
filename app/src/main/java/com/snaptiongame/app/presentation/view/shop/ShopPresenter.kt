package com.snaptiongame.app.presentation.view.shop

import com.snaptiongame.app.data.providers.getAllOffers
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import timber.log.Timber

/**
 * @author Tyler Wong
 */
class ShopPresenter(private val shopView: ShopContract.View) : ShopContract.Presenter {

    val disposables: CompositeDisposable = CompositeDisposable()

    override fun getOffers() {
        val disposable: Disposable = getAllOffers()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    when {
                        it.special -> shopView.showSpecialOffer(it)
                        it.soft != 0 -> shopView.showSoftOffer(it)
                        else -> shopView.showHardOffer(it)
                    }
                }, Timber::e, shopView::onCompleted)
        disposables.add(disposable)
    }

    override fun subscribe() {
        getOffers()
    }

    override fun unsubscribe() {
        disposables.clear()
    }
}