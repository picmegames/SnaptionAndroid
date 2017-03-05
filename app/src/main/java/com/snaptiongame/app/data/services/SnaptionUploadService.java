package com.snaptiongame.app.data.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;

import com.snaptiongame.app.R;
import com.snaptiongame.app.SnaptionApplication;
import com.snaptiongame.app.data.models.Snaption;
import com.snaptiongame.app.presentation.view.main.MainActivity;

import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;

/**
 * @author Tyler Wong
 */
public class SnaptionUploadService extends Service {

    private NotificationManager notificationManager;
    private NotificationCompat.Builder builder;
    private Context context;

    private CompositeDisposable disposables;

    private static final int MAX_PROGRESS = 0;
    private static final int CURRENT_PROGRESS = 0;
    private static final int ID = 100;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        disposables = new CompositeDisposable();
        context = SnaptionApplication.getContext();
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            Bundle uploadBundle = intent.getExtras();
            int userId = uploadBundle.getInt(Snaption.ID);
            boolean isPublic = uploadBundle.getBoolean(Snaption.IS_PUBLIC);
            byte[] encodedBytes = uploadBundle.getByteArray(Snaption.PICTURE);
            ArrayList<Integer> friendIds = uploadBundle.getIntegerArrayList(Snaption.FRIENDS);
            String type = uploadBundle.getString(Snaption.IMG_TYPE);

            showUploadProgressNotification();
//            Disposable disposable = SnaptionProvider.addSnaption(new Snaption(userId, !isPublic, 1,
//                    Base64.encodeToString(encodedBytes, Base64.DEFAULT), type, friendIds))
//                    .subscribe(
//                            snaption -> {
//                            },
//                            Timber::e,
//                            this::uploadComplete
//                    );
//            disposables.add(disposable);
        }

        return START_REDELIVER_INTENT;
    }

    private void uploadComplete() {
        showUploadCompleteNotification();
        disposables.clear();
        stopSelf();
    }

    private void showUploadProgressNotification() {
        builder = new NotificationCompat.Builder(SnaptionApplication.getContext());
        builder.setContentTitle(context.getString(R.string.upload_title))
                .setContentText(context.getString(R.string.upload_message))
                .setSmallIcon(R.drawable.ic_notification)
                .setProgress(MAX_PROGRESS, CURRENT_PROGRESS, true)
                .setOngoing(true);
        notificationManager.notify(ID, builder.build());
    }

    private void showUploadCompleteNotification() {
        Intent resultIntent = new Intent(context, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder = new NotificationCompat.Builder(SnaptionApplication.getContext());
        builder.setContentTitle(context.getString(R.string.upload_complete_title))
                .setContentText(context.getString(R.string.upload_complete_message))
                .setSmallIcon(R.drawable.ic_notification)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent);
        notificationManager.notify(ID, builder.build());
    }
}
