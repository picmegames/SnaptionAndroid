package com.snaptiongame.app;

import android.app.Application;
import android.content.Context;

import com.snaptiongame.app.data.authentication.AuthenticationManager;
import com.squareup.leakcanary.LeakCanary;

import io.branch.referral.Branch;
import timber.log.Timber;

/**
 * This is the entry point for the application. When the application is started up, the
 * AuthenticationManager, LeakCanary, and Timber are initialized.
 *
 * @author Tyler Wong
 * @version 1.0
 */
public class SnaptionApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        SnaptionApplication.context = getApplicationContext();

        // INIT Branch
        Branch.getAutoInstance(this);

        // INIT Authentication Manager
        AuthenticationManager.init(context);

        // INIT Leak Canary (Memory leak checking)
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }

        LeakCanary.install(this);

        if (BuildConfig.DEBUG) {
            // INIT Timber (Logger for debug builds)
            Timber.plant(new Timber.DebugTree());
        }
    }

    public static Context getContext() {
        return SnaptionApplication.context;
    }
}
