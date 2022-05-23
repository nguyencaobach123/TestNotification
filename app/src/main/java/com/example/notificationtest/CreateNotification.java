package com.example.notificationtest;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.session.MediaSession;
import android.os.Build;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.notificationtest.services.NotificationActionService;

public class CreateNotification {
    public static final String CHANNEL_ID = "CHANNEL_1";
    public static final String ACTION_NEXT = "NEXT";
    public static final String ACTION_PREV = "PREVIOUS";
    public static final String ACTION_PLAY = "PLAY";
    public static Notification notification;

    public static void createNotification(Context context,TrackFiles track, int playButton) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
            MediaSessionCompat mediaSessionCompat = new MediaSessionCompat(context, "tag");
            Bitmap icon = BitmapFactory.decodeResource(context.getResources(), track.getThumbnail());
            PendingIntent pendingIntentPrevious;
            PendingIntent pendingIntentPlay;
            PendingIntent pendingIntentNext;
            PendingIntent contentIntent;
            Intent notificationIntent = new Intent(context, MainActivity.class);
            contentIntent = PendingIntent.getActivity(context,
                    0, notificationIntent,
                    PendingIntent.FLAG_CANCEL_CURRENT);
            Intent intentPrevious = new Intent(context, NotificationActionService.class).setAction(ACTION_PREV);
            pendingIntentPrevious = PendingIntent.getBroadcast(context, 0, intentPrevious, PendingIntent.FLAG_UPDATE_CURRENT);
            Intent intentPlay = new Intent(context, NotificationActionService.class).setAction(ACTION_PLAY);
            pendingIntentPlay = PendingIntent.getBroadcast(context, 0, intentPlay, PendingIntent.FLAG_UPDATE_CURRENT);
            Intent intentNext = new Intent(context, NotificationActionService.class).setAction(ACTION_NEXT);
            pendingIntentNext = PendingIntent.getBroadcast(context, 0, intentNext, PendingIntent.FLAG_UPDATE_CURRENT);
            notification = new NotificationCompat.Builder(context, CHANNEL_ID).
                    setSmallIcon(R.drawable.ic_baseline_play_arrow_24).
                    setContentTitle(track.getTitle()).
                    setContentText(track.getArtist()).
                    setContentIntent(contentIntent).
                    setLargeIcon(icon).
                    setOnlyAlertOnce(true).
                    setShowWhen(false).
                    addAction(R.drawable.ic_baseline_skip_previous_24, "Previous", pendingIntentPrevious).
                    addAction(playButton, "Play", pendingIntentPlay).
                    addAction(R.drawable.ic_baseline_skip_next_24, "Next", pendingIntentNext).
                    setStyle(new androidx.media.app.NotificationCompat.MediaStyle().
                            setShowActionsInCompactView(0, 1, 2).
                            setMediaSession(mediaSessionCompat.getSessionToken())).
                    setPriority(NotificationCompat.PRIORITY_LOW).
                    build();
            notificationManagerCompat.notify(1, notification);
        }
    }
}
