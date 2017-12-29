package com.snaptiongame.app.data.services.notifications

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.support.v4.app.NotificationCompat
import android.support.v4.app.TaskStackBuilder
import android.support.v4.content.ContextCompat

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.snaptiongame.app.R
import com.snaptiongame.app.data.auth.AuthManager
import com.snaptiongame.app.data.models.Game
import com.snaptiongame.app.data.models.User
import com.snaptiongame.app.data.utils.ImageUtils
import com.snaptiongame.app.presentation.view.game.GameActivity
import com.snaptiongame.app.presentation.view.MainActivity
import com.snaptiongame.app.presentation.view.profile.ProfileActivity

import java.util.Date
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build

/**
 * The NotificationService class handles getting notifications from the server
 * and displaying the right notification to the user.
 *
 * @author Tyler Wong
 * @version 1.0
 */
class NotificationService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        val data = remoteMessage?.data
        val type: String?

        // IF data has well-formatted data
        if (data?.containsKey(TYPE) == true) {
            type = data[TYPE]

            // IF the data is composed of a GAME related notification
            // AND GAME notifications are enabled
            if (type == GAME && AuthManager.isGameNotificationsEnabled()) {
                handleNotification(data, type)
            }
            else if (type == FRIEND && AuthManager.isFriendNotificationsEnabled()) {
                handleNotification(data, type)
            }// ELSE IF the data is composed of a FRIEND related notifications
            // AND FRIEND notifications are enabled
        }
    }

    @SuppressLint("NewApi")
    private fun handleNotification(data: Map<String, String>, type: String) {
        var title: String? = ""
        var message: String? = ""
        val imageUrl: String?
        val userImageUrl: String?
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.BLUE
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        // IF there is data
        if (data.isNotEmpty()) {
            // IF the data has a TITLE and MESSAGE
            if (data.containsKey(TITLE) && data.containsKey(MESSAGE)) {
                title = data[TITLE]
                message = data[MESSAGE]
            }

            // BUILD default notification
            val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
                    .setContentTitle(title)
                    .setContentText(message)
                    .setColor(ContextCompat.getColor(applicationContext, R.color.colorPrimary))

            // INIT target intent
            var resultIntent = Intent(this, MainActivity::class.java)
            // INIT stack builder
            val stackBuilder = TaskStackBuilder.create(this)

            // IF this is a GAME notification
            if (type == GAME) {
                // SET result intent to go to GameActivity
                resultIntent = Intent(this, GameActivity::class.java)

                // IF the data has a Game ID
                if (data.containsKey(Game.ID)) {
                    // PASS Game ID to GameActivity to load game
                    resultIntent.putExtra(Game.ID, Integer.valueOf(data[Game.ID]))
                }
                // IF the data has a picture
                if (data.containsKey(PICTURE)) {
                    // SET notification big picture
                    imageUrl = data[PICTURE]

                    // IF the picture is not null and not empty
                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        val style = NotificationCompat.BigPictureStyle()
                        style.setBigContentTitle(title)
                        style.setSummaryText(message)
                        style.bigPicture(ImageUtils.getBitmapFromURL(imageUrl))
                        builder.setStyle(style)
                    }
                    // PASS image url to GameActivity
                    resultIntent.putExtra(Game.IMAGE_URL, imageUrl)
                }
                // IF the data has an associated user picture
                if (data.containsKey(USER_PICTURE)) {
                    userImageUrl = data[USER_PICTURE]

                    // IF the user picture is not null and not empty
                    if (userImageUrl != null && !userImageUrl.isEmpty()) {
                        // SET notification large icon
                        builder.setLargeIcon(ImageUtils.getCircularBitmapFromUrl(data[USER_PICTURE] ?: ""))
                    }
                }
                // SPECIFY we are coming from a clicked notification
                resultIntent.putExtra(FROM_NOTIFICATION, true)
                // SET parent as GameActivity
                stackBuilder.addParentStack(GameActivity::class.java)
            }
            else if (type == FRIEND) {
                // SET result intent to go to ProfileActivity
                resultIntent = Intent(this, ProfileActivity::class.java)

                // IF the data has a User ID
                if (data.containsKey(User.ID)) {
                    resultIntent.putExtra(User.ID, Integer.valueOf(data[User.ID]))
                }
                // IF the data has a picture
                if (data.containsKey(PICTURE)) {
                    userImageUrl = data[PICTURE]

                    // IF the user picture is not null and not empty
                    if (userImageUrl != null && !userImageUrl.isEmpty()) {
                        // SET notification large icon
                        builder.setLargeIcon(ImageUtils.getCircularBitmapFromUrl(userImageUrl))
                    }
                }
                // SPECIFY that the profile we are viewing is not the current user's
                // (Impossible to get a friend notification from yourself)
                resultIntent.putExtra(ProfileActivity.IS_CURRENT_USER, false)
                // SET parent as ProfileActivity
                stackBuilder.addParentStack(ProfileActivity::class.java)
            }// ELSE IF this is a FRIEND notification

            // INIT unique notification ID
            val notificationIdentifier = (Date().time / THOUSAND % Integer.MAX_VALUE).toInt()
            // BUILD activity backstack
            stackBuilder.addNextIntent(resultIntent)
            val resultPendingIntent = stackBuilder.getPendingIntent(
                    notificationIdentifier, PendingIntent.FLAG_UPDATE_CURRENT)
            // SET notification intent
            builder.setContentIntent(resultPendingIntent)
            // BUILD and SHOW notification
            notificationManager.notify(notificationIdentifier, builder.build())
        }
    }

    companion object {
        private val THOUSAND = 1000L
        private val TYPE = "type"
        private val TITLE = "title"
        private val MESSAGE = "message"
        private val GAME = "game"
        private val FRIEND = "user"
        private val PICTURE = "picture"
        private val USER_PICTURE = "userPicture"
        const val CHANNEL_ID = "snaptionChannel"
        const val CHANNEL_NAME = "Snaption"
        const val FROM_NOTIFICATION = "notification"
    }
}
