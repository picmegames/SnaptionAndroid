package com.snaptiongame.app.presentation.view.settings;

import com.snaptiongame.app.presentation.BasePresenter;
import com.snaptiongame.app.presentation.BaseView;

/**
 * @author Tyler Wong
 */

public class PreferencesContract {
    interface View extends BaseView<Presenter> {
        void updateCacheSummary(String cacheSize);

        void clearCacheSuccess();

        void clearCacheFailure();

        void updateLoginSummary();

        void goToLogin();
    }

    interface Presenter extends BasePresenter {
        void loadCacheSize();

        void clearCache();

        String getUsername();

        boolean isLoggedIn();

        void logout();
    }
}
