package com.snaptiongame.app.data.services.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.snaptiongame.app.R;
import com.snaptiongame.app.data.auth.AuthManager;
import com.snaptiongame.app.data.models.Game;
import com.snaptiongame.app.data.models.User;
import com.snaptiongame.app.presentation.view.game.GameActivity;
import com.snaptiongame.app.presentation.view.main.MainActivity;
import com.snaptiongame.app.presentation.view.profile.ProfileActivity;

import java.util.Date;
import java.util.Map;

/**
 * @author Tyler Wong
 */

public class NotificationService extends FirebaseMessagingService {

    private static final long THOUSAND = 1000L;
    private static final String TYPE = "type";
    private static final String TITLE = "title";
    private static final String MESSAGE = "message";
    private static final String GAME = "game";
    private static final String FRIEND = "user";
    private static final String PICTURE = "picture";
    public static final String FROM_NOTIFICATION = "notification";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();
        String type;

        if (data.containsKey(TYPE)) {
            type = data.get(TYPE);

            if (type.equals(GAME) && AuthManager.isGameNotificationsEnabled()) {
                handleNotification(data, type);
            }
            else if(type.equals(FRIEND) && AuthManager.isFriendNotificationsEnabled()) {
                handleNotification(data, type);
            }
        }
    }

    private void handleNotification(Map<String, String> data, String type) {
        String title = "";
        String message = "";

        if (data.size() > 0) {
            if (data.containsKey(TITLE) && data.containsKey(MESSAGE)) {
                title = data.get(TITLE);
                message = data.get(MESSAGE);
            }

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .setContentTitle(title)
                    .setContentText(message);

            Intent resultIntent = new Intent(this, MainActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

            if (type.equals(GAME)) {
                resultIntent = new Intent(this, GameActivity.class);

                if (data.containsKey(Game.ID)) {
                    resultIntent.putExtra(Game.ID, Integer.valueOf(data.get(Game.ID)));
                }
                if (data.containsKey(PICTURE)) {
                    resultIntent.putExtra(Game.IMAGE_URL, data.get(PICTURE));
                }
                resultIntent.putExtra(FROM_NOTIFICATION, true);
                stackBuilder.addParentStack(GameActivity.class);
            }
            else if (type.equals(FRIEND)) {
                resultIntent = new Intent(this, ProfileActivity.class);

                if (data.containsKey(User.ID)) {
                    resultIntent.putExtra(User.ID, Integer.valueOf(data.get(User.ID)));
                }
                resultIntent.putExtra(ProfileActivity.IS_CURRENT_USER, false);
                stackBuilder.addParentStack(ProfileActivity.class);
            }

            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(resultPendingIntent);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify((int) ((new Date().getTime() / THOUSAND) % Integer.MAX_VALUE), builder.build());
        }
    }
}
