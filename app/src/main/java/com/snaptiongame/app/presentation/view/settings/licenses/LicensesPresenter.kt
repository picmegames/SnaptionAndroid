package com.snaptiongame.app.presentation.view.settings.licenses

import com.snaptiongame.app.data.providers.getLicenses
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import timber.log.Timber

/**
 * @author Tyler Wong
 */
class LicensesPresenter(private val licensesView: LicensesContract.View) : LicensesContract.Presenter {

    val disposables: CompositeDisposable = CompositeDisposable()

    override fun loadLicenses() {
        val disposable: Disposable = getLicenses(licensesView.getContext())
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
