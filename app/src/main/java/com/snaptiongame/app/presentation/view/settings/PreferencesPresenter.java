package com.snaptiongame.app.presentation.view.settings;

import com.snaptiongame.app.data.auth.AuthManager;
import com.snaptiongame.app.data.utils.CacheUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @author Tyler Wong
 */

public class PreferencesPresenter implements PreferencesContract.Presenter {

    private PreferencesContract.View mPreferencesView;
    private AuthManager mAuthManager;
    private CompositeDisposable mDisposables;

    public PreferencesPresenter(PreferencesContract.View preferencesView) {
        mPreferencesView = preferencesView;
        mPreferencesView.setPresenter(this);
        mAuthManager = AuthManager.getInstance();
        mDisposables = new CompositeDisposable();
    }

    @Override
    public void loadCacheSize() {
        mPreferencesView.updateCacheSummary(CacheUtils.getCacheSize());
    }

    @Override
    public void clearCache() {
        Disposable disposable = CacheUtils.clearCache()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                            mPreferencesView.clearCacheSuccess();
                            loadCacheSize();
                        },
                        e -> {
                            Timber.e(e);
                            mPreferencesView.clearCacheFailure();
                        });
        mDisposables.add(disposable);
    }

    @Override
    public String getUsername() {
        return AuthManager.getUsername();
    }

    @Override
    public boolean isLoggedIn() {
        return AuthManager.isLoggedIn();
    }

    @Override
    public void logout() {
        mAuthManager.logout();
    }

    @Override
    public void subscribe() {
        loadCacheSize();
        mPreferencesView.updateLoginSummary();
    }

    @Override
    public void unsubscribe() {
        mDisposables.clear();
    }
}
