package com.snaptiongame.app.presentation.view.settings.licenses

import com.snaptiongame.app.data.providers.getLicenses
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import timber.log.Timber

/**
 * @author Tyler Wong
 */
class LicensesPresenter(licensesView: LicensesContract.View) : LicensesContract.Presenter {

    val licensesView: LicensesContract.View = licensesView
    val disposables: CompositeDisposable = CompositeDisposable()

    override fun loadLicenses() {
        var disposable: Disposable = getLicenses(licensesView.getContext())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe (
                        licensesView::showLicenses,
                        Timber::e
                )
        disposables.add(disposable)
    }

    override fun subscribe() {
        loadLicenses()
    }

    override fun unsubscribe() {
        disposables.clear()
    }
}
