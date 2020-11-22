package com.google.android.exoplayer2.demo;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.IBinder;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;

public class PlayerService extends Service {
    public static final String CHANNEL_ID = "player_channel";
    public static final int NOTIFICATION_ID = 2;
    private PlayerNotificationManager playerNotificationManager;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(playerNotificationManager == null) {
            playerNotificationManager = PlayerNotificationManager.createWithNotificationChannel(
                    this,
                    CHANNEL_ID,
                    R.string.channel_name,
                    R.string.channel_description,
                    NOTIFICATION_ID,
                    new PlayerNotificationManager.MediaDescriptionAdapter() {

                        @NonNull
                        @Override
                        public CharSequence getCurrentContentTitle(@NonNull Player player) {
                            return "title";
                        }

                        @Nullable
                        @Override
                        public PendingIntent createCurrentContentIntent(@NonNull Player player) {
                            return null;
                        }

                        @Override
                        public CharSequence getCurrentContentText(@NonNull Player player) {
                            return "text";
                        }

                        @Nullable
                        @Override
                        public Bitmap getCurrentLargeIcon(
                                @NonNull Player player,
                                @NonNull PlayerNotificationManager.BitmapCallback callback) {
                            return null;
                        }
                    },
                    new PlayerNotificationManager.NotificationListener() {

                        @Override
                        public void onNotificationPosted(
                                int notificationId,
                                @NonNull Notification notification,
                                boolean ongoing) {

                            if (!ongoing) {
                                stopForeground(false);
                            } else {
                                startForeground(notificationId, notification);
                            }
                        }

                        @Override
                        public void onNotificationCancelled(int notificationId,
                                                            boolean dismissedByUser) {

                            if (dismissedByUser) {
                                stopForeground(true);
                            }
                            stopSelf();
                        }
                    }
            );
            playerNotificationManager.setPlayer(PlayerActivity.player);
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (playerNotificationManager != null) {
            playerNotificationManager.setPlayer(null);
            playerNotificationManager = null;
        }
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
