package com.snaptiongame.app.data.services.notifications

import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import com.snaptiongame.app.data.auth.AuthManager
import com.snaptiongame.app.data.providers.*

/**
 * @author Tyler Wong
 */

class IdentificationService : FirebaseInstanceIdService() {
    override fun onTokenRefresh() {
        super.onTokenRefresh()

        if (AuthManager.isLoggedIn()) {
            sendRegistrationToServer(FirebaseInstanceId.getInstance().token)
        }
    }

    private fun sendRegistrationToServer(token: String?) {
        refreshNotificationToken(token ?: "")
    }
}