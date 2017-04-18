package com.snaptiongame.app;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;

import com.snaptiongame.app.data.auth.AuthManager;
import com.snaptiongame.app.data.utils.NetworkListener;
import com.squareup.leakcanary.LeakCanary;

import io.branch.referral.Branch;
import timber.log.Timber;

/**
 * This is the entry point for the application. When the application is started up, the
 * AuthManager, LeakCanary, and Timber are initialized.
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
        AuthManager.init(context);

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
