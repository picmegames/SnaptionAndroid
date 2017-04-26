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
import com.snaptiongame.app.data.models.Game;
import com.snaptiongame.app.data.models.User;
import com.snaptiongame.app.presentation.view.game.GameActivity;
import com.snaptiongame.app.presentation.view.main.MainActivity;
import com.snaptiongame.app.presentation.view.profile.ProfileActivity;

/**
 * @author Tyler Wong
 */

public class NotificationService extends FirebaseMessagingService {

    private static final String TYPE = "type";
    private static final String GAME = "game";
    private static final String FRIEND = "user";
    private static final String PICTURE = "picture";
    public static final String FROM_NOTIFICATION = "notification";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_notification)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody());

        Intent resultIntent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        if (remoteMessage.getData().size() > 0) {
            if (remoteMessage.getData().get(TYPE).equals(GAME)) {
                resultIntent = new Intent(this, GameActivity.class);
                resultIntent.putExtra(Game.ID, Integer.valueOf(remoteMessage.getData().get(Game.ID)));
                resultIntent.putExtra(Game.IMAGE_URL, remoteMessage.getData().get(PICTURE));
                resultIntent.putExtra(FROM_NOTIFICATION, true);
                stackBuilder.addParentStack(GameActivity.class);
            }
            else if (remoteMessage.getData().get(TYPE).equals(FRIEND)) {
                resultIntent = new Intent(this, ProfileActivity.class);
                resultIntent.putExtra(User.ID, Integer.valueOf(remoteMessage.getData().get(User.ID)));
                resultIntent.putExtra(ProfileActivity.IS_CURRENT_USER, false);
                stackBuilder.addParentStack(ProfileActivity.class);
            }
        }

        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }
}
