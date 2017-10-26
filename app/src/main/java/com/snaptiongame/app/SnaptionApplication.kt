package com.snaptiongame.app

import android.content.Context
import android.support.multidex.MultiDexApplication

import com.crashlytics.android.Crashlytics
import com.snaptiongame.app.data.auth.AuthManager
import com.snaptiongame.app.data.providers.api.ApiProvider

import io.branch.referral.Branch
import io.fabric.sdk.android.Fabric
import timber.log.Timber

/**
 * This is the entry point for the application. When the application is started up, the
 * AuthManager, Branch, Crashlytics, LeakCanary, and Timber are initialized.
 *
 * @author Tyler Wong
 * @version 1.0
 */
class SnaptionApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        SnaptionApplication.context = applicationContext

        // INIT Snaption API
        ApiProvider.init(this)

        // INIT Branch
        Branch.getAutoInstance(this)

        // INIT Crashlytics
        Fabric.with(this, Crashlytics())

        // INIT Authentication Manager
        AuthManager.init(this)

        if (BuildConfig.DEBUG) {
            // INIT Timber (Logger for debug builds)
            Timber.plant(Timber.DebugTree())
        }

        // REFRESH if session has expired
        AuthManager.getInstance().refreshSession()
    }

    companion object {
        var context: Context? = null
            private set
    }
}
