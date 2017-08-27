package com.snaptiongame.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.snaptiongame.app.data.auth.AuthManager;

import io.branch.referral.Branch;
import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

/**
 * This is the entry point for the application. When the application is started up, the
 * AuthManager, Branch, Crashlytics, LeakCanary, and Timber are initialized.
 *
 * @author Tyler Wong
 * @version 1.0
 */
public class SnaptionApplication extends MultiDexApplication {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        SnaptionApplication.context = getApplicationContext();

        // INIT Branch
        Branch.getAutoInstance(this);

        // INIT Crashlytics
        Fabric.with(this, new Crashlytics());

        // INIT Authentication Manager
        AuthManager.init(context);

        if (BuildConfig.DEBUG) {
            // INIT Timber (Logger for debug builds)
            Timber.plant(new Timber.DebugTree());
        }

        // REFRESH if session has expired
        AuthManager.getInstance().refreshSession();
    }

    public static Context getContext() {
        return context;
    }
}
