package com.snaptiongame.app.presentation.view.settings;

import android.support.annotation.NonNull;

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

    @NonNull
    private PreferencesContract.View preferencesView;
    @NonNull
    private CompositeDisposable disposables;

    private AuthManager authManager;

    public PreferencesPresenter(PreferencesContract.View preferencesView) {
        this.preferencesView = preferencesView;
        this.preferencesView.setPresenter(this);
        authManager = AuthManager.getInstance();
        disposables = new CompositeDisposable();
    }

    @Override
    public void loadCacheSize() {
        preferencesView.updateCacheSummary(CacheUtils.getCacheSize());
    }

    @Override
    public void clearCache() {
        Disposable disposable = CacheUtils.clearCache()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                            preferencesView.clearCacheSuccess();
                            loadCacheSize();
                        },
                        e -> {
                            Timber.e(e);
                            preferencesView.clearCacheFailure();
                        });
        disposables.add(disposable);
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
        authManager.logout();
    }

    @Override
    public void subscribe() {
        loadCacheSize();
        preferencesView.updateLoginSummary();
    }

    @Override
    public void unsubscribe() {
        disposables.clear();
    }
}
