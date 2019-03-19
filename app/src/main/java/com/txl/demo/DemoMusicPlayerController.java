package com.txl.demo;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.txl.page.MainActivity;
import com.txl.player.music.AbsPlayerController;
import com.txl.player.music.AbsNotificationFactory;
import com.txl.player.music.PlayerTag;
import com.txl.player.android.player.R;

import java.util.ArrayList;
import java.util.List;


/**
 * @author TXL
 * description :
 */
public class DemoMusicPlayerController extends AbsPlayerController {
    private static final int REQUEST_CODE = 1568;
    List<MusicData> musicData;
    int currentPlayIndex = 0;

    public static final String ACTION_PLAY_NEXT = "play_next";
    public static final String ACTION_PLAY_PRE = "play_pre";

    public DemoMusicPlayerController(Context context) {
        super(context);
    }

    @Override
    public AbsNotificationFactory createMediaNotificationManager(Context context) {
        return new DemoNotificationFactory(context);
    }

    @Override
    public void init(Intent initParams) {
        prepareMusicData();
        //原来的
        PlayerTag lastPlayerTag = getPlayTag();
        PlayerTag tag = new PlayerTag(musicData.get( currentPlayIndex ).getPlayUrl());
        //前后播放的不是同一个切换播放源
        if(!tag.equals(lastPlayerTag)){
            open(musicData.get( currentPlayIndex ).getPlayUrl(),tag);
            onReceiveControllerCommand(ACTION_PLAY_NEXT, musicData.get( currentPlayIndex ));
        }
    }

    private void prepareMusicData(){
        musicData = new ArrayList<>(  );
        //需要自己处理播放地址，这个地址仅限当天有效
        musicData.add( new MusicData(R.drawable.music_author_01,"生僻字","http://ts2.ijntv.cn/jnyypl/sd/live.m3u8") );
        musicData.add( new MusicData(R.drawable.music_author_02,"一曲相思","http://fs.w.kugou.com/201903171124/91a30e730f830675816c05bba1f6707a/G085/M07/0B/10/lQ0DAFujV42AK4xpACkHR2d9qTo587.mp3") );
    }

    @Override
    public void playNext() {
        currentPlayIndex ++;
        currentPlayIndex = currentPlayIndex % musicData.size();
        PlayerTag tag = new PlayerTag(musicData.get( currentPlayIndex ).getPlayUrl());
        open( musicData.get( currentPlayIndex ).getPlayUrl(),tag );
        onReceiveControllerCommand(ACTION_PLAY_NEXT, musicData.get( currentPlayIndex ));
        play();
    }

    @Override
    public void playPre() {
        if(currentPlayIndex == 0){
            currentPlayIndex = musicData.size()-1;
        }else {
            currentPlayIndex--;
        }
        PlayerTag tag = new PlayerTag(musicData.get( currentPlayIndex ).getPlayUrl());
        setPlayTag(tag);
        open(musicData.get( currentPlayIndex ).getPlayUrl());
        onReceiveControllerCommand(ACTION_PLAY_PRE, musicData.get( currentPlayIndex ));
        play();
    }

    @Override
    public void setPlayMode(int mode) {

    }

    @Override
    public void receiveCommand(String action, Object... o) {

    }

    /**
     * @author TXL
     * description :
     */
    public class DemoNotificationFactory extends AbsNotificationFactory {
        public DemoNotificationFactory(Context context) {
            super(context);
        }

        @Override
        public Notification createPlayNotification() {
            if (isAndroidOOrHigher()) {
                createChannel();
            }
            final NotificationCompat.Builder builder = new NotificationCompat.Builder( mContext, CHANNEL_ID);
            final RemoteViews normalRemoteViews = new RemoteViews( mContext.getPackageName(),R.layout.normal_notification);
            normalRemoteViews.setImageViewResource(R.id.image_icon,musicData.get(currentPlayIndex).playerUserIconResId);
            normalRemoteViews.setImageViewResource(R.id.ib_toggle,R.drawable.image_pause);
            Intent intent = new Intent();
            String packageName = mContext.getPackageName();
            intent.setAction(packageName+MusicBroadcastReceiver.ACTION_PAUSE);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext,REQUEST_CODE,intent,PendingIntent.FLAG_CANCEL_CURRENT);
            normalRemoteViews.setOnClickPendingIntent(R.id.ib_toggle, pendingIntent);
            intent = new Intent();
            intent.setAction(packageName+MusicBroadcastReceiver.ACTION_PLAY_PRE);
            normalRemoteViews.setOnClickPendingIntent(R.id.ib_play_pre, PendingIntent.getBroadcast(mContext,REQUEST_CODE,intent,PendingIntent.FLAG_CANCEL_CURRENT));
            intent = new Intent();
            intent.setAction(packageName+MusicBroadcastReceiver.ACTION_PLAY_NEXT);
            normalRemoteViews.setOnClickPendingIntent(R.id.ib_play_next, PendingIntent.getBroadcast(mContext,REQUEST_CODE,intent,PendingIntent.FLAG_CANCEL_CURRENT));
            normalRemoteViews.setTextViewText(R.id.tv_audio_title,musicData.get(currentPlayIndex).musicName);

            builder
                    .setDefaults(NotificationCompat.FLAG_ONLY_ALERT_ONCE)
                    .setVibrate(new long[]{0})
                    .setSound(null)
                    .setCustomContentView(normalRemoteViews)
                    .setSmallIcon(R.drawable.easy_player_icon)
                    .setShowWhen(false)
                    .setColor(Color.RED)//可以主动设置
                    .setContentIntent(createContentIntent())
                    // Show controls on lock screen even when user hides sensitive content.
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            return builder.build();
        }

        @Override
        public Notification createPauseNotification() {
            if (isAndroidOOrHigher()) {
                createChannel();
            }
            final NotificationCompat.Builder builder = new NotificationCompat.Builder( mContext, CHANNEL_ID);
            final RemoteViews normalRemoteViews = new RemoteViews( mContext.getPackageName(),R.layout.normal_notification);
            normalRemoteViews.setImageViewResource(R.id.ib_toggle,R.drawable.image_play);
            normalRemoteViews.setOnClickPendingIntent(R.id.ib_toggle, createPauseIntent());
            normalRemoteViews.setImageViewResource(R.id.image_icon,musicData.get(currentPlayIndex).playerUserIconResId);
            String packageName = mContext.getPackageName();
            Intent intent = new Intent();
            intent.setAction(packageName+MusicBroadcastReceiver.ACTION_PLAY_PRE);
            normalRemoteViews.setOnClickPendingIntent(R.id.ib_play_pre, PendingIntent.getBroadcast(mContext,REQUEST_CODE,intent,PendingIntent.FLAG_CANCEL_CURRENT));
            intent = new Intent();
            intent.setAction(packageName+MusicBroadcastReceiver.ACTION_PLAY_NEXT);
            normalRemoteViews.setOnClickPendingIntent(R.id.ib_play_next, PendingIntent.getBroadcast(mContext,REQUEST_CODE,intent,PendingIntent.FLAG_CANCEL_CURRENT));
            normalRemoteViews.setTextViewText(R.id.tv_audio_title,musicData.get(currentPlayIndex).musicName);

            builder
                    .setDefaults(NotificationCompat.FLAG_ONLY_ALERT_ONCE)
                    .setVibrate(new long[]{0})
                    .setSound(null)
                    .setCustomContentView(normalRemoteViews)
                    .setSmallIcon(R.drawable.easy_player_icon)
                    .setShowWhen(false)
                    .setColor(Color.RED)//可以主动设置
                    .setContentIntent(createContentIntent())
                    // Show controls on lock screen even when user hides sensitive content.
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            return builder.build();
        }

        private PendingIntent createContentIntent() {
            Intent intent = new Intent(mContext, MainActivity.class);
            return PendingIntent.getActivity(mContext,REQUEST_CODE,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        }


        private PendingIntent createPauseIntent(){
            Intent intent = new Intent();
            String packageName = mContext.getPackageName();
            intent.setAction(packageName+MusicBroadcastReceiver.ACTION_PLAY);
            return PendingIntent.getBroadcast(mContext,REQUEST_CODE,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        }

        @Override
        public Notification createSeekNotification(long pos) {
            return null;
        }

        @Override
        public Notification createOtherNotification(String action, Object... o) {
            return null;
        }
    }
}
