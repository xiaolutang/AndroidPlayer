/*
 * Copyright 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.txl.player.android.music;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import android.util.Log;
import android.widget.RemoteViews;

import com.txl.player.android.player.R;


/**
 * Keeps track of a notification and updates it automatically for a given MediaSession. This is
 * required so that the music service don't get killed during playback.
 */
public class MediaNotificationManager {
    public static int TYPE_VOD = 0;
    public static int TYPE_IMAGE_TEXT = 1;
    public static int TYPE_ALBUM = 2;
    public static int TYPE_LIVE = 3;

    public static final int NOTIFICATION_ID = 499;

    private static final String TAG = MediaNotificationManager.class.getSimpleName();
    private static final int REQUEST_CODE = 521;

    private final Service mService;
    private int mType;

    private final NotificationManager mNotificationManager;
    private final String CHANNEL_ID;


    public void setmType(int mType) {
        this.mType = mType;
    }

    public MediaNotificationManager(Service service, int type) {
        mService = service;
        mType = type;
        CHANNEL_ID = service.getPackageName();
        mNotificationManager =
                (NotificationManager) mService.getSystemService(Context.NOTIFICATION_SERVICE);



        // Cancel all notifications to handle the case where the Service was killed and
        // restarted by the system.
        mNotificationManager.cancelAll();
    }

    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
    }

    public NotificationManager getNotificationManager() {
        return mNotificationManager;
    }

    public Notification getNotification( @DrawableRes int logoRes, @DrawableRes int toggleRes, String musicName) {

        NotificationCompat.Builder builder =
                buildNotification(logoRes, toggleRes,musicName,null);
        return builder.build();
    }

    public Notification getNotification(@DrawableRes int logoRes, @DrawableRes int toggleRes, String musicName, Bitmap logoBitmap) {
        NotificationCompat.Builder builder =
                buildNotification(logoRes, toggleRes,musicName,logoBitmap);
        return builder.build();
    }


    /**
     * @param logoRes
     * @param toggleRes 播放控制按钮的图标
     * */
    private NotificationCompat.Builder buildNotification(@DrawableRes int logoRes, @DrawableRes int toggleRes, String musicName, Bitmap logoBitmap) {

        // Create the (mandatory) notification channel when running on Android Oreo.
        if (isAndroidOOrHigher()) {
            createChannel();
        }
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(mService, CHANNEL_ID);
        final RemoteViews normalRemoteViews = new RemoteViews(mService.getPackageName(),R.layout.normal_notification);
        normalRemoteViews.setImageViewResource(R.id.ib_toggle,toggleRes);
        normalRemoteViews.setOnClickPendingIntent(R.id.ib_toggle,createStopIntent());
        normalRemoteViews.setTextViewText(R.id.tv_audio_title,musicName);
        if(logoBitmap!= null){
            normalRemoteViews.setImageViewBitmap(R.id.image_icon,logoBitmap);
        }
        builder
                .setDefaults(NotificationCompat.FLAG_ONLY_ALERT_ONCE)
                .setVibrate(new long[]{0})
                .setSound(null)
                .setCustomContentView(normalRemoteViews)
                .setSmallIcon(logoRes)
                .setShowWhen(false)
                .setColor(Color.RED)//可以主动设置
                .setContentIntent(createContentIntent())
                // Show controls on lock screen even when user hides sensitive content.
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        return builder;
    }

    private PendingIntent createStopIntent() {
        return null;
    }

    private PendingIntent createContentIntent() {
        return null;
    }

    // Does nothing on versions of Android earlier than O.
    @RequiresApi(Build.VERSION_CODES.O)
    private void createChannel() {
        if (mNotificationManager.getNotificationChannel(CHANNEL_ID) == null) {
            // The user-visible name of the channel.
            CharSequence name = "MediaSession";
            // The user-visible description of the channel.
            String description = "MediaSession and MediaPlayer";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            // Configure the notification channel.
            mChannel.setDescription(description);
            mChannel.enableLights(false);
            // Sets the notification light color for notifications posted to this
            // channel, if the device supports this feature.
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(false);
            mChannel.setVibrationPattern(new long[]{0});
            mChannel.setSound(null, null);
            mNotificationManager.createNotificationChannel(mChannel);
            Log.d(TAG, "createChannel: New channel created");
        } else {
            Log.d(TAG, "createChannel: Existing channel reused");
        }
    }

    private boolean isAndroidOOrHigher() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }

}