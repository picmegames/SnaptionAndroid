package com.snaptiongame.app.data.services.notifications;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.snaptiongame.app.data.auth.AuthManager;
import com.snaptiongame.app.data.providers.NotificationProvider;

/**
 * @author Tyler Wong
 */

public class IdentificationService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        if (AuthManager.isLoggedIn()) {
            sendRegistrationToServer(FirebaseInstanceId.getInstance().getToken());
        }
    }

    private void sendRegistrationToServer(String token) {
        NotificationProvider.refreshNotificationToken(token);
    }
}