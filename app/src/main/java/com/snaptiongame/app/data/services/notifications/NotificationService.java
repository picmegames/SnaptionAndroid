package com.snaptiongame.app.data.services.notifications;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.snaptiongame.app.R;
import com.snaptiongame.app.data.auth.AuthManager;
import com.snaptiongame.app.data.models.Game;
import com.snaptiongame.app.data.models.User;
import com.snaptiongame.app.data.utils.ImageUtils;
import com.snaptiongame.app.presentation.view.game.GameActivity;
import com.snaptiongame.app.presentation.view.main.MainActivity;
import com.snaptiongame.app.presentation.view.profile.ProfileActivity;

import java.util.Date;
import java.util.Map;

/**
 * The NotificationService class handles getting notifications from the server
 * and displaying the right notification to the user.
 *
 * @author Tyler Wong
 * @version 1.0
 */
public class NotificationService extends FirebaseMessagingService {

    private static final long THOUSAND = 1000L;
    private static final String TYPE = "type";
    private static final String TITLE = "title";
    private static final String MESSAGE = "message";
    private static final String GAME = "game";
    private static final String FRIEND = "user";
    private static final String PICTURE = "picture";
    private static final String USER_PICTURE = "userPicture";
    public static final String FROM_NOTIFICATION = "notification";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();
        String type;

        // IF data has well-formatted data
        if (data.containsKey(TYPE)) {
            type = data.get(TYPE);

            // IF the data is composed of a GAME related notification
            // AND GAME notifications are enabled
            if (type.equals(GAME) && AuthManager.isGameNotificationsEnabled()) {
                handleNotification(data, type);
            }
            // ELSE IF the data is composed of a FRIEND related notifications
            // AND FRIEND notifications are enabled
            else if(type.equals(FRIEND) && AuthManager.isFriendNotificationsEnabled()) {
                handleNotification(data, type);
            }
        }
    }

    private void handleNotification(Map<String, String> data, String type) {
        String title = "";
        String message = "";
        String imageUrl;
        String userImageUrl;

        // IF there is data
        if (data.size() > 0) {
            // IF the data has a TITLE and MESSAGE
            if (data.containsKey(TITLE) && data.containsKey(MESSAGE)) {
                title = data.get(TITLE);
                message = data.get(MESSAGE);
            }

            // BUILD default notification
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .setContentTitle(title)
                    .setContentText(message)
                    .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));

            // INIT target intent
            Intent resultIntent = new Intent(this, MainActivity.class);
            // INIT stack builder
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

            // IF this is a GAME notification
            if (type.equals(GAME)) {
                // SET result intent to go to GameActivity
                resultIntent = new Intent(this, GameActivity.class);

                // IF the data has a Game ID
                if (data.containsKey(Game.ID)) {
                    // PASS Game ID to GameActivity to load game
                    resultIntent.putExtra(Game.ID, Integer.valueOf(data.get(Game.ID)));
                }
                // IF the data has a picture
                if (data.containsKey(PICTURE)) {
                    // SET notification big picture
                    imageUrl = data.get(PICTURE);

                    // IF the picture is not null and not empty
                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle();
                        style.setBigContentTitle(title);
                        style.setSummaryText(message);
                        style.bigPicture(ImageUtils.INSTANCE.getBitmapFromURL(imageUrl));
                        builder.setStyle(style);
                    }
                    // PASS image url to GameActivity
                    resultIntent.putExtra(Game.IMAGE_URL, imageUrl);
                }
                // IF the data has an associated user picture
                if (data.containsKey(USER_PICTURE)) {
                    userImageUrl = data.get(USER_PICTURE);

                    // IF the user picture is not null and not empty
                    if (userImageUrl != null && !userImageUrl.isEmpty()) {
                        // SET notification large icon
                        builder.setLargeIcon(ImageUtils.INSTANCE.getCircularBitmapFromUrl(data.get(USER_PICTURE)));
                    }
                }
                // SPECIFY we are coming from a clicked notification
                resultIntent.putExtra(FROM_NOTIFICATION, true);
                // SET parent as GameActivity
                stackBuilder.addParentStack(GameActivity.class);
            }
            // ELSE IF this is a FRIEND notification
            else if (type.equals(FRIEND)) {
                // SET result intent to go to ProfileActivity
                resultIntent = new Intent(this, ProfileActivity.class);

                // IF the data has a User ID
                if (data.containsKey(User.ID)) {
                    resultIntent.putExtra(User.ID, Integer.valueOf(data.get(User.ID)));
                }
                // IF the data has a picture
                if (data.containsKey(PICTURE)) {
                    userImageUrl = data.get(PICTURE);

                    // IF the user picture is not null and not empty
                    if (userImageUrl != null && !userImageUrl.isEmpty()) {
                        // SET notification large icon
                        builder.setLargeIcon(ImageUtils.INSTANCE.getCircularBitmapFromUrl(userImageUrl));
                    }
                }
                // SPECIFY that the profile we are viewing is not the current user's
                // (Impossible to get a friend notification from yourself)
                resultIntent.putExtra(ProfileActivity.IS_CURRENT_USER, false);
                // SET parent as ProfileActivity
                stackBuilder.addParentStack(ProfileActivity.class);
            }

            // INIT unique notification ID
            int notificationIdentifier = (int) ((new Date().getTime() / THOUSAND) % Integer.MAX_VALUE);
            // BUILD activity backstack
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                    notificationIdentifier, PendingIntent.FLAG_UPDATE_CURRENT);
            // SET notification intent
            builder.setContentIntent(resultPendingIntent);
            // BUILD and SHOW notification
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
            notificationManager.notify(notificationIdentifier, builder.build());
        }
    }
}
